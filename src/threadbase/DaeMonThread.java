package threadbase;

import java.util.concurrent.TimeUnit;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/10  14:45
 * @Description 守护线程
 * 守护线程是指为其他线程提供服务的线程，JVM只需要执行完用户线程就会自行关闭，不会等待守护线程执行完
 */
public class DaeMonThread {

	public static void main(String[] args) {

		Thread thread=new Thread(()->{
			while(true){
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("我是守护线程,JVM不需要等待我结束运行");
			}
		});

		//将线程设置为守护线程，一定要在开启该线程前设置
		thread.setDaemon(true);
		thread.start();

		for(int i=0;i<10;i++){
			try {
				TimeUnit.MILLISECONDS.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(i);
		}
	}
}
