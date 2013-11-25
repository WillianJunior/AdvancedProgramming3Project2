import java.util.*;
import java.lang.UnsupportedOperationException;

public class MyConcurrentBlockingList extends MyConcurrentList {

	public MyConcurrentBlockingList () {
		super();
	}

	@Override
	public void add (String item) throws Exception {
		synchronized (this) {
			super.add(item);
			this.notifyAll();
		}
	}

}