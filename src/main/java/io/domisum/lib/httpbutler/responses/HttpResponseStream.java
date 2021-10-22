package io.domisum.lib.httpbutler.responses;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.datacontainers.KnownLengthInputStream;
import io.domisum.lib.httpbutler.HttpResponse;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
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
	@Nullable
	private final Long streamLength;
	
	
	// INIT
	public HttpResponseStream(KnownLengthInputStream stream)
	{
		this(stream.getInputStream(), stream.getLength());
	}
	
	public HttpResponseStream(InputStream stream)
	{
		this(stream, null);
	}
	
	private HttpResponseStream(InputStream stream, Long streamLength)
	{
		this("application/octet-stream", stream, streamLength);
	}
	
	
	// SEND
	@Override
	protected void sendSpecific(HttpServerExchange httpServerExchange)
		throws IOException
	{
		httpServerExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, contentType);
		if(streamLength != null)
			httpServerExchange.getResponseHeaders().put(Headers.CONTENT_LENGTH, streamLength);
		
		stream.transferTo(httpServerExchange.getOutputStream());
	}
	
}
