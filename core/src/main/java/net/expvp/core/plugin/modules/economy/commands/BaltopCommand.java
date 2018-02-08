package net.expvp.core.plugin.modules.economy.commands;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.Callable;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;

import net.expvp.api.NumberUtil;
import net.expvp.api.Pair;
import net.expvp.api.interfaces.pages.IPage;
import net.expvp.api.interfaces.pages.IPager;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.api.pages.Pager;
import net.expvp.core.commands.ModuleCommand;
import net.expvp.core.plugin.modules.economy.AccountBalanceComparator;
import net.expvp.core.plugin.modules.economy.EconomyModule;

/**
 * Class used for handling module command /baltop
 * 
 * @author NullUser
 * @see ModuleCommand<T>
 */
public class BaltopCommand extends ModuleCommand<EconomyModule> {

	private final AccountBalanceComparator comparator;
	private final Map<UUID, Integer> requesting;
	private Pair<Boolean, Integer> consoleRequesting;
	private IPager<OfflinePlayerAccount> cachedPager;
	private boolean loading;

	public BaltopCommand(EconomyModule module) {
		super(module.getContainer(), module, "baltop", "core.baltop", "/baltop [page]", 0, true);
		this.comparator = new AccountBalanceComparator();
		this.requesting = Maps.newHashMap();
		this.consoleRequesting = Pair.of(false, -1);
		this.cachedPager = null;
		this.loading = false;
	}

	/**
	 * @see ModuleCommand
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		int pageID = 1;
		if (args.length > 0) {
			if (!NumberUtil.isInteger(args[0])) {
				getContainer().getMessages().sendMessage(sender, null, "gen.invalid-number", args[0]);
				return;
			}
			pageID = NumberUtil.getInteger(args[0]);
		}
		if (loading) {
			sendMessage(sender, null, "loading");
			if (!(sender instanceof Player)) {
				consoleRequesting = Pair.of(true, pageID);
				return;
			}
			requesting.put(((Player) sender).getUniqueId(), pageID);
			return;
		}
		if (cachedPager == null) {
			sendMessage(sender, null, "loading");
			if (!(sender instanceof Player)) {
				consoleRequesting = Pair.of(true, pageID);
				return;
			}
			requesting.put(((Player) sender).getUniqueId(), pageID);
			loading = true;
			getContainer().getDataSavingThread().processRequest(() -> {
				Set<OfflinePlayerAccount> orderedSet = new TreeSet<>(comparator);
				orderedSet.addAll(getContainer().getPlayerDataHandler().getCachedAccounts());
				this.cachedPager = Pager.parse(orderedSet, 7);
				Bukkit.getScheduler().callSyncMethod(getContainer().getPlugin(), new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						loading = false;
						if (consoleRequesting.getLeft()) {
							send(Bukkit.getConsoleSender(), cachedPager, consoleRequesting.getRight());
						}
						consoleRequesting = Pair.of(false, -1);
						for (Entry<UUID, Integer> entry : requesting.entrySet()) {
							OfflinePlayer oPlayer = Bukkit.getOfflinePlayer(entry.getKey());
							if (oPlayer.isOnline()) {
								send(oPlayer.getPlayer(), cachedPager, entry.getValue());
							}
						}
						requesting.clear();
						Bukkit.getScheduler().scheduleSyncDelayedTask(getContainer().getPlugin(), () -> {
							cachedPager = null;
						}, 20 * 60);
						return true;
					}
				});
			});
			return;
		}
		send(sender, cachedPager, pageID);
	}

	/**
	 * Sends page data to sender
	 * 
	 * @param sender
	 * @param pager
	 * @param page
	 */
	public void send(CommandSender sender, IPager<OfflinePlayerAccount> pager, int pageID) {
		if (pageID > cachedPager.getPageAmount() || pageID <= 0) {
			sendMessage(sender, null, "invalid-page", pager.getPageAmount());
			return;
		}
		IPage<OfflinePlayerAccount> page = cachedPager.getPage(pageID - 1);
		sendMessage(sender, null, "header", pager.getDefaultCollection().size());
		for (OfflinePlayerAccount account : page.getPageItems()) {
			sendMessage(sender, account, "format", account.getBalance());
		}
		sendMessage(sender, null, "footer", pageID, pager.getPageAmount());
	}

}
