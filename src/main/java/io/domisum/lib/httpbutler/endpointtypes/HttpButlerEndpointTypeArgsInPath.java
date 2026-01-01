package io.domisum.lib.httpbutler.endpointtypes;

import io.domisum.lib.auxiliumlib.PHR;
import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.StringUtil;
import io.domisum.lib.httpbutler.HttpButlerEndpoint;
import io.domisum.lib.httpbutler.exceptions.HttpBadRequest;
import io.domisum.lib.httpbutler.request.HttpMethod;
import io.domisum.lib.httpbutler.request.HttpRequest;

import java.util.function.Function;

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
		String pathRegex = "^" + pathWithPlaceholdersEscaped.replace(PLACEHOLDER, PARAMETER_REGEX) + "$";
		
		boolean matches = request.getPath().toLowerCase().matches(pathRegex);
		if(!matches)
			return DOES_NOT_ACCEPT;
		
		return endpointPathWithPlaceholders.length();
	}
	
	
	// UTIL
	protected <T> T parseArg(int index, Function<String, T> parse, HttpRequest request)
		throws HttpBadRequest
	{return parse.apply(arg(index, request));}
	
	protected String arg(int index, HttpRequest request)
		throws HttpBadRequest
	{
		var segments = StringUtil.splitByLiteral(HttpRequest.cleanUpPath(PATH_WITH_PLACEHOLDERS()), "/");
		int phi = 0;
		for(int i = 0; i < segments.size(); i++)
			if(segments.get(i).equals(PLACEHOLDER))
			{
				if(phi == index)
					return request.getPathSegment(i);
				phi++;
			}
		throw new IllegalArgumentException(PHR.r("Path only has {} placeholders, but requested index {}", phi, index));
	}
	
}
