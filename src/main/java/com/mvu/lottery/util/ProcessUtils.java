package com.mvu.lottery.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.springframework.stereotype.Component;




public class ProcessUtils {

	interface ProcessOutputProcessingCallable<T extends Object> extends Callable<T> {
		ProcessOutputProcessingCallable<Object> setInputStream(InputStream stream);
		ProcessOutputProcessingCallable<Object> buildCall(Consumer<String> processor);
	};
	public ProcessUtils() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Execute the command line
	 * @param cmdLine
	 * @return
	 * @throws Exception
	 */
	public int runProces(String cmdLine, String workingDir, Optional<Consumer<String>> outputMonitor) throws Exception {
		Runtime runtime = Runtime.getRuntime();
		
		
		
		ExecutorService executor = Executors.newSingleThreadExecutor();
		
		
		BiConsumer<InputStream, Consumer<String>> monitor = (input, consumerOfString) -> {
			try {
				int numBytes = 0;
				
				while(numBytes != -1) {
					
					byte[] block = new byte[512];
					numBytes = input.read(block);
					if(numBytes > 0) {
						consumerOfString.accept(new String(  Arrays.copyOf(block, numBytes)  ));
					}
				}
			} catch (Exception e) {
				throw new RuntimeException (e);
			}
			
		};
		
		ProcessOutputProcessingCallable<Object> monitorTask = new ProcessOutputProcessingCallable<Object>() {

			InputStream inputStream;
			Consumer<String> consumer;

			@Override
			public ProcessOutputProcessingCallable<Object> setInputStream(InputStream stream) {
				
				this.inputStream = stream;
				return this;
				
			}

			@Override
			public Object call() throws Exception {
				try {
					int numBytes = 0;
				
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
					for(String line = reader.readLine(); line != null; line=reader.readLine()) {
						consumer.accept(line);
					}
					
					
				} catch (Exception e) {
					throw new RuntimeException (e);
				}
		
				notifyAll();
				return null;
			}

			@Override
			public ProcessOutputProcessingCallable<Object> buildCall(Consumer<String> processor) {
				
				this.consumer = processor;
				
				return this;
			}
			
			
		};
		
		String workingFolder = workingDir;
		
		Process process = runtime.exec(cmdLine, null, new File(workingFolder));
		
		executor.submit(monitorTask.setInputStream(process.getInputStream()).buildCall((data) -> {
			outputMonitor.ifPresent(extMonitor -> extMonitor.accept(data));
		}));
		
		
		process.waitFor();
		executor.shutdown();
		return process.exitValue();
	}
	
}
