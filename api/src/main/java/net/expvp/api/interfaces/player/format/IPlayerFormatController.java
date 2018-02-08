package net.expvp.api.interfaces.player.format;

import net.expvp.api.bukkit.chat.ChatComponent;
import net.expvp.api.interfaces.Reloadable;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;

/**
 * Interface used to control formats
 * 
 * @author NullUser
 * @see Reloadable
 */
public interface IPlayerFormatController extends Reloadable {

	/**
	 * @param parsable
	 * @return the PlayerFormat to be used based on the key
	 */
	IPlayerFormat get(String parsable);

	/**
	 * Fixes a string to be properly formatted with players
	 * 
	 * @param str
	 *            To format
	 * @param accounts
	 *            The accounts to format with
	 * @param names
	 *            The keys to format with
	 * @return the formatted component
	 */
	ChatComponent fix(String str, OfflinePlayerAccount[] accounts, String[] names);

	/**
	 * Fixes a component to be properly formatted with players
	 * 
	 * @param component
	 *            To format
	 * @param accounts
	 *            The accounts to format with
	 * @param names
	 *            The keys to format with
	 * @return the formatted component
	 */
	ChatComponent fix(ChatComponent component, OfflinePlayerAccount[] accounts, String[] names);

}
