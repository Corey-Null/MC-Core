package net.expvp.core.plugin.modules.permissions.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.expvp.api.TextUtil;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.core.player.ConsoleAccount;
import net.expvp.core.player.WildcardPlayerAccount;
import net.expvp.core.plugin.modules.permissions.PlayerPermissions;
import net.expvp.core.plugin.modules.permissions.groups.Rank;

/**
 * Class for handling /nullpermissions user ...
 * 
 * @author NullUser
 * @see PermissionsSubCommand
 */
public class PermissionsUserSubCommand extends PermissionsSubCommand {

	public PermissionsUserSubCommand(PermissionsCommand command) {
		super(command);
	}

	/**
	 * @see PermissionsSubCommand
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		ChatColor c1 = ChatColor.GRAY;
		ChatColor c2 = ChatColor.GOLD;
		ChatColor c3 = ChatColor.RED;
		OfflinePlayerAccount account = container.getPlayerDataHandler().getAccount(args[0]);
		if (account == null || account instanceof WildcardPlayerAccount || account instanceof ConsoleAccount) {
			container.getMessages().sendMessage(sender, null, "gen.account-does-not-exist", args[0]);
			return;
		}
		if (args.length == 1) {
			if (!command.checkPermission(sender, "core.nullperms.user.view", true)) {
				return;
			}
			sender.sendMessage(c1 + "---" + c2 + account.getName() + c1 + "---");
			sender.sendMessage(c2 + " Rank: " + c3 + (account.getRank() == null ? " " : account.getRank().getName()));
			String pref = account.getPrefix();
			sender.sendMessage(c2 + " Prefix: " + c3 + "\"" + (pref == null ? "" : pref) + "\"");
			String suff = account.getSuffix();
			sender.sendMessage(c2 + " Suffix: " + c3 + "\"" + (suff == null ? "" : suff) + "\"");
			sender.sendMessage(c2 + " Inherited Groups:");
			for (String rank : account.getInheritedRanks()) {
				sender.sendMessage(c3 + "  " + rank);
			}
			sender.sendMessage(c2 + " Permissions:");
			for (String perm : account.getPermissions()) {
				sender.sendMessage(c3 + "  " + perm);
			}
			for (String perm : account.getNonPermissions()) {
				sender.sendMessage(c3 + "  -" + perm);
			}
			return;
		}
		PlayerPermissions perms = module.getPlayerPermissions();
		switch (args[1]) {
		case "add": {
			if (!command.checkPermission(sender, "core.nullperms.user.perms", true)) {
				return;
			}
			if (args.length < 3) {
				command.sendHelp(sender, PermissionsCommand.INDEX_USERS);
				return;
			}
			if (sender instanceof Player && args[2].equals("*")) {
				sender.sendMessage(ChatColor.RED + "Only the console can add wildcard perms.");
				return;
			}
			perms.addPermission(account, args[2]);
			command.sendMessage(sender, account, "users.addperm", args[2]);
			break;
		}
		case "remove": {
			if (!command.checkPermission(sender, "core.nullperms.user.perms", true)) {
				return;
			}
			if (args.length < 3) {
				command.sendHelp(sender, PermissionsCommand.INDEX_USERS);
				return;
			}
			perms.removePermission(account, args[2]);
			command.sendMessage(sender, account, "users.removeperm", args[2]);
			break;
		}
		case "prefix": {
			if (!command.checkPermission(sender, "core.nullperms.user.prefix", true)) {
				return;
			}
			if (args.length < 3) {
				command.sendHelp(sender, PermissionsCommand.INDEX_USERS);
				return;
			}
			String prefix;
			if (args[2].startsWith("\"")) {
				String str = TextUtil.connect(args, 2, args.length);
				prefix = TextUtil.loadQuotes(str);
			} else {
				prefix = args[2];
			}
			perms.setPrefix(account, prefix);
			command.sendMessage(sender, account, "users.setprefix", prefix);
			break;
		}
		case "suffix": {
			if (!command.checkPermission(sender, "core.nullperms.user.suffix", true)) {
				return;
			}
			if (args.length < 3) {
				command.sendHelp(sender, PermissionsCommand.INDEX_USERS);
				return;
			}
			String suffix;
			if (args[2].startsWith("\"")) {
				String str = TextUtil.connect(args, 2, args.length);
				suffix = TextUtil.loadQuotes(str);
			} else {
				suffix = args[2];
			}
			perms.setSuffix(account, suffix);
			command.sendMessage(sender, account, "users.setsuffix", suffix);
			break;
		}
		case "group": {
			if (!command.checkPermission(sender, "core.nullperms.user.modifygroups", true)) {
				return;
			}
			if (args.length < 4) {
				command.sendHelp(sender, PermissionsCommand.INDEX_USERS);
				return;
			}
			Rank rank = module.getRankHandler().getRank(args[3]);
			if (rank == null) {
				command.sendMessage(sender, null, "group.does-not-exist", args[3]);
				return;
			}
			switch (args[2].toLowerCase()) {
			case "add": {
				perms.handleRank(account, rank, true);
				command.sendMessage(sender, account, "users.addgroup", rank.getName());
				break;
			}
			case "remove": {
				perms.handleRank(account, rank, false);
				command.sendMessage(sender, account, "users.removegroup", rank.getName());
				break;
			}
			case "set": {
				account.setRank(rank);
				command.sendMessage(sender, account, "users.setgroup", rank.getName());
				break;
			}
			default: {
				command.sendHelp(sender, PermissionsCommand.INDEX_USERS);
				break;
			}
			}
			break;
		}
		}
	}

}
