package io.domisum.lib.httpbutler;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.datacontainers.tuple.Duo;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class HttpResponse
{
	
	// GENERAL RESPONSE
	private final List<Duo<String>> headers = new ArrayList<>();
	
	
	// GENERAL RESPONSE
	@API
	public void addHeader(String key, String value)
	{
		headers.add(new Duo<>(key, value));
	}
	
	
	// SENDING
	protected final void send(HttpServerExchange httpServerExchange)
		throws IOException
	{
		for(var header : headers)
			httpServerExchange.getResponseHeaders().put(new HttpString(header.getA()), header.getB());
		
		sendSpecific(httpServerExchange);
	}
	
	protected abstract void sendSpecific(HttpServerExchange httpServerExchange)
		throws IOException;
	
}
