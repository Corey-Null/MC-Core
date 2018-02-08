package net.expvp.core.commands.general.tp;

import net.expvp.api.interfaces.IMessages;
import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.api.interfaces.teleport.ITeleportRequest;
import net.expvp.api.interfaces.teleport.ITeleportRequestHandler;
import net.expvp.core.NullContainer;
import net.expvp.core.commands.AccountCommand;

/**
 * Class used for handling command /tpdeny
 * 
 * @author NullUser
 * @see AccountCommand
 */
public class CommandTPDeny extends AccountCommand {

	public CommandTPDeny(NullContainer container) {
		super(container, "tpdeny", "core.tpdeny", "/tpdeny", 0);
	}

	/**
	 * @see AccountCommand
	 */
	@Override
	public void execute(OnlinePlayerAccount account, String[] args) {
		IMessages msgs = getContainer().getMessages();
		ITeleportRequestHandler handler = getContainer().getTeleportRequestHandler();
		ITeleportRequest request = handler.getPendingRequest(account);
		if (request == null) {
			msgs.sendMessage(account.getPlayer(), null, "tprequest.no-pending-request");
			return;
		}
		handler.declineRequest(request);
	}

}
