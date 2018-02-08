package net.expvp.core.plugin.modules.serveraccess.data;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.expvp.api.Pair;
import net.expvp.api.interfaces.ip.IAddress;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.core.ip.IPAddress;

/**
 * Class used to manager the ban data
 * 
 * @author NullUser
 */
public class BanDataManager {

	public final static BanDataComparator COMPARATOR = new BanDataComparator();
	private final Map<UUID, Set<BanData<UUID>>> uuidBanHistory;
	private final Map<IAddress, Set<BanData<IAddress>>> ipBanHistory;
	private final Map<UUID, BanData<UUID>> activeUUIDBans;
	private final Map<IAddress, BanData<IAddress>> activeIPBans;

	public BanDataManager() {
		this.uuidBanHistory = Maps.newHashMap();
		this.ipBanHistory = Maps.newHashMap();
		this.activeUUIDBans = Maps.newHashMap();
		this.activeIPBans = Maps.newHashMap();
	}

	/**
	 * Kicks the given UUID
	 * 
	 * @param id
	 *            To kick
	 */
	public void addKick(UUID id, BanData<UUID> data) {
		addHistory(id, data);
	}

	/**
	 * Kicks the given IP address
	 * 
	 * @param address
	 *            To kick
	 */
	public void addKick(IPAddress address, BanData<IAddress> data) {
		addHistory(address, data);
	}

	/**
	 * Unbans a specified address
	 * 
	 * @param address
	 * @return if the address was initially banned
	 */
	public boolean unban(IAddress address) {
		if (!activeIPBans.containsKey(address)) {
			return false;
		}
		BanData<IAddress> data = activeIPBans.get(address);
		data.setExpired(true);
		addHistory(address, data);
		activeIPBans.remove(address);
		return true;
	}

	/**
	 * Unbans a specified id
	 * 
	 * @param id
	 * @return if the id was initially banned
	 */
	public boolean unban(UUID id) {
		if (!activeUUIDBans.containsKey(id)) {
			return false;
		}
		BanData<UUID> data = activeUUIDBans.get(id);
		data.setExpired(true);
		addHistory(id, data);
		activeUUIDBans.remove(id);
		return true;
	}

	/**
	 * Bans a specified ID
	 * 
	 * @param id
	 *            ID of the user
	 * @param data
	 *            Data for the ban
	 * @return true if the ban went through
	 */
	public boolean ban(UUID id, BanData<UUID> data) {
		if (data.getType() == BanType.TEMP_BAN && data.getExpire() < System.currentTimeMillis()) {
			data.setExpired(true);
			addHistory(id, data);
			return true;
		}
		if (activeUUIDBans.containsKey(id)) {
			if (activeUUIDBans.get(id).getType() == BanType.TEMP_BAN) {
				data.setExpired(true);
				addHistory(id, activeUUIDBans.get(id));
				activeUUIDBans.replace(id, data);
				return true;
			}
			return false;
		}
		activeUUIDBans.put(id, data);
		return true;
	}

	/**
	 * Bans a specified IP
	 * 
	 * @param address
	 *            IP to ban
	 * @param data
	 *            Data for the ban
	 * @return true if the ban went through
	 */
	public boolean ban(IAddress address, BanData<IAddress> data) {
		if (data.getType() == BanType.TEMP_BAN && data.getExpire() < System.currentTimeMillis()) {
			data.setExpired(true);
			addHistory(address, data);
			return true;
		}
		if (activeIPBans.containsKey(address)) {
			if (activeIPBans.get(address).getType() == BanType.TEMP_BAN) {
				data.setExpired(true);
				addHistory(address, activeIPBans.get(address));
				activeIPBans.replace(address, data);
				return true;
			}
			return false;
		}
		activeIPBans.put(address, data);
		return true;
	}

