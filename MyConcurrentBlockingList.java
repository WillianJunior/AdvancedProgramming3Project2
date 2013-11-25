import java.util.*;
import java.lang.UnsupportedOperationException;

public class MyConcurrentBlockingList extends MyConcurrentHashMap {

	public MyConcurrentBlockingList () {
		super();
	}

	public Map.Entry<Integer, String> pop () {
		throw new UnsupportedOperationException();
	}

	public String blockingPop () throws InterruptedException {
		//throw new UnsupportedOperationException();
		Map.Entry<Integer, String> output;
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
		return output.getValue();
	}

	@Override
	public void add (String item) throws Exception  {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add (int id, String item) throws Exception {
		synchronized (this) {
			super.add(id, item);
			this.notifyAll();
		}
	}

}