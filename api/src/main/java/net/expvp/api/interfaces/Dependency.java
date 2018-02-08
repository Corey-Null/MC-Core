package net.expvp.api.interfaces;

/**
 * Used to mark a dependency of the plugin
 * 
 * @author NullUser
 */
public interface Dependency {

	/**
	 * @return the name of the dependency
	 */
	String getName();

	/**
	 * @return the held container
	 */
	Container<?> getContainer();

}
