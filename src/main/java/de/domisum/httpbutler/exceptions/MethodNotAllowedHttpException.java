package de.domisum.httpbutler.exceptions;

import de.domisum.httpbutler.HttpResponseSender;
import de.domisum.lib.auxilium.util.java.annotations.API;
import io.undertow.util.StatusCodes;

@API
public class MethodNotAllowedHttpException extends HttpException
{

	// INIT
	@API public MethodNotAllowedHttpException()
	{

	}

	@API public MethodNotAllowedHttpException(String message)
	{
		super(message);
	}


	// ERROR
	@Override public void sendError(HttpResponseSender responseSender)
	{
		sendError(responseSender, StatusCodes.METHOD_NOT_ALLOWED, StatusCodes.METHOD_NOT_ALLOWED_STRING);
	}

}
