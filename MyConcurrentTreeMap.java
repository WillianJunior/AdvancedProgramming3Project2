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

public class MyConcurrentTreeMap {

	// the int is the id to order the files
	protected TreeMap<Integer, String> list;
	private int inKey;

	public MyConcurrentTreeMap () {
		list = new TreeMap<Integer, String>();
		inKey = 0;
	}

	public void add (String item) throws Exception {
		//System.out.println("Add n" + inKey + " - " + item);
		add(inKey, item);
		inKey++;
	}

	public void add (int id, String item) throws Exception {
		synchronized (list) {
			//System.out.println("[MyConcurrentList.add] lock");
			list.put(id, item);
			//System.out.println("[MyConcurrentList.add] unlock");
		}
	}

	public int size() {
		return inKey;
	}

	public void printout () {
		synchronized (list) {
			//System.out.println("[MyConcurrentList.printout] lock");
			for (Map.Entry<Integer, String> e : list.entrySet()) {
				System.out.println(e.getKey() + " - " + e.getValue());
			}
			//System.out.println("[MyConcurrentList.printout] unlock");
		}
	}

	// returns the first String or null if the list is empty
	public Map.Entry<Integer, String> pop() {
		synchronized (list) {
			//System.out.println("[MyConcurrentList.pop] lock");
			if (list.size() > 0) {
				Map.Entry<Integer, String> firstEntry = list.firstEntry();
				//System.out.println("Add n" + key + " - " + item);
				list.remove(firstEntry.getKey());
				//System.out.println("[MyConcurrentList.pop] unlock");
				return firstEntry;
			} else {
				//System.out.println("[MyConcurrentList.pop] unlock");
				return null;
			}
		}
	}

}