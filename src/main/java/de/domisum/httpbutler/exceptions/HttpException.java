package de.domisum.httpbutler.exceptions;

import de.domisum.httpbutler.HttpResponseSender;

public abstract class HttpException extends Exception
{

	// INIT
	protected HttpException()
	{

	}

	protected HttpException(String message)
	{
		super(message);
	}


	public abstract void sendError(HttpResponseSender responseSender);


	// UTIL
	protected void sendError(HttpResponseSender responseSender, int errorCode, String errorName)
	{
		responseSender.setStatusCode(errorCode);
		responseSender.sendPlaintext(errorName+(getMessage() == null ? "" : ": "+getMessage()));
	}

}
