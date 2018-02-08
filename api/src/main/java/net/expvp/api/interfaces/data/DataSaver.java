package net.expvp.api.interfaces.data;

/**
 * Interface for classes saving data
 * 
 * @author NullUser
 *
 * @param <T>
 *            Object to be saved
 */
public interface DataSaver<T> {

	/**
	 * @return type of datasaving
	 */
	String getType();

	/**
	 * @return thread for datasaving
	 */
	IDataSavingThread getThread();

	/**
	 * @param object
	 *            To be saved
	 */
	void processSaveRequest(T object);

}
