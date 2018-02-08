package net.expvp.api.interfaces.teleport;

import net.expvp.api.enums.TPRequestMotion;
import net.expvp.api.interfaces.player.OnlinePlayerAccount;

/**
 * Interface used for representing a teleport request
 * 
 * @author NullUser
 */
public interface ITeleportRequest {

	/**
	 * Accepts the teleport request
	 */
	void accept();

	/**
	 * Denies the teleport request
	 */
	void deny();

	/**
	 * @return the requester
	 */
	OnlinePlayerAccount getAccount();

	/**
	 * @return the target
	 */
	OnlinePlayerAccount getTarget();

	/**
	 * @return the motion
	 */
	TPRequestMotion getMotion();

	/**
	 * @return the task ID of the auto remover
	 */
	int taskID();

}
