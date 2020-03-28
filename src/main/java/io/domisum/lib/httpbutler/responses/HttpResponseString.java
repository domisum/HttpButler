package io.domisum.lib.httpbutler.responses;

import io.domisum.lib.httpbutler.HttpResponse;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HttpResponseString
		extends HttpResponse
{
	
	// ATTRIBUTES
	private final String contentType;
	private final String content;
	
	
	// SEND
	@Override
	protected void sendSpecific(HttpServerExchange httpServerExchange)
	{
		httpServerExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, contentType);
		httpServerExchange.getResponseSender().send(content);
	}
	
}
