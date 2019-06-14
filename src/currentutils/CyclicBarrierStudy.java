package currentutils;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/14  20:10
 * @Description
 *
 * CyclicBarrier 翻译过来即循环屏障(内存屏障)，
 * 让一组线程到达一个屏障(同步点)时被阻塞，直到最后一个线程到达屏障时，屏障才会开门，所有被屏障拦截的线程才能继续执行
 */
public class CyclicBarrierStudy {

	public static void main(String[] args) {

		CyclicBarrier cyclicBarrier=new CyclicBarrier(10,()->{
			System.out.println("终于可以执行了");
		});

		for (int i = 0; i <10 ; i++) {
			final int temp=i;
			new Thread(()->{
				System.out.println(Thread.currentThread().getName()+"........."+temp);
				try {
					cyclicBarrier.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}
				System.out.println("达到屏障值");
			}).start();
		}

	}
}
