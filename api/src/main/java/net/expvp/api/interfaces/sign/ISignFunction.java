package net.expvp.api.interfaces.sign;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 * Class used to handle the functions for ISigns
 * 
 * @author NullUser
 */
public interface ISignFunction {

	/**
	 * @param player
	 *            To handle the click of the sign
	 * @param sign
	 *            To look at the lines for
	 */
	void apply(Player player, Sign sign);

}
