package threadsafe;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/13  14:50
 * @Description
 *
 * Lock与synchronized的区别2:
 * Lock可以使用tyrLock()方法尝试获取锁，如果不能获取锁可以转去做其他任务，
 * synchronized如果不能获取锁，则进入等待队列一直等待获取锁
 *
 *
 *
 */
public class TyrLock {

	public static void main(String[] args) {
		TryLockDemo tryLockDemo=new TryLockDemo();

		//线程1:
		new Thread(()->{
			tryLockDemo.m1();
		}).start();

		//线程2
		new Thread(()->{
			tryLockDemo.m2();
		}).start();
	}
}

class TryLockDemo{

	private Lock lock=new ReentrantLock();

	public void m1(){
		lock.lock();
		try {
			for (int i = 0; i <10 ; i++) {
				System.out.println(Thread.currentThread().getName()+"......"+i);
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public void m2(){

		boolean flag=false;
		//指定时间长度与时间单位，当m2想要获得锁时，会等待5秒，如果3秒后还不能获得锁，继续其他的操作
		try {
			flag=lock.tryLock(5,TimeUnit.SECONDS);
			if(flag){
					System.out.println(Thread.currentThread().getName()+"得到了锁");
			}else{
				for (int i = 0; i <10 ; i++) {
					System.out.println("没有得到锁"+"......"+i);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
