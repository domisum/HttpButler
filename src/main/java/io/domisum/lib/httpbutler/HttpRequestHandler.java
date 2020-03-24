package io.domisum.lib.httpbutler;

import io.domisum.lib.httpbutler.exceptions.HttpException;
import io.domisum.lib.httpbutler.request.HttpRequest;
import io.domisum.lib.auxiliumlib.annotations.API;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@API
public abstract class HttpRequestHandler // TODO convert to interface
{

	protected final Logger logger = LoggerFactory.getLogger(getClass());


	// HANDLER
	protected abstract void handleRequest(HttpRequest request, HttpResponseSender responseSender) throws HttpException;

}
