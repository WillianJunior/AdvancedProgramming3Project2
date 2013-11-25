import java.util.*;

public class CrawlerThread implements Runnable {

	private int threadNum;
	private volatile MyConcurrentList workQ;
	private volatile MyConcurrentBlockingList outputList;
	private volatile MyConcurrentLockedList dirList;

	public CrawlerThread (int threadNum, MyConcurrentList workQ, MyConcurrentLockedList dirList, MyConcurrentBlockingList outputList) {
		this.threadNum = threadNum;
		this.workQ = workQ;
		this.outputList = outputList;
		this.dirList = dirList;
	}

	public void run () {
		System.out.println("[CrawlerThread" + threadNum + "] Thread started");
		String fileName;
		// process a file while there is still one in the list
		while ((fileName = workQ.getFirstElement()) != null) {
			System.out.println("[CrawlerThread" + threadNum + "] processing " + fileName);
			String result = fileName + ".o: ";
			try {
				outputList.add(result);
			} catch (Exception e) {
				System.out.println("[CrawlerThread" + threadNum + "] Exception: ");
				e.printStackTrace();
			}
		}
	}

	// recursive file processing function
	private String process (String fileName) {
		// parse the file
		// create a list of dependencies
		// call process on each dependency
		return "";
	}

}