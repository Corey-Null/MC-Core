package net.expvp.core.commands.general.warp;

import org.bukkit.command.CommandSender;

import net.expvp.core.NullContainer;
import net.expvp.core.commands.NullCommand;

/**
 * Command used for handling command /delwarp
 * 
 * @author NullUser
 * @see NullCommand
 */
public class CommandDelWarp extends NullCommand {

	public CommandDelWarp(NullContainer container) {
		super(container, "delwarp", "core.delwarp", "/delwarp <warp>", 1, true);
	}

	/**
	 * @see NullCommand
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (getContainer().getWorldData().delWarp(args[0])) {
			sendMessage(sender, null, "deleted", args[0]);
		} else {
			sendMessage(sender, null, "not-exist", args[0]);
		}
	}

}
