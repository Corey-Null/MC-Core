package net.expvp.api.interfaces;

import org.bukkit.command.CommandExecutor;

/**
 * Interface for commands
 * 
 * @author NullUser
 * @see CommandExecutor
 */
public interface ICommand extends CommandExecutor {

	/**
	 * @return the container
	 */
	Container<?> getContainer();

	/**
	 * @return the command
	 */
	String getCommand();

	/**
	 * @return the permission
	 */
	String getPermission();

	/**
	 * @return true if the console can use the command
	 */
	boolean canConsoleUse();

	/**
	 * @return the usage
	 */
	String getUsage();

	/**
	 * @return the required amount of args
	 */
	int getRequiredArgs();

}
