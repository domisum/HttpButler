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
	private final String contentType;
	private final byte[] content;
	
	
	// INIT
	public HttpResponseRaw(byte[] content)
	{
		this("application/octet-stream", content);
	}
	
	public static HttpResponseRaw pdf(byte[] rawPdf)
	{
		return new HttpResponseRaw("application/pdf", rawPdf);
	}
	
	public static HttpResponseRaw png(byte[] rawPng)
	{
		return new HttpResponseRaw("image/png", rawPng);
	}
	
	public static HttpResponseRaw jpg(byte[] rawJpg)
	{
		return new HttpResponseRaw("image/jpeg", rawJpg);
	}
	
	
	// SEND
	@Override
	protected void sendSpecific(HttpServerExchange httpServerExchange)
	{
		httpServerExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, contentType);
		httpServerExchange.getResponseSender().send(ByteBuffer.wrap(content));
	}
	
}
