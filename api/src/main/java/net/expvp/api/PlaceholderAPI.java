package net.expvp.api;

import java.awt.EventQueue;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.Maps;

import net.expvp.api.interfaces.player.OfflinePlayerAccount;

/**
 * API to fix placeholders when necessary
 * 
 * @author NullUser
 */
public final class PlaceholderAPI {

	private final static Pattern pattern = Pattern.compile("\\{(.*?)\\}");
	private final static Map<String, Function<String, String>> placeholders;
	private final static Map<String, BiFunction<Player, String, String>> playerPlaceholders;
	private final static Map<String, BiFunction<OfflinePlayerAccount, String, String>> accountPlaceholders;

	static {
		placeholders = Maps.newHashMap();
		playerPlaceholders = Maps.newHashMap();
		accountPlaceholders = Maps.newHashMap();
	}

	/**
	 * Removes all local placeholders
	 */
	public static void clean() {
		placeholders.entrySet().forEach((entry) -> {
			String str = entry.getKey();
			if (!str.contains("_")) {
				EventQueue.invokeLater(() -> placeholders.remove(str));
			}
		});
		playerPlaceholders.entrySet().forEach((entry) -> {
			String str = entry.getKey();
			if (!str.contains("_")) {
				EventQueue.invokeLater(() -> placeholders.remove(str));
			}
		});
		accountPlaceholders.entrySet().forEach((entry) -> {
			String str = entry.getKey();
			if (!str.contains("_")) {
				EventQueue.invokeLater(() -> placeholders.remove(str));
			}
		});
	}

	/**
	 * Formats a string with placeholders
	 * 
	 * @param string
	 *            To format
	 * @param player
	 *            To format with
	 * @return the formatted string
	 */
	public static String pass(String string, OfflinePlayerAccount player) {
		if (player.isOnline()) {
			string = pass(string, player.getOnlineAccount().getPlayer());
		}
		Matcher matcher = pattern.matcher(string);
		while (matcher.find()) {
			String key = matcher.group();
			string = string.replace(key, parse(key, player));
		}
		return string;
	}

	/**
	 * Formats a string with placeholders
	 * 
	 * @param string
	 *            To format
	 * @param player
	 *            To format with
	 * @return the formatted string
	 */
	public static String pass(String string, Player player) {
		string = pass(string);
		Matcher matcher = pattern.matcher(string);
		while (matcher.find()) {
			String key = matcher.group();
			string = string.replace(key, parse(key, player));
		}
		return string;
	}

	/**
	 * Formats a string with placeholders
	 * 
	 * @param string
	 *            To format
	 * @return the formatted string
	 */
	public static String pass(String string) {
		Matcher matcher = pattern.matcher(string);
		while (matcher.find()) {
			String key = matcher.group();
			string = string.replace(key, parse(key));
		}
		return string;
	}

	/**
	 * Parses a key with valid placeholders
	 * 
	 * @param key
	 *            To get the placeholder
	 * @return the placeholder return value
	 */
	private final static String parse(String key) {
		key = key.substring(1, key.length() - 1);
		if (placeholders.containsKey(key)) {
			return placeholders.get(key).apply(key);
		}
		return '{' + key + '}';
	}

	/**
	 * Parses a key with valid placeholders
	 * 
	 * @param key
	 *            To get the placeholder
	 * @param player
	 *            To format with
	 * @return the placeholder return value
	 */
	private final static String parse(String key, Player player) {
		key = key.substring(1, key.length() - 1);
		if (playerPlaceholders.containsKey(key)) {
			return playerPlaceholders.get(key).apply(player, key);
		}
		return '{' + key + '}';
	}

	/**
	 * Parses a key with valid placeholders
	 * 
	 * @param key
	 *            To get the placeholder
	 * @param player
	 *            To format with
	 * @return the placeholder return value
	 */
	private final static String parse(String key, OfflinePlayerAccount player) {
		key = key.substring(1, key.length() - 1);
		if (accountPlaceholders.containsKey(key)) {
			return accountPlaceholders.get(key).apply(player, key);
		}
		return '{' + key + '}';
	}

	/**
	 * Unregisters all placeholders related to a plugin
	 * 
	 * @param plugin
	 *            To unregister from
	 */
	public static void unregisterPlaceholders(Plugin plugin) {
		String pre = plugin.getName() + "_";
		placeholders.entrySet().forEach((entry) -> {
			String str = entry.getKey();
			if (str.startsWith(pre)) {
				EventQueue.invokeLater(() -> placeholders.remove(str));
			}
		});
		playerPlaceholders.entrySet().forEach((entry) -> {
			String str = entry.getKey();
			if (str.startsWith(pre)) {
				EventQueue.invokeLater(() -> placeholders.remove(str));
			}
		});
		accountPlaceholders.entrySet().forEach((entry) -> {
			String str = entry.getKey();
			if (str.startsWith(pre)) {
				EventQueue.invokeLater(() -> placeholders.remove(str));
			}
		});
	}

	/**
	 * Unregisters all placeholders with the specified value
	 * 
	 * @param value
	 *            To unregister
	 */
	public static void unregisterPlaceholder(String value) {
		placeholders.remove(value);
		playerPlaceholders.remove(value);
		accountPlaceholders.remove(value);
	}

	/**
	 * Registers placeholders
	 * 
	 * @param value
	 *            The key for the placeholder
	 * @param function
	 *            To apply to the placeholder
	 */
	public static void registerPlaceholder(String value, Function<String, String> function) {
		placeholders.put(value, function);
	}

	/**
	 * Registers player placeholders
	 * 
	 * @param value
	 *            The key for the placeholder
	 * @param function
	 *            To apply to the placeholder
	 */
	public static void registerPlayerPlaceholder(String value, BiFunction<Player, String, String> function) {
		playerPlaceholders.put(value, function);
	}

	/**
	 * Registers account placeholders
	 * 
	 * @param value
	 *            The key for the placeholder
	 * @param function
	 *            To apply to the placeholder
	 */
	public static void registerAccountPlaceholder(String value,
			BiFunction<OfflinePlayerAccount, String, String> function) {
		accountPlaceholders.put(value, function);
	}

	/**
	 * Registers placeholders
	 * 
	 * @param plugin
	 *            Plugin to register from
	 * @param value
	 *            Key for the placeholder
	 * @param function
	 *            The function to apply the placeholder
	 */
	public static void registerPlaceholder(Plugin plugin, String value, Function<String, String> function) {
		placeholders.put(plugin.getName() + "_" + value, function);
	}

	/**
	 * Registers player placeholders
	 * 
	 * @param plugin
	 *            Plugin to register from
	 * @param value
	 *            Key for the placeholder
	 * @param function
	 *            The function to apply the placeholder
	 */
	public static void registerPlayerPlaceholder(Plugin plugin, String value,
			BiFunction<Player, String, String> function) {
		playerPlaceholders.put(plugin.getName() + "_" + value, function);
	}

	/**
	 * Registers account placeholders
	 * 
	 * @param plugin
	 *            Plugin to register from
	 * @param value
	 *            Key for the placeholder
	 * @param function
	 *            The function to apply the placeholder
	 */
	public static void registerAccountPlaceholder(Plugin plugin, String value,
			BiFunction<OfflinePlayerAccount, String, String> function) {
		accountPlaceholders.put(plugin.getName() + "_" + value, function);
	}

}
