package com.mvu.lottery.service;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mvu.lottery.stateholder.LastDrawnTicketStateHolder;
import com.mvu.lottery.util.SessionDataBySessionIdAndDataKey;

@Service
public class AsynchTaskMaintenanceService {
	
	interface MaintenanceTask extends Callable<Integer> {
		void shutdown();
	}
	
	private ExecutorService executor;
	
	@Autowired
	private SessionDataBySessionIdAndDataKey<List<LastDrawnTicketStateHolder>, Future<List<LastDrawnTicketStateHolder>>> asynchStorage;

	
	
	
	private MaintenanceTask maintTask;
	
	
	public AsynchTaskMaintenanceService() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	public void setup() {
		this.executor = Executors.newSingleThreadExecutor();
		maintTask = new MaintenanceTask() {
			
			AtomicBoolean needToTerminate = new AtomicBoolean(false);
			
			
			@Override
			public Integer call() throws Exception {
				
				while(!needToTerminate.get()) {
				
						List<String> itemToBeRemoved = new LinkedList<>();
						asynchStorage.dump((k, v) -> {
							long timeLapse = System.currentTimeMillis() - Long.valueOf(k.split("_")[1]);
							if ((timeLapse/1000) > 5) {
								itemToBeRemoved.add(k);
							}
						});
						
						if (!itemToBeRemoved.isEmpty()) {
							itemToBeRemoved.forEach((k) -> {
								asynchStorage.remove(k);
							});
						}
						
						Thread.sleep(500);
				}
				
				return 0;
			}
			
			@Override
			public void shutdown() {
				needToTerminate.set(true);
				
			}
		};
		
	}
	
	@PreDestroy
	public void tearDown() {
		if (maintTask != null) {
			maintTask.shutdown();
		}
		
		this.executor.shutdown();
		
	}
	
}
