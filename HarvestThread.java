import java.util.*;

public class HarvestThread implements Runnable {

	MyConcurrentBlockingList outputList;
	boolean finished;
	
	public HarvestThread (MyConcurrentBlockingList outputList) {
		this.outputList = outputList;
		finished = false;
	}

	public void finish () {
		finished = true;
		synchronized (outputList) {
			outputList.finish();
		}
	}

	public void run () {
		
		// will run until there are no more elements on the output list
		// and the main thread set the finished flag
		System.out.println("[HarvestThread] Starting the harvest");
		String output;
		try {
			while (true) {
				if ((output = outputList.blockingPop()) != null)
					System.out.println("[HarvestThread] " + output);
				else if (finished) {
					break;
				}
			}
		} catch (InterruptedException e) {}
		System.out.println("[HarvestThread] The harvest is finished");
	
	}

}