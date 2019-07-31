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

		String causeString = (getCause() == null) ? "" : (" Exception: \n"+convertThrowableToString(getCause()));
		String response = errorName+(getMessage() == null ? "" : ": "+getMessage())+causeString;
		responseSender.sendPlaintext(response);
	}

	private String convertThrowableToString(Throwable throwable)
	{
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(throwable.toString()).append("\n");

		for(StackTraceElement stackTraceElement : throwable.getStackTrace())
			stringBuilder.append("    ").append(stackTraceElement.toString()).append("\n");

		if(throwable.getCause() != null)
			stringBuilder.append("Caused by: ").append(convertThrowableToString(throwable.getCause()));

		return stringBuilder.toString();
	}

}
