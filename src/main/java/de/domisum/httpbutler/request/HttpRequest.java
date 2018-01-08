package de.domisum.httpbutler.request;

import de.domisum.httpbutler.exceptions.BadRequestHttpException;
import de.domisum.lib.auxilium.util.PHR;
import de.domisum.lib.auxilium.util.StringUtil;
import de.domisum.lib.auxilium.util.java.annotations.API;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@ToString
public class HttpRequest
{

	@Getter private final HttpMethod method;
	@Getter private final String path;
	@Getter private final Map<String, List<String>> queryParameters;


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
					pathSegmentString));
		}
	}

}
