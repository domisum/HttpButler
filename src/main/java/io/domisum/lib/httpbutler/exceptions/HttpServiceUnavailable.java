package io.domisum.lib.httpbutler.exceptions;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.undertow.util.StatusCodes;

@API
public class HttpServiceUnavailable
	extends HttpException
{
	
	// CONSTANTS
	public static final int ERROR_CODE_INT = StatusCodes.SERVICE_UNAVAILABLE;
	
	
	// CONSTANT METHODS
	@Override
	public int ERROR_CODE_INT()
	{
		return ERROR_CODE_INT;
	}
	
	@Override
	public String ERROR_CODE_STRING()
	{
		return StatusCodes.SERVICE_UNAVAILABLE_STRING;
	}
	
	
	// INIT
	@API
	public HttpServiceUnavailable(String message)
	{
		super(message);
	}
	
}
