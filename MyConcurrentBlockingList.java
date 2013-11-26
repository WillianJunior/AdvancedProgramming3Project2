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
import java.lang.UnsupportedOperationException;

/*
 * Concurrent list that have a blocking pop method.
 * pop is done ordered, by the structure on the superclass
 */

public class MyConcurrentBlockingList extends MyConcurrentTreeMap {

	private int currentKey;

	public MyConcurrentBlockingList () {
		super();
		currentKey = 0;
	}

	public Map.Entry<Integer, String> pop () {
		// keep the user from doing something that do not
		// make any sense: have a blocking list and not use
		// the block feature.
		throw new UnsupportedOperationException();
	}

	public String blockingPop () {
		
		Map.Entry<Integer, String> output;
		
		// check first if any output is still being waited for
		while ((list.size() > 0 && list.firstKey() > currentKey) 
				|| (output = super.pop()) == null) {
			try {
				synchronized (this) {
					// lock the caller untill another add is performed
					this.wait();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		currentKey++;
		return output.getValue();
	}

	@Override
	public void add (String item) throws Exception  {
		// the same principle of pop applies here
		throw new UnsupportedOperationException();
	}

	@Override
	public void add (int id, String item) throws Exception {
		synchronized (this) {
			super.add(id, item);
			// unlock all threads waiting for this add
			this.notifyAll();
		}
	}

}