package net.expvp.core.commands.general;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.core.NullContainer;
import net.expvp.core.commands.NullCommand;

/**
 * Command used for handling command /heal
 * 
 * @author NullUser
 * @see NullCommand
 */
public class CommandHeal extends NullCommand {

	public CommandHeal(NullContainer container) {
		super(container, "heal", "core.heal", "/heal [player]", 0, true);
	}

	/**
	 * @see NullCommand
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length > 0 && sender.hasPermission("core.heal.other")) {
			OfflinePlayerAccount targ = getContainer().getPlayerDataHandler().getAccount(args[0]);
			if (targ == null || !targ.isOnline()) {
				sendMessage(sender, null, "gen.account-offline", args[1]);
				return;
			}
			OnlinePlayerAccount target = targ.getOnlineAccount();
			target.heal();
			sendMessage(target, null, "self");
			sendMessage(sender, target, "other");
			return;
		}
		if (!checkSender(sender, true)) {
			return;
		}
		OfflinePlayerAccount acc = getContainer().getPlayerDataHandler().getAccount(((Player) sender).getUniqueId());
		OnlinePlayerAccount account = acc.getOnlineAccount();
		account.heal();
		sendMessage(account, null, "self");
	}

}
