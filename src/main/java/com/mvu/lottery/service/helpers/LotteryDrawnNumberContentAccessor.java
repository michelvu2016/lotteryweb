package com.mvu.lottery.service.helpers;

import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LotteryDrawnNumberContentAccessor implements IDataRetrieverService {

	private static Logger logger = LoggerFactory.getLogger(LotteryDrawnNumberContentAccessor.class); 
	
	
	
	
	
	public LotteryDrawnNumberContentAccessor() {
		// TODO Auto-generated constructor stub
	}


	/**
	 * 
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private String getContent(HttpResponse response) throws Exception {
		InputStream inputStream = null;
		StringBuilder data = new StringBuilder();
		try {
			inputStream = response.getEntity().getContent();
			byte[] buffer = new byte[512];

			for (int count = inputStream.read(buffer); count > 0; count = inputStream.read(buffer)) {
				data.append(new String(Arrays.copyOf(buffer, count)));
			}
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
		
		return data.toString();
	}
	
	@Override
	public void retrieveContent(String url, Consumer<String> contentProcessor) throws Exception {
		CloseableHttpClient httpclient = HttpClients.custom()
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();
		try {
			HttpClientContext context = HttpClientContext.create();
			this.getHttp(httpclient, context, url,
					(httpResp, httpGet) -> {
						int statusCode = httpResp.getStatusLine().getStatusCode();
						if(statusCode >= 300 && statusCode < 399) {
							//Get redirected url
							try {
								String redUrl = this.getRedirectedUrl(context, httpGet );
								this.getHttp(httpclient, context, redUrl,
										(tresp, thttpGet) -> {
											if(tresp.getStatusLine().getStatusCode() == 200) {
												try {
													contentProcessor.accept(this.getContent(tresp));
												} catch (Exception e) {
													throw new RuntimeException(e);
													
												}
											} else {
												///Access failed
												throw new RuntimeException(""+tresp.getStatusLine().getStatusCode());
											}
										}
										);
								
								
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								throw new RuntimeException(e);
							}
						} else if(statusCode == 200) {
							//Retrieve content
							try {
								contentProcessor.accept(this.getContent(httpResp));
							} catch (Exception e) {
								throw new RuntimeException(e);
								
							}
						}
						
					});
			
		} finally {
			
		}
		
	}

	/**
	 * 
	 * @param client
	 * @param context
	 * @return
	 * @throws Exception
	 */
	private void getHttp(HttpClient client, 
			HttpClientContext context, String url,
			BiConsumer<HttpResponse, HttpGet> responseProcessor
			) throws Exception  {
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = client.execute(httpGet, context);
		responseProcessor.accept(response, httpGet);
		}
	
	
	private String getRedirectedUrl(HttpClientContext context, HttpRequestBase httpRequest) throws Exception {
		HttpHost target = context.getTargetHost();
        List<URI> redirectLocations = context.getRedirectLocations();
        URI location = URIUtils.resolve(httpRequest.getURI(), target, redirectLocations);
        return location.toASCIIString();
	}
	

	
}
