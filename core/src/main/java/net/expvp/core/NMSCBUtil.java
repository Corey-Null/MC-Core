package net.expvp.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;

import net.expvp.api.ReflectionUtil;

/**
 * Class used to access MC version specific classes
 * 
 * @author NullUser
 */
public final class NMSCBUtil {

	private static Method craftPlayerGetHandle;
	private static Method playerSendPacket;
	private static Field entityPlayerPlayerConnection;
	private static Field craftPlayerPermissions;
	private static Field pingField;

	/**
	 * Initializes all required reflection fields
	 */
	static {
		Class<?> craftPlayer = ReflectionUtil.getCBClass("entity.CraftPlayer");
		Class<?> entityPlayer = ReflectionUtil.getNMSClass("EntityPlayer");
		craftPlayerGetHandle = ReflectionUtil.getMethod(craftPlayer, "getHandle");
		entityPlayerPlayerConnection = ReflectionUtil.getField(entityPlayer, "playerConnection");
		playerSendPacket = ReflectionUtil.getMethod(ReflectionUtil.getNMSClass("PlayerConnection"), "sendPacket",
				ReflectionUtil.getNMSClass("Packet"));
		craftPlayerPermissions = ReflectionUtil.getField(craftPlayer.getSuperclass(), "perm");
		pingField = ReflectionUtil.getField(entityPlayer, "ping");
	}

	/**
	 * Copies permissions from old base to new base
	 * 
	 * @param old
	 *            To copy from
	 * @param newPerm
	 *            To copy to
	 */
	@SuppressWarnings("unchecked")
	public static void copyValues(Object old, PermissibleBase newPerm) {
		Field attachmentField = ReflectionUtil.getField(PermissibleBase.class, "attachments");
		List<Object> attachmentPerms = (List<Object>) ReflectionUtil.get(attachmentField, newPerm);
		attachmentPerms.clear();
		attachmentPerms.addAll((List<Object>) ReflectionUtil.get(attachmentField, old));
		newPerm.recalculatePermissions();
	}

	/**
	 * Attaches permissions to a player
	 * 
	 * @param player
	 *            To attach to
	 * @param base
	 *            To attach to player
	 */
	public static void attachPermissions(Player player, PermissibleBase base) {
		copyValues(ReflectionUtil.get(craftPlayerPermissions, player), base);
		ReflectionUtil.setField(craftPlayerPermissions, player, base);
	}

	/**
	 * @param player
	 *            The player being used
	 * @param packet
	 *            The packet being sent to the player
	 */
	public static void sendPacket(Player player, Object packet) {
		ReflectionUtil.invoke(playerSendPacket, getConnection(player), packet);
	}

	/**
	 * @param player
	 *            The player being used
	 * @return the connection of the player
	 */
	public static Object getConnection(Player player) {
		return ReflectionUtil.get(entityPlayerPlayerConnection, getCraftPlayer(player));
	}

	/**
	 * @param player
	 *            To get the ping from
	 * @return the ping of player
	 */
	public static Object getPing(Player player) {
		return ReflectionUtil.get(pingField, getCraftPlayer(player));
	}

	/**
	 * @param player
	 *            The player being used
	 * @return the EntityPlayer version of the player
	 */
	public static Object getCraftPlayer(Player player) {
		return ReflectionUtil.invoke(craftPlayerGetHandle, player);
	}

}
