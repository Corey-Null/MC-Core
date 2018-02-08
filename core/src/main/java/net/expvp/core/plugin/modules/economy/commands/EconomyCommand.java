package net.expvp.core.plugin.modules.economy.commands;

import org.bukkit.command.CommandSender;

import net.expvp.api.NumberUtil;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.core.NullContainer;
import net.expvp.core.commands.ModuleCommand;
import net.expvp.core.plugin.modules.economy.EconomyModule;

/**
 * Command for handling the economy
 *
 * @author NullUser
 * @see ModuleCommand
 */
public class EconomyCommand extends ModuleCommand<EconomyModule> {

	/**
	 * @see ModuleCommand
	 * @param container
	 * @param module
	 */
	public EconomyCommand(EconomyModule module) {
		super(module.getContainer(), module, "economy", "core.econ.balance.edit",
				"/economy set|give|take|reset [player] [numb]", 1, true);
	}

	/**
	 * @see ModuleCommand
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		NullContainer container = getContainer();
		if (!checkPermission(sender, "core.econ.balance.edit", true)) {
			return;
		}
		if (args[0].equalsIgnoreCase("reset")) {
			container.getPlayerDataHandler().getCachedAccounts().forEach(player -> player.setBalance(0));
		}
		if (args.length < 2) {
			sendUsage(sender);
			return;
		}
		OfflinePlayerAccount account = container.getPlayerDataHandler().getAccount(args[1]);
		if (account == null) {
			container.getMessages().sendMessage(sender, null, "gen.account-does-not-exist", args[1]);
			return;
		}
		if (checkPermission(sender, "core.econ.balance.edit", false)) {
			if (args.length < 3) {
				sendUsage(sender);
				return;
			}
			if (!NumberUtil.isDouble(args[2])) {
				container.getMessages().sendMessage(sender, null, "gen.invalid-number", args[2]);
				return;
			}
			double amount = NumberUtil.getDouble(args[2]);
			switch (args[0].toLowerCase()) {
			case "set": {
				account.setBalance(amount);
				sendMessage(sender, account, "set", amount);
				return;
			}
			case "give": {
				account.addBalance(amount);
				sendMessage(sender, account, "give", amount);
				return;
			}
			case "take": {
				sendMessage(sender, account, "take", account.removeBalance(amount));
				return;
			}
			default: {
				sendUsage(sender);
				return;
			}
			}
		}
	}

}
