package io.domisum.lib.httpbutler.preprocessor;

import io.domisum.lib.httpbutler.exceptions.HttpException;
import io.domisum.lib.httpbutler.request.HttpRequest;

public interface HttpRequestPreprocessor
{

	HttpRequest preprocess(HttpRequest httpRequest) throws HttpException;

}
