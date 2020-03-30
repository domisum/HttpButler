package io.domisum.lib.httpbutler.exceptions;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.undertow.util.StatusCodes;

@API
public class HttpNotFound
		extends HttpException
{
	
	// CONSTANTS
	@Override
	public int ERROR_CODE_INT()
	{
		return StatusCodes.NOT_FOUND;
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
