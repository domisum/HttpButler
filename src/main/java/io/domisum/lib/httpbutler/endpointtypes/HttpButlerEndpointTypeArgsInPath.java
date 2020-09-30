package io.domisum.lib.httpbutler.endpointtypes;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.StringUtil;
import io.domisum.lib.httpbutler.HttpButlerEndpoint;
import io.domisum.lib.httpbutler.request.HttpMethod;
import io.domisum.lib.httpbutler.request.HttpRequest;

@API
public abstract class HttpButlerEndpointTypeArgsInPath
	extends HttpButlerEndpoint
{
	
	// CONSTANTS
	@API
	public static final String PLACEHOLDER = "#";
	private static final String PARAMETER_REGEX = "[0-9a-zA-Z-_%\\.]+";
	
	
	// CONSTANT METHODS
	@API
	protected abstract HttpMethod METHOD();
	
	@API
	protected abstract String PATH_WITH_PLACEHOLDERS();
	
	
	// ACCEPTANCE
	@Override
	protected double getAcceptance(HttpRequest request)
	{
		if(METHOD() != request.getMethod())
			return DOES_NOT_ACCEPT;
		
		String endpointPathWithPlaceholders = HttpRequest.cleanUpPath(PATH_WITH_PLACEHOLDERS()).toLowerCase();
		String pathWithPlaceholdersEscaped = StringUtil.escapeStringForRegex(endpointPathWithPlaceholders);
		String pathRegex = "^"+pathWithPlaceholdersEscaped.replace(PLACEHOLDER, PARAMETER_REGEX)+"$";
		
		boolean matches = request.getPath().toLowerCase().matches(pathRegex);
		if(!matches)
			return DOES_NOT_ACCEPT;
		
		return endpointPathWithPlaceholders.length();
	}
	
}
