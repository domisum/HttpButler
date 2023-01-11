package io.domisum.lib.httpbutler.responses;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import io.undertow.util.StatusCodes;
import lombok.RequiredArgsConstructor;

@API
@RequiredArgsConstructor
public class HttpResponseRedirect
	extends HttpResponseNoBody
{
	
	private final String url;
	
	
	// INIT
	@Override
	protected void sendSpecific(HttpServerExchange httpServerExchange)
	{
		httpServerExchange.setStatusCode(StatusCodes.FOUND);
		httpServerExchange.getResponseHeaders().add(new HttpString("Location"), url);
	}
	
}
