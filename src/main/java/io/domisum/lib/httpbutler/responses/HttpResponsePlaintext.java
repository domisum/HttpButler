package io.domisum.lib.httpbutler.responses;

import io.domisum.lib.auxiliumlib.annotations.API;

@API
public class HttpResponsePlaintext
	extends HttpResponseString
{
	
	// INIT
	@API
	public HttpResponsePlaintext(String plaintext)
	{
		super("text/plain", plaintext);
	}
	
}
