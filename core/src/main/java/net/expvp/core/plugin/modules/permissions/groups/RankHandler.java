package net.expvp.core.plugin.modules.permissions.groups;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.expvp.api.Pair;
import net.expvp.api.interfaces.Reloadable;
import net.expvp.api.interfaces.Saveable;
import net.expvp.core.NullContainer;
import net.expvp.core.plugin.modules.permissions.PermissionsModule;

/**
 * Class used for handling ranks
 * 
 * @author NullUser
 */
public class RankHandler implements Reloadable, Saveable {

	private final NullContainer container;
	private final PermissionsModule module;
	private final Map<String, Rank> cachedRanks;
	private Rank defaultRank;
	private final File file;
	private FileConfiguration config;

	public RankHandler(PermissionsModule module) {
		this.container = module.getContainer();
		this.module = module;
		this.cachedRanks = Maps.newHashMap();
		this.file = new File(module.getDataFolder(), "groups.yml");
		container.getPlugin().saveResource(module.getName() + "/groups.yml", false);
		config = YamlConfiguration.loadConfiguration(file);
		loadCache();
	}

	/**
	 * @return unmodifiable version of cached ranks
	 */
	public Collection<Rank> getCachedRanks() {
		return Collections.unmodifiableCollection(cachedRanks.values());
	}

	/**
	 * @return the config
	 */
	public FileConfiguration getConfig() {
		return config;
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
	 * Deletes the specified rank
	 * 
	 * @param rank
	 *            To delete
	 */
	public void deleteRank(Rank rank) {
		cachedRanks.remove(rank.getName().toLowerCase());
		config.set(rank.getName(), null);
		container.getPlayerDataHandler().getCachedAccounts().forEach(account -> {
			if (account.getInheritedRanks().contains(rank)) {
				account.unInheritRank(rank);
			}
			if (account.getRank() != null && account.getRank().equals(rank)) {
				if (defaultRank != null) {
					account.setRank(defaultRank);
				} else {
					account.setRank(null);
				}
			}
		});
	}

	/**
	 * @see Reloadable
	 */
	@Override
	public void reload() {
		loadCache();
	}

	/**
	 * @return the default rank
	 */
	public Rank getDefaultRank() {
		return defaultRank;
	}

	/**
	 * Loads the ranks from the permissions.yml file and checks for illegal
	 * ranks in accounts
	 */
	public void loadCache() {
		defaultRank = null;
		cachedRanks.clear();
		container.getPlugin().saveResource(module.getName() + "/groups.yml", false);
		config = YamlConfiguration.loadConfiguration(file);
		for (String key : config.getKeys(false)) {
			if (config.isConfigurationSection(key)) {
				Rank rank = loadRankFromConfig(key, config.getConfigurationSection(key));
				if (rank == null) {
					System.out.println("Failed to load rank " + key);
					continue;
				}
			}
		}
		container.getPlayerDataHandler().getCachedAccounts().forEach(account -> {
			Set<String> ranks = Sets.newHashSet();
			Rank newRank;
			for (String inherited : account.getInheritedRanks()) {
				Rank rank = getRank(inherited);
				if (rank != null) {
					ranks.add(inherited);
				}
			}
			account.getInheritedRanks().clear();
			account.getInheritedRanks().addAll(ranks);
			if ((newRank = getRank(account.getRank().getName())) == null) {
				if (defaultRank != null) {
					account.setRank(defaultRank);
				} else {
					account.setRank(null);
				}
			} else {
				account.setRank(newRank);
			}
		});
	}

	/**
	 * Loads a rank from the specified config section
	 * 
	 * @param key
	 *            To identify the rank
	 * @param section
	 *            To load the rank from
	 * @return the loaded rank
	 */
	public Rank loadRankFromConfig(String key, ConfigurationSection section) {
		if (key == null || section == null) {
			return null;
		}
		List<String> permissions = section.getStringList("permissions");
		if (permissions == null) {
			permissions = Lists.newArrayList();
		}
		Set<String> perms = Sets.newHashSet();
		boolean wildcard = false;
		for (String string : permissions) {
			if (string.equals("*")) {
				wildcard = true;
				continue;
			}
			if (string.equals("-*")) {
				wildcard = false;
				continue;
			}
			perms.add(string);
		}
		int ladder = section.getInt("ladder", 1);
		Pair<Rank, Boolean> result = createRank(key, perms, ladder);
		if (!result.getRight()) {
			return null;
		}
		Rank rank = result.getLeft();
		if (wildcard) {
			rank.addWildCard();
		}
		List<String> inherited = section.getStringList("inherited-groups");
		if (inherited != null) {
			rank.addInheritedRanks(inherited);
		}
		String prefix = section.getString("prefix");
		if (prefix != null) {
			rank.setPrefix(prefix);
		}
		String suffix = section.getString("suffix");
		if (suffix != null) {
			rank.setSuffix(suffix);
		}
		if (section.getBoolean("default", false)) {
			defaultRank = rank;
		}
		return rank;
	}

	/**
	 * @param name
	 *            To identify the rank
	 * @param permissions
	 *            To add to the rank
	 * @return the set rank and if it was successfully added to the cached ranks
	 */
	public Pair<Rank, Boolean> createRank(String name, Set<String> permissions, int ladder) {
		Rank rank = new Rank(container, this, name, permissions, ladder);
		if (cachedRanks.containsKey(name.toLowerCase())) {
			return Pair.of(rank, false);
		}
		cachedRanks.put(name.toLowerCase(), rank);
		return Pair.of(rank, true);
	}

	/**
	 * @return the container
	 */
	public NullContainer getContainer() {
		return container;
	}

	/**
	 * @return the module
	 */
	public PermissionsModule getModule() {
		return module;
	}

	/**
	 * @param name
	 *            To base the rank off of
	 * @return rank based off the name
	 */
	public Rank getRank(String name) {
		if (name == null) {
			return null;
		}
		return cachedRanks.get(name.toLowerCase());
	}

}
