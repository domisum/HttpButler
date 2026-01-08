package io.domisum.lib.httpbutler.request;

import io.domisum.lib.auxiliumlib.PHR;
import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.httpbutler.exceptions.HttpBadRequest;
import io.undertow.server.handlers.form.FormData;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MultiPartForm
{
	
	// DATA
	private final FormData formData;
	
	
	// INTERFACE
	@API
	public boolean contains(String partName)
	{
		var values = formData.get(partName);
		return values != null && values.size() > 0;
	}
	
	@API
	public String getPartAsString(String partName)
		throws HttpBadRequest, IOException
	{
		var value = getSingleValue(partName);
		if(!value.isFileItem())
			return value.getValue();
		
		var inputStream = value.getFileItem().getInputStream();
		String charsetName = value.getCharset() == null ? "ISO-8859-1" : value.getCharset();
		return IOUtils.toString(inputStream, charsetName);
	}
	
	@API
	public InputStream getPartAsInputStream(String partName)
		throws HttpBadRequest, IOException
	{
		var value = getSingleValue(partName);
		return value.getFileItem().getInputStream();
	}
	
	@API
	public String getPartFileName(String partName)
		throws HttpBadRequest
	{
		var value = getSingleValue(partName);
		return value.getFileName();
	}
	
	
	// INTERNAL
	private FormData.FormValue getSingleValue(String partName)
		throws HttpBadRequest
	{
		var values = formData.get(partName);
		if(values == null || values.isEmpty())
			throw new HttpBadRequest(PHR.r("Multi part form is missing part with name '{}'", partName));
		if(values.size() > 1)
			throw new HttpBadRequest(PHR.r("Multi part form has duplicate parts with name '{}'", partName));
		return values.getFirst();
	}
	
}
