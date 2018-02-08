package net.expvp.core.teleport;

import net.expvp.api.enums.TPRequestMotion;
import net.expvp.api.interfaces.IMessages;
import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.api.interfaces.teleport.ITeleportRequest;
import net.expvp.core.NullContainer;

/**
 * Class used to represent a teleport request
 * 
 * @author NullUser
 * @see ITeleportRequest
 */
public class TeleportRequest implements ITeleportRequest {

	private final NullContainer container;
	private final OnlinePlayerAccount account;
	private final OnlinePlayerAccount target;
	private final TPRequestMotion motion;
	private int id;

	public TeleportRequest(NullContainer container, OnlinePlayerAccount account, OnlinePlayerAccount target,
			TPRequestMotion motion) {
		this.container = container;
		this.account = account;
		this.target = target;
		this.motion = motion;
	}

	/**
	 * @param id
	 *            To set the ID
	 */
	protected void setTaskID(int id) {
		this.id = id;
	}

	/**
	 * @see ITeleportRequest
	 */
	@Override
	public void accept() {
		IMessages msgs = container.getMessages();
		switch (motion) {
		case HERE:
			if (!account.isOnline()) {
				msgs.sendMessage(target.getPlayer(), account.getOfflineAccount(), "tprequest.now-offline");
				return;
			}
			target.teleport(account.getPlayer().getLocation());
			msgs.sendMessage(target.getPlayer(), account.getOfflineAccount(), "tprequest.accept");
			break;
		case TO:
			if (!account.isOnline()) {
				return;
			}
			account.teleport(target.getPlayer().getLocation());
			msgs.sendMessage(target.getPlayer(), account.getOfflineAccount(), "tprequest.accept");
			msgs.sendMessage(account.getPlayer(), target.getOfflineAccount(), "tprequest.acc.accept");
			break;
		}
	}

	/**
	 * @see ITeleportRequest
	 */
	@Override
	public void deny() {
		IMessages msgs = container.getMessages();
		if (account.isOnline()) {
			msgs.sendMessage(account.getPlayer(), target, "tprequest.acc.deny");
		}
		msgs.sendMessage(target.getPlayer(), account, "tprequest.deny");
	}

	/**
	 * @see ITeleportRequest
	 */
	@Override
	public OnlinePlayerAccount getAccount() {
		return account;
	}

	/**
	 * @see ITeleportRequest
	 */
	@Override
	public OnlinePlayerAccount getTarget() {
		return target;
	}

	/**
	 * @see ITeleportRequest
	 */
	@Override
	public TPRequestMotion getMotion() {
		return motion;
	}

	/**
	 * @see ITeleportRequest
	 */
	@Override
	public int taskID() {
		return id;
	}

}
