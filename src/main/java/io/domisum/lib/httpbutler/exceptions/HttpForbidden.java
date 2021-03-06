package io.domisum.lib.httpbutler.exceptions;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.undertow.util.StatusCodes;

@API
public class HttpForbidden
	extends HttpException
{
	
	// CONSTANTS
	@Override
	public int ERROR_CODE_INT()
	{
		return StatusCodes.FORBIDDEN;
	}
	
	@Override
	public String ERROR_CODE_STRING()
	{
		return StatusCodes.FORBIDDEN_STRING;
	}
	
	
	// INIT
	@API
	public HttpForbidden(String message)
	{
		super(message);
	}
	
}
