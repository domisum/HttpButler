package io.domisum.lib.httpbutler.exceptions;

public abstract class HttpException
		extends Exception
{
	
	// CONSTANTS
	public abstract int ERROR_CODE_INT();
	
	public abstract String ERROR_CODE_STRING();
	
	
	// INIT
	protected HttpException(String message)
	{
		super(message);
	}
	
	
	// GETTERS
	public String getResponseMessage()
	{
		String response = ERROR_CODE_STRING()+": "+getMessage();
		return response;
	}
	
}
