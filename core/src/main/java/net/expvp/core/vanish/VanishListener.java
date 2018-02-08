package net.expvp.core.vanish;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.expvp.api.bukkit.events.account.AccountMessageEvent;
import net.expvp.api.enums.VanishOption;
import net.expvp.api.enums.VanishPriority;
import net.expvp.api.interfaces.vanish.IVanishManager;
import net.expvp.api.interfaces.vanish.IVanishedListener;

/**
 * Implemented default version of IVanishedListener
 * 
 * @author NullUser
 * @see IVanishedListener
 */
public class VanishListener implements IVanishedListener {

	private final IVanishManager manager;

	public VanishListener(IVanishManager manager) {
		this.manager = manager;
	}

	/**
	 * @see IVanishedListener
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	@Override
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		manager.vanishPlayers(player);
		if (manager.getVanishPriority(player) != VanishPriority.VISIBLE
				&& manager.getConfigOption(VanishOption.SILENT_JOIN)) {
			String message = event.getJoinMessage();
			event.setJoinMessage(null);
			for (Player target : Bukkit.getOnlinePlayers()) {
				if (!manager.canSee(target, player)) {
					target.hidePlayer(player);
				}
			}
			if (message == null) {
				return;
			}
			for (Player target : Bukkit.getOnlinePlayers()) {
				if (target.canSee(player)) {
					if (!manager.canSee(target, player)) {
						target.hidePlayer(player);
					}
				} else {
					if (manager.canSee(target, player)) {
						target.showPlayer(player);
					}
				}
			}
		}
	}

	/**
	 * @see IVanishedListener
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	@Override
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (manager.getVanishPriority(player) != VanishPriority.VISIBLE
				&& manager.getConfigOption(VanishOption.SILENT_QUIT)) {
			String message = event.getQuitMessage();
			event.setQuitMessage(null);
			if (message == null) {
				return;
			}
			for (Player target : Bukkit.getOnlinePlayers()) {
				if (manager.canSee(target, player)) {
					target.sendMessage(message);
				}
			}
		}
	}

	/**
	 * @see IVanishedListener
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMessage(AccountMessageEvent event) {
		if (manager.getVanishPriority(event.getAccount().getOnlineAccount().getPlayer()) != VanishPriority.VISIBLE) {
			if (!manager.canSee(event.getTarget().getPlayer(), event.getAccount().getOnlineAccount().getPlayer())) {
				event.setCancelled(true);
			}
		}
	}

	/**
	 * @see IVanishedListener
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	@Override
	public void onDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		if (manager.getVanishPriority((Player) event.getEntity()) != VanishPriority.VISIBLE) {
			event.setCancelled(true);
		}
	}

}
