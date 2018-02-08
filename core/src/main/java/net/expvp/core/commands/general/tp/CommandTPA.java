package net.expvp.core.commands.general.tp;

import net.expvp.api.enums.TPRequestMotion;
import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.api.interfaces.teleport.ITeleportRequestHandler;
import net.expvp.core.NullContainer;
import net.expvp.core.commands.AccountCommand;

/**
 * Class used for handling command /tpa
 * 
 * @author NullUser
 * @see AccountCommand
 */
public class CommandTPA extends AccountCommand {

	public CommandTPA(NullContainer container) {
		super(container, "tpa", "core.tpa", "/tpa <player>", 1);
	}

	/**
	 * @see AccountCommand
	 */
	@Override
	public void execute(OnlinePlayerAccount account, String[] args) {
		ITeleportRequestHandler handler = getContainer().getTeleportRequestHandler();
		OnlinePlayerAccount target = checkAccount(account.getPlayer(), args[0], true);
		if (target == null) {
			return;
		}
		handler.initRequest(account, target, TPRequestMotion.TO);
		sendMessage(account, target, "sent");
		sendMessage(target, account, "received");
	}

}
