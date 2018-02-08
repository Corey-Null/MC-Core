package net.expvp.core;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Sets;

import net.expvp.api.interfaces.Container;
import net.expvp.api.interfaces.ICommand;
import net.expvp.api.interfaces.IMessages;
import net.expvp.api.interfaces.Reloadable;
import net.expvp.api.interfaces.Saveable;
import net.expvp.api.interfaces.data.IDataSavingThread;
import net.expvp.api.interfaces.data.IWorldData;
import net.expvp.api.interfaces.data.PlayerDataSavingMethod;
import net.expvp.api.interfaces.data.WorldDataSavingMethod;
import net.expvp.api.interfaces.logging.ILogger;
import net.expvp.api.interfaces.player.IPlayerDataHandler;
import net.expvp.api.interfaces.player.format.IPlayerFormatController;
import net.expvp.api.interfaces.plugin.IModule;
import net.expvp.api.interfaces.teleport.ITeleportRequestHandler;
import net.expvp.api.interfaces.vanish.IVanishManager;
import net.expvp.api.interfaces.version.VersionProvider;
import net.expvp.core.logger.ChatLogger;
import net.expvp.core.logger.CommandLogger;
import net.expvp.core.plugin.CorePlugin;

/**
 * Simple container class for the NullCore containing all required wrappers
 * 
 * @author NullUser
 * @version 1.0
 * @see Container
 */
public class NullContainer implements Container<CorePlugin> {

	private static NullContainer instance = null;

	/**
	 * @return the NullContainer instance
	 */
	public static NullContainer getInstance() {
		return instance;
	}

	private VersionProvider versionProvider;
	private boolean stopped;
	private Set<ICommand> commands;
	private Set<Saveable> saveableObjects;
	private Set<Reloadable> reloadableObjects;
	private JavaPlugin plugin;
	private IPlayerDataHandler playerDataHandler;
	private IDataSavingThread dataSavingThread;
	private PlayerDataSavingMethod playerDataSavingMethod;
	private WorldDataSavingMethod worldDataSavingMethod;
	private IWorldData worldData;
	private IMessages messages;
	private Set<IModule<? extends Container<?>>> modules;
	private ITeleportRequestHandler teleportRequestHandler;
	private IVanishManager vanishManager;
	private ILogger commandLogger;
	private ILogger chatLogger;
	private IPlayerFormatController playerFormatController;

	/**
	 * Initializes the NullContainer with null or preset objects
	 */
	public NullContainer() {
		if (instance != null) {
			throw new SecurityException("This class cannot be created twice");
		}
		instance = this;
		this.versionProvider = null;
		this.stopped = false;
		this.commands = Sets.newHashSet();
		this.saveableObjects = Sets.newHashSet();
		this.reloadableObjects = Sets.newHashSet();
		this.plugin = null;
		this.playerDataHandler = null;
		this.dataSavingThread = null;
		this.playerDataSavingMethod = null;
		this.worldDataSavingMethod = null;
		this.worldData = null;
		this.messages = null;
		this.modules = Sets.newHashSet();
		this.teleportRequestHandler = null;
		this.vanishManager = null;
		this.commandLogger = null;
		this.chatLogger = null;
		this.playerFormatController = null;
	}

	// REGISTERING
	/**
	 * @see Container<T>
	 */
	public void registerCommand(ICommand command) {
		command.getContainer().getPlugin().getCommand(command.getCommand()).setExecutor(command);
		commands.add(command);
	}

	/**
	 * @see Container<T>
	 */
	public void registerModule(IModule<? extends Container<?>> module) {
		if (module == null) {
			return;
		}
		modules.add(module);
		if (module instanceof Saveable) {
			saveableObjects.add((Saveable) module);
		}
		reloadableObjects.add((Reloadable) module);
		module.registerCommands();
		module.registerListeners();
		module.enable();
	}

	/**
	 * @see Container<T>
	 */
	public void removeSaveable(Class<? extends Saveable> clazz) {
		Saveable s = null;
		for (Saveable sa : saveableObjects) {
			if (sa.getClass().isInstance(clazz)) {
				s = sa;
			}
		}
		if (s != null) {
			saveableObjects.remove(s);
		}
	}

