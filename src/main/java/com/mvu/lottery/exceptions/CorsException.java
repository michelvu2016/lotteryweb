package com.mvu.lottery.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus (
		value = HttpStatus.NO_CONTENT
		)
public class CorsException extends RuntimeException {

	public CorsException() {
		// TODO Auto-generated constructor stub
	}

	public CorsException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public CorsException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public CorsException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public CorsException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

}
