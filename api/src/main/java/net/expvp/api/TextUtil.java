package net.expvp.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;

/**
 * Util class for text
 * 
 * @author NullUser
 */
public final class TextUtil {

	private final static Pattern quotePattern = Pattern.compile("([\"'])(?:(?=(\\\\?))\\2.)*?\\1");
	private final static Pattern alphaNumeric = Pattern.compile("[^a-zA-Z0-9]");
	
	public static boolean isAlphaNumeric(String string) {
		return !alphaNumeric.matcher(string).find();
	}

	/**
	 * @param string
	 *            To read quotes from
	 * @return The selection inside of the quotes
	 */
	public static String loadQuotes(String string) {
		Matcher matcher = quotePattern.matcher(string);
		if (!matcher.find()) {
			return " ";
		}
		String inQuote = matcher.group();
		inQuote = inQuote.substring(1, inQuote.length() - 1);
		return inQuote;
	}

	/**
	 * @param string
	 *            To color
	 * @return the colored string
	 */
	public static String color(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	/**
	 * @param array
	 *            To read from
	 * @param start
	 *            Position of start of string
	 * @param end
	 *            Position of end of string
	 * @return connected string
	 */
	public static String connect(String[] array, int start, int end) {
		if (start > array.length || end > array.length || end < 0 || start < 0) {
			return null;
		}
		if (start > end) {
			int temp = start;
			start = end;
			end = temp;
		}
		StringBuilder builder = new StringBuilder("");
		for (int i = start; i < end; i++) {
			builder.append(array[i]);
			if (i < end) {
				builder.append(' ');
			}
		}
		return builder.toString();
	}

}
