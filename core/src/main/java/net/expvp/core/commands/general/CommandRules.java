package net.expvp.core.commands.general;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.google.common.collect.Lists;

import net.expvp.api.TextUtil;
import net.expvp.api.interfaces.Reloadable;
import net.expvp.core.NullContainer;
import net.expvp.core.commands.NullCommand;

/**
 * Command used for handling command /rules
 * 
 * @author NullUser
 * @see NullCommand
 * @see Reloadable
 */
public class CommandRules extends NullCommand implements Reloadable {

	private final List<String> rules;

	public CommandRules(NullContainer container) {
		super(container, "rules", "core.rules", "/rules", 0, true);
		container.getReloadableObjects().add(this);
		rules = Lists.newArrayList();
		reload();
	}

	/**
	 * @see Reloadable
	 */
	@Override
	public void reload() {
		getContainer().getPlugin().saveResource("rules.txt", false);
		rules.clear();
		File file = new File(getContainer().getPlugin().getDataFolder(), "rules.txt");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = reader.readLine()) != null) {
				rules.add(TextUtil.color(line));
			}
			reader.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @see NullCommand
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		rules.forEach(sender::sendMessage);
	}

}
