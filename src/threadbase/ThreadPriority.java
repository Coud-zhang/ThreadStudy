package threadbase;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/11  14:20
 * @Description
 *
 * 线程的优先级从1-10，默认为5，优先级只是线程被调度的概率，并不是优先级越高就一定会优先被调度
 */
public class ThreadPriority {
	public static void main(String[] args) {
		//线程1--优先级为10
		Thread thread1=new Thread(()->{
			System.out.println(Thread.currentThread().getName()+"线程优先级为"+Thread.currentThread().getPriority());
		});
		thread1.setPriority(10);

		//线程1--优先级为3
		Thread thread2=new Thread(()->{
			System.out.println(Thread.currentThread().getName()+"线程优先级为"+Thread.currentThread().getPriority());
		});
		thread2.setPriority(3);

		//线程1--优先级为5
		Thread thread3=new Thread(()->{
			System.out.println(Thread.currentThread().getName()+"线程优先级为"+Thread.currentThread().getPriority());
		});

		thread3.setPriority(5);


		thread1.start();
		thread2.start();
		thread3.start();
	}
}
