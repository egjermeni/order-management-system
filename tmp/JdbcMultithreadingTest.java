package edd.example.java.thread;


import edd.example.java.thread.dao.AccountDao;
import edd.example.java.thread.dao.AccountDaoJdbcImpl;
import edd.example.java.thread.dao.MysqlDbConnector;
import edd.example.java.thread.domain.Account;
import edd.example.java.thread.domain.AccountNotThreadSafe;
import edd.example.java.util.ExecutorBuilder;
import edd.example.java.util.TestUtil;
import org.junit.*;
import org.junit.rules.Timeout;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.atomic.AtomicBoolean;

public class JdbcMultithreadingTest {
	@Rule
	public Timeout globalTimeout = new Timeout(60, TimeUnit.MINUTES);
	private final AccountDao accountDao = new AccountDaoJdbcImpl();

//	private static final MysqlDbConnector connector;
//	static {
//		connector = getConnector();
//	}
//	private static MysqlDbConnector getConnector(){
//		final String username = "edmond";
//		final String password = "Password1";
//		final String host = "127.0.0.1";
//		final String port = "3306";
//		final String schema = "edmond";
//		MysqlDbConnector connector = new MysqlDbConnector(username, password, host, port, schema);
//		return connector;
//	}

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	//////////////////////////////////////////////////////////////////
	public static void main(String[] args) throws Exception {
		JdbcMultithreadingTest test = new JdbcMultithreadingTest();
		//System.out.println("#### args: "+Arrays.toString(args));
		
		test.test_Producer_Consumer_2();
		//test.test_Jdbc_Create_Account_Thread_Single();
		//test.test_Jdbc_Update_Account_Thread_Single();
		//test.test_Jdbc_Get_All_Account_Thread_Single();
	}
	//////////////////////////////////////////////////////////////////


	///////////////////  Multi Thread Test ///////////////////////////
	@Test
	public void test_Producer_Consumer_2() throws Exception {
		System.out.println("----- Test start ---");
		final AtomicBoolean isCompleted = new AtomicBoolean(false);
		// reste balance to 0.00
		accountDao.update(new AccountNotThreadSafe(1, 0));

		//----------------- Configs ---------------------------
		// common config
		//final long nElements = 1000000000L;
		final long nElements = 1000000L;
		final long duration = TimeUnit.MINUTES.toNanos(1);
		final long timeout = TimeUnit.SECONDS.toMillis(10);
		final long reportDelay = TimeUnit.SECONDS.toMillis(1);
		final int queueSize = 500000;
		final BlockingQueue<Account> queue = new LinkedBlockingQueue<>(queueSize);
		//final BlockingQueue<Account> queue 		=new ArrayBlockingQueue<>(queueSize);
		// producer configq
		final ExecutorBuilder.ExecutorType producerExecutorType = ExecutorBuilder.ExecutorType.ARRAY_BLOCKING_QUEUE;
		final int producerCapacityQueue = 500000;
		final int producerCorePoolSize = 1;
		final int producerMaximumPoolSize = 1;
		final long producerKeepAliveTime = Long.MAX_VALUE;
		final long produceDelay = TimeUnit.SECONDS.toMillis(0);
		// consumer config
		final ExecutorBuilder.ExecutorType consumerExecutorType = ExecutorBuilder.ExecutorType.ARRAY_BLOCKING_QUEUE;
		final int consumerCapacityQueue = 500000;
		final int consumerCorePoolSize = 100;
		final int consumerMaximumPoolSize = 200;
		final long consumerKeepAliveTime = Long.MAX_VALUE;
		final long consumeDelay = TimeUnit.MILLISECONDS.toNanos(0);
//		case ARRAY_BLOCKING_QUEUE:
//		return new ThreadPoolExecutor(
//				corePoolSize,
//				maximumPoolSize,
//				keepAliveTime,
//				timeUnit,
//				new ArrayBlockingQueue<Runnable>(capacityQueue, fairQueue),
//				threadFactory,
//				rejectedExecutionHandler);


		//----------------- Configs ---------------------------

		// create queue
		//fillQueue(nElements, queue);
		System.out.println("Queue.size=" + queue.size());
		//TimeUnit.SECONDS.sleep(10);

		// create producer executor threads
		ExecutorService producerEs = new ExecutorBuilder()
				.type(producerExecutorType)
				.namePattern("Producer-%d")
				.capacityQueue(producerCapacityQueue) //cap ram usage
				.corePoolSize(producerCorePoolSize)
				.corePoolSize(producerCorePoolSize)
				.maximumPoolSize(producerMaximumPoolSize)
				.keepAliveTime(producerKeepAliveTime)
				.buid();

		// create consumer executor threads
		ExecutorService consumerEs = new ExecutorBuilder()
				.type(consumerExecutorType)
				.namePattern("Consumer-%d")
				.capacityQueue(consumerCapacityQueue) //cap ram usage
				.corePoolSize(consumerCorePoolSize)
				.maximumPoolSize(consumerMaximumPoolSize)
				.keepAliveTime(consumerKeepAliveTime)
				.buid();

		List<Runnable> tasks = new ArrayList<>();
		//tasks.add(new TestUtil.ExecutorStateTask(reportDelay, producerEs));
		tasks.add(new TestUtil.ExecutorStateTask(reportDelay, consumerEs));
		tasks.add(new TestUtil.QueueStateTask(reportDelay, isCompleted, queue));
		tasks.add(new ProducersTask(nElements, duration, produceDelay, queue, producerEs));
		tasks.add(new ConsumersTask(nElements, consumeDelay, timeout, isCompleted, queue, consumerEs));
		runTasks(tasks);

		System.out.println("Queue.size=" + queue.size());
		//TimeUnit.MINUTES.sleep(10);
		System.out.println("----- Test end -----");
	}

