package net.expvp.core.commands.general.tp;

import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.core.NullContainer;
import net.expvp.core.commands.AccountCommand;

/**
 * Class used for handling command /tpall
 * 
 * @author NullUser
 * @see AccountCommand
 */
public class CommandTPAll extends AccountCommand {

	public CommandTPAll(NullContainer container) {
		super(container, "tpall", "core.tpall", "/tpall", 0);
	}

	/**
	 * @see AccountCommand
	 */
	@Override
	public void execute(OnlinePlayerAccount account, String[] args) {
		for (OfflinePlayerAccount acc : getContainer().getPlayerDataHandler().getCachedAccounts()) {
			if (acc.isOnline() && !acc.getOnlineAccount().equals(account)) {
				OnlinePlayerAccount oAcc = acc.getOnlineAccount();
				oAcc.teleport(account.getPlayer().getLocation().clone());
				sendMessage(oAcc, account, "to");
			}
		}
		sendMessage(account, null, "from");
	}

}
