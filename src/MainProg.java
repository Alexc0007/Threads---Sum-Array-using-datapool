
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

/**
 * @author Alex Cherniak
 * this is a main method that creates a data pool and workers
 * each worker will take 2 values(number) out of the data pool , will sum them and return 1 value to the data pool
 * when all the workers are done , the main will print out the sum of the numbers that were in the data pool
 */
public class MainProg 
{
	public static void main(String[] args) 
	{
		final int MAX_VALUE = 100; //maximum value for a number that can be generated to be in the array
		final int MIN_VALUE = 1; //minimum value for a number that can be generated to be in the array
		int numberOfValues = 0; //will be inserted by the user to determine how many numbers will be in the array we want to sum
		int numberOfWorkers = 0; //will be inserted by the user to determine the number of parallel active workers that will be performing the sum
		int workerSerial = 0; //each worker will be given a serial number - will help the debugging process
		Random rn = new Random();
		Scanner scan = new Scanner(System.in);
		
		//insert values by user
		System.out.println("please insert the amount of values for the array to have");
		numberOfValues = scan.nextInt();
		System.out.println("please insert the number of active workers you would like to use:");
		numberOfWorkers = scan.nextInt();
		
		scan.close(); //close scanner
		CountDownLatch latch = new CountDownLatch(numberOfWorkers); //create a CountDown Latch with the number of workers
		
		int[] nums = new int[numberOfValues];
		//fill with random numbers between 1 and 100
		for(int i=0;i<nums.length;i++)
		{
			nums[i] = rn.nextInt(MAX_VALUE - MIN_VALUE +1) + MIN_VALUE; //generate a random int between 1-100
		}
		
		//for debugging - print the created array
		System.out.println("the created array is:");
		for(int i=0;i<nums.length;i++)
		{
			System.out.print(nums[i] + "  ");
		}
		System.out.println(); //print an empty line after the array
		
		DataPool pool = new DataPool(nums , numberOfWorkers); //create the data pool out of the array above
		
		
		//create a workers array
		Worker[] threads = new Worker[numberOfWorkers];
		//send workers to work
		for(Worker worker : threads)
		{
			workerSerial++;
			worker = new Worker(pool , workerSerial, latch); //crate a worker with its serial number and latch
			worker.start();
		}
		
		//need to wait for all threads to finish before going forward
		try 
		{
			latch.await(); //wait for all threads to finish
		} catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
		
		//print the final result
		System.out.println(pool.getSum());
	}

}
