package net.expvp.core.commands.general.warp;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.expvp.api.interfaces.data.IWorldData;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.core.NullContainer;
import net.expvp.core.commands.NullCommand;

/**
 * Class used for handling command /warp
 * 
 * @author NullUser
 * @see NullCommand
 */
public class CommandWarp extends NullCommand {

	public CommandWarp(NullContainer container) {
		super(container, "warp", "core.warp", "/warp [warp]", 0, true);
	}

	/**
	 * @see NullCommand
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		IWorldData wd = getContainer().getWorldData();
		if (args.length == 0) {
			StringBuilder builder = new StringBuilder("");
			for (String warp : wd.getWarps().keySet()) {
				if (sender.hasPermission("core.warp.*") || sender.hasPermission("core.warp." + warp)) {
					builder.append(warp + ", ");
				}
			}
			String list;
			if (builder.length() == 0) {
				list = getMessage("empty-list");
			} else {
				builder.delete(builder.length() - 2, builder.length() - 1);
				list = builder.toString();
			}
			sendMessage(sender, null, "list-format", list);
			return;
		}
		Location warp = wd.getWarp(args[0]).clone();
		if (warp == null) {
			sendMessage(sender, null, "not-exist");
			return;
		}
		if (!(sender.hasPermission("core.warp.*") || sender.hasPermission("core.warp." + args[0]))) {
			sendMessage(sender, null, "no-permission", args[0]);
			return;
		}
		if (args.length == 2 && sender.hasPermission("core.warp.other")) {
			OfflinePlayerAccount target = getContainer().getPlayerDataHandler().getAccount(args[1]);
			if (target == null || !target.isOnline()) {
				getContainer().getMessages().sendMessage(sender, null, "gen.account-offline", args[1]);
				return;
			}
			OnlinePlayerAccount oTarget = target.getOnlineAccount();
			oTarget.teleport(warp);
			sendMessage(oTarget, null, "sent", args[0]);
			sendMessage(sender, oTarget, "sent-other", args[0]);
			return;
		}
		if (!checkSender(sender, true)) {
			return;
		}
		OnlinePlayerAccount account = getContainer().getPlayerDataHandler().getAccount(((Player) sender).getUniqueId())
				.getOnlineAccount();
		account.teleport(warp);
		sendMessage(account, null, "sent", args[0]);
	}

}
