package net.expvp.core.data.saving.flatfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.expvp.api.interfaces.Container;
import net.expvp.api.interfaces.data.DataSaver;
import net.expvp.api.interfaces.data.IDataSavingThread;
import net.expvp.api.interfaces.data.PlayerDataSavingMethod;
import net.expvp.api.interfaces.ip.IAddress;
import net.expvp.api.interfaces.permissions.IRank;
import net.expvp.api.interfaces.player.OfflinePlayerAccount;
import net.expvp.api.interfaces.plugin.IModule;
import net.expvp.core.NullContainer;
import net.expvp.core.ip.IPAddress;
import net.expvp.core.player.PlayerAccount;
import net.expvp.core.plugin.modules.permissions.PermissionsModule;

/**
 * FlatFile saving method for Player Data
 * 
 * @author NullUser
 * @see PlayerDataSavingMethod
 */
public class PlayerFFDataSavingMethod implements PlayerDataSavingMethod {

	private final Container<?> container;
	private final File playerDataFolder;
	private final File backupFolder;

	/**
	 * Initializes required fields for saving player data
	 * 
	 * @param container
	 */
	public PlayerFFDataSavingMethod(NullContainer container) {
		this.container = container;
		this.playerDataFolder = new File(container.getPlugin().getDataFolder() + File.separator + "players");
		if (!this.playerDataFolder.exists()) {
			this.playerDataFolder.mkdirs();
		}
		this.backupFolder = new File(playerDataFolder + File.separator + "backup");
		if (!this.backupFolder.exists()) {
			this.backupFolder.mkdirs();
		}
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
	 * @see PlayerDataSavingMethod
	 */
	@Override
	public Set<OfflinePlayerAccount> loadAccounts() {
		Set<OfflinePlayerAccount> accounts = Sets.newHashSet();
		for (File file : playerDataFolder.listFiles()) {
			if (file.isDirectory() || !file.getName().endsWith(".pl")) {
				continue;
			}
			try {
				System.out.println("Loading account for: " + file.getName().split("\\.")[0]);
				BufferedReader reader = new BufferedReader(new FileReader(file));
				List<String> data = Lists.newArrayList();
				String line = null;
				while ((line = reader.readLine()) != null) {
					data.add(line);
				}
				UUID id = UUID.fromString(data.get(0));
				String name = data.get(1);
				double balance = Double.parseDouble(data.get(2));
				long lastLogout = Long.parseLong(data.get(3));
				String next = data.get(7);
				Set<UUID> ignoring = Sets.newHashSet();
				int i;
				for (i = 7; i < data.size() && (next != null && !next.equals("--")); i++, next = data.get(i)) {
					if (next.equalsIgnoreCase("--")) {
						break;
					}
					ignoring.add(UUID.fromString(next));
				}
				i++;
				next = data.get(i);
				Set<IAddress> addresses = Sets.newHashSet();
				for (; i < data.size() && (next != null && !next.equals("--")); i++, next = data.get(i)) {
					if (next.equalsIgnoreCase("--")) {
						break;
					}
					addresses.add(new IPAddress(next));
				}
				IAddress address = new IPAddress(data.get(6));
				PlayerAccount account = new PlayerAccount(container, id, balance, ignoring, name, lastLogout);
				if (Boolean.parseBoolean(data.get(4))) {
					account.addWildCard();
				}
				account.setCurrentIPAddress(address);
				account.initPastIPAddresses(addresses);
				IModule<?> module = container.getModule("permissions");
				if (module != null && module instanceof PermissionsModule && module.isEnabled()) {
					PermissionsModule pModule = (PermissionsModule) module;
					account.setRank(pModule.getRankHandler().getRank(data.get(5)));
					if (account.getRank() == null && !data.get(5).equals("null")) {
						System.out.println("Could not load rank " + data.get(5) + " from "
								+ pModule.getRankHandler().getCachedRanks().size());
						account.setRank(pModule.getRankHandler().getDefaultRank());
					} else if (account.getRank() == null) {
						account.setRank(pModule.getRankHandler().getDefaultRank());
					}
				}
				account.setDirty(false);
				accounts.add(account);
				reader.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("<--- Please do not edit the player data files. --->");
			}
		}
		return accounts;
	}

	/**
	 * @see DataSaver
	 */
	@Override
	public void processSaveRequest(OfflinePlayerAccount account) {
		if (account == null) {
			return;
		}
		UUID id = account.getUUID();
		File file = new File(playerDataFolder, id.toString() + ".pl");
		container.getDataSavingThread().processRequest(new Runnable() {
			@Override
			public void run() {
				boolean backedup;
				File backupFile = new File(backupFolder, id.toString() + ".pl");
				if (backedup = file.exists()) {
					file.renameTo(backupFile);
				}
				try {
					file.createNewFile();
					PrintWriter writer = new PrintWriter(new FileWriter(file));
					writer.println(id.toString());
					writer.println(account.getName());
					writer.println(account.getBalance());
					writer.println(account.getLastLogoutTime());
					writer.println(account.hasWildCard());
					IRank rank = account.getRank();
					if (rank == null) {
						writer.println("null");
					} else {
						writer.println(rank.getName());
					}
					IAddress address = account.getCurrentIPAddress();
					writer.println(address.getAddress());
					for (UUID id : account.getIgnoredPlayers()) {
						writer.println(id.toString());
					}
					writer.println("--");
					for (IAddress pastAddress : account.getPastIPAddresses()) {
						writer.println(pastAddress.getAddress());
					}
					writer.println("--");
					writer.close();
				} catch (IOException e) {
					System.out.println("Failed to save " + account.getName() + "'s account, attempting to backup.");
					if (backedup) {
						backupFile.renameTo(file);
					}
					System.out.println("Printing error... contact the developer for support.");
					e.printStackTrace();
				}
			}
		});
	}

}
