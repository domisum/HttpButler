package io.domisum.lib.httpbutler.exceptions;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.undertow.util.StatusCodes;

@API
public class HttpInsufficientStorage
	extends HttpException
{
	
	// CONSTANTS
	@Override
	public int ERROR_CODE() {return StatusCodes.INSUFFICIENT_STORAGE;}
	
	@Override
	public String ERROR_MESSAGE() {return StatusCodes.INSUFFICIENT_STORAGE_STRING;}
	
	
	// INIT
	@API
	public HttpInsufficientStorage(String message) {super(message);}
	
	@API
	public HttpInsufficientStorage(String message, Throwable cause) {super(message, cause);}
	
}
