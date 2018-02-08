package net.expvp.core.vanish;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.google.common.collect.Maps;

import net.expvp.api.TextUtil;
import net.expvp.api.enums.VanishOption;
import net.expvp.api.enums.VanishPriority;
import net.expvp.api.interfaces.vanish.IVanishManager;
import net.expvp.api.interfaces.vanish.IVanishedListener;
import net.expvp.core.NullContainer;

/**
 * Implemented default version of IVanishManager
 * 
 * @author NullUser
 * @see IVanishManager
 */
public class VanishManager implements IVanishManager {

	private final NullContainer container;
	private final IVanishedListener listener;
	private final Map<UUID, VanishPriority> vanishPriorities;
	private final Map<VanishOption, Boolean> vanishOptions;

	public VanishManager(NullContainer container) {
		this.container = container;
		this.listener = new VanishListener(this);
		Bukkit.getPluginManager().registerEvents(listener, container.getPlugin());
		this.vanishPriorities = Maps.newHashMap();
		this.vanishOptions = Maps.newHashMap();
	}

	/**
	 * @see IVanishManager
	 */
	@Override
	public void setVanishPriority(Player player, VanishPriority priority) {
		UUID id = player.getUniqueId();
		switch (priority) {
		case PLAYERS:
		case STAFF:
			if (vanishPriorities.containsKey(id)) {
				vanishPriorities.replace(id, priority);
			} else {
				vanishPriorities.put(id, priority);
			}
			for (Player target : Bukkit.getOnlinePlayers()) {
				if (target.canSee(player)) {
					if (!canSee(target, player)) {
						target.hidePlayer(player);
					}
				} else {
					if (canSee(target, player)) {
						target.showPlayer(player);
					}
				}
			}
			if (getConfigOption(VanishOption.FAKE_QUIT)) {
				String message = container.getPlugin().getConfig().getString("quit-message");
				if (message == null) {
					break;
				}
				message = TextUtil.color(message.replace("%player%", player.getName()));
				for (Player target : Bukkit.getOnlinePlayers()) {
					if (!target.canSee(player)) {
						target.sendMessage(message);
					}
				}
			}
			break;
		case VISIBLE:
			vanishPriorities.remove(id);
			for (Player target : Bukkit.getOnlinePlayers()) {
				if (!target.canSee(player)) {
					target.showPlayer(player);
				}
			}
			if (getConfigOption(VanishOption.FAKE_JOIN)) {
				String message = container.getPlugin().getConfig().getString("join-message");
				if (message == null) {
					break;
				}
				message = TextUtil.color(message.replace("%player%", player.getName()));
				for (Player target : Bukkit.getOnlinePlayers()) {
					if (!target.canSee(player)) {
						target.sendMessage(message);
					}
				}
			}
			break;
		}
	}

	/**
	 * @see IVanishManager
	 */
	@Override
	public VanishPriority getVanishPriority(Player player) {
		return vanishPriorities.getOrDefault(player.getUniqueId(), VanishPriority.VISIBLE);
	}

	/**
	 * @see IVanishManager
	 */
	@Override
	public boolean canSee(Player player, Player vanishedPlayer) {
		if (player.getUniqueId().equals(vanishedPlayer.getUniqueId())) {
			return true;
		}
		VanishPriority priority = getVanishPriority(vanishedPlayer);
		switch (priority) {
		case PLAYERS:
			return player.hasPermission("core.vanish.player.bypass");
		case STAFF:
			return player.hasPermission("core.vanish.staff.bypass");
		case VISIBLE:
			return true;
		default:
			return false;
		}
	}

	/**
	 * @see IVanishManager
	 */
	@Override
	public IVanishedListener getVanishListener() {
		return listener;
	}

	/**
	 * @see IVanishManager
	 */
	@Override
	public void loadOptions(ConfigurationSection section) {
		if (section == null) {
			return;
		}
		for (VanishOption option : VanishOption.values()) {
			vanishOptions.put(option, section.getBoolean(option.getName(), true));
		}
	}

	/**
	 * @see IVanishManager
	 */
	@Override
	public boolean getConfigOption(VanishOption option) {
		return vanishOptions.getOrDefault(option, true);
	}

	@Override
	public void vanishPlayers(Player player) {
		if (player.hasPermission("core.vanish.staff.bypass")) {
			return;
		}
		for (Entry<UUID, VanishPriority> entry : vanishPriorities.entrySet()) {
			OfflinePlayer otarget = Bukkit.getOfflinePlayer(entry.getKey());
			if (otarget.isOnline()) {
				Player target = otarget.getPlayer();
				if (player.canSee(target)) {
					if (!canSee(player, target)) {
						player.hidePlayer(target);
					}
				} else {
					if (canSee(player, target)) {
						player.showPlayer(target);
					}
				}
			}
		}
	}

}
