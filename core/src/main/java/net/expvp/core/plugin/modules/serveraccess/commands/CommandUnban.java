package net.expvp.core.plugin.modules.serveraccess.commands;

import org.bukkit.command.CommandSender;

import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.core.player.ConsoleAccount;
import net.expvp.core.player.WildcardPlayerAccount;
import net.expvp.core.plugin.modules.serveraccess.ServerAccessModule;

/**
 * Class used for handling command /unban
 * 
 * @author NullUser
 * @see BanCommand
 */
public class CommandUnban extends BanCommand {

	public CommandUnban(ServerAccessModule module) {
		super(module, "unban", "core.unban", "/unban <player>");
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
		if (manager().unban(acc.getUUID())) {
			sendMessage(sender, acc, "execute");
		} else {
			sendMessage(sender, acc, "not-banned");
		}
	}

}
