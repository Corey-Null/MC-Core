package net.expvp.core.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.expvp.api.TextUtil;
import net.expvp.api.interfaces.ICommand;
import net.expvp.api.interfaces.logging.ILogger;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.core.NullContainer;

/**
 * Base abstract core command
 * 
 * @author NullUser
 * @see ICommand
 */
public abstract class NullCommand implements ICommand {

	private final String command;
	private final NullContainer container;
	private final String permission;
	private final int requiredArgs;
	private final String usage;
	private final boolean consoleUse;

	/**
	 * Initializes all components of the command
	 * 
	 * @param container
	 * @param cmd
	 * @param perm
	 * @param usage
	 * @param args
	 * @param consoleUse
	 */
	public NullCommand(NullContainer container, String cmd, String perm, String usage, int args, boolean consoleUse) {
		this.command = cmd;
		this.container = container;
		this.permission = perm;
		this.requiredArgs = args;
		this.usage = usage;
		this.consoleUse = consoleUse;
	}

	/**
	 * @see ICommand
	 */
	@Override
	public String getCommand() {
		return command;
	}

	/**
	 * @see ICommand
	 */
	@Override
	public NullContainer getContainer() {
		return container;
	}

	/**
	 * @see ICommand
	 */
	@Override
	public String getPermission() {
		return permission;
	}

	/**
	 * @see ICommand
	 */
	@Override
	public int getRequiredArgs() {
		return requiredArgs;
	}

	/**
	 * @see ICommand
	 */
	@Override
	public String getUsage() {
		return usage;
	}

	/**
	 * @see ICommand
	 */
	@Override
	public boolean canConsoleUse() {
		return consoleUse;
	}

	/**
	 * @see CommandExecutor
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
		ILogger logger = container.getCommandLogger();
		String msg = cmd.getLabel() + " " + TextUtil.connect(args, 0, args.length);
		if (!consoleUse && !checkSender(sender, true)) {
			return true;
		}
		if (!checkPermission(sender, permission, true)) {
			logger.warn(sender.getName() + " tried to use the command /" + cmd.getLabel() + " without the permission "
					+ permission);
			return true;
		}
		if (args.length < requiredArgs) {
			sendUsage(sender);
			logger.log(sender.getName() + " Used the command /");
			return true;
		}
		try {
			execute(sender, args);
			logger.log(sender.getName() + " Used the command /" + msg);
		} catch (Exception ex) {
			sender.sendMessage(ChatColor.RED + "[" + ex.toString() + "] error occurred when running this command.");
			sender.sendMessage(ChatColor.RED + "Please contact the staff with a screenshot of these messages.");
			logger.error(sender.getName() + " Tried to use the command /" + msg, ex);
			if (!container.getPlugin().getConfig().getBoolean("supress-command-errors", false)) {
				ex.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * @param name
	 * @return Player based off the name
	 */
	public Player getPlayer(String name) {
		OfflinePlayerAccount account = container.getPlayerDataHandler().getAccount(name);
		if (account == null || !account.isOnline()) {
			return null;
		}
		return account.getOnlineAccount().getPlayer();
	}

	/**
	 * @param key
	 *            Of message
	 * @param args
	 *            To replace in message
	 * @return formatted message
	 */
	public String getMessage(String key, Object... args) {
		return container.getMessages().getMessage(command + "." + key, args);
	}

	/**
	 * Sends a message to the user
	 * 
	 * @param sender
	 * @param target
	 * @param key
	 * @param args
	 */
	public void sendMessage(CommandSender sender, OfflinePlayerAccount target, String key, Object... args) {
		container.getMessages().sendMessage(sender, target, command + "." + key, args);
	}

	/**
	 * Sends a message to the user
	 * 
	 * @param sender
	 * @param target
	 * @param key
	 * @param args
	 */
	public void sendMessage(OnlinePlayerAccount sender, OfflinePlayerAccount target, String key, Object... args) {
		sendMessage(sender.getPlayer(), target, command + "." + key, args);
	}

	/**
	 * Sends usage message to the sender
	 * 
	 * @param sender
	 */
	public void sendUsage(CommandSender sender) {
		container.getMessages().sendMessage(sender, null, "gen.usage-format", usage);
	}

	/**
	 * Checks permission of a sender
	 * 
	 * @param sender
	 * @param perm
	 *            Permission to test against the sender
	 * @param message
	 *            To define if the sender should receive a message
	 * @return true if the sender has permission defined by perm
	 */
	public boolean checkPermission(CommandSender sender, String perm, boolean message) {
		if (perm == null || !(checkSender(sender, false)) || sender.hasPermission(perm)) {
			return true;
		}
		if (message) {
			container.getMessages().sendMessage(sender, null, "gen.no-permission");
		}
		return false;
	}

	/**
	 * Checks if sender is a player
	 * 
	 * @param sender
	 * @param message
	 *            To define if the sender should receive a message
	 * @return true if the sender is a player
	 */
	public boolean checkSender(CommandSender sender, boolean message) {
		if (sender instanceof Player) {
			return true;
		}
		if (message) {
			container.getMessages().sendMessage(sender, null, "gen.console-restricted");
		}
		return false;
	}

	/**
	 * Gets the account of a command sender
	 * 
	 * @param sender
	 * @return the online player account instance of the sender
	 */
	public OnlinePlayerAccount getAccount(CommandSender sender) {
		if (!(sender instanceof Player)) {
			return null;
		}
		return container.getPlayerDataHandler().getAccount(((Player) sender).getUniqueId()).getOnlineAccount();
	}

	/**
	 * Gets the account from a string
	 * 
	 * @param sender
	 * @return the online player account instance of the name, null if there's
	 *         no account
	 */
	public OnlinePlayerAccount checkAccount(CommandSender sender, String arg, boolean message) {
		OfflinePlayerAccount acc = getContainer().getPlayerDataHandler().getAccount(arg);
		if (acc == null || !acc.isOnline()) {
			if (message) {
				getContainer().getMessages().sendMessage(sender, null, "gen.account-offline", arg);
			}
			return null;
		}
		return acc.getOnlineAccount();
	}

	/**
	 * Used to execute the command
	 * 
	 * @param sender
	 * @param args
	 */
	public abstract void execute(CommandSender sender, String[] args);

}
