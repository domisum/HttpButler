package de.domisum.httpbutler.exceptions;

import de.domisum.httpbutler.HttpResponseSender;
import de.domisum.lib.auxilium.util.java.annotations.API;
import io.undertow.util.StatusCodes;

@API
public class BadRequestHttpException extends HttpException
{

	// INIT
	@API public BadRequestHttpException()
	{

	}

	@API public BadRequestHttpException(String message)
	{
		super(message);
	}

	@API public BadRequestHttpException(String message, Throwable t)
	{
		this(message+" "+t);
	}


	// ERROR
	@Override public void sendError(HttpResponseSender responseSender)
	{
		sendError(responseSender, StatusCodes.BAD_REQUEST, StatusCodes.BAD_REQUEST_STRING);
	}

}
