package io.domisum.lib.httpbutler.responses;

import io.domisum.lib.auxiliumlib.PHR;
import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.httpbutler.HttpResponse;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Set;

@API
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpResponseImage
	extends HttpResponse
{
	
	// CONSTANTS
	private static final Set<String> DIRECT_CONTENT_TYPE_FORMATS = Set.of(
		"png", "jpeg", "bmp", "avif", "gif", "svg+xml", "tiff", "webp"
	);
	private static final Map<String, String> CONTENT_TYPE_MAPPINGS = Map.of(
		"jpg", "jpeg",
		"svg", "svg+xml"
	);
	
	// ATTRIBUTES
	private final String contentType;
	private final byte[] content;
	
	
	// INIT
	@API
	public static HttpResponseImage ofFormat(byte[] raw, String format)
	{return new HttpResponseImage(contentTypeFromFormat(format), raw);}
	
	@API
	public static HttpResponseImage png(byte[] rawPng)
	{return new HttpResponseImage("image/png", rawPng);}
	
	@API
	public static HttpResponseImage jpg(byte[] rawJpg)
	{return new HttpResponseImage("image/jpeg", rawJpg);}
	
	
	// SEND
	@Override
	protected void sendSpecific(HttpServerExchange httpServerExchange)
	{
		httpServerExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, contentType);
		httpServerExchange.getResponseSender().send(ByteBuffer.wrap(content));
	}
	
	
	// UTIL
	public static String contentTypeFromFormat(String fileExtension)
	{
		String ext = fileExtension;
		ext = ext.toLowerCase().strip();
		if(ext.startsWith("."))
			ext = ext.substring(1);
		
		if(DIRECT_CONTENT_TYPE_FORMATS.contains(ext))
			return "image/" + ext;
		String mapsTo = CONTENT_TYPE_MAPPINGS.get(ext);
		if(mapsTo != null)
			return "image/" + mapsTo;
		
		throw new IllegalArgumentException(PHR.r("Unknown image format: '{}'", fileExtension));
	}
	
}
