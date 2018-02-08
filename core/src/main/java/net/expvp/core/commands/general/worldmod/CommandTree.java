package net.expvp.core.commands.general.worldmod;

import org.bukkit.Location;
import org.bukkit.TreeType;

import net.expvp.api.bukkit.LocationUtil;
import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.core.NullContainer;
import net.expvp.core.commands.AccountCommand;

/**
 * Command used for handling command /tree
 * 
 * @author NullUser
 * @see AccountCommand
 */
public class CommandTree extends AccountCommand {

	public CommandTree(NullContainer container) {
		super(container, "tree", "core.tree", "/tree [type]", 0);
	}

	/**
	 * @see AccountCommand
	 */
	@Override
	public void execute(OnlinePlayerAccount account, String[] args) {
		TreeType tree = null;
		if (args.length > 0) {
			for (TreeType type : TreeType.values()) {
				if (type.name().replace("_", "").equalsIgnoreCase(args[0])) {
					tree = type;
					break;
				}
			}
			if (args[0].equalsIgnoreCase("jungle")) {
				tree = TreeType.SMALL_JUNGLE;
			}
		}
		if (tree == null) {
			tree = TreeType.TREE;
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
