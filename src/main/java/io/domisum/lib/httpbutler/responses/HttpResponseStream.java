package io.domisum.lib.httpbutler.responses;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.httpbutler.HttpResponse;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;

@API
@RequiredArgsConstructor
public class HttpResponseStream
	extends HttpResponse
{
	
	// ATTRIBUTES
	private final String contentType;
	private final InputStream stream;
	
	
	// INIT
	public HttpResponseStream(InputStream stream)
	{
		this("application/octet-stream", stream);
	}
	
	
	// SEND
	@Override
	protected void sendSpecific(HttpServerExchange httpServerExchange)
		throws IOException
	{
		httpServerExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, contentType);
		stream.transferTo(httpServerExchange.getOutputStream());
	}
	
}
