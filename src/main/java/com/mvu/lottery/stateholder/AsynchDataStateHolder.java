package com.mvu.lottery.stateholder;

import com.mvu.lottery.constant.LotteryConstants;

public class AsynchDataStateHolder implements LotteryConstants {

	@Override
	public String toString() {
		return "AsynchDataStateHolder [status=" + status + ", data=" + data + ", baton=" + baton + "]";
	}

	private ActionStatus status;
	private Object data;
	private String baton;
	
	public AsynchDataStateHolder() {
		
	}

	//Factory methods
	
	public static AsynchDataStateHolder createWithBaton(String baton) {
		AsynchDataStateHolder stateHolder = new AsynchDataStateHolder();
		stateHolder.baton = baton;
		stateHolder.status = ActionStatus.PENDING;
		return stateHolder;
	}
	
	public static AsynchDataStateHolder createWithData(Object data) {
		AsynchDataStateHolder stateHolder = new AsynchDataStateHolder();
		stateHolder.data = data;
		stateHolder.status = ActionStatus.SUCCESS;
		return stateHolder;
	}
	
	public static AsynchDataStateHolder createDefault() {
		AsynchDataStateHolder stateHolder = new AsynchDataStateHolder();
		stateHolder.status = ActionStatus.FAILURE;
		return stateHolder;
	}
	
	
	public ActionStatus getStatus() {
		return status;
	}

	public void setStatus(ActionStatus status) {
		this.status = status;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getBaton() {
		return baton;
	}

	public void setBaton(String baton) {
		this.baton = baton;
	}
	
	

}
