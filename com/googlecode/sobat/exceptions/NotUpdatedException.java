package com.googlecode.sobat.exceptions;

public class NotUpdatedException extends PersistenceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotUpdatedException() {
	}

	public NotUpdatedException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotUpdatedException(String message) {
		super(message);
	}

	public NotUpdatedException(Throwable cause) {
		super(cause);
	}

}
