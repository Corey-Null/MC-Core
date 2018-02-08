package net.expvp.api.bukkit.events.account;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.expvp.api.bukkit.events.AccountEvent;
import net.expvp.api.interfaces.player.OnlinePlayerAccount;

/**
 * Event to watch the sudo of an account
 * 
 * @author NullUser
 * @see AccountEvent
 */
public class AccountSudoEvent extends AccountEvent {

	public final static HandlerList handlers = new HandlerList();

	private String command;

	/**
	 * Initializes the messaging event with the account, target, and message
	 * 
	 * @param account
	 * @param target
	 * @param message
	 */
	public AccountSudoEvent(OnlinePlayerAccount account, String command) {
		super(account);
		this.command = command;
	}

	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @param command
	 *            To set command
	 */
	public void setCommand(String command) {
		this.command = command;
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
