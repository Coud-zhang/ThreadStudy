package threadbase;

import java.util.concurrent.*;

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

		//继承Thread类
		ThreadTest threadTest=new ThreadTest();
		threadTest.start();

		//实现Runnable接口
		ThreadTest1 threadTest1=new ThreadTest1();
		Thread thread=new Thread(threadTest1);
		thread.start();


		//使用lambda实现runnable接口简洁的创建线程
		new Thread(()->{
			for(int i=0;i<10;i++){
				System.out.println(Thread.currentThread().getName()+"......."+i);
			}
		}).start();


		//实现Callable<V>接口
		ThreadTest2 threadTest2=new ThreadTest2();
		FutureTask<String> stringFutureTask=new FutureTask<>(threadTest2);
		if(stringFutureTask.isDone()){
			try {
				String result=stringFutureTask.get();
				System.out.println(result);
			} catch (InterruptedException |ExecutionException e) {
				e.printStackTrace();
			}
		}

		//通过Lambda简洁方式并且实现Callable接口创建线程
		FutureTask<String> stringFutureTask1=new FutureTask<>(()->{
			return "Hello World";
		});


		//通过线程池创建新线程
		ExecutorService threadpool=new ThreadPoolExecutor(2,5,1,TimeUnit.SECONDS,new LinkedBlockingDeque<>(3),Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
		for(int i=0;i<4;i++){
			//通过submit()提交一个任务
			threadpool.submit(()->{
				System.out.println("通过线程池创建新线程");
			});
		}
		//关闭线程池
		threadpool.shutdown();
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

/**
 * 实现Callable<V>接口创建线程
 */
class ThreadTest2 implements Callable<String>{

	@Override
	public String call() throws Exception {
		return "Hello World";
	}
}
