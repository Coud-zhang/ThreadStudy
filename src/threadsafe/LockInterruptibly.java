package threadsafe;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/13  15:15
 * @Description
 *
 * synchronized与Lock的区别三:
 *
 * 某个线程通过调用lockInterruptibly()去获取锁，但是锁长时间被另外线程占用时，该线程可以被打断，synchronized不可以被打断
 * 打断某个线程时会抛出java.lang.InterruptedException异常
 */
public class LockInterruptibly {

	public static void main(String[] args) {

		Lock lock=new ReentrantLock();
		//线程1
		new Thread(()->{
			lock.lock();
			try {
				System.out.println(Thread.currentThread().getName()+"开始执行");
				TimeUnit.SECONDS.sleep(4);
				System.out.println(Thread.currentThread().getName()+"执行完毕");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		},"AA").start();

		//线程2
		Thread BB=new Thread(()->{
			try {
				lock.lockInterruptibly();
				System.out.println(Thread.currentThread().getName()+"开始执行");
			} catch (InterruptedException e) {
				System.out.println(Thread.currentThread().getName()+"被打断");
				e.printStackTrace();
			}finally {
				System.out.println(Thread.currentThread().getName()+"释放了锁");
				lock.unlock();
			}
		},"BB");

		BB.start();


		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//打断线程BB
		BB.interrupt();
	}
}
