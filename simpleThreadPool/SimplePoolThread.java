package simpleThreadPool;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * SimplePoolThread is the continuously running worker thread started by
 * SimpleThreadPool
 * 
 * @author Vasileios Manolis
 *
 */
public class SimplePoolThread implements ISimplePoolThread {

	private LinkedBlockingQueue<ISimpleTask> tasksQ = new LinkedBlockingQueue<>();

	/**
	 * A constructor for SimplePoolThread
	 * 
	 * @param tasksQ
	 *            a LinkedBlockingQueue with objects of ISimpleTask
	 */
	public SimplePoolThread(LinkedBlockingQueue<ISimpleTask> tasksQ) {
		this.tasksQ = tasksQ;
	}

	/**
	 * In method run(), we use a while(true) loop to retrieve new tasks (ISimpleTask
	 * instances) and perform them.
	 */
	@Override
	public void run() {

		while (true) {

			try {
				tasksQ.take().run();
			} catch (InterruptedException e) {
				System.out.print("Catch, SimplePoolThread - Print Mark");
				e.printStackTrace();
				break;

			}

		}

	}

}
