package com.mvu.lottery.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.mvu.lottery.data.LotteryTicketImpl;
import com.mvu.lottery.stateholder.LastDrawnTicketStateHolder;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class LotteryTicketToLastDrawnTicketStateHolder implements Converter<LotteryTicketImpl, LastDrawnTicketStateHolder> {

	@Autowired
	private StringToLocalDateConverter converter;
	
	public LotteryTicketToLastDrawnTicketStateHolder() {
		
	}

	/**
	 * Return the instance of {@link LastDrawnTicketStateHolder}
	 */
	@Override
	public LastDrawnTicketStateHolder convert(LotteryTicketImpl source) {
		LastDrawnTicketStateHolder stateHolder = new LastDrawnTicketStateHolder();
		stateHolder.setDate(
				converter.convert(
						source.getDate()
						)
				);
		stateHolder.setMega(source.getMega());
		stateHolder.setNumbers(source.getNumbers());
		stateHolder.setSeqNum(source.getSeqNum());
		
		
		return stateHolder;
		
		
		
		
	}

}
