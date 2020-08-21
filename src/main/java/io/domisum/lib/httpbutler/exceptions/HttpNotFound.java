package io.domisum.lib.httpbutler.exceptions;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.undertow.util.StatusCodes;

@API
public class HttpNotFound
	extends HttpException
{
	
	// CONSTANTS
	public static final int ERROR_CODE_INT = StatusCodes.NOT_FOUND;
	
	
	// CONSTANT METHODS
	@Override
	public int ERROR_CODE_INT()
	{
		return ERROR_CODE_INT;
	}
	
	@Override
	public String ERROR_CODE_STRING()
	{
		return StatusCodes.NOT_FOUND_STRING;
	}
	
	
	// INIT
	@API
	public HttpNotFound(String message)
	{
		super(message);
	}
	
}