	/**
	 * Initializes the ban data
	 * 
	 * @param banData
	 *            To parse
	 */
	public void initBans(Map<String, Set<BanData<?>>> banData) {
		for (Entry<String, Set<BanData<?>>> entry : banData.entrySet()) {
			String info = entry.getKey();
			for (BanData<?> parsable : entry.getValue()) {
				try {
					UUID id = UUID.fromString(info);
					UUIDBanData data = (UUIDBanData) parsable;
					if (!data.isExpired()) {
						if (data.getType() == BanType.TEMP_BAN && data.getExpire() < System.currentTimeMillis()) {
							data.setExpired(true);
							addHistory(id, data);
							continue;
						}
						activeUUIDBans.put(id, data);
					} else {
						addHistory(id, data);
					}
				} catch (IllegalArgumentException ex) {
					IPAddress address = new IPAddress(info);
					IPBanData data = (IPBanData) parsable;
					if (!data.isExpired()) {
						if (data.getType() == BanType.TEMP_BAN && data.getExpire() < System.currentTimeMillis()) {
							data.setExpired(true);
							addHistory(address, data);
							continue;
						}
						activeIPBans.put(address, data);
					} else {
						addHistory(address, data);
					}
				}
			}
		}
	}

	/**
	 * @return the active ip bans
	 */
	public Map<IAddress, BanData<IAddress>> getActiveIPBans() {
		return Collections.unmodifiableMap(activeIPBans);
	}

	/**
	 * @return the active uuid bans
	 */
	public Map<UUID, BanData<UUID>> getActiveUUIDBans() {
		return Collections.unmodifiableMap(activeUUIDBans);
	}

	/**
	 * @return the ip address ban history
	 */
	public Map<IAddress, Set<BanData<IAddress>>> getIpBanHistory() {
		return Collections.unmodifiableMap(ipBanHistory);
	}

	/**
	 * @return the uuid ban history
	 */
	public Map<UUID, Set<BanData<UUID>>> getUuidBanHistory() {
		return Collections.unmodifiableMap(uuidBanHistory);
	}

	/**
	 * @param account
	 * @return the ban data and true if the user has an active ban or IP ban
	 */
	public Pair<BanData<?>, Boolean> isUserBanned(OfflinePlayerAccount account) {
		if (activeUUIDBans.containsKey(account.getUUID())) {
			BanData<UUID> banData = activeUUIDBans.get(account.getUUID());
			if (banData.getType() == BanType.TEMP_BAN) {
				if (banData.getExpire() < System.currentTimeMillis()) {
					activeUUIDBans.remove(account.getUUID());
					banData.setExpired(true);
					addHistory(banData.getUser(), banData);
				} else {
					return Pair.of(banData, true);
				}
			} else {
				return Pair.of(banData, true);
			}
		}
		return checkIPBan(account);
	}

	/**
	 * @param account
	 * @return the ban data and true if the user has an active ip ban
	 */
	public Pair<BanData<?>, Boolean> checkIPBan(OfflinePlayerAccount account) {
		IAddress addr = account.getCurrentIPAddress();
		Set<IAddress> past = account.getPastIPAddresses();
		for (Entry<IAddress, BanData<IAddress>> entry : activeIPBans.entrySet()) {
			IAddress address = entry.getKey();
			BanData<IAddress> data = entry.getValue();
			if (data.getType() == BanType.TEMP_BAN) {
				if (data.getExpire() < System.currentTimeMillis()) {
					activeIPBans.remove(address);
					data.setExpired(true);
					addHistory(data.getUser(), data);
					continue;
				}
			}
			if (addr.equals(address) || past.stream().filter(address::equals).findAny().isPresent()) {
				return Pair.of(data, true);
			}
		}
		return Pair.of(null, false);
	}

	/**
	 * Adds history to the ID
	 * 
	 * @param id
	 *            To add to
	 * @param data
	 *            To add
	 */
	public void addHistory(UUID id, BanData<UUID> data) {
		Set<BanData<UUID>> history = uuidBanHistory.get(id);
		if (history == null) {
			history = Sets.newTreeSet(COMPARATOR);
			history.add(data);
			uuidBanHistory.put(id, history);
		} else {
			history.add(data);
		}
	}

	/**
	 * Adds history to the IP
	 * 
	 * @param address
	 *            To add to
	 * @param data
	 *            To add
	 */
	public void addHistory(IAddress address, BanData<IAddress> data) {
		Set<BanData<IAddress>> history = ipBanHistory.get(address);
		if (history == null) {
			history = Sets.newTreeSet(COMPARATOR);
			history.add(data);
			ipBanHistory.put(address, history);
		} else {
			history.add(data);
		}
	}

}
