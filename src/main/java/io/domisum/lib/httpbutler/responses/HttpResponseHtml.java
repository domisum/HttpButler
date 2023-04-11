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
	
	
	public static HttpResponseHtml build(String title, String style, String body)
	{
		String head = PHR.r("<head><title>{}</title><style>{}</style></head>", title, style);
		String html = PHR.r("<html>{}<body>{}</body></html>", head, body);
		return new HttpResponseHtml(html);
	}
	
}
