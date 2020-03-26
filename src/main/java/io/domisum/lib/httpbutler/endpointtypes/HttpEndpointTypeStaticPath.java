package io.domisum.lib.httpbutler.endpointtypes;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.httpbutler.HttpEndpoint;
import io.domisum.lib.httpbutler.request.HttpMethod;
import io.domisum.lib.httpbutler.request.HttpRequest;

@API
public abstract class HttpEndpointTypeStaticPath
		extends HttpEndpoint
{
	
	// ACCEPTANCE
	@Override
	protected double getAcceptance(HttpRequest request)
	{
		if(METHOD() != request.getMethod())
			return DOES_NOT_ACCEPT;
		
		String endpointPath = HttpRequest.cleanUpPath(PATH());
		if(!endpointPath.equalsIgnoreCase(request.getPath()))
			return DOES_NOT_ACCEPT;
		
		return endpointPath.length()+(1d/2); // add some to trump "starts with" endpoint type
	}
	
	@API
	protected abstract HttpMethod METHOD();
	
	@API
	protected abstract String PATH();
	
}
