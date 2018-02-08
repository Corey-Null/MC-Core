package net.expvp.api.enums;

/**
 * Enum for handling all property types
 * 
 * @author NullUser
 */
public enum PropertyType {

	SHOW_TEXT("show_text"),
	RUN_COMMAND("run_command"),
	SUGGEST_COMMAND("suggest_command"),
	OPEN_URL("open_url");

	private final String name;

	private PropertyType(String name) {
		this.name = name;
	}

	/**
	 * Overrides the default toString option
	 * 
	 * @return the string value of the type
	 */
	@Override
	public String toString() {
		return name;
	}

}
