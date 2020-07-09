package com.mvu.lottery.util;

import javax.inject.Inject;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.mvu.lottery.data.LotteryTicket;
import com.mvu.lottery.data.LotteryTicketImpl;
import com.mvu.lottery.stateholder.LastDrawnTicketStateHolder;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class LastDrawnTicketStateHolderToLotteryTicket implements Converter<LastDrawnTicketStateHolder, LotteryTicket> {

	
	
	@Inject
	private LocalDateToStringConverter localDateConverter;
	
	
	public LastDrawnTicketStateHolderToLotteryTicket() {
		
	}


	@Override
	public LotteryTicket convert(LastDrawnTicketStateHolder source) {
		
		LotteryTicket ticket = new LotteryTicketImpl();
		ticket.setDate(this.localDateConverter.convert(source.getDate()));
		ticket.setMega(source.getMega());
		ticket.setNumbers(source.getNumbers());
		ticket.setSeqNum(source.getSeqNum());
		return ticket;
	}

}
