import java.util.*;

public class HarvestThread implements Runnable {

	MyConcurrentBlockingList outputList;
	
	public HarvestThread (MyConcurrentBlockingList outputList) {
		this.outputList = outputList;
	}

	public void run () {
		
		// will run until there are no more elements on the output list
		// and the main thread set the finished flag
		//System.out.println("[HarvestThread] Starting the harvest");
		String output;
		try {
			while (true) {
				if ((output = outputList.blockingPop()) != null)
					System.out.println(output);
					//System.out.println("[HarvestThread] " + output);
			}
		} catch (InterruptedException e) {}
		//System.out.println("[HarvestThread] The harvest is finished");
	
	}

}