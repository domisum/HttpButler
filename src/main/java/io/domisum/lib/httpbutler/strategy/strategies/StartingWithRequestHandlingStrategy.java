package io.domisum.lib.httpbutler.strategy.strategies;

import io.domisum.lib.httpbutler.HttpRequestHandler;
import io.domisum.lib.httpbutler.request.HttpMethod;
import io.domisum.lib.httpbutler.request.HttpRequest;
import io.domisum.lib.httpbutler.strategy.RequestHandlingStrategy;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StartingWithRequestHandlingStrategy implements RequestHandlingStrategy
{

	// ATTRIBUTES
	private final HttpMethod httpMethod;
	private final String httpPath;

	private final HttpRequestHandler requestHandler;


	// HANDLING STRATEGY
	@Override
	public boolean doesApplyTo(HttpRequest request)
	{
		if(request.getMethod() != httpMethod)
			return false;

		return request.getPath().toLowerCase().startsWith(httpPath.toLowerCase());
	}

	@Override
	public HttpRequestHandler getHandler()
	{
		return requestHandler;
	}

}