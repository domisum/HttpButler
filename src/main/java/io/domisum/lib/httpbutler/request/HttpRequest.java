package io.domisum.lib.httpbutler.request;

import com.google.common.collect.Iterables;
import io.domisum.lib.auxiliumlib.PHR;
import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.StringUtil;
import io.domisum.lib.httpbutler.exceptions.BadRequestHttpException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@RequiredArgsConstructor
public class HttpRequest
		implements AutoCloseable
{
	
	@Getter
	private final HttpMethod method;
	@Getter
	private final String path;
	
	private final Map<String,List<String>> queryParameters; // keys lowercase
	private final Map<String,List<String>> headers; // keys lowercase
	
	@Getter
	private final InputStream body;
	
	
	// OBJECT
	@Override
	public String toString()
	{
		var lines = new ArrayList<>();
		
		lines.add(method+" "+path);
		
		lines.add("Query parameters:");
		if(queryParameters.isEmpty())
			lines.add(" (none)");
		else
			for(var queryParameter : queryParameters.entrySet())
				for(String value : queryParameter.getValue())
					lines.add(" - "+queryParameter.getKey()+"="+value);
		
		lines.add("Headers:");
		if(headers.isEmpty())
			lines.add(" (none)");
		else
			for(var header : headers.entrySet())
				for(String value : header.getValue())
					lines.add(" - "+header.getKey()+": "+value);
		
		lines.add("Body: (omitted)");
		
		return StringUtil.listToString(lines, "\n");
	}
	
	
	// PATH
	@API
	public String getPathSegment(int segmentIndex)
			throws BadRequestHttpException
	{
		String[] pathSplit = path.split(StringUtil.escapeStringForRegex("/"));
		
		// path starts with slash, but first argument after slash should be regarded as 0
		int splitIndex = segmentIndex+1;
		if(splitIndex >= pathSplit.length)
			throw new BadRequestHttpException("request did not contain path segment on index "+segmentIndex);
		
		return pathSplit[splitIndex];
	}
	
	@API
	public int getPathSegmentAsInt(int segementIndex)
			throws BadRequestHttpException
	{
		String pathSegmentString = getPathSegment(segementIndex);
		
		try
		{
			return Integer.parseInt(pathSegmentString);
		}
		catch(NumberFormatException ignored)
		{
			throw new BadRequestHttpException(PHR.r("path segment on index {} supposed to be integer, was {}",
					pathSegmentString
			));
		}
	}
	
	
	// PARAMETERS
	@API
	public List<String> getQueryParameterValues(String key)
	{
		key = key.toLowerCase();
		
		var values = queryParameters.get(key);
		if(values == null)
			return new ArrayList<>();
		return values;
	}
	
	@API
	public String getQueryParameterValue(String key)
			throws BadRequestHttpException
	{
		key = key.toLowerCase();
		var parameterValues = getQueryParameterValues(key);
		
		if(parameterValues.size() > 1)
			throw new BadRequestHttpException(PHR.r("request contained multiple values for paramenter '{}' but only one is expected", key));
		if(parameterValues.isEmpty())
			throw new BadRequestHttpException(PHR.r("expected value for parameter '{}' but none was provided", key));
		
		return parameterValues.get(0);
	}
	
	@API
	public <T> T parseQueryParameterValue(String key, Function<String,T> parser)
			throws BadRequestHttpException
	{
		String parameterValueString = getQueryParameterValue(key);
		try
		{
			return parser.apply(parameterValueString);
		}
		catch(RuntimeException e)
		{
			String error = PHR.r("invalid value for parameter '{}', given: '{}', problem: {}", key, parameterValueString, e.getMessage());
			throw new BadRequestHttpException(error);
		}
	}
	
	
	// HEADERS
	@API
	public List<String> getHeaderValuesOrException(String key)
			throws BadRequestHttpException
	{
		if(!headers.containsKey(key.toLowerCase()))
			throw new BadRequestHttpException(PHR.r("request is missing header with key '{}'", key));
		
		return headers.get(key.toLowerCase());
	}
	
	@API
	public String getHeaderValueOrException(String key)
			throws BadRequestHttpException
	{
		var headerValues = getHeaderValuesOrException(key);
		if(headerValues.size() > 1)
			throw new BadRequestHttpException(PHR.r("request contained multiple values for header '{}', must be one", key));
		
		return Iterables.getOnlyElement(headerValues);
	}
	
	@API
	public Optional<List<String>> getHeaderValues(String key)
	{
		if(!headers.containsKey(key.toLowerCase()))
			return Optional.empty();
		
		return Optional.of(headers.get(key.toLowerCase()));
	}
	
	@API
	public Optional<String> getHeaderValue(String key)
			throws BadRequestHttpException
	{
		var headerValuesOptional = getHeaderValues(key);
		if(headerValuesOptional.isEmpty())
			return Optional.empty();
		
		var headerValues = headerValuesOptional.get();
		if(headerValues.size() > 1)
			throw new BadRequestHttpException(PHR.r("request contained multiple values for header '{}', must be one", key));
		
		return Optional.of(headerValues.get(0));
	}
	
	
	// BODY
	@API
	public String getBodyAsString()
			throws IOException
	{
		return IOUtils.toString(body, StandardCharsets.UTF_8);
	}
	
	@Override
	public void close()
			throws IOException
	{
		body.close();
	}
	
}
