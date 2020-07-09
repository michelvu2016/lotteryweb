package com.mvu.lottery.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MongoDBConfig {

	@Value("${host}")
	private String host;
	
	@Value("${port}")
	private int port;
	
	@Value("${collectionName}")
	private String collectionName;
	
	@Value("${dbName}")
	private String dbName;
	
	
	public String getHost() {
		return host;
	}


	public void setHost(String host) {
		this.host = host;
	}


	public int getPort() {
		return port;
	}


	public void setPort(int port) {
		this.port = port;
	}


	public String getCollectionName() {
		return collectionName;
	}


	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}


	public String getDbName() {
		return dbName;
	}


	public void setDbName(String dbName) {
		this.dbName = dbName;
	}


	public MongoDBConfig() {
		
	}

}
