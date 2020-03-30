package io.domisum.lib.httpbutler;

import com.google.common.collect.Iterables;
import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.StringUtil;
import io.domisum.lib.httpbutler.exceptions.HttpException;
import io.domisum.lib.httpbutler.exceptions.HttpInternalServerError;
import io.domisum.lib.httpbutler.exceptions.HttpNotFound;
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
			throw new IllegalStateException("Can't change settings while server is running");
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
		try(var request = buildHttpRequest(exchange))
		{
			var httpResponse = handleRequestCatchInternalErrors(request);
			httpResponse.send(exchange);
		}
		catch(HttpException e)
		{
			exchange.setStatusCode(e.ERROR_CODE_INT());
			exchange.getResponseSender().send(e.getResponseMessage());
		}
	}
	
	private HttpResponse handleRequestCatchInternalErrors(HttpRequest request)
			throws HttpException
	{
		try
		{
			return handleRequest(request);
		}
		catch(RuntimeException e)
		{
			logger.error("An unexpected error occured while processing the request", e);
			throw new HttpInternalServerError("An unexpected error occured while processing the request");
		}
	}
	
	private HttpResponse handleRequest(HttpRequest request)
			throws HttpException
	{
		var endpoint = selectEndpoint(request);
		logger.debug("Processing request {} in endpoint {}", request, endpoint);
		var httpResponse = endpoint.handleRequest(request);
		if(httpResponse == null)
		{
			logger.error("Endpoint {} returned null as response", endpoint);
			throw new HttpInternalServerError("The endpoint did not return a response");
		}
		
		return httpResponse;
	}
	
	
	// UTIL
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
	
	private HttpButlerEndpoint selectEndpoint(HttpRequest request)
			throws HttpNotFound, HttpInternalServerError
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
			throw new HttpNotFound("No endpoint registered for handling this request");
		
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
			throw new HttpInternalServerError("Multiple endpoints tied for handling this request: "+tieDisplayString);
		}
		
		// one endpoint found
		return Iterables.getOnlyElement(endpointsWithMaxAcceptance);
	}
	
}
