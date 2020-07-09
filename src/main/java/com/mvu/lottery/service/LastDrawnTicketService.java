package com.mvu.lottery.service;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import com.mvu.lottery.constant.LotteryConfiguration;
import com.mvu.lottery.constant.LotteryConstants;
import com.mvu.lottery.data.LastDrawnTicketData;
import com.mvu.lottery.exceptions.TicketDataInvalidFormatException;
import com.mvu.lottery.stateholder.LastDrawnNumberOnfileInfo;
import com.mvu.lottery.stateholder.LastDrawnTicketStateHolder;
import com.mvu.lottery.util.JsonStringToOptionalTicketStateHolderList;
import com.mvu.lottery.util.LoteryDataConverter;
import com.mvu.lottery.util.SessionDataBySessionIdAndDataKey;
import com.mvu.lottery.util.TicketUtils;


@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class LastDrawnTicketService implements LotteryConstants {

	private static Logger log = LoggerFactory.getLogger(LastDrawnTicketService.class);

	private LastDrawnTicketData dataRepo;
	@Inject
	private LotteryConfiguration lotteryConfig;

	private LoteryDataConverter dataConverter;

	private ExecutorService executor;

	@Autowired
	       //private ConversionService conversionService;
	
	private JsonStringToOptionalTicketStateHolderList jsonStringToOptionalTicketStateHolderList;
	
	@Autowired
	private SessionDataBySessionIdAndDataKey<LastDrawnNumberOnfileInfo, Future<LastDrawnNumberOnfileInfo>> asynchStorage;
	
	@Inject
	public LastDrawnTicketService(LastDrawnTicketData dataRepo, LoteryDataConverter dataConverter) {
		this.dataRepo = dataRepo;
		this.dataConverter = dataConverter;
	}

	@PostConstruct
	public void serviceInit() {
		this.executor = Executors.newSingleThreadExecutor();
	}

	@PreDestroy
	public void serviceShutdown() {
		this.executor.shutdown();

	}

/////--------------- For data retrieval -------------------////

	/**
	 * Generic retrieval method for all type of lottery
	 * 
	 * @param numLines
	 * @param lotteryType
	 * @return
	 * @throws Exception
	 */
	public List<LastDrawnTicketStateHolder> retrieveLastDrawnTicketsFor(final int numLines,
			final LotteryType lotteryType) throws Exception {

		List<LastDrawnTicketStateHolder> returnData = new ArrayList<>();

		this.dataRepo.retrieveXLinesFromDataFile(lotteryConfig.getDataPathForType(lotteryType), numLines)
				.forEach((line) -> {
					try {
						log.info(">>>Processing line:" + line);
						returnData.add(dataConverter.toStateHolder(line));
					} catch (TicketDataInvalidFormatException e) {
						// Ignore this line, continue to the end of the stream
					} catch (Exception e) {
						throw new RuntimeException(e);
					}

				});

		return returnData;
	}

	/**
	 * Generic retrieval method for all type of lottery. Asynchronously.
	 * 
	 * @param numLines
	 * @param lotteryType
	 * @return String baton for subsequent call to claim the result
	 * @throws Exception
	 */
	public String retrieveLastDrawnTicketsAsynchFor(final int numLines,
			final LotteryType lotteryType, final String ticketPrefix) throws Exception {


		AsynchServiceTaskInterface<LastDrawnNumberOnfileInfo> job = new AsynchServiceTaskInterface<LastDrawnNumberOnfileInfo>() {
			private LastDrawnTicketData dataRepot = dataRepo;
			private String dataKey;
			
			
			@Override
			public LastDrawnNumberOnfileInfo call() throws Exception {
				List<LastDrawnTicketStateHolder> returnData = new ArrayList<>();
				this.dataRepot.retrieveXLinesFromDataFile(lotteryConfig.getDataPathForType(lotteryType), numLines)
						.forEach((line) -> {
							try {
								log.info(">>>Processing line:" + line);
								returnData.add(dataConverter.toStateHolder(line));
							} catch (TicketDataInvalidFormatException e) {
								// Ignore this line, continue to the end of the stream
							} catch (Exception e) {
								throw new RuntimeException(e);
							}

						});
				
				int numPastDrawnsNeeded = numberOfDrawnTicketsTobeUpdated(lotteryType);
				
				LastDrawnNumberOnfileInfo lastDrawnNumberInfo = new LastDrawnNumberOnfileInfo(returnData, numPastDrawnsNeeded);
				
				if (numPastDrawnsNeeded > 0) {
					lastDrawnNumberInfo.setDataRetrievalUrl(getLastDrawnNumberApiUrl(lotteryType, numPastDrawnsNeeded));
				}
				
				return lastDrawnNumberInfo;
			}


			@Override
			public void setDataKey(String key) {
				this.dataKey = key;
				
			}

		};
		
		String baton = ticketPrefix+"_"+System.currentTimeMillis();
		
		Future<LastDrawnNumberOnfileInfo> futureResult = this.executor.submit(job); //Submit the job
				
		this.asynchStorage.storeData(baton, futureResult);
		
		
		return baton;
		
	}

	///// ---------------------- For adding new entries
	///// -------------------------------------////

	/**
	 * Generic add for all types of Lottery
	 * 
	 * @param stateHolderCollection
	 * @param forType
	 * @throws Exception
	 */
	public void insertNewLastDrawnTicketFor(final Collection<LastDrawnTicketStateHolder> stateHolderCollection,
			final LotteryType forType) throws Exception {
		this.dataRepo.insert(stateHolderCollection.stream(), lotteryConfig.getDataPathForType(forType));
	}

	/**
	 * 
	 * @param jsonString
	 * @param forType
	 * @throws Exception
	 * Extract the number from the json string and update the target lottery drawn mumber base
	 */
	public void insertNewLastDrawnTicketFor(String jsonString, final LotteryType forType) throws Exception {
		
		log.info(">>>>>LastDrawnTicketService insertNewLastDrawnTicketFor - convert jsonString to list of LastDrawnTicketStateHolder");
		Optional<List<LastDrawnTicketStateHolder>> opTicketHolder = jsonStringToOptionalTicketStateHolderList.convert(jsonString);
		
		if (opTicketHolder.isPresent()) {
			log.info(">>>>>LastDrawnTicketService insertNewLastDrawnTicketFor - ticket list:"+opTicketHolder.get().toString());
			
			List<LastDrawnTicketStateHolder> listOfDrawnNumbers = opTicketHolder.get();
			
			log.info(">>>>>LastDrawnTicketService insertNewLastDrawnTicketFor - sort the list");
			
			Collections.sort(listOfDrawnNumbers, (me, other) -> {
				int result = 0;
				
				if (me.getDate().isAfter(other.getDate())) {
					result = 1;
				} else if (me.getDate().isBefore(other.getDate())) {
					result = -1;
				}
				
				return result;
			});
			
			insertNewLastDrawnTicketFor(listOfDrawnNumbers, forType);
		}
	}
	
	/**
	 * Generic add for all types of Lottery
	 * 
	 * @param stateHolder
	 * @throws Exception
	 */
	public void insertNewLastDrawnTicketFor(final LastDrawnTicketStateHolder stateHolder, final LotteryType forType)
			throws Exception {
		this.dataRepo.insert(stateHolder, lotteryConfig.getDataPathForType(forType));
	}

	/**
	 * 
	 * @param stateHolderCollection
	 * @throws Exception
	 */
	public void insertNewLastDrawnTicketFor(final Collection<LastDrawnTicketStateHolder> stateHolderCollection)
			throws Exception {
		this.dataRepo.insert(stateHolderCollection.stream(), lotteryConfig.getSuperLottoDataPath());
	}

	//// --- Super lotto ---------
	/**
	 * Add the list of last drawn ticket for SuperLotto
	 * 
	 * @param stateHolderCollection
	 * @throws Exception
	 */
	public void insertNewLastDrawnTicketForSuperLotto(
			final Collection<LastDrawnTicketStateHolder> stateHolderCollection) throws Exception {
		this.dataRepo.insert(stateHolderCollection.stream(), lotteryConfig.getSuperLottoDataPath());
	}

	/**
	 * 
	 * @param stateHolder
	 * @throws Exception
	 */
	public void insertNewLastDrawnTicketForSuperLotto(final LastDrawnTicketStateHolder stateHolder) throws Exception {
		this.dataRepo.insert(stateHolder, lotteryConfig.getSuperLottoDataPath());
	}

////--- Fantasy 5 ---------
	/**
	 * Add the list of last drawn ticket for Fantasy5
	 * 
	 * @param stateHolderCollection
	 * @throws Exception
	 */
	public void insertNewLastDrawnTicketForFantasy5(final Collection<LastDrawnTicketStateHolder> stateHolderCollection)
			throws Exception {
		this.dataRepo.insert(stateHolderCollection.stream(), lotteryConfig.getFantasy5DataPath());
	}

	/**
	 * 
	 * @param stateHolder
	 * @throws Exception
	 */
	public void insertNewLastDrawnTicketForFantasy5(final LastDrawnTicketStateHolder stateHolder) throws Exception {
		this.dataRepo.insert(stateHolder, lotteryConfig.getFantasy5DataPath());
	}

////--- Powerball ---------
	/**
	 * Add the list of last drawn ticket for Powerball
	 * 
	 * @param stateHolderCollection
	 * @throws Exception
	 */
	public void insertNewLastDrawnTicketForPowerball(final Collection<LastDrawnTicketStateHolder> stateHolderCollection)
			throws Exception {
		this.dataRepo.insert(stateHolderCollection.stream(), lotteryConfig.getPowerballDataPath());
	}

	/**
	 * 
	 * @param stateHolder
	 * @throws Exception
	 */
	public void insertNewLastDrawnTicketForPowerball(final LastDrawnTicketStateHolder stateHolder) throws Exception {
		this.dataRepo.insert(stateHolder, lotteryConfig.getPowerballDataPath());
	}

	//// ------------------ Common processing -------------------------////

	private List<LastDrawnTicketStateHolder> retrieveLastDrawnTicketData(final int numLines,
			final Supplier<Path> filePathSupplier) throws Exception {

		List<LastDrawnTicketStateHolder> returnData = new ArrayList<>();

		this.dataRepo.retrieveXLinesFromDataFile(filePathSupplier.get(), numLines).forEach((line) -> {
			try {
				returnData.add(dataConverter.toStateHolder(line));
			} catch (TicketDataInvalidFormatException e) {
				// Ignore this line, continue to the end of the stream
			} catch (Exception e) {
				throw new RuntimeException(e);
			}

		});

		return returnData;
	}	
	
	/**
	 * 
	 * @param lottoType
	 * @return
	 */
	public int numberOfDrawnTicketsTobeUpdated(LotteryType lottoType) throws Exception {
		
		String[] drawDates = getDrawingDaysForLottoryType(lottoType);
		
		final int[] numDrawnsNeeded = new int[] {0};
		
		this.retrieveLastDrawnTicketsFor(1, lottoType).forEach(ticketInfo -> {
			numDrawnsNeeded[0] = TicketUtils.numberOfDrawnDatesFrom(ticketInfo.getDate().plusDays(1), LocalDate.now(), isDrawnDateDaily(drawDates) ? null : drawDates);
		});
		
		return numDrawnsNeeded[0];
	}
	
	
}
