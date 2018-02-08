package net.expvp.api.interfaces.sign;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Class used to control custom signs
 * 
 * @author NullUser
 * @see Listener
 */
public interface ISign extends Listener {

	/**
	 * Loads the sign as a custom sign
	 * 
	 * @param creator
	 *            The creator of the sign
	 * @param sign
	 *            The sign to turn into a custom sign
	 * @param currentLines
	 *            The current lines of the sign
	 * @return true if the sign was successfully created
	 */
	boolean loadSign(Player creator, Sign sign, String[] currentLines);

	/**
	 * @param sign
	 *            To test if the sign is of the instance
	 * @return true if the sign is this type of custom sign
	 */
	boolean isInstance(Sign sign);

	/**
	 * @param lines
	 *            To check
	 * @return true if the current state of the sign can be formed into an
	 *         instance
	 */
	boolean canBeSign(String[] lines);

	/**
	 * Event used for handling the right click of a custom sign
	 * 
	 * @param event
	 *            To watch
	 */
	void onInteractEvent(PlayerInteractEvent event);

	/**
	 * Event used for handling the placement of signs
	 * 
	 * @param event
	 *            To watch
	 */
	void onPlaceEvent(SignChangeEvent event);

	/**
	 * @return the SignFunction
	 */
	ISignFunction getSignFunction();

	/**
	 * @param function
	 *            To set the signFunction
	 */
	void applySignFunction(ISignFunction function);

}
