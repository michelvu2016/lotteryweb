package com.mvu.lottery.service;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.hibernate.annotations.Proxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.mvu.lottery.constant.LotteryConfiguration;
import com.mvu.lottery.constant.LotteryConstants;
import com.mvu.lottery.data.AnalyzedDrawnNumberData;
import com.mvu.lottery.exceptions.SystemIsBusyOrDataNotAvailableException;
import com.mvu.lottery.stateholder.AsynchDataStateHolder;
import com.mvu.lottery.util.FutureTransactionResultManager;
import com.mvu.lottery.util.ProcessUtils;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AnalyzedDrawnNumberService implements LotteryConstants {
	
	private static Logger log = LoggerFactory.getLogger(AnalyzedDrawnNumberService.class);
	
	@FunctionalInterface
	interface RunTaskAsynch<T> {
		public T execute();
	}

	private AnalyzedDrawnNumberData data;
	private LotteryConfiguration lotteryAppConfig;
	
	private ExecutorService executor;
	
	@Autowired
	private FutureTransactionResultManager<Future<?>> futureTransactionResultManager;
	
	@Autowired
	private ProcessUtils processUtil;
	
	@Autowired()
	public AnalyzedDrawnNumberService(AnalyzedDrawnNumberData data, LotteryConfiguration lotteryAppConfig) {
		this.data = data;
		this.lotteryAppConfig = lotteryAppConfig;
	}

	@PostConstruct
	private void setup() {
		this.executor = Executors.newFixedThreadPool(2);
	}
	
	@PreDestroy
	private void tearDown() {
		try {
			this.executor.awaitTermination(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			
		} finally {
			if(!this.executor.isTerminated()) {
				this.executor.shutdown();
			}
		}
		
		this.executor.shutdown();
	}
	
	
	/**
	 * Return the analyzed data from the data file (path)
	 * @param lotteryType
	 * @return
	 * @throws Exception
	 */
	public String retrieveAnalyzedData(LotteryType lotteryType) throws Exception {
		return this.data.getAnalyzedData(this.lotteryAppConfig.getAnalyzedDataPathForType(lotteryType));
	}
	
	/**
	 * 
	 * @param lotteryType
	 * @param resultDataPrefix
	 * @return
	 * @throws Exception
	 */
	public AsynchDataStateHolder retrieveAnalyzedDataAsynch(LotteryType lotteryType, String resultDataPrefixOrBaton) throws Exception {
		
		AsynchDataStateHolder stateHolder = AsynchDataStateHolder.createDefault();
		
		if (null != resultDataPrefixOrBaton && resultDataPrefixOrBaton.indexOf("_") != -1) {
			
			if(this.futureTransactionResultManager.isDataAvailable(resultDataPrefixOrBaton))
			{
				Object data = this.futureTransactionResultManager.retrieveData(resultDataPrefixOrBaton);
				stateHolder = AsynchDataStateHolder.createWithData( data);
				
			} else {
				throw new SystemIsBusyOrDataNotAvailableException("Data not available");
			}
		} else {
			log.info(">>>>Defining the task...");
			RunTaskAsynch<String> task = () -> {
				log.info(">>>>Enter execute code block...");
				try {
					//Check if the data file is up to date
					log.info("Final commandline format:"+lotteryAppConfig.getProcessCommandLineFormat());
					log.info("Lottery type name:"+lotterNameFromLotteryType(lotteryType));
					
					String cmdLine = String.format(lotteryAppConfig.getProcessCommandLineFormat(), lotterNameFromLotteryType(lotteryType), 75);
					
					log.info("Final commandline:" + cmdLine);
					log.info(">>>>processUtil.runProces(..)....");
					processUtil.runProces(cmdLine,
							lotteryAppConfig.getWorkingDir(), Optional.ofNullable((data) -> {
								System.out.println(data);
							}));
					
					return this.data.getAnalyzedData(this.lotteryAppConfig.getAnalyzedDataPathForType(lotteryType));
				} catch (Exception e) {
					
					throw new RuntimeException(e);
				}
				
			};
			
			Future<String> result = this.runFunctionAsynch(task);
			
			String baton = "RandomStringForUniqueNess" + "_" + System.currentTimeMillis();
			
			this.futureTransactionResultManager.storeData(baton, result);
			
			stateHolder = AsynchDataStateHolder.createWithBaton(baton);
		}
		
		return stateHolder;
	}
	
	/**
	 * 
	 * @param task
	 */
	private <T> Future<T> runFunctionAsynch(RunTaskAsynch<T> task) {
		
		AsynchServiceTaskInterface<T> callable = new AsynchServiceTaskInterface<T>() {

			@Override
			public T call() throws Exception {
				log.info(">>>>Execute the task...");
				return task.execute();
			}

			@Override
			public void setDataKey(String key) {
				
				
			}
		};
		
		Future<T> futureResult = this.executor.submit(callable);
		
		return futureResult;
		
	}
	
	
	
}
