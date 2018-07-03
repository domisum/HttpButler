package de.domisum.httpbutler.request;

import com.google.common.collect.Iterables;
import de.domisum.httpbutler.exceptions.BadRequestHttpException;
import de.domisum.lib.auxilium.util.PHR;
import de.domisum.lib.auxilium.util.StringUtil;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@ToString
public class HttpRequest
{

	@Getter private final HttpMethod method;
	@Getter private final String path;
	@Getter private final Map<String, List<String>> headers;
	@Getter private final Map<String, List<String>> queryParameters;
	@Getter private final byte[] body;


	// PATH
	@API public String getPathSegment(int segmentIndex) throws BadRequestHttpException
	{
		String[] pathSplit = path.split(StringUtil.escapeStringForRegex("/"));

		// path starts with slash, but first argument after slash should be regarded as 0
		int splitIndex = segmentIndex+1;
		if(splitIndex >= pathSplit.length)
			throw new BadRequestHttpException("request did not contain path segment on index "+segmentIndex);

		return pathSplit[splitIndex];
	}

	@API public int getPathSegmentAsInt(int segementIndex) throws BadRequestHttpException
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


	// HEADERS
	@API public List<String> getHeaderValues(String key) throws BadRequestHttpException
	{
		if(!headers.containsKey(key))
			throw new BadRequestHttpException(PHR.r("request is missing header with key '{}'", key));

		return headers.get(key);
	}

	@API public String getHeaderValue(String key) throws BadRequestHttpException
	{
		List<String> headerValues = getHeaderValues(key);
		if(headerValues.size() > 1)
			throw new BadRequestHttpException(PHR.r("request contained multiple values for header '{}', must be one", key));

		return Iterables.getOnlyElement(headerValues);
	}

	// PARAMETERS
	@API public List<String> getParameterValues(String key) throws BadRequestHttpException
	{
		if(!queryParameters.containsKey(key))
			throw new BadRequestHttpException(PHR.r("request is missing query parameter with key '{}'", key));

		return queryParameters.get(key);
	}

	@API public String getParameterValue(String key) throws BadRequestHttpException
	{
		List<String> parameterValues = getParameterValues(key);
		if(parameterValues.size() > 1)
			throw new BadRequestHttpException(PHR.r("request contained multiple values for parameter '{}', must be one", key));

		return Iterables.getOnlyElement(parameterValues);
	}

	// BODY
	@API public String getBodyAsString()
	{
		return new String(body, StandardCharsets.UTF_8);
	}

}
