package CasAndAtomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/14  15:33
 * @Description
 *
 * CAS程序示例
 */
public class CasDemo {

	public static void main(String[] args) {
		/**
		 * AtomicInteger类底层使用的就是CAS原理
		 */
		AtomicInteger atomicInteger=new AtomicInteger(10);
		System.out.println(atomicInteger.compareAndSet(10, 11));

		//getAndIncrement()方法相当于i++操作，不过该方法能够保证原子性
		atomicInteger.getAndIncrement();

		System.out.println(atomicInteger.get());

		System.out.println(atomicInteger.compareAndSet(10,0));
	}
}
