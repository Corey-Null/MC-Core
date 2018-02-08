package net.expvp.core.plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.expvp.api.PlaceholderAPI;
import net.expvp.api.ReflectionUtil;
import net.expvp.api.TextUtil;
import net.expvp.api.bukkit.chat.Chat;
import net.expvp.api.interfaces.Reloadable;
import net.expvp.api.interfaces.data.IDataSavingThread;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.api.interfaces.plugin.IModule;
import net.expvp.api.interfaces.version.VersionProvider;
import net.expvp.core.Messages;
import net.expvp.core.NMSCBUtil;
import net.expvp.core.NullContainer;
import net.expvp.core.commands.general.CommandBack;
import net.expvp.core.commands.general.CommandFeed;
import net.expvp.core.commands.general.CommandFly;
import net.expvp.core.commands.general.CommandGod;
import net.expvp.core.commands.general.CommandHeal;
import net.expvp.core.commands.general.CommandInvsee;
import net.expvp.core.commands.general.CommandJellylegs;
import net.expvp.core.commands.general.CommandNightVision;
import net.expvp.core.commands.general.CommandRules;
import net.expvp.core.commands.general.CommandSetSpawn;
import net.expvp.core.commands.general.CommandSpawn;
import net.expvp.core.commands.general.FakeCommand;
import net.expvp.core.commands.general.admin.CommandBroadcast;
import net.expvp.core.commands.general.admin.CommandSudo;
import net.expvp.core.commands.general.admin.CommandVanish;
import net.expvp.core.commands.general.gamemode.CommandGamemode;
import net.expvp.core.commands.general.gamemode.CommandGamemodeAdventure;
import net.expvp.core.commands.general.gamemode.CommandGamemodeCreative;
import net.expvp.core.commands.general.gamemode.CommandGamemodeSpectator;
import net.expvp.core.commands.general.gamemode.CommandGamemodeSurvival;
import net.expvp.core.commands.general.ip.CommandCheckAlts;
import net.expvp.core.commands.general.ip.CommandCheckIP;
import net.expvp.core.commands.general.management.CommandNullReload;
import net.expvp.core.commands.general.management.CommandSave;
import net.expvp.core.commands.general.management.CommandStop;
import net.expvp.core.commands.general.message.CommandMessage;
import net.expvp.core.commands.general.message.CommandReply;
import net.expvp.core.commands.general.tp.CommandTP;
import net.expvp.core.commands.general.tp.CommandTPA;
import net.expvp.core.commands.general.tp.CommandTPAHere;
import net.expvp.core.commands.general.tp.CommandTPAccept;
import net.expvp.core.commands.general.tp.CommandTPAll;
import net.expvp.core.commands.general.tp.CommandTPDeny;
import net.expvp.core.commands.general.tp.CommandTPHere;
import net.expvp.core.commands.general.tp.CommandTPPos;
import net.expvp.core.commands.general.warp.CommandDelWarp;
import net.expvp.core.commands.general.warp.CommandSetWarp;
import net.expvp.core.commands.general.warp.CommandWarp;
import net.expvp.core.commands.general.worldmod.CommandBigTree;
import net.expvp.core.commands.general.worldmod.CommandTree;
import net.expvp.core.data.saving.DataSavingThread;
import net.expvp.core.data.saving.flatfile.PlayerFFDataSavingMethod;
import net.expvp.core.data.saving.flatfile.WorldFFDataSavingMethod;
import net.expvp.core.listeners.ChatListener;
import net.expvp.core.listeners.CommandPreProcessListener;
import net.expvp.core.listeners.DamageListener;
import net.expvp.core.listeners.PlayerDeathListener;
import net.expvp.core.listeners.PlayerJoinQuitListener;
import net.expvp.core.listeners.ServerPingListener;
import net.expvp.core.logger.ChatLogger;
import net.expvp.core.logger.CommandLogger;
import net.expvp.core.player.PlayerDataHandler;
import net.expvp.core.player.format.PlayerFormatController;
import net.expvp.core.plugin.modules.economy.EconomyModule;
import net.expvp.core.plugin.modules.permissions.PermissionsModule;
import net.expvp.core.plugin.modules.serveraccess.ServerAccessModule;
import net.expvp.core.sign.SignDispose;
import net.expvp.core.teleport.TeleportRequestHandler;
import net.expvp.core.vanish.VanishManager;
import net.expvp.nms.v1_10_R1.V1_10_R1Provider;
import net.expvp.nms.v1_11_R1.V1_11_R1Provider;
import net.expvp.nms.v1_12_R1.V1_12_R1Provider;
import net.expvp.nms.v1_8_R1.V1_8_R1Provider;
import net.expvp.nms.v1_8_R2.V1_8_R2Provider;
import net.expvp.nms.v1_8_R3.V1_8_R3Provider;
import net.expvp.nms.v1_9_R1.V1_9_R1Provider;
import net.expvp.nms.v1_9_R2.V1_9_R2Provider;

