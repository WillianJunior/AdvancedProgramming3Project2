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

	// the int is the id to order the files
	protected List<String> list;

	public MyConcurrentList () {
		list = new ArrayList<String>();
	}

	public void add (String item) throws Exception {
		synchronized (list) {
			//System.out.println("[MyConcurrentList.add] lock");
			list.add(item);
			//System.out.println("[MyConcurrentList.add] unlock");
		}
	}

	public void printout () {
		synchronized (list) {
			//System.out.println("[MyConcurrentList.printout] lock");
			for (String s: list) {
				System.out.println(s);
			}
			//System.out.println("[MyConcurrentList.printout] unlock");
		}
	}

	// returns the first String or null if the list is empty
	public String pop() {
		synchronized (list) {
			//System.out.println("[MyConcurrentList.pop] lock");
			if (list.size() > 0) {
				String temp = list.get(0);
				list.remove(0);
				//System.out.println("[MyConcurrentList.pop] unlock");
				return temp;
			} else {
				//System.out.println("[MyConcurrentList.pop] unlock");
				return null;
			}
		}
	}

}