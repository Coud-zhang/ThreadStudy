package threadbase;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/10  10:08
 * @Description TODO
 *
 * 创建线程的四种方式
 * 1.继承Thread类
 * 2.实现Runnable类
 * 3.实现Callable类
 * 4，线程池(推荐)
 */
public class ExtendsThread{

	public static void main(String[] args) {

		ThreadTest threadTest=new ThreadTest();
		threadTest.start();

		ThreadTest1 threadTest1=new ThreadTest1();
		Thread thread=new Thread(threadTest1);
		thread.start();

		/**
		 * 使用lambda简介的创建线程
		 *
		 */

		new Thread(()->{
			for(int i=0;i<10;i++){
				System.out.println(Thread.currentThread().getName()+"......."+i);
			}
		}).start();
	}
}

/**
 * 通过继承Thread类创建线程
 */
class ThreadTest extends  Thread{

	@Override
	public void run(){

		for(int i=0;i<10;i++){
			System.out.println(Thread.currentThread().getName()+"......."+i);
		}
	}
}

/**
 * 实现Runnable接口的常规方式创建线程
 */
class ThreadTest1 implements Runnable{

	@Override
	public void run() {
		for(int i=0;i<10;i++){
			System.out.println(Thread.currentThread().getName()+"........"+i);
		}
	}
}
