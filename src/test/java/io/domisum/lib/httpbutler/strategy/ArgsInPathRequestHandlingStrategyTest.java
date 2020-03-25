package io.domisum.lib.httpbutler.strategy;

import io.domisum.lib.auxiliumlib.contracts.strategy.Strategy;
import io.domisum.lib.httpbutler.request.HttpMethod;
import io.domisum.lib.httpbutler.request.HttpRequest;
import io.domisum.lib.httpbutler.strategy.strategies.ArgsInPathRequestHandlingStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.HashMap;

public class ArgsInPathRequestHandlingStrategyTest
{
	
	@Test
	void testSimplePatternMatching()
	{
		var strategy = new ArgsInPathRequestHandlingStrategy(HttpMethod.GET, "/query/#", null);
		
		assertDoesApplyTo(strategy, requestFromMethodAndPath(HttpMethod.GET, "/query/33"));
		assertDoesApplyTo(strategy, requestFromMethodAndPath(HttpMethod.GET, "/QuEry/33"));
		assertDoesApplyTo(strategy, requestFromMethodAndPath(HttpMethod.GET, "/query/abc"));
		assertDoesApplyTo(strategy, requestFromMethodAndPath(HttpMethod.GET, "/query/wos338axd"));
		assertDoesApplyTo(strategy, requestFromMethodAndPath(HttpMethod.GET, "/query/a33%20memebcw"));
		
		assertNotApplyTo(strategy, requestFromMethodAndPath(HttpMethod.PATCH, "/query/xddd"));
		assertNotApplyTo(strategy, requestFromMethodAndPath(HttpMethod.GET, "/query/a33/wow"));
		assertNotApplyTo(strategy, requestFromMethodAndPath(HttpMethod.GET, "/query/"));
	}
	
	@Test
	void testPatternMatching()
	{
		var strategy = new ArgsInPathRequestHandlingStrategy(HttpMethod.PUT, "/observer-mode/rest/consumer/getGameMetaData/#/#/#/token", null);
		assertDoesApplyTo(strategy, requestFromMethodAndPath(HttpMethod.PUT, "/observer-mode/rest/consumer/getGameMetaData/NA1/3407553715/0/token"));
	}
	
	
	// ARRANGE
	private HttpRequest requestFromMethodAndPath(HttpMethod method, String path)
	{
		return new HttpRequest(method, path, new HashMap<>(), new HashMap<>(), new ByteArrayInputStream(new byte[] {}));
	}
	
	
	// ASSERT
	private void assertDoesApplyTo(Strategy<HttpRequest> strategy, HttpRequest request)
	{
		Assertions.assertTrue(strategy.doesApplyTo(request), "strategy didn't apply to request, but was supposed to: "+request);
	}
	
	private void assertNotApplyTo(Strategy<HttpRequest> strategy, HttpRequest request)
	{
		Assertions.assertFalse(strategy.doesApplyTo(request), "strategy did apply to request, but wasn't supposed to: "+request);
	}
	
}
