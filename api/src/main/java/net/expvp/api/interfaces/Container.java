package net.expvp.api.interfaces;

import java.util.Set;

import org.bukkit.plugin.java.JavaPlugin;

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

/**
 * Interface for classes which can contain a java plugin
 * 
 * @author NullUser
 *
 * @param <T>
 *            To define the plugin
 * @see Saveable
 * @see Reloadable
 */
public interface Container<T extends JavaPlugin> extends Saveable, Reloadable {

	// REGISTERING
	/**
	 * @param command
	 *            To register the bukkit command set
	 */
	void registerCommand(ICommand command);

	/**
	 * Adds a module to the saving, reloading pattern and registers its commands
	 * and listeners
	 * 
	 * @param module
	 *            To add to the modules
	 */
	void registerModule(IModule<? extends Container<?>> module);

	/**
	 * Removes a defined saveable object from a specific class
	 * 
	 * @param clazz
	 */
	void removeSaveable(Class<? extends Saveable> clazz);

	/**
	 * Removes a defined reloadable object from a specific class
	 * 
	 * @param clazz
	 */
	void removeReloadable(Class<? extends Reloadable> clazz);

	// GETTERS
	/**
	 * @return the commands
	 */
	Set<ICommand> getCommands();

	/**
	 * @return the modules
	 */
	Set<IModule<?>> getModules();

	/**
	 * @return the playerFormatController
	 */
	IPlayerFormatController getPlayerFormatController();

	/**
	 * @return the chatLogger
	 */
	ILogger getChatLogger();

	/**
	 * @return the commandLogger
	 */
	ILogger getCommandLogger();

	/**
	 * @return the vanishManager
	 */
	IVanishManager getVanishManager();

	/**
	 * @return the teleportRequestHandler
	 */
	ITeleportRequestHandler getTeleportRequestHandler();

	/**
	 * @param moduleName
	 * @return true if the module is enabled
	 */
	boolean isModuleEnabled(String moduleName);

	/**
	 * @param name
	 *            To specify the module
	 * @return module specified by name
	 */
	IModule<? extends Container<?>> getModule(String name);

	/**
	 * @return the Messages
	 */
	IMessages getMessages();

	/**
	 * @return the WorldData
	 */
	IWorldData getWorldData();

	/**
	 * @return the WorldDataSavingMethod
	 */
	WorldDataSavingMethod getWorldDataSavingMethod();

	/**
	 * @return the PlayerDataSavingMethod
	 */
	PlayerDataSavingMethod getPlayerDataSavingMethod();

	/**
	 * @return the DataSavingThread
	 */
	IDataSavingThread getDataSavingThread();

	/**
	 * @return the PlayerDataHandler
	 */
	IPlayerDataHandler getPlayerDataHandler();

	/**
	 * @return the plugin
	 */
	JavaPlugin getPlugin();

	/**
	 * @return the VersionProvider
	 */
	VersionProvider getVersionProvider();

	/**
	 * @return the ReloadableObjects
	 */
	Set<Reloadable> getReloadableObjects();

	/**
	 * @return the SaveableObjects
	 */
	Set<Saveable> getSaveableObjects();

	// SETTERS
	/**
	 * @param playerFormatController
	 *            To set the playerFormatController
	 */
	void setPlayerFormatController(IPlayerFormatController playerFormatController);

	/**
	 * @param chatLogger
	 *            To set the chatLogger
	 */
	void setChatLogger(ILogger chatLogger);

	/**
	 * @param commandLogger
	 *            To set the commandLogger
	 */
	void setCommandLogger(ILogger commandLogger);

	/**
	 * @param vanishManager
	 *            To set the vanishManagerf
	 */
	void setVanishManager(IVanishManager vanishManager);

	/**
	 * @param teleportRequestHandler
	 *            To set the teleportRequestHandler
	 */
	void setTeleportRequestHandler(ITeleportRequestHandler teleportRequestHandler);

	/**
	 * @param messages
	 *            To set the messages
	 */
	void setMessages(IMessages messages);

	/**
	 * @param worldData
	 *            To set the worldData
	 */
	void setWorldData(IWorldData worldData);

	/**
	 * @param worldDataSavingMethod
	 *            To set the worldDataSavingMethod
	 */
	void setWorldDataSavingMethod(WorldDataSavingMethod worldDataSavingMethod);

	/**
	 * @param playerDataSavingMethod
	 *            To set the playerDataSavingMethod
	 */
	void setPlayerDataSavingMethod(PlayerDataSavingMethod playerDataSavingMethod);

	/**
	 * @param dataSavingThread
	 *            To set the dataSavingThread
	 */
	void setDataSavingThread(IDataSavingThread dataSavingThread);

	/**
	 * @param playerDataHandler
	 *            To set the playerDataHandler
	 */
	void setPlayerDataHandler(IPlayerDataHandler playerDataHandler);

	/**
	 * @param plugin
	 *            To set the plugin
	 */
	void setPlugin(JavaPlugin plugin);

	/**
	 * @param versionProvider
	 *            To set the versionProvider
	 */
	void setVersionProvider(VersionProvider versionProvider);

	/**
	 * Stops the container from being enabled for thread running and other
	 * features
	 */
	void softStop();

	/**
	 * Safely stops the server
	 */
	void stopServer();

	/**
	 * @return stopped defining if the server is stopped
	 */
	boolean isStopped();

}
