package net.expvp.core.commands.general.management;

import org.bukkit.command.CommandSender;

import net.expvp.core.NullContainer;
import net.expvp.core.commands.NullCommand;

/**
 * @author NullUser
 * @see NullCommand
 */
public class CommandStop extends NullCommand {

	/**
	 * @see NullCommand
	 * @param container
	 */
	public CommandStop(NullContainer container) {
		super(container, "stop", "core.stop", "/stop", 0, true);
	}

	/**
	 * @see NullCommand
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		getContainer().stopServer();
	}

}
