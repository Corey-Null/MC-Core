package net.expvp.api.interfaces.permissions;

import java.util.Set;

/**
 * Interface used to specify classes which can inherit ranks
 * 
 * @author NullUser
 */
public interface RankInheritor {

	/**
	 * @return the inherited ranks
	 */
	Set<String> getInheritedRanks();

	/**
	 * Starts inheriting a specified rank
	 * 
	 * @param rank
	 *            To inherit
	 */
	void inheritRank(String rank);

	/**
	 * Stops inheriting the rank
	 * 
	 * @param rank
	 *            To stop inheriting
	 */
	void uninheritRank(String rank);

	/**
	 * @param rank
	 * @return true if the class inherits the rank
	 */
	boolean inherits(IRank rank);

}
