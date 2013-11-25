import java.util.*;
import java.io.*;

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
		while ((fileName = workQ.pop()) != null) {
			System.out.println("[CrawlerThread" + threadNum + "] processing " + fileName);
			String result = fileName.split("\\.")[0] + ".o: " + fileName;
			try {
				List<String> dependencies = removeRepeated(process(fileName, new ArrayList<String>()));
				for (String dep : dependencies)
					result += " " + dep;
				outputList.add(result);
			} catch (Exception e) {
				System.out.println("[CrawlerThread" + threadNum + "] Exception: ");
				e.printStackTrace();
			}
		}

		// suicide
		return;
	}

	// given a list, remove all repeated strings
	private List<String> removeRepeated (List<String> list) {
		List<String> newList = new ArrayList<String>();
		for (String s : list) 
			if (!newList.contains(s)) 
				newList.add(s);

		return newList;
	}

	// recursive file processing function
	private List<String> process (String fileName, List<String> includeList) throws Exception {
		
		List<String> outputTemp = new ArrayList<String>();
		List<String> output = new ArrayList<String>(); // this variable is to force includeList to be
													   // unreferenced sooner (like free)

		// find the path of the file
		String filePath = getFilePath(fileName);

		// parse the file
		FileReader fr = new FileReader(filePath);
		BufferedReader reader = new BufferedReader(fr);
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.matches("( )*#include( )*\"(.)*\"( )*")) {
				String dependency = line.split("\"")[1];
				if (!includeList.contains(dependency))
					outputTemp.add(dependency);
			}
		}

		// IMPORTANT!!! without this there is the real chance of
		// exceeding the max num of open files (happened!!)
		fr.close();

		
		output.addAll(includeList);
		output.addAll(outputTemp);
		for (String s : outputTemp)
			output.addAll(process(s, output));

		return output;
	}

	private String getFilePath (String fileName) throws Exception {

		Iterator dirIterator = dirList.getIterator();
		String dir;
		while (dirIterator.hasNext()) {
			dir = (String)dirIterator.next();
			// get current directory files
			File currentDir = new File(dir);
			File[] files = currentDir.listFiles();
			for (File f : files)
				if (f.isFile() && f.getName().equals(fileName))
					return dir + "/" + fileName;			
		}

		throw new Exception("file not found");

	}

}