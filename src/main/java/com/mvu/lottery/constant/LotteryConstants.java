package com.mvu.lottery.constant;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mvu.lottery.util.StringUtils;
import com.mvu.lottery.util.StringUtils.StringTemplate;

public interface LotteryConstants {
	String DATE_TICKET_DRAWN_FORMAT_PATTERN = "MMM d, yyyy";
    int NUM_TICKETS_MAINTAINED_IN_FILE = 200; //Maintain 200 lines;	
    String RECORD_FORMAT_WITH_MEGA = "%s - %s\t%s\t%s";
    String RECORD_FORMAT_WITHOUT_MEGA = "%s - %s\t%s";
    
    String DAILY = "Daily";

    
    enum LotteryType {
    	SUPERLOTTO,
    	FANTASY5,
    	POWERBALL,
    	MEGAMILLION
    	
    };
    
    
    //String lastDrawnNumberApiUrl  = "http://www.calottery.com/api/DrawGameApi/DrawGamePastDrawResults/{lotteryTypeCode}/1/{NumberOfLastDrawn}";
    
    Map<LotteryType, Integer> lottertTypeCodeMap = Stream.of(
    		new Object[][] {
    			{LotteryType.SUPERLOTTO, 8},
    			{LotteryType.POWERBALL, 12},
    			{LotteryType.FANTASY5, 10},
    			{LotteryType.MEGAMILLION, 15},
    		
    		
    		}
    		
    		).collect(Collectors.toMap(data -> (LotteryType) data[0], data -> (Integer) data[1]));
    		
    		; 
    
    
    
    static Map<LotteryType, String[]> drawnDateMap = Stream.of(
    		new Object[][] {
    			{LotteryType.SUPERLOTTO, new String[] {"Wednesday", "Saturday"}},
    			{LotteryType.POWERBALL, new String[] {"Wednesday", "Saturday"}},
    			{LotteryType.FANTASY5, new String[] {DAILY}},
    			{LotteryType.MEGAMILLION, new String[] {"Tuesday", "Friday"}},
    		
    		
    		}
    		
    		).collect(Collectors.toMap(data -> (LotteryType) data[0], data -> (String[]) data[1]));
    		
    		; 
    
    //Action status indicator
    enum ActionStatus {
    	PENDING ("pending"),
    	SUCCESS ("success"),
    	FAILURE ("failure");
    	
    	private String asString;
    	
    	ActionStatus(String asString) {
    		this.asString = asString;
    	}
    	
    	public String getAsString() {
    		
    		return this.asString;
    		
    	}
    	
    	
    	
    }
    
    /**
     * 
     * @param lotteryType
     * @return
     */
    default public String lotterNameFromLotteryType(LotteryType lotteryType) {
    		
    	String retName = "";
    	
    	switch(lotteryType) {
    		case SUPERLOTTO:
    			retName = "superlotto";
    			break;
    		case POWERBALL:
    			retName = "powerball";
    			break;
    		case FANTASY5:
    			retName = "fantasy5";
    			break;
    		case MEGAMILLION:
    			retName = "megamillion";
    			break;
    	}
    	
    	return retName;
    }
    
    /**
     * 
     * @param name
     * @return
     */
    default public LotteryType getLotteryTypeFromName(final String name) {
    	
    	LotteryType retValue = null;
    	
    	switch(name.toLowerCase()) {
	    	case "superlotto" : {
	    		retValue = LotteryType.SUPERLOTTO;
	    		break;
	    	}
	    	case "fantasy5" : {
	    		retValue = LotteryType.FANTASY5;
	    		break;
	    	}
	    	case "powerball" : {
	    		retValue = LotteryType.POWERBALL;
	    		break;
	    	}
	    	case "megamillion" : {
	    		retValue = LotteryType.MEGAMILLION;
	    		break;
	    	}
    	}
    	System.out.println(">>>>>name passed:"+name);
    	return retValue;
    }
    
    
    public enum AuthorityType {
    	ROLE_ADMIN ("ROLE_ADMIN"),
    	ROLE_USER ("ROLE_USER");
    	
    	final private String name;
    	
    	AuthorityType(String name) {
    		this.name = name;
    	}
    	
    	public String toString() {
    		return this.name;
    	}
    }
    
    /**
     * Return the drawing days of the lotto
     * @param lottoType
     * @return
     */
    default public String[] getDrawingDaysForLottoryType(LotteryType lottoType) {
    	
    	return drawnDateMap.get(lottoType);
    }
    
    /**
     * 
     * @param lotteryType
     * @param numberOfDrawn
     * @return
     */
    default public String getLastDrawnNumberApiUrl(LotteryType lotteryType, int numberOfDrawn, String lotteryLastDrawnApiUrl) {
    	return StringUtils.createStringTeamplate()
    								.markerAndValue("lotteryTypeCode", String.valueOf(lottertTypeCodeMap.get(lotteryType)))
    								
    								.markerAndValue("NumberOfLastDrawn", String.valueOf(numberOfDrawn))
    								.format(lotteryLastDrawnApiUrl)
    								;
    }
    
    /**
     * 
     * @param drawnDates
     * @return
     */
    default public boolean isDrawnDateDaily(String[] drawnDates) {
    	return Stream.of(drawnDates).anyMatch(date -> {
    		return date.equals(DAILY); 
    	});
    }
}
