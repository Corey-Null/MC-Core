package net.expvp.core.commands.general;

import org.bukkit.command.CommandSender;

import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.core.NullContainer;
import net.expvp.core.commands.NullCommand;
import net.expvp.core.player.WildcardPlayerAccount;

/**
 * Command used for handling command /feed
 * 
 * @author NullUser
 * @see NullCommand
 */
public class CommandFly extends NullCommand {

	public CommandFly(NullContainer container) {
		super(container, "fly", "core.fly", "/fly [player] [true|false]", 0, true);
	}

	/**
	 * @see NullCommand
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length == 0) {
			if (!checkSender(sender, true)) {
				return;
			}
			OnlinePlayerAccount account = getAccount(sender);
			account.toggleFlight();
			if (account.getPlayer().getAllowFlight()) {
				sendMessage(account, null, "enable");
			} else {
				sendMessage(account, null, "disable");
			}
			return;
		}
		OfflinePlayerAccount acc = getContainer().getPlayerDataHandler().getAccount(args[0]);
		if (acc == null || !acc.isOnline()) {
			getContainer().getMessages().sendMessage(sender, null, "gen.account-offline", args[0]);
			return;
		}
		OnlinePlayerAccount account = checkAccount(sender, args[0], true);
		if (account == null) {
			return;
		}
		boolean enabled = false;
		boolean toggle = false;
		if (args.length > 1) {
			switch (args[1]) {
			case "true":
				enabled = true;
				account.fly(true);
				break;
			case "false":
				enabled = false;
				account.fly(false);
				break;
			default:
				toggle = true;
				account.toggleFlight();
				if (account.getPlayer() != null) {
					enabled = account.getPlayer().getAllowFlight();
				}
				break;
			}
		} else {
			toggle = true;
			account.toggleFlight();
			if (account.getPlayer() != null) {
				enabled = account.getPlayer().getAllowFlight();
			}
		}
		if (toggle && account instanceof WildcardPlayerAccount) {
			sendMessage(sender, null, "other.toggle");
			sendMessage(account, null, "toggle");
		} else {
			if (enabled) {
				sendMessage(sender, account, "other.enable", args[0]);
				sendMessage(account, null, "enable");
			} else {
				sendMessage(sender, account, "other.disable");
				sendMessage(account, null, "disable");
			}
		}
	}

}
