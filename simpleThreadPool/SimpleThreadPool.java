package simpleThreadPool;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * SimpleThreadPool manages a queue of ISimpleTask() and a set of
 * ISimplePoolThread()
 * 
 * @author Vasileios Manolis
 *
 */
public class SimpleThreadPool implements ISimpleThreadPool {

	private LinkedBlockingQueue<ISimpleTask> tasksQ = new LinkedBlockingQueue<>();
	private Thread[] threads = new Thread[7];

	/**
	 * The start() method starts a number of SimplePoolThread and initialize the
	 * task queue.
	 */
	@Override
	public void start() {

		for (int i = 0; i < threads.length; i++) {
			this.threads[i] = (new Thread(new SimplePoolThread(this.tasksQ)));
			this.threads[i].start();
		}
	}

	/**
	 * The stop() method stops the threads with an interrupt() method.
	 */
	@Override
	public void stop() {

		for (int i = 0; i < threads.length; i++) {
			this.threads[i].interrupt();
		}
	}

	/**
	 * The addTask() method puts the task to the queue.
	 */
	@Override
	public void addTask(ISimpleTask task) {
		try {
			// Putting the task to the queue
			this.tasksQ.put(task);
		} catch (InterruptedException e) {
			// Catching an exception
			System.out.print("Catch, SimpleThreadPool - Print Mark");
			e.printStackTrace();
		}
	}

}
