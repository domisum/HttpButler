package io.domisum.lib.httpbutler.endpointtypes;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.httpbutler.HttpEndpoint;
import io.domisum.lib.httpbutler.request.HttpMethod;
import io.domisum.lib.httpbutler.request.HttpRequest;

@API
public abstract class HttpEndpointTypePathStartsWith
		extends HttpEndpoint
{
	
	// ACCEPTANCE
	@Override
	protected double getAcceptance(HttpRequest request)
	{
		if(METHOD() != request.getMethod())
			return DOES_NOT_ACCEPT;
		
		String endpointPathStart = HttpRequest.cleanUpPath(PATH_START());
		if(!request.getPath().toLowerCase().startsWith(endpointPathStart.toLowerCase()))
			return DOES_NOT_ACCEPT;
		
		return endpointPathStart.length();
	}
	
	protected abstract HttpMethod METHOD();
	
	protected abstract String PATH_START();
	
}
