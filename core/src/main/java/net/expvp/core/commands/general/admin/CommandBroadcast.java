package net.expvp.core.commands.general.admin;

import org.bukkit.command.CommandSender;

import net.expvp.api.TextUtil;
import net.expvp.api.bukkit.chat.Chat;
import net.expvp.api.bukkit.chat.ChatComponent;
import net.expvp.api.bukkit.chat.Component;
import net.expvp.core.NullContainer;
import net.expvp.core.commands.NullCommand;

/**
 * Class used for handling command /broadcast
 * 
 * @author NullUser
 * @see NullCommand
 */
public class CommandBroadcast extends NullCommand {

	public CommandBroadcast(NullContainer container) {
		super(container, "broadcast", "core.broadcast", "/broadcast <message>", 1, true);
	}

	/**
	 * @see NullCommand
	 */
	@Override
	public void execute(CommandSender sender, String[] args) {
		ChatComponent component = new ChatComponent();
		component.add(new Component(getMessage("format", TextUtil.connect(args, 0, args.length).replace("\\n", "\n"))));
		Chat.broadcast(component, getContainer().getPlayerFormatController());
	}

}
