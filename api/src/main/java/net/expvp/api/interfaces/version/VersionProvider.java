package net.expvp.api.interfaces.version;

import org.bukkit.entity.Player;

/**
 * Provider for version compatibility
 * 
 * @author NullUser
 */
public interface VersionProvider {

	/**
	 * Heals the player, version specific for Attribute modifier reasons
	 * 
	 * @param player
	 *            To heal
	 */
	void heal(Player player);

	/**
	 * Sends a chat packet with specified byte to the player
	 * 
	 * @param b
	 *            Byte to define packet
	 * @param player
	 *            Player to send the packet to
	 * @param serialized
	 *            Serialized message
	 */
	void sendChatPacket(byte b, Player player, Object serialized);

}
