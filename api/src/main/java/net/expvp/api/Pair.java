package net.expvp.api;

/**
 * Class used to pair 2 objects
 * 
 * @author NullUser
 *
 * @param <K>
 *            First object
 * @param <V>
 *            Second object
 */
public final class Pair<K, V> {

	private final V right;
	private final K left;

	private Pair() {
		throw new SecurityException("This class cannot be initialized without params.");
	}

	/**
	 * @param right
	 *            First object
	 * @param left
	 *            Second object
	 */
	private Pair(K left, V right) {
		this.right = right;
		this.left = left;
	}

	/**
	 * @return first object
	 */
	public K getLeft() {
		return left;
	}

	/**
	 * @return second object
	 */
	public V getRight() {
		return right;
	}

	/**
	 * Constructs a pair of objects
	 * 
	 * @param right
	 * @param left
	 * @return the constructed Pair
	 */
	public static <K, V> Pair<K, V> of(K left, V right) {
		return new Pair<K, V>(left, right);
	}

}
