package net.expvp.core.plugin.modules.serveraccess.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.expvp.api.Pair;
import net.expvp.api.TextUtil;
import net.expvp.api.interfaces.ip.IAddress;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.core.ip.IPAddress;
import net.expvp.core.player.ConsoleAccount;
import net.expvp.core.player.WildcardPlayerAccount;
import net.expvp.core.plugin.modules.serveraccess.ServerAccessModule;
import net.expvp.core.plugin.modules.serveraccess.data.BanData;
import net.expvp.core.plugin.modules.serveraccess.data.BanType;
import net.expvp.core.plugin.modules.serveraccess.data.IPBanData;

/**
 * Class used for handling command /banip
 * 
 * @author NullUser
 * @see BanCommand
 */
public class CommandBanIP extends BanCommand {

	public CommandBanIP(ServerAccessModule module) {
		super(module, "banip", "core.banip", "/banip <player|ip> [reason]");
	}

	/**
	 * @see BanCommand
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		IAddress address;
		if (IPAddress.isIP(args[0])) {
			address = new IPAddress(args[0]);
		} else {
			OfflinePlayerAccount target = getContainer().getPlayerDataHandler().getAccount(args[0]);
			if (target == null || target instanceof WildcardPlayerAccount || target instanceof ConsoleAccount) {
				getContainer().getMessages().sendMessage(sender, null, "gen.account-does-not-exist", args[0]);
				return;
			}
			address = target.getCurrentIPAddress();
		}
		if (manager().getActiveIPBans().containsKey(address)) {
			sendMessage(sender, null, "already-banned", address);
			return;
		}
		IPBanData data = new IPBanData(address, BanType.BAN);
		data.setBegin(System.currentTimeMillis());
		data.setExpire(-1);
		data.setExpired(false);
		data.setHander((sender instanceof Player) ? ((Player) sender).getUniqueId() : null);
		data.setReason(args.length > 1 ? TextUtil.connect(args, 1, args.length) : getModule().getDefaultBanReason());
		manager().ban(address, data);
		sendMessage(sender, null, "execute", address);
		for (OfflinePlayerAccount account : getContainer().getPlayerDataHandler().getCachedAccounts()) {
			if (!account.isOnline()) {
				continue;
			}
			Pair<BanData<?>, Boolean> result = manager().checkIPBan(account);
			if (result.getRight()) {
				removeFromServer(account.getOnlineAccount().getPlayer(), data);
			}
		}
	}

}
