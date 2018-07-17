package simpleThreadPool;

/*
 *   @author Behzad_Bordbar & Yi_Chen
 */

/**
 *   <<-- Pool Thread -->> 
 *
 *   It will be running continuously. It will try to retrieve new tasks when it is idle. 
 */
public interface ISimplePoolThread extends Runnable {
	/**
	 *   Use an infinite loop to retrieve and perform tasks.
	 */
    @Override
    public void run();
}
