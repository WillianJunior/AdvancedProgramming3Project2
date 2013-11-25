import java.util.*;
import java.lang.UnsupportedOperationException;

public class MyConcurrentBlockingList extends MyConcurrentList {

	public MyConcurrentBlockingList () {
		super();
	}

	public void finish () {}

	public String pop () {
		throw new UnsupportedOperationException();
	}

	public String blockingPop () throws InterruptedException {
		//throw new UnsupportedOperationException();
		String output;
		while ((output = super.pop()) == null)
			try {
				synchronized (this) {
					this.wait();
				}
			} catch (InterruptedException e) {
				throw e;
			} catch (Exception e) {
				e.printStackTrace();
			}
		return output;
	}

	//public void pop (Object o) {}

	@Override
	public void add (String item) throws Exception {
		//throw new UnsupportedOperationException();
		synchronized (this) {
			super.add(item);
			this.notifyAll();
		}
	}

}