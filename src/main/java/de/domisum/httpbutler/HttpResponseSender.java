package de.domisum.httpbutler;

import de.domisum.lib.auxilium.util.java.annotations.API;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import lombok.RequiredArgsConstructor;

import java.nio.ByteBuffer;

@RequiredArgsConstructor
public class HttpResponseSender
{

	private final HttpServerExchange undertowExchange;


	// SENDING
	@API public void sendPlaintext(String text)
	{
		undertowExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
		undertowExchange.getResponseSender().send(text);
	}

	@API public void sendJson(String json)
	{
		undertowExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
		undertowExchange.getResponseSender().send(json);
	}

	@API public void sendRaw(byte[] data)
	{
		undertowExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/octet-stream");
		undertowExchange.getResponseSender().send(ByteBuffer.wrap(data));
	}


	// ERRORS
	@API public void setStatusCode(int statusCode)
	{
		undertowExchange.setStatusCode(statusCode);
	}

}
