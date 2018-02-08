package net.expvp.api.bukkit.exceptions;

/**
 * Throws if the player is offline but needs to be online
 * 
 * @author NullUser
 * @see Exception
 */
public class PlayerOfflineException extends Exception {

	private static final long serialVersionUID = -5575152919601020364L;

	/**
	 * @see Exception
	 * @param message
	 */
	public PlayerOfflineException(String message) {
		super(message);
	}

}
