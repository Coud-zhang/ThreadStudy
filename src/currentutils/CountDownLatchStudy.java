package currentutils;

import java.util.concurrent.CountDownLatch;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/14  20:01
 * @Description
 *
 *CountDownLatch(门闩),该类的构造函数中传入一个int类型的count,该类的实例调用await()方法会造成调用线程阻塞
 * 直到count值减为0(没调用一次countdown()方法，count的值减1),，被阻塞的线程才能得以继续执行
 */
public class CountDownLatchStudy {

	public static void main(String[] args) {

		/**
		 * 关门例子:
		 * 教师中有6个同学，班长必须要等待6个同学都离开教师在锁门
		 */


		//构造该对象时传入的count，只有当count减为0时，被await()方法阻塞的方法才有可能执行
		CountDownLatch countDownLatch=new CountDownLatch(6);

		for (int i = 0; i <6 ; i++) {
			final int temp=i;
			new Thread(()->{
				System.out.println("学生"+(temp+1)+"离开教室");
				countDownLatch.countDown();
			}).start();
		}


		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("班长关灯离开教室");
	}
}
