package net.expvp.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.expvp.api.interfaces.plugin.IModule;
import net.expvp.core.NullContainer;

/**
 * @author NullUser
 * @see NullCommand
 * @param <T>
 */
public abstract class ModuleCommand<T extends IModule<?>> extends NullCommand {

	private final T module;

	/**
	 * @see NullCommand
	 * @param container
	 * @param module
	 *            Of command
	 * @param cmd
	 * @param perm
	 * @param usage
	 * @param args
	 * @param consoleUse
	 */
	public ModuleCommand(NullContainer container, T module, String cmd, String perm, String usage, int args,
			boolean consoleUse) {
		super(container, cmd, perm, usage, args, consoleUse);
		this.module = module;
	}

	/**
	 * @return the module held by the command
	 */
	public T getModule() {
		return module;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
		if (!module.isEnabled()) {
			return false;
		}
		super.onCommand(sender, cmd, str, args);
		return true;
	}

}
