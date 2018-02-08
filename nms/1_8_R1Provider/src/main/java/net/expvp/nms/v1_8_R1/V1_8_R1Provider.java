package net.expvp.nms.v1_8_R1;

import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.expvp.api.interfaces.version.VersionProvider;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.PacketPlayOutChat;
import net.minecraft.server.v1_8_R1.PlayerConnection;

/**
 * Represents the version 1_8_R1
 * 
 * @author NullUser
 * @see VersionProvider
 */
public class V1_8_R1Provider implements VersionProvider {

	/**
	 * @see VersionProvider
	 */
	public void heal(Player player) {
		player.setHealth(player.getMaxHealth());
	}

	/**
	 * @see VersionProvider
	 */
	public void sendChatPacket(byte b, Player player, Object serialized) {
		PlayerConnection con = ((CraftPlayer) player).getHandle().playerConnection;
		PacketPlayOutChat packet = new PacketPlayOutChat((IChatBaseComponent) serialized, b);
		con.sendPacket(packet);
	}

}