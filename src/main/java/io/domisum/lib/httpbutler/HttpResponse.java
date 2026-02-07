package io.domisum.lib.httpbutler;

import io.domisum.lib.auxiliumlib.PHR;
import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.datacontainers.tuple.Duo;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public abstract class HttpResponse
{
	
	// CONSTANTS
	private static final long SECONDS_PER_YEAR = 365L * 24 * 60 * 60;
	
	
	// GENERAL RESPONSE
	private final List<Duo<String>> headers = new ArrayList<>();
	
	
	// INTERFACE
	@API
	public HttpResponse addHeader(String key, String value)
	{
		headers.add(new Duo<>(key, value));
		return this;
	}
	
	@API
	public HttpResponse asDownload(String fileName)
	{
		setContentDisposition("attachment", fileName);
		return this;
	}
	
	@API
	public HttpResponse displayInline(String fileName)
	{
		setContentDisposition("inline", fileName);
		return this;
	}
	
	@API
	public HttpResponse cacheImmutable()
	{
		setCacheControl("public,immutable,max-age=" + SECONDS_PER_YEAR);
		return this;
	}
	
	@API
	public HttpResponse cacheFor(Duration maxAge)
	{
		if(maxAge.isNegative())
			maxAge = Duration.ZERO;
		if(maxAge.getSeconds() > SECONDS_PER_YEAR)
			maxAge = Duration.ofSeconds(SECONDS_PER_YEAR);
		
		setCacheControl("public,max-age=" + maxAge.getSeconds());
		return this;
	}
	
	
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
