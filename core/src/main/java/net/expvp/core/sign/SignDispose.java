package net.expvp.core.sign;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import net.expvp.core.NullContainer;

/**
 * Class used for handling the Dispose sign
 * 
 * @author NullUser
 * @see AbstractSign
 */
public class SignDispose extends AbstractSign {

	public SignDispose(NullContainer container) {
		super(container);
		applySignFunction((player, sign) -> {
			if (!player.hasPermission("core.sign.dispose")) {
				container.getMessages().sendMessage(player, null, "gen.no-permission");
				return;
			}
			player.openInventory(Bukkit.createInventory(null, 54,
					new StringBuilder().append(ChatColor.BLUE).append(ChatColor.BOLD).append("Disposal").toString()));
		});
	}

	/**
	 * @see AbstractSign
	 */
	@Override
	public boolean loadSign(Player creator, Sign sign, String[] currentLines) {
		if (!canBeSign(currentLines)) {
			return false;
		}
		if (!creator.hasPermission("core.sign.dispose.create")) {
			return false;
		}
		sign.setLine(0, "");
		sign.setLine(1, ChatColor.DARK_BLUE + "[Dispose]");
		sign.setLine(2, "");
		sign.setLine(3, "");
		sign.update(true);
		return true;
	}

	/**
	 * @see AbstractSign
	 */
	@Override
	public boolean isInstance(Sign sign) {
		if (sign.getLines()[1] == null) {
			return false;
		}
		return sign.getLines()[1].equalsIgnoreCase(ChatColor.DARK_BLUE + "[Dispose]");
	}

	/**
	 * @see AbstractSign
	 */
	@Override
	public boolean canBeSign(String[] lines) {
		if (lines[0] == null) {
			return false;
		}
		return ChatColor.stripColor(lines[0]).equalsIgnoreCase("[dispose]");
	}

}
