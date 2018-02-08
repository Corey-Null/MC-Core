package net.expvp.core.ip;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.Sets;

import net.expvp.api.interfaces.ip.IAddress;
import net.expvp.api.interfaces.ip.IPContainer;

/**
 * Class used for matching IP addresses
 * 
 * @author NullUser
 * @param <T>
 */
public class IPMatcher<T extends IPContainer> {

	/**
	 * @return matched IP addresses
	 */
	public Set<T> match(T object, Collection<T> toMatch) {
		Set<T> returning = Sets.newHashSet();
		Iterator<T> iterator = toMatch.iterator();
		while (iterator.hasNext()) {
			T next = iterator.next();
			if (next.equals(object)) {
				continue;
			}
			if (object.getCurrentIPAddress().equals(next.getCurrentIPAddress())) {
				returning.add(object);
			} else if (isMatch(object.getCurrentIPAddress(), next.getPastIPAddresses())) {
				returning.add(object);
			} else if (isMatch(next.getCurrentIPAddress(), object.getPastIPAddresses())) {
				returning.add(object);
			} else if (matchSets(object, next)) {
				returning.add(object);
			}
		}
		return returning;
	}

	/**
	 * @param container
	 * @param container2
	 * @return true if the container matches any IP addresses from the second
	 *         container
	 */
	private final boolean matchSets(IPContainer container, IPContainer container2) {
		for (IAddress address : container.getPastIPAddresses()) {
			if (address.equals(container2.getCurrentIPAddress())) {
				return true;
			}
			if (isMatch(address, container2.getPastIPAddresses())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param address
	 * @param addresses
	 * @return true if any of the addresses match the address
	 */
	private final boolean isMatch(IAddress address, Set<IAddress> addresses) {
		return addresses.stream().filter(add -> add.equals(address)).findAny().isPresent();
	}

}
