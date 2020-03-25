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
		String errorType = ERROR_CODE_INT()+" "+ERROR_CODE_STRING();
		String response = errorType+": "+getMessage();
		return response;
	}
	
}
