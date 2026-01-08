package io.domisum.lib.httpbutler.endpointtypes;

import io.domisum.lib.httpbutler.HttpResponse;
import io.domisum.lib.httpbutler.request.HttpMethod;
import io.domisum.lib.httpbutler.request.HttpRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.HashMap;

public class HttpButlerEndpointTypeArgsInPathTest
{
	
	@Test
	void testSimplePatternMatching()
	{
		var handler = createArgsInPathEndpoint("/query/#");
		
		assertDoesAccept(handler, createRequest("/query/33"));
		assertDoesAccept(handler, createRequest("/QuEry/33"));
		assertDoesAccept(handler, createRequest("/query/abc"));
		assertDoesAccept(handler, createRequest("/query/wos338axd"));
		assertDoesAccept(handler, createRequest("/query/a33%20memebcw"));
		
		assertDoesNotAccept(handler, createRequest("/query/a33/wow"));
		assertDoesNotAccept(handler, createRequest("/query/"));
	}
	
	@Test
	void testPatternMatching()
	{
		var handler = createArgsInPathEndpoint("/observer-mode/rest/consumer/getGameMetaData/#/#/#/token");
		
		assertDoesAccept(handler, createRequest("/observer-mode/rest/consumer/getGameMetaData/NA1/3407553715/0/token"));
		assertDoesAccept(handler, createRequest("/observer-mode/rest/CONSUMER/getGameMetaData/NA1/3407553715/0/token"));
		
		assertDoesNotAccept(handler, createRequest("/observer-mode/rest/consumer/getGameMetaData//3407553715/0/token"));
		assertDoesNotAccept(handler, createRequest("/observer-mode/rest/consumer/getGameMetaData/NA1/ok/3407553715/0/token"));
	}
	
	
	// ARRANGE
	private HttpButlerEndpointTypeArgsInPath createArgsInPathEndpoint(String pathWithPlaceholders)
	{
		// noinspection AnonymousInnerClassWithTooManyMethods
		return new HttpButlerEndpointTypeArgsInPath()
		{
			
			@Override
			protected HttpMethod METHOD()
			{
				return HttpMethod.GET;
			}
			
			@Override
			protected String PATH_WITH_PLACEHOLDERS()
			{
				return pathWithPlaceholders;
			}
			
			@Override
			protected HttpResponse handleRequest(HttpRequest request)
			{
				return null;
			}
			
		};
	}
	
	private HttpRequest createRequest(String path)
	{
		return new HttpRequest(HttpMethod.GET, path, new HashMap<>(), new HashMap<>(),
			new ByteArrayInputStream(new byte[]{}), null);
	}
	
	
	// ASSERT
	private void assertDoesAccept(HttpButlerEndpointTypeArgsInPath endpoint, HttpRequest request)
	{
		Assertions.assertTrue(endpoint.getAcceptance(request) > 0, "endpoint didn't accept request, but was supposed to:\n" + request);
	}
	
	private void assertDoesNotAccept(HttpButlerEndpointTypeArgsInPath endpoint, HttpRequest request)
	{
		Assertions.assertEquals(0, endpoint.getAcceptance(request), "endpoint accepted request, but wasn't supposed to:\n" + request);
	}
	
}
