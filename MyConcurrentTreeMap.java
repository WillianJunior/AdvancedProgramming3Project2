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
		add(inKey, item);
		inKey++;
	}

	public void add (int id, String item) throws Exception {
		synchronized (list) {
			list.put(id, item);
		}
	}

	public int size() {
		return inKey;
	}

	// returns the first String or null if the list is empty
	public Map.Entry<Integer, String> pop() {
		synchronized (list) {
			if (list.size() > 0) {
				Map.Entry<Integer, String> firstEntry = list.firstEntry();
				list.remove(firstEntry.getKey());
				return firstEntry;
			} else
				return null;
		}
	}

}