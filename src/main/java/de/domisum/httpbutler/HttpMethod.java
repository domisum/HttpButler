package de.domisum.httpbutler;

public enum HttpMethod
{

	GET,
	HEAD,
	POST,
	PUT,
	DELETE,
	TRACE,
	OPTIONS,
	CONNECT,
	PATCH;


	public static HttpMethod fromName(String name)
	{
		for(HttpMethod m : values())
			if(m.name().equalsIgnoreCase(name))
				return m;

		throw new IllegalArgumentException("Unknown method name: "+name);
	}

}
