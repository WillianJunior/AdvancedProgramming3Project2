import java.util.*;
import java.lang.UnsupportedOperationException;

public class MyConcurrentList {

	private List<Object> list;
	private static MyConcurrentList instance = new MyConcurrentList();

	private MyConcurrentList () {
		list = new ArrayList<Object>();
	}

	public static MyConcurrentList getInstance () {
		return instance;
	}

	public void add (Object item) {
		synchronized (list) {
			System.out.println("new path: " + (String)item);
			list.add(item);
		}
	}

}