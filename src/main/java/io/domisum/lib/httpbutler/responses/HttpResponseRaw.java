package io.domisum.lib.httpbutler.responses;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.httpbutler.HttpResponse;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import lombok.RequiredArgsConstructor;

import java.nio.ByteBuffer;

@API
@RequiredArgsConstructor
public class HttpResponseRaw
	extends HttpResponse
{
	
	// ATTRIBUTES
	private final byte[] content;
	
	
	// SEND
	@Override
	protected void sendSpecific(HttpServerExchange httpServerExchange)
	{
		httpServerExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/octet-stream");
		httpServerExchange.getResponseSender().send(ByteBuffer.wrap(content));
	}
	
}
