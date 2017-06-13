import java.util.concurrent.CountDownLatch;

/**
 * @author Alex Cherniak
 * this class represents a worker - thread that will pull 2 numbers out of a data pool ,sum them and will insert back a value - the sum of 2 numbers
 */

public class Worker extends Thread
{
	private DataPool pool; //represents the datapool that the worker is working on
	private int serialNumber; //will hold a serial number of the current worker - for debugging
	protected CountDownLatch latch; // a countDown latch to allow each worker to let the main know that its done
	
	public Worker(DataPool p , int sn , CountDownLatch latch)
	{
		pool = p;
		serialNumber = sn;
		this.latch = latch;
	}
	
	public void run()
	{
		System.out.println("worker number " + serialNumber + " is running"); //print the number of the worker that is running
		int[] result;
		result = pool.extractTwo(); //get 2 numbers from the pool
		while(result != null)
		{
			pool.insertOne(result[0]+result[1]); //return the sum to the pull
			result = pool.extractTwo(); //get another 2 numbers
		}
		System.out.println("worker number " + serialNumber + " has finished"); //print the number of the worker that is finished
		latch.countDown(); //count down the latch before thread's death
	}
}
