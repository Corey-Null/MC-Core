package net.expvp.core.commands.general.tp;

import org.bukkit.command.CommandSender;

import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.core.NullContainer;
import net.expvp.core.commands.NullCommand;
import net.expvp.core.player.WildcardPlayerAccount;

/**
 * Class used for handling command /tp
 * 
 * @author NullUser
 * @see NullCommand
 */
public class CommandTP extends NullCommand {

	public CommandTP(NullContainer container) {
		super(container, "tp", "core.tp", "/tp <player> [other player]", 1, true);
	}

	/**
	 * @see NullCommand
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length == 1) {
			if (!checkSender(sender, true)) {
				return;
			}
			OnlinePlayerAccount account = getAccount(sender);
			OnlinePlayerAccount target = checkAccount(sender, args[0], true);
			if (target == null) {
				return;
			}
			if (target instanceof WildcardPlayerAccount) {
				sendMessage(account, null, "wild-card-player");
				return;
			}
			if (account.equals(target)) {
				sendMessage(account, null, "self-exception");
				return;
			}
			sendMessage(account, target, "to");
			account.teleport(target.getPlayer().getLocation().clone());
			return;
		}
		OnlinePlayerAccount account = checkAccount(sender, args[0], true);
		OnlinePlayerAccount target = checkAccount(sender, args[1], true);
		if (account == null || target == null) {
			return;
		}
		if (target instanceof WildcardPlayerAccount) {
			sendMessage(account, null, "wild-card-player");
			return;
		}
		if (account.equals(target)) {
			sendMessage(account, null, "self-exception");
			return;
		}
		if (account instanceof WildcardPlayerAccount) {
			for (OfflinePlayerAccount acc : getContainer().getPlayerDataHandler().getCachedAccounts()) {
				if (acc.isOnline() && !acc.getOnlineAccount().getPlayer().equals(sender)) {
					OnlinePlayerAccount oAcc = acc.getOnlineAccount();
					oAcc.teleport(target.getPlayer().getLocation().clone());
					sendMessage(oAcc, target, "to");
				}
			}
		} else {
			account.teleport(target.getPlayer().getLocation().clone());
			sendMessage(account, target, "to");
		}
		getContainer().getMessages().sendMessage(sender, target, "other.sender", new OfflinePlayerAccount[] { account },
				new String[] { "player" });
	}

}
