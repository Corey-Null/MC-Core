package net.expvp.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import net.expvp.core.NullContainer;

/**
 * Class used for listening for players dying
 * 
 * @author NullUser
 * @see Listener
 */
public class PlayerDeathListener implements Listener {

	private final NullContainer container;

	public PlayerDeathListener(NullContainer container) {
		this.container = container;
	}

	/**
	 * Event used to watch when a player dies
	 * 
	 * @param event
	 *            To watch
	 */
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		container.getPlayerDataHandler().getAccount(event.getEntity().getUniqueId()).getOnlineAccount()
				.updateLastLocation(event.getEntity().getLocation());
		if (container.getPlugin().getConfig().getBoolean("auto-respawn", false)) {
			event.getEntity().spigot().respawn();
		}
	}

	/**
	 * Event used to watch when a player respawns
	 * 
	 * @param event
	 *            To watch
	 */
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		if (container.getWorldData().getSpawnLocation() != null) {
			event.getPlayer().teleport(container.getWorldData().getSpawnLocation());
		}
	}

}
