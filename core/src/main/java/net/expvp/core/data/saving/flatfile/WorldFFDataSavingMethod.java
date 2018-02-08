package net.expvp.core.data.saving.flatfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.expvp.api.interfaces.data.DataSaver;
import net.expvp.api.interfaces.data.IDataSavingThread;
import net.expvp.api.interfaces.data.IWorldData;
import net.expvp.api.interfaces.data.WorldDataSavingMethod;
import net.expvp.core.NullContainer;
import net.expvp.core.data.WorldData;

/**
 * FlatFile saving method for World Data
 * 
 * @author NullUser
 * @see WorldDataSavingMethod
 */
public class WorldFFDataSavingMethod implements WorldDataSavingMethod {

	private final NullContainer container;
	private final File file;

	/**
	 * Initializes required fields for saving world data
	 * 
	 * @param container
	 */
	public WorldFFDataSavingMethod(NullContainer container) {
		this.container = container;
		this.file = new File(container.getPlugin().getDataFolder(), "world data.data");
	}

	/**
	 * @see DataSaver
	 */
	@Override
	public String getType() {
		return "Flat-File";
	}

	/**
	 * @see DataSaver
	 */
	@Override
	public IDataSavingThread getThread() {
		return container.getDataSavingThread();
	}

	/**
	 * @see DataSaver
	 */
	@Override
	public void processSaveRequest(IWorldData data) {
		String spawn = locationToString(data.getSpawnLocation());
		Set<String> warps = Sets.newHashSet();
		for (Entry<String, Location> entry : data.getWarps().entrySet()) {
			warps.add(entry.getKey() + ":" + locationToString(entry.getValue()));
		}
		container.getDataSavingThread().processRequest(new Runnable() {
			@Override
			public void run() {
				try {
					if (!file.exists()) {
						file.createNewFile();
					}
					PrintWriter writer = new PrintWriter(new FileWriter(file));
					writer.println(spawn);
					warps.forEach(writer::println);
					writer.print("--");
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("WorldData saving failed, please contact the developer for support.");
				}
			}
		});
	}

	/**
	 * @see WorldDataSavingMethod
	 */
	@Override
	public IWorldData loadData() {
		if (!file.exists()) {
			return new WorldData(container);
		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			List<String> data = Lists.newArrayList();
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.isEmpty()) {
					continue;
				}
				data.add(line);
			}
			int place = 1;
			Map<String, Location> warps = Maps.newHashMap();
			String piece = data.get(place);
			for (; place < data.size() && !(piece = data.get(place)).equals("--"); place++) {
				if (piece.equals("--")) {
					break;
				}
				String[] args = piece.split(":");
				warps.put(args[0], stringToLocation(args[1]));
			}
			place++;
			reader.close();
			IWorldData wd = new WorldData(container);
			wd.setSpawnLocation(stringToLocation(data.get(0)));
			wd.initWarps(warps);
			return wd;
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("WorldData file corrupted, please contact the developer for support.");
			return new WorldData(container);
		}
	}

	/**
	 * @param loc
	 * @return parsed location as string
	 */
	private final String locationToString(Location loc) {
		if (loc == null) {
			return "null";
		}
		StringBuilder builder = new StringBuilder();
		builder.append(loc.getWorld().getUID());
		builder.append(";");
		builder.append(loc.getX());
		builder.append(";");
		builder.append(loc.getY());
		builder.append(";");
		builder.append(loc.getZ());
		builder.append(";");
		builder.append(loc.getYaw());
		builder.append(";");
		builder.append(loc.getPitch());
		return builder.toString();
	}

	/**
	 * @param string
	 * @return parsed string as location
	 */
	private final Location stringToLocation(String string) {
		if (string.equalsIgnoreCase("null")) {
			return null;
		}
		String[] parts = string.split(";");
		return new Location(Bukkit.getWorld(UUID.fromString(parts[0])), Double.parseDouble(parts[1]),
				Double.parseDouble(parts[2]), Double.parseDouble(parts[3]), Float.parseFloat(parts[4]),
				Float.parseFloat(parts[5]));
	}

}
