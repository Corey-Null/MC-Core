package net.expvp.core.commands.general;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.core.NullContainer;
import net.expvp.core.commands.NullCommand;

/**
 * Command used for handling command /god
 * 
 * @author NullUser
 * @see NullCommand
 */
public class CommandGod extends NullCommand {

	public CommandGod(NullContainer container) {
		super(container, "god", "core.god", "/god [player]", 0, true);
	}

	/**
	 * @see NullCommand
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length > 0 && sender.hasPermission("core.god.other")) {
			OfflinePlayerAccount targ = getContainer().getPlayerDataHandler().getAccount(args[0]);
			if (targ == null || !targ.isOnline()) {
				getContainer().getMessages().sendMessage(sender, null, "gen.account-offline", args[0]);
				return;
			}
			OnlinePlayerAccount target = targ.getOnlineAccount();
			target.god();
			sendMessage(target, null, "self");
			sendMessage(sender, target, "other");
			return;
		}
		if (!checkSender(sender, true)) {
			return;
		}
		OfflinePlayerAccount acc = getContainer().getPlayerDataHandler().getAccount(((Player) sender).getUniqueId());
		OnlinePlayerAccount account = acc.getOnlineAccount();
		account.god();
		sendMessage(account, null, "self");
	}

}
