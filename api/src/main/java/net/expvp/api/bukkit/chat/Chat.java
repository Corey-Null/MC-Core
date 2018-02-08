package net.expvp.api.bukkit.chat;

import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.expvp.api.ReflectionUtil;
import net.expvp.api.TextUtil;
import net.expvp.api.interfaces.Container;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.api.interfaces.player.format.IPlayerFormat;
import net.expvp.api.interfaces.player.format.IPlayerFormatController;
import net.expvp.api.interfaces.version.VersionProvider;

/**
 * Final class to replace Bukkit.# and handle chat
 * 
 * @author NullUser
 */
public final class Chat {

	private static Method serializeMethod = ReflectionUtil
			.getMethod(ReflectionUtil.getNMSClass("IChatBaseComponent$ChatSerializer"), "a", String.class);
	private static VersionProvider version;
	private static Container<?> container;

	/**
	 * @param provider
	 *            To set the version
	 */
	public static void initContainer(Container<?> container) {
		Chat.container = container;
		version = container.getVersionProvider();
	}

	/**
	 * Broadcasts a message
	 * 
	 * @param message
	 *            The message
	 */
	public static void broadcast(String message) {
		Bukkit.broadcastMessage(TextUtil.color(message));
	}

	/**
	 * Broadcasts a message with a permission
	 * 
	 * @param message
	 *            The message
	 * @param permission
	 *            The permission
	 */
	public static void broadcast(String message, String permission) {
		Bukkit.broadcast(TextUtil.color(message), permission);
	}

	/**
	 * Broadcasts a message with a replacer and special players
	 * 
	 * @param message
	 *            The message
	 * @param replacer
	 *            The replacer
	 * @param players
	 *            The players
	 * @param playerTypes
	 *            The player types
	 */
	public static void broadcast(String message, IPlayerFormatController replacer, OfflinePlayerAccount[] players,
			String... playerTypes) {
		ChatComponent component = new ChatComponent();
		component.add(new Component(message));
		broadcast(component, replacer, players, playerTypes);
	}

	/**
	 * Broadcasts a message with a permission
	 * 
	 * @param message
	 *            The message
	 * @param permission
	 *            The permission
	 * @param replacer
	 *            The replacer
	 * @param players
	 *            The players
	 * @param playerTypes
	 *            The player types
	 */
	public static void broadcast(String message, String permission, IPlayerFormatController replacer,
			OfflinePlayerAccount[] players, String... playerTypes) {
		ChatComponent component = new ChatComponent();
		component.add(new Component(message));
		broadcast(component, permission, replacer, players, playerTypes);
	}

