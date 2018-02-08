package net.expvp.core.plugin.modules.serveraccess;

import org.bukkit.Bukkit;

import net.expvp.api.TextUtil;
import net.expvp.api.interfaces.Saveable;
import net.expvp.core.NullContainer;
import net.expvp.core.plugin.modules.Module;
import net.expvp.core.plugin.modules.serveraccess.commands.CommandBan;
import net.expvp.core.plugin.modules.serveraccess.commands.CommandBanIP;
import net.expvp.core.plugin.modules.serveraccess.commands.CommandBanlist;
import net.expvp.core.plugin.modules.serveraccess.commands.CommandHistory;
import net.expvp.core.plugin.modules.serveraccess.commands.CommandKick;
import net.expvp.core.plugin.modules.serveraccess.commands.CommandKickAll;
import net.expvp.core.plugin.modules.serveraccess.commands.CommandTempBan;
import net.expvp.core.plugin.modules.serveraccess.commands.CommandUnban;
import net.expvp.core.plugin.modules.serveraccess.commands.CommandUnbanIP;
import net.expvp.core.plugin.modules.serveraccess.data.BanDataManager;
import net.expvp.core.plugin.modules.serveraccess.data.saving.BanDataSavingMethod;
import net.expvp.core.plugin.modules.serveraccess.data.saving.BanFFDataSavingMethod;

/**
 * Module for the ban manager
 * 
 * @author NullUser
 * @see Module<T>
 * @see Saveable
 */
public class ServerAccessModule extends Module<NullContainer> implements Saveable {

	private final BanDataManager manager;
	private final BanDataSavingMethod saving;
	private String banMessage;
	private String tempBanMessage;
	private String kickMessage;
	private String banipMessage;
	private String defaultBanReason;
	private String defaultKickReason;

	public ServerAccessModule(NullContainer container) {
		super(container, "banmanager", true);
		load();
		this.manager = new BanDataManager();
		this.saving = new BanFFDataSavingMethod(this);
		manager.initBans(saving.loadBanData());
	}

	@Override
	public void registerCommands() {
		getContainer().registerCommand(new CommandBan(this));
		getContainer().registerCommand(new CommandBanIP(this));
		getContainer().registerCommand(new CommandBanlist(this));
		getContainer().registerCommand(new CommandHistory(this));
		getContainer().registerCommand(new CommandKick(this));
		getContainer().registerCommand(new CommandKickAll(this));
		getContainer().registerCommand(new CommandTempBan(this));
		getContainer().registerCommand(new CommandUnban(this));
		getContainer().registerCommand(new CommandUnbanIP(this));
	}

	@Override
	public void registerListeners() {
		Bukkit.getPluginManager().registerEvents(new LoginListener(this), getContainer().getPlugin());
	}

	/**
	 * @see Saveable
	 */
	@Override
	public void save() {
		saving.processSaveRequest(manager);
	}

	/**
	 * @see Module<T>
	 */
	@Override
	public void reload() {
		super.reload();
		load();
	}

	/**
	 * Loads the default messages
	 */
	public void load() {
		this.banMessage = TextUtil.color(getConfig().getString("ban-message",
				"&cYou have been banned from this server.\n&cBanned by: %banner% For: %reason%"));
		this.tempBanMessage = TextUtil.color(getConfig().getString("temp-ban-message",
				"&cYou have been temporarily banned for: %time%\n&cBanned by: %banner% For: %reason%"));
		this.kickMessage = TextUtil.color(getConfig().getString("kick-message",
				"&aYou have been kicked by {0}.\n&cKicked by: %kicker% For: %reason%"));
		this.banipMessage = TextUtil.color(getConfig().getString("ban-ip-message",
				"&cYour IP address has been banned from this server.\n&cBanned by: %banner% For: %reason%"));
		this.defaultBanReason = TextUtil
				.color(getConfig().getString("default-ban-reason", "Breaking the server rules."));
		this.defaultKickReason = TextUtil.color(getConfig().getString("default-kick-reason", "Disrupting the server."));
	}

	/**
	 * @return the banMessage
	 */
	public String getBanMessage() {
		return banMessage;
	}

	/**
	 * @return the tempBanMessage
	 */
	public String getTempBanMessage() {
		return tempBanMessage;
	}

	/**
	 * @return the kickMessage
	 */
	public String getKickMessage() {
		return kickMessage;
	}

	/**
	 * @return the banipMessage
	 */
	public String getBanipMessage() {
		return banipMessage;
	}

	/**
	 * @return the defaultBanReason
	 */
	public String getDefaultBanReason() {
		return defaultBanReason;
	}

	/**
	 * @return the defaultKickReason
	 */
	public String getDefaultKickReason() {
		return defaultKickReason;
	}

	/**
	 * @return the manager
	 */
	public BanDataManager getManager() {
		return manager;
	}

}
