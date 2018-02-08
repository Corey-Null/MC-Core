package net.expvp.nms.v1_11_R1;

import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.expvp.api.interfaces.version.VersionProvider;
import net.minecraft.server.v1_11_R1.IChatBaseComponent;
import net.minecraft.server.v1_11_R1.PacketPlayOutChat;
import net.minecraft.server.v1_11_R1.PlayerConnection;

/**
 * Represents the version 1_11_R1
 * 
 * @author NullUser
 * @see VersionProvider
 */
public class V1_11_R1Provider implements VersionProvider {

	/**
	 * @see VersionProvider
	 */
	public void heal(Player player) {
		player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
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
