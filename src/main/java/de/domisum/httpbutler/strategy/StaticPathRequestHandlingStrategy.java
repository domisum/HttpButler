package de.domisum.httpbutler.strategy;

import de.domisum.httpbutler.HttpRequestHandler;
import de.domisum.httpbutler.request.HttpMethod;
import de.domisum.httpbutler.request.HttpRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StaticPathRequestHandlingStrategy implements RequestHandlingStrategy
{

	// ATTRIBUTES
	private final HttpMethod httpMethod;
	private final String httpPath;

	private final HttpRequestHandler requestHandler;


	// HANDLING STRATEGY
	@Override public boolean doesApplyTo(HttpRequest request)
	{
		if(request.getMethod() != httpMethod)
			return false;

		if(!request.getPath().equalsIgnoreCase(httpPath))
			return false;

		return true;
	}

	@Override public HttpRequestHandler getHandler()
	{
		return requestHandler;
	}

}
