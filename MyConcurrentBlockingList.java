import java.util.*;
import java.lang.UnsupportedOperationException;

public class MyConcurrentBlockingList extends MyConcurrentTreeMap {

	private int currentKey;

	public MyConcurrentBlockingList () {
		super();
		currentKey = 0;
	}

	public Map.Entry<Integer, String> pop () {
		throw new UnsupportedOperationException();
	}

	public String blockingPop () throws InterruptedException {
		//throw new UnsupportedOperationException();
		Map.Entry<Integer, String> output;
		while ((list.size() > 0 && list.firstKey() > currentKey) || (output = super.pop()) == null) {
			try {
				synchronized (this) {
					this.wait();
				}
			} catch (InterruptedException e) {
				throw e;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		currentKey++;
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