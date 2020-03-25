package io.domisum.lib.httpbutler.request;

import io.domisum.lib.auxiliumlib.exceptions.IncompleteCodeError;

public enum HttpMethod
{
	
	GET,
	HEAD,
	POST,
	PUT,
	DELETE,
	TRACE,
	OPTIONS,
	CONNECT,
	PATCH;
	
	
	public static HttpMethod fromName(String name)
	{
		for(var httpMethod : values())
			if(httpMethod.name().equalsIgnoreCase(name))
				return httpMethod;
		
		throw new IncompleteCodeError("Unknown http method: "+name);
	}
	
}
