package de.domisum.httpbutler.exceptions;

import de.domisum.httpbutler.HttpResponseSender;
import de.domisum.lib.auxilium.util.java.annotations.API;
import io.undertow.util.StatusCodes;

@API
public class BadGatewayHttpException extends HttpException
{

	// INIT
	@API
	public BadGatewayHttpException()
	{

	}

	@API
	public BadGatewayHttpException(String message)
	{
		super(message);
	}

	@API
	public BadGatewayHttpException(String message, Throwable cause)
	{
		super(message, cause);
	}


	// ERROR
	@Override
	public void sendError(HttpResponseSender responseSender)
	{
		sendError(responseSender, StatusCodes.BAD_GATEWAY, StatusCodes.BAD_GATEWAY_STRING);
	}

}
