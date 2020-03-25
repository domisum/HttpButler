package io.domisum.lib.httpbutler.exceptions;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.undertow.util.StatusCodes;

@API
public class BadGatewayHttpException
		extends HttpException
{
	
	// CONSTANTS
	@Override
	public int ERROR_CODE_INT()
	{
		return StatusCodes.BAD_GATEWAY;
	}
	
	@Override
	public String ERROR_CODE_STRING()
	{
		return StatusCodes.BAD_GATEWAY_STRING;
	}
	
	
	// INIT
	@API
	public BadGatewayHttpException(String message)
	{
		super(message);
	}
	
}
