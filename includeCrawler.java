import java.util.*;
import java.io.File;

public class includeCrawler {

	private static final String CPATH = "CPATH";
	private static final String CRAWLER_THREADS = "CRAWLER_THREADS";

	public static volatile MyConcurrentList workQ;
	public static volatile MyConcurrentLockedList dirList;

	public static void main(String[] args) throws Exception {
		 
		System.out.println("srgs" + args.length);
		// instanciate the workQ
		workQ = MyConcurrentList.getInstance();
		dirList = MyConcurrentLockedList.getInstance();

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

		// get local directory files passed by argv
		for(int i=argc; i<args.length; i++) {
			if (args[i].matches("^.+[.y]$") || args[i].matches("^.+[.l]$") || args[i].matches("^.+[.c]$"))
				workQ.add(args[i]);
			else {
				System.out.println("Illegal extension: " + args[i] + " - must be .c, .y or .l\n");
				return;
			}
		}

		/*
		// create the helper thread
		// this will create the other threads (harvest and worker threads)
		String threadNumStr;
		long crawler_threads;
		if ((threadNumStr = System.getenv(CRAWLER_THREADS)) != null)
			crawler_threads = Long.parseLong(threadNumStr);
		//else
		Thread threadSpawner = new Thread(new ThreadSpawner(crawler_threads, workQ));
		*/

		// wait for all threads to finish by waiting for the spawner
		System.out.println("the crawling is finished");

	}

}
