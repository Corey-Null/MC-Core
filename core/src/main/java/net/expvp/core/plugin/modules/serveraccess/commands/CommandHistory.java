package net.expvp.core.plugin.modules.serveraccess.commands;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.command.CommandSender;

import com.google.common.collect.Lists;

import net.expvp.api.NumberUtil;
import net.expvp.api.TimeUtil;
import net.expvp.api.interfaces.ip.IAddress;
import net.expvp.api.interfaces.pages.IPage;
import net.expvp.api.interfaces.pages.IPager;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.api.pages.Pager;
import net.expvp.core.ip.IPAddress;
import net.expvp.core.player.ConsoleAccount;
import net.expvp.core.player.WildcardPlayerAccount;
import net.expvp.core.plugin.modules.serveraccess.ServerAccessModule;
import net.expvp.core.plugin.modules.serveraccess.data.BanData;

/**
 * Class used for handling command /history
 * 
 * @author NullUser
 * @see BanCommand
 */
public class CommandHistory extends BanCommand {

	public CommandHistory(ServerAccessModule module) {
		super(module, "history", "core.history", "/history <player|ip> [page]");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		String name;
		int page = 1;
		if (args.length > 1 && NumberUtil.isInteger(args[1])) {
			page = NumberUtil.getInteger(args[1]);
			if (page < 0) {
				page = -page;
			}
		}
		if (IPAddress.isIP(args[0])) {
			IAddress ip = new IPAddress(args[0]);
			name = ip.getAddress();
			BanData<IAddress> data = manager().getActiveIPBans().get(ip);
			Set<BanData<IAddress>> history = manager().getIpBanHistory().get(ip);
			if (data != null || (history != null && !history.isEmpty())) {
				sender.sendMessage(getMessage("header").replace("{target}", ip.getAddress()));
				sendIPBanData(sender, data, history, page);
				return;
			}
		} else {
			OfflinePlayerAccount acc = getContainer().getPlayerDataHandler().getAccount(args[0]);
			if (acc == null || acc instanceof WildcardPlayerAccount || acc instanceof ConsoleAccount) {
				getContainer().getMessages().sendMessage(sender, null, "gen.account-does-not-exist", args[0]);
				return;
			}
			name = acc.getName();
			UUID id = acc.getUUID();
			BanData<UUID> data = manager().getActiveUUIDBans().get(id);
			Set<BanData<UUID>> history = manager().getUuidBanHistory().get(id);
			if (data != null || (history != null && !history.isEmpty())) {
				sendMessage(sender, acc, "header");
				sendUUIDBanData(sender, data, history, page);
				return;
			}
		}
		sendMessage(sender, null, "no-history-message", name);
	}

	/**
	 * Sends UUID Ban data
	 * 
	 * @param s
	 *            To send the data to
	 * @param current
	 *            The current ban
	 * @param history
	 *            The history
	 * @param p
	 *            The page
	 */
	public void sendUUIDBanData(CommandSender s, BanData<UUID> current, Set<BanData<UUID>> history, int p) {
		List<String> list = Lists.newArrayList();
		if (current != null) {
			list.add(formatCurrent(current));
		}
		if (history != null) {
			Iterator<BanData<UUID>> iterator = history.iterator();
			while (iterator.hasNext()) {
				list.add(format(iterator.next()));
			}
		}
		String size = getMessage("page-size");
		send(s, p, Pager.parse(list, NumberUtil.isInteger(size) ? NumberUtil.getInteger(size) : 3));
	}

	/**
	 * Sends IP Ban data
	 * 
	 * @param s
	 *            To send the data to
	 * @param current
	 *            The current ban
	 * @param history
	 *            The history
	 * @param p
	 *            The page
	 */
	public void sendIPBanData(CommandSender s, BanData<IAddress> current, Set<BanData<IAddress>> history, int p) {
		List<String> list = Lists.newArrayList();
		if (current != null) {
			list.add(formatCurrent(current));
		}
		if (history != null) {
			Iterator<BanData<IAddress>> iterator = history.iterator();
			while (iterator.hasNext()) {
				list.add(format(iterator.next()));
			}
		}
		String size = getMessage("page-size");
		send(s, p, Pager.parse(list, NumberUtil.isInteger(size) ? NumberUtil.getInteger(size) : 3));
	}

	/**
	 * Sends paging data to the sender
	 * 
	 * @param sender
	 *            The sender
	 * @param where
	 *            The page
	 * @param pager
	 *            The pager
	 */
	public void send(CommandSender sender, int where, IPager<String> pager) {
		where = where - 1;
		IPage<String> page = pager.getPage(where);
		if (page == null) {
			sendMessage(sender, null, "invalid-page", pager.getPageAmount());
			return;
		}
		String splitter = getMessage("splitter");
		Iterator<String> iterator = page.getPageItems().iterator();
		while (iterator.hasNext()) {
			String str = iterator.next();
			sender.sendMessage(str);
			if (iterator.hasNext()) {
				sender.sendMessage(splitter);
			}
		}
		sendMessage(sender, null, "footer", where + 1, pager.getPageAmount());
	}

	/**
	 * @param data
	 *            To format
	 * @return the formatted data
	 */
	public String formatCurrent(BanData<?> data) {
		String start = TimeUtil.formatDate(data.getBegin(), true);
		String end = data.getExpire() == -1 ? "PERMANENT" : TimeUtil.formatDate(data.getExpire(), true);
		String name = data.getHander() == null ? "Console"
				: getContainer().getPlayerDataHandler().getAccount(data.getHander()).getName();
		return getMessage("current-ban", start, end, name, data.getReason());
	}

	/**
	 * @param data
	 *            To format
	 * @return the formatted data
	 */
	public String format(BanData<?> data) {
		String start = TimeUtil.formatDate(data.getBegin(), true);
		String name = data.getHander() == null ? "Console"
				: getContainer().getPlayerDataHandler().getAccount(data.getHander()).getName();
		return getMessage("format", start, data.getType().name().replace('_', ' '), name, data.getReason());
	}

}
