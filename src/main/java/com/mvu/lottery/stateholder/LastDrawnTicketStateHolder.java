package com.mvu.lottery.stateholder;

import java.time.LocalDate;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mvu.lottery.util.LocalDateToStringConverter;
import com.mvu.lottery.util.StringToLocalDateConverter;

@JsonFilter("filteronmega")
public class LastDrawnTicketStateHolder {
	
	@JsonSerialize(converter=LocalDateToStringConverter.class)
	@JsonDeserialize(converter=StringToLocalDateConverter.class)
	private LocalDate date;
	private String seqNum;	
	private String[] numbers;
	private String mega;
	
	
	public LastDrawnTicketStateHolder() {}
	
	
	public LastDrawnTicketStateHolder(final LocalDate date, final String seqNum, final String[] numbers, final String mega) {
		
		this.date = date;
		this.seqNum = seqNum;
		this.numbers = numbers;
		this.mega = mega;
	}
	

	
	
	


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((mega == null) ? 0 : mega.hashCode());
		result = prime * result + Arrays.hashCode(numbers);
		result = prime * result + ((seqNum == null) ? 0 : seqNum.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LastDrawnTicketStateHolder other = (LastDrawnTicketStateHolder) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (mega == null) {
			if (other.mega != null)
				return false;
		} else if (!mega.equals(other.mega))
			return false;
		if (!Arrays.equals(numbers, other.numbers))
			return false;
		if (seqNum == null) {
			if (other.seqNum != null)
				return false;
		} else if (!seqNum.equals(other.seqNum))
			return false;
		return true;
	}


	public String getSeqNum() {
		return seqNum;
	}
	public void setSeqNum(String seqNum) {
		this.seqNum = seqNum;
	}

	
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public String[] getNumbers() {
		return numbers;
	}
	public void setNumbers(String[] numbers) {
		this.numbers = numbers;
	}
	public String getMega() {
		return mega;
	}
	public void setMega(String mega) {
		this.mega = mega;
	}


	@Override
	public String toString() {
		return "LastDrawnTicketStateHolder [date=" + date + ", seqNum=" + seqNum + ", numbers="
				+ Arrays.toString(numbers) + ", mega=" + mega + "]";
	}
	
	
}
