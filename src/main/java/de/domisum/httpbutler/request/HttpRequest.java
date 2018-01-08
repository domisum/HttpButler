package de.domisum.httpbutler.request;

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

}
