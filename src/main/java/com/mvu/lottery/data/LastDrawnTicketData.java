package com.mvu.lottery.data;

import java.io.BufferedReader;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;

import com.mvu.lottery.constant.LotteryConstants;
import com.mvu.lottery.exceptions.LastDrawnTicketAlreadyExistException;
import com.mvu.lottery.stateholder.LastDrawnTicketStateHolder;
import com.mvu.lottery.util.TicketUtils;

@Repository
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class LastDrawnTicketData implements LotteryConstants {
	
	  private static Logger log = LoggerFactory.getLogger(LastDrawnTicketData.class);
	
	  public List<String> retrieveXLinesFromDataFile(Path filePath, int numLines) throws Exception {
		  return (List<String>) this.readXLinesFromFilePath(filePath, this.textFileLineLength(filePath), numLines)
				 .filter((line) -> line != null && line.contains("\t") && line.split("\t").length >= 2)
		  		.collect(Collectors.toList());
	  }
	
	
	
	//Path for each
		private void textFileLineLength(final Path filePath, Consumer<Path> consumer) throws Exception {
			filePath.forEach((path) -> consumer.accept(path));
		}
		
		//Return the line length of the fixed record text file.
		private int textFileLineLength(final Path path) throws Exception {
			try (BufferedReader reader = Files.newBufferedReader(path) ) {
				String line = "";
				int count = 0;
				while(line.length() == 0 && count < 5) {
					line = reader.readLine();
				}
				return (reader.readLine()+"/n").getBytes().length;
			}
		}
		
		/**
		 * Return the byte  buffer block from text file
		 * @param path
		 * @param lineLength
		 * @param numLines
		 * @return
		 * @throws Exception
		 */
		private byte[] readBytesFromFilePath(final Path path, int lineLength, int numLines) throws Exception {
			byte[] buffer = new byte[lineLength * numLines];
			String[] lines = new String[] {};
			
			
			
			try (InputStream fileStream = Files.newInputStream(path, StandardOpenOption.READ)) {
				int numBytesRead = fileStream.read(buffer);
			}
			
			return buffer;
		}

		/**
		 * 
		 * @param path
		 * @param numLines
		 * @return
		 * @throws Exception
		 */
		private byte[] readBytesFromFilePath(final Path path, int numLines) throws Exception {
			
			final int lineLength = this.textFileLineLength(path);
			
			byte[] buffer = new byte[lineLength * numLines];
			String[] lines = new String[] {};
			
			
			
			try (InputStream fileStream = Files.newInputStream(path, StandardOpenOption.READ)) {
				int numBytesRead = fileStream.read(buffer);
			}
			
			return buffer;
		}
		
		/**
		 * Return the stream of string from the file path
		 * @param path
		 * @param lineLength
		 * @param numLines
		 * @return
		 * @throws Exception
		 */
	private Stream<String> readXLinesFromFilePath(final Path path, int lineLength, int numLines) throws Exception {

		byte[] buffer = new byte[lineLength * numLines];
		String[] lines = new String[] {};

		byte[] dataBlock = this.readBytesFromFilePath(path, lineLength, numLines+1);

		lines = new String(dataBlock).split("\n");
		lines = Arrays.copyOf(lines, lines.length -1);

		return Arrays.stream(lines);
	}

		/**
		 * Return the stream
		 * @param path
		 * @param numLines
		 * @return
		 * @throws Exception
		 */
		private Stream<String> readXLinesFromFilePath(final Path path, final int numLines) throws Exception {
			return this.readXLinesFromFilePath(path, this.textFileLineLength(path), numLines);
		}

		/**
		 * Write String data into the file path
		 * @param path
		 * @param data
		 * @throws Exception
		 */
		private void writeDataToFilePath(final Path path, final String data) throws Exception {
			Files.write(path, data.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
		}
		
		/**
		 * Insert new record into the data file. Throws exception if the record already exist in the file
		 * @param stateHolderStream
		 * @param dataPath
		 * @throws Exception
		 */
		public void insert(final Stream<LastDrawnTicketStateHolder> stateHolderStream, final Path dataPath) throws Exception {
			
				List<String> ticketFailedList = new LinkedList<String>();
			
				stateHolderStream.forEach((stateHolder) -> { 
					try {
							this.insert(stateHolder, dataPath);
						} catch(LastDrawnTicketAlreadyExistException e) {
							log.info(String.format("Ticket %s already exists in data file.", stateHolder));
							ticketFailedList.add(stateHolder.toString());
						}
						catch (Exception e) {
							throw new RuntimeException(e);
						}
				});
				
				if(!ticketFailedList.isEmpty()) {
					throw new LastDrawnTicketAlreadyExistException(String.join(" | ", ticketFailedList));
				}
			
		}
		
		/**
		 * 
		 * @param stateHolder
		 */
		public void insert(LastDrawnTicketStateHolder stateHolder, final Path dataPath) throws Exception {
			//Take the snapshot of the last 10 lines
			//Check to see if the ticket already exist or not
			//If not exist, insert into the stream
			
			boolean recNotExist = this.readXLinesFromFilePath(dataPath, 10).noneMatch((line) -> {
				LocalDate date = null;
				try {
					log.info(">>>Line:"+line);
					date = TicketUtils.extractLocalDateTimeFromTicketString((String) line);
					
					log.info(">>>Date converted:"+date);
					
					boolean dateEquals =  date.equals(stateHolder.getDate());
					
					log.info(String.format("Date:%s compared to date: %s, result: %s", date, stateHolder.getDate(), dateEquals));
					
					return dateEquals;
					
				} catch (Exception e) {
					log.error(">>>Exception: "+e);
					return true;
				}
				
				
			});
			
			if (recNotExist) {
				//Prepend the new record to the existing record string
				String dataInFile = new String(this.readBytesFromFilePath(dataPath, NUM_TICKETS_MAINTAINED_IN_FILE));
				
				int pos = dataInFile.lastIndexOf("\n"); //Remove the last line to make clear out partial line
				if (pos != -1) {
					dataInFile = dataInFile.substring(0, pos);
				}
					
				
				String updatedRec = String.format("%s\n%s", TicketUtils.stringFromStateHolder(stateHolder),
						dataInFile);
				
				//Write the new content to the file
				this.writeDataToFilePath(dataPath, updatedRec);
			} else {
				throw new LastDrawnTicketAlreadyExistException("Record already exist in file");
			}
		}
		
}
