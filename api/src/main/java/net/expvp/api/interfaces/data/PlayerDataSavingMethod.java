package net.expvp.api.interfaces.data;

import java.util.Set;

import net.expvp.api.interfaces.player.OfflinePlayerAccount;

/**
 * Interface for PlayerDataSaving methods
 * 
 * @author NullUser
 * @see DataSaver
 */
public interface PlayerDataSavingMethod extends DataSaver<OfflinePlayerAccount> {

	/**
	 * @return loaded accounts
	 */
	Set<OfflinePlayerAccount> loadAccounts();

}
