package net.expvp.core.commands.general;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.core.NullContainer;
import net.expvp.core.commands.NullCommand;

/**
 * @author NullUser
 * @see NullCommand
 */
public class CommandSpawn extends NullCommand {

	/**
	 * @see NullCommand
	 * @param container
	 */
	public CommandSpawn(NullContainer container) {
		super(container, "spawn", "core.spawn", "/spawn <player>", 0, true);
	}

	/**
	 * @see NullCommand
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		Location spawn = getContainer().getWorldData().getSpawnLocation();
		if (spawn == null) {
			sendMessage(sender, null, "not-set");
			return;
		}
		if (args.length == 0) {
			if (!checkSender(sender, true)) {
				return;
			}
			OfflinePlayerAccount account = getContainer().getPlayerDataHandler()
					.getAccount(((Player) sender).getUniqueId());
			OnlinePlayerAccount oAccount = account.getOnlineAccount();
			oAccount.teleport(spawn);
			sendMessage(oAccount.getPlayer(), null, "self");
			return;
		}
		OfflinePlayerAccount account = checkAccount(sender, args[0], true);
		if (account == null) {
			return;
		}
		OnlinePlayerAccount oAccount = account.getOnlineAccount();
		oAccount.teleport(spawn);
		sendMessage(oAccount.getPlayer(), null, "self");
		sendMessage(sender, account, "other");
	}

}
