package net.expvp.core.player;

import java.net.InetAddress;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.google.common.collect.Sets;

import net.expvp.api.interfaces.Saveable;
import net.expvp.api.interfaces.player.IPlayerDataHandler;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.api.interfaces.plugin.IModule;
import net.expvp.core.NullContainer;
import net.expvp.core.plugin.modules.permissions.PermissionsModule;
import net.expvp.core.plugin.modules.permissions.groups.RankHandler;

/**
 * Handler for Player Accounts
 * 
 * @author NullUser
 * @see Saveable
 */
public class PlayerDataHandler implements IPlayerDataHandler {

	private final NullContainer container;
	private final WildcardPlayerAccount wildcard;
	private final ConsoleAccount console;
	private final Set<OfflinePlayerAccount> cachedAccounts;
	private boolean loading;

	/**
	 * Main initializer for the PlayerDtaHandler's fields
	 * 
	 * @param container
	 * @param saveIncrement
	 */
	public PlayerDataHandler(NullContainer container, int saveIncrement) {
		this.loading = true;
		this.container = container;
		this.wildcard = new WildcardPlayerAccount(container);
		this.console = new ConsoleAccount();
		this.cachedAccounts = Sets.newHashSet();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(container.getPlugin(), new Runnable() {
			@Override
			public void run() {
				save();
			}
		}, saveIncrement, saveIncrement);
		this.loading = false;
	}

	/**
	 * @return the console
	 */
	public OnlinePlayerAccount getConsole() {
		return console.getOnlineAccount();
	}

	/**
	 * @return the cached accounts
	 */
	public Set<OfflinePlayerAccount> getCachedAccounts() {
		return Collections.unmodifiableSet(cachedAccounts);
	}

	/**
	 * @return true if the player data is still being loaded
	 */
	public boolean isLoading() {
		return loading;
	}

	/**
	 * Loads player
	 * 
	 * @param player
	 * @return true if the load was successful
	 */
	public boolean login(Player player, InetAddress address) {
		OfflinePlayerAccount account = getAccount(player.getUniqueId());
		if (account == null) {
			try {
				cachedAccounts.add(account = new PlayerAccount(container, player, address));
				IModule<?> permModule = container.getModule("permissions");
				if (permModule != null && permModule.isEnabled() && permModule instanceof PermissionsModule) {
					PermissionsModule module = (PermissionsModule) permModule;
					RankHandler rHandler = module.getRankHandler();
					if (rHandler.getDefaultRank() != null) {
						account.setRank(rHandler.getDefaultRank());
					}
				}
				account.getOnlineAccount().teleport(container.getWorldData().getSpawnLocation());
				return true;
			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			}
		}
		if (account.isOnline()) {
			account.getOnlineAccount().getPlayer().kickPlayer(ChatColor.RED + "Something went wrong, sorry.");
			account.getOnlineAccount().logout();
			return false;
		}
		try {
			account.login(player, address);
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * Unloads player
	 * 
	 * @param player
	 */
	public void unloadPlayer(Player player) {
		OfflinePlayerAccount account = getAccount(player.getUniqueId());
		if (account == null || !account.isOnline()) {
			return;
		}
		account.getOnlineAccount().logout();
	}

	/**
	 * Gets player account
	 * 
	 * @param id
	 * @return Player account specified by id
	 */
	public OfflinePlayerAccount getAccount(UUID id) {
		Optional<OfflinePlayerAccount> optional = cachedAccounts.stream().filter(acc -> acc.getUUID().equals(id))
				.findAny();
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	/**
	 * Gets player account
	 * 
	 * @param name
	 * @return Player account specified by name
	 */
	public OfflinePlayerAccount getAccount(String name) {
		if (name.equals("*")) {
			return wildcard;
		}
		if (name.equalsIgnoreCase("#console")) {
			return console;
		}
		Optional<OfflinePlayerAccount> optional = cachedAccounts.stream()
				.filter(acc -> acc.getName().toLowerCase().startsWith(name.toLowerCase())).findAny();
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	/**
	 * Loads all cached accounts
	 */
	public void loadAccounts() {
		this.cachedAccounts.clear();
		this.cachedAccounts.addAll(this.container.getPlayerDataSavingMethod().loadAccounts());
	}

	/**
	 * @see Saveable
	 */
	@Override
	public void save() {
		this.cachedAccounts.stream().filter(OfflinePlayerAccount::isDirty).forEach(OfflinePlayerAccount::save);
	}

	@Override
	public OfflinePlayerAccount getAccount(Player player) {
		return getAccount(player.getUniqueId());
	}

	@Override
	public OnlinePlayerAccount getWildcard() {
		return wildcard.getOnlineAccount();
	}

}
