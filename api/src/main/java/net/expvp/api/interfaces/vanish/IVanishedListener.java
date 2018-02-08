package net.expvp.api.interfaces.vanish;

import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Class for listening to events concerning vanished players
 * 
 * @author NullUser
 * @see Listener
 */
public interface IVanishedListener extends Listener {

	/**
	 * Watches the join event
	 * 
	 * @param event
	 *            To listen for
	 */
	void onJoin(PlayerJoinEvent event);

	/**
	 * Watches the quit event
	 * 
	 * @param event
	 *            To listen for
	 */
	void onQuit(PlayerQuitEvent event);

	/**
	 * Watches the damage event
	 * 
	 * @param event
	 *            To listen for
	 */
	void onDamage(EntityDamageEvent event);

}
