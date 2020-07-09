package com.mvu.lottery.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import org.springframework.stereotype.Component;



@Component
public class SessionDataBySessionIdAndDataKey<R, T extends Future<R>> {

	private  Map<String, T> dataStore = Collections.synchronizedMap(new HashMap<String, T>());
	
	public SessionDataBySessionIdAndDataKey() {
		
	}
	
	/**
	 * Store the data into the cache
	 * @param request
	 * @param dataKey
	 * @param data
	 * @throws Exception
	 */
	public void storeData(final String dataKey, T data) throws Exception {
		
		dataStore.put(dataKey, data);
	}

	
	/**
	 * Store the future result in the data cache and return the access key (baton)
	 * @param keyPrefix
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public String storeDataAndReturnAccessKey(final String keyPrefix, T data) throws Exception {
		
		String dataKey = keyPrefix + "_" + System.currentTimeMillis();
		
		dataStore.put(dataKey, data);
		
		return dataKey;
	}
	
	/**
	 * Return true if the dataSet is available
	 * @param dataKey
	 * @return
	 * @throws Exception
	 */
	public boolean isDataAvailable(final String dataKey) throws Exception {
		
		return this.dataStore.containsKey(dataKey) && 
                this.dataStore.get(dataKey)
                .isDone();
		
	}
	
	/**
	 * 
	 * @param request
	 * @param dataKey
	 * @return
	 * @throws Exception
	 */
	public R retrieveData(final String dataKey) throws Exception {
		
		return (R) dataStore.remove(dataKey).get();
	}	
	
	/**
	 * Dump the content to the caller
	 * @param processor
	 */
	public void dump(BiConsumer<String, T> processor) {
		
		
		this.dataStore.forEach((k, v) -> {
			processor.accept(k, v);
		});
	}

	/**
	 * Remove the item
	 * @param k
	 */
	public void remove(String k) {
		
		this.dataStore.remove(k);
	}

	


}
