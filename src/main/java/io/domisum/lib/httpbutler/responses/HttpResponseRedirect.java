package io.domisum.lib.httpbutler.responses;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.StatusCodes;

@API
public class HttpResponseRedirect
	extends HttpResponseString
{
	
	// INIT
	@API
	public HttpResponseRedirect(String url)
	{
		super("text/plain", "Location: "+url);
	}
	
	@Override
	protected void sendSpecific(HttpServerExchange httpServerExchange)
	{
		httpServerExchange.setStatusCode(StatusCodes.FOUND);
		super.sendSpecific(httpServerExchange);
	}
	
}
