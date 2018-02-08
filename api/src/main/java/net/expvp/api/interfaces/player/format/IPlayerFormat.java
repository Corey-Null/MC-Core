package net.expvp.api.interfaces.player.format;

import net.expvp.api.bukkit.chat.Component;
import net.expvp.api.interfaces.chat.ChatProperty;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;

/**
 * Interface used to hold the data of a player format
 * 
 * @author NullUser
 */
public interface IPlayerFormat {

	/**
	 * @param click
	 *            To set the click
	 */
	void setClick(String click);

	/**
	 * @param click
	 *            To set the hover
	 */
	void setHover(String hover);

	/**
	 * @return the click
	 */
	String getClick();

	/**
	 * @return the hover
	 */
	String getHover();

	/**
	 * @return the text
	 */
	String getText();

	/**
	 * @param player
	 *            Makes a component to represent the player
	 * @return the made component from the player
	 */
	Component make(OfflinePlayerAccount player);

	/**
	 * @param player
	 *            To load from
	 * @return the loaded hover property
	 */
	ChatProperty loadHover(OfflinePlayerAccount player);

	/**
	 * @param player
	 *            To load from
	 * @return the loaded click property
	 */
	ChatProperty loadClick(OfflinePlayerAccount player);

}
