package io.domisum.lib.httpbutler.exceptions;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.undertow.util.StatusCodes;

@API
public class BadRequestHttpException
		extends HttpException
{
	
	// CONSTANTS
	@Override
	public int ERROR_CODE_INT()
	{
		return StatusCodes.BAD_REQUEST;
	}
	
	@Override
	public String ERROR_CODE_STRING()
	{
		return StatusCodes.BAD_REQUEST_STRING;
	}
	
	
	// INIT
	@API
	public BadRequestHttpException(String message)
	{
		super(message);
	}
	
}
