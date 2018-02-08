package net.expvp.core.plugin.modules.permissions.commands;

import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.google.common.collect.Sets;

import net.expvp.api.NumberUtil;
import net.expvp.api.TextUtil;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.core.plugin.modules.permissions.groups.Rank;
import net.expvp.core.plugin.modules.permissions.groups.RankHandler;

/**
 * Class for handling /nullpermissions group ...
 * 
 * @author NullUser
 * @see PermissionsSubCommand
 */
public class PermissionsGroupSubCommand extends PermissionsSubCommand {

	public PermissionsGroupSubCommand(PermissionsCommand command) {
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
		RankHandler handler = command.getModule().getRankHandler();
		if (args[0].equalsIgnoreCase("list")) {
			if (!command.checkPermission(sender, "core.nullperms.group.list", true)) {
				return;
			}
			Set<OfflinePlayerAccount> accounts = command.getContainer().getPlayerDataHandler().getCachedAccounts();
			sender.sendMessage(c1 + "---" + c2 + "NullPerms Groups" + c1 + "---");
			for (Rank rank : handler.getCachedRanks()) {
				long size = accounts.stream().filter(a -> a.getRank() != null && a.getRank().equals(rank)).count();
				sender.sendMessage("  " + c2 + rank.getName() + c3 + " Users: " + size);
			}
			return;
		}
		if (!TextUtil.isAlphaNumeric(args[0])) {
			container.getMessages().sendMessage(sender, null, "gen.non-alpha-numeric", args[0]);
			return;
		}
		Rank rank = handler.getRank(args[0]);
		if (args.length < 2) {
			if (!command.checkPermission(sender, "core.nullperms.group.view", true)) {
				return;
			}
			if (rank == null) {
				command.sendMessage(sender, null, "group.does-not-exist", args[0]);
				return;
			}
			sender.sendMessage(c1 + "---" + c2 + rank.getName() + c1 + "---");
			String pref = rank.getPrefix();
			sender.sendMessage(c2 + " Prefix: " + c3 + "\"" + (pref == null ? "" : pref) + "\"");
			String suff = rank.getSuffix();
			sender.sendMessage(c2 + " Suffix: " + c3 + "\"" + (suff == null ? "" : suff) + "\"");
			sender.sendMessage(c2 + " Ladder: " + rank.getLadder());
			sender.sendMessage(c2 + " Inherited Groups:");
			for (String r : rank.getInheritedRanks()) {
				sender.sendMessage(c3 + "  " + r);
			}
			sender.sendMessage(c2 + " Permissions:");
			for (String perm : rank.getPermissions()) {
				sender.sendMessage(c3 + "  " + perm);
			}
			for (String perm : rank.getNonPermissions()) {
				sender.sendMessage(c3 + "  -" + perm);
			}
			return;
		}
		FileConfiguration config = handler.getConfig();
		switch (args[1].toLowerCase()) {
		case "inherit": {
			if (!command.checkPermission(sender, "core.nullperms.group.inherit", true)) {
				return;
			}
			if (args.length < 3) {
				command.sendHelp(sender, PermissionsCommand.INDEX_GROUPS);
				return;
			}
			if (rank == null) {
				command.sendMessage(sender, null, "group.does-not-exist", args[0]);
				return;
			}
			Rank toInherit = handler.getRank(args[2]);
			if (toInherit == null) {
				command.sendMessage(sender, null, "group.does-not-exist", args[0]);
				return;
			}
			if (!rank.inheritsRank(toInherit)) {
				rank.addInheritedRank(args[2]);
				String key = rank.getName() + ".inherited-groups";
				List<String> list = config.getStringList(key);
				list.add(args[2]);
				config.set(key, list);
			}
			command.sendMessage(sender, null, "group.inherit", rank.getName(), toInherit.getName());
			break;
		}
		case "add": {
			if (!command.checkPermission(sender, "core.nullperms.group.perms", true)) {
				return;
			}
			if (args.length < 3) {
				command.sendHelp(sender, PermissionsCommand.INDEX_GROUPS);
				return;
			}
			if (rank == null) {
				command.sendMessage(sender, null, "group.does-not-exist", args[0]);
				return;
			}
			String key = rank.getName() + ".permissions";
			List<String> permissions = config.getStringList(key);
			if (sender instanceof Player && args[2].equals("*")) {
				sender.sendMessage(ChatColor.RED + "Only the console can add wildcard perms.");
				return;
			}
			permissions.add(args[2]);
			rank.setPermission(args[2], true);
			config.set(key, permissions);
			command.sendMessage(sender, null, "group.addperm", args[2], rank.getName());
			break;
		}
		case "remove": {
			if (!command.checkPermission(sender, "core.nullperms.group.perms", true)) {
				return;
			}
			if (args.length < 3) {
				command.sendHelp(sender, PermissionsCommand.INDEX_GROUPS);
				return;
			}
			if (rank == null) {
				command.sendMessage(sender, null, "group.does-not-exist", args[0]);
				return;
			}
			String key = rank.getName() + ".permissions";
			List<String> permissions = config.getStringList(key);
			permissions.remove(args[2]);
			rank.setPermission(args[2], false);
			config.set(key, permissions);
			command.sendMessage(sender, null, "group.removeperm", args[2], rank.getName());
			break;
		}
		case "prefix": {
			if (!command.checkPermission(sender, "core.nullperms.group.prefix", true)) {
				return;
			}
			if (args.length < 3) {
				command.sendHelp(sender, PermissionsCommand.INDEX_GROUPS);
				return;
			}
			if (rank == null) {
				command.sendMessage(sender, null, "group.does-not-exist", args[0]);
				return;
			}
			String prefix;
			if (args[2].startsWith("\"")) {
				String str = TextUtil.connect(args, 2, args.length);
				prefix = TextUtil.loadQuotes(str);
			} else {
				prefix = args[2];
			}
			if (prefix.isEmpty()) {
				rank.setPrefix(null);
				config.set(rank.getName() + ".prefix", null);
			} else {
				rank.setPrefix(prefix);
				config.set(rank.getName() + ".prefix", prefix);
			}
			command.sendMessage(sender, null, "group.setprefix", rank.getName(), prefix);
			break;
		}
		case "suffix": {
			if (!command.checkPermission(sender, "core.nullperms.group.suffix", true)) {
				return;
			}
			if (args.length < 3) {
				command.sendHelp(sender, PermissionsCommand.INDEX_GROUPS);
				return;
			}
			if (rank == null) {
				command.sendMessage(sender, null, "group.does-not-exist", args[0]);
				return;
			}
			String suffix;
			if (args[2].startsWith("\"")) {
				String str = TextUtil.connect(args, 2, args.length);
				suffix = TextUtil.loadQuotes(str);
			} else {
				suffix = args[2];
			}
			if (suffix.isEmpty()) {
				rank.setSuffix(null);
				config.set(rank.getName() + ".suffix", null);
			} else {
				rank.setSuffix(suffix);
				config.set(rank.getName() + ".suffix", suffix);
			}
			command.sendMessage(sender, null, "group.setsuffix", rank.getName(), suffix);
			break;
		}
		case "delete": {
			if (!command.checkPermission(sender, "core.nullperms.group.delete", true)) {
				return;
			}
			if (rank == null) {
				command.sendMessage(sender, null, "group.does-not-exist", args[0]);
				return;
			}
			handler.deleteRank(rank);
			command.sendMessage(sender, null, "group.delete", rank.getName());
			break;
		}
		case "create": {
			if (!command.checkPermission(sender, "core.nullperms.group.create", true)) {
				return;
			}
			if (rank != null) {
				command.sendMessage(sender, null, "group.already-exists", args[0]);
				return;
			}
			if (!TextUtil.isAlphaNumeric(args[0])) {
				command.sendMessage(sender, null, "gen.non-alpha-numeric", args[0]);
			}
			handler.createRank(args[0], Sets.newHashSet(),
					args.length > 1 && NumberUtil.isInteger(args[1]) ? NumberUtil.getInteger(args[1]) : 1);
			command.sendMessage(sender, null, "group.create", args[0]);
			break;
		}
		}
	}

}
