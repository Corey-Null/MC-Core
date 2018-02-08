package net.expvp.core.ip;

import java.util.regex.Pattern;

import net.expvp.api.NumberUtil;
import net.expvp.api.interfaces.ip.IAddress;

/**
 * Class used for handling an IP address
 * 
 * @author NullUser
 * @see Bannable
 */
public class IPAddress implements IAddress {

	private final static Pattern PATTERN = Pattern.compile("\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b");
	private final String ipAddress;

	public IPAddress(String ip) {
		if (!isIP(ip)) {
			throw new SecurityException("The ip " + ip + " does not match standard format.");
		}
		this.ipAddress = ip;
	}

	/**
	 * @return the ipAddress
	 */
	public String getAddress() {
		return ipAddress;
	}

	/**
	 * Overrides Object toString to replace it with the formatted version of the
	 * IP
	 */
	@Override
	public String toString() {
		return ipAddress;
	}

	/**
	 * Overrides Object equals to replace it with own method of comparing
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof String) {
			return ((String) obj).equalsIgnoreCase(ipAddress);
		} else if (obj instanceof IPAddress) {
			return ((IPAddress) obj).getAddress().equalsIgnoreCase(ipAddress);
		} else {
			return super.equals(obj);
		}
	}

	/**
	 * Returns a valued hash code
	 */
	@Override
	public int hashCode() {
		String[] parts = ipAddress.split("\\.");
		int[] parsed = new int[] { NumberUtil.getInteger(parts[0]), NumberUtil.getInteger(parts[1]),
				NumberUtil.getInteger(parts[2]), NumberUtil.getInteger(parts[3]) };
		int hash = parsed[0] + parsed[1] + parsed[2] + parsed[3];
		hash = hash >>> parsed[0] * 0xFFEEE;
		hash = hash >> parsed[1] * 0xFEFFE;
		hash = hash << parsed[2];
		hash = hash << parsed[3];
		return hash;
	}

	/**
	 * @param test
	 *            To test
	 * @return true if the tested string is in the format of an IP
	 */
	public static boolean isIP(String test) {
		return PATTERN.matcher(test).find();
	}

}
