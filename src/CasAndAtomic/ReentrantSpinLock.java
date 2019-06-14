package CasAndAtomic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/14  17:03
 * @Description
 *
 * 可重入自旋锁
 */
public class ReentrantSpinLock {
	AtomicReference<Thread> atomicReference=new AtomicReference<>();

	int count=0;

	public void addLock(){
		Thread thread=Thread.currentThread();
		System.out.println(thread.getName()+"...................come in");
		if(thread==atomicReference.get()){
			count++;
			System.out.println(Thread.currentThread().getName()+"第"+count+"次获得锁");
			return;
		}
		while(!atomicReference.compareAndSet(null,thread)){

		}
	}

	public void relaseLock(){
		Thread thread=Thread.currentThread();
		if(thread==atomicReference.get()){
			if(count>0){
				count--;
			}else{
				atomicReference.compareAndSet(thread,null);
			}
		}
	}

	public static void main(String[] args) {


		ReentrantSpinLock s=new ReentrantSpinLock();

		new Thread(()->{
			s.addLock();
			s.addLock();
			s.addLock();
			//睡眠5秒，为了能让线程B自旋
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			s.relaseLock();
			s.relaseLock();
			s.relaseLock();
		},"A").start();

		//main线程睡眠1秒，确保B线程启动时A线程已经启动
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		new Thread(()->{
			s.addLock();
			s.relaseLock();
		},"B").start();
	}
}
