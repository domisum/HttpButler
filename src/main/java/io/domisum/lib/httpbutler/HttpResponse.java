package io.domisum.lib.httpbutler;

import io.undertow.server.HttpServerExchange;

public abstract class HttpResponse
{
	
	protected abstract void send(HttpServerExchange httpServerExchange);
	
}
