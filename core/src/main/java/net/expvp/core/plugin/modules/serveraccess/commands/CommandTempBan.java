package net.expvp.core.plugin.modules.serveraccess.commands;

import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.expvp.api.TextUtil;
import net.expvp.api.TimeUtil;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.core.player.ConsoleAccount;
import net.expvp.core.player.WildcardPlayerAccount;
import net.expvp.core.plugin.modules.serveraccess.ServerAccessModule;
import net.expvp.core.plugin.modules.serveraccess.data.BanType;
import net.expvp.core.plugin.modules.serveraccess.data.UUIDBanData;

/**
 * Class used for handling command /tempban
 * 
 * @author NullUser
 * @see BanCommand
 */
public class CommandTempBan extends BanCommand {

	private final static Pattern TIMES = Pattern.compile("[wdhms]");

	public CommandTempBan(ServerAccessModule module) {
		super(module, "tempban", "core.tempban", "/tempban <player> <time|wmdhs> [reason]", 2);
	}

	/**
	 * @see BanCommand
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		OfflinePlayerAccount acc = getAccount(args[0]);
		if (acc == null || acc instanceof WildcardPlayerAccount || acc instanceof ConsoleAccount) {
			getContainer().getMessages().sendMessage(sender, null, "gen.account-does-not-exist", args[0]);
			return;
		}
		String reason;
		if (args.length > 2) {
			reason = TextUtil.connect(args, 2, args.length);
		} else {
			reason = getModule().getDefaultBanReason();
		}
		String time = args[1];
		Bukkit.broadcastMessage(time);
		try {
			time = time.replaceAll("[^wdhmsWDHMS0-9]", "");
			StringBuilder builder = new StringBuilder();
			char[] array = time.toCharArray();
			for (int i = 0; i < array.length; i++) {
				char c = array[i];
				builder.append(c);
				if (args.length > i + 1) {
					char next = array[i + 1];
					if (Character.isLetter(next)) {
						builder.append(' ');
						builder.append(next);
						while (args.length > (i = i + 1) + 1 && Character.isLetter(array[i + 1])) {
							builder.append(next);
						}
						builder.append(' ');
					}
				}
			}
			time = builder.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Bukkit.broadcastMessage(time);
		String[] parts = time.split(" ");
		long expiresIn = 0;
		for (int i = 0; i < parts.length; i++) {
			String part1 = parts[i];
			if (parts.length <= i + 1) {
				break;
			}
			String part2 = parts[i + 1];
			if (TIMES.matcher(part1).find() || !TIMES.matcher(part2).find()) {
				continue;
			}
			int t = Integer.parseInt(part1);
			if (part2.contains("w")) {
				expiresIn += t * 60 * 60 * 24 * 7;
			} else if (part2.contains("d")) {
				expiresIn += t * 60 * 60 * 24;
			} else if (part2.contains("h")) {
				expiresIn += t * 60 * 60;
			} else if (part2.contains("m")) {
				expiresIn += t * 60;
			} else if (part2.contains("s")) {
				expiresIn += t;
			}
			i++;
		}
		if (expiresIn <= 0) {
			sendMessage(sender, null, "malformed-time", args[1]);
			return;
		}
		expiresIn = expiresIn * 1000;
		long current = System.currentTimeMillis();
		long expire = current + expiresIn;
		String date = TimeUtil.getTime(current, expire);
		UUIDBanData data = new UUIDBanData(acc.getUUID(), BanType.TEMP_BAN);
		data.setBegin(current);
		data.setExpire(expire);
		data.setExpired(false);
		data.setHander(sender instanceof Player ? ((Player) sender).getUniqueId() : null);
		data.setReason(reason);
		if (manager().ban(acc.getUUID(), data)) {
			sendMessage(sender, acc, "execute", date);
			if (acc.isOnline()) {
				removeFromServer(acc.getOnlineAccount().getPlayer(), data);
			}
		} else {
			sendMessage(sender, acc, "already-banned");
		}
	}

}
