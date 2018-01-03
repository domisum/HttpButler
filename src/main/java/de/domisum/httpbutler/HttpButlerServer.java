package de.domisum.httpbutler;

import de.domisum.httpbutler.exceptions.HttpException;
import de.domisum.httpbutler.exceptions.MethodNotAllowedHttpException;
import de.domisum.lib.auxilium.util.PHR;
import de.domisum.lib.auxilium.util.java.annotations.API;
import io.undertow.Undertow;
import io.undertow.Undertow.Builder;
import io.undertow.server.HttpServerExchange;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@API
public class HttpButlerServer
{

	private final Logger logger = LoggerFactory.getLogger(getClass());


	// SETTINGS
	private final int port;
	private final Map<RequestHandlerKey, HttpRequestHandler> handlersByKey = new HashMap<>();

	// SERVER
	private Undertow server;


	// INIT
	@API public HttpButlerServer(int port)
	{
		this.port = port;
	}


	@API public void start()
	{
		if(server != null)
			return;
		logger.info("Starting {}...", getClass().getSimpleName());

		Builder serverBuilder = Undertow.builder();
		serverBuilder.addHttpListener(port, "localhost", this::handleRequest);
		server = serverBuilder.build();

		server.start();
	}

	@API public void stop()
	{
		logger.info("Stopping {}...", getClass().getSimpleName());
		server.stop();
	}


	// REQUEST HANDLERS
	@API public synchronized void registerRequestHandler(HttpMethod method, String path, HttpRequestHandler handler)
	{
		handlersByKey.put(new RequestHandlerKey(method, path), handler);
	}


	// REQUEST
	private synchronized void handleRequest(HttpServerExchange exchange)
	{
		// method
		HttpMethod method = HttpMethod.fromName(exchange.getRequestMethod().toString());

		// path
		String requestPath = exchange.getRequestPath();

		// params
		Map<String, List<String>> queryParams = new HashMap<>();
		for(Entry<String, Deque<String>> entry : exchange.getQueryParameters().entrySet())
			queryParams.put(entry.getKey(), Collections.unmodifiableList(new ArrayList<>(entry.getValue())));


		HttpRequest request = new HttpRequest(method, requestPath, queryParams);
		HttpResponseSender httpResponseSender = new HttpResponseSender(exchange);
		handleRequest(request, httpResponseSender);
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
	}

	private void handleOrThrowHttpException(HttpRequest request, HttpResponseSender responseSender) throws HttpException
	{
		RequestHandlerKey key = new RequestHandlerKey(request.getMethod(), request.getPath());
		HttpRequestHandler handler = handlersByKey.get(key);
		if(handler == null)
		{
			logger.warn("Received request {}, no request handler speicified for that request method and type", request);
			throw new MethodNotAllowedHttpException(PHR.r("Server unable to process method {} on path '{}'",
					request.getMethod(),
					request.getPath()));
		}

		logger.debug("Processing request {} in handler {}...", request, handler);
		handler.handleRequest(request, responseSender);
	}


	@EqualsAndHashCode
	private static class RequestHandlerKey
	{

		private final HttpMethod method;
		private final String path;


		public RequestHandlerKey(HttpMethod method, String path)
		{
			this.method = method;
			this.path = path.toLowerCase();
		}

	}

}
