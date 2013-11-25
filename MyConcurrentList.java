import java.util.*;
import java.lang.UnsupportedOperationException;

public class MyConcurrentList {

	// yes, this can be implemented as a stack, but using arraylist
	// gives me more freedom in the future
	protected List<String> list;

	public MyConcurrentList () {
		list = new ArrayList<String>();
	}

	public void add (String item) throws Exception {
		synchronized (list) {
			list.add(item);
		}
	}

	public void printout () {
		synchronized (list) {
			System.out.println("List printout: ");
			for (String s : list) {
				System.out.println(s);
			}
		}
	}

	// returns the first String or null if the list is empty
	public String pop() {
		synchronized (list) {
			//System.out.println("[MyConcurrentList] in sync lock");
			if (list.size() > 0) {
				String temp = list.get(0);
				list.remove(0);
				//System.out.println("[MyConcurrentList] out sync lock");
				return temp;
			} else {
				//System.out.println("[MyConcurrentList] out sync lock");
				return null;
			}
		}
	}

}