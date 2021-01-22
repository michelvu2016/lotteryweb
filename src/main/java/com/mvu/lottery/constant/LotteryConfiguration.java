package com.mvu.lottery.constant;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.mvu.lottery.data.LastDrawnTicketData;

@Component()
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@PropertySource(value = {"classpath:externalprocess.properties"})
public class LotteryConfiguration implements LotteryConstants {
	//------- Process property -----------
	
	@Value("${process.commandline.format}")
	private String processCommandLineFormat;
	
	public String getProcessCommandLineFormat() {
		return processCommandLineFormat;
	}

	@Value("${process.workingdir}")
	private String workingDir;
	
	@Value("${lastDrawnNumberApiUrl}")
	private String lastDrawnNumberApiUrl;
	
	public String getLastDrawnNumberApiUrl() {
		return lastDrawnNumberApiUrl;
	}

	public String getWorkingDir() {
		return workingDir;
	}

	

	//------------------- Lottery Drawn data ------------------
	@Value("${datafile.path.superlotto}")
	private String superlottoDataFile;

	@Value("${datafile.path.fantasy5}")
	private String fantasy5DataFile;
	
	@Value("${datafile.path.powerball}")
	private String powerballDataFile;
	
	@Value("${datafile.path.megamillion}")
	private String megamillionDataFile;
	
	//----------------- Analyzed data ---------//
	@Value("${analyzeddata.path.superlotto}")
	private String superlottoAnalyzedDataFile;
	
	@Value("${analyzeddata.path.fantasy5}")
	private String fantasyAnalyzedDataFile;
	
	@Value("${analyzeddata.path.powerball}")
	private String powerballAnalyzedDataFile;
	
	@Value("${analyzeddata.path.megamillion}")
	private String megamillionAnalyzedDataFile;
	
	private Map<LotteryType, String> dataFileMap = new HashMap<>();
	
	private Map<LotteryType, String> analyzedDataMap = new HashMap<>();
	
	private Map<LotteryType, DayOfWeek[]> drawnDatesLotteryTypeMap = new HashMap<>();
	
	private static DayOfWeek[] drawnDaySuperLotto = { DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY };
	private static DayOfWeek[] drawnDayPowerBall = { DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY };
	private static DayOfWeek[] drawnDayFantasy5 = { DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
			DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY };
	private static DayOfWeek[] drawnDayMegaMillion = { DayOfWeek.TUESDAY, DayOfWeek.FRIDAY };
	
	//------------------ target resource data file mapping ---------------//
	public LotteryConfiguration() {
		
		
	}
	
	@PostConstruct
	private void setupMapping() {
		this.dataFileMap.put(LotteryType.FANTASY5, this.fantasy5DataFile);
		this.dataFileMap.put(LotteryType.SUPERLOTTO, this.superlottoDataFile);
		this.dataFileMap.put(LotteryType.POWERBALL, this.powerballDataFile);
		this.dataFileMap.put(LotteryType.MEGAMILLION, this.megamillionDataFile);
		
		
		this.analyzedDataMap.put(LotteryType.FANTASY5, this.fantasyAnalyzedDataFile);
		this.analyzedDataMap.put(LotteryType.SUPERLOTTO, this.superlottoAnalyzedDataFile);
		this.analyzedDataMap.put(LotteryType.POWERBALL, this.powerballAnalyzedDataFile);
		this.analyzedDataMap.put(LotteryType.MEGAMILLION, this.megamillionAnalyzedDataFile);
		
		
		this.drawnDatesLotteryTypeMap.put(LotteryType.SUPERLOTTO, drawnDaySuperLotto);
		this.drawnDatesLotteryTypeMap.put(LotteryType.POWERBALL, drawnDayPowerBall);
		this.drawnDatesLotteryTypeMap.put(LotteryType.FANTASY5, drawnDayFantasy5);
		this.drawnDatesLotteryTypeMap.put(LotteryType.MEGAMILLION, drawnDayMegaMillion);
		
	}
	
	/**
	 * Return the data path for the game type
	 * @param forType
	 * @return
	 */
	public Path getDataPathForType(LotteryType forType) {
		
		return Paths.get(this.dataFileMap.get(forType));
	}
	
	/**
	 * Return the analyzed data path for the game type
	 * @param forType
	 * @return
	 */
	public Path getAnalyzedDataPathForType(LotteryType forType) {
		
		return Paths.get(this.analyzedDataMap.get(forType));
	}
	
	
	/**
	 * 
	 * @return
	 */
	public Path getSuperLottoDataPath() {
		return Paths.get(this.superlottoDataFile);
	}
	
	/**
	 * 
	 * @return
	 */
	public Path getFantasy5DataPath() {
		return Paths.get(this.fantasy5DataFile);
	}
	
	/**
	 * 
	 * @return
	 */
	public Path getPowerballDataPath() {
		return Paths.get(this.powerballDataFile);
	}
	
	
	/**
	 * 
	 * @return
	 */
	public Path getMegamillionDataPath() {
		return Paths.get(this.megamillionDataFile);
	}
	
	/**
	 * Return the drawn dates for the lottery type
	 * @param lotteryType
	 * @return
	 */
	public DayOfWeek[] getDrawnDates(final LotteryType lotteryType) {
		return this.drawnDatesLotteryTypeMap.get(lotteryType);
	}
	
	
}