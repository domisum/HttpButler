package io.domisum.lib.httpbutler.responses;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.ValidationUtil;
import io.domisum.lib.httpbutler.HttpResponse;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.io.IOException;
import java.io.InputStream;

@API
public class HttpResponseStream
	extends HttpResponse
{
	
	// ATTRIBUTES
	private final InputStream stream;
	
	
	// INIT
	public HttpResponseStream(InputStream stream)
	{
		ValidationUtil.notNull(stream, "stream");
		this.stream = stream;
	}
	
	
	// SEND
	@Override
	protected void sendSpecific(HttpServerExchange httpServerExchange)
		throws IOException
	{
		httpServerExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/octet-stream");
		stream.transferTo(httpServerExchange.getOutputStream());
	}
	
}
