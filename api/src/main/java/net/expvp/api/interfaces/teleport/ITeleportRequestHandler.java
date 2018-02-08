package net.expvp.api.interfaces.teleport;

import net.expvp.api.enums.TPRequestMotion;
import net.expvp.api.interfaces.player.OnlinePlayerAccount;

/**
 * Interface used for handling teleport requests
 * 
 * @author NullUser
 */
public interface ITeleportRequestHandler {

	/**
	 * Initializes a teleport request
	 * 
	 * @param account
	 *            Request initializer
	 * @param target
	 *            Request target
	 * @param motion
	 *            Requesting motion
	 */
	void initRequest(OnlinePlayerAccount account, OnlinePlayerAccount target, TPRequestMotion motion);

	/**
	 * @param account
	 *            To look for pending request
	 * @return pending request from account
	 */
	ITeleportRequest getPendingRequest(OnlinePlayerAccount account);

	/**
	 * @param account
	 * @return true if account has a pending request
	 */
	boolean hasPendingRequest(OnlinePlayerAccount account);

	/**
	 * Accepts a teleport request
	 * 
	 * @param request
	 *            To accept
	 * @return true if request can be accepted
	 */
	boolean acceptRequest(ITeleportRequest request);

	/**
	 * Declines a teleport request
	 * 
	 * @param request
	 *            To decline
	 * @return true if request can be accepted
	 */
	boolean declineRequest(ITeleportRequest request);

}
