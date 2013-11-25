import java.util.*;

public class HarvestThread implements Runnable {

	MyConcurrentBlockingList outputList;
	boolean finished;
	
	public HarvestThread (MyConcurrentBlockingList outputList) {
		this.outputList = outputList;
		finished = false;
	}

	public void finished () {
		finished = true;
	}

	public void run () {
		
		// will run until there are no more elements on the output list
		// and the main thread set the finished flag
		System.out.println("[HarvestThread] Starting the harvest");
		String output;
		while (true) {
			if ((output = outputList.pop()) != null)
				System.out.println("[HarvestThread] " + output);
			else if (!finished)
				try {
					synchronized (this) {
						System.out.println("[HarvestThread] waiting");
						this.wait();
					}
				} catch (Exception e) {
					System.out.println("Get exception:");
					e.printStackTrace();
					System.exit(0);
				}
			else 
				break;
		}
		System.out.println("[HarvestThread] The harvest is finished");
	
	}

}