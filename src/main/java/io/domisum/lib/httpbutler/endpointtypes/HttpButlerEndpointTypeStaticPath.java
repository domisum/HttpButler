package io.domisum.lib.httpbutler.endpointtypes;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.httpbutler.HttpButlerEndpoint;
import io.domisum.lib.httpbutler.request.HttpMethod;
import io.domisum.lib.httpbutler.request.HttpRequest;

@API
public abstract class HttpButlerEndpointTypeStaticPath
		extends HttpButlerEndpoint
{
	
	// CONSTANT METHODS
	@API
	protected abstract HttpMethod METHOD();
	
	@API
	protected abstract String PATH();
	
	
	// ACCEPTANCE
	@Override
	protected double getAcceptance(HttpRequest request)
	{
		if(METHOD() != request.getMethod())
			return DOES_NOT_ACCEPT;
		
		String endpointPath = HttpRequest.cleanUpPath(PATH());
		if(!endpointPath.equalsIgnoreCase(request.getPath()))
			return DOES_NOT_ACCEPT;
		
		final double baseAcceptance = 0.9;
		return endpointPath.length()+baseAcceptance; // add some to trump "starts with" endpoint type
	}
	
}
