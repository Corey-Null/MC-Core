package net.expvp.api.interfaces.plugin;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import net.expvp.api.interfaces.Container;
import net.expvp.api.interfaces.Reloadable;

public interface IModule<T extends Container<? extends JavaPlugin>> extends Reloadable {

	/**
	 * Enables the module
	 */
	void enable();

	/**
	 * Disables the module
	 */
	void disable();

	/**
	 * @return true if the module is enabled
	 */
	boolean isEnabled();

	/**
	 * @return the container for the Module
	 */
	T getContainer();

	/**
	 * Registers commands
	 */
	void registerCommands();

	/**
	 * Registers Listeners
	 */
	void registerListeners();

	/**
	 * Registers placeholders
	 */
	void registerPlaceholders();

	/**
	 * Creates all required configuration files
	 */
	void createFiles();

	/**
	 * @return true if module has a config
	 */
	boolean hasConfig();

	/**
	 * @return module's config
	 */
	FileConfiguration getConfig();

	/**
	 * @return module's config file
	 */
	File getConfigFile();

	/**
	 * @return module's data folder
	 */
	File getDataFolder();

	/**
	 * @return module's name
	 */
	String getName();

}
