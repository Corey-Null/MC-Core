package net.expvp.core.commands.general;

import org.bukkit.Location;

import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.core.NullContainer;
import net.expvp.core.commands.AccountCommand;
import net.expvp.core.commands.NullCommand;

/**
 * @author NullUser
 * @see NullCommand
 */
public class CommandSetSpawn extends AccountCommand {

	/**
	 * @see NullCommand
	 * @param container
	 */
	public CommandSetSpawn(NullContainer container) {
		super(container, "setspawn", "core.spawn.setspawn", "/setspawn", 0);
	}

	/**
	 * @see NullCommand
	 */
	@Override
	public void execute(OnlinePlayerAccount account, String[] args) {
		Location loc = account.getPlayer().getLocation();
		getContainer().getWorldData().setSpawnLocation(loc);
		sendMessage(account.getPlayer(), null, "set", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(),
				loc.getBlockZ());
		try {
			loc.getWorld().setSpawnLocation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		} catch (Exception ex) {
		}
	}

}
