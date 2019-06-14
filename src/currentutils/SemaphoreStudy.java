package currentutils;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/14  20:21
 * @Description
 *
 * 主要用于多个共享资源的互斥使用(抢购/秒杀)，并发线程数的控制
 */
public class SemaphoreStudy {

	public static void main(String[] args) {
		Semaphore semaphore=new Semaphore(2);

		for (int i = 1; i <= 4; i++) {
			new Thread(()->{
				try {
					/**
					 * 如果把System.out.println()放在acquire()方法前面，程序会出现问题，不知为何
					 */
					   System.out.println(Thread.currentThread().getName()+"抢占一个资源");
						semaphore.acquire();
						//System.out.println(Thread.currentThread().getName()+"抢占一个资源");
						//模仿某个用户对资源占用4秒钟
						Thread.sleep(4000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
					semaphore.release();
				}
			}).start();
		}
	}
}
