package net.expvp.core.commands.general.admin;

import org.bukkit.entity.Player;

import net.expvp.api.enums.VanishPriority;
import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.api.interfaces.vanish.IVanishManager;
import net.expvp.core.NullContainer;
import net.expvp.core.commands.AccountCommand;

/**
 * Class used for handling command /vanish
 * 
 * @author NullUser
 * @see AccountCommand
 */
public class CommandVanish extends AccountCommand {

	public CommandVanish(NullContainer container) {
		super(container, "vanish", "core.vanish.player", "/vanish [players|staff]", 0);
	}

	/**
	 * @see AccountCommand
	 */
	@Override
	public void execute(OnlinePlayerAccount account, String[] args) {
		Player player = account.getPlayer();
		IVanishManager vanishManager = getContainer().getVanishManager();
		VanishPriority priority = vanishManager.getVanishPriority(player);
		if (args.length == 0 || args[0].equalsIgnoreCase("players")) {
			if (priority == VanishPriority.PLAYERS) {
				vanishManager.setVanishPriority(player, VanishPriority.VISIBLE);
				sendMessage(player, null, "disabled");
			} else {
				vanishManager.setVanishPriority(player, VanishPriority.PLAYERS);
				sendMessage(player, null, "enabled");
			}
		} else if (args[0].equalsIgnoreCase("staff")) {
			if (!checkPermission(account, "core.vanish.staff", true)) {
				return;
			}
			if (priority == VanishPriority.STAFF) {
				vanishManager.setVanishPriority(player, VanishPriority.VISIBLE);
				sendMessage(player, null, "disabled");
			} else {
				vanishManager.setVanishPriority(player, VanishPriority.STAFF);
				sendMessage(player, null, "enabled");
			}
		} else {
			sendUsage(player);
		}
	}

}
