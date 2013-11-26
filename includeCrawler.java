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
import java.io.File;

public class includeCrawler {

	private static final String CPATH = "CPATH";
	private static final String CRAWLER_THREADS = "CRAWLER_THREADS";

	public static volatile MyConcurrentTreeMap workQ;
	public static volatile MyConcurrentBlockingList outputList;
	public static volatile MyConcurrentLockedList dirList;

	public static void main(String[] args) throws Exception {
		 
		// instanciate the workQ
		workQ = new MyConcurrentTreeMap();
		outputList = new MyConcurrentBlockingList();
		dirList = new MyConcurrentLockedList();

		// assemble the files list (working queue)
		// add the current directory to the list
		dirList.add("./");
		// get -Idir argument files
		int argc = 0;
		for (String arg : args)
			if (arg.matches("-I(.)*")) {
				dirList.add(arg.substring(2));
				argc++;
			}

		// get path files
		String cpathStr = System.getenv(CPATH);
		if (cpathStr != null) {
			String[] cpath = cpathStr.split(":");
			for (String path : cpath) {
				dirList.add(path);
			}
		}
		// just some precaution: lock the list for updates
		dirList.lock();

		// get local directory files passed by argv
		for(int i=argc; i<args.length; i++) {
			//System.out.println(args[i]);
			if (args[i].matches("^.+[.y]$") || args[i].matches("^.+[.l]$") || args[i].matches("^.+[.c]$"))
				workQ.add(args[i]);
			else {
				System.out.println("[includeCrawler] Illegal extension: " + args[i] + " - must be .c, .y or .l\n");
				return;
			}
		}

		//workQ.printout();
		//System.in.read();

		// create the harvest thread
		Thread harvestThread;
		harvestThread = new Thread(new HarvestThread(outputList, workQ.size()));
		harvestThread.start();

		// start timing the crawling execution
		long startTime = System.currentTimeMillis();

		// create working thread pool
		String workersNumVar = System.getenv(CRAWLER_THREADS);
		int workersNum;
		if (workersNumVar != null) {
			try {
				workersNum = Integer.parseInt(workersNumVar);
			} catch (Exception e){
				System.out.println("[includeCrawler] CRAWLER_THREADS env var not parsable");
				return;
			}
		} else
			workersNum = 2; // change to 2 later
		Thread[] threadPool = new Thread[workersNum];

		// start the workers thread
		for (int i=0; i<workersNum; i++) {
			threadPool[i] = new Thread(new CrawlerThread(i, workQ, dirList, outputList));
			threadPool[i].start();
		}

		// wait for the workers threads to finish
		for (int i=0; i<workersNum; i++) {
			threadPool[i].join();
			//System.out.println("[includeCrawler] Thread " + i + " is done");
		}

		// get end time for crawling execution
		long stopTime = System.currentTimeMillis();
		long crawlingTime = stopTime - startTime;

		// wait for the harvest thread to end
		harvestThread.join();

		//System.out.println("[includeCrawler] main is finished");
		//System.out.println("[includeCrawler] execution crawling time: " + crawlingTime + " millis");
		//System.out.print(crawlingTime);

	}

}
