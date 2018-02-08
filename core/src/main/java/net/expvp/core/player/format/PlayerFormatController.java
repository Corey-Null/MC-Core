package net.expvp.core.player.format;

import java.io.File;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.collect.Maps;

import net.expvp.api.bukkit.chat.ChatComponent;
import net.expvp.api.interfaces.Reloadable;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.api.interfaces.player.format.IPlayerFormatController;
import net.expvp.core.NullContainer;

/**
 * Class used for handling player formatting
 * 
 * @author NullUser
 * @see Reloadable
 */
public class PlayerFormatController implements IPlayerFormatController {

	private final NullContainer container;
	private final File file;
	private PlayerFormat defaultFormat;
	private final Map<String, PlayerFormat> playerFormats;

	public PlayerFormatController(NullContainer container) {
		this.container = container;
		this.file = new File(container.getPlugin().getDataFolder(), "player.yml");
		this.playerFormats = Maps.newHashMap();
		reload();
	}

	/**
	 * @see Reloadable
	 */
	@Override
	public void reload() {
		playerFormats.clear();
		container.getPlugin().saveResource("player.yml", false);
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		defaultFormat = new PlayerFormat(config.getString("default-format", "{effective_prefix} &b{player_name}"));
		if (config.contains("default-hover") && config.isString("default-hover")) {
			defaultFormat.setHover(config.getString("default-hover"));
		}
		if (config.contains("default-click") && config.isString("default-click")) {
			defaultFormat.setClick(config.getString("default-click"));
		}
		for (String key : config.getKeys(false)) {
			String fKey = key + ".format";
			if (!config.isConfigurationSection(key) || !config.contains(fKey) || !config.isString(fKey)) {
				continue;
			}
			PlayerFormat format = new PlayerFormat(config.getString(fKey));
			if (config.contains(key + ".hover") && config.isString(key + ".hover")) {
				format.setHover(config.getString(key + ".hover"));
			}
			if (config.contains(key + ".click") && config.isString(key + ".click")) {
				format.setClick(config.getString(key + ".click"));
			}
			playerFormats.put(key, format);
		}
	}

	/**
	 * @param parsable
	 * @return the PlayerFormat to be used based on the key
	 */
	public PlayerFormat get(String parsable) {
		String[] parts = parsable.split("_");
		if (parts.length == 1 || !playerFormats.containsKey(parts[1])) {
			return defaultFormat;
		}
		return playerFormats.get(parts[1]);
	}

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
	public ChatComponent fix(String str, OfflinePlayerAccount[] accounts, String[] names) {
		ChatComponent component = new ChatComponent();
		component.add(str);
		return fix(component, accounts, names);
	}

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
	public ChatComponent fix(ChatComponent component, OfflinePlayerAccount[] accounts, String[] names) {
		ChatComponent returning = component.clone();
		for (int i = 0; i < names.length && i < accounts.length; i++) {
			returning.insert("{" + names[i] + "}", get(names[i]).make(accounts[i]));
		}
		return returning;
	}

}
