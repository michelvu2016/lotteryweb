package com.mvu.lottery.data;

import java.time.LocalDate;

public class LotteryTicketImpl implements LotteryTicket {

	private String date;
	private String seqNum;	
	private String[] numbers;
	private String mega;
	
	
	public LotteryTicketImpl() {
		// TODO Auto-generated constructor stub
	}

	
	/* (non-Javadoc)
	 * @see com.mvu.lottery.data.LotteryTicket#getDate()
	 */
	@Override
	public String getDate() {
		return date;
	}


	/* (non-Javadoc)
	 * @see com.mvu.lottery.data.LotteryTicket#setDate(java.lang.String)
	 */
	@Override
	public void setDate(String date) {
		this.date = date;
	}


	/* (non-Javadoc)
	 * @see com.mvu.lottery.data.LotteryTicket#getSeqNum()
	 */
	@Override
	public String getSeqNum() {
		return seqNum;
	}


	/* (non-Javadoc)
	 * @see com.mvu.lottery.data.LotteryTicket#setSeqNum(java.lang.String)
	 */
	@Override
	public void setSeqNum(String seqNum) {
		this.seqNum = seqNum;
	}


	/* (non-Javadoc)
	 * @see com.mvu.lottery.data.LotteryTicket#getNumbers()
	 */
	@Override
	public String[] getNumbers() {
		return numbers;
	}


	/* (non-Javadoc)
	 * @see com.mvu.lottery.data.LotteryTicket#setNumbers(java.lang.String[])
	 */
	@Override
	public void setNumbers(String[] numbers) {
		this.numbers = numbers;
	}


	/* (non-Javadoc)
	 * @see com.mvu.lottery.data.LotteryTicket#getMega()
	 */
	@Override
	public String getMega() {
		return mega;
	}


	/* (non-Javadoc)
	 * @see com.mvu.lottery.data.LotteryTicket#setMega(java.lang.String)
	 */
	@Override
	public void setMega(String mega) {
		this.mega = mega;
	}



}
