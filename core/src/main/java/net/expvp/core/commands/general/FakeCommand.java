package net.expvp.core.commands.general;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.expvp.core.NullContainer;
import net.expvp.core.commands.NullCommand;

/**
 * Fake command for testing the logging of exceptions
 * 
 * @author NullUser
 * @see NullCommand
 */
public class FakeCommand extends NullCommand {

	public FakeCommand(NullContainer container) {
		super(container, "fake", "core.*", "", 0, true);
	}

	/**
	 * @see NullCommand
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		sender.sendMessage(ChatColor.RED + "This is a null command, used for testing.");
		sender.sendMessage(ChatColor.RED + "This command has no actual use for players.");
		sender.sendMessage(" ");
		Bukkit.broadcastMessage(null);
	}

}
