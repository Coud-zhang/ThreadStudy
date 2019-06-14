package CasAndAtomic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/14  15:49
 * @Description
 *
 * CAS存在ABA问题:
 *
 * ABA问题即:假设存在两个线程One Two,两个线程同时从主内存中相同位置(V)拷贝变量A到自己的工作内存中
 *
 * 线程Two未抢到CPU执行权，在这一段时间内，One线程把V位置的值改为B，又从B改为A，线程Two就会认为主内存中的值没有其他线程进行过改变，从而造成问题
 */
public class Aba {

	public static void main(String[] args) {

		AtomicReference<Integer> atomicReference=new AtomicReference<>(100);

		System.out.println(Thread.currentThread().getName()+"未修改前，值为"+atomicReference.get());

		//第一个线程，进行ABA操作
		new Thread(()->{
			System.out.println(Thread.currentThread().getName()+"线程第一次修改，结果为:"+atomicReference.compareAndSet(100,1));

			System.out.println(Thread.currentThread().getName()+"线程第二次修改，结果为:"+atomicReference.compareAndSet(1,100));
		},"A").start();


		new Thread(()->{
			//这里睡眠1秒，保证线程1的操作先完成
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName()+"线程执行前，其他线程发生ABA修改，但是该线程修改结果为:"+atomicReference.compareAndSet(100,200));
			System.out.println("最终的结果为:"+atomicReference.get());
		},"B").start();
	}
}
