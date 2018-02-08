package net.expvp.core.plugin.modules.permissions.groups;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.permissions.Permission;

import com.google.common.collect.Sets;

import net.expvp.api.interfaces.permissions.IRank;
import net.expvp.api.interfaces.permissions.PermissionContainer;
import net.expvp.core.NullContainer;
import net.expvp.core.plugin.modules.permissions.HierarchyManager;
import net.expvp.core.plugin.modules.permissions.permissible.RankPermissibleBase;

/**
 * Used to hold rank information
 * 
 * @author NullUser
 * @see PermissionContainer
 */
public class Rank implements IRank {

	private final NullContainer container;
	private boolean wildcard;
	private final RankPermissibleBase base;
	private final Set<String> permissions;
	private final String name;
	private String prefix;
	private String suffix;
	private final Set<String> inheritedRanks;
	private final RankHandler handler;
	private final int ladder;

	public Rank(NullContainer container, RankHandler handler, String name, Set<String> permissions, int ladder) {
		this.container = container;
		this.wildcard = false;
		this.name = name;
		this.permissions = permissions;
		base = new RankPermissibleBase(container, this);
		this.prefix = "";
		this.suffix = "";
		this.inheritedRanks = Sets.newHashSet();
		this.handler = handler;
		this.ladder = ladder;
	}

	/**
	 * @return the ladder
	 */
	public int getLadder() {
		return ladder;
	}

	/**
	 * @return the unmodifiable version of inherited ranks
	 */
	public Set<String> getInheritedRanks() {
		return Collections.unmodifiableSet(inheritedRanks);
	}

	/**
	 * @param rank
	 * @return true if inherits rank
	 */
	public boolean inheritsRank(Rank rank) {
		return inheritedRanks.stream().filter(r -> r.equalsIgnoreCase(rank.getName())).findAny().isPresent();
	}

	/**
	 * @param ranks
	 *            To add to inherited ranks
	 */
	public void addInheritedRanks(Collection<String> ranks) {
		inheritedRanks.addAll(ranks);
	}

	/**
	 * @param rank
	 *            To add to inherited ranks
	 */
	public void addInheritedRank(String rank) {
		inheritedRanks.add(rank);
	}

	/**
	 * @see PermissionContainer
	 */
	@Override
	public Set<String> getNonPermissions() {
		return base.getNonPermissions();
	}

	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @return the suffix
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * @param prefix
	 *            To set the prefix
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @param suffix
	 *            To set the suffix
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	/**
	 * @return the name of the rank
	 */
	public String getName() {
		return name;
	}

	/**
	 * @see PermissionContainer
	 */
	@Override
	public Set<String> getPermissions() {
		return permissions;
	}

	/**
	 * @see PermissionContainer
	 */
	@Override
	public boolean hasPermission(String permission) {
		int result = HierarchyManager.hasPermission(permission.toLowerCase(),
				inheritedRanks.stream().map(r -> handler.getRank(r)).collect(Collectors.toList()));
		if (result == -1) {
			return false || base.hasPermission(permission);
		} else if (result == 1) {
			return true && !base.getNonPermissions().contains(permission.toLowerCase());
		}
		return base.hasPermission(permission);
	}

	/**
	 * @see PermissionContainer
	 */
	@Override
	public boolean hasPermission(Permission permission) {
		int result = HierarchyManager.hasPermission(permission.getName(),
				inheritedRanks.stream().map(r -> handler.getRank(r)).collect(Collectors.toList()));
		if (result == -1) {
			return false || base.hasPermission(permission);
		} else if (result == 1) {
			return true && !base.getNonPermissions().contains(permission.getName().toString());
		}
		return base.hasPermission(permission);
	}

	/**
	 * @see PermissionContainer
	 */
	@Override
	public void addWildCard() {
		wildcard = true;
	}

	/**
	 * @see PermissionContainer
	 */
	@Override
	public boolean hasWildCard() {
		return wildcard;
	}

	/**
	 * @see PermissionContainer
	 */
	@Override
	public void removeWildCard() {
		wildcard = false;
	}

	/**
	 * @see PermissionContainer
	 */
	@Override
	public void setPermission(String perm, boolean add) {
		base.addNonPermission(perm.substring(1));
		permissions.add(add ? perm.toLowerCase() : '-' + perm.toLowerCase());
		base.addAttachment(container.getPlugin(), perm, add);
	}

	@Override
	public void inheritRank(String rank) {
		inheritedRanks.add(rank);
	}

	@Override
	public void uninheritRank(String rank) {
		inheritedRanks.remove(rank);
	}

	@Override
	public boolean inherits(IRank rank) {
		return inheritedRanks.contains(rank.getName());
	}

}
