package net.expvp.core.plugin.modules.serveraccess.commands;

import org.bukkit.command.CommandSender;

import net.expvp.api.interfaces.ip.IAddress;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.core.ip.IPAddress;
import net.expvp.core.player.ConsoleAccount;
import net.expvp.core.player.WildcardPlayerAccount;
import net.expvp.core.plugin.modules.serveraccess.ServerAccessModule;

/**
 * Class used for handling command /unbanip
 * 
 * @author NullUser
 * @see BanCommand
 */
public class CommandUnbanIP extends BanCommand {

	public CommandUnbanIP(ServerAccessModule module) {
		super(module, "unbanip", "core.unbanip", "/unbanip <player|ip>");
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
		if (manager().unban(address)) {
			sendMessage(sender, null, "execute", address);
		} else {
			sendMessage(sender, null, "not-banned", address);
		}
	}

}
