package net.expvp.core.commands.general;

import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.core.NullContainer;
import net.expvp.core.commands.AccountCommand;

/**
 * Class for handling the command /back
 * 
 * @author NullUser
 * @see AccountCommand
 */
public class CommandBack extends AccountCommand {

	public CommandBack(NullContainer container) {
		super(container, "back", "core.back", "/back", 0);
	}

	/**
	 * @see AccountCommand
	 */
	@Override
	public void execute(OnlinePlayerAccount account, String[] args) {
		if (account.teleport(account.getLastLocation())) {
			sendMessage(account, null, "success");
		} else {
			sendMessage(account, null, "denied");
		}
	}

}
