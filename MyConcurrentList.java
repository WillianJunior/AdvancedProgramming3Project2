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

public class MyConcurrentList {

	protected List<String> list;

	public MyConcurrentList () {
		list = new ArrayList<String>();
	}

	// this method ensure mutual exclusion on list
	public void add (String item) throws Exception {
		synchronized (list) {
			list.add(item);
		}
	}

	// pops the first String or null if the list is empty
	public String pop() {
		synchronized (list) {
			if (list.size() > 0) {
				String temp = list.get(0);
				list.remove(0);
				return temp;
			} else
				return null;
		}
	}

}