package net.expvp.core.commands.general.gamemode;

import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import net.expvp.core.NullContainer;
import net.expvp.core.commands.NullCommand;

/**
 * Class used for handling command /gamemode
 * 
 * @author NullUser
 * @see NullCommand
 */
public class CommandGamemode extends NullCommand {

	public CommandGamemode(NullContainer container) {
		super(container, "gamemode", "core.gamemode", "/gamemode <gm> [player]", 1, true);
	}

	/**
	 * @see NullCommand
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		PluginCommand cmd = getContainer().getPlugin().getCommand("gamemode" + args[0]);
		if (cmd == null || !cmd.isRegistered() || cmd.getExecutor() == null) {
			sendMessage(sender, null, "invalid-gamemode", args[0]);
			return;
		}
		int newSize = args.length - 1;
		String[] nArray = new String[newSize];
		if (newSize > 0) {
			nArray[0] = args[1];
		}
		cmd.execute(sender, cmd.getLabel(), nArray);
	}

}
