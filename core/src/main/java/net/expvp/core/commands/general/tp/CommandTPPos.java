package net.expvp.core.commands.general.tp;

import org.bukkit.Location;

import net.expvp.api.NumberUtil;
import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.core.NullContainer;
import net.expvp.core.commands.AccountCommand;

/**
 * Class used for handling command /tppos
 * 
 * @author NullUser
 * @see AccountCommand
 */
public class CommandTPPos extends AccountCommand {

	public CommandTPPos(NullContainer container) {
		super(container, "tppos", "core.tppos", "/tppos <x> <y> <z> [yaw+] [+pitch]", 3);
	}

	/**
	 * @see AccountCommand
	 */
	@Override
	public void execute(OnlinePlayerAccount account, String[] args) {
		for (int i = 0; i < 3; i++) {
			if (!NumberUtil.isDouble(args[i])) {
				getContainer().getMessages().sendMessage(account.getPlayer(), null, "gen.invalid-number", args[i]);
				return;
			}
		}
		double x = NumberUtil.getDouble(args[0]);
		double y = NumberUtil.getDouble(args[1]);
		double z = NumberUtil.getDouble(args[2]);
		Location loc = new Location(account.getPlayer().getWorld(), x, y, z);
		if (args.length > 4) {
			for (int i = 3; i < 5; i++) {
				if (!NumberUtil.isFloat(args[i])) {
					getContainer().getMessages().sendMessage(account.getPlayer(), null, "gen.invalid-number", args[i]);
					return;
				}
			}
			loc.setYaw(NumberUtil.getFloat(args[3]));
			loc.setPitch(NumberUtil.getFloat(args[4]));
		}
		sendMessage(account, null, "sent", x, y, z);
		account.teleport(loc);
	}

}
