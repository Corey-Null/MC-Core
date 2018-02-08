package net.expvp.api.bukkit.chat;

import java.util.List;

import com.google.common.collect.Lists;

import net.expvp.api.TextUtil;
import net.expvp.api.interfaces.chat.ChatProperty;

/**
 * Class for holding the values of a component for json
 * 
 * @author NullUser
 */
public class Component {

	private final String text;
	private List<ChatProperty> properties;

	public Component(String str) {
		this(str, true);
	}

	public Component(String str, boolean color) {
		if (color) {
			str = TextUtil.color(str);
		}
		this.properties = Lists.newArrayList();
		this.text = str;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @return the properties
	 */
	public List<ChatProperty> getProperties() {
		return properties;
	}

	/**
	 * Sets the properties of the component
	 * 
	 * @param properties
	 *            To set the properties
	 */
	public void setProperties(List<ChatProperty> properties) {
		this.properties = properties;
	}

	/**
	 * Adds a property to the component
	 * 
	 * @param property
	 *            To add to the properties
	 */
	public void addProperty(ChatProperty property) {
		this.properties.add(property);
	}

	/**
	 * Overrides the current toString() function to return the json value
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("{\"text\":\"").append(text).append('"');
		for (ChatProperty property : properties) {
			builder.append(',').append('"');
			builder.append(property.getName()).append("\":{");
			builder.append("\"action\":\"").append(property.getType().toString()).append('"');
			builder.append(",\"value\":\"").append(property.getValue()).append('"');
			builder.append('}');
		}
		return builder.append('}').toString();
	}

}
