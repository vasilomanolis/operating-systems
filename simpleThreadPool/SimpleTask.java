package simpleThreadPool;

/**
 * SimpleTask is the task to be performed by SimplePoolThread.
 * 
 * @author Vasileios Manolis
 *
 */
public class SimpleTask implements ISimpleTask {

	private int a;

	/**
	 * A constructor for the task
	 * 
	 * @param a
	 *            an integer
	 */
	public SimpleTask(int a) {
		this.a = a;
	}

	@Override
	public void run() {
		System.out.println("The " + Thread.currentThread().getName() + " is a fly and has flown " + a + " times.");

	}

}
