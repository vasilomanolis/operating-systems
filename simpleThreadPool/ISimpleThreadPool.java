package simpleThreadPool;

/*
 *   @author Behzad_Bordbar & Yi_Chen
 */

/**
 *   <<-- Thread Pool -->> 
 *   It manages a queue of tasks, starts some pool threads.
 * 		
 *   #1. Create a task queue by using queue data structures, or designing your own data structure. 
 */
public interface ISimpleThreadPool {
	
	/**
	 *   #1. Initialize your queue (or do so in somewhere)
	 *   #2. Starts some ISimplePoolThreads.
	 */
	public void start();
	
	/**
	 *   #1. Stops everything
	 */
	public void stop();
	
	/**
	 *   #1. Add a task to your queue.
	 */
	public void addTask(ISimpleTask task);
}

