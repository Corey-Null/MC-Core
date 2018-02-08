package net.expvp.api.interfaces;

import java.io.File;

import org.bukkit.command.CommandSender;

import net.expvp.api.interfaces.player.OfflinePlayerAccount;

/**
 * Interface for classes that hold language data
 * 
 * @author NullUser
 * @see Reloadable
 */
public interface IMessages extends Reloadable {

	/**
	 * @param string
	 * @param args
	 * @return formatted message from the key (string)
	 */
	String getMessage(String string, Object... args);

	/**
	 * Sends a formatted message to the sender
	 * 
	 * @param sender
	 * @param target
	 * @param key
	 * @param players
	 * @param objectNames
	 * @param args
	 */
	void sendMessage(CommandSender sender, OfflinePlayerAccount target, String key, OfflinePlayerAccount[] players,
			String[] objectNames, Object... args);

	/**
	 * Sends a formatted message to the sender
	 * 
	 * @param sender
	 * @param target
	 * @param key
	 * @param args
	 */
	void sendMessage(CommandSender sender, OfflinePlayerAccount target, String key, Object... args);

	/**
	 * @return the file
	 */
	File getFile();

}
