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

public class MyConcurrentLockedList extends MyConcurrentList {

	private boolean locked;

	public MyConcurrentLockedList () {
		super();
		locked = false;
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

	@Override
	public void add (String item) throws Exception {
		synchronized (this) {
			if (!locked)
				super.add(item);
			else
				throw new Exception("locked");
		}
	}

	public Iterator getIterator () throws Exception {
		synchronized (this) {
			if (locked)
				return Collections.unmodifiableCollection(list).iterator();
			else
				throw new Exception("not locked, so can't get an iterator yet");
		}
	}

}