package io.domisum.lib.httpbutler;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.httpbutler.exceptions.HttpException;
import io.domisum.lib.httpbutler.request.HttpRequest;

import java.io.IOException;

public abstract class HttpButlerEndpoint
{
	
	// CONSTANTS
	protected static final double DOES_NOT_ACCEPT = 0;
	
	
	// ENDPOINT
	protected abstract double getAcceptance(HttpRequest request);
	
	@API
	protected abstract HttpResponse handleRequest(HttpRequest request)
		throws IOException, HttpException;
	
}
