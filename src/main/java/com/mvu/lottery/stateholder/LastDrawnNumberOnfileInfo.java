package com.mvu.lottery.stateholder;

import java.util.List;

public class LastDrawnNumberOnfileInfo {

	private List<LastDrawnTicketStateHolder> listOfTicketHolders;
	private int numOfDrawnsNeeded;
	private String dataRetrievalUrl;
	
	public String getDataRetrievalUrl() {
		return dataRetrievalUrl;
	}

	public void setDataRetrievalUrl(String dataRetrievalUrl) {
		this.dataRetrievalUrl = dataRetrievalUrl;
	}

	public LastDrawnNumberOnfileInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public LastDrawnNumberOnfileInfo(List<LastDrawnTicketStateHolder> listOfTicketHolders, int numDrawnNeeded) {
		this.listOfTicketHolders = listOfTicketHolders;
		this.numOfDrawnsNeeded = numDrawnNeeded;
	}

	public List<LastDrawnTicketStateHolder> getListOfTicketHolders() {
		return listOfTicketHolders;
	}

	public void setListOfTicketHolders(List<LastDrawnTicketStateHolder> listOfTicketHolders) {
		this.listOfTicketHolders = listOfTicketHolders;
	}

	public int getNumOfDrawnsNeeded() {
		return numOfDrawnsNeeded;
	}

	public void setNumOfDrawnsNeeded(int numOfDrawnsNeeded) {
		this.numOfDrawnsNeeded = numOfDrawnsNeeded;
	}
	
	

}
