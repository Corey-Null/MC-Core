package net.expvp.api.bukkit.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.expvp.api.interfaces.player.OfflinePlayerAccount;

/**
 * Account based event
 * 
 * @author NullUser
 * @see Event
 * @see Cancellable
 */
public class AccountEvent extends Event implements Cancellable {

	public final static HandlerList handlers = new HandlerList();

	private final OfflinePlayerAccount account;
	private boolean cancelled;

	/**
	 * Initializes event with the defined account
	 * 
	 * @param account
	 */
	public AccountEvent(OfflinePlayerAccount account) {
		this.account = account;
		this.cancelled = false;
	}

	/**
	 * @return the account
	 */
	public OfflinePlayerAccount getAccount() {
		return account;
	}

	/**
	 * @see Cancellable
	 */
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	/**
	 * @see Cancellable
	 */
	public boolean isCancelled() {
		return cancelled;
	}

	/**
	 * @see Event
	 */
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	/**
	 * @return handlers
	 */
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
