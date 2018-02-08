package net.expvp.api.enums;

/**
 * Class used for handling vanish priority
 * 
 * @author NullUser
 */
public enum VanishPriority {
	VISIBLE(0),
	PLAYERS(1),
	STAFF(2);

	private final int priority;

	private VanishPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * @return the priority of the VanishPriority
	 */
	public int getPriority() {
		return priority;
	}

}
