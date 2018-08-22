package de.domisum.httpbutler;

import de.domisum.httpbutler.exceptions.HttpException;
import de.domisum.httpbutler.request.HttpRequest;
import de.domisum.lib.auxilium.util.java.annotations.API;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@API
public abstract class HttpRequestHandler
{

	protected final Logger logger = LoggerFactory.getLogger(getClass());


	// HANDLER
	protected abstract void handleRequest(HttpRequest request, HttpResponseSender responseSender)
			throws HttpException, IOException;

}
