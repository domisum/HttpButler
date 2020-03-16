package io.domisum.lib.httpbutler.exceptions;

import io.domisum.lib.httpbutler.HttpResponseSender;
import io.domisum.lib.auxiliumlib.util.java.annotations.API;
import io.undertow.util.StatusCodes;

@API
public class NotFoundHttpException extends HttpException
{

	// INIT
	@API
	public NotFoundHttpException()
	{

	}

	@API
	public NotFoundHttpException(String message)
	{
		super(message);
	}


	// ERROR
	@Override
	public void sendError(HttpResponseSender responseSender)
	{
		sendError(responseSender, StatusCodes.NOT_FOUND, StatusCodes.NOT_FOUND_STRING);
	}

}
