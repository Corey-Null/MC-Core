package net.expvp.core.commands.general.message;

import net.expvp.api.Pair;
import net.expvp.api.TextUtil;
import net.expvp.api.bukkit.exceptions.PlayerOfflineException;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.core.NullContainer;
import net.expvp.core.commands.AccountCommand;
import net.expvp.core.commands.NullCommand;

/**
 * Class used for handling command /reply
 * 
 * @author NullUser
 * @see NullCommand
 */
public class CommandReply extends AccountCommand {

	public CommandReply(NullContainer container) {
		super(container, "reply", "core.reply", "/reply <message>", 1);
	}

	/**
	 * @see NullCommand
	 */
	@Override
	public void execute(OnlinePlayerAccount account, String[] args) {
		OfflinePlayerAccount target = account.getReplyTo();
		if (target == null || !target.isOnline()) {
			sendMessage(account, null, "nobody-to-reply-to");
			return;
		}
		Pair<String, Boolean> result;
		try {
			result = account.message(target, TextUtil.connect(args, 0, args.length));
		} catch (PlayerOfflineException ex) {
			return;
		}
		if (result.getRight()) {
			sendMessage(account, target, "format.to", result.getLeft());
			sendMessage(target.getOnlineAccount(), account, "format.from", result.getLeft());
		} else {
			sendMessage(account, null, "denied");
		}
	}

}
