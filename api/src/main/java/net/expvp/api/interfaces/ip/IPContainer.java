package net.expvp.api.interfaces.ip;

import java.util.Set;

/**
 * Class used for handling classes which contain IP addresses
 * 
 * @author NullUser
 */
public interface IPContainer {

	/**
	 * @return All past IP addresses
	 */
	Set<IAddress> getPastIPAddresses();

	/**
	 * @return current IP address
	 */
	IAddress getCurrentIPAddress();

	/**
	 * @param ip
	 *            To set the current IP address
	 */
	void setCurrentIPAddress(IAddress ip);

	/**
	 * @param addresses
	 *            To init the past addresses
	 */
	void initPastIPAddresses(Set<IAddress> addresses);

}
