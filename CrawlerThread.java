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
import java.io.*;

public class CrawlerThread implements Runnable {

	private int threadNum;
	private volatile MyConcurrentTreeMap workQ;
	private volatile MyConcurrentBlockingList outputList;
	private volatile MyConcurrentLockedList dirList;

	public CrawlerThread (int threadNum, MyConcurrentTreeMap workQ, MyConcurrentLockedList dirList, MyConcurrentBlockingList outputList) {
		this.threadNum = threadNum; // debuging purposes
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
				Set<String> dependencies = process(fileName);
				for (String dep : dependencies)
					result += " " + dep;
				//System.out.println("Adding: " + fileNameEntry.getKey() + " -> " + result);
				outputList.add(fileNameEntry.getKey(), result);
			} catch (FileNotFoundException fnfe) {
				System.out.println(fnfe.getMessage());
				System.exit(0);
			} catch (Exception e) {
				//System.out.println("[CrawlerThread" + threadNum + "] Exception: ");
				e.printStackTrace();
			}
		}
	}

	private Set<String> process (String fileName) throws Exception {
		
		Set<String> output = new LinkedHashSet<String>();
		Set<String> depList = new LinkedHashSet<String>();
		Set<String> tempDepList = new LinkedHashSet<String>();

		// find the path of the file
		String filePath = getFilePath(fileName);
		//System.out.println(filePath);
		depList.add(filePath);

		do {
			tempDepList.clear();
			for (String dep : depList) {
				//System.out.println("parsing " + dep);
				// parse the file
				filePath = getFilePath(dep);
				FileReader file = new FileReader(filePath);
				BufferedReader reader = new BufferedReader(file);
				String line;
				while ((line = reader.readLine()) != null) {
					if (line.matches("( )*#include( )*\"(.)*\".*")) {
						String dependency = line.split("\"")[1];
						if (!output.contains(dependency))
							tempDepList.add(dependency);
					}
				}
				file.close();
			}

			output.addAll(tempDepList);
			depList.clear();
			depList.addAll(tempDepList);
		} while (!tempDepList.isEmpty());

		return output;

	}

	// recursive file processing function
	@Deprecated
	private Set<String> process (String fileName, Set<String> includeList) throws Exception {
		
		Set<String> outputTemp = new LinkedHashSet<String>();
		Set<String> output = new LinkedHashSet<String>();

		// find the path of the file
		String filePath = getFilePath(fileName);

		// parse the file
		FileReader file = new FileReader(filePath);
		BufferedReader reader = new BufferedReader(file);
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
		file.close();

		
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
			//System.out.println("dir: " + dir);
			// get current directory files
			File currentDir = new File(dir);
			File[] files = currentDir.listFiles();
			for (File f : files) {
				//System.out.println("comparing " + getFileName(f.getName()) + " with " + getFileName(fileName));
				if (f.isFile() && getFileName(fileName).equals(getFileName(f.getName())))
					return dir + "/" + fileName;			
			}
		}

		throw new Exception("file not found");

	}

	private String getFileName (String filePath) {
		String[] names = filePath.split("/");
		return names[names.length-1];
	}

}