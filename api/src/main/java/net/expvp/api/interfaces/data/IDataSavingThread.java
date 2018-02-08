package net.expvp.api.interfaces.data;

public interface IDataSavingThread {

	/**
	 * @return true if the thread is still finishing
	 */
	boolean isFinishing();

	/**
	 * Shuts down the thread
	 */
	void shutdown();

	/**
	 * @param request
	 *            to process
	 */
	void processRequest(Runnable request);

}
