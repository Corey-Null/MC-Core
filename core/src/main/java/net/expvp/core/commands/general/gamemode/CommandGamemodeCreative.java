package net.expvp.core.commands.general.gamemode;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.expvp.core.NullContainer;
import net.expvp.core.commands.NullCommand;

/**
 * Class used for handling command /gamemodecreative
 * 
 * @author NullUser
 * @see NullCommand
 */
public class CommandGamemodeCreative extends NullCommand {

	public CommandGamemodeCreative(NullContainer container) {
		super(container, "gamemodecreative", "core.gamemode.creative", "/gmc [player]", 0, true);
	}

	/**
	 * @see NullCommand
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		Player target;
		if (args.length >= 1) {
			target = getPlayer(args[0]);
			if (target == null) {
				getContainer().getMessages().sendMessage(sender, null, "gen.account-offline", args[0]);
				return;
			}
		} else {
			if (!checkSender(sender, true)) {
				return;
			}
			target = (Player) sender;
		}
		target.setGameMode(GameMode.CREATIVE);
		if (target.getName().equals(sender.getName())) {
			sendMessage(sender, null, "execute");
		} else {
			sendMessage(target, null, "execute");
			sendMessage(sender, getContainer().getPlayerDataHandler().getAccount(target.getUniqueId()), "other");
		}
	}

}
