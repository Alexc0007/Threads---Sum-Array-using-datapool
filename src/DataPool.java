import java.util.ArrayList;
/**
 * @author Alex Cherniak
 * this class represents a data pool of numbers + a synchronizer for the workers that will take numbers from the data pool , sum them
 * and will return values to the data pool
 */
public class DataPool 
{
	/**********************************************************************************************************************************
	 * Instance Variables
	 *********************************************************************************************************************************/
	private ArrayList<Integer> numbers = new ArrayList<Integer>(); //arraylist of numbers
	private int maxAllowedThreads; //the number of allowed threads
	private int waitingThreads; //number of threads waiting
	private boolean doneFlag = false;

	/**********************************************************************************************************************************
	 * Constructor
	 **********************************************************************************************************************************/
	public DataPool(int[] nums , int numOfThreads)
	{
		//move the array into an arraylist
		for(int i=0;i<nums.length;i++)
		{
			numbers.add(nums[i]);
		}
		maxAllowedThreads = numOfThreads; //set the number of threads
		waitingThreads = 0; //set the number of waiting threads
	}
	
	/*
	 * extractTwo method - will extract 2 values from the data pool and return an array with 2 values for the worker to use
	 * in case there are no 2 values available it will either make the thread wait before extracting or will return null if there is 
	 * nothing to wait for
	 */
	public synchronized int[] extractTwo()
	{
		if(numbers.size() <= 1)
		{
			//im the last thread running
			if(waitingThreads == maxAllowedThreads -1)
			{
				doneFlag = true; //means we are about to finish
			}
			else //not the last thread running
			{
				waitingThreads++; //rise the waiting threads because im gonna wait for other threads
				try 
				{
					wait(); //wait for other threads to finish
				} catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
				waitingThreads--; //reduce the waiting threads because im starting to run again
			}
		}
		if(doneFlag)
		{
			notifyAll();
			return null; //can't extract
		}
		//else need to get the first 2 indexes in the arraylist and return them
		if(numbers.size() > 1)
		{
			int[] result = new int[2]; //create an array with 2 slots
			result[0] = numbers.remove(0); //get the first index
			result[1] = numbers.remove(0); //get the second index
			return result;
		}
		else
		{
			return null;
		}	
	}
	
	/*
	 * insertOne mehtod - once the worker is done summing 2 values - will insert 1 value into the datapool
	 * will notify all other threads when a value is inserted
	 */
	public synchronized void insertOne(int number)
	{
		numbers.add(number);
		notifyAll();
	}
	
	/*
	 * will return the final value from the datapool - the total sum of the array
	 */
	public synchronized int getSum()
	{
		return numbers.get(0); //the sum will always be stored in the first index since its the only value on the arraylist
	}
}
