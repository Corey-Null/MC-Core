package net.expvp.api.interfaces.permissions;

/**
 * Interface used to represent objects which can hold prefixes and suffixes
 * 
 * @author NullUser
 */
public interface PrefixSuffixHolder {

	/**
	 * @return the prefix of the rank
	 */
	String getPrefix();

	/**
	 * @return the suffix of the rank
	 */
	String getSuffix();

	/**
	 * @param prefix
	 *            To set the prefix
	 */
	void setPrefix(String prefix);

	/**
	 * @param suffix
	 *            To set the suffix
	 */
	void setSuffix(String suffix);

}
