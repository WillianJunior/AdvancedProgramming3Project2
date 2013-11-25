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
			//System.out.println(output);
		}
		//System.out.println("[HarvestThread] The harvest is finished");
	
	}

}