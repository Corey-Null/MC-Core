package net.expvp.api.interfaces.permissions;

/**
 * Interface used to represent a rank
 * 
 * @author NullUser
 * @see PermissionContainer
 */
public interface IRank extends PermissionContainer, RankInheritor, PrefixSuffixHolder {

	/**
	 * @return the name of the rank
	 */
	String getName();

}
