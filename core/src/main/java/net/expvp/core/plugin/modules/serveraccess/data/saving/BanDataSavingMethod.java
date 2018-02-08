package net.expvp.core.plugin.modules.serveraccess.data.saving;

import java.util.Map;
import java.util.Set;

import net.expvp.api.interfaces.data.DataSaver;
import net.expvp.core.plugin.modules.serveraccess.data.BanData;
import net.expvp.core.plugin.modules.serveraccess.data.BanDataManager;

/**
 * Interface for handling the saving of ban data with flatfiles
 * 
 * @author NullUser
 * @see DataSaver<T>
 */
public interface BanDataSavingMethod extends DataSaver<BanDataManager> {

	/**
	 * @return the loaded ban data
	 */
	Map<String, Set<BanData<?>>> loadBanData();

}
