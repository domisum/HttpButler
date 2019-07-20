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

	protected HttpException(String message, Throwable cause)
	{
		super(message, cause);
	}


	public abstract void sendError(HttpResponseSender responseSender);


	// UTIL
	protected void sendError(HttpResponseSender responseSender, int errorCode, String errorName)
	{
		responseSender.setStatusCode(errorCode);

		String causeString = (getCause() == null) ? "" : (" Exception: \n"+getCause().toString());
		String response = errorName+(getMessage() == null ? "" : ": "+getMessage())+causeString;
		responseSender.sendPlaintext(response);
	}

}
