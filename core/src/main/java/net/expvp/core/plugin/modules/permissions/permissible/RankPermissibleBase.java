package net.expvp.core.plugin.modules.permissions.permissible;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import net.expvp.core.NullContainer;
import net.expvp.core.plugin.modules.permissions.groups.Rank;

/**
 * Class to handle Rank permissions
 * 
 * @author NullUser
 * @see PermissibleBase
 */
public class RankPermissibleBase extends PermissibleBase {

	private final JavaPlugin plugin;
	private final Rank rank;
	private final Set<String> nonPermissions;

	public RankPermissibleBase(NullContainer container, Rank rank) {
		super(null);
		this.plugin = container.getPlugin();
		rank.getPermissions().forEach(this::addPermission0);
		this.rank = rank;
		this.nonPermissions = new HashSet<>();
	}

	/**
	 * Used to forcefully add a permission
	 * 
	 * @param name
	 */
	private final void addPermission0(String name) {
		if (name.startsWith("-")) {
			nonPermissions.add(name.substring(1).toLowerCase());
			return;
		}
		addAttachment(plugin, name, true);
	}

	/**
	 * Adds a permission to be evaded
	 * 
	 * @param nonPermission
	 */
	public void addNonPermission(String nonPermission) {
		nonPermissions.add(nonPermission.toLowerCase());
	}

	/**
	 * @return the permissions which are not allowed for the rank
	 */
	public Set<String> getNonPermissions() {
		return nonPermissions;
	}

	/**
	 * @see PermissibleBase
	 */
	@Override
	public boolean hasPermission(String inName) {
		if (rank.getPermissions().contains(inName.toLowerCase())) {
			return true;
		}
		if (nonPermissions.contains(inName.toLowerCase())) {
			return false;
		}
		return super.hasPermission(inName) || rank.hasWildCard();
	}

	/**
	 * @see PermissibleBase
	 */
	@Override
	public boolean hasPermission(Permission perm) {
		if (nonPermissions.contains(perm.getName().toLowerCase())) {
			return false;
		}
		return super.hasPermission(perm) || rank.hasWildCard();
	}

}
