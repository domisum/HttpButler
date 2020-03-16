package io.domisum.lib.httpbutler.strategy;

import io.domisum.lib.httpbutler.HttpRequestHandler;
import io.domisum.lib.httpbutler.request.HttpRequest;
import io.domisum.lib.auxiliumlib.contracts.strategy.Strategy;

public interface RequestHandlingStrategy extends Strategy<HttpRequest>
{

	@Override
	boolean doesApplyTo(HttpRequest object);

	HttpRequestHandler getHandler();

}
