package net.expvp.api.interfaces.player;

import java.net.InetAddress;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

import net.expvp.api.interfaces.Container;
import net.expvp.api.interfaces.Saveable;
import net.expvp.api.interfaces.ip.IPContainer;
import net.expvp.api.interfaces.permissions.IRank;
import net.expvp.api.interfaces.permissions.PermissionContainer;

/**
 * Offline version of the player account
 * 
 * @author NullUser
 * @see Saveable
 * @see PermissionContainer
 * @see IPContainer
 */
public interface OfflinePlayerAccount extends Saveable, PermissionContainer, IPContainer {

	/**
	 * @return the container
	 */
	Container<?> getContainer();

	/**
	 * @param newBalance
	 *            To set the balance
	 */
	void setBalance(double newBalance);

	/**
	 * @param toAdd
	 *            To add to the balance
	 */
	void addBalance(double toAdd);

	/**
	 * @param toRemove
	 *            To take from the balance
	 * @return the difference of the old balance and the new balance
	 */
	double removeBalance(double toRemove);

	/**
	 * @return the balance
	 */
	double getBalance();

	/**
	 * @return the last logout time
	 */
	long getLastLogoutTime();

	/**
	 * Logs the specified player into the account
	 * 
	 * @param player
	 */
	void login(Player player, InetAddress address);

	/**
	 * @return true if the account is online
	 */
	boolean isOnline();

	/**
	 * @return the online account
	 */
	OnlinePlayerAccount getOnlineAccount();

	/**
	 * @return the ignored players
	 */
	Set<UUID> getIgnoredPlayers();

	/**
	 * @param id
	 * @return true if account is ignoring the ID
	 */
	boolean isIgnoring(UUID id);

	/**
	 * @return true if the account is dirty
	 */
	boolean isDirty();

	/**
	 * @return the uuid
	 */
	UUID getUUID();

	/**
	 * @return the name
	 */
	String getName();

	/**
	 * @return the rank
	 */
	IRank getRank();

	/**
	 * @param rank
	 *            To set the rank
	 */
	void setRank(IRank rank);

	/**
	 * @param permissions
	 *            To initialize permissions
	 */
	void initPerms(Collection<String> permissions);

	/**
	 * @param rank
	 *            To inherit
	 */
	void inheritRank(IRank rank);

	/**
	 * @param rank
	 *            To unInherit
	 */
	void unInheritRank(IRank rank);

	/**
	 * @return the inherited ranks
	 */
	Set<String> getInheritedRanks();

	/**
	 * @return the prefix
	 */
	String getPrefix();

	/**
	 * @return the suffix
	 */
	String getSuffix();

	/**
	 * @param prefix
	 *            To set the prefix
	 */
	void setPrefix(String prefix);

	/**
	 * @param suffix
	 *            To set the suffix
	 */
	void setSuffix(String suffix);

}
