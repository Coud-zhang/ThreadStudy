package threadbase;

import java.util.concurrent.TimeUnit;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/11  10:42
 * @Description 常用操作线程的方法
 *
 * 1、添加标志位来停止线程运行的方法
 * 2、sleep()方法，暂停线程的执行，调用该方法时，线程不会释放锁
 *
 */
public class OperationThread {

	public static void main(String[] args) throws InterruptedException {

		System.out.println("---------------------停止线程执行--------------");
		MyThread my=new MyThread();

		Thread thread1=new Thread(()->{
			my.run();
		});

		thread1.start();
		//让主线程睡眠2秒
		try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		my.flag=false;
	}
}

/**
 * 停止线程的stop()方法已经过时，可以通过添加标志位来停止线程的运行
 */
class MyThread{

	/**
	 * 保证flag在线程间的可见性
	 */
	volatile boolean flag=true;

	public void run() {
		System.out.println("线程开始运行");
		while(flag){

			//注意这里不要添加输出语句，因为输出语句sout使用了synchronized代码块，在释放锁时会把共享变量刷新回主内存
		}
		System.out.println("线程终止");
	}
}