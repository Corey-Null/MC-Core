package net.expvp.api.interfaces.data;

import java.util.Map;

import org.bukkit.Location;

import net.expvp.api.interfaces.Saveable;

/**
 * Interface for classes which can hold world data
 * 
 * @author NullUser
 * @see Saveable
 */
public interface IWorldData extends Saveable {

	/**
	 * @return the location
	 */
	Location getSpawnLocation();

	/**
	 * @param loc
	 *            To set the location
	 */
	void setSpawnLocation(Location loc);

	/**
	 * @param name
	 * @return the specified warp location
	 */
	Location getWarp(String name);

	/**
	 * Sets a warp to a specified location
	 * 
	 * @param name
	 *            To set the warp name
	 * @param loc
	 *            To set the warp location
	 */
	void setWarp(String name, Location loc);

	/**
	 * Deletes the specified warp
	 * 
	 * @param name
	 *            To set the warp's name
	 */
	boolean delWarp(String name);

	/**
	 * @return the warps
	 */
	Map<String, Location> getWarps();

	/**
	 * Initializes the warps
	 */
	void initWarps(Map<String, Location> warps);

}
