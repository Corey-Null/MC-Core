package net.expvp.core.plugin.modules.economy.commands;

import org.bukkit.Bukkit;

import net.expvp.api.NumberUtil;
import net.expvp.api.bukkit.events.account.AccountBalanceChangeEvent;
import net.expvp.api.enums.BalanceChangeReason;
import net.expvp.api.interfaces.IMessages;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.core.commands.ModuleAccountCommand;
import net.expvp.core.plugin.modules.economy.EconomyModule;

/**
 * Class to handle pay command
 * 
 * @author NullUser
 * @see ModuleAccountCommand
 */
public class PayCommand extends ModuleAccountCommand<EconomyModule> {

	public PayCommand(EconomyModule module) {
		super(module.getContainer(), module, "pay", "core.econ.pay", "/pay <player> <amount>", 2);
	}

	/**
	 * @see ModuleAccountCommand
	 */
	@Override
	public void execute(OnlinePlayerAccount account, String[] args) {
		IMessages messages = getContainer().getMessages();
		if (!NumberUtil.isDouble(args[1])) {
			messages.sendMessage(account.getPlayer(), null, "gen.invalid-number");
			return;
		}
		OfflinePlayerAccount target = getContainer().getPlayerDataHandler().getAccount(args[0]);
		if (target == null) {
			messages.sendMessage(account.getPlayer(), null, "gen.account-does-not-exist", args[0]);
			return;
		}
		double toMove = NumberUtil.getDouble(args[1]);
		if (account.getBalance() < toMove) {
			sendMessage(account, null, "inefficient-balance");
			return;
		}
		AccountBalanceChangeEvent event = new AccountBalanceChangeEvent(account, toMove, BalanceChangeReason.PAY);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			return;
		}
		account.removeBalance(toMove);
		sendMessage(account, target, "sending", toMove);
		target.addBalance(toMove);
		if (target.isOnline()) {
			sendMessage(target.getOnlineAccount(), account, "receiving", toMove);
		}
	}

}
