package net.expvp.core.commands.general.message;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.expvp.api.Pair;
import net.expvp.api.TextUtil;
import net.expvp.api.bukkit.exceptions.PlayerOfflineException;
import net.expvp.api.interfaces.player.IPlayerDataHandler;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.core.NullContainer;
import net.expvp.core.commands.NullCommand;

/**
 * Class used for handling command /message
 * 
 * @author NullUser
 * @see NullCommand
 */
public class CommandMessage extends NullCommand {

	public CommandMessage(NullContainer container) {
		super(container, "message", "core.message", "/message <player> <message>", 2, true);
	}

	/**
	 * @see NullCommand
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		OfflinePlayerAccount target = getContainer().getPlayerDataHandler().getAccount(args[0]);
		if (target == null || !target.isOnline()) {
			getContainer().getMessages().sendMessage(sender, null, "gen.account-offline", args[0]);
			return;
		}
		IPlayerDataHandler pdh = getContainer().getPlayerDataHandler();
		OfflinePlayerAccount acc = (sender instanceof Player) ? pdh.getAccount(((Player) sender).getUniqueId())
				: pdh.getConsole();
		OnlinePlayerAccount account = acc.getOnlineAccount();
		Pair<String, Boolean> result;
		try {
			result = account.message(target, TextUtil.connect(args, 1, args.length));
		} catch (PlayerOfflineException ex) {
			return;
		}
		if (result.getRight()) {
			sendMessage(sender, target, "format.to", result.getLeft());
			sendMessage(target.getOnlineAccount(), account, "format.from", result.getLeft());
		} else {
			sendMessage(sender, null, "denied");
		}
	}

}
