import java.util.*;
import java.lang.UnsupportedOperationException;

public class MyConcurrentHashMap {

	// the int is the id to order the files
	protected Map<Integer, String> list;
	private int inKey;
	private int outKey;

	public MyConcurrentHashMap () {
		list = new TreeMap<Integer, String>();
		inKey = 0;
		outKey = 0;
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
				Integer key = (Integer)list.keySet().toArray()[0];
				String val = (String)list.values().toArray()[0];
				list.remove(outKey++);
				//System.out.println("[MyConcurrentList.pop] unlock");
				return new AbstractMap.SimpleImmutableEntry<Integer,String>(key, val);
			} else {
				//System.out.println("[MyConcurrentList.pop] unlock");
				return null;
			}
		}
	}

}