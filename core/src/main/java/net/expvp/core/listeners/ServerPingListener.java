package net.expvp.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import net.expvp.api.TextUtil;
import net.expvp.core.NullContainer;

/**
 * Class used for watching the server list ping event
 * 
 * @author NullUser
 * @see Listeners
 */
public class ServerPingListener implements Listener {

	private final NullContainer container;

	public ServerPingListener(NullContainer container) {
		this.container = container;
	}

	/**
	 * @param event
	 *            To watch the server list ping event
	 */
	@EventHandler
	public void onServerPing(ServerListPingEvent event) {
		String motd = container.getPlugin().getConfig().getString("motd");
		if (motd != null) {
			event.setMotd(TextUtil.color(motd));
		}
	}

}
