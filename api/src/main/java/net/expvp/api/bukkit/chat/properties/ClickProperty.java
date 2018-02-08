package net.expvp.api.bukkit.chat.properties;

import net.expvp.api.enums.PropertyType;
import net.expvp.api.interfaces.chat.ChatProperty;

/**
 * Class for containing the click property
 * 
 * @author NullUser
 * @see ChatProperty
 */
public class ClickProperty implements ChatProperty {

	private PropertyType type;
	private String value;

	public ClickProperty(PropertyType type) {
		this(type, "");
	}

	public ClickProperty(PropertyType type, String value) {
		this.type = type;
		this.value = value;
	}

	/**
	 * @see ChatProperty
	 */
	@Override
	public String getName() {
		return "clickEvent";
	}

	/**
	 * @see ChatProperty
	 */
	@Override
	public PropertyType getType() {
		return type;
	}

	/**
	 * @see ChatProperty
	 */
	@Override
	public void setType(PropertyType type) {
		this.type = type;
	}

	/**
	 * @see ChatProperty
	 */
	@Override
	public String getValue() {
		return value;
	}

	/**
	 * @see ChatProperty
	 */
	@Override
	public void setValue(String value) {
		this.value = value;
	}

}
