package net.expvp.core;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import net.expvp.api.PlaceholderAPI;
import net.expvp.api.TextUtil;
import net.expvp.api.bukkit.chat.Chat;
import net.expvp.api.bukkit.chat.ChatComponent;
import net.expvp.api.interfaces.IMessages;
import net.expvp.api.interfaces.Reloadable;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.api.interfaces.player.format.IPlayerFormat;
import net.expvp.api.interfaces.player.format.IPlayerFormatController;

/**
 * Class for handling all language
 * 
 * @author NullUser
 */
public class Messages implements IMessages {

	private final static Pattern pattern = Pattern.compile("\\{(.*?)\\}");
	private final NullContainer container;
	private final File file;
	private FileConfiguration cachedConfiguration;

	/**
	 * Solo constructor for intializing the Messages
	 * 
	 * @param container
	 *            To set the container
	 * @param file
	 *            To set the file
	 */
	public Messages(NullContainer container, File file) {
		this.container = container;
		this.file = file;
		container.getPlugin().saveResource("messages.yml", false);
		cachedConfiguration = YamlConfiguration.loadConfiguration(file);
	}

	/**
	 * @see Reloadable
	 */
	@Override
	public void reload() {
		if (!this.file.exists()) {
			this.container.getPlugin().saveResource("messages.yml", false);
		}
		this.cachedConfiguration = YamlConfiguration.loadConfiguration(this.file);
	}

	/**
	 * @see IMessages
	 */
	@Override
	public String getMessage(String key, Object... args) {
		String message = this.cachedConfiguration.getString(key, ChatColor.RED + "Config does not contain " + key);
		for (int i = 0; i < args.length; i++) {
			message = message.replace("{" + i + "}", String.valueOf(args[i]));
		}
		return TextUtil.color(message);
	}

	/**
	 * @see IMessages
	 */
	public void sendMessage(CommandSender sender, OfflinePlayerAccount target, String key, Object... args) {
		sendMessage(sender, target, key, new OfflinePlayerAccount[] {}, new String[] {}, args);
	}

	/**
	 * @see IMessages
	 */
	public void sendMessage(CommandSender sender, OfflinePlayerAccount target, String key,
			OfflinePlayerAccount[] players, String[] objectNames, Object... args) {
		ChatComponent component = new ChatComponent();
		if (sender instanceof Player) {
			component.add(PlaceholderAPI.pass(getMessage(key, args),
					container.getPlayerDataHandler().getAccount(((Player) sender).getUniqueId())));
		} else {
			component.add(PlaceholderAPI.pass(getMessage(key, args)));
		}
		IPlayerFormatController controller = container.getPlayerFormatController();
		if (target != null) {
			Matcher matcher = pattern.matcher(component.getRawText());
			while (matcher.find()) {
				String group = matcher.group();
				if (!group.startsWith("{target")) {
					continue;
				}
				IPlayerFormat format = controller.get(group.substring(1, group.length() - 1));
				component = component.insert(group, format.make(target));
			}
		}
		for (int i = 0; i < players.length && i < objectNames.length; i++) {
			String name = objectNames[i];
			IPlayerFormat format = controller.get(name);
			component = component.insert('{' + name + '}', format.make(players[i]));
		}
		Chat.sendMessage(sender, component);
	}

	/**
	 * @see IMessages
	 */
	@Override
	public File getFile() {
		return this.file;
	}

}
