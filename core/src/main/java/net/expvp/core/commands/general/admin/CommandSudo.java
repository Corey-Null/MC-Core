package net.expvp.core.commands.general.admin;

import org.bukkit.command.CommandSender;

import net.expvp.api.TextUtil;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.core.NullContainer;
import net.expvp.core.commands.NullCommand;

/**
 * Command used for handling command /sudo
 * 
 * @author NullUser
 * @see NullCommand
 */
public class CommandSudo extends NullCommand {

	public CommandSudo(NullContainer container) {
		super(container, "sudo", "core.sudo", "/sudo <player> [command]", 2, true);
	}

	/**
	 * @see NullCommand
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		OfflinePlayerAccount targ = getContainer().getPlayerDataHandler().getAccount(args[0]);
		if (targ == null || !targ.isOnline()) {
			getContainer().getMessages().sendMessage(sender, null, "gen.account-offline", args[1]);
			return;
		}
		OnlinePlayerAccount target = targ.getOnlineAccount();
		String command = TextUtil.connect(args, 1, args.length);
		target.sudo(command);
		sendMessage(sender, targ, "response", command);
	}

}
