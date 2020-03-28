package io.domisum.lib.httpbutler.responses;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.httpbutler.HttpResponse;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.StatusCodes;
import lombok.NoArgsConstructor;

@API
@NoArgsConstructor
public class HttpResponseNoBody
		extends HttpResponse
{
	
	// SEND
	@Override
	protected void send(HttpServerExchange httpServerExchange)
	{
		httpServerExchange.setStatusCode(StatusCodes.NO_CONTENT);
	}
	
}
