package io.domisum.lib.httpbutler.exceptions;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.undertow.util.StatusCodes;

@API
public class HttpInternalServerError
	extends HttpException
{
	
	// CONSTANTS
	@Override
	public int ERROR_CODE_INT()
	{
		return StatusCodes.INTERNAL_SERVER_ERROR;
	}
	
	@Override
	public String ERROR_CODE_STRING()
	{
		return StatusCodes.INTERNAL_SERVER_ERROR_STRING;
	}
	
	
	// INIT
	@API
	public HttpInternalServerError(String message)
	{
		super(message);
	}
	
	@API
	public HttpInternalServerError(String message, Throwable cause)
	{
		super(message, cause);
	}
	
}
