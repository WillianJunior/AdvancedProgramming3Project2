import java.util.*;
import java.io.File;

public class includeCrawler {

	private static final String CPATH = "CPATH";
	private static final String CRAWLER_THREADS = "CRAWLER_THREADS";

	public static volatile MyConcurrentList workQ;
	public static volatile MyConcurrentBlockingList outputList;
	public static volatile MyConcurrentLockedList dirList;

	public static void main(String[] args) throws Exception {
		 
		// instanciate the workQ
		workQ = new MyConcurrentList();
		outputList = new MyConcurrentBlockingList();
		dirList = new MyConcurrentLockedList();

		// assemble the files list (working queue)
		// get path files
		String cpathStr = System.getenv(CPATH);
		if (cpathStr != null) {
			String[] cpath = cpathStr.split(":");
			for (String path : cpath) {
				workQ.add(path);
			}
		}

		// get -Idir argument files
		int argc = 0;
		// add the current directory to the list
		dirList.add("./");
		for (String arg : args)
			if (arg.matches("-I*")) {
				dirList.add(arg.substring(2));
				argc++;
			}
		// just some precaution: lock the list for updates
		dirList.lock();

		// get local directory files passed by argv
		for(int i=argc; i<args.length; i++) {
			if (args[i].matches("^.+[.y]$") || args[i].matches("^.+[.l]$") || args[i].matches("^.+[.c]$"))
				workQ.add(args[i]);
			else {
				System.out.println("[includeCrawler] Illegal extension: " + args[i] + " - must be .c, .y or .l\n");
				return;
			}
		}

		// create the harvest thread
		HarvestThread harvestThread;
		new Thread(harvestThread = new HarvestThread(outputList)).start();

		// create working thread pool
		String workersNumVar = System.getenv(CRAWLER_THREADS);
		int workersNum;
		if (cpathStr != null) {
			try {
				workersNum = Integer.parseInt(workersNumVar);
			} catch (Exception e){
				System.out.println("[includeCrawler] CRAWLER_THREADS env var not parsable");
				return;
			}
		} else
			workersNum = 1; // change to 2 later
		Thread[] threadPool = new Thread[workersNum];

		// start the workers thread
		for (int i=0; i<workersNum; i++) {
			threadPool[i] = new Thread(new CrawlerThread(workQ, dirList, outputList));
			threadPool[i].start();
		}

		// wait for the workers threads to finish
		for (int i=0; i<workersNum; i++) {
			threadPool[i].join();
			System.out.println("[includeCrawler] Thread " + i + " is done");
		}

		// signal the harvest thread that it can be finished
		harvestThread.finished();
		synchronized (harvestThread) {
			harvestThread.notify();
		}

		/*/create the working threads
		String threadNumStr;
		long crawler_threads;
		if ((threadNumStr = System.getenv(CRAWLER_THREADS)) != null)
			crawler_threads = Long.parseLong(threadNumStr);
		*///else
		//Thread threadSpawner = new Thread(new ThreadSpawner(crawler_threads, workQ));

		// wait for all threads to finish by waiting for the spawner
		System.out.println("[includeCrawler] main is finished");

	}

}
