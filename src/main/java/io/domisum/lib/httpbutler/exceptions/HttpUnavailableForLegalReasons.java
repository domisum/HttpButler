package io.domisum.lib.httpbutler.exceptions;

import io.domisum.lib.auxiliumlib.annotations.API;

@API
public class HttpUnavailableForLegalReasons
	extends HttpException
{
	
	// CONSTANTS
	public static final int ERROR_CODE_INT = 451;
	
	
	// CONSTANT METHODS
	@Override
	public int ERROR_CODE()
	{
		return ERROR_CODE_INT;
	}
	
	@Override
	public String ERROR_MESSAGE()
	{
		return "Unavailable For Legal Reasons";
	}
	
	
	// INIT
	@API
	public HttpUnavailableForLegalReasons(String message)
	{
		super(message);
	}
	
}
