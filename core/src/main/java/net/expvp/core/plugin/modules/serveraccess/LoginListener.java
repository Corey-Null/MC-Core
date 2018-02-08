package net.expvp.core.plugin.modules.serveraccess;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import net.expvp.api.Pair;
import net.expvp.api.TimeUtil;
import net.expvp.api.interfaces.player.IPlayerDataHandler;
import net.expvp.core.NullContainer;
import net.expvp.core.plugin.modules.serveraccess.data.BanData;
import net.expvp.core.plugin.modules.serveraccess.data.BanDataManager;
import net.expvp.core.plugin.modules.serveraccess.data.BanType;
import net.expvp.core.plugin.modules.serveraccess.data.IPBanData;

/**
 * Class used for watching when players log in
 * 
 * @author NullUser
 * @see Listener
 */
public class LoginListener implements Listener {

	private final ServerAccessModule module;
	private final NullContainer container;
	private final BanDataManager manager;

	public LoginListener(ServerAccessModule module) {
		this.module = module;
		this.container = module.getContainer();
		this.manager = module.getManager();
	}

	/**
	 * Event to watch for players logging in
	 * 
	 * @param event
	 *            To watch
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLogin(PlayerLoginEvent event) {
		if (event.getResult() == Result.KICK_OTHER) {
			return;
		}
		Player player = event.getPlayer();
		IPlayerDataHandler handler = container.getPlayerDataHandler();
		Pair<BanData<?>, Boolean> result = manager.isUserBanned(handler.getAccount(player.getUniqueId()));
		if (result.getRight()) {
			BanData<?> data = result.getLeft();
			event.setResult(Result.KICK_OTHER);
			UUID hander = data.getHander();
			String name;
			if (hander == null) {
				name = "Console";
			} else {
				name = handler.getAccount(hander).getName();
			}
			String reason = data.getReason();
			String message;
			if (data.getType() == BanType.TEMP_BAN) {
				message = module.getTempBanMessage().replace("%time%",
						TimeUtil.getTime(data.getBegin(), data.getExpire()));
			} else if (data instanceof IPBanData) {
				message = module.getBanipMessage();
			} else {
				message = module.getBanMessage();
			}
			message = message.replace("%banner%", name);
			message = message.replace("%reason%", reason);
			event.setKickMessage(message);
		}
	}

}
