package net.expvp.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.core.NullContainer;

/**
 * Class used for listening for all forms of damage
 * 
 * @author NullUser
 * @see Listener
 */
public class DamageListener implements Listener {

	private final NullContainer container;

	public DamageListener(NullContainer container) {
		this.container = container;
	}

	/**
	 * Event called when a player is damaged
	 * 
	 * @param event
	 *            To watch
	 */
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			OnlinePlayerAccount acc = container.getPlayerDataHandler().getAccount(event.getEntity().getUniqueId())
					.getOnlineAccount();
			if (acc.isGodMode()) {
				event.setCancelled(true);
			}
		}
	}

}
