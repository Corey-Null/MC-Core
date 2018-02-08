package net.expvp.core.plugin.modules.permissions.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import net.expvp.core.commands.ModuleCommand;
import net.expvp.core.plugin.modules.permissions.PermissionsModule;

/**
 * Command for handling /nullpermissions
 * 
 * @author NullUser
 * @see ModuleCommand
 */
public class PermissionsCommand extends ModuleCommand<PermissionsModule> {

	protected final static int INDEX_NORMAL = 0xA1;
	protected final static int INDEX_USERS = 0xB1;
	protected final static int INDEX_GROUPS = 0xB2;

	private final PermissionsUserSubCommand userCommand;
	private final PermissionsGroupSubCommand groupCommand;

	public PermissionsCommand(PermissionsModule module) {
		super(module.getContainer(), module, "nullpermissions", null, "/nullpermissions help", 0, true);
		this.userCommand = new PermissionsUserSubCommand(this);
		this.groupCommand = new PermissionsGroupSubCommand(this);
	}

	/**
	 * @see ModuleCommand
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
			sendHelp(sender, INDEX_NORMAL);
			return;
		}
		switch (args[0].toLowerCase()) {
		case "user":
			if (args.length < 2) {
				sendHelp(sender, INDEX_USERS);
				return;
			}
			userCommand.execute(sender, voidStart(args));
			break;
		case "group":
			if (args.length < 2) {
				sendHelp(sender, INDEX_GROUPS);
				return;
			}
			groupCommand.execute(sender, voidStart(args));
			break;
		default: {
			sendHelp(sender, INDEX_NORMAL);
			break;
		}
		}
	}

	/**
	 * Sends a help message to the sender
	 * 
	 * @param sender
	 *            To send the help message to
	 * @param index
	 *            To match the help message
	 */
	public void sendHelp(CommandSender sender, int index) {
		ChatColor c1 = ChatColor.GRAY;
		ChatColor c2 = ChatColor.GOLD;
		ChatColor c3 = ChatColor.RED;
		if (!checkPermission(sender, "core.nullperms.help", true)) {
			return;
		}
		String header = c1 + "---" + c2 + "NullPermissions" + c1 + "---";
		ChatColor cmd = c2;
		ChatColor desc = c3;
		switch (index) {
		case INDEX_NORMAL: {
			sender.sendMessage(header);
			sender.sendMessage(cmd + "/nullpermissions group " + desc + "displays help message for groups");
			sender.sendMessage(cmd + "/nullpermissions user " + desc + "displays help message regarding users");
			sender.sendMessage(cmd + "/nullpermissions help " + desc + "displays this help message");
			break;
		}
		case INDEX_USERS: {
			sender.sendMessage(header);
			sender.sendMessage(cmd + "... <user> add <perm> " + desc + "adds a permission to the user.");
			sender.sendMessage(cmd + "... <user> remove <perm> " + desc + "removes a permission from the user.");
			sender.sendMessage(cmd + "... <user> prefix <prefix> " + desc + "sets the user's prefix.");
			sender.sendMessage(cmd + "... <user> suffix <suffix> " + desc + "sets the user's suffix.");
			sender.sendMessage(cmd + "... <user> group add <group> " + desc + "adds a group to inherit");
			sender.sendMessage(cmd + "... <user> group remove <group> " + desc + "removes inherited gruop");
			sender.sendMessage(cmd + "... <user> group set <group> " + desc + "sets a player to the specified group.");
			sender.sendMessage(cmd + "... <user> " + desc + "views the variables of the user.");
			break;
		}
		case INDEX_GROUPS: {
			sender.sendMessage(header);
			sender.sendMessage(cmd + "... <group> inherit <group> " + desc + "adds a group for that group to inherit.");
			sender.sendMessage(cmd + "... <group> add <perm> " + desc + "adds a permission to the group.");
			sender.sendMessage(cmd + "... <group> remove <perm> " + desc + "removes a permission from the group.");
			sender.sendMessage(cmd + "... <group> prefix <prefix> " + desc + "sets the group's prefix.");
			sender.sendMessage(cmd + "... <group> suffix <suffix> " + desc + "sets the group's suffix.");
			sender.sendMessage(cmd + "... <group> create [ladder] " + desc + "creates a group.");
			sender.sendMessage(cmd + "... <group> delete " + desc + "deletes a group.");
			sender.sendMessage(cmd + "... list " + desc + "lists all groups.");
			sender.sendMessage(cmd + "... <group> " + desc + "views the variables of the group.");
			break;
		}
		default: {
			sender.sendMessage(ChatColor.RED + "Vital error, contact dev for help.");
			System.out.println("Index " + index + " not matched to a help message.");
		}
		}
	}

	/**
	 * Voids the first argument of an array of strings
	 * 
	 * @param original
	 *            To modify
	 * @return the modified array
	 */
	private String[] voidStart(String[] original) {
		int newLength = original.length - 1;
		String[] newArgs = new String[newLength];
		System.arraycopy(original, 1, newArgs, 0, newLength);
		return newArgs;
	}

}
