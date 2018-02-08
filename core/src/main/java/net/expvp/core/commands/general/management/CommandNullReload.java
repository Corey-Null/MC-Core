package net.expvp.core.commands.general.management;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import net.expvp.api.interfaces.Reloadable;
import net.expvp.api.interfaces.plugin.IModule;
import net.expvp.core.NullContainer;
import net.expvp.core.commands.NullCommand;
import net.expvp.core.plugin.CorePlugin;

/**
 * @author NullUser
 * @see NullCommand
 */
public class CommandNullReload extends NullCommand {

	/**
	 * @see NullCommand
	 * @param container
	 */
	public CommandNullReload(NullContainer container) {
		super(container, "nullreload", "core.reload", "/nullreload", 0, true);
	}

	/**
	 * @see NullCommand
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length > 0) {
			IModule<?> module = getContainer().getModule(args[0]);
			if (module != null) {
				sendMessage(sender, null, "plugin.start");
				long start = System.currentTimeMillis();
				module.reload();
				double time = (System.currentTimeMillis() - start) / 1000.0;
				sendMessage(sender, null, "plugin.finish", time);
				return;
			}
			Plugin plugin = (Plugin) Bukkit.getPluginManager().getPlugin(args[0]);
			if (plugin == null) {
				sendMessage(sender, null, "plugin.not-exist");
				return;
			}
			if (!plugin.isEnabled()) {
				sendMessage(sender, null, "plugin.start");
				long start = System.currentTimeMillis();
				plugin.onEnable();
				double time = (System.currentTimeMillis() - start) / 1000.0;
				sendMessage(sender, null, "plugin.finish", time);
				return;
			} else {
				if (plugin instanceof CorePlugin) {
					sendMessage(sender, null, "start");
					long start = System.currentTimeMillis();
					getContainer().reload();
					double time = (System.currentTimeMillis() - start) / 1000.0;
					sendMessage(sender, null, "finish", time);
				} else if (plugin instanceof Reloadable) {
					sendMessage(sender, null, "plugin.start");
					long start = System.currentTimeMillis();
					((Reloadable) plugin).reload();
					double time = (System.currentTimeMillis() - start) / 1000.0;
					sendMessage(sender, null, "plugin.finish", time);
				} else {
					sendMessage(sender, null, "plugin.start");
					long start = System.currentTimeMillis();
					plugin.onDisable();
					plugin.onEnable();
					double time = (System.currentTimeMillis() - start) / 1000.0;
					sendMessage(sender, null, "plugin.finish", time);
				}
				return;
			}
		}
		sendMessage(sender, null, "start");
		long start = System.currentTimeMillis();
		getContainer().reload();
		double time = (System.currentTimeMillis() - start) / 1000.0;
		sendMessage(sender, null, "finish", time);
	}

}
