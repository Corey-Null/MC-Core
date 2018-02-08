package net.expvp.core.commands.general.worldmod;

import org.bukkit.Location;
import org.bukkit.TreeType;

import net.expvp.api.bukkit.LocationUtil;
import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.core.NullContainer;
import net.expvp.core.commands.AccountCommand;

/**
 * Class for handling the command /bigtree
 * 
 * @author NullUser
 * @see AccountCommand
 */
public class CommandBigTree extends AccountCommand {

	public CommandBigTree(NullContainer container) {
		super(container, "bigtree", "core.bigtree", "/bigtree <treetype>", 0);
	}

	/**
	 * @see AccountCommand
	 */
	@Override
	public void execute(OnlinePlayerAccount account, String[] args) {
		TreeType tree;
		if (args.length > 0) {
			if (args.length > 0 && args[0].equalsIgnoreCase("redwood")) {
				tree = TreeType.TALL_REDWOOD;
			} else if (args.length > 0 && args[0].equalsIgnoreCase("tree")) {
				tree = TreeType.BIG_TREE;
			} else if (args.length > 0 && args[0].equalsIgnoreCase("jungle")) {
				tree = TreeType.JUNGLE;
			} else {
				tree = TreeType.BIG_TREE;
			}
		} else {
			tree = TreeType.BIG_TREE;
		}
		try {
			Location loc = LocationUtil.getTarget(account.getPlayer());
			Location safeLocation = LocationUtil.getSafeDestination(loc);
			if (safeLocation.getWorld().generateTree(safeLocation, tree)) {
				sendMessage(account, null, "success");
			} else {
				sendMessage(account, null, "denied");
			}
		} catch (Exception e) {
			sendMessage(account, null, "denied");
		}
	}

}
