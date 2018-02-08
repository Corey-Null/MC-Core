package net.expvp.core.plugin.modules.serveraccess.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.expvp.api.TextUtil;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.core.player.ConsoleAccount;
import net.expvp.core.player.WildcardPlayerAccount;
import net.expvp.core.plugin.modules.serveraccess.ServerAccessModule;
import net.expvp.core.plugin.modules.serveraccess.data.BanType;
import net.expvp.core.plugin.modules.serveraccess.data.UUIDBanData;

/**
 * Class used for handling command /kick
 * 
 * @author NullUser
 * @see BanCommand
 */
public class CommandKick extends BanCommand {

	public CommandKick(ServerAccessModule module) {
		super(module, "kick", "core.kick", "/kick <player> [reason]");
	}

	/**
	 * @see BanCommand
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		OfflinePlayerAccount acc = getAccount(args[0]);
		if (acc == null || acc instanceof WildcardPlayerAccount || acc instanceof ConsoleAccount) {
			getContainer().getMessages().sendMessage(sender, null, "gen.account-does-not-exist", args[0]);
			return;
		}
		if (!acc.isOnline()) {
			getContainer().getMessages().sendMessage(sender, null, "gen.account-offline", acc.getName());
			return;
		}
		String reason;
		if (args.length > 1) {
			reason = TextUtil.connect(args, 1, args.length);
		} else {
			reason = getModule().getDefaultKickReason();
		}
		UUIDBanData data = new UUIDBanData(acc.getUUID(), BanType.KICK);
		data.setBegin(System.currentTimeMillis());
		data.setExpire(-1);
		data.setExpired(true);
		data.setHander(sender instanceof Player ? ((Player) sender).getUniqueId() : null);
		data.setReason(reason);
		manager().addKick(acc.getUUID(), data);
		removeFromServer(acc.getOnlineAccount().getPlayer(), data);
		sendMessage(sender, null, "execute", args[0]);
	}

}
