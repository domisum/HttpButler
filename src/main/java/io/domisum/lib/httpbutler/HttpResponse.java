package io.domisum.lib.httpbutler;

import io.domisum.lib.auxiliumlib.PHR;
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
	
	@API
	public void asDownload(String fileName)
	{
		setContentDisposition("attachment", fileName);
	}
	
	@API
	public void displayInline(String fileName)
	{
		setContentDisposition("inline", fileName);
	}
	
	private void setContentDisposition(String contentDispositionType, String fileName)
	{
		final String headerKey = "Content-Disposition";
		String headerValue = PHR.r("{}; filename=\"{}\"", contentDispositionType, fileName);
		headers.add(new Duo<>(headerKey, headerValue));
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
