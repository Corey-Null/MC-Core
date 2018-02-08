package net.expvp.core.commands.general.ip;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.google.common.collect.Maps;

import net.expvp.api.NumberUtil;
import net.expvp.api.interfaces.ip.IAddress;
import net.expvp.api.interfaces.pages.IPage;
import net.expvp.api.interfaces.pages.IPager;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.api.pages.Pager;
import net.expvp.core.NullContainer;
import net.expvp.core.commands.NullCommand;
import net.expvp.core.player.ConsoleAccount;
import net.expvp.core.player.WildcardPlayerAccount;

/**
 * Class used for handling the command /checkip
 * 
 * @author NullUser
 * @see NullCommand
 */
public class CommandCheckIP extends NullCommand {

	private final Map<UUID, IPager<IAddress>> cachedPagers;

	public CommandCheckIP(NullContainer container) {
		super(container, "checkip", "core.checkip", "/checkip <player> [page]", 1, true);
		this.cachedPagers = Maps.newHashMap();
	}

	/**
	 * @see NullCommand
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		OfflinePlayerAccount account = getContainer().getPlayerDataHandler().getAccount(args[0]);
		if (account == null || account instanceof WildcardPlayerAccount || account instanceof ConsoleAccount) {
			getContainer().getMessages().sendMessage(sender, null, "gen.account-does-not-exist", args[0]);
			return;
		}
		IPager<IAddress> pager;
		if (cachedPagers.containsKey(account.getUUID())) {
			pager = cachedPagers.get(account.getUUID());
		} else {
			pager = Pager.parse(account.getPastIPAddresses());
			cachedPagers.put(account.getUUID(), pager);
			Bukkit.getScheduler().scheduleSyncDelayedTask(getContainer().getPlugin(), () -> {
				cachedPagers.remove(account.getUUID());
			}, 20 * 60);
		}
		int pageID;
		if (args.length < 2) {
			pageID = 1;
		} else {
			if (!NumberUtil.isInteger(args[1])) {
				getContainer().getMessages().sendMessage(sender, null, "gen.invalid-number", args[1]);
				return;
			}
			pageID = Math.min(pager.getPageAmount(), NumberUtil.getInteger(args[1]));
		}
		sendMessage(sender, account, "main-header");
		sendMessage(sender, null, "base", account.getCurrentIPAddress().toString());
		if (pager.getDefaultCollection().size() == 0) {
			sendMessage(sender, null, "no-results");
		} else {
			sendMessage(sender, null, "others-header");
			String format = getMessage("others-format");
			IPage<IAddress> page = pager.getPage(pageID - 1);
			for (IAddress address : page.getPageItems()) {
				sender.sendMessage(format.replace("{0}", address.toString()));
			}
			sendMessage(sender, null, "others-footer", pageID, pager.getPageAmount());
		}
	}

}
