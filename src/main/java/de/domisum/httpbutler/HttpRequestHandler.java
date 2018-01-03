package de.domisum.httpbutler;

import de.domisum.httpbutler.exceptions.HttpException;
import de.domisum.lib.auxilium.util.java.annotations.API;

@API
public abstract class HttpRequestHandler
{

	protected abstract void handleRequest(HttpRequest httpRequest, HttpResponseSender httpResponseSender) throws HttpException;

}
