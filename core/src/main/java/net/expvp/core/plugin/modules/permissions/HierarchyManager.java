package net.expvp.core.plugin.modules.permissions;

import java.util.Collection;
import java.util.Comparator;
import java.util.Set;

import com.google.common.collect.Sets;

import net.expvp.core.plugin.modules.permissions.groups.Rank;

/**
 * Class used to handle rank sorting based on hierarchy
 * 
 * @author NullUser
 */
public final class HierarchyManager {

	private final static Comparator<Rank> RANK_COMPARATOR = new Comparator<Rank>() {
		@Override
		public int compare(Rank r1, Rank r2) {
			if (r1.getLadder() > r2.getLadder()) {
				return -1;
			} else if (r1.getLadder() == r2.getLadder()) {
				return 0;
			} else {
				return 1;
			}
		}
	};

	/**
	 * Checks if a list of ranks has a certain permission
	 * 
	 * @param permission
	 *            To check against
	 * @param ranks
	 *            To check
	 * @return -1 if no perm, 1 if perm, 0 if undecided
	 */
	public static int hasPermission(String permission, Collection<Rank> ranks) {
		Set<Rank> sorted = sortRanks(ranks);
		for (Rank rank : sorted) {
			if (rank.getNonPermissions().contains(permission.toLowerCase())) {
				return -1;
			}
			if (rank.getPermissions().contains(permission.toLowerCase())) {
				return 1;
			}
		}
		return 0;
	}

	/**
	 * Sorts a list of ranks
	 * 
	 * @param ranks
	 *            To sort
	 * @return sorted ranks
	 */
	public static Set<Rank> sortRanks(Collection<Rank> ranks) {
		Set<Rank> nt = Sets.newTreeSet(RANK_COMPARATOR);
		nt.addAll(ranks);
		return nt;
	}

}