	/////////////////////////////////////////////////////////////////////////

	private Account produce(final long count, final long delay) throws Exception {
		if (delay > 0){
			//TestUtil.useCpu(TimeUnit.MILLISECONDS.toNanos(delay), 0, 0);
		}
		Account account = new AccountNotThreadSafe(1L, (int)count);
		//Account account = new AccountNotThreadSafe((int)count);
		//System.out.println("produced, "+account.toString()+", "+Thread.currentThread().getName());
		return account;
	}

	private void consume(final Account account, final long delay) throws Exception {
		if (delay > 0){
			//TestUtil.useCpu(TimeUnit.MILLISECONDS.toNanos(delay), 0, 0);
			TestUtil.useCpu(0, TimeUnit.NANOSECONDS.toMillis(delay), 0);
			//TestUtil.useCpu(0, delay, 0);
		}
		accountDao.update(account);
		//accountDao.get(account.getId());
		//accountDao.create(account);

		//System.out.println("consumed, "+account.toString()+", "+Thread.currentThread().getName());
	}

	private void runTasks(List<Runnable> tasks) {
		ExecutorService rootEs = new ExecutorBuilder()
		//.type(ExecutorType.ARRAY_BLOCKING_QUEUE)
		.type(ExecutorBuilder.ExecutorType.LINKED_BLOCKING_QUEUE)
		.namePattern("Root-%d")
		.corePoolSize(tasks.size())
		.maximumPoolSize(tasks.size())
		.buid();
		for (Runnable task : tasks) {
			rootEs.execute(task);
		}
		TestUtil.shutdownExecutor(rootEs);
	}

	public class ProducerTask implements Runnable {
		private final BlockingQueue<Account> queue;
		private final long count;
		private final long delay;

		public ProducerTask(final long delay, final BlockingQueue queue) {
			this.count = 0;
			this.delay = delay;
			this.queue = queue;
		}

		public ProducerTask(final long count, final long delay, final BlockingQueue queue) {
			this.count = count;
			this.delay = delay;
			this.queue = queue;
		}

