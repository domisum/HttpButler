package io.domisum.lib.httpbutler.exceptions;

import io.domisum.lib.httpbutler.HttpResponseSender;
import io.domisum.lib.auxiliumlib.annotations.API;
import io.undertow.util.StatusCodes;
import lombok.NoArgsConstructor;

@API
@NoArgsConstructor
public class ForbiddenHttpException extends HttpException
{

	// INIT
	@API
	public ForbiddenHttpException(String message)
	{
		super(message);
	}


	// ERROR
	@Override
	public void sendError(HttpResponseSender responseSender)
	{
		sendError(responseSender, StatusCodes.FORBIDDEN, StatusCodes.FORBIDDEN_STRING);
	}

}
