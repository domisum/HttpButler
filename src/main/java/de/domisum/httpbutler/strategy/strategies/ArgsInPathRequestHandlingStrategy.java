package de.domisum.httpbutler.strategy.strategies;

import de.domisum.httpbutler.HttpRequestHandler;
import de.domisum.httpbutler.request.HttpMethod;
import de.domisum.httpbutler.request.HttpRequest;
import de.domisum.httpbutler.strategy.RequestHandlingStrategy;
import de.domisum.lib.auxilium.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class ArgsInPathRequestHandlingStrategy implements RequestHandlingStrategy
{

	private final Logger logger = LoggerFactory.getLogger(getClass());


	// CONSTANTS
	private static final String PLACEHOLDER = "#";
	private static final String PARAMETER_REGEX = "[0-9a-zA-Z-_%\\.]+";

	// ATTRIBUTES
	private final HttpMethod httpMethod;
	private final String httpPath;

	private final HttpRequestHandler requestHandler;


	// HANDLING STRATEGY
	@Override
	public boolean doesApplyTo(HttpRequest request)
	{
		if(request.getMethod() != httpMethod)
			return false;

		String urlPatternWithTokenPlaceholders = StringUtil.escapeStringForRegex(httpPath.toLowerCase());
		String urlPattern = urlPatternWithTokenPlaceholders.replace(PLACEHOLDER, PARAMETER_REGEX);
		String regex = "^"+urlPattern+"$";
		logger.debug("urlRegex: {}", regex);

		if(!request.getPath().toLowerCase().matches(regex))
			return false;

		return true;
	}

	@Override
	public HttpRequestHandler getHandler()
	{
		return requestHandler;
	}

}
