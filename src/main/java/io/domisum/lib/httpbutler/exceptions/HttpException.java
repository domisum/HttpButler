package io.domisum.lib.httpbutler.exceptions;

import io.domisum.lib.httpbutler.HttpResponseSender;
import io.domisum.lib.auxiliumlib.util.java.ExceptionUtil;

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

		String causeString = (getCause() == null) ? "" : (" Exception: \n"+ExceptionUtil.convertThrowableToString(getCause()));
		String response = errorName+(getMessage() == null ? "" : ": "+getMessage())+causeString;
		responseSender.sendPlaintext(response);
	}

}