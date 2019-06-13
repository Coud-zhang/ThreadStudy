package threadsafe;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/13  15:27
 * @Description
 *
 * synchronized与Lock的区别:
 *
 * synchronized为非公平锁，Lock默认为非公平锁，可以指定为公平锁
 * 公平锁即FIFO，获取锁的顺序与获得锁的顺序相同，非公平锁允许加塞现象的发生，非公平锁与公平锁相比，非公平锁效率更高
 */
public class FairReentrantLock {

	/**
	 * 通过构造方法指定为公平锁
	 */
	Lock lock=new ReentrantLock(true);



	public static void main(String[] args) {
		FairReentrantLock fairReentrantLock=new FairReentrantLock();

		new Thread(()->{
			System.out.println(Thread.currentThread().getName()+"启动");
			fairReentrantLock.add();
		},"线程1").start();

		new Thread(()->{
			System.out.println(Thread.currentThread().getName()+"启动");
			fairReentrantLock.add();
		},"线程2").start();

	}

	public void add() {
		for (int i = 0; i < 100; i++) {
		lock.lock();
		try {
				System.out.println(Thread.currentThread().getName() + "输出。。。。" + i);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	}
}
