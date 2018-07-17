package simpleThreadPool;

/*
 *   @author Behzad_Bordbar & Yi_Chen
 */

/**
 *   <<-- Simple Task -->> 
 *
 *   ISimpleTask is to be performed by PoolThread. 
 */
public interface ISimpleTask{
	/**
	 *   #1. Create a class to implement ISimpleTask, put content of the task to method run().
	 */
	public void run();
}