	/**
	 * @see Container<T>
	 */
	public void removeReloadable(Class<? extends Reloadable> clazz) {
		Reloadable r = null;
		for (Reloadable re : reloadableObjects) {
			if (re.getClass().isInstance(clazz)) {
				r = re;
			}
		}
		if (r != null) {
			reloadableObjects.remove(r);
		}
	}

	// GETTERS
	/**
	 * @see Container<T>
	 */
	@Override
	public Set<ICommand> getCommands() {
		return commands;
	}

	/**
	 * @see Container<T>
	 */
	public Set<IModule<?>> getModules() {
		return Collections.unmodifiableSet(modules);
	}

	/**
	 * @see Container<T>
	 */
	public IPlayerFormatController getPlayerFormatController() {
		return playerFormatController;
	}

	/**
	 * @see Container<T>
	 */
	public ILogger getChatLogger() {
		return chatLogger;
	}

	/**
	 * @see Container<T>
	 */
	public ILogger getCommandLogger() {
		return commandLogger;
	}

	/**
	 * @see Container<T>
	 */
	public IVanishManager getVanishManager() {
		return vanishManager;
	}

	/**
	 * @see Container<T>
	 */
	public ITeleportRequestHandler getTeleportRequestHandler() {
		return teleportRequestHandler;
	}

	/**
	 * @see Container<T>
	 */
	public boolean isModuleEnabled(String moduleName) {
		IModule<? extends Container<?>> module = getModule(moduleName);
		return !(module == null || !module.isEnabled());
	}

	/**
	 * @see Container<T>
	 */
	public IModule<? extends Container<?>> getModule(String name) {
		Optional<IModule<? extends Container<?>>> optional = modules.stream()
				.filter(module -> module.getName().equalsIgnoreCase(name)).findAny();
		return optional.isPresent() ? optional.get() : null;
	}

	/**
	 * @see Container<T>
	 */
	public IMessages getMessages() {
		return messages;
	}

	/**
	 * @see Container<T>
	 */
	public IWorldData getWorldData() {
		return worldData;
	}

	/**
	 * @see Container<T>
	 */
	public WorldDataSavingMethod getWorldDataSavingMethod() {
		return worldDataSavingMethod;
	}

	/**
	 * @see Container<T>
	 */
	public PlayerDataSavingMethod getPlayerDataSavingMethod() {
		return playerDataSavingMethod;
	}

	/**
	 * @see Container<T>
	 */
	public IDataSavingThread getDataSavingThread() {
		return dataSavingThread;
	}

	/**
	 * @see Container<T>
	 */
	public IPlayerDataHandler getPlayerDataHandler() {
		return playerDataHandler;
	}

	/**
	 * @see Container<T>
	 */
	public JavaPlugin getPlugin() {
		return plugin;
	}

	/**
	 * @see Container<T>
	 */
	public VersionProvider getVersionProvider() {
		return versionProvider;
	}

	/**
	 * @see Container<T>
	 */
	public Set<Reloadable> getReloadableObjects() {
		return reloadableObjects;
	}

	/**
	 * @see Container<T>
	 */
	public Set<Saveable> getSaveableObjects() {
		return saveableObjects;
	}

	// SETTERS
	/**
	 * @see Container<T>
	 */
	public void setPlayerFormatController(IPlayerFormatController playerFormatController) {
		this.playerFormatController = playerFormatController;
		removeReloadable(IPlayerFormatController.class);
		reloadableObjects.add(playerFormatController);
	}

	/**
	 * @see Container<T>
	 */
	public void setChatLogger(ILogger chatLogger) {
		this.chatLogger = chatLogger;
		removeSaveable(ChatLogger.class);
		saveableObjects.add(chatLogger);
	}

	/**
	 * @see Container<T>
	 */
	public void setCommandLogger(ILogger commandLogger) {
		this.commandLogger = commandLogger;
		removeSaveable(CommandLogger.class);
		saveableObjects.add(commandLogger);
	}

