import java.util.*;
import java.lang.UnsupportedOperationException;

public class MyConcurrentLockedList {

	private List<Object> list;
	private static MyConcurrentLockedList instance = new MyConcurrentLockedList();
	private boolean locked;

	private MyConcurrentLockedList () {
		list = new ArrayList<Object>();
		locked = false;
	}

	public static MyConcurrentLockedList getInstance () {
		return instance;
	}

	// this should only happen once
	// also, synchronized avoids running condition
	// between lock() and add()
	public  void lock () throws Exception {
		synchronized (this) {
			if (!locked)
				locked = true;
			else
				throw new Exception("already locked");
		}
	}

	public void add (Object item) throws Exception {
		if (!locked)
			synchronized (list) {
				System.out.println("new path: " + (String)item);
				list.add(item);
			}
		else
			throw new Exception("locked");
	}

}