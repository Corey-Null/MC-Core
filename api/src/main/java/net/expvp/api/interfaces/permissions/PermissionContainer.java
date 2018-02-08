package net.expvp.api.interfaces.permissions;

import java.util.Set;

import org.bukkit.permissions.Permission;

/**
 * Interface for classes which hold permissions
 * 
 * @author NullUser
 */
public interface PermissionContainer {

	/**
	 * @param permission
	 * @return true if player has permission
	 */
	boolean hasPermission(String permission);

	/**
	 * @param permission
	 * @return true if container has permission
	 */
	boolean hasPermission(Permission permission);

	/**
	 * Sets a desired permission
	 * 
	 * @param perm
	 * @param add
	 */
	void setPermission(String perm, boolean add);

	/**
	 * @return true if account has wildcard
	 */
	boolean hasWildCard();

	/**
	 * Adds wildcard permission
	 */
	void addWildCard();

	/**
	 * Removes wildcard permission
	 */
	void removeWildCard();

	/**
	 * @return the account's permissions
	 */
	Set<String> getPermissions();

	/**
	 * @return the account's evaded permissions
	 */
	Set<String> getNonPermissions();

}
