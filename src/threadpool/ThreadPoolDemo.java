package threadpool;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/9  20:08
 * @Description 线程池
 */
public class ThreadPoolDemo {

	public static void main(String[] args) {

		//创建一个具有五个线程的线程池
		//ExecutorService executorService= Executors.newFixedThreadPool(10);
		//创建只有一个线程的线程池
		//ExecutorService executorService= Executors.newSingleThreadExecutor();
		//创建一个不规定线程数且待缓存的线程池
		ExecutorService executorService= Executors.newCachedThreadPool();

		//第一个具有五个线程的线程池
		try{
			//模仿是个线程
			for(int i=0;i<10;i++){
				executorService.execute(()->{
					System.out.println(Thread.currentThread().getName());
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
