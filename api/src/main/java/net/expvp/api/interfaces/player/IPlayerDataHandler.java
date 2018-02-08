package net.expvp.api.interfaces.player;

import java.net.InetAddress;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

import net.expvp.api.interfaces.Saveable;

/**
 * Handler for Player Accounts
 * 
 * @author NullUser
 * @see Saveable
 */
public interface IPlayerDataHandler extends Saveable {

	/**
	 * @return the console
	 */
	OnlinePlayerAccount getConsole();

	/**
	 * @return the wildcard account
	 */
	OnlinePlayerAccount getWildcard();

	/**
	 * @return the cached accounts
	 */
	Set<OfflinePlayerAccount> getCachedAccounts();

	/**
	 * @return true if the player data is still being loaded
	 */
	boolean isLoading();

	/**
	 * Loads player
	 * 
	 * @param player
	 * @return true if the load was successful
	 */
	boolean login(Player player, InetAddress address);

	/**
	 * Unloads player
	 * 
	 * @param player
	 */
	void unloadPlayer(Player player);

	/**
	 * Gets player account
	 * 
	 * @param id
	 * @return Player account specified by id
	 */
	OfflinePlayerAccount getAccount(UUID id);

	/**
	 * Gets player account
	 * 
	 * @param player
	 * @return Player account specified by player
	 */
	OfflinePlayerAccount getAccount(Player player);

	/**
	 * Gets player account
	 * 
	 * @param name
	 * @return Player account specified by name
	 */
	OfflinePlayerAccount getAccount(String name);

	/**
	 * Loads all cached accounts
	 */
	void loadAccounts();

}
