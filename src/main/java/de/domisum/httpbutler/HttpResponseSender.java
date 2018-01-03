package de.domisum.httpbutler;

import de.domisum.lib.auxilium.util.java.annotations.API;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;
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
	public void sendMethodNotAllowed(String errorMessage)
	{
		undertowExchange.setStatusCode(StatusCodes.METHOD_NOT_ALLOWED);
		sendPlaintext(StatusCodes.METHOD_NOT_ALLOWED_STRING+": "+errorMessage);
	}

	@API public void sendNotFound(String errorMessage)
	{
		undertowExchange.setStatusCode(StatusCodes.NOT_FOUND);
		sendPlaintext(StatusCodes.NOT_FOUND_STRING+": "+errorMessage);
	}

	@API public void sendBadRequest(String errorMessage)
	{
		undertowExchange.setStatusCode(StatusCodes.BAD_REQUEST);
		sendPlaintext(StatusCodes.BAD_REQUEST_STRING+": "+errorMessage);
	}

}
