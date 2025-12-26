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
	
	
	// INTERFACE
	@API
	public void addHeader(String key, String value) {headers.add(new Duo<>(key, value));}
	
	@API
	public void asDownload(String fileName) {setContentDisposition("attachment", fileName);}
	
	@API
	public void displayInline(String fileName) {setContentDisposition("inline", fileName);}
	
	@API
	public void cacheImmutable() {setCacheControl("public,max-age=31536000,immutable");}
	
	
	// INTERNAL INTERFACE
	protected final void send(HttpServerExchange httpServerExchange)
		throws IOException
	{
		for(var header : headers)
			httpServerExchange.getResponseHeaders().put(new HttpString(header.getA()), header.getB());
		
		sendSpecific(httpServerExchange);
	}
	
	protected abstract void sendSpecific(HttpServerExchange httpServerExchange)
		throws IOException;
	
	
	// INTERNAL
	private void setContentDisposition(String contentDispositionType, String fileName)
	{
		final String key = "content-disposition";
		clearHeader(key);
		addHeader(key, PHR.r("{}; filename=\"{}\"", contentDispositionType, fileName));
	}
	
	private void setCacheControl(String value)
	{
		final String key = "cache-control";
		clearHeader(key);
		addHeader(key, value);
	}
	
	private void clearHeader(String key) {headers.removeIf(d -> d.getA().equalsIgnoreCase(key));}
	
}
