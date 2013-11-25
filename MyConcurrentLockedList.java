import java.util.*;
import java.lang.UnsupportedOperationException;

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
		if (!locked)
			super.add(item);
		else
			throw new Exception("locked");
	}

}