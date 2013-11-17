import java.util.*;

public class ThreadSpawner implements Runnable {
	
	private long workersNum;
	private volatile MyConcurrentList workQ;

	public ThreadSpawner (long workersNum, MyConcurrentList workQ) {
		this.workersNum = workersNum;
	}

	public void run () {
		
	}

}