package net.expvp.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.expvp.api.PlaceholderAPI;
import net.expvp.api.TextUtil;
import net.expvp.api.interfaces.player.IPlayerDataHandler;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.core.NullContainer;
import net.expvp.core.plugin.CorePlugin;

/**
 * Watches for the players logging in and quitting
 * 
 * @author NullUser
 * @see Listener
 */
public class PlayerJoinQuitListener implements Listener {

	private final NullContainer container;

	public PlayerJoinQuitListener(NullContainer container) {
		this.container = container;
	}

	/**
	 * @param event
	 *            Watched login event
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void onLogin(PlayerLoginEvent event) {
		IPlayerDataHandler data = container.getPlayerDataHandler();
		if (data.isLoading()) {
			event.setKickMessage("We are currently loading player accounts, please try again later.");
			event.setResult(Result.KICK_OTHER);
			return;
		}
		if (!data.login(event.getPlayer(), event.getAddress())) {
			event.setKickMessage("Something went wrong, please try again later.");
			event.setResult(Result.KICK_OTHER);
		}
	}

	/**
	 * @param event
	 *            Watched join event
	 */
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		String joinMessage = container.getPlugin().getConfig().getString("join-message");
		if (joinMessage == null) {
			event.setJoinMessage(null);
			return;
		}
		Player player = event.getPlayer();
		event.setJoinMessage(TextUtil.color(joinMessage.replace("%player%", player.getName())));
		OfflinePlayerAccount acc = container.getPlayerDataHandler().getAccount(player.getUniqueId());
		if (acc == null) {
			return;
		}
		JavaPlugin plugin = container.getPlugin();
		if (plugin instanceof CorePlugin) {
			player.setPlayerListName(
					TextUtil.color(PlaceholderAPI.pass(((CorePlugin) plugin).getPlayerListFormat(), acc)));
		}
	}

	/**
	 * @param event
	 *            Watched quit event
	 */
	@EventHandler
	public void onLogout(PlayerQuitEvent event) {
		container.getPlayerDataHandler().unloadPlayer(event.getPlayer());
		String quitMessage = container.getPlugin().getConfig().getString("quit-message");
		if (quitMessage == null) {
			event.setQuitMessage(null);
			return;
		}
		event.setQuitMessage(TextUtil.color(quitMessage.replace("%player%", event.getPlayer().getName())));
	}

}
