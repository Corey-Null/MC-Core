package net.expvp.core.player.format;

import net.expvp.api.PlaceholderAPI;
import net.expvp.api.TextUtil;
import net.expvp.api.bukkit.chat.Component;
import net.expvp.api.bukkit.chat.properties.ClickProperty;
import net.expvp.api.bukkit.chat.properties.HoverProperty;
import net.expvp.api.enums.PropertyType;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.api.interfaces.player.format.IPlayerFormat;

/**
 * Class used for holding player format data
 * 
 * @author NullUser
 */
public class PlayerFormat implements IPlayerFormat {

	private final String text;
	private String hover;
	private String click;

	public PlayerFormat(String text) {
		this.text = text;
	}

	/**
	 * @param click
	 *            To set the click
	 */
	public void setClick(String click) {
		this.click = click;
	}

	/**
	 * @param click
	 *            To set the hover
	 */
	public void setHover(String hover) {
		this.hover = hover;
	}

	/**
	 * @return the click
	 */
	public String getClick() {
		return click;
	}

	/**
	 * @return the hover
	 */
	public String getHover() {
		return hover;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param player
	 *            Makes a component to represent the player
	 * @return the made component from the player
	 */
	public Component make(OfflinePlayerAccount player) {
		Component component = new Component(PlaceholderAPI.pass(text, player));
		if (click != null) {
			component.addProperty(loadClick(player));
		}
		if (hover != null) {
			component.addProperty(loadHover(player));
		}
		return component;
	}

	/**
	 * @param player
	 *            To load from
	 * @return the loaded hover property
	 */
	public HoverProperty loadHover(OfflinePlayerAccount player) {
		return new HoverProperty(PropertyType.SHOW_TEXT, TextUtil.color(PlaceholderAPI.pass(hover, player)));
	}

	/**
	 * @param player
	 *            To load from
	 * @return the loaded click property
	 */
	public ClickProperty loadClick(OfflinePlayerAccount player) {
		return new ClickProperty(PropertyType.RUN_COMMAND, PlaceholderAPI.pass(click, player));
	}

}
