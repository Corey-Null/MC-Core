package net.expvp.api.interfaces.chat;

import net.expvp.api.enums.PropertyType;

/**
 * Interface for holding a chat property (hover/click)
 * 
 * @author NullUser
 */
public interface ChatProperty {

	/**
	 * @return the name of the property
	 */
	String getName();

	/**
	 * @return the type of the property
	 */
	PropertyType getType();

	/**
	 * @param type
	 *            To set the property type
	 */
	void setType(PropertyType type);

	/**
	 * @return the value of the action
	 */
	String getValue();

	/**
	 * @param value
	 *            To set the action value
	 */
	void setValue(String value);

}
