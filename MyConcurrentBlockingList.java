import java.util.*;
import java.lang.UnsupportedOperationException;

public class MyConcurrentBlockingList extends MyConcurrentList {

	public MyConcurrentBlockingList () {
		super();
	}

	@Override
	public void add (String item) throws Exception {
		synchronized (list) {
			super.add(item);
			synchronized (this) {
				this.notifyAll();
			}
		}
	}

}