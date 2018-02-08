package net.expvp.core.plugin.modules.serveraccess.commands;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.expvp.api.TimeUtil;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.core.commands.ModuleCommand;
import net.expvp.core.plugin.modules.serveraccess.ServerAccessModule;
import net.expvp.core.plugin.modules.serveraccess.data.BanData;
import net.expvp.core.plugin.modules.serveraccess.data.BanDataManager;
import net.expvp.core.plugin.modules.serveraccess.data.IPBanData;

/**
 * Class used for managing all ban commands
 * 
 * @author NullUser
 * @see ModuleCommand<T>
 */
public abstract class BanCommand extends ModuleCommand<ServerAccessModule> {

	public BanCommand(ServerAccessModule module, String cmd, String perm, String usage) {
		super(module.getContainer(), module, cmd, perm, usage, 1, true);
	}

	public BanCommand(ServerAccessModule module, String cmd, String perm, String usage, int length) {
		super(module.getContainer(), module, cmd, perm, usage, length, true);
	}
	
	/**
	 * Removes the specified player with the server
	 * 
	 * @param player
	 *            To kick
	 * @param data
	 *            To parse the kick message
	 */
	public void removeFromServer(Player player, BanData<?> data) {
		UUID hander = data.getHander();
		String name;
		if (hander == null) {
			name = "Console";
		} else {
			name = getContainer().getPlayerDataHandler().getAccount(hander).getName();
		}
		String message = null;
		switch (data.getType()) {
		case BAN:
			if (data instanceof IPBanData) {
				message = getModule().getBanipMessage();
			} else {
				message = getModule().getBanMessage();
			}
			break;
		case KICK:
			message = getModule().getKickMessage();
			message = message.replace("%kicker%", name);
			break;
		case TEMP_BAN:
			message = getModule().getTempBanMessage();
			message = message.replace("%time%", TimeUtil.getTime(data.getBegin(), data.getExpire()));
			break;
		}
		if (message == null) {
			player.kickPlayer(ChatColor.RED + "You have been removed from the server.");
			return;
		}
		message = message.replace("%banner%", name);
		message = message.replace("%reason%", data.getReason());
		player.kickPlayer(message);
	}

	/**
	 * @return the data manager
	 */
	public BanDataManager manager() {
		return getModule().getManager();
	}

	/**
	 * @param arg
	 * @return the account based on the arg
	 */
	public OfflinePlayerAccount getAccount(String arg) {
		return getContainer().getPlayerDataHandler().getAccount(arg);
	}

}
