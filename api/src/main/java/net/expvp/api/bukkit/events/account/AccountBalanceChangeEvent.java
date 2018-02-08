package net.expvp.api.bukkit.events.account;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import net.expvp.api.bukkit.events.AccountEvent;
import net.expvp.api.enums.BalanceChangeReason;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;

/**
 * Event to watch balance changing
 * 
 * @author NullUser
 * @see AccountEvent
 */
public class AccountBalanceChangeEvent extends AccountEvent {

	public final static HandlerList handlers = new HandlerList();

	private final BalanceChangeReason reason;
	private double newBalance;

	/**
	 * Initializes event with the new balance, and reason for changing
	 * 
	 * @param account
	 * @param newBalance
	 * @param reason
	 */
	public AccountBalanceChangeEvent(OfflinePlayerAccount account, double newBalance, BalanceChangeReason reason) {
		super(account);
		this.reason = reason;
		this.newBalance = newBalance;
	}

	/**
	 * @return the newBalance
	 */
	public double getNewBalance() {
		return newBalance;
	}

	/**
	 * @return the reason
	 */
	public BalanceChangeReason getReason() {
		return reason;
	}

	/**
	 * @param newBalance
	 *            To set newBalance
	 */
	public void setNewBalance(double newBalance) {
		if (newBalance < 0) {
			newBalance = 0;
		}
		this.newBalance = newBalance;
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
