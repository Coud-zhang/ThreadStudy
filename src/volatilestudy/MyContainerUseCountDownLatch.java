package volatilestudy;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/11  16:33
 * @Description
 *
 * 使用CountDownLatch的上述程序改进版
 * CountDownLatch实例调用await()会造成调用线程阻塞
 * 当CountDownLatch构造方法中的count减为0时，阻塞的线程会与别的线程进行CPU执行权的竞争，该方法只有让插入线程调用sleep()方法暂时休眠才行，个人不推荐
 *
 */
public class MyContainerUseCountDownLatch {

	public static void main(String[] args) {

		//CountDownLatch实例
		CountDownLatch countDownLatch=new CountDownLatch(1);

		//容器实例
		MyContainer myContainer=new MyContainer();
		//监控线程
		new Thread(()->{
			System.out.println("监控线程启动");
			if (myContainer.size()!=5){
				try {
					countDownLatch.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("监控线程终止");
		}).start();

		//插入线程
		new Thread(()->{
			for (int i = 1; i <10 ; i++) {
				myContainer.add(String.valueOf(i));
				if(myContainer.size()==5){
					//每次调用countDown()方法，构造方法中的值减1
					countDownLatch.countDown();
				}

				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
