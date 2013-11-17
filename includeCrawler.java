import java.io.File;

public class includeCrawler {

	private static final String CPATH = "CPATH";
	private static final String CRAWLER_THREADS = "CRAWLER_THREADS";

	public static volatile MyConcurrentList workQ;

	public static void main(String[] args) throws Exception {
		 
		// instanciate the workQ
		workQ = MyConcurrentList.getInstance();

		// create the helper thread
		// this will create the other threads (harvest and worker threads)
		String threadNumStr;
		long crawler_threads;
		if ((threadNumStr = System.getenv(CRAWLER_THREADS)) != null)
			crawler_threads = Long.parseLong(threadNumStr);
		else
			crawler_threads = 2;
		Thread threadSpawner = new Thread(new ThreadSpawner(crawler_threads, workQ));
		threadSpawner.start();
		
		// set up the harvest structure

		// assemble the files list (working queue)
		// get path files
		String cpathStr = System.getenv(CPATH);
		if (cpathStr != null) {
			String[] cpath = cpathStr.split(":");
			for (String path : cpath)
				workQ.add(path);
		}

		// get local directory files
		File currentDir = new File(".");
		File[] files = currentDir.listFiles();
		for (File f : files)
			if (f.isFile())
				workQ.add(f.getCanonicalPath());

		// wait for all threads to finish by waiting for the spawner
		threadSpawner.join();
		System.out.println("the crawling is finished");

	}

}