		@Override
		public void run() {
			try {
				//System.out.println("produced: "+t.toString());
				//queue.add(t);
				//queue.offer(t));
				//Account account = produce(delay);
				Account account = produce(count, delay);
				queue.put(account);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public class ProducersTask implements Runnable {
		private final long nElements;
		private final long duration;
		private final long produceDelay;
		private final BlockingQueue<Account> queue;
		private final ExecutorService producerEs;

		public ProducersTask(
				final long nElements,
				final long duration,
				final long produceDelay,
				final BlockingQueue queue,
				final ExecutorService producerEs
		) {
			this.nElements = nElements;
			this.duration = duration;
			this.produceDelay = produceDelay;
			this.queue = queue;
			this.producerEs = producerEs;
		}

		@Override
		public void run() {
			try {
				long count = 0;
				while (count < nElements) {
					producerEs.execute(new ProducerTask(count, produceDelay, queue));
					//producerEs.execute(new ProducerTask(produceDelay, queue));
					count++;
				}
				TestUtil.shutdownExecutor(producerEs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public class ConsumerTask implements Runnable {
		private final long consumeDelay;
		private final long timeout;
		private final BlockingQueue<Account> queue;

		public ConsumerTask(
				final long consumeDelay,
				final long timeout,
				final BlockingQueue queue
		) {
			this.consumeDelay = consumeDelay;
			this.timeout = timeout;
			this.queue = queue;
		}

		@Override
		public void run() {
			Account account = null;
			try {
				//Account account = queue.peek();
				//Account account = queue.remove();
				//Account account = queue.poll();
				//Account account = queue.take();
				account = queue.poll(timeout, TimeUnit.MILLISECONDS);
				consume(account, consumeDelay);
			} catch (Throwable re) {
				re.printStackTrace();
			}
		}
	}

	public class ConsumersTask implements Runnable {
		private final long iterations;
		private final long consumeDelay;
		private final long timeout;
		private final AtomicBoolean isCompleted;
		private final BlockingQueue<Account> queue;
		private final ExecutorService consumerEs;
		private final LongAdder iteration;

		public ConsumersTask( 
				final long iterations,
				final long consumeDelay,
				final long timeout,
				final AtomicBoolean isCompleted,
				final BlockingQueue queue,
				final ExecutorService consumerEs
		) {
			this.iterations = iterations;
			this.iteration = new LongAdder();
			this.consumeDelay = consumeDelay;
			this.timeout = timeout;
			this.isCompleted = isCompleted;
			this.queue = queue;
			this.consumerEs = consumerEs;
		}

		@Override
		public void run() {
			try {
				while (iteration.longValue() < iterations) {
					consumerEs.execute(new ConsumerTask(consumeDelay, timeout, queue));
					iteration.increment();
				}
				TestUtil.shutdownExecutor(consumerEs);
				isCompleted.set(true);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
	///////////////////  Multi Thread Test ///////////////////////////
	//////////////////////////////////////////////////////////////////



	///////////////////////////////////////////////////////////////////
	///////////////////  Single Thread Test ///////////////////////////
	@Test
	public void test_Jdbc_Create_Account_Thread_Single() throws Exception {
		System.out.println("----- Test start ---");
		// given
		// when
		accountDao.create(new AccountNotThreadSafe(0, 55));
		// then
		System.out.println("----- Test end -----");
	}

	@Test
	public void test_Jdbc_Update_Account_Thread_Single() throws Exception {
		System.out.println("----- Test start ---");
		// given
		// when
		accountDao.update(new AccountNotThreadSafe(1, 5555));
		// then
		System.out.println("----- Test end -----");
	}

	@Test
	public void test_Jdbc_Get_Account_Thread_Single() throws Exception {
		System.out.println("----- Test start ---");
		// given
		// when
		Account account = accountDao.get(1);
		// then
		Assert.assertEquals(1, account.getId());
		Assert.assertEquals(333, account.getBalance());
		System.out.println("----- Test end -----");
	}

	@Test
	public void test_Jdbc_Get_All_Account_Thread_Single() throws Exception {
		System.out.println("----- Test start ---");
		// given
		// when
		List<Account> accounts = accountDao.getAll();
		// then
		for (Account a:	accounts ) {
			System.out.println(a.toString());
		}
		//Assert.assertEquals(1, accounts.size());
		System.out.println("----- Test end -----");
	}
	///////////////////  Single Thread Test ///////////////////////////

	@Test
	public void test_Jdbc_Delete_From_Account_Thread_Single() throws Exception {
		System.out.println("----- Test start ---");
		// given
		// when
		accountDao.delete(new AccountNotThreadSafe(4));
		// then
		System.out.println("----- Test end -----");
	}

}
