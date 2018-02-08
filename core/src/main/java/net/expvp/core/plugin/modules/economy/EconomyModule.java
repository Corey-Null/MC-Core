package net.expvp.core.plugin.modules.economy;

import java.text.DecimalFormat;

import net.expvp.api.PlaceholderAPI;
import net.expvp.api.TextUtil;
import net.expvp.api.interfaces.Reloadable;
import net.expvp.core.NullContainer;
import net.expvp.core.plugin.modules.Module;
import net.expvp.core.plugin.modules.economy.commands.BalanceCommand;
import net.expvp.core.plugin.modules.economy.commands.BaltopCommand;
import net.expvp.core.plugin.modules.economy.commands.EconomyCommand;
import net.expvp.core.plugin.modules.economy.commands.PayCommand;

/**
 * Module for handling the economy
 * 
 * @author NullUser
 * @see Module
 */
public class EconomyModule extends Module<NullContainer> {

	private DecimalFormat deciFormat;
	private String currency;
	private String format;

	/**
	 * Main initializer for Module's fields
	 * 
	 * @param container
	 * @see Module
	 */
	public EconomyModule(NullContainer container) {
		super(container, "economy", true);
		this.deciFormat = new DecimalFormat(getConfig().getString("format", "##,###.##"));
		this.currency = getConfig().getString("currency", "$");
		this.format = getConfig().getString("complete-format", "&b{currency}&c{balance}");
		PlaceholderAPI.registerAccountPlaceholder("balance", (player, string) -> {
			return String.valueOf(player.getBalance());
		});
	}

	/**
	 * @see Module
	 */
	@Override
	public void registerPlaceholders() {
		PlaceholderAPI.registerAccountPlaceholder("balance", (player, string) -> {
			return String.valueOf(player.getBalance());
		});
	}

	/**
	 * @see Module
	 */
	@Override
	public void registerCommands() {
		getContainer().registerCommand(new BalanceCommand(this));
		getContainer().registerCommand(new BaltopCommand(this));
		getContainer().registerCommand(new EconomyCommand(this));
		getContainer().registerCommand(new PayCommand(this));
	}

	/**
	 * @see Reloadable
	 */
	@Override
	public void reload() {
		super.reload();
		this.deciFormat = new DecimalFormat(getConfig().getString("format", "##,###.##"));
		this.currency = getConfig().getString("currency", "$");
		this.format = getConfig().getString("complete-format", "&b{currency}&c{balance}");
	}

	/**
	 * Formats a double to fit the currency format
	 * 
	 * @param value
	 *            To format
	 * @return formatted value
	 */
	public String format(double value) {
		String formatted = format;
		String get = deciFormat.format(value);
		formatted = formatted.replace("{balance}", get);
		formatted = formatted.replace("{currency}", currency);
		return TextUtil.color(formatted);
	}

}
