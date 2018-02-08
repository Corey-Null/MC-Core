package net.expvp.api.interfaces.logging;

import java.io.File;

import net.expvp.api.interfaces.Saveable;

/**
 * Class used for handling custom logging
 * 
 * @author NullUser
 * @see Saveable
 */
public interface ILogger extends Saveable {

	/**
	 * @return the latest log file
	 */
	File getLatest();

	/**
	 * @return the containing folder
	 */
	File getFolder();

	/**
	 * Zips the latest log and replaces it with a fresh log
	 */
	void zipAndReplace();

	/**
	 * Logs a message into the file
	 * 
	 * @param message
	 *            To log
	 */
	void log(String message);

	/**
	 * Logs a message into the file as a warning
	 * 
	 * @param message
	 *            To log
	 */
	void warn(String message);

	/**
	 * Logs a message into the file as an error
	 * 
	 * @param message
	 *            To log
	 */
	void error(String message, Exception ex);

	/**
	 * @return the date and time in a string format
	 */
	String getDateAndTime();

}
