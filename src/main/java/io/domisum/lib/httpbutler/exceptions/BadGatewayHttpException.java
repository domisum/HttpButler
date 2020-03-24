package io.domisum.lib.httpbutler.exceptions;

import io.domisum.lib.httpbutler.HttpResponseSender;
import io.domisum.lib.auxiliumlib.annotations.API;
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
