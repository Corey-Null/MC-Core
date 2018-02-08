package net.expvp.api.enums;

/**
 * Enum used for listing out the config options for vanished players
 * 
 * @author NullUser
 */
public enum VanishOption {

	SILENT_JOIN("silent-join"),
	SILENT_QUIT("silent-quit"),
	FAKE_JOIN("fake-join"),
	FAKE_QUIT("fake-quit");

	private String type;

	private VanishOption(String type) {
		this.type = type;
	}

	/**
	 * @return the type of config option
	 */
	public String getName() {
		return type;
	}

}