	/**
	 * Broadcasts a component
	 * 
	 * @param message
	 *            To broadcast
	 */
	public static void broadcast(ChatComponent message) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			sendMessage(player, message);
		}
		sendMessage(Bukkit.getConsoleSender(), message.getRawText());
	}

	/**
	 * Broadcasts a message with a special player replacer
	 * 
	 * @param message
	 *            The message
	 * @param replacer
	 *            The replacer
	 */
	public static void broadcast(ChatComponent message, IPlayerFormatController replacer) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			IPlayerFormat format = replacer.get("player");
			Component comp = format.make(container.getPlayerDataHandler().getAccount(player));
			sendMessage(player, message.clone().insert("{player}", comp));
		}
		sendMessage(Bukkit.getConsoleSender(), message);
	}

	/**
	 * Broadcasts a message with a special player replacer and a permission
	 * 
	 * @param message
	 *            The message
	 * @param permission
	 *            The permission
	 * @param replacer
	 *            The replacer
	 */
	public static void broadcast(ChatComponent message, String permission, IPlayerFormatController replacer) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.hasPermission(permission)) {
				IPlayerFormat format = replacer.get("player");
				Component comp = format.make(container.getPlayerDataHandler().getAccount(player));
				sendMessage(player, message.clone().insert("{player}", comp));
			}
		}
		sendMessage(Bukkit.getConsoleSender(), message);
	}

	/**
	 * Broadcasts a component with a permission
	 * 
	 * @param message
	 *            The message
	 * @param permission
	 *            The permission
	 */
	public static void broadcast(ChatComponent message, String permission) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.hasPermission(permission)) {
				sendMessage(player, message);
			}
		}
		sendMessage(Bukkit.getConsoleSender(), message.getRawText());
	}

	/**
	 * Broadcasts a specified message
	 * 
	 * @param message
	 *            The message
	 * @param replacer
	 *            The message replacer
	 * @param players
	 *            The players
	 * @param playerTypes
	 *            The playerTypes
	 */
	public static void broadcast(ChatComponent message, IPlayerFormatController replacer,
			OfflinePlayerAccount[] players, String... playerTypes) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			sendMessage(container.getPlayerDataHandler().getAccount(player).getOnlineAccount(), message, replacer,
					players, playerTypes);
		}
	}

	/**
	 * Broadcasts a component with a permission
	 * 
	 * @param message
	 *            The message
	 * @param permission
	 *            To receive the message
	 * @param replacer
	 *            The replacing interface
	 * @param players
	 *            Replaced players
	 * @param playerTypes
	 *            The playertypes
	 */
	public static void broadcast(ChatComponent message, String permission, IPlayerFormatController replacer,
			OfflinePlayerAccount[] players, String... playerTypes) {
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.hasPermission(permission)) {
				sendMessage(container.getPlayerDataHandler().getAccount(player).getOnlineAccount(), message, replacer,
						players, playerTypes);
			}
		}
	}

	/**
	 * Sends raw text to a sender
	 * 
	 * @param sender
	 *            To send the message to
	 * @param message
	 *            The message
	 */
	public static void sendMessage(CommandSender sender, String message) {
		sender.sendMessage(TextUtil.color(message));
	}

	/**
	 * Sends a message to a command sender
	 * 
	 * @param sender
	 *            To send to
	 * @param component
	 *            The component to send
	 */
	public static void sendMessage(CommandSender sender, ChatComponent component) {
		if (sender instanceof Player) {
			sendChat(serialize(component), (Player) sender);
		} else {
			sender.sendMessage(component.getRawText());
		}
	}

	/**
	 * Sends a chat component message to a player
	 * 
	 * @param player
	 *            The player
	 * @param component
	 *            The component to send
	 * @param replacer
	 *            The message replacer
	 * @param players
	 *            The players to replace the tags with
	 * @param playerTypes
	 *            The player types
	 */
	public static void sendMessage(OnlinePlayerAccount player, ChatComponent component,
			IPlayerFormatController replacer, OfflinePlayerAccount[] players, String... playerTypes) {
		component = replacer.fix(component, players, playerTypes);
		sendMessage(player, component, replacer);
	}

	/**
	 * Sends a message to the player
	 * 
	 * @param player
	 *            The player
	 * @param component
	 *            The component
	 * @param replacer
	 *            The replacer
	 */
	public static void sendMessage(OnlinePlayerAccount player, ChatComponent component,
			IPlayerFormatController replacer) {
		IPlayerFormat format = replacer.get("player");
		Component comp = format.make(player);
		sendMessage(player.getPlayer(), component.clone().insert("{player}", comp));
		sendMessage(player.getOnlineAccount().getPlayer(), component);
	}

	/**
	 * @param string
	 *            To serialize
	 * @return the serialized version of a basic string
	 */
	public static Object serialize(String string) {
		return ReflectionUtil.invoke(serializeMethod, null, "{\"text\":\"" + string + "\"}");
	}

	/**
	 * @param component
	 *            To serialize
	 * @return the serialized version of the component
	 */
	public static Object serialize(ChatComponent component) {
		return ReflectionUtil.invoke(serializeMethod, null, component.toString());
	}

	/**
	 * Sends serialized chat to a player
	 * 
	 * @param serialized
	 *            To send
	 * @param player
	 *            To send to
	 */
	public static void sendChat(Object serialized, Player player) {
		version.sendChatPacket((byte) 0, player, serialized);
	}

}
