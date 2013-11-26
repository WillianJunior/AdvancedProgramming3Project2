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

public class HarvestThread implements Runnable {

	MyConcurrentBlockingList outputList;
	int dependencyCount;
	
	public HarvestThread (MyConcurrentBlockingList outputList, int dependencyCount) {
		this.outputList = outputList;
		this.dependencyCount = dependencyCount;
	}

	// will run until there are no more elements on the output list
	public void run () {
		
		//System.out.println("[HarvestThread] Starting the harvest");
		String output;
		for (int i=0; i<dependencyCount; i++) {
			output = outputList.blockingPop();
			System.out.println(output);
		}
		//System.out.println("[HarvestThread] The harvest is finished");
	
	}

}