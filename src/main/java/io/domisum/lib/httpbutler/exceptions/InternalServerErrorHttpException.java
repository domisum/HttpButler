package io.domisum.lib.httpbutler.exceptions;

import io.domisum.lib.httpbutler.HttpResponseSender;
import io.domisum.lib.auxiliumlib.util.java.annotations.API;
import io.undertow.util.StatusCodes;

@API
public class InternalServerErrorHttpException extends HttpException
{

	// INIT
	@API
	public InternalServerErrorHttpException()
	{

	}

	@API
	public InternalServerErrorHttpException(String message)
	{
		super(message);
	}

	@API
	public InternalServerErrorHttpException(String message, Throwable cause)
	{
		super(message, cause);
	}


	// ERROR
	@Override
	public void sendError(HttpResponseSender responseSender)
	{
		sendError(responseSender, StatusCodes.INTERNAL_SERVER_ERROR, StatusCodes.INTERNAL_SERVER_ERROR_STRING);
	}

}
