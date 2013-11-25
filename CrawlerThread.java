import java.util.*;
import java.io.*;

public class CrawlerThread implements Runnable {

	private int threadNum;
	private volatile MyConcurrentTreeMap workQ;
	private volatile MyConcurrentBlockingList outputList;
	private volatile MyConcurrentLockedList dirList;

	public CrawlerThread (int threadNum, MyConcurrentTreeMap workQ, MyConcurrentLockedList dirList, MyConcurrentBlockingList outputList) {
		this.threadNum = threadNum;
		this.workQ = workQ;
		this.outputList = outputList;
		this.dirList = dirList;
	}

	public void run () {
		//System.out.println("[CrawlerThread" + threadNum + "] Thread started");
		Map.Entry<Integer,String> fileNameEntry;
		String fileName;
		// process a file while there is still one in the list
		while ((fileNameEntry = workQ.pop()) != null) {
			//System.out.println("[CrawlerThread" + threadNum + "] processing " + fileName);
			fileName = fileNameEntry.getValue();
			String result = fileName.split("\\.")[0] + ".o: " + fileName;
			try {
				Set<String> dependencies = process(fileName, new LinkedHashSet<String>());
				for (String dep : dependencies)
					result += " " + dep;
				//System.out.println("Adding: " + fileNameEntry.getKey() + " -> " + result);
				outputList.add(fileNameEntry.getKey(), result);
			} catch (Exception e) {
				//System.out.println("[CrawlerThread" + threadNum + "] Exception: ");
				e.printStackTrace();
			}
		}
	}

	// recursive file processing function
	private Set<String> process (String fileName, Set<String> includeList) throws Exception {
		
		Set<String> outputTemp = new LinkedHashSet<String>();
		Set<String> output = new LinkedHashSet<String>(); // this variable is to force includeList to be
													   // unreferenced sooner (like free)

		// find the path of the file
		String filePath = getFilePath(fileName);

		// parse the file
		FileReader fr = new FileReader(filePath);
		BufferedReader reader = new BufferedReader(fr);
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.matches("( )*#include( )*\"(.)*\".*")) {
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
		//System.out.println();
		//System.out.println(fileName); 
		//System.out.println("file includeList - " + includeList.toString());
		//System.out.println("file outputTemp - " + outputTemp.toString());
		//System.in.read();

		for (String s : outputTemp) {
			//System.out.println(s);
			output.addAll(process(s, output));
		}

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