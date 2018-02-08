package net.expvp.core.commands.general.ip;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.google.common.collect.Maps;

import net.expvp.api.NumberUtil;
import net.expvp.api.bukkit.chat.Chat;
import net.expvp.api.bukkit.chat.ChatComponent;
import net.expvp.api.bukkit.chat.Component;
import net.expvp.api.interfaces.pages.IPage;
import net.expvp.api.interfaces.pages.IPager;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.api.pages.Pager;
import net.expvp.core.NullContainer;
import net.expvp.core.commands.NullCommand;
import net.expvp.core.ip.IPMatcher;
import net.expvp.core.player.ConsoleAccount;
import net.expvp.core.player.WildcardPlayerAccount;

/**
 * Class used for handling command /checkalts
 * 
 * @author NullUser
 * @see NullCommand
 */
public class CommandCheckAlts extends NullCommand {

	private final static IPMatcher<OfflinePlayerAccount> matcher = new IPMatcher<>();

	private final Map<UUID, IPager<OfflinePlayerAccount>> cachedPagers;

	public CommandCheckAlts(NullContainer container) {
		super(container, "checkalts", "core.checkalts", "/checkalts <player> [page]", 1, true);
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
		IPager<OfflinePlayerAccount> pager;
		if (cachedPagers.containsKey(account.getUUID())) {
			pager = cachedPagers.get(account.getUUID());
		} else {
			pager = Pager.parse(matcher.match(account, getContainer().getPlayerDataHandler().getCachedAccounts()));
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
		if (pager.getDefaultCollection().size() == 0) {
			sendMessage(sender, null, "no-results");
		} else {
			sendMessage(sender, null, "header");
			String format = getMessage("format");
			IPage<OfflinePlayerAccount> page = pager.getPage(pageID - 1);
			for (OfflinePlayerAccount alt : page.getPageItems()) {
				ChatComponent component = new ChatComponent();
				component.add(new Component(format));
				component.insert("{target}", getContainer().getPlayerFormatController().get(" ").make(alt));
				Chat.sendMessage(sender, component);
				sender.sendMessage(format.replace("{0}", alt.getName()));
			}
			sendMessage(sender, null, "footer", pageID, pager.getPageAmount());
		}
	}

}
