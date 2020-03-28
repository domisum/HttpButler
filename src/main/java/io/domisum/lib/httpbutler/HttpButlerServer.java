package io.domisum.lib.httpbutler;

import com.google.common.collect.Iterables;
import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.StringUtil;
import io.domisum.lib.httpbutler.exceptions.HttpException;
import io.domisum.lib.httpbutler.exceptions.InternalServerErrorHttpException;
import io.domisum.lib.httpbutler.exceptions.NotFoundHttpException;
import io.domisum.lib.httpbutler.request.HttpMethod;
import io.domisum.lib.httpbutler.request.HttpRequest;
import io.undertow.Undertow;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.BlockingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

@API
public class HttpButlerServer
{
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	// SETTINGS
	private final String host;
	private final int port;
	
	private int numberOfIoThreads = Runtime.getRuntime().availableProcessors();
	private int numberOfWorkerThreads = Runtime.getRuntime().availableProcessors();
	
	// ENDPOINTS
	private final Set<HttpButlerEndpoint> endpoints = new HashSet<>();
	
	// STATUS
	private Undertow server;
	
	
	// INIT
	@API
	public HttpButlerServer(String host, int port)
	{
		this.host = host;
		this.port = port;
	}
	
	@API
	public HttpButlerServer(String host, int port, Collection<HttpButlerEndpoint> endpoints)
	{
		this(host, port);
		registerEndpoints(endpoints);
	}
	
	
	// CONTROL
	@API
	public synchronized void start()
	{
		if(server != null)
			return;
		logger.info("Starting {} on {}:{}...", getClass().getSimpleName(), host, port);
		
		var serverBuilder = Undertow.builder();
		serverBuilder.addHttpListener(port, host, new BlockingHandler(this::handleExchange));
		serverBuilder.setIoThreads(numberOfIoThreads);
		serverBuilder.setWorkerThreads(numberOfWorkerThreads);
		server = serverBuilder.build();
		
		server.start();
	}
	
	@API
	public synchronized void stop()
	{
		if(server == null)
			return;
		
		logger.info("Stopping {}...", getClass().getSimpleName());
		server.stop();
		server = null;
	}
	
	
	// SETTINGS
	private void validateCanChangeSettings()
	{
		if(server != null)
			throw new IllegalStateException("can't change settings while server is running");
	}
	
	@API
	public synchronized void setNumberOfIoThreads(int numberOfIoThreads)
	{
		validateCanChangeSettings();
		this.numberOfIoThreads = numberOfIoThreads;
	}
	
	@API
	public synchronized void setNumberOfWorkerThreads(int numberOfWorkerThreads)
	{
		validateCanChangeSettings();
		this.numberOfWorkerThreads = numberOfWorkerThreads;
	}
	
	
	// ENDPOINTS
	@API
	public synchronized void registerEndpoint(HttpButlerEndpoint endpoint)
	{
		validateCanChangeSettings();
		endpoints.add(endpoint);
	}
	
	@API
	public synchronized void registerEndpoints(Collection<HttpButlerEndpoint> endpoints)
	{
		validateCanChangeSettings();
		this.endpoints.addAll(endpoints);
	}
	
	
	// EXCHANGE
	private void handleExchange(HttpServerExchange exchange)
			throws IOException
	{
		var responseSender = new HttpResponseSender(exchange);
		try(var request = buildHttpRequest(exchange))
		{
			handleRequestCaught(request, responseSender);
		}
		catch(HttpException e)
		{
			if(responseSender.isSent())
			{
				logger.error("Can't throw http exception after already sending response");
				return;
			}
			
			exchange.setStatusCode(e.ERROR_CODE_INT());
			exchange.getResponseSender().send(e.getResponseMessage());
		}
	}
	
	private HttpRequest buildHttpRequest(HttpServerExchange exchange)
	{
		var method = HttpMethod.fromName(exchange.getRequestMethod().toString());
		String path = exchange.getRequestPath();
		var body = exchange.getInputStream();
		
		var queryParameters = new HashMap<String,List<String>>();
		for(var entry : exchange.getQueryParameters().entrySet())
			queryParameters.put(entry.getKey(), List.copyOf(entry.getValue()));
		
		var headers = new HashMap<String,List<String>>();
		for(var headerValues : exchange.getRequestHeaders())
			headers.put(headerValues.getHeaderName().toString(), List.copyOf(headerValues));
		
		return new HttpRequest(method, path, queryParameters, headers, body);
	}
	
	private void handleRequestCaught(HttpRequest request, HttpResponseSender responseSender)
			throws HttpException
	{
		try
		{
			handleRequest(request, responseSender);
		}
		catch(RuntimeException e)
		{
			logger.error("an error occured while processing the request", e);
			throw new InternalServerErrorHttpException("an unexpected error occured while processing the request");
		}
	}
	
	private void handleRequest(HttpRequest request, HttpResponseSender responseSender)
			throws HttpException
	{
		var requestHandler = selectEndpoint(request);
		
		logger.debug("Processing request {} in handler {}", request, requestHandler.getClass().getSimpleName());
		requestHandler.handleRequest(request, responseSender);
		
		if(!responseSender.isSent())
			logger.error("Request handler {} didn't send any response", requestHandler);
	}
	
	private HttpButlerEndpoint selectEndpoint(HttpRequest request)
			throws NotFoundHttpException, InternalServerErrorHttpException
	{
		var endpointAcceptances = new HashMap<HttpButlerEndpoint,Double>();
		for(var endpoint : endpoints)
		{
			double acceptance = endpoint.getAcceptance(request);
			if(acceptance > HttpButlerEndpoint.DOES_NOT_ACCEPT)
				endpointAcceptances.put(endpoint, acceptance);
		}
		
		// no endpoint accepts this request
		if(endpointAcceptances.isEmpty())
			throw new NotFoundHttpException("No endpoint registered for handling this request");
		
		// endpoints tied
		double maxAcceptance = endpointAcceptances.values().stream()
				.max(Double::compareTo)
				.orElseThrow();
		var endpointsWithMaxAcceptance = endpointAcceptances.entrySet().stream()
				.filter(e->e.getValue() == maxAcceptance)
				.map(Entry::getKey)
				.collect(Collectors.toSet());
		if(endpointsWithMaxAcceptance.size() > 1)
		{
			var tiedEndpointNames = endpointsWithMaxAcceptance.stream()
					.map(e->e.getClass().getSimpleName())
					.collect(Collectors.toSet());
			String tieDisplayString = StringUtil.collectionToString(tiedEndpointNames, ",")+" (acceptance: "+maxAcceptance+")";
			
			logger.error("Multiple endpoints tied for handling request: {}; request:\n{}", tieDisplayString, request);
			throw new InternalServerErrorHttpException("Multiple endpoints tied for handling this request: "+tieDisplayString);
		}
		
		// one endpoint found
		return Iterables.getOnlyElement(endpointsWithMaxAcceptance);
	}
	
}
