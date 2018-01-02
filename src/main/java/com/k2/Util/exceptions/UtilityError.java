package com.k2.Util.exceptions;

/**
 * This Error class is the root of all unchecked errors defined by the k2 utilities
 * 
 * @author simon
 *
 */
public class UtilityError extends Error {

	private static final long serialVersionUID = -1782268655017879994L;

	/**
	 * Create a utility error with the given message
	 * @param message	The error message
	 */
	public UtilityError(String message) {
		super(message);
	}
	/**
	 * Create a utility error for the given throwable cause
	 * @param cause	The throwable that gave rise to this error
	 */
	public UtilityError(Throwable cause) {
		super(cause);
	}
	/**
	 * Create a utility error for the given message and throwable cause
	 * @param message	The error message
	 * @param cause	The throwable cause
	 */
	public UtilityError(String message, Throwable cause) {
		super(message, cause);
	}
	/**
	 * Create a utility error for the given message and throwable cause defining whether the error can be suppressed and
	 * Whether the stack trace should be writable. 
	 * @param message	The error message
	 * @param cause	The throwable cause
	 * @param enableSuppression	True if the error can be suppressed
	 * @param writableStackTrace	True if the stack trace should be writable
	 */
	public UtilityError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
