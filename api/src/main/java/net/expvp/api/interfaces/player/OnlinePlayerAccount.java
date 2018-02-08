package net.expvp.api.interfaces.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.expvp.api.Pair;
import net.expvp.api.bukkit.exceptions.PlayerOfflineException;

/**
 * Online version of the player account
 * 
 * @author NullUser
 * @see OfflinePlayerAccount
 */
public interface OnlinePlayerAccount extends OfflinePlayerAccount {

	/**
	 * @return the player
	 */
	Player getPlayer();

	/**
	 * Logs out of the account
	 */
	void logout();

	/**
	 * @return last login time
	 */
	long getLastLoginTime();

	/**
	 * @return last location
	 */
	Location getLastLocation();

	/**
	 * @param loc
	 *            To set the last location
	 */
	void updateLastLocation(Location loc);

	/**
	 * Teleports the account
	 * 
	 * @param loc
	 *            To teleport to
	 * @return true if the teleport was successful
	 */
	boolean teleport(Location loc);

	/**
	 * @return account to reply to
	 */
	OfflinePlayerAccount getReplyTo();

	/**
	 * @param account
	 *            To set account to reply to
	 */
	void updateReplyTo(OfflinePlayerAccount account);

	/**
	 * Used to message a target player account
	 * 
	 * @param target
	 *            To message
	 * @param message
	 *            The message
	 * @return true if the message is successful along with the message string
	 * @throws PlayerOfflineException
	 */
	Pair<String, Boolean> message(OfflinePlayerAccount target, String message) throws PlayerOfflineException;

	/**
	 * @return the offline account
	 */
	OfflinePlayerAccount getOfflineAccount();

	/**
	 * Sends the player attached to the account a message
	 * 
	 * @param message
	 *            To send
	 */
	void sendMessage(String message);

	/**
	 * Attaches permissions to the player
	 */
	void attachPermissions();

	/**
	 * Feeds the player
	 */
	void feed();

	/**
	 * Heals the player
	 */
	void heal();

	/**
	 * Puts the player into godmode
	 */
	void god();

	/**
	 * @return true if the player is in god mode
	 */
	boolean isGodMode();

	/**
	 * Toggles flight of player
	 */
	void toggleFlight();

	/**
	 * Sets flight of player
	 * 
	 * @param fly
	 *            To determine if player should fly
	 */
	void fly(boolean fly);

	/**
	 * Sudo's a command from the account
	 * 
	 * @param command
	 *            To sudo
	 */
	void sudo(String command);

}
