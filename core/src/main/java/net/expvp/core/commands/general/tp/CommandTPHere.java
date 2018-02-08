package net.expvp.core.commands.general.tp;

import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.core.NullContainer;
import net.expvp.core.commands.AccountCommand;
import net.expvp.core.player.WildcardPlayerAccount;

/**
 * Class used for handling command /tphere
 * 
 * @author NullUser
 * @see AccountCommand
 */
public class CommandTPHere extends AccountCommand {

	public CommandTPHere(NullContainer container) {
		super(container, "tphere", "core.tphere", "/tphere <player>", 1);
	}

	/**
	 * @see AccountCommand
	 */
	@Override
	public void execute(OnlinePlayerAccount account, String[] args) {
		OnlinePlayerAccount target = checkAccount(account.getPlayer(), args[0], true);
		if (target == null) {
			return;
		}
		if (target instanceof WildcardPlayerAccount) {
			for (OfflinePlayerAccount acc : getContainer().getPlayerDataHandler().getCachedAccounts()) {
				if (acc.isOnline() && !acc.getOnlineAccount().equals(account)) {
					OnlinePlayerAccount oAcc = acc.getOnlineAccount();
					oAcc.teleport(account.getPlayer().getLocation().clone());
					sendMessage(oAcc, account, "to");
				}
			}
		} else {
			target.teleport(account.getPlayer().getLocation().clone());
			sendMessage(target, account, "to");
		}
		sendMessage(account, target, "from");
	}

}
