package com.googlecode.sobat.exceptions;

public class NotMappedException extends PersistenceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotMappedException() {
	}

	public NotMappedException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotMappedException(String message) {
		super(message);
	}

	public NotMappedException(Throwable cause) {
		super(cause);
	}

}
