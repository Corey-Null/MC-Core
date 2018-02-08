package net.expvp.api.bukkit.events.account;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.expvp.api.bukkit.events.AccountEvent;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.api.interfaces.player.OnlinePlayerAccount;

/**
 * Event to watch messaging between accounts
 * 
 * @author NullUser
 * @see AccountEvent
 */
public class AccountMessageEvent extends AccountEvent {

	public final static HandlerList handlers = new HandlerList();

	private final OnlinePlayerAccount target;
	private String message;

	/**
	 * Initializes the messaging event with the account, target, and message
	 * 
	 * @param account
	 * @param target
	 * @param message
	 */
	public AccountMessageEvent(OfflinePlayerAccount account, OnlinePlayerAccount target, String message) {
		super(account);
		this.target = target;
		this.message = message;
	}

	/**
	 * @return the target
	 */
	public OnlinePlayerAccount getTarget() {
		return target;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            To set message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @see Event
	 */
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	/**
	 * @see AccountEvent
	 */
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
