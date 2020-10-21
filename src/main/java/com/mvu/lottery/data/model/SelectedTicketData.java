package com.mvu.lottery.data.model;

import java.util.Arrays;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = com.mvu.lottery.customserializer.SelectedTicketDeserializer.class)
public class SelectedTicketData {
	public static class Ticket {
		private String mega;
		private String[] number;
		public String getMega() {
			return mega;
		}
		public void setMega(String mega) {
			this.mega = mega;
		}
		public String[] getNumber() {
			return number;
		}
		public void setNumber(String[] number) {
			this.number = number;
		}
		@Override
		public String toString() {
			return "Ticket [mega=" + mega + ", number=" + Arrays.toString(number) + "]";
		}
		
	}
	
  public static class TicketSet {
	  private Ticket[] numberList;

	public Ticket[] getnumberList() {
		return numberList;
	}

	public void setnumberList(Ticket[] numberList) {
		this.numberList = numberList;
	}
	


	@Override
	public String toString() {
		return "TicketSet [numberList=" + Arrays.toString(numberList) + "]";
	}
  
  
  }

	private Date drawingDate;
	private TicketSet ticketSet;
	
	public SelectedTicketData() {
		// TODO Auto-generated constructor stub
	}

	
	
	public Date getDrawingDate() {
		return drawingDate;
	}



	public void setDrawingDate(Date drawingDate) {
		this.drawingDate = drawingDate;
	}



	public TicketSet getTicketSet() {
		return ticketSet;
	}



	public void setTicketSet(TicketSet ticketSet) {
		this.ticketSet = ticketSet;
	}




	@Override
	public String toString() {
		return "SelectedTicketData [drawingDate=" + drawingDate + ", ticketSet=" + ticketSet + "]";
	}





	
	
}
