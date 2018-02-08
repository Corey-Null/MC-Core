package net.expvp.api.interfaces.data;

/**
 * Interface for WorldDataSaving methods
 * 
 * @author NullUser
 * @see DataSaver
 */
public interface WorldDataSavingMethod extends DataSaver<IWorldData> {

	/**
	 * @return loaded world data
	 */
	IWorldData loadData();

}