	/**
	 * @see Container<T>
	 */
	public void setVanishManager(IVanishManager vanishManager) {
		this.vanishManager = vanishManager;
	}

	/**
	 * @see Container<T>
	 */
	public void setTeleportRequestHandler(ITeleportRequestHandler teleportRequestHandler) {
		this.teleportRequestHandler = teleportRequestHandler;
	}

	/**
	 * @see Container<T>
	 */
	public void setMessages(IMessages messages) {
		if (messages == null) {
			return;
		}
		if (this.messages != null) {
			Reloadable toRemove = null;
			for (Reloadable saveable : this.reloadableObjects) {
				if (saveable instanceof IMessages) {
					toRemove = saveable;
					break;
				}
			}
			if (toRemove != null) {
				this.reloadableObjects.remove(toRemove);
			}
		}
		this.messages = messages;
		this.reloadableObjects.add(messages);
	}

	/**
	 * @see Container<T>
	 */
	public void setWorldData(IWorldData worldData) {
		if (worldData == null) {
			return;
		}
		if (this.worldData != null) {
			Saveable toRemove = null;
			for (Saveable saveable : this.saveableObjects) {
				if (saveable instanceof IWorldData) {
					toRemove = saveable;
					break;
				}
			}
			if (toRemove != null) {
				this.saveableObjects.remove(toRemove);
			}
		}
		this.worldData = worldData;
		this.saveableObjects.add(worldData);
	}

	/**
	 * @see Container<T>
	 */
	public void setWorldDataSavingMethod(WorldDataSavingMethod worldDataSavingMethod) {
		if (worldDataSavingMethod == null) {
			return;
		}
		this.worldDataSavingMethod = worldDataSavingMethod;
	}

	/**
	 * @see Container<T>
	 */
	public void setPlayerDataSavingMethod(PlayerDataSavingMethod playerDataSavingMethod) {
		if (playerDataSavingMethod == null) {
			return;
		}
		this.playerDataSavingMethod = playerDataSavingMethod;
	}

	/**
	 * @see Container<T>
	 */
	public void setDataSavingThread(IDataSavingThread dataSavingThread) {
		if (dataSavingThread == null) {
			return;
		}
		this.dataSavingThread = dataSavingThread;
	}

	/**
	 * @see Container<T>
	 */
	public void setPlayerDataHandler(IPlayerDataHandler playerDataHandler) {
		if (this.playerDataHandler != null) {
			throw new SecurityException("This module has already been set.");
		}
		if (playerDataHandler == null) {
			return;
		}
		this.playerDataHandler = playerDataHandler;
		this.saveableObjects.add(playerDataHandler);
	}

	/**
	 * @see Container<T>
	 */
	public void setPlugin(JavaPlugin plugin) {
		if (this.plugin != null) {
			throw new SecurityException("This module has already been set.");
		}
		if (plugin == null) {
			return;
		}
		if (!plugin.isEnabled()) {
			return;
		}
		this.plugin = plugin;
		if (plugin instanceof Reloadable) {
			this.reloadableObjects.add((Reloadable) plugin);
		}
	}

	/**
	 * @see Container<T>
	 */
	public void setVersionProvider(VersionProvider versionProvider) {
		this.versionProvider = versionProvider;
	}

	// DEFAULT METHODS
	/**
	 * @see Container<T>
	 */
	@Override
	public void reload() {
		reloadableObjects.forEach(Reloadable::reload);
	}

	/**
	 * @see Container<T>
	 */
	@Override
	public void save() {
		saveableObjects.forEach(Saveable::save);
	}

	/**
	 * @see Container<T>
	 */
	public void softStop() {
		stopped = true;
	}

	/**
	 * @see Container<T>
	 */
	public void stopServer() {
		stopped = true;
		Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer(messages.getMessage("gen.stop-kick-message")));
		Bukkit.getServer().shutdown();
	}

	/**
	 * @see Container<T>
	 */
	public boolean isStopped() {
		return stopped;
	}

}
