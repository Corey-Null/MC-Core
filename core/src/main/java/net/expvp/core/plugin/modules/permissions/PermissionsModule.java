package net.expvp.core.plugin.modules.permissions;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import net.expvp.api.PlaceholderAPI;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.core.NullContainer;
import net.expvp.core.plugin.modules.Module;
import net.expvp.core.plugin.modules.permissions.commands.PermissionsCommand;
import net.expvp.core.plugin.modules.permissions.groups.Rank;
import net.expvp.core.plugin.modules.permissions.groups.RankHandler;

/**
 * Module for permissions system
 * 
 * @author NullUser
 * @see Module
 */
public class PermissionsModule extends Module<NullContainer> {

	private final RankHandler rankHandler;
	private final PlayerPermissions playerPermissions;

	public PermissionsModule(NullContainer container) {
		super(container, "permissions", false);
		container.getPlugin().saveResource("permissions/permissions.yml", false);
		this.rankHandler = new RankHandler(this);
		container.getReloadableObjects().add(rankHandler);
		container.getSaveableObjects().add(rankHandler);
		this.playerPermissions = new PlayerPermissions(this);
		container.getReloadableObjects().add(playerPermissions);
		container.getSaveableObjects().add(playerPermissions);
	}

	/**
	 * @see Module
	 */
	@Override
	public void registerPlaceholders() {
		regPlaceholder("effective_prefix", (acc) -> {
			return acc.getPrefix() == null
					? acc.getRank() == null ? "" : acc.getRank().getPrefix() == null ? "" : acc.getRank().getPrefix()
					: acc.getPrefix();
		});
		regPlaceholder("player_prefix", (acc) -> {
			return acc.getPrefix() == null ? "" : acc.getPrefix();
		});
		regPlaceholder("rank_prefix", (acc) -> {
			return acc.getRank() == null ? "" : acc.getRank().getPrefix() == null ? "" : acc.getRank().getPrefix();
		});
		regPlaceholder("inherited_prefixes", (acc) -> {
			Set<Rank> pre = Sets.newHashSet();
			pre.addAll(acc.getInheritedRanks().stream().map(rankHandler::getRank).filter(rank -> rank != null)
					.collect(Collectors.toList()));
			if (acc.getRank() != null) {
				pre.addAll(acc.getRank().getInheritedRanks().stream().map(rankHandler::getRank)
						.filter(rank -> rank != null).collect(Collectors.toList()));
			}
			Set<Rank> sorted = HierarchyManager.sortRanks(pre);
			if (sorted.isEmpty()) {
				return "";
			}
			StringBuilder builder = new StringBuilder();
			sorted.forEach(rank -> {
				if (rank.getPrefix() != null) {
					builder.append(rank.getPrefix());
				}
			});
			return builder.toString();
		});
		regPlaceholder("effective_suffix", (acc) -> {
			return acc.getSuffix() == null
					? acc.getRank() == null ? "" : acc.getRank().getSuffix() == null ? "" : acc.getRank().getSuffix()
					: acc.getSuffix();
		});
		regPlaceholder("player_suffix", (acc) -> {
			return acc.getSuffix() == null ? "" : acc.getSuffix();
		});
		regPlaceholder("rank_suffix", (acc) -> {
			return acc.getRank() == null ? "" : acc.getRank().getSuffix() == null ? "" : acc.getRank().getSuffix();
		});
		regPlaceholder("inherited_suffixes", (acc) -> {
			Set<Rank> pre = Sets.newHashSet();
			pre.addAll(acc.getInheritedRanks().stream().map(rankHandler::getRank).filter(rank -> rank != null)
					.collect(Collectors.toList()));
			if (acc.getRank() != null) {
				pre.addAll(acc.getRank().getInheritedRanks().stream().map(rankHandler::getRank)
						.filter(rank -> rank != null).collect(Collectors.toList()));
			}
			Set<Rank> sorted = HierarchyManager.sortRanks(pre);
			if (sorted.isEmpty()) {
				return "";
			}
			StringBuilder builder = new StringBuilder();
			sorted.forEach(rank -> {
				if (rank.getSuffix() != null) {
					builder.append(rank.getSuffix());
				}
			});
			return builder.toString();
		});
		regPlaceholder("rank_name", (acc) -> {
			return acc.getRank() == null ? "" : acc.getRank().getName();
		});
		regPlaceholder("inherited_names", (acc) -> {
			Set<Rank> pre = Sets.newHashSet();
			pre.addAll(acc.getInheritedRanks().stream().map(rankHandler::getRank).filter(rank -> rank != null)
					.collect(Collectors.toList()));
			if (acc.getRank() != null) {
				pre.addAll(acc.getRank().getInheritedRanks().stream().map(rankHandler::getRank)
						.filter(rank -> rank != null).collect(Collectors.toList()));
			}
			Set<Rank> sorted = HierarchyManager.sortRanks(pre);
			if (sorted.isEmpty()) {
				return "";
			}
			StringBuilder builder = new StringBuilder();
			sorted.forEach(rank -> {
				builder.append(rank.getName());
			});
			return builder.toString();
		});
	}

	/**
	 * Method used to simplify registering placeholders for the perms module
	 * 
	 * @param key
	 * @param fun
	 */
	public void regPlaceholder(String key, Function<OfflinePlayerAccount, String> fun) {
		PlaceholderAPI.registerAccountPlaceholder(key, (player, str) -> fun.apply(player));
	}

	/**
	 * @see Module
	 */
	@Override
	public void registerCommands() {
		getContainer().registerCommand(new PermissionsCommand(this));
	}

	/**
	 * @return the player permissions handler
	 */
	public PlayerPermissions getPlayerPermissions() {
		return playerPermissions;
	}

	/**
	 * @return the rank handler
	 */
	public RankHandler getRankHandler() {
		return rankHandler;
	}

}
