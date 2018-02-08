package net.expvp.core.commands.general.management;

import org.bukkit.command.CommandSender;

import net.expvp.core.NullContainer;
import net.expvp.core.commands.NullCommand;

/**
 * @author NullUser
 * @see NullCommand
 */
public class CommandSave extends NullCommand {

	/**
	 * @see NullCommand
	 * @param container
	 */
	public CommandSave(NullContainer container) {
		super(container, "save", "core.save", "/save", 0, true);
	}

	/**
	 * @see NullCommand
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		sendMessage(sender, null, "start");
		long start = System.currentTimeMillis();
		getContainer().reload();
		double time = (System.currentTimeMillis() - start) / 1000.0;
		sendMessage(sender, null, "finish", time);
	}

}
