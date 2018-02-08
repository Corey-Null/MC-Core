package net.expvp.core.commands.general.warp;

import net.expvp.api.TextUtil;
import net.expvp.api.bukkit.LocationUtil;
import net.expvp.api.interfaces.data.IWorldData;
import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.core.NullContainer;
import net.expvp.core.commands.AccountCommand;

/**
 * Command used for handling command /setwarp
 * 
 * @author NullUser
 * @see AccountCommand
 */
public class CommandSetWarp extends AccountCommand {

	public CommandSetWarp(NullContainer container) {
		super(container, "setwarp", "core.setwarp", "/setwarp <name>", 1);
	}

	/**
	 * @see AccountCommand
	 */
	@Override
	public void execute(OnlinePlayerAccount account, String[] args) {
		if (!TextUtil.isAlphaNumeric(args[0])) {
			getContainer().getMessages().sendMessage(account.getPlayer(), null, "gen.non-alpha-numeric", args[0]);
			return;
		}
		IWorldData wd = getContainer().getWorldData();
		boolean exists = wd.getWarp(args[0]) != null;
		wd.setWarp(args[0], LocationUtil.getRoundedDestination(account.getPlayer().getLocation()));
		if (exists) {
			sendMessage(account, null, "updated", args[0]);
		} else {
			sendMessage(account, null, "created", args[0]);
		}
	}

}
