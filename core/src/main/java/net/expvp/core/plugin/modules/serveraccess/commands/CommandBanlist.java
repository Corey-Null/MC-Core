package net.expvp.core.plugin.modules.serveraccess.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.command.CommandSender;

import com.google.common.collect.Lists;

import net.expvp.api.NumberUtil;
import net.expvp.api.TimeUtil;
import net.expvp.api.interfaces.pages.IPage;
import net.expvp.api.interfaces.pages.IPager;
import net.expvp.api.pages.Pager;
import net.expvp.core.ip.IPAddress;
import net.expvp.core.plugin.modules.serveraccess.ServerAccessModule;
import net.expvp.core.plugin.modules.serveraccess.data.BanData;

/**
 * Class used for handling command /banlist
 * 
 * @author NullUser
 * @see BanCommand
 */
public class CommandBanlist extends BanCommand {

	public CommandBanlist(ServerAccessModule module) {
		super(module, "banlist", "core.banlist", "/banlist <ip|player> [page]", 0);
	}

	/**
	 * @see BanCommand
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		String type = args.length > 0 ? args[0] : "all";
		List<String> data = Lists.newArrayList();
		int p = args.length > 1 ? (NumberUtil.isInteger(args[1]) ? NumberUtil.getInteger(args[1]) : 1) : 1;
		switch (type.toLowerCase()) {
		case "ip":
			data.addAll(addToList(manager().getActiveIPBans()));
			break;
		case "player":
			data.addAll(addToList(manager().getActiveUUIDBans()));
			break;
		case "all":
		default:
			data.addAll(addToList(manager().getActiveUUIDBans()));
			data.addAll(addToList(manager().getActiveIPBans()));
			break;
		}
		if (data.size() == 0) {
			sendMessage(sender, null, "no-list");
			return;
		}
		IPager<String> pager = Pager.parse(data);
		IPage<String> page = pager.getPage(p - 1);
		if (page == null) {
			sendMessage(sender, null, "invalid-page", pager.getPageAmount());
			return;
		}
		sendMessage(sender, null, "header");
		Iterator<String> iterator = page.getPageItems().iterator();
		while (iterator.hasNext()) {
			sender.sendMessage(iterator.next());
		}
		sendMessage(sender, null, "footer", p, pager.getPageAmount());
	}

	/**
	 * Adds a map of ban data to a list
	 * 
	 * @param map
	 *            To add from
	 * @param list
	 *            To add to
	 */
	public <K> List<String> addToList(Map<K, BanData<K>> map) {
		List<String> list = new ArrayList<>();
		for (Entry<K, BanData<K>> entry : map.entrySet()) {
			String key = String.valueOf(entry.getKey());
			String date = TimeUtil.formatDate(entry.getValue().getBegin(), true);
			String type = IPAddress.isIP(key) ? "ip" : "uuid";
			if (type.equals("uuid")) {
				key = getContainer().getPlayerDataHandler().getAccount(UUID.fromString(key)).getName();
			}
			list.add(getMessage(type + "-format", key, date));
		}
		return list;
	}

}
