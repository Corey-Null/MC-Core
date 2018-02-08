package net.expvp.core.data.saving;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import net.expvp.api.interfaces.data.IDataSavingThread;

/**
 * Thread for saving data
 * 
 * @author NullUser
 * @see Thread
 */
public class DataSavingThread extends Thread implements IDataSavingThread {

	private final BlockingQueue<Runnable> queue;
	private boolean running;

	/**
	 * @see Thread
	 * @param name
	 */
	public DataSavingThread(String name) {
		super(name);
		this.queue = new LinkedBlockingQueue<>();
		this.running = true;
		start();
	}

	/**
	 * @see Thread
	 */
	@Override
	public void run() {
		while (running || isFinishing()) {
			Runnable run = queue.poll();
			if (run == null) {
				continue;
			}
			run.run();
		}
	}

	/**
	 * @return true if the thread is still finishing
	 */
	public synchronized boolean isFinishing() {
		return !queue.isEmpty();
	}

	/**
	 * Shuts down the thread
	 */
	public synchronized void shutdown() {
		running = false;
	}

	/**
	 * @param request
	 *            to process
	 */
	public synchronized void processRequest(Runnable request) {
		if (running) {
			queue.add(request);
		}
	}

}
