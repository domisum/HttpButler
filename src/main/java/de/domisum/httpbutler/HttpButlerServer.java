package de.domisum.httpbutler;

import de.domisum.httpbutler.exceptions.HttpException;
import de.domisum.httpbutler.exceptions.MethodNotAllowedHttpException;
import de.domisum.httpbutler.request.HttpMethod;
import de.domisum.httpbutler.request.HttpRequest;
import de.domisum.httpbutler.strategy.ArgsInPathRequestHandlingStrategy;
import de.domisum.httpbutler.strategy.RequestHandlingStrategy;
import de.domisum.httpbutler.strategy.StaticPathRequestHandlingStrategy;
import de.domisum.lib.auxilium.contracts.strategy.StrategySelector;
import de.domisum.lib.auxilium.util.PHR;
import de.domisum.lib.auxilium.util.java.annotations.API;
import io.undertow.Undertow;
import io.undertow.Undertow.Builder;
import io.undertow.server.HttpServerExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private final int port;

	private final List<RequestHandlingStrategy> requestHandlingStrategies = new ArrayList<>();
	private final RequestHandlingStrategy defaultHandlingStrategy = null;

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
	@API public synchronized void registerStaticPathRequestHandler(HttpMethod method, String path, HttpRequestHandler handler)
	{
		requestHandlingStrategies.add(new StaticPathRequestHandlingStrategy(method, path, handler));
	}

	@API public synchronized void registerArgsInPathRequestHandler(HttpMethod method, String path, HttpRequestHandler handler)
	{
		requestHandlingStrategies.add(new ArgsInPathRequestHandlingStrategy(method, path, handler));
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
		Optional<HttpRequestHandler> handlerOptional = selectRequestHandler(request);
		if(!handlerOptional.isPresent())
		{
			logger.warn("Received request {}, no request handler speicified for that request method and type", request);
			throw new MethodNotAllowedHttpException(PHR.r("Server unable to process method {} on path '{}'",
					request.getMethod(),
					request.getPath()));
		}

		logger.debug("Processing request {} in handler {}...", request, handlerOptional);
		handlerOptional.get().handleRequest(request, responseSender);
	}

	private Optional<HttpRequestHandler> selectRequestHandler(HttpRequest httpRequest)
	{
		StrategySelector<HttpRequest, RequestHandlingStrategy> selector = new StrategySelector<>(defaultHandlingStrategy,
				requestHandlingStrategies);

		Optional<RequestHandlingStrategy> requestHandlingStrategyOptional = selector.selectFor(httpRequest);
		return requestHandlingStrategyOptional.isPresent() ?
				Optional.ofNullable(requestHandlingStrategyOptional.get().getHandler()) :
				Optional.empty();
	}

}
