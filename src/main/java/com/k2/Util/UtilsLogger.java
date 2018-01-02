package com.k2.Util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This static class provides logging facilities for utility classes
 * 
 * @author simon
 *
 */
public class UtilsLogger {
	
	/**
	 * The utilities logger
	 */
	private static Logger logger = Logger.getAnonymousLogger();
	/**
	 * This method returns the current utilities logger
	 * @return	The current utilities logger
	 */
	public static Logger logger() { return logger; }
	/**
	 * This method set a new logger as the utilities logger
	 * 
	 * @param newLogger	The new utilities logger
	 */
	public static void logger(Logger newLogger) { logger = newLogger; }
	/**
	 * A convenience method for logging a thrown exception as an error with a custom message
	 * @param msg	The custom log message
	 * @param e	The thrown exception giving rise to the logged error
	 */
	public static void error(String msg, Throwable e) { logger.log(Level.SEVERE, msg, e);}
	/**
	 * A convenience method for logging a thrown exception as a warning with a custom message
	 * @param msg	The custom log message
	 * @param e	The thrown exception
	 */
	public static void warning(String msg, Throwable e) { logger.log(Level.WARNING, msg, e);}
	/**
	 * A convenience method for logging a severe log message
	 * @param msg	The log message
	 */
	public static void severe(String msg) { logger.severe(msg);}
	/**
	 * A convenience method for logging a warning log message
	 * @param msg	The log message
	 */
	public static void warning(String msg) { logger.warning(msg);}
	/**
	 * A convenience method for logging an info log message
	 * @param msg	The log message
	 */
	public static void info(String msg) { logger.info(msg);}
	/**
	 * A convenience method for logging a fine log message
	 * @param msg	The log message
	 */
	public static void fine(String msg) { logger.fine(msg);}
	/**
	 * A convenience method for logging a finer log message
	 * @param msg	The log message
	 */
	public static void finer(String msg) { logger.finer(msg);}
	/**
	 * A convenience method for logging a finest log message
	 * @param msg	The log message
	 */
	public static void finest(String msg) { logger.finest(msg);}


}
