package net.expvp.core.plugin.modules.permissions.permissible;

import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;

import net.expvp.api.interfaces.player.OnlinePlayerAccount;

/**
 * Class to handle account permissions
 * 
 * @author NullUser
 * @see PermissibleBase
 */
public class AccountPermissibleBase extends PermissibleBase {

	private final OnlinePlayerAccount account;

	public AccountPermissibleBase(OnlinePlayerAccount account) {
		super(account.getPlayer());
		this.account = account;
		account.getPermissions().forEach(this::addPermission0);
	}

	/**
	 * Used to forcefully add a permission
	 * 
	 * @param name
	 *            To add
	 */
	private final void addPermission0(String name) {
		addAttachment(account.getContainer().getPlugin(), name, true);
	}

	/**
	 * @see PermissibleBase
	 */
	@Override
	public boolean hasPermission(String perm) {
		if (account.getPermissions().contains(perm.toLowerCase())) {
			return true;
		}
		if (account.getNonPermissions().contains(perm.toLowerCase())) {
			return false;
		}
		if (account.getRank() != null) {
			if (!account.getPermissions().contains(perm.toLowerCase())
					&& account.getRank().getNonPermissions().contains(perm.toLowerCase())) {
				return false;
			}
			return super.hasPermission(perm) || account.hasWildCard() || account.getRank().hasPermission(perm);
		}
		return super.hasPermission(perm) || account.hasWildCard();
	}

	/**
	 * @see PermissibleBase
	 */
	@Override
	public boolean hasPermission(Permission perm) {
		if (account.getNonPermissions().contains(perm.getName().toLowerCase())) {
			return false;
		}
		if (account.getRank() != null) {
			if (!account.getPermissions().contains(perm.getName().toLowerCase())
					&& account.getRank().getNonPermissions().contains(perm.getName().toLowerCase())) {
				return false;
			}
			return super.hasPermission(perm) || account.hasWildCard() || account.getRank().hasPermission(perm);
		}
		return super.hasPermission(perm) || account.hasWildCard();
	}

}
