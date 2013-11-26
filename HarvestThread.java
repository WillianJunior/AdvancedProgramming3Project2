/**************************************************/
/**             Authorship Statement             **/
/**************************************************/
/** Author: Willian de Oliveira Barreiros Junior **/
/** Login: 2105514D                              **/
/** Title of Assignment: AP3 Exercise 2          **/
/**************************************************/
/** This is my own work as defined in the        **/
/** Academic Ethics agreement I have signed.     **/
/**************************************************/

import java.util.*;

/*
 * The harvest thread print a result as soon as possible.
 * It is worth noticing that blockingPop() will always
 * return a string, but it will block itself if it
 * can't return one yet, unlocking itself whenever
 * the pop operation is possible again.
 */

public class HarvestThread implements Runnable {

	MyConcurrentBlockingList outputList;
	int dependencyCount;
	
	public HarvestThread (MyConcurrentBlockingList outputList, int dependencyCount) {
		this.outputList = outputList;
		this.dependencyCount = dependencyCount;
	}

	// will print the dependencies the extact amount of times defined by dependencyCount
	public void run () {
		
		for (int i=0; i<dependencyCount; i++)
			System.out.println(outputList.blockingPop());
	
	}

}