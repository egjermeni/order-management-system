package edd.example.java.util;

import edd.example.java.thread.MyBlockingQueue;
import edd.example.java.thread.xenos.clbq.LinkedBlockingQueueConcurrent;
import edd.example.java.thread.xenos.clbq.LinkedBlockingQueueConcurrent.WaitType;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Comparator;
import java.util.concurrent.*;

public class ExecutorBuilder {
	// init defaults
	private Thread thread;
	private ExecutorType type = ExecutorType.LINKED_BLOCKING_QUEUE;
	private String namePattern = "pool-%d";
	private boolean daemone = false;
	private int priority = Thread.NORM_PRIORITY;
	private int nThreads = 1;
	private int corePoolSize = 1;
	private int maximumPoolSize = 62510; 	//62510; cat /proc/sys/kernel/threads-max
    private long keepAliveTime = TimeUnit.SECONDS.toNanos(10);
    //private long keepAliveTime = 0L;
	private TimeUnit timeUnit = TimeUnit.NANOSECONDS;
	private int capacityQueue = 1000;
	private boolean fairQueue = false;
	private Comparator comparator = null;
	private WaitType waitStrategy = WaitType.LOCK_WAIT_STRATEGY;
	private int consumer = 10;
	private boolean asyncMode = true;
	private int parallelism = Runtime.getRuntime().availableProcessors();
	//private RejectedExecutionHandler rejectedExecutionHandler = new AbortPolicy();
	private RejectedExecutionHandler rejectedExecutionHandler = new RequeuePolicy();
    //private UncaughtExceptionHandler uncaughtExceptionHandler = new MyUncaughtExceptionHandler();
    private UncaughtExceptionHandler uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

	public ExecutorBuilder type(ExecutorType type) {
		this.type = type;
		return this;
	}

	public ExecutorBuilder namePattern(String namePattern) {
		this.namePattern = namePattern;
		return this;
	}

	public ExecutorBuilder daemone(boolean daemone) {
		this.daemone = daemone;
		return this;
	}

	public ExecutorBuilder threads(int nThreads) {
		this.nThreads = nThreads;
		return this;
	}

	public ExecutorBuilder priority(int priority) {
		this.priority = priority;
		return this;
	}

	public ExecutorBuilder corePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
		return this;
	}

	public ExecutorBuilder maximumPoolSize(int maximumPoolSize) {
		this.maximumPoolSize = maximumPoolSize;
		return this;
	}

	public ExecutorBuilder keepAliveTime(long keepAliveTime) {
		this.keepAliveTime = keepAliveTime;

		return this;
	}

//	public ExecutorBuilder timeUnit(TimeUnit timeUnit) {
//		this.timeUnit = timeUnit;
//		return this;
//	}

	public ExecutorBuilder capacityQueue(int capacityQueue) {
		this.capacityQueue = capacityQueue;
		return this;
	}

	public ExecutorBuilder fairQueue(boolean fairQueue) {
		this.fairQueue = fairQueue;
		return this;
	}

	public ExecutorBuilder comparator(Comparator comparator) {
		this.comparator = comparator;
		return this;
	}

	public ExecutorBuilder waitStrategy(WaitType waitStrategy) {
		this.waitStrategy = waitStrategy;
		return this;
	}

	public ExecutorBuilder consumer(int consumer) {
		this.consumer = consumer;
		return this;
	}

	public ExecutorBuilder parallelism(int parallelism) {
		this.parallelism = parallelism;
		return this;
	}

	public ExecutorBuilder asyncMode(boolean asyncMode) {
		this.asyncMode = asyncMode;
		return this;
	}

	public ExecutorBuilder rejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler) {
		this.rejectedExecutionHandler = rejectedExecutionHandler;
		return this;
	}

    public ExecutorBuilder uncaughtExceptionHandler(UncaughtExceptionHandler uncaughtExceptionHandler) {
        if(uncaughtExceptionHandler != null){
            this.uncaughtExceptionHandler = uncaughtExceptionHandler;
        }
        return this;
    }

    public ExecutorService buid() {
		ExecutorService ex = prebuid();
		ThreadPoolExecutor tx = getThreadPoolExecutor(ex); 
		if(tx != null){
			tx.allowCoreThreadTimeOut(true);
			//BlockingQueue<Runnable> queue = tx.getQueue();
            //System.out.println("---------------------------------------");
            //System.out.println(tx.toString());
            //System.out.println("---------------------------------------");
		}
		return ex;
	}


	private ExecutorService prebuid() {
		ThreadFactory threadFactory = null;
		try {
			threadFactory = new ThreadFactoryBuilder()
            .setNameFormat(namePattern)
            .setDaemon(daemone)
            .setPriority(priority)
            //.setUncaughtExceptionHandler(uncaughtExceptionHandler)
            .build();
		} catch (RuntimeException e) {
			//e.printStackTrace();
            System.out.println("WARNING: could not build threadFactory. Using Executors.defaultThreadFactory. "+e.getMessage());
			threadFactory = Executors.defaultThreadFactory();
		}

		switch (type) {
			case SINGLE_THREAD:
				return Executors.newSingleThreadExecutor(threadFactory);

			case FIXED_THREAD_POOL:
				return Executors.newFixedThreadPool(nThreads, threadFactory);

			case CACHED_THREAD_POOL:
				return Executors.newCachedThreadPool(threadFactory);

			case SCHEDULED_THREAD_POOL:
				return Executors.newScheduledThreadPool(nThreads, threadFactory);

			case WORK_STEALING_POOL:
				return Executors.newWorkStealingPool();

			case SYNCHRONOUS_QUEUE:
				return new ThreadPoolExecutor(
                    corePoolSize,
                    maximumPoolSize,
                    keepAliveTime,
                    timeUnit,
                    new SynchronousQueue<Runnable>(fairQueue),
                    threadFactory,
                    rejectedExecutionHandler);

			case LINKED_BLOCKING_QUEUE:
				return new ThreadPoolExecutor(
					corePoolSize,
					maximumPoolSize,
					keepAliveTime,
					timeUnit,
					new LinkedBlockingQueue<Runnable>(capacityQueue),
					threadFactory,
					rejectedExecutionHandler);

			case My_BLOCKING_QUEUE:
				return new ThreadPoolExecutor(
					corePoolSize,
					maximumPoolSize,
					keepAliveTime,
					timeUnit,
					new MyBlockingQueue<Runnable>(),
					threadFactory,
					rejectedExecutionHandler);

			case LINKED_BLOCKING_QUEUE_CONCURRENT:
				return new ThreadPoolExecutor(
					corePoolSize,
					maximumPoolSize,
					keepAliveTime,
					timeUnit,
					new LinkedBlockingQueueConcurrent<>(waitStrategy, consumer),
					threadFactory,
					rejectedExecutionHandler);

			case ARRAY_BLOCKING_QUEUE:
				return new ThreadPoolExecutor(
                    corePoolSize,
                    maximumPoolSize,
                    keepAliveTime,
                    timeUnit,
                    new ArrayBlockingQueue<Runnable>(capacityQueue, fairQueue),
                    threadFactory,
                    rejectedExecutionHandler);

			case PRIORITY_BLOCKING_QUEUE:
				return new ThreadPoolExecutor(
                    corePoolSize,
                    maximumPoolSize,
                    keepAliveTime,
                    timeUnit,
                    new PriorityBlockingQueue<>(capacityQueue, comparator),
                    threadFactory,
                    rejectedExecutionHandler);

			case FORK_JOIN_POOL:
				return new ForkJoinPool(
                    parallelism,
                    ForkJoinPool.defaultForkJoinWorkerThreadFactory,
                    uncaughtExceptionHandler,
                    asyncMode);

			default:
				return Executors.newFixedThreadPool(nThreads, threadFactory);
		}
	}

	public enum ExecutorType {
		FIXED_THREAD_POOL,
		CACHED_THREAD_POOL,
		SINGLE_THREAD,
		SCHEDULED_THREAD_POOL,
		SYNCHRONOUS_QUEUE,
		LINKED_BLOCKING_QUEUE,
		LINKED_BLOCKING_QUEUE_CONCURRENT,
		My_BLOCKING_QUEUE,
		ARRAY_BLOCKING_QUEUE,
		PRIORITY_BLOCKING_QUEUE,
		FORK_JOIN_POOL,
		WORK_STEALING_POOL
	}

    //https://stackoverflow.com/questions/44650732/how-to-implement-blocking-thread-pool-executor
    public class RequeuePolicy implements RejectedExecutionHandler{
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                // block until there's room
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RejectedExecutionException("Producer thread interrupted", e);
            }
        }
    }


    public class MyUncaughtExceptionHandler implements  UncaughtExceptionHandler{
        @Override
        public void uncaughtException(Thread t, Throwable e) {

        }
    }

    public static ThreadPoolExecutor getThreadPoolExecutor(final ExecutorService ex){
        ThreadPoolExecutor tx = null;
        if(ex instanceof ThreadPoolExecutor){
            tx = (ThreadPoolExecutor)ex;
        }
        return tx;
    }

}


