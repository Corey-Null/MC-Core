package net.expvp.core.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandPreProcessListener implements Listener {

	@EventHandler
	public void onCommandPreProcess(PlayerCommandPreprocessEvent event) {
		String msg = event.getMessage().toLowerCase();
		if (msg.startsWith("/reload") || msg.startsWith("/rl")) {
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.DARK_RED + "Running this command is not good. Do not use this.");
			event.getPlayer().sendMessage(ChatColor.RED + "If you need something reloaded use /nreload [plugin]");
			return;
		}
	}

}
