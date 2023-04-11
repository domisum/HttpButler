package io.domisum.lib.httpbutler.responses;

import io.domisum.lib.auxiliumlib.PHR;
import io.domisum.lib.auxiliumlib.annotations.API;

@API
public class HttpResponseHtml
	extends HttpResponseString
{
	
	@API
	public HttpResponseHtml(String html)
	{
		super("text/html", html);
	}
	
	
	public static HttpResponseHtml build(String title, String body, String bodyStyle)
	{
		String html = PHR.r("<html><head><title>{}</title></head><body style=\"{}\">{}</body></html>",
			title, body, bodyStyle);
		return new HttpResponseHtml(html);
	}
	
}
