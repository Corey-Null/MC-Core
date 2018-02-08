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
 * Class used for handling command /ban
 * 
 * @author NullUser
 * @see BanCommand
 */
public class CommandBan extends BanCommand {

	public CommandBan(ServerAccessModule module) {
		super(module, "ban", "core.ban", "/ban <player> [reason]");
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
		String reason;
		if (args.length > 1) {
			reason = TextUtil.connect(args, 1, args.length);
		} else {
			reason = getModule().getDefaultBanReason();
		}
		UUIDBanData data = new UUIDBanData(acc.getUUID(), BanType.BAN);
		data.setBegin(System.currentTimeMillis()).setExpire(-1).setExpired(false);
		data.setHander(sender instanceof Player ? ((Player) sender).getUniqueId() : null).setReason(reason);
		if (manager().ban(acc.getUUID(), data)) {
			sendMessage(sender, acc, "execute");
			if (acc.isOnline()) {
				removeFromServer(acc.getOnlineAccount().getPlayer(), data);
			}
		} else {
			sendMessage(sender, acc, "already-banned");
		}
	}

}
