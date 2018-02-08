package net.expvp.core.plugin.modules;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import net.expvp.api.interfaces.Container;
import net.expvp.api.interfaces.Reloadable;
import net.expvp.api.interfaces.plugin.IModule;

/**
 * Module pattern for parts of the plugin
 * 
 * @author NullUser
 * @see Reloadable
 * @param <T>
 *            Container for the module
 */
public class Module<T extends Container<? extends JavaPlugin>> implements IModule<T> {

	private final T container;
	private final String name;
	private final boolean hasConfig;
	private final File dataFolder;
	private final File configFile;
	private FileConfiguration config;
	private boolean enabled;

	/**
	 * Main module initializer for the Module's fields
	 * 
	 * @param container
	 *            Container of the module
	 * @param name
	 *            Name of the module
	 * @param hasConfig
	 *            Defines if the module has a config
	 */
	public Module(T container, String name, boolean hasConfig) {
		this.container = container;
		this.name = name;
		this.dataFolder = new File(container.getPlugin().getDataFolder() + File.separator + name);
		if (!this.dataFolder.exists()) {
			this.dataFolder.mkdirs();
		}
		this.hasConfig = hasConfig;
		if (hasConfig) {
			this.configFile = new File(dataFolder, "config.yml");
			createFiles();
			this.config = YamlConfiguration.loadConfiguration(configFile);
		} else {
			this.configFile = null;
			this.config = null;
		}
		this.enabled = true;
		registerPlaceholders();
	}

	/**
	 * Enables the module
	 */
	public void enable() {
		enabled = true;
	}

	/**
	 * Disables the module
	 */
	public void disable() {
		enabled = false;
	}

	/**
	 * @return true if the module is enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @return the container for the Module
	 */
	public T getContainer() {
		return container;
	}

	/**
	 * @see Reloadable
	 */
	@Override
	public void reload() {
		if (hasConfig) {
			createFiles();
			config = YamlConfiguration.loadConfiguration(configFile);
		}
	}

	/**
	 * Creates all required configuration files
	 */
	public void createFiles() {
		if (hasConfig) {
			if (!dataFolder.exists()) {
				dataFolder.mkdirs();
			}
			if (!configFile.exists()) {
				container.getPlugin().saveResource(getName() + "/config.yml", false);
			}
		}
	}

	/**
	 * @return true if module has a config
	 */
	public boolean hasConfig() {
		return hasConfig;
	}

	/**
	 * @return module's config
	 */
	public FileConfiguration getConfig() {
		return config;
	}

	/**
	 * @return module's config file
	 */
	public File getConfigFile() {
		return configFile;
	}

	/**
	 * @return module's data folder
	 */
	public File getDataFolder() {
		return dataFolder;
	}

	/**
	 * @return module's name
	 */
	public String getName() {
		return name;
	}

	@Override
	public void registerCommands() {
	}

	@Override
	public void registerListeners() {
	}

	@Override
	public void registerPlaceholders() {
	}

}
