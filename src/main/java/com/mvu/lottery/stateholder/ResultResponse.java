package com.mvu.lottery.stateholder;

public class ResultResponse {
	private String result;

	private String status;
	
	private String baton;
	
	private Object data;
	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public static ResultResponse success() {
		ResultResponse resp = new ResultResponse();
		resp.setResult("Operation successful");
		resp.status = "success";
		return resp;
	}
	
	public static ResultResponse pending() {
		ResultResponse resp = new ResultResponse();
		resp.setResult("Operation pending");
		resp.status = "pending";
		return resp;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBaton() {
		return baton;
	}

	public void setBaton(String baton) {
		this.baton = baton;
	}

	/**
	 * Return the failed ResultResponse
	 * @param msg
	 * @return
	 */
	public static ResultResponse failed(String msg) {
		ResultResponse resp = new ResultResponse();
		resp.setResult(msg);
		resp.status = "faiure";
		return resp;
	}

	/**
	 * Create and return the ResultResponse from the state holder objectg
	 * @param stateHolder
	 * @return
	 */
	public static ResultResponse fromStateHolder(AsynchDataStateHolder stateHolder) {
		ResultResponse resp = new ResultResponse();
		resp.status = stateHolder.getStatus().getAsString();
		resp.baton = stateHolder.getBaton();
		resp.data = stateHolder.getData();
		return resp;
	}

	@Override
	public String toString() {
		return "ResultResponse [result=" + result + ", status=" + status + ", baton=" + baton + ", data=" + data + "]";
	}

}
