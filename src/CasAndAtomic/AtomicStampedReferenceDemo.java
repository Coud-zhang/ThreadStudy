package CasAndAtomic;

import java.sql.Time;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/14  16:11
 * @Description
 *
 * 既然CAS存在AB问题，为了解决ABA问题，可以使用带版本号的原子引用即AtomicStampedReference
 *
 * 该类会为主内存中的每次修改都添加一个版本号，在线程工作内存中值与主内存中值进行比较时不仅比较值是否相等同时会比较版本号是否相同
 */
public class AtomicStampedReferenceDemo {

	public static void main(String[] args) {

		//第一个参数为原子引用，第二个参数为版本号
		AtomicStampedReference<Integer> atomicStampedReference=new AtomicStampedReference<>(100,1);


		//发生ABA修改的线程
		new Thread(()->{
			System.out.println(atomicStampedReference.compareAndSet(100,1,1,2));
			System.out.println(atomicStampedReference.compareAndSet(1,100,2,3));
		}).start();



		//
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		new Thread(()->{
			System.out.println(atomicStampedReference.compareAndSet(100,200,1,2));
		}).start();
	}
}
