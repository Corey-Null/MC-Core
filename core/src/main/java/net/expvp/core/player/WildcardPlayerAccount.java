package net.expvp.core.player;

import java.net.InetAddress;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import net.expvp.api.Pair;
import net.expvp.api.bukkit.exceptions.PlayerOfflineException;
import net.expvp.api.interfaces.Container;
import net.expvp.api.interfaces.ip.IAddress;
import net.expvp.api.interfaces.permissions.IRank;
import net.expvp.api.interfaces.player.IPlayerDataHandler;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.api.interfaces.player.OnlinePlayerAccount;
import net.expvp.core.NullContainer;

/**
 * Used to represent all player accounts (with exceptions to certain
 * modifications)
 * 
 * @author NullUser
 * @see OnlinePlayerAccount
 */
public class WildcardPlayerAccount implements OnlinePlayerAccount {

	private final NullContainer container;

	public WildcardPlayerAccount(NullContainer container) {
		this.container = container;
	}

	@Override
	public Container<?> getContainer() {
		return container;
	}

	@Override
	public void setBalance(double newBalance) {
		getAccs().forEach(acc -> acc.setBalance(newBalance));
	}

	@Override
	public void addBalance(double toAdd) {
		getAccs().forEach(acc -> acc.addBalance(toAdd));
	}

	@Override
	public double removeBalance(double toRemove) {
		getAccs().forEach(acc -> acc.removeBalance(toRemove));
		return -1;
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
		return "All Players";
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
		getAccs().forEach(OfflinePlayerAccount::save);
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
		getOAccs().forEach(oAcc -> oAcc.updateLastLocation(loc));
	}

	@Override
	public boolean teleport(Location loc) {
		if (loc == null) {
			return false;
		}
		getOAccs().forEach(oAcc -> oAcc.teleport(loc));
		return true;
	}

	@Override
	public OfflinePlayerAccount getReplyTo() {
		return null;
	}

	@Override
	public void updateReplyTo(OfflinePlayerAccount account) {
		getOAccs().forEach(oAcc -> oAcc.updateReplyTo(account));
	}

	@Override
	public Pair<String, Boolean> message(OfflinePlayerAccount target, String message) throws PlayerOfflineException {
		return null;
	}

	@Override
	public OfflinePlayerAccount getOfflineAccount() {
		return this;
	}

	@Override
	public void sendMessage(String message) {
		getOAccs().forEach(acc -> acc.sendMessage(message));
	}

	@Override
	public void attachPermissions() {
	}

	@Override
	public void feed() {
		getOAccs().forEach(OnlinePlayerAccount::feed);
	}

	@Override
	public void fly(boolean fly) {
		getOAccs().forEach(acc -> acc.fly(fly));
	}

	@Override
	public void god() {
		getOAccs().forEach(OnlinePlayerAccount::god);
	}

	@Override
	public void heal() {
		getOAccs().forEach(OnlinePlayerAccount::heal);
	}

	@Override
	public boolean isGodMode() {
		return false;
	}

	@Override
	public void toggleFlight() {
		getOAccs().forEach(OnlinePlayerAccount::toggleFlight);
	}

	@Override
	public void sudo(String command) {
		getOAccs().forEach(acc -> acc.sudo(command));
	}

	/**
	 * Native method for returning the player data handler
	 * 
	 * @return the PlayerDataHandler
	 */
	private final IPlayerDataHandler handler() {
		return container.getPlayerDataHandler();
	}

	/**
	 * Native method for returning all offline accounts.
	 * 
	 * @return all offline player accounts
	 */
	private final Set<OfflinePlayerAccount> getAccs() {
		return handler().getCachedAccounts();
	}

	/**
	 * Native method for returning all online accounts.
	 * 
	 * @return all online player accounts
	 */
	private final Set<OnlinePlayerAccount> getOAccs() {
		return getAccs().stream().filter(OfflinePlayerAccount::isOnline).map(OfflinePlayerAccount::getOnlineAccount)
				.collect(Collectors.toSet());
	}

	@Override
	public Set<IAddress> getPastIPAddresses() {
		return null;
	}

	@Override
	public IAddress getCurrentIPAddress() {
		return null;
	}

	@Override
	public void setCurrentIPAddress(IAddress ip) {
	}

	@Override
	public void initPastIPAddresses(Set<IAddress> addresses) {
	}

}
