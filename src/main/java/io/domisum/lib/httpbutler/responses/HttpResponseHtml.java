package io.domisum.lib.httpbutler.responses;

import io.domisum.lib.auxiliumlib.annotations.API;

@API
public class HttpResponseHtml
	extends HttpResponseString
{
	
	// INIT
	@API
	public HttpResponseHtml(String html)
	{
		super("text/html", html);
	}
	
}
