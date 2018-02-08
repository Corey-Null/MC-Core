package net.expvp.core.commands.general;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.core.NullContainer;
import net.expvp.core.commands.AccountCommand;
import net.expvp.core.commands.NullCommand;

/**
 * Class used for handling command /jellylegs
 * 
 * @author NullUser
 * @see NullCommand
 * @see Listener
 */
public class CommandJellylegs extends AccountCommand implements Listener {

	private final Set<UUID> jellylegs;

	public CommandJellylegs(NullContainer container) {
		super(container, "jellylegs", "core.jellylegs", "/jellylegs", 0);
		Bukkit.getPluginManager().registerEvents(this, getContainer().getPlugin());
		jellylegs = new HashSet<>();
	}

	/**
	 * @see NullCommand
	 */
	@Override
	public void execute(OnlinePlayerAccount account, String[] args) {
		if (jellylegs.contains(account.getUUID())) {
			jellylegs.remove(account.getUUID());
			sendMessage(account, null, "disable");
		} else {
			jellylegs.add(account.getUUID());
			sendMessage(account, null, "enable");
		}
	}

	/**
	 * Used to watch for when a player with jelly legs hits the ground
	 * 
	 * @param event
	 *            To watch
	 */
	@EventHandler
	public void onFallDamage(EntityDamageEvent event) {
		if (event.getCause() == DamageCause.FALL && jellylegs.contains(event.getEntity().getUniqueId())) {
			event.setCancelled(true);
		}
	}

}
