package net.expvp.core.logger;

import net.expvp.core.NullContainer;

/**
 * Class used to log chat
 * 
 * @author NullUser
 * @see AbstractLogger
 */
public class ChatLogger extends AbstractLogger {

	public ChatLogger(NullContainer container) {
		super(container, "chat log");
	}

}
