package de.domisum.httpbutler.exceptions;

import de.domisum.httpbutler.HttpResponseSender;
import de.domisum.lib.auxilium.util.java.annotations.API;
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
