package io.domisum.lib.httpbutler.exceptions;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.undertow.util.StatusCodes;

@API
public class HttpBadRequest
	extends HttpException
{
	
	// CONSTANTS
	@Override
	public int ERROR_CODE()
	{
		return StatusCodes.BAD_REQUEST;
	}
	
	@Override
	public String ERROR_MESSAGE()
	{
		return StatusCodes.BAD_REQUEST_STRING;
	}
	
	
	// INIT
	@API
	public HttpBadRequest(String message)
	{
		super(message);
	}
	
	@API
	public HttpBadRequest(String message, Throwable cause)
	{
		super(message, cause);
	}
	
}
