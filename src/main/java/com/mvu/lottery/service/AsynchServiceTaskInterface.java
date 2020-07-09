package com.mvu.lottery.service;

import java.util.concurrent.Callable;



public interface AsynchServiceTaskInterface<T> extends Callable<T> {
	public void setDataKey(String key);
	

}
