package com.mvu.lottery.data;

public interface LotteryTicket {

	String getDate();

	void setDate(String date);

	String getSeqNum();

	void setSeqNum(String seqNum);

	String[] getNumbers();

	void setNumbers(String[] numbers);

	String getMega();

	void setMega(String mega);

}