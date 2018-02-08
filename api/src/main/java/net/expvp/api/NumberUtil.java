package net.expvp.api;

public final class NumberUtil {

	/**
	 * @param toTest
	 *            Tested string
	 * @return true if tested string is an integer
	 */
	public static boolean isInteger(String toTest) {
		try {
			Integer.parseInt(toTest);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	/**
	 * @param parsed
	 *            String to turn into an integer
	 * @return integer made from parsed string
	 */
	public static int getInteger(String parsed) {
		try {
			return Integer.parseInt(parsed);
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
			return -1;
		}
	}

	/**
	 * @param toTest
	 *            Tested string
	 * @return true if tested string is a double
	 */
	public static boolean isDouble(String toTest) {
		try {
			Double.parseDouble(toTest);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	/**
	 * @param parsed
	 *            String to turn into a double
	 * @return double made from parsed string
	 */
	public static double getDouble(String parsed) {
		try {
			return Double.parseDouble(parsed);
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
			return -1;
		}
	}

	/**
	 * @param toTest
	 *            Tested string
	 * @return true if tested string is a float
	 */
	public static boolean isFloat(String toTest) {
		try {
			Float.parseFloat(toTest);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}

	/**
	 * @param parsed
	 *            String to turn into a float
	 * @return float made from parsed string
	 */
	public static Float getFloat(String parsed) {
		try {
			return Float.parseFloat(parsed);
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
			return 1F;
		}
	}

}
