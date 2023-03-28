package com.forexapp.exception;

public class HoldingNotFoundException extends RuntimeException{

	public HoldingNotFoundException() {
	}

	public HoldingNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public HoldingNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public HoldingNotFoundException(String message) {
		super(message);
	}

	public HoldingNotFoundException(Throwable cause) {
		super(cause);
	}

}
