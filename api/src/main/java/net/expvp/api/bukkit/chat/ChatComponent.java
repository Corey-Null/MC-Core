package net.expvp.api.bukkit.chat;

import java.util.List;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;

import net.expvp.api.interfaces.chat.ChatProperty;

/**
 * Class for handling a list of components to serialize
 * 
 * @author NullUser
 */
public class ChatComponent {

	private final List<Component> components;

	public ChatComponent() {
		this.components = Lists.newArrayList();
	}

	/**
	 * Inserts a component wherever a string is
	 * 
	 * @param replacing
	 *            The string to replace
	 * @param toInsert
	 *            The component to insert
	 * @return this
	 */
	public ChatComponent insert(String replacing, Component toInsert) {
		for (int i = 0; i < components.size(); i++) {
			Component component = components.get(i);
			List<ChatProperty> properties = component.getProperties();
			String text = component.getText();
			if (!text.contains(replacing)) {
				continue;
			}
			int index = i;
			String[] splitters = components.get(i).getText().split(Pattern.quote(replacing));
			removeIndex(i);
			for (int a = 0; a < splitters.length; a++) {
				Component comp = new Component(splitters[a]);
				comp.setProperties(properties);
				insert(index, comp);
				index++;
				if (a + 1 != splitters.length || text.endsWith(replacing)) {
					insert(index, toInsert);
					index++;
				}
			}
		}
		return this;
	}

	/**
	 * Inserts a component at a specified index in the list
	 * 
	 * @param index
	 *            The index
	 * @param component
	 *            To add
	 * @return this
	 */
	public ChatComponent insert(int index, Component component) {
		components.add(index, component);
		return this;
	}

	/**
	 * Adds a message
	 * 
	 * @param message
	 *            To add
	 * @return this
	 */
	public ChatComponent add(String message) {
		return add(new Component(message));
	}

	/**
	 * Adds a component
	 * 
	 * @param component
	 *            To add
	 * @return this
	 */
	public ChatComponent add(Component component) {
		components.add(component);
		return this;
	}

	/**
	 * Removes the specific index
	 * 
	 * @param index
	 *            The index
	 */
	public void removeIndex(int index) {
		if (index < 0) {
			index = -index;
		}
		if (index >= components.size()) {
			return;
		}
		components.remove(index);
	}

	/**
	 * @return the raw text
	 */
	public String getRawText() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < components.size(); i++) {
			builder.append(components.get(i).getText());
			if (i + 1 != components.size()) {
				builder.append(' ');
			}
		}
		return builder.toString();
	}

	/**
	 * @return the cloned chat component
	 */
	public ChatComponent clone() {
		ChatComponent comp = new ChatComponent();
		comp.components.addAll(components);
		return comp;
	}

	/**
	 * Overrides the current toString() function to return the json value
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("[\"\",");
		for (int i = 0; i < components.size(); i++) {
			builder.append(components.get(i).toString());
			if (i + 1 < components.size()) {
				builder.append(',');
			}
		}
		return builder.append(']').toString();
	}

}
