package io.domisum.lib.httpbutler.exceptions;

import io.domisum.lib.auxiliumlib.util.ExceptionUtil;

public abstract class HttpException
	extends Exception
{
	
	// CONSTANTS
	public abstract int ERROR_CODE();
	
	public abstract String ERROR_MESSAGE();
	
	
	// INIT
	protected HttpException(String message)
	{
		super(message);
	}
	
	protected HttpException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	
	// GETTERS
	public String getResponseMessage()
	{
		String response = ERROR_MESSAGE()+": "+getMessage();
		if(getCause() != null)
			response += "\n"+ExceptionUtil.convertToString(getCause());
		
		return response;
	}
	
}
