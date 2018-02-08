package net.expvp.api;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Util class for tracking time
 * 
 * @author NullUser
 */
public final class TimeUtil {

	private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd 'at' h:m:s");
	private final static SimpleDateFormat FILE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * Formats the specified date into a readable string
	 * 
	 * @param time
	 *            To format
	 * @param extended
	 *            To determine which format to use
	 * @return the formatted date
	 */
	public static String formatDate(long time, boolean extended) {
		Date date = new Date(time);
		return extended ? DATE_FORMAT.format(date) : FILE_DATE_FORMAT.format(date);
	}

	/**
	 * @param start
	 * @param end
	 * @return formatted time between the start and end
	 */
	public static String getTime(long start, long end) {
		long time = end - start;
		if (time <= 0) {
			return "-1";
		}
		StringBuilder builder = new StringBuilder();
		long seconds = time / 1000;
		long minutes = seconds / 60;
		seconds = seconds - (minutes * 60);
		long hours = minutes / 60;
		minutes = minutes - (hours * 60);
		long days = hours / 24;
		hours = hours - (days * 24);
		long weeks = days / 7;
		days = days - (weeks * 7);
		append(builder, seconds, "Seconds");
		append(builder, minutes, "Minutes");
		append(builder, hours, "Hours");
		append(builder, days, "Days");
		append(builder, weeks, "Weeks");
		return builder.toString();
	}

	/**
	 * Appends a string with time to a builder correctly
	 * 
	 * @param builder
	 * @param time
	 * @param type
	 */
	private final static void append(StringBuilder builder, long time, String type) {
		if (time <= 0) {
			return;
		}
		if (builder.length() > 0) {
			builder.append(' ');
		}
		builder.append(time).append(' ').append(type);
	}

}