/**
 * Main plugin for the NullCore
 * 
 * @author NullUser
 * @see JavaPlugin
 * @see Reloadable
 */
public class CorePlugin extends JavaPlugin implements Reloadable {

	private boolean foundVersion;
	private final NullContainer container;
	private String playerListFormat;

	/**
	 * Initializer to setup the container
	 */
	public CorePlugin() {
		this.container = new NullContainer();
	}

	/**
	 * @see JavaPlugin
	 */
	@Override
	public void onEnable() {
		saveResource("info.txt", true);
		container.setPlugin(this);
		if (!(foundVersion = loadVersion())) {
			System.out.println("<!----                   ----!>");
			System.out.println("Fatal error while trying to load NullCore.");
			System.out.println("Your version may not be available in the current version of this plugin.");
			System.out.println("To fix this, please keep up to date with the plugin & your server.");
			System.out.println("If your server is up to date then your version will be implemented soon.");
			System.out.println("<!----                   ----!>");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		saveDefaultConfig();
		reloadConfig();
		Chat.initContainer(container);
		container.setCommandLogger(new CommandLogger(container));
		container.setChatLogger(new ChatLogger(container));
		container.setMessages(new Messages(container, new File(getDataFolder(), "messages.yml")));
		container.setDataSavingThread(new DataSavingThread("nc-datasaving"));
		container.setPlayerDataSavingMethod(new PlayerFFDataSavingMethod(container));
		container.setPlayerDataHandler(new PlayerDataHandler(container, 20 * 60));
		container.setWorldDataSavingMethod(new WorldFFDataSavingMethod(container));
		container.setWorldData(container.getWorldDataSavingMethod().loadData());
		container.setTeleportRequestHandler(new TeleportRequestHandler(container));
		VanishManager vanishManager = new VanishManager(container);
		vanishManager.loadOptions(getConfig().getConfigurationSection("vanish"));
		container.setVanishManager(vanishManager);
		container.setPlayerFormatController(new PlayerFormatController(container));
		registerPlaceholders();
		loadListeners();
		loadCommands();
		registerModules();
		loadSigns();
		this.playerListFormat = getConfig().getString("player-list-format", "{player_default}");
		new BukkitRunnable() {
			@Override
			public void run() {
				Bukkit.getOnlinePlayers().forEach((player) -> {
					OfflinePlayerAccount acc = container.getPlayerDataHandler().getAccount(player.getUniqueId());
					if (acc == null) {
						return;
					}
					player.setPlayerListName(TextUtil.color(PlaceholderAPI.pass(playerListFormat, acc)));
				});
			}
		}.runTaskTimer(this, 40, 40);
	}

	/**
	 * @return the playerlist format
	 */
	public String getPlayerListFormat() {
		return playerListFormat;
	}

	/**
	 * @param name
	 * @return true if a dependency is present
	 */
	public boolean checkDependency(String name) {
		Plugin plugin = Bukkit.getPluginManager().getPlugin(name);
		return plugin != null && plugin.isEnabled();
	}

	/**
	 * Registers all placeholders
	 */
	public void registerPlaceholders() {
		PlaceholderAPI.registerPlayerPlaceholder("ping", (player, string) -> {
			return String.valueOf(NMSCBUtil.getPing(player));
		});
		PlaceholderAPI.registerPlayerPlaceholder("player", (player, string) -> {
			return player.getDisplayName();
		});
		PlaceholderAPI.registerPlayerPlaceholder("player_default", (player, string) -> {
			return player.getName();
		});
		saveResource("placeholders.yml", false);
	}

	/**
	 * Loads the version into the container
	 */
	public boolean loadVersion() {
		String v = ReflectionUtil.VERSION;
		switch (v.toUpperCase()) {
		case "V1_8_R1":
			container.setVersionProvider(new V1_8_R1Provider());
			return true;
		case "V1_8_R2":
			container.setVersionProvider(new V1_8_R2Provider());
			return true;
		case "V1_8_R3":
			container.setVersionProvider(new V1_8_R3Provider());
			return true;
		case "V1_9_R1":
			container.setVersionProvider(new V1_9_R1Provider());
			return true;
		case "V1_9_R2":
			container.setVersionProvider(new V1_9_R2Provider());
			return true;
		case "V1_10_R1":
			container.setVersionProvider(new V1_10_R1Provider());
			return true;
		case "V1_11_R1":
			container.setVersionProvider(new V1_11_R1Provider());
			return true;
		case "V1_12_R1":
			container.setVersionProvider(new V1_12_R1Provider());
			return true;
		default:
			try {
				Class<?> version = Class.forName("net.expvp.nms." + v + "." + v.toUpperCase() + "Provider");
				container.setVersionProvider((VersionProvider) version.newInstance());
				return true;
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {

				return false;
			}
		}
	}

	/**
	 * Loads all custom signs
	 */
	public void loadSigns() {
		new SignDispose(container);
	}

	/**
	 * Registers all modules connected with the Core Plugin
	 */
	public void registerModules() {
		PermissionsModule pMod = null;
		boolean pModEnabled = getConfig().getBoolean("enabled-modules.permissions", true);
		if (pModEnabled) {
			System.out.println("Loading Permissions Module...");
			container.registerModule(pMod = new PermissionsModule(container));
		}
		if (getConfig().getBoolean("enabled-modules.banmanager", true)) {
			System.out.println("Loading BanManager Module...");
			container.registerModule(new ServerAccessModule(container));
		}
		System.out.println("Loading accounts...");
		container.getPlayerDataHandler().loadAccounts();
		if (pModEnabled) {
			pMod.getPlayerPermissions().loadPermissions();
		}
		if (getConfig().getBoolean("enabled-modules.economy", true)) {
			System.out.println("Loading Economy Module...");
			container.registerModule(new EconomyModule(container));
		}
	}

	/**
	 * Registers all commands connected with the Core Plugin
	 */
	public void loadCommands() {
		container.registerCommand(new FakeCommand(container));

		container.registerCommand(new CommandGamemode(container));
		container.registerCommand(new CommandGamemodeAdventure(container));
		container.registerCommand(new CommandGamemodeCreative(container));
		container.registerCommand(new CommandGamemodeSpectator(container));
		container.registerCommand(new CommandGamemodeSurvival(container));
		container.registerCommand(new CommandCheckAlts(container));
		container.registerCommand(new CommandCheckIP(container));
		container.registerCommand(new CommandMessage(container));
		container.registerCommand(new CommandReply(container));
		container.registerCommand(new CommandTP(container));
		container.registerCommand(new CommandTPA(container));
		container.registerCommand(new CommandTPAccept(container));
		container.registerCommand(new CommandTPAHere(container));
		container.registerCommand(new CommandTPAll(container));
		container.registerCommand(new CommandTPDeny(container));
		container.registerCommand(new CommandTPHere(container));
		container.registerCommand(new CommandTPPos(container));
		container.registerCommand(new CommandDelWarp(container));
		container.registerCommand(new CommandSetWarp(container));
		container.registerCommand(new CommandWarp(container));
		container.registerCommand(new CommandBigTree(container));
		container.registerCommand(new CommandTree(container));
		container.registerCommand(new CommandBack(container));
		container.registerCommand(new CommandBroadcast(container));
		container.registerCommand(new CommandFeed(container));
		container.registerCommand(new CommandFly(container));
		container.registerCommand(new CommandGod(container));
		container.registerCommand(new CommandHeal(container));
		container.registerCommand(new CommandInvsee(container));
		container.registerCommand(new CommandJellylegs(container));
		container.registerCommand(new CommandNightVision(container));
		container.registerCommand(new CommandNullReload(container));
		container.registerCommand(new CommandRules(container));
		container.registerCommand(new CommandSave(container));
		container.registerCommand(new CommandSetSpawn(container));
		container.registerCommand(new CommandSpawn(container));
		container.registerCommand(new CommandStop(container));
		container.registerCommand(new CommandSudo(container));
		container.registerCommand(new CommandVanish(container));
	}

	/**
	 * Registers all listeners connected with the Core Plugin
	 */
	public void loadListeners() {
		PluginManager manager = getServer().getPluginManager();
		manager.registerEvents(new PlayerJoinQuitListener(container), this);
		manager.registerEvents(new DamageListener(container), this);
		manager.registerEvents(new PlayerDeathListener(container), this);
		manager.registerEvents(new CommandPreProcessListener(), this);
		manager.registerEvents(new ServerPingListener(container), this);
		manager.registerEvents(new ChatListener(container), this);
	}

	/**
	 * @see JavaPlugin
	 */
	@Override
	public void onDisable() {
		if (!foundVersion) {
			return;
		}
		if (!container.isStopped()) {
			container.softStop();
		}
		Bukkit.getScheduler().cancelTasks(this);
		container.save();
		IDataSavingThread thread = container.getDataSavingThread();
		thread.shutdown();
		while (thread.isFinishing()) {
			continue;
		}
		if (thread instanceof Thread) {
			((Thread) thread).interrupt();
		}
	}

	/**
	 * @see Reloadable
	 */
	@Override
	public void reload() {
		saveDefaultConfig();
		reloadConfig();
		PlaceholderAPI.clean();
		registerPlaceholders();
		this.playerListFormat = getConfig().getString("player-list-format", "{player_default}");
		container.getModules().forEach(IModule::registerPlaceholders);
		container.getVanishManager().loadOptions(getConfig().getConfigurationSection("vanish"));
	}

	/**
	 * @see CommandExecutor
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		getLogger().warning("Command " + label + " not connected to a supplier.");
		return true;
	}

	/**
	 * @see JavaPlugin
	 */
	@Override
	public void saveResource(String resourcePath, boolean replace) {
		if (resourcePath == null || resourcePath.equals("")) {
			throw new IllegalArgumentException("ResourcePath cannot be null or empty");
		}

		resourcePath = resourcePath.replace('\\', '/');
		InputStream in = getResource(resourcePath);
		if (in == null) {
			throw new IllegalArgumentException(
					"The embedded resource '" + resourcePath + "' cannot be found in " + getFile());
		}

		File outFile = new File(getDataFolder(), resourcePath);
		int lastIndex = resourcePath.lastIndexOf('/');
		File outDir = new File(getDataFolder(), resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));

		if (!outDir.exists()) {
			outDir.mkdirs();
		}

		try {
			if (!outFile.exists() || replace) {
				OutputStream out = new FileOutputStream(outFile);
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.close();
				in.close();
			} else {
			}
		} catch (IOException ex) {
			getLogger().log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, ex);
		}
	}

}
