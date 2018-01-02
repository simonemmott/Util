package com.k2.Util.exceptions;

import java.io.File;

/**
 * This exception is thrown when a file has been locked using java's FileLock API and an application attempts to lock the file.
 * 
 * @author simon
 *
 */
public class FileLockedException extends Exception {

	private static final long serialVersionUID = -6658824095995737120L;

	/**
	 * Create a FileLockedExcpetion for the given file
	 * @param f	The file that is locked
	 */
	public FileLockedException(File f) {
		super("The file '"+f.getAbsolutePath()+"' is already locked!");
	}
	/**
	 * Create a FileLockedException for the given file in response to the given throwable cause.
	 * @param f	The file that is locked
	 * @param cause	The exception that was thrown identifying the file as locked.
	 */
	public FileLockedException(File f, Throwable cause) {
		super("The file '"+f.getAbsolutePath()+"' is already locked!", cause);
	}

	/**
	 * Create a FileLockedException for the given file in response to the given throwable cause 
	 * controlling whether the exception can be suppressed and whether the stack trace is writable.
	 * @param f	The file that is locked
	 * @param cause	The exception that identified the file as locked
	 * @param enableSuppression	True if the exception can be supressed
	 * @param writableStackTrace	True if the exception stack trace is writable
	 */
	public FileLockedException(File f, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super("The file '"+f.getAbsolutePath()+"' is already locked!", cause, enableSuppression, writableStackTrace);
	}

}
