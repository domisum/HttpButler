package de.domisum.httpbutler.preprocessor;

import de.domisum.httpbutler.exceptions.HttpException;
import de.domisum.httpbutler.request.HttpRequest;

public interface HttpRequestPreprocessor
{

	HttpRequest preprocess(HttpRequest httpRequest) throws HttpException;

}
