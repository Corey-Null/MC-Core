package net.expvp.core.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.expvp.core.NullContainer;

/**
 * Class used to watch and log the chat event
 * 
 * @author NullUser
 * @see Listener
 */
public class ChatListener implements Listener {

	private final NullContainer container;

	public ChatListener(NullContainer container) {
		this.container = container;
	}

	/**
	 * Watches chat messages to log
	 * 
	 * @param event
	 *            To watch
	 */
	@EventHandler()
	public void onChat(AsyncPlayerChatEvent event) {
		container.getChatLogger().log(event.getPlayer().getName() + ": " + event.getMessage().replace('§', '&'));
	}

}