package net.expvp.core.plugin.modules.economy.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.core.NullContainer;
import net.expvp.core.commands.ModuleCommand;
import net.expvp.core.plugin.modules.economy.EconomyModule;

/**
 * Command for checking balance
 * 
 * @author NullUser
 * @see ModuleCommand
 */
public class BalanceCommand extends ModuleCommand<EconomyModule> {

	/**
	 * @see ModuleCommand
	 * @param container
	 * @param module
	 */
	public BalanceCommand(EconomyModule module) {
		super(module.getContainer(), module, "balance", "core.econ.balance", "/balance [player]", 0, true);
	}

	/**
	 * @see ModuleCommand
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		NullContainer container = getContainer();
		if (args.length == 0 || !checkPermission(sender, "core.econ.balance.other", false)) {
			if (checkSender(sender, true)) {
				OfflinePlayerAccount account = container.getPlayerDataHandler()
						.getAccount(((Player) sender).getUniqueId());
				sendMessage(sender, null, "self", getModule().format(account.getBalance()));
			}
			return;
		}
		OfflinePlayerAccount account = container.getPlayerDataHandler().getAccount(args[0]);
		if (account == null) {
			container.getMessages().sendMessage(sender, null, "gen.account-does-not-exist", args[0]);
			return;
		}
		sendMessage(sender, account, "other", getModule().format(account.getBalance()));
	}

}
