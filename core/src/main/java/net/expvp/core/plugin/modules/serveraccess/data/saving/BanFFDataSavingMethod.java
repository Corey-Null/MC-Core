package net.expvp.core.plugin.modules.serveraccess.data.saving;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.expvp.api.interfaces.data.IDataSavingThread;
import net.expvp.core.NullContainer;
import net.expvp.core.ip.IPAddress;
import net.expvp.core.plugin.modules.serveraccess.ServerAccessModule;
import net.expvp.core.plugin.modules.serveraccess.data.BanData;
import net.expvp.core.plugin.modules.serveraccess.data.BanDataManager;
import net.expvp.core.plugin.modules.serveraccess.data.BanType;
import net.expvp.core.plugin.modules.serveraccess.data.IPBanData;
import net.expvp.core.plugin.modules.serveraccess.data.UUIDBanData;

/**
 * Flatfile implementation for the ban data saving
 * 
 * @author NullUser
 * @see BanDataSavingMethod
 */
public class BanFFDataSavingMethod implements BanDataSavingMethod {

	private final String quote = Pattern.quote("§:");
	private final NullContainer container;
	private final File file;

	public BanFFDataSavingMethod(ServerAccessModule module) {
		this.container = module.getContainer();
		this.file = new File(module.getDataFolder(), "bans.data");
	}

	/**
	 * @see BanDataSavingMethod
	 */
	@Override
	public String getType() {
		return "flat-file";
	}

	/**
	 * @see BanDataSavingMethod
	 */
	@Override
	public IDataSavingThread getThread() {
		return container.getDataSavingThread();
	}

	/**
	 * @see BanDataSavingMethod
	 */
	@Override
	public void processSaveRequest(BanDataManager object) {
		Set<String> toWrite = Sets.newHashSet();
		object.getActiveIPBans().forEach((addr, data) -> {
			toWrite.add(parse(data) + addr.getAddress());
		});
		object.getActiveUUIDBans().forEach((uuid, data) -> {
			toWrite.add(parse(data) + uuid.toString());
		});
		object.getIpBanHistory().forEach((addr, dataSet) -> {
			toWrite.addAll(dataSet.stream().map(data -> parse(data) + addr.getAddress()).collect(Collectors.toSet()));
		});
		object.getUuidBanHistory().forEach((uuid, dataSet) -> {
			toWrite.addAll(dataSet.stream().map(data -> parse(data) + uuid.toString()).collect(Collectors.toSet()));
		});
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			PrintWriter writer = new PrintWriter(file);
			toWrite.forEach(writer::println);
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @param data
	 * @return the parsed data
	 */
	private final String parse(BanData<?> data) {
		StringBuilder builder = new StringBuilder();
		builder.append(data.getType().getName()).append('§').append(':');
		builder.append(data.getBegin()).append('§').append(':');
		builder.append(data.getExpire()).append('§').append(':');
		builder.append(data.isExpired()).append('§').append(':');
		builder.append(data.getHander()).append('§').append(':');
		builder.append(data.getReason()).append('§').append(':');
		return builder.toString();
	}

	/**
	 * @see BanDataSavingMethod
	 */
	@Override
	public Map<String, Set<BanData<?>>> loadBanData() {
		Map<String, Set<BanData<?>>> map = Maps.newHashMap();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(quote);
				String it = parts[6];
				BanData<?> data;
				if (IPAddress.isIP(it)) {
					data = new IPBanData(new IPAddress(it), BanType.get(parts[0]));
				} else {
					data = new UUIDBanData(UUID.fromString(it), BanType.get(parts[0]));
				}
				data.setBegin(Long.parseLong(parts[1]));
				data.setExpire(Long.parseLong(parts[2]));
				data.setExpired(Boolean.parseBoolean(parts[3]));
				data.setHander(UUID.fromString(parts[4]));
				data.setReason(parts[5]);
				if (map.containsKey(it)) {
					map.get(it).add(data);
				} else {
					Set<BanData<?>> set = Sets.newTreeSet(BanDataManager.COMPARATOR);
					set.add(data);
					map.put(it, set);
				}
			}
		} catch (FileNotFoundException e) {
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}

}
