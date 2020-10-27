package io.domisum.lib.httpbutler.request;

import com.google.common.collect.Iterables;
import io.domisum.lib.auxiliumlib.PHR;
import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.StringListUtil;
import io.domisum.lib.auxiliumlib.util.StringUtil;
import io.domisum.lib.httpbutler.exceptions.HttpBadRequest;
import lombok.Getter;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class HttpRequest
	implements AutoCloseable
{
	
	@Getter
	private final HttpMethod method;
	@Getter
	private final String path; // no slash at end or beginning
	
	private final Map<String, List<String>> queryParameters; // keys lowercase
	private final Map<String, List<String>> headers; // keys lowercase
	
	@Getter
	private final InputStream body;
	
	
	// INIT
	public HttpRequest(HttpMethod method, String path,
		Map<String, List<String>> queryParameters, Map<String, List<String>> headers, InputStream body)
	{
		this.method = method;
		this.path = cleanUpPath(path);
		
		var queryParametersCleaned = new HashMap<String, List<String>>();
		for(var entry : queryParameters.entrySet())
			queryParametersCleaned.put(entry.getKey().toLowerCase(), List.copyOf(entry.getValue()));
		this.queryParameters = Map.copyOf(queryParametersCleaned);
		
		var headersCleaned = new HashMap<String, List<String>>();
		for(var entry : headers.entrySet())
			headersCleaned.put(entry.getKey().toLowerCase(), List.copyOf(entry.getValue()));
		this.headers = Map.copyOf(headersCleaned);
		
		this.body = body;
	}
	
	
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
		
		return StringListUtil.listHorizontally(lines, "\n");
	}
	
	
	// PATH
	@API
	public String getPathSegment(int segmentIndex)
		throws HttpBadRequest
	{
		String[] pathSplit = path.split(StringUtil.escapeStringForRegex("/"));
		
		if(segmentIndex >= pathSplit.length)
			throw new HttpBadRequest("Request did not contain path segment on index "+segmentIndex);
		
		return pathSplit[segmentIndex];
	}
	
	@API
	public <T> T parsePathSegment(int segmentIndex, Function<String, T> parser)
		throws HttpBadRequest
	{
		String pathSegmentString = getPathSegment(segmentIndex);
		try
		{
			return parser.apply(pathSegmentString);
		}
		catch(RuntimeException e)
		{
			String error = PHR.r("Invalid value for path segment at index {}, given: '{}', problem: {}", segmentIndex, pathSegmentString, e.getMessage());
			throw new HttpBadRequest(error);
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
		throws HttpBadRequest
	{
		key = key.toLowerCase();
		var parameterValues = getQueryParameterValues(key);
		
		if(parameterValues.size() > 1)
			throw new HttpBadRequest(PHR.r("Request contained multiple values for paramenter '{}' but only one is expected", key));
		if(parameterValues.isEmpty())
			throw new HttpBadRequest(PHR.r("Expected value for parameter '{}' but none was provided", key));
		
		return parameterValues.get(0);
	}
	
	@API
	public Optional<String> getQueryParameterValueOptional(String key)
	{
		key = key.toLowerCase();
		var parameterValues = getQueryParameterValues(key);
		
		if(parameterValues.size() != 1)
			return Optional.empty();
		
		String parameterValue = parameterValues.get(0);
		return Optional.of(parameterValue);
	}
	
	@API
	public <T> T parseQueryParameterValue(String key, Function<String, T> parser)
		throws HttpBadRequest
	{
		String parameterValueString = getQueryParameterValue(key);
		try
		{
			return parser.apply(parameterValueString);
		}
		catch(RuntimeException e)
		{
			String error = PHR.r("Invalid value for parameter '{}', given: '{}', problem: {}", key, parameterValueString, e.getMessage());
			throw new HttpBadRequest(error);
		}
	}
	
	@API
	public <T> T parseQueryParameterValueOrDefault(String key, T defaultValue, Function<String, T> parser)
		throws HttpBadRequest
	{
		var parameterValueOptional = getQueryParameterValueOptional(key);
		if(parameterValueOptional.isEmpty())
			return defaultValue;
		String parameterValueString = parameterValueOptional.get();
		
		try
		{
			return parser.apply(parameterValueString);
		}
		catch(RuntimeException e)
		{
			String error = PHR.r("Invalid value for parameter '{}', given: '{}', problem: {}", key, parameterValueOptional, e.getMessage());
			throw new HttpBadRequest(error);
		}
	}
	
	
	// HEADERS
	@API
	public List<String> getHeaderValues(String key)
		throws HttpBadRequest
	{
		if(!headers.containsKey(key.toLowerCase()))
			throw new HttpBadRequest(PHR.r("Request is missing header with key '{}'", key));
		
		return headers.get(key.toLowerCase());
	}
	
	@API
	public String getHeaderValue(String key)
		throws HttpBadRequest
	{
		var headerValues = getHeaderValues(key);
		if(headerValues.size() > 1)
			throw new HttpBadRequest(PHR.r("Request contained multiple values for header '{}', must be one", key));
		
		return Iterables.getOnlyElement(headerValues);
	}
	
	
	// BODY
	@API
	public String getBodyAsString()
		throws IOException
	{
		return IOUtils.toString(body, StandardCharsets.UTF_8);
	}
	
	@API
	public byte[] getBodyRaw()
		throws IOException
	{
		return IOUtils.toByteArray(body);
	}
	
	@Override
	public void close()
		throws IOException
	{
		body.close();
	}
	
	
	// UTIL
	public static String cleanUpPath(String path)
	{
		if(path.startsWith("/"))
			path = path.substring(1);
		if(path.endsWith("/"))
			path = path.substring(0, path.length()-1);
		
		return path;
	}
	
}
