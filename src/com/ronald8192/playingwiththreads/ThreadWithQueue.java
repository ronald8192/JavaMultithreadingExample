package com.ronald8192.playingwiththreads;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ThreadWithQueue implements Runnable {
	private static int totalPause = 32; // how many times to pause
	private static int pauseTime = 1000; // ms, how long does the threads will
											// pause each time
	private static ConcurrentLinkedQueue<Integer> clq = new ConcurrentLinkedQueue<Integer>();
	private static long begin = -1; // program begin time
	private static int threadRunning = 0; // count how many threads are running
	private Thread t;
	// the result from queue
	private static List<Integer> theOutput = new ArrayList<Integer>();
	private static List<Integer> synclist = Collections.synchronizedList(theOutput);
	/**
	 * Initialize queue
	 */
	static {
		for (int i = 0; i < totalPause; i++) {
			clq.offer(i);
		}
		System.out.println("clq.size(): " + clq.size());
	}

	/**
	 * Constructor
	 */
	public ThreadWithQueue() {
	}

	@Override
	public void run() {
		if (begin == -1) begin = System.currentTimeMillis();
		while (clq.size() > 0) {
			// ConcurrentLinkedQueue is thread-safe
			// int sthToProcess = pollResourcesFromQueue();
			int sthToProcess = clq.poll();
			try {
				// do the thing
				Thread.sleep(pauseTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synclist.add(sthToProcess);
			//use synchronized method if List is not thread-safe
			//storeResult(sthToProcess);
		}

		// job done, thread will be terminate
		System.out.println(threadCountDecrement() + " threads is still running");
	}

	/**
	 * Start a new thread if queue have any element and thread is not started
	 * before.
	 */
	public void start() {
		if (t == null && clq.size() > 0) {
			t = new Thread(this);
			t.start();
			threadCountIncrement();
			System.out.println("ThreadWithQueue.start()");
		}
	}

	/**
	 * Use this if the queue is not thread-safe. Get and remove next element
	 * from queue.
	 * 
	 * @return next queue element
	 */
	@SuppressWarnings("unused")
	synchronized private static int pollResourcesFromQueue() {
		return clq.poll();
	}

	/**
	 * Store the result to non-thread-safe List.
	 * 
	 * @param result
	 */
	@SuppressWarnings("unused")
	synchronized private static void storeResult(int result) {
		theOutput.add(result);
	}

	/**
	 * Decrement thread count. Print time token and all the result when all
	 * threads terminated.
	 * 
	 * @return number of threads are running.
	 */
	synchronized private static int threadCountDecrement() {
		if (--threadRunning == 0) {
			System.out.println("Time token:" + (System.currentTimeMillis() - begin));
			System.out.println(ThreadWithQueue.synclist.toString());
		}
		return threadRunning;

	}

	/**
	 * Increment thread count.
	 * 
	 * @return number of threads are running.
	 */
	synchronized private static int threadCountIncrement() {
		return ++threadRunning;
	}
}
