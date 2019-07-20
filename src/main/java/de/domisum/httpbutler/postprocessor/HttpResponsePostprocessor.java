package de.domisum.httpbutler.postprocessor;

import de.domisum.httpbutler.HttpResponseSender;
import de.domisum.httpbutler.exceptions.HttpException;
import de.domisum.httpbutler.request.HttpRequest;

public interface HttpResponsePostprocessor
{

	void postprocess(HttpRequest httpRequest, HttpResponseSender responseSender) throws HttpException;

}
