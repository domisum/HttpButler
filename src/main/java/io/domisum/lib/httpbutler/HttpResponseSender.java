package io.domisum.lib.httpbutler;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import io.undertow.util.StatusCodes;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.nio.ByteBuffer;

@RequiredArgsConstructor
public class HttpResponseSender
{
	
	// REFERENCES
	private final HttpServerExchange undertowExchange;
	
	// STATUS
	@Getter(AccessLevel.PROTECTED)
	private boolean sent = false;
	
	
	// RESPONSE
	@API
	public void addHeader(String key, String value)
	{
		undertowExchange.getResponseHeaders().put(new HttpString(key), value);
	}
	
	private void setStatusCode(int statusCode)
	{
		undertowExchange.setStatusCode(statusCode);
	}
	
	
	// SENDING
	private void validateSendPossible()
	{
		if(sent)
			throw new IllegalStateException("send can only be invoked once");
	}
	
	
	@API
	public void sendPlaintext(String text)
	{
		sendText("text/plain", text);
	}
	
	@API
	public void sendJson(String json)
	{
		sendText("application/json", json);
	}
	
	@API
	public void sendRaw(byte[] data)
	{
		sendRaw("application/octet-stream", data);
	}
	
	@API
	public void sendNoBody()
	{
		validateSendPossible();
		setStatusCode(StatusCodes.NO_CONTENT);
		sent = true;
	}
	
	
	private void sendText(String contentType, String text)
	{
		validateSendPossible();
		undertowExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, contentType);
		undertowExchange.getResponseSender().send(text);
		sent = true;
	}
	
	@API
	public void sendRaw(String contentType, byte[] data)
	{
		validateSendPossible();
		undertowExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, contentType);
		undertowExchange.getResponseSender().send(ByteBuffer.wrap(data));
		sent = true;
	}
	
}
