package net.expvp.core.plugin.modules.serveraccess.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.expvp.api.TextUtil;
import net.expvp.core.plugin.modules.serveraccess.ServerAccessModule;
import net.expvp.core.plugin.modules.serveraccess.data.BanType;
import net.expvp.core.plugin.modules.serveraccess.data.UUIDBanData;

/**
 * Class used for handling command /kickall
 * 
 * @author NullUser
 * @see BanCommand
 */
public class CommandKickAll extends BanCommand {

	public CommandKickAll(ServerAccessModule module) {
		super(module, "kickall", "core.kickall", "/kickall [reason]", 0);
	}

	/**
	 * @see BanCommand
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		String reason;
		if (args.length > 0) {
			reason = TextUtil.connect(args, 0, args.length);
		} else {
			reason = getModule().getDefaultKickReason();
		}
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.getName().equals(sender.getName())) {
				continue;
			}
			UUIDBanData data = parse(player, sender, reason);
			manager().addKick(player.getUniqueId(), data);
			removeFromServer(player, data);
		}
		sendMessage(sender, null, "execute");
	}

	/**
	 * Parses a ban data for a kick ban
	 * 
	 * @param player
	 * @param reason
	 * @return parsed ban data from player
	 */
	private final UUIDBanData parse(Player player, CommandSender sender, String reason) {
		UUIDBanData data = new UUIDBanData(player.getUniqueId(), BanType.KICK);
		data.setBegin(System.currentTimeMillis());
		data.setExpire(-1);
		data.setExpired(true);
		data.setHander(sender instanceof Player ? ((Player) sender).getUniqueId() : null);
		data.setReason(reason);
		return data;
	}

}
