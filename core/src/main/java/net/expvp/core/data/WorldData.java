package net.expvp.core.data;

import java.util.Map;

import org.bukkit.Location;

import com.google.common.collect.Maps;

import net.expvp.api.interfaces.data.IWorldData;
import net.expvp.core.NullContainer;

/**
 * Class containing all world data
 * 
 * @author NullUser
 * @see IWorldData
 */
public class WorldData implements IWorldData {

	private final NullContainer container;
	private Location spawnLocation;
	private Map<String, Location> warps;

	/**
	 * Initializes the required params
	 * 
	 * @param container
	 * @param spawnLocation
	 */
	public WorldData(NullContainer container) {
		this.container = container;
		this.spawnLocation = null;
		this.warps = Maps.newHashMap();
	}

	/**
	 * @see IWorldData
	 */
	@Override
	public boolean delWarp(String name) {
		if (getWarp(name) != null) {
			return false;
		}
		warps.remove(name);
		return true;
	}

	/**
	 * @see IWorldData
	 */
	@Override
	public Location getWarp(String name) {
		return warps.get(name);
	}

	/**
	 * @see IWorldData
	 */
	@Override
	public Map<String, Location> getWarps() {
		return warps;
	}

	/**
	 * @see IWorldData
	 */
	@Override
	public void initWarps(Map<String, Location> warps) {
		this.warps.putAll(warps);
	}

	/**
	 * @see IWorldData
	 */
	@Override
	public void setWarp(String name, Location loc) {
		if (getWarp(name) != null) {
			warps.replace(name, loc);
			return;
		}
		warps.put(name, loc);
	}

	/**
	 * @see IWorldData
	 */
	@Override
	public Location getSpawnLocation() {
		return spawnLocation;
	}

	/**
	 * @see IWorldData
	 */
	@Override
	public void setSpawnLocation(Location spawnLocation) {
		this.spawnLocation = spawnLocation;
	}

	/**
	 * @see IWorldData
	 */
	@Override
	public void save() {
		container.getWorldDataSavingMethod().processSaveRequest(this);
	}

}
