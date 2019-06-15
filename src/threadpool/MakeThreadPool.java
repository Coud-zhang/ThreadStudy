package threadpool;

import java.util.concurrent.*;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/10  10:44
 * @Description TODO
 *
 *
 * 手写一个线程池
 * 为什么在阿里巴巴开发者手册中禁止使用Executors类来创建线程池?
 *
 *LinkedBlockingQueue<>阻塞队列的最大长度为Integer.MAX_VALUE，会大值大量请求堆积，造成oom
 */
public class MakeThreadPool {
	public static void main(String[] args) {

		ExecutorService threadPool=new ThreadPoolExecutor(2,
															5,
															1,
															TimeUnit.SECONDS,
															new LinkedBlockingDeque<>(3),
															Executors.defaultThreadFactory(),
															new ThreadPoolExecutor.CallerRunsPolicy());

		for(int i=0;i<10;i++){
			final int temp=i;
			threadPool.submit(()->{
				System.out.println(Thread.currentThread().getName()+"...."+temp);
			},"AA");
		}

		threadPool.shutdown();


		//得到CPU线程数
		System.out.println(Runtime.getRuntime().availableProcessors());
	}
}
