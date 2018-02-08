package net.expvp.core.plugin.modules.permissions;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.expvp.api.interfaces.Container;
import net.expvp.api.interfaces.Reloadable;
import net.expvp.api.interfaces.Saveable;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.core.plugin.modules.permissions.groups.Rank;

/**
 * Class used to handle player permissions
 * 
 * @author NullUser
 * @see Reloadable
 * @see Saveable
 */
public class PlayerPermissions implements Reloadable, Saveable {

	private final PermissionsModule module;
	private final Container<?> container;
	private final File file;
	private FileConfiguration config;

	public PlayerPermissions(PermissionsModule module) {
		this.module = module;
		this.container = module.getContainer();
		this.file = new File(module.getDataFolder(), "permissions.yml");
		container.getPlugin().saveResource(module.getName() + "/permissions.yml", false);
		config = YamlConfiguration.loadConfiguration(file);
	}

	/**
	 * Adds a permission to the account
	 * 
	 * @param account
	 *            To add permission to
	 * @param permission
	 *            The permission to add to the account
	 */
	public void addPermission(OfflinePlayerAccount account, String permission) {
		handlePermission(account, permission, true);
	}

	/**
	 * Removes a permission from the account
	 * 
	 * @param account
	 *            To remove permission from
	 * @param permission
	 *            The permission to remove from the account
	 */
	public void removePermission(OfflinePlayerAccount account, String permission) {
		handlePermission(account, permission, false);
	}

	/**
	 * Handles all permission changing
	 * 
	 * @param account
	 *            To handle when dealing with the permission
	 * @param permission
	 *            The permission to add or remove
	 * @param add
	 *            To define if the permission should be added or removed
	 */
	private final void handlePermission(OfflinePlayerAccount account, String permission, boolean add) {
		UUID id = account.getUUID();
		String sID = id.toString();
		if (permission.equals("*")) {
			if (add) {
				account.addWildCard();
			} else {
				account.removeWildCard();
				add = false;
			}
		} else if (permission.equals("-*")) {
			account.removeWildCard();
		} else {
			account.setPermission(permission, add);
		}
		if (config.contains(sID)) {
			String name = config.getString(sID + ".name");
			if (!name.equals(account.getName())) {
				config.set(sID + ".name", account.getName());
			}
			List<String> perms = config.getStringList(sID + ".perms");
			if (add) {
				perms.add(permission);
			} else {
				perms.remove(permission);
			}
			config.set(sID + ".perms", perms);
		} else {
			if (!add) {
				return;
			}
			ConfigurationSection section = config.createSection(sID);
			section.set("name", account.getName());
			List<String> perms = Lists.newArrayList(permission);
			section.set("perms", perms);
			config.set(sID, section);
		}
	}

	/**
	 * Sets the prefix of account
	 * 
	 * @param account
	 *            To get the account to set the prefix to
	 * @param prefix
	 *            To set the prefix of the account to
	 */
	public void setPrefix(OfflinePlayerAccount account, String prefix) {
		UUID id = account.getUUID();
		String sID = id.toString();
		if (config.contains(sID)) {
			ConfigurationSection section = config.getConfigurationSection(sID);
			if (section.contains("prefix") && prefix.isEmpty()) {
				section.set("prefix", null);
				account.setPrefix(null);
				return;
			}
			section.set("prefix", prefix);
			account.setPrefix(prefix);
		} else {
			if (prefix.isEmpty()) {
				account.setPrefix(null);
				return;
			}
			ConfigurationSection section = config.createSection(sID);
			section.set("name", account.getName());
			section.set("perms", Lists.newArrayList());
			section.set("prefix", prefix);
			config.set(sID, section);
			account.setPrefix(prefix);
		}
	}

	/**
	 * Sets the suffix of account
	 * 
	 * @param account
	 *            To get the account to set the suffix to
	 * @param suffix
	 *            To set the suffix of the account to
	 */
	public void setSuffix(OfflinePlayerAccount account, String suffix) {
		UUID id = account.getUUID();
		String sID = id.toString();
		if (config.contains(sID)) {
			ConfigurationSection section = config.getConfigurationSection(sID);
			if (section.contains("suffix") && suffix.isEmpty()) {
				section.set("suffix", null);
				account.setSuffix(null);
				return;
			}
			section.set("suffix", suffix);
			account.setSuffix(suffix);
		} else {
			if (suffix.isEmpty()) {
				account.setSuffix(null);
				return;
			}
			ConfigurationSection section = config.createSection(sID);
			section.set("name", account.getName());
			section.set("perms", Lists.newArrayList());
			section.set("suffix", suffix);
			config.set(sID, section);
			account.setSuffix(suffix);
		}
	}

	/**
	 * Adds or removed inheritence from a group
	 * 
	 * @param account
	 *            To inherit the rank
	 * @param rank
	 *            To inherit
	 * @param inherit
	 *            Specifies whether to add or remove inheritence
	 */
	public void handleRank(OfflinePlayerAccount account, Rank rank, boolean inherit) {
		UUID id = account.getUUID();
		String sID = id.toString();
		if (config.contains(sID)) {
			List<String> inherited = config.getStringList(sID + ".inherited");
			if (inherit) {
				account.inheritRank(rank);
				inherited.add(rank.getName());
			} else {
				account.unInheritRank(rank);
				inherited.remove(rank.getName());
			}
			config.set(sID + ".inherited", inherited);
		} else {
			if (!inherit) {
				return;
			}
			ConfigurationSection section = config.createSection(sID);
			section.set("name", account.getName());
			section.set("perms", Lists.newArrayList());
			section.set("inherited", Lists.newArrayList(rank.getName()));
			config.set(sID, section);
			account.inheritRank(rank);
		}
	}

	/**
	 * Loads permissions from the config
	 */
	public void loadPermissions() {
		for (String key : config.getKeys(false)) {
			if (!config.isConfigurationSection(key)) {
				continue;
			}
			UUID id;
			try {
				id = UUID.fromString(key);
			} catch (IllegalArgumentException ex) {
				System.out.println("Error 1: Failed to load permissions from %id%: " + key);
				continue;
			}
			List<?> questionableList = config.getList(key + ".perms");
			OfflinePlayerAccount account = container.getPlayerDataHandler().getAccount(id);
			if (account == null) {
				System.out.println("Error 2: Failed to load permissions from %id%: " + key);
				continue;
			}
			Set<String> permissions = Sets.newHashSet();
			for (Object obj : questionableList) {
				permissions.add(String.valueOf(obj));
			}
			account.initPerms(permissions);
			account.setPrefix(config.getString(key + ".prefix", null));
			account.setSuffix(config.getString(key + ".suffix", null));
			account.getInheritedRanks().clear();
			List<?> inheritedRanks = config.getList(key + ".inherited");
			if (inheritedRanks != null && !inheritedRanks.isEmpty()) {
				for (Object obj : inheritedRanks) {
					Rank rank = module.getRankHandler().getRank(String.valueOf(obj));
					if (rank != null) {
						account.inheritRank(rank);
					}
				}
			}
		}
	}

	/**
	 * @see Saveable
	 */
	@Override
	public void save() {
		FileConfiguration temp = new YamlConfiguration();
		for (String key : config.getKeys(false)) {
			temp.set(key, config.get(key));
		}
		container.getDataSavingThread().processRequest(() -> {
			try {
				temp.save(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * @see Reloadable
	 */
	@Override
	public void reload() {
		loadPermissions();
	}

}
