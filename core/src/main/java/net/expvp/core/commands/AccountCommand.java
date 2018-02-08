package net.expvp.core.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.expvp.api.bukkit.exceptions.PlayerOfflineException;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.core.NullContainer;

/**
 * Account based command base
 * 
 * @author NullUser
 * @see NullCommand
 */
public abstract class AccountCommand extends NullCommand {

	/**
	 * @see NullCommand
	 * @param container
	 * @param cmd
	 * @param perm
	 * @param usage
	 * @param args
	 */
	public AccountCommand(NullContainer container, String cmd, String perm, String usage, int args) {
		super(container, cmd, perm, usage, args, false);
	}

	/**
	 * @see NullCommand
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		OfflinePlayerAccount oAccount = getContainer().getPlayerDataHandler().getAccount(player.getUniqueId());
		if (!oAccount.isOnline()) {
			player.kickPlayer(ChatColor.RED + "Your account has been nulled... something went wrong.");
			new PlayerOfflineException("\"Offline\" player tried to use a command.").printStackTrace();
			return;
		}
		execute(oAccount.getOnlineAccount(), args);
	}

	/**
	 * Checks permission of a sender
	 * 
	 * @param sender
	 * @param perm
	 *            Permission to test against the sender
	 * @param message
	 *            To define if the sender should receive a message
	 * @return true if the sender has permission defined by perm
	 */
	public boolean checkPermission(OfflinePlayerAccount account, String perm, boolean message) {
		if (perm == null || account.hasPermission(perm)) {
			return true;
		}
		if (message && account.isOnline()) {
			getContainer().getMessages().sendMessage(account.getOnlineAccount().getPlayer(), null, "gen.no-permission");
		}
		return false;
	}

	/**
	 * Executes the command with the specified account
	 * 
	 * @param account
	 * @param args
	 */
	public abstract void execute(OnlinePlayerAccount account, String[] args);

}
