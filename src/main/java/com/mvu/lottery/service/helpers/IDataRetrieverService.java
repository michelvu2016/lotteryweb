package com.mvu.lottery.service.helpers;

import java.util.function.Consumer;

public interface IDataRetrieverService {
	
	public static int RESULT_CODE_SUCCESS = 200;
	public static int RESULT_CODE_FAILURE = 500;
	
	void retrieveContent(String url, Consumer<String> contentProcessor) throws Exception;

}