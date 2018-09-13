package de.domisum.httpbutler.exceptions;

import de.domisum.httpbutler.HttpResponseSender;
import de.domisum.lib.auxilium.util.java.annotations.API;
import io.undertow.util.StatusCodes;

@API
public class InternalServerErrorHttpException extends HttpException
{

	// INIT
	@API public InternalServerErrorHttpException()
	{

	}

	@API public InternalServerErrorHttpException(String message)
	{
		super(message);
	}

	@API public InternalServerErrorHttpException(Exception e)
	{
		this(e.getClass().getName()+": "+e.getMessage());
	}


	// ERROR
	@Override public void sendError(HttpResponseSender responseSender)
	{
		sendError(responseSender, StatusCodes.INTERNAL_SERVER_ERROR, StatusCodes.INTERNAL_SERVER_ERROR_STRING);
	}

}
