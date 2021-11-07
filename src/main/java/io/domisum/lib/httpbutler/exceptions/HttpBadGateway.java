package io.domisum.lib.httpbutler.exceptions;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.undertow.util.StatusCodes;

@API
public class HttpBadGateway
	extends HttpException
{
	
	// CONSTANTS
	@Override
	public int ERROR_CODE()
	{
		return StatusCodes.BAD_GATEWAY;
	}
	
	@Override
	public String ERROR_MESSAGE()
	{
		return StatusCodes.BAD_GATEWAY_STRING;
	}
	
	
	// INIT
	@API
	public HttpBadGateway(String message)
	{
		super(message);
	}
	
	@API
	public HttpBadGateway(String message, Throwable cause)
	{
		super(message, cause);
	}
	
}
