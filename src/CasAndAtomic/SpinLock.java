package CasAndAtomic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/14  16:51
 * @Description
 *
 * CAS中用到了自旋锁，这里手写一个自旋锁(非可重入的自旋锁)
 */
public class SpinLock {

	AtomicReference<Thread> atomicReference=new AtomicReference<>();

	public void addLock(){
		Thread thread=Thread.currentThread();
		System.out.println(thread.getName()+"...................come in");
		while(!atomicReference.compareAndSet(null,thread)){

		}
	}

	public void relaseLock(){
		Thread thread=Thread.currentThread();
		System.out.println(Thread.currentThread().getName()+"..............come out");
		while(!atomicReference.compareAndSet(thread,null)){

		}
	}

	public static void main(String[] args) {

		SpinLock spinLock=new SpinLock();

		new Thread(()->{
			spinLock.addLock();
			//睡眠5秒，为了能够让B线程自旋
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			spinLock.relaseLock();
		},"A").start();


		//睡眠1秒，确保B线程启动时A线程已经启动
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//该线程因为无法A线程在获得锁后睡眠5秒，B线程无法得到锁，一直处于while(){}循环，直到A线程释放锁
		new Thread(()->{
			spinLock.addLock();
			spinLock.relaseLock();
		},"B").start();
	}
}
