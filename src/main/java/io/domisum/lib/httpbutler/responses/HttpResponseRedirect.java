package io.domisum.lib.httpbutler.responses;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.httpbutler.HttpResponse;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import io.undertow.util.StatusCodes;
import lombok.RequiredArgsConstructor;

@API
@RequiredArgsConstructor
public class HttpResponseRedirect
	extends HttpResponse
{
	
	private final String url;
	
	
	@Override
	protected void sendSpecific(HttpServerExchange httpServerExchange)
	{
		httpServerExchange.setStatusCode(StatusCodes.FOUND);
		httpServerExchange.getResponseHeaders().add(new HttpString("Location"), url);
	}
	
}
