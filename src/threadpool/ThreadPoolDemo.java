package threadpool;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/9  20:08
 * @Description 线程池
 *
 * 线程池要点:
 * 1.三种常用的创建线程池的方式
 * 2.ThreadPoolExecutor的7个参数含义
 * 3.ThreadPoolExecutor类中拒绝策略详解
 *
 */


public class ThreadPoolDemo {

	public static void main(String[] args) {

		//在生产中不通过这三个方法来创建线程池，一般自定义线程池
		//创建一个具有10个线程的线程池
		//ExecutorService executorService= Executors.newFixedThreadPool(10);
		//创建只有一个线程的线程池
		//ExecutorService executorService= Executors.newSingleThreadExecutor();
		//创建一个不规定线程数的线程池(线程数会随着任务数自动变化)
		ExecutorService executorService= Executors.newCachedThreadPool();


		try{
			//模仿客户端发来10个任务
			for(int i=0;i<10;i++){
				Future<String> future=executorService.submit(()->{
					System.out.println(Thread.currentThread().getName());
					return "aaaa";
				});
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//关闭线程池
			executorService.shutdown();
		}
	}
}
