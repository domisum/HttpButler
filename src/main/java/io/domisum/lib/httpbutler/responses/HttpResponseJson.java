package io.domisum.lib.httpbutler.responses;

import io.domisum.lib.auxiliumlib.annotations.API;

@API
public class HttpResponseJson
		extends HttpResponseString
{
	
	// INIT
	@API
	public HttpResponseJson(String json)
	{
		super("application/json", json);
	}
	
}
