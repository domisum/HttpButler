package io.domisum.lib.httpbutler.exceptions;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.undertow.util.StatusCodes;

@API
public class HttpForbidden
	extends HttpException
{
	
	// CONSTANTS
	@Override
	public int ERROR_CODE()
	{
		return StatusCodes.FORBIDDEN;
	}
	
	@Override
	public String ERROR_MESSAGE()
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
