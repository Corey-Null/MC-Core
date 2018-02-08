package net.expvp.core.player;

import java.net.InetAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import com.google.common.collect.Sets;
import com.google.common.util.concurrent.AtomicDouble;

import net.expvp.api.Pair;
import net.expvp.api.bukkit.events.account.AccountBalanceChangeEvent;
import net.expvp.api.bukkit.events.account.AccountMessageEvent;
import net.expvp.api.bukkit.events.account.AccountSudoEvent;
import net.expvp.api.bukkit.exceptions.PlayerOfflineException;
import net.expvp.api.enums.BalanceChangeReason;
import net.expvp.api.interfaces.Container;
import net.expvp.api.interfaces.Saveable;
import net.expvp.api.interfaces.ip.IAddress;
import net.expvp.api.interfaces.permissions.IRank;
import net.expvp.api.interfaces.permissions.PermissionContainer;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.api.interfaces.plugin.IModule;
import net.expvp.core.NMSCBUtil;
import net.expvp.core.NullContainer;
import net.expvp.core.ip.IPAddress;
import net.expvp.core.plugin.modules.permissions.PermissionsModule;
import net.expvp.core.plugin.modules.permissions.permissible.AccountPermissibleBase;

/**
 * Class for containing all PlayerData
 * 
 * @author NullUser
 * @see OnlinePlayerAccount
 * @see Bannable
 */
public class PlayerAccount implements OnlinePlayerAccount {

	private final Container<?> container;
	private final UUID id;
	private boolean dirty;
	private volatile AtomicDouble balance;
	private final Set<UUID> ignoredPlayers;
	private String name;
	private long lastDisconnectTime;
	private final Set<String> permissions;
	private final Set<String> nonPermissions;
	private IRank rank;
	private boolean wildcard;
	private String prefix;
	private String suffix;
	private final Set<String> inheritedRanks;
	private IAddress address;
	private final Set<IAddress> pastAddresses;

	private long lastLoginTime = -1;

	private AccountPermissibleBase permissible = null;
	private Player player = null;
	private OfflinePlayerAccount replyTo = null;
	private Location lastLocation = null;
	private boolean god = false;

	/**
	 * Intializer for new player
	 * 
	 * @param container
	 * @param player
	 */
	public PlayerAccount(NullContainer container, Player player, InetAddress address) {
		this(container, player.getUniqueId(), 0, Sets.newConcurrentHashSet(), player.getName(), -1);
		login(player, address);
		save();
	}

	/**
	 * Initializer for existing player
	 * 
	 * @param container
	 * @param id
	 * @param balance
	 * @param ignoredPlayers
	 * @param name
	 * @param lastDisconnect
	 */
	public PlayerAccount(Container<?> container, UUID id, double balance, Set<UUID> ignoredPlayers, String name,
			long lastDisconnect) {
		this.container = container;
		this.id = id;
		this.dirty = false;
		this.balance = new AtomicDouble(balance);
		this.ignoredPlayers = ignoredPlayers;
		this.name = name;
		this.lastDisconnectTime = lastDisconnect;
		this.wildcard = false;
		this.rank = null;
		this.nonPermissions = Sets.newHashSet();
		this.permissions = Sets.newHashSet();
		this.prefix = null;
		this.suffix = null;
		this.inheritedRanks = Sets.newHashSet();
		this.pastAddresses = Sets.newHashSet();
	}

	/**
	 * @param dirty
	 *            To set dirty
	 */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	// Offline Methods

	/**
	 * @see Saveable
	 */
	@Override
	public void save() {
		if (dirty) {
			container.getPlayerDataSavingMethod().processSaveRequest(this);
			dirty = false;
		}
	}

	/**
	 * Used to set the balance along with the event call
	 * 
	 * @param newBalance
	 *            Player's new balance
	 * @param reason
	 *            For changing balance
	 */
	private final void setBalance0(double newBalance, BalanceChangeReason reason) {
		AccountBalanceChangeEvent event = new AccountBalanceChangeEvent(this, newBalance, reason);
		if (event.isCancelled()) {
			return;
		}
		this.balance.set(event.getNewBalance());
		this.dirty = true;
	}

	/**
	 * @see OnlinePlayerAccount
	 */
	@Override
	public void setBalance(double newBalance) {
		if (newBalance < 0) {
			newBalance = 0;
		}
		if (newBalance > Double.MAX_VALUE) {
			newBalance = Double.MAX_VALUE;
		}
		setBalance0(newBalance, BalanceChangeReason.SET);
	}

	/**
	 * @see OnlinePlayerAccount
	 */
	@Override
	public void addBalance(double toAdd) {
		if (toAdd < 0) {
			toAdd = -toAdd;
		}
		double current = balance.get();
		double newBal = 0;
		try {
			newBal = current + toAdd;
		} catch (ArithmeticException ex) {
			newBal = Double.MAX_VALUE;
		}
		if (newBal < 0) {
			newBal = 0;
		}
		setBalance0(newBal, BalanceChangeReason.ADD);
	}

	/**
	 * @see OnlinePlayerAccount
	 */
	@Override
	public double removeBalance(double toRemove) {
		if (toRemove < 0) {
			toRemove = -toRemove;
		}
		double current = balance.get();
		double newBal = 0;
		try {
			newBal = current - toRemove;
		} catch (ArithmeticException ex) {
			newBal = 0;
		}
		if (newBal < 0) {
			newBal = 0;
		}
		setBalance0(newBal, BalanceChangeReason.REMOVE);
		return current - newBal;
	}

	/**
	 * @see OnlinePlayerAccount
	 */
	@Override
	public double getBalance() {
		return balance.get();
	}

	/**
	 * @see OnlinePlayerAccount
	 */
	@Override
	public long getLastLogoutTime() {
		return lastDisconnectTime;
	}

	/**
	 * @see OnlinePlayerAccount
	 */
	@Override
	public void login(Player player, InetAddress socketAddress) {
		if (!id.equals(player.getUniqueId())) {
			throw new SecurityException("ID mismatch, this player cannot be linked with this account.");
		}
		this.player = player;
		if (this.name != player.getName()) {
			this.name = player.getName();
			this.dirty = true;
		}
		this.lastLoginTime = System.currentTimeMillis();
		String ip = socketAddress.getHostAddress();
		if (this.address == null || !this.address.equals(ip)) {
			IAddress past = null;
			for (IAddress address : pastAddresses) {
				if (address.equals(ip)) {
					past = address;
					break;
				}
			}
			if (address != null) {
				this.pastAddresses.add(address);
			}
			if (past != null) {
				pastAddresses.remove(past);
				address = past;
			} else {
				address = new IPAddress(ip);
			}
			dirty = true;
		}
		attachPermissions();
	}

	/**
	 * @see OnlinePlayerAccount
	 */
	@Override
	public boolean isOnline() {
		return player != null;
	}

	/**
	 * @see OnlinePlayerAccount
	 */
	@Override
	public OnlinePlayerAccount getOnlineAccount() {
		if (isOnline()) {
			return this;
		}
		return null;
	}

	/**
	 * @see OnlinePlayerAccount
	 */
	@Override
	public Set<UUID> getIgnoredPlayers() {
		return ignoredPlayers;
	}

	/**
	 * @see OnlinePlayerAccount
	 */
	@Override
	public boolean isIgnoring(UUID id) {
		return ignoredPlayers.contains(id);
	}

	/**
	 * @see OnlinePlayerAccount
	 */
	@Override
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * @see OnlinePlayerAccount
	 */
	@Override
	public UUID getUUID() {
		return id;
	}

	/**
	 * @see OnlinePlayerAccount
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @see PermissionContainer
	 */
	@Override
	public void addWildCard() {
		if (!wildcard) {
			dirty = true;
		}
		this.wildcard = true;
	}

	/**
	 * @see PermissionContainer
	 */
	@Override
	public void removeWildCard() {
		if (wildcard) {
			dirty = true;
		}
		this.wildcard = false;
	}

	/**
	 * @see PermissionContainer
	 */
	@Override
	public boolean hasWildCard() {
		return wildcard;
	}

	/**
	 * @see PermissionContainer
	 */
	@Override
	public boolean hasPermission(Permission permission) {
		if (nonPermissions.contains(permission.getName().toLowerCase())) {
			return false;
		}
		IModule<?> pMod = container.getModule("permissions");
		if (pMod instanceof PermissionsModule && inheritedRanks.stream().map(str -> {
			PermissionsModule mod = (PermissionsModule) pMod;
			return mod.getRankHandler().getRank(str);
		}).filter(rank -> rank.hasPermission(permission)).findAny().isPresent()) {
			return true;
		}
		if (isOnline()) {
			return permissible.hasPermission(permission);
		}
		return false;
	}

	/**
	 * @see PermissionContainer
	 */
	@Override
	public boolean hasPermission(String permission) {
		if (nonPermissions.contains(permission.toLowerCase())) {
			return false;
		}
		IModule<?> pMod = container.getModule("permissions");
		if (pMod instanceof PermissionsModule && inheritedRanks.stream().map(str -> {
			PermissionsModule mod = (PermissionsModule) pMod;
			return mod.getRankHandler().getRank(str);
		}).filter(rank -> rank.hasPermission(permission)).findAny().isPresent()) {
			return true;
		}
		if (isOnline()) {
			return permissible.hasPermission(permission);
		}
		return permissions.contains(permission);
	}

	/**
	 * @see PermissionContainer
	 */
	@Override
	public void setPermission(String perm, boolean add) {
		if (add) {
			if (perm.startsWith("-")) {
				nonPermissions.add(perm.substring(1));
				add = false;
			} else {
				permissions.add(perm);
			}
		} else {
			if (perm.startsWith("-")) {
				nonPermissions.remove(perm.substring(1));
				return;
			} else {
				permissions.remove(perm);
			}
		}
		if (isOnline()) {
			permissible.addAttachment(container.getPlugin(), perm, add);
		}
	}

	/**
	 * @see PermissionContainer
	 */
	@Override
	public Set<String> getNonPermissions() {
		return nonPermissions;
	}

	/**
	 * @see OfflinePlayerAccount
	 */
	@Override
	public void setRank(IRank rank) {
		if (!(this.rank == null && rank != null) && ((this.rank == null && rank == null) || this.rank.equals(rank))) {
			return;
		}
		this.rank = rank;
		this.dirty = true;
	}

	/**
	 * @see OfflinePlayerAccount
	 */
	@Override
	public IRank getRank() {
		return rank;
	}

	/**
	 * @see PermissionContainer
	 */
	@Override
	public Set<String> getPermissions() {
		return permissions;
	}

	/**
	 * @see OfflinePlayerAccount
	 */
	public Container<?> getContainer() {
		return container;
	}

	/**
	 * @see OfflinePlayerAccount
	 */
	@Override
	public void initPerms(Collection<String> permissions) {
		this.permissions.clear();
		this.nonPermissions.clear();
		permissions.forEach(perm -> {
			if (perm.equals("*")) {
				addWildCard();
				return;
			} else if (perm.equals("-*")) {
				removeWildCard();
				return;
			}
			if (perm.startsWith("-")) {
				this.nonPermissions.add(perm.substring(1));
			} else {
				this.permissions.add(perm);
			}
		});
		if (isOnline()) {
			attachPermissions();
		}
	}

	/**
	 * @see OfflinePlayerAccount
	 */
	@Override
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @see OfflinePlayerAccount
	 */
	@Override
	public String getSuffix() {
		return suffix;
	}

	/**
	 * @see OfflinePlayerAccount
	 */
	@Override
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @see OfflinePlayerAccount
	 */
	@Override
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	/**
	 * @see OfflinePlayerAccount
	 */
	@Override
	public void inheritRank(IRank rank) {
		inheritedRanks.add(rank.getName());
	}

	/**
	 * @see OfflinePlayerAccount
	 */
	@Override
	public void unInheritRank(IRank rank) {
		inheritedRanks.remove(rank.getName());
	}

	/**
	 * @see OfflinePlayerAccount
	 */
	@Override
	public Set<String> getInheritedRanks() {
		return inheritedRanks;
	}

	// Online methods

	/**
	 * @see OnlinePlayerAccount
	 */
	@Override
	public Player getPlayer() {
		return player;
	}

	/**
	 * @see OnlinePlayerAccount
	 */
	@Override
	public void logout() {
		this.lastDisconnectTime = System.currentTimeMillis();
		this.lastLocation = null;
		this.player = null;
		this.dirty = true;
		this.permissible = null;
	}

	/**
	 * @see OnlinePlayerAccount
	 */
	@Override
	public long getLastLoginTime() {
		return lastLoginTime;
	}

	/**
	 * @see OnlinePlayerAccount
	 */
	@Override
	public Location getLastLocation() {
		return lastLocation;
	}

	/**
	 * @see OnlinePlayerAccount
	 */
	@Override
	public void updateLastLocation(Location loc) {
		this.lastLocation = loc.clone();
	}

	/**
	 * @see OnlinePlayerAccount
	 */
	@Override
	public boolean teleport(Location loc) {
		if (loc == null) {
			return false;
		}
		updateLastLocation(player.getLocation().clone());
		player.teleport(loc.clone());
		return true;
	}

	/**
	 * @see OnlinePlayerAccount
	 */
	@Override
	public OfflinePlayerAccount getReplyTo() {
		return replyTo;
	}

	/**
	 * @see OnlinePlayerAccount
	 */
	@Override
	public void updateReplyTo(OfflinePlayerAccount account) {
		this.replyTo = account;
	}

	/**
	 * @see OnlinePlayerAccount
	 */
	@Override
	public Pair<String, Boolean> message(OfflinePlayerAccount target, String message) throws PlayerOfflineException {
		if (!target.isOnline()) {
			throw new PlayerOfflineException("You cannot message an offline player.");
		}
		OnlinePlayerAccount onlineTarget = target.getOnlineAccount();
		AccountMessageEvent event = new AccountMessageEvent(this, onlineTarget, message);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			return Pair.of(event.getMessage(), false);
		}
		onlineTarget.updateReplyTo(this);
		updateReplyTo(target);
		return Pair.of(event.getMessage(), true);
	}

	/**
	 * @see OnlinePlayerAccount
	 */
	@Override
	public OfflinePlayerAccount getOfflineAccount() {
		return this;
	}

	/**
	 * @see OnlinePlayerAccount
	 */
	@Override
	public void sendMessage(String message) {
		player.sendMessage(message);
	}

	/**
	 * @see OnlinePlayerAccount
	 */
	@Override
	public void attachPermissions() {
		NMSCBUtil.attachPermissions(player, permissible = new AccountPermissibleBase(this));
		nonPermissions.forEach(perm -> permissible.addAttachment(container.getPlugin(), perm, false));
	}

	/**
	 * @see OnlinePlayerAccount
	 */
	@Override
	public void feed() {
		player.setFoodLevel(20);
	}

	/**
	 * @see OnlinePlayerAccount
	 */
	@Override
	public void fly(boolean fly) {
		player.setAllowFlight(fly);
		player.setFlying(fly);
	}

	/**
	 * @see OnlinePlayerAccount
	 */
	@Override
	public void god() {
		god = !god;
	}

	/**
	 * @see OnlinePlayerAccount
	 */
	@Override
	public void heal() {
		container.getVersionProvider().heal(player);
	}

	/**
	 * @see OnlinePlayerAccount
	 */
	@Override
	public boolean isGodMode() {
		return god;
	}

	/**
	 * @see OnlinePlayerAccount
	 */
	@Override
	public void sudo(String command) {
		AccountSudoEvent event = new AccountSudoEvent(this, command);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			return;
		}
		player.performCommand(event.getCommand());
	}

	/**
	 * @see OnlinePlayerAccount
	 */
	@Override
	public void toggleFlight() {
		player.setAllowFlight(!player.getAllowFlight());
		player.setFlying(player.getAllowFlight());
	}

	@Override
	public Set<IAddress> getPastIPAddresses() {
		return Collections.unmodifiableSet(pastAddresses);
	}

	@Override
	public IAddress getCurrentIPAddress() {
		return address;
	}

	@Override
	public void setCurrentIPAddress(IAddress ip) {
		this.address = ip;
	}

	@Override
	public void initPastIPAddresses(Set<IAddress> addresses) {
		pastAddresses.addAll(addresses);
	}

}
