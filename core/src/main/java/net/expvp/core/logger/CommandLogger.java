package net.expvp.core.logger;

import net.expvp.core.NullContainer;

/**
 * Class used to log commands
 * 
 * @author NullUser
 * @see AbstractLogger
 */
public class CommandLogger extends AbstractLogger {

	public CommandLogger(NullContainer container) {
		super(container, "commands log");
	}

}
