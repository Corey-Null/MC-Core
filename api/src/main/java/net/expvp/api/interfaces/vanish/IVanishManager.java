package net.expvp.api.interfaces.vanish;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import net.expvp.api.enums.VanishOption;
import net.expvp.api.enums.VanishPriority;

/**
 * Interface used for handling vanishing and unvanishing players
 * 
 * @author NullUser
 */
public interface IVanishManager {

	/**
	 * Sets the vanish priority of a player
	 * 
	 * @param player
	 * @param priority
	 */
	void setVanishPriority(Player player, VanishPriority priority);

	/**
	 * @param player
	 * @return the vanish priority of a certain player
	 */
	VanishPriority getVanishPriority(Player player);

	/**
	 * @param player
	 * @param vanishedPlayer
	 * @return true if the player can see the vanished player
	 */
	boolean canSee(Player player, Player vanishedPlayer);

	/**
	 * @return the listener
	 */
	IVanishedListener getVanishListener();

	/**
	 * Loads the config options based on the section
	 * 
	 * @param section
	 */
	void loadOptions(ConfigurationSection section);

	/**
	 * @param option
	 * @return true if the vanish option is enabled
	 */
	boolean getConfigOption(VanishOption option);

	/**
	 * Vanishes all vanished players from specified player
	 * 
	 * @param player
	 */
	void vanishPlayers(Player player);

}
