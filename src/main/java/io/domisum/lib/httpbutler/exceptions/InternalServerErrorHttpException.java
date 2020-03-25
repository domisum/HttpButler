package io.domisum.lib.httpbutler.exceptions;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.undertow.util.StatusCodes;

@API
public class InternalServerErrorHttpException
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
	public InternalServerErrorHttpException(String message)
	{
		super(message);
	}
	
}
