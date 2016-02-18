package com.zandroid.network.lib.entity;

import android.os.Process;

import com.zandroid.utils.MGAppUtil;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ZThreadFactory {

	/** ����ִ����. */
	public static Executor mExecutorService = null;

	/** �����߳����� . */
	private static final int CORE_POOL_SIZE = 5;

	/** ����߳����� . */
	private static final int MAXIMUM_POOL_SIZE = 64;

	/** ��߳����� . */
	private static final int KEEP_ALIVE = 5;

	/** �̹߳��� . */
	private static final ThreadFactory mThreadFactory = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		public Thread newThread(Runnable r) {
			return new Thread(r, "AbThread #" + mCount.getAndIncrement());
		}
	};

	/** ����. */
	private static final BlockingQueue<Runnable> mPoolWorkQueue = new LinkedBlockingQueue<Runnable>(
			10);

	/**
	 * ��ȡִ����.
	 * 
	 * @return the executor service
	 */
	public static Executor getExecutorService() {
		if (mExecutorService == null) {
			int numCores = MGAppUtil.getNumCores();
			mExecutorService = new ThreadPoolExecutor(
					numCores * CORE_POOL_SIZE, numCores * MAXIMUM_POOL_SIZE,
					numCores * KEEP_ALIVE, TimeUnit.SECONDS, mPoolWorkQueue,
					mThreadFactory);
		}
		Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
		return mExecutorService;
	}

}
