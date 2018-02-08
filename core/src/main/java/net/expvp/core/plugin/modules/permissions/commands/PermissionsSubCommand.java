package net.expvp.core.plugin.modules.permissions.commands;

import org.bukkit.command.CommandSender;

import net.expvp.core.NullContainer;
import net.expvp.core.plugin.modules.permissions.PermissionsModule;

/**
 * Class used for processing sub commands of the /nullpermissions command
 * 
 * @author NullUser
 */
public abstract class PermissionsSubCommand {

	protected final PermissionsCommand command;
	protected final NullContainer container;
	protected final PermissionsModule module;

	public PermissionsSubCommand(PermissionsCommand command) {
		this.command = command;
		this.container = command.getContainer();
		this.module = command.getModule();
	}

	/**
	 * Method to execute the command through the sub command
	 * 
	 * @param sender
	 *            To match the sender of the command
	 * @param args
	 *            To math the args in the comamnd message
	 */
	public abstract void execute(CommandSender sender, String[] args);

}
