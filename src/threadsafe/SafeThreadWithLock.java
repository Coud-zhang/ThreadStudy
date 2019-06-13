package threadsafe;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/11  20:18
 * @Description
 *
 * 使用Lock锁保证线程安全，
 *
 * Lock是一个接口，该接口常用的实现类有ReentrantLock  ReentrantReadWriteLock等实现类
 * Lock与synchronized的区别1:Lock在程序抛出异常时不会自动释放锁，synchronized会自动释放锁
 */
public class SafeThreadWithLock {
	public static void main(String[] args) {

		TicketWithLock ticketWithLock=new TicketWithLock();

		for (int i = 0; i <10 ; i++) {
			new Thread(()->{
				for (int j = 0; j <10 ; j++) {
					ticketWithLock.saleTicket();
				}
			}).start();
		}
	}
}

class TicketWithLock{

	/**
	 * 总票数
	 */
	private int count=100;

	/**
	 * Lock锁
	 */
	private Lock lock=new ReentrantLock();

	public void saleTicket(){
		lock.lock();
		try {
			System.out.println(Thread.currentThread().getName()+"出售了第"+count+"张票");
			count--;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
}
