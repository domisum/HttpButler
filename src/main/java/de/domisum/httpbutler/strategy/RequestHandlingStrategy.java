package de.domisum.httpbutler.strategy;

import de.domisum.httpbutler.HttpRequest;
import de.domisum.httpbutler.HttpRequestHandler;
import de.domisum.lib.auxilium.contracts.strategy.Strategy;

public interface RequestHandlingStrategy extends Strategy<HttpRequest>
{

	@Override boolean doesApplyTo(HttpRequest object);

	HttpRequestHandler getHandler();

}
