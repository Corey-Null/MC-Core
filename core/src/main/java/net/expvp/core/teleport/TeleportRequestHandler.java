package net.expvp.core.teleport;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.google.common.collect.Maps;

import net.expvp.api.enums.TPRequestMotion;
import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.api.interfaces.teleport.ITeleportRequest;
import net.expvp.api.interfaces.teleport.ITeleportRequestHandler;
import net.expvp.core.NullContainer;

/**
 * Class used for handling teleport requests
 * 
 * @author NullUser
 * @see ITeleportRequestHandler
 */
public class TeleportRequestHandler implements ITeleportRequestHandler {

	private final NullContainer container;
	private final Map<UUID, ITeleportRequest> requests;

	public TeleportRequestHandler(NullContainer container) {
		this.container = container;
		this.requests = Maps.newHashMap();
	}

	/**
	 * @see ITeleportRequestHandler
	 */
	@Override
	public void initRequest(OnlinePlayerAccount account, OnlinePlayerAccount target, TPRequestMotion motion) {
		TeleportRequest request = new TeleportRequest(container, account, target, motion);
		int id = Bukkit.getScheduler().scheduleSyncDelayedTask(container.getPlugin(), () -> {
			if (requests.containsKey(target.getUUID())) {
				requests.remove(target.getUUID());
			}
		}, 20 * 60);
		request.setTaskID(id);
		UUID uuid = target.getUUID();
		if (requests.containsKey(uuid)) {
			ITeleportRequest req = requests.get(uuid);
			Bukkit.getScheduler().cancelTask(req.taskID());
			requests.remove(uuid);
		}
		requests.put(uuid, request);
	}

	/**
	 * @see ITeleportRequestHandler
	 */
	@Override
	public ITeleportRequest getPendingRequest(OnlinePlayerAccount account) {
		return requests.get(account.getUUID());
	}

	/**
	 * @see ITeleportRequestHandler
	 */
	@Override
	public boolean hasPendingRequest(OnlinePlayerAccount account) {
		return getPendingRequest(account) != null;
	}

	/**
	 * @see ITeleportRequestHandler
	 */
	@Override
	public boolean acceptRequest(ITeleportRequest request) {
		UUID targ = request.getTarget().getUUID();
		if (!requests.containsKey(targ)) {
			return false;
		}
		requests.remove(targ);
		request.accept();
		Bukkit.getScheduler().cancelTask(request.taskID());
		return true;
	}

	/**
	 * @see ITeleportRequestHandler
	 */
	@Override
	public boolean declineRequest(ITeleportRequest request) {
		UUID targ = request.getTarget().getUUID();
		if (!requests.containsKey(targ)) {
			return false;
		}
		requests.remove(targ);
		request.deny();
		Bukkit.getScheduler().cancelTask(request.taskID());
		return true;
	}

}
