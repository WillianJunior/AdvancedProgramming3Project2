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

	private volatile MyConcurrentTreeMap workQ;
	private volatile MyConcurrentLockedList dirList;
	private volatile MyConcurrentBlockingList outputList;

	public CrawlerThread (MyConcurrentTreeMap workQ, MyConcurrentLockedList dirList, MyConcurrentBlockingList outputList) {
		this.workQ = workQ;
		this.dirList = dirList;
		this.outputList = outputList;
	}

	public void run () {
		
		Map.Entry<Integer,String> fileNameEntry;
		String fileName;
		String result;

		// process a file while there is still one in the list
		while ((fileNameEntry = workQ.pop()) != null) {
			fileName = fileNameEntry.getValue();
			result = fileName.split("\\.")[0] + ".o: " + fileName;
			try {
				Set<String> dependencies = process(fileName);
				for (String dep : dependencies)
					result += " " + dep;
				outputList.add(fileNameEntry.getKey(), result);
			} catch (FileNotFoundException fnfe) {
				System.out.println(fnfe.getMessage());
				System.exit(0);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

	private Set<String> process (String fileName) throws Exception {
		
		// final return list
		Set<String> output = new LinkedHashSet<String>();
		// list of dependencies to be solved
		Set<String> depList = new LinkedHashSet<String>();
		// list of dependencies found into the parsed file
		Set<String> tempDepList = new LinkedHashSet<String>();
		String line;

		// find the path of the file
		String filePath = getFileName(fileName);
		depList.add(filePath);

		// iterate until all dependencies are solved
		do {
			tempDepList.clear();
			for (String dep : depList) {
				// parse the file
				filePath = getFileName(dep);
				FileReader file = new FileReader(filePath);
				BufferedReader reader = new BufferedReader(file);
				while ((line = reader.readLine()) != null) {
					// check if it is a ("") dependency
					if (line.matches("( )*#include( )*\"(.)*\".*")) {
						String dependency = line.split("\"")[1];
						// check it this dependency is already on the output list
						if (!output.contains(dependency))
							tempDepList.add(dependency);
					}
				}
				file.close();
			}

			// add all found dependencies to the output list
			output.addAll(tempDepList);
			// set the depList with the new dependencies to be processed
			depList.clear();
			depList.addAll(tempDepList);
		} while (!tempDepList.isEmpty());

		return output;

	}

	// return the filename of the dependency if it is found within the search directories
	private String getFileName (String fileName) throws Exception {

		Iterator dirIterator = dirList.getIterator();
		String dir;

		while (dirIterator.hasNext()) {
			dir = (String)dirIterator.next();
			
			// get current directory files
			File currentDir = new File(dir);
			File[] files = currentDir.listFiles();

			// search for the file
			for (File f : files) {
				if (f.isFile() && extractFileName(fileName).equals(extractFileName(f.getName())))
					return dir + "/" + fileName;			
			}
		}

		throw new Exception("file not found");

	}

	// get only the filename given a filepath
	private String extractFileName (String filePath) {
		String[] names = filePath.split("/");
		return names[names.length-1];
	}

}