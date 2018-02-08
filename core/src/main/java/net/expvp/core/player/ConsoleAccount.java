package net.expvp.core.player;

import java.net.InetAddress;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import net.expvp.api.Pair;
import net.expvp.api.bukkit.events.account.AccountMessageEvent;
import net.expvp.api.bukkit.events.account.AccountSudoEvent;
import net.expvp.api.bukkit.exceptions.PlayerOfflineException;
import net.expvp.api.interfaces.Container;
import net.expvp.api.interfaces.ip.IAddress;
import net.expvp.api.interfaces.permissions.IRank;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.core.ip.IPAddress;

/**
 * PlayerAccount for console impersonation
 * 
 * @author NullUser
 * @see OnlinePlayerAccount
 */
public class ConsoleAccount implements OnlinePlayerAccount {

	@Override
	public Container<?> getContainer() {
		return null;
	}

	@Override
	public void setBalance(double newBalance) {
	}

	@Override
	public void addBalance(double toAdd) {
	}

	@Override
	public double removeBalance(double toRemove) {
		return 0;
	}

	@Override
	public double getBalance() {
		return 0;
	}

	@Override
	public long getLastLogoutTime() {
		return 0;
	}

	@Override
	public void login(Player player, InetAddress address) {
	}

	@Override
	public boolean isOnline() {
		return true;
	}

	@Override
	public OnlinePlayerAccount getOnlineAccount() {
		return this;
	}

	@Override
	public Set<UUID> getIgnoredPlayers() {
		return null;
	}

	@Override
	public boolean isIgnoring(UUID id) {
		return false;
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public UUID getUUID() {
		return null;
	}

	@Override
	public String getName() {
		return "#Console";
	}

	@Override
	public IRank getRank() {
		return null;
	}

	@Override
	public void setRank(IRank rank) {
	}

	@Override
	public void initPerms(Collection<String> permissions) {
	}

	@Override
	public void inheritRank(IRank rank) {
	}

	@Override
	public void unInheritRank(IRank rank) {
	}

	@Override
	public Set<String> getInheritedRanks() {
		return null;
	}

	@Override
	public String getPrefix() {
		return null;
	}

	@Override
	public String getSuffix() {
		return null;
	}

	@Override
	public void setPrefix(String prefix) {
	}

	@Override
	public void setSuffix(String suffix) {
	}

	@Override
	public void save() {
	}

	@Override
	public boolean hasPermission(String permission) {
		return false;
	}

	@Override
	public boolean hasPermission(Permission permission) {
		return false;
	}

	@Override
	public void setPermission(String perm, boolean add) {
	}

	@Override
	public boolean hasWildCard() {
		return false;
	}

	@Override
	public void addWildCard() {
	}

	@Override
	public void removeWildCard() {
	}

	@Override
	public Set<String> getPermissions() {
		return null;
	}

	@Override
	public Set<String> getNonPermissions() {
		return null;
	}

	@Override
	public Player getPlayer() {
		return null;
	}

	@Override
	public void logout() {
	}

	@Override
	public long getLastLoginTime() {
		return 0;
	}

	@Override
	public Location getLastLocation() {
		return null;
	}

	@Override
	public void updateLastLocation(Location loc) {
	}

	@Override
	public boolean teleport(Location loc) {
		return false;
	}

	@Override
	public OfflinePlayerAccount getReplyTo() {
		return null;
	}

	@Override
	public void updateReplyTo(OfflinePlayerAccount account) {
	}

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

	@Override
	public OfflinePlayerAccount getOfflineAccount() {
		return this;
	}

	@Override
	public void sendMessage(String message) {
		Bukkit.getConsoleSender().sendMessage(message);
	}

	@Override
	public void attachPermissions() {
	}

	@Override
	public void feed() {
	}

	@Override
	public void fly(boolean fly) {
	}

	@Override
	public void god() {
	}

	@Override
	public void heal() {
	}

	@Override
	public boolean isGodMode() {
		return false;
	}

	@Override
	public void sudo(String command) {
		AccountSudoEvent event = new AccountSudoEvent(this, command);
		Bukkit.getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			return;
		}
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), event.getCommand());
	}

	@Override
	public void toggleFlight() {
	}

	@Override
	public Set<IAddress> getPastIPAddresses() {
		return null;
	}

	@Override
	public IPAddress getCurrentIPAddress() {
		return null;
	}

	@Override
	public void setCurrentIPAddress(IAddress ip) {
	}

	@Override
	public void initPastIPAddresses(Set<IAddress> addresses) {
	}

}
