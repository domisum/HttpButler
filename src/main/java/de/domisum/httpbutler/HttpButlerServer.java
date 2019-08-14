package de.domisum.httpbutler;

import de.domisum.httpbutler.exceptions.HttpException;
import de.domisum.httpbutler.exceptions.InternalServerErrorHttpException;
import de.domisum.httpbutler.exceptions.MethodNotAllowedHttpException;
import de.domisum.httpbutler.preprocessor.HttpRequestPreprocessor;
import de.domisum.httpbutler.request.HttpMethod;
import de.domisum.httpbutler.request.HttpRequest;
import de.domisum.httpbutler.strategy.RequestHandlingStrategy;
import de.domisum.httpbutler.strategy.strategies.ArgsInPathRequestHandlingStrategy;
import de.domisum.httpbutler.strategy.strategies.StartingWithRequestHandlingStrategy;
import de.domisum.httpbutler.strategy.strategies.StaticPathRequestHandlingStrategy;
import de.domisum.lib.auxilium.contracts.strategy.StrategySelector;
import de.domisum.lib.auxilium.util.PHR;
import de.domisum.lib.auxilium.util.java.annotations.API;
import io.undertow.Undertow;
import io.undertow.Undertow.Builder;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.util.HeaderValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

@API
public class HttpButlerServer
{

	private final Logger logger = LoggerFactory.getLogger(getClass());


	// SETTINGS
	private final String host;
	private final int port;

	private final List<RequestHandlingStrategy> requestHandlingStrategies = new ArrayList<>();
	private final RequestHandlingStrategy fallbackHandlingStrategy = null; // TODO

	private final List<HttpRequestPreprocessor> requestPreprocessors = new ArrayList<>();

	// SERVER
	private Undertow server;


	// INIT
	@API
	public HttpButlerServer(String host, int port)
	{
		this.host = host;
		this.port = port;
	}


	@API
	public synchronized void start()
	{
		if(server != null)
			return;
		logger.info("Starting {} on {}:{}...", getClass().getSimpleName(), host, port);

		Builder serverBuilder = Undertow.builder();
		serverBuilder.addHttpListener(port, host, new BlockingHandler(new HttpButlerServerHttpHandler()));
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
	}


	// HANDLERS REGISTRATION
	@API
	public synchronized void registerStaticPathRequestHandler(HttpMethod method, String path, HttpRequestHandler handler)
	{
		registerRequestHandlingStrategy(new StaticPathRequestHandlingStrategy(method, path, handler));
	}

	@API
	public synchronized void registerArgsInPathRequestHandler(HttpMethod method, String path, HttpRequestHandler handler)
	{
		registerRequestHandlingStrategy(new ArgsInPathRequestHandlingStrategy(method, path, handler));
	}

	@API
	public synchronized void registerStartingWithRequestHandler(HttpMethod method, String path, HttpRequestHandler handler)
	{
		registerRequestHandlingStrategy(new StartingWithRequestHandlingStrategy(method, path, handler));
	}


	@API
	public synchronized void registerRequestHandlingStrategy(RequestHandlingStrategy requestHandlingStrategy)
	{
		requestHandlingStrategies.add(requestHandlingStrategy);
	}

	@API
	public synchronized void registerRequestPreprocessor(HttpRequestPreprocessor requestPreprocessor)
	{
		requestPreprocessors.add(requestPreprocessor);
	}


	// REQUEST
	private HttpRequest buildHttpRequest(HttpServerExchange exchange)
	{
		// method
		HttpMethod method = HttpMethod.fromName(exchange.getRequestMethod().toString());

		// path
		String requestPath = exchange.getRequestPath();

		// body
		exchange.startBlocking();
		InputStream body = exchange.getInputStream();

		// headers
		Map<String, List<String>> headers = new HashMap<>();
		for(HeaderValues h : exchange.getRequestHeaders())
		{
			String headerName = h.getHeaderName().toString().toLowerCase();
			List<String> values = Collections.unmodifiableList(new ArrayList<>(h));

			headers.put(headerName, values);
		}
		headers = Collections.unmodifiableMap(headers);

		// params
		Map<String, List<String>> queryParams = new HashMap<>();
		for(Entry<String, Deque<String>> entry : exchange.getQueryParameters().entrySet())
			queryParams.put(entry.getKey(), Collections.unmodifiableList(new ArrayList<>(entry.getValue())));
		queryParams = Collections.unmodifiableMap(queryParams);


		return new HttpRequest(method, requestPath, headers, queryParams, body);
	}

	private void handleRequest(HttpRequest request, HttpResponseSender responseSender)
	{
		try
		{
			handleOrThrowHttpException(request, responseSender);
		}
		catch(HttpException e)
		{
			e.sendError(responseSender);
		}
		catch(Throwable e)
		{
			new InternalServerErrorHttpException("an error occured while processing the request", e).sendError(responseSender);

			if(e instanceof Error)
				throw e;
			else
				logger.error("an error occured while processing the request", e);
		}
	}

	private void handleOrThrowHttpException(HttpRequest rawRequest, HttpResponseSender responseSender) throws HttpException
	{
		HttpRequest request = preprocessRequest(rawRequest);

		HttpRequestHandler requestHandler = selectRequestHandler(request);
		logger.debug("Processing request {} in handler {}...", request, requestHandler);
		requestHandler.handleRequest(request, responseSender);
	}

	private HttpRequest preprocessRequest(HttpRequest rawRequest) throws HttpException
	{
		HttpRequest r = rawRequest;
		for(HttpRequestPreprocessor rpp : requestPreprocessors)
			r = rpp.preprocess(r);

		return r;
	}

	private HttpRequestHandler selectRequestHandler(HttpRequest request) throws MethodNotAllowedHttpException
	{
		Optional<RequestHandlingStrategy> handlingStrategy = new StrategySelector<>(requestHandlingStrategies,
				fallbackHandlingStrategy
		).selectFirstApplicable(request);

		if(!handlingStrategy.isPresent())
			throw new MethodNotAllowedHttpException(PHR.r("Server unable to process method {} on path '{}'",
					request.getMethod(),
					request.getPath()
			));

		return handlingStrategy.get().getHandler();
	}


	// UNDERTOW HANDLER
	private class HttpButlerServerHttpHandler implements HttpHandler
	{

		@Override
		public void handleRequest(HttpServerExchange exchange) throws IOException
		{
			try(HttpRequest request = buildHttpRequest(exchange))
			{
				HttpResponseSender responseSender = new HttpResponseSender(exchange);
				HttpButlerServer.this.handleRequest(request, responseSender);
			}
		}

	}

}
