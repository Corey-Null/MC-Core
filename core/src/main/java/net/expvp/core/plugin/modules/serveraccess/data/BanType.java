package net.expvp.core.plugin.modules.serveraccess.data;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Enum for representing a type of server access restriction
 * 
 * @author NullUser
 */
public enum BanType {

	BAN("ban"), TEMP_BAN("temp-ban"), KICK("kick");

	private final static Map<String, BanType> types = Maps.newHashMap();

	static {
		for (BanType type : BanType.values()) {
			types.put(type.getName(), type);
		}
	}

	private final String name;

	private BanType(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param type
	 * @return the BanType based off the type
	 */
	public static BanType get(String type) {
		return types.get(type.toLowerCase());
	}

}
