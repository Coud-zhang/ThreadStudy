# CountDownLatch 

**是什么**

CountDownLatch 翻译过来即倒计时门闩，让一些线程阻塞，直到另一些线程完成一系列操作后才被唤醒

**主要方法**:

```java
//当一个或多个线程调用await()方法，调用线程会被阻塞，直到count值减为0，被阻塞的线程会被唤醒，继续执行
await();
//其他线程调用countDown()方法会将计数器减1(调用countDown()方法的线程不会被阻塞)
countDown()
```

代码演示:

```java
public class CountDownLatchStudy {

	private static final int  COUNT=6;
	
	
	public static void main(String [] args) throws InterruptedException{
		/**
		 * 关门例子:
		 * 教师中有6个同学，班长必须要等待6个同学都离开教师在锁门
		 */
        //构造该对象时传入的count，只有当count减为0时，被await()方法阻塞的方法才有可能执行
		CountDownLatch latch=new CountDownLatch(COUNT);
		
	for(int i=1;i<=6;i++){
		new Thread(()->{
			System.out.println("离开教师");
            //没调用一次countDown()方法，构造方法中传入的count-1
			latch.countDown();
		}).start();
	}
	//调用await()方法的线程会处于等待状态，等待count的值减为0后才有可能执行
		latch.await();
		System.out.println("关门");
	}
}
```



**使用场景**

（1）开启多个线程分块下载一个大文件，每个线程只下载固定的一截，最后由另外一个线程来拼接所有的分段。

（2）应用程序的主线程希望在负责启动框架服务的线程已经启动所有的框架服务之后再执行。

（3）确保一个计算不会执行，直到所需要的资源被初始化。

# CyclicBarrier 

**是什么**

CyclicBarrier 翻译过来即循环屏障(内存屏障)，让一组线程到达一个屏障(同步点)时被阻塞，直到最后一个线程到达屏障时，屏障才会开门，所有被屏障拦截的线程才能继续执行

**主要方法:**

```java
//构造方法，parties即达到多少以后barrierAction线程才能执行
public CyclicBarrier(int parties, Runnable barrierAction) {}

//构造方法，只需要知道屏障值
public CyclicBarrier(int parties) {}

//让线程进入屏障
await()
```

代码演示:

```java
public class CyclicBarrierStudy {

	public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
		
		//设置屏障值为10，当满足屏障值后才执行输出语句
		CyclicBarrier cyclicBarrier=new CyclicBarrier(10);
		CyclicBarrier cyclicBarrier1=new CyclicBarrier(10,()->{System.out.println("qwertyuiop");});
		for(int i=1;i<=10;i++){
			final int temp=i;
			new Thread(()->{
				System.out.println(temp);
                //将该线程加入屏障的代码部分
				/*try {
                  
					cyclicBarrier.await();
					cyclicBarrier1.await();
                    System.out.println("eeeeeeeeeeeeeeeeee");
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}*/
			}).start();
		}
	}
}
//添加内存屏障输入结果:
1 3 5 7 9 2 4 6 8 10              
eeeeeeeeeeeeeeeeee
eeeeeeeeeeeeeeeeee
eeeeeeeeeeeeeeeeee
eeeeeeeeeeeeeeeeee
eeeeeeeeeeeeeeeeee
eeeeeeeeeeeeeeeeee
eeeeeeeeeeeeeeeeee
eeeeeeeeeeeeeeeeee
eeeeeeeeeeeeeeeeee
eeeeeeeeeeeeeeeeee

//不添加内存屏障结果
2
eeeeeeeeeeeeeeeeee
4
eeeeeeeeeeeeeeeeee
6
eeeeeeeeeeeeeeeeee
8
eeeeeeeeeeeeeeeeee
10
eeeeeeeeeeeeeeeeee
1
eeeeeeeeeeeeeeeeee
3
eeeeeeeeeeeeeeeeee
5
eeeeeeeeeeeeeeeeee
7
eeeeeeeeeeeeeeeeee
9
eeeeeeeeeeeeeeeeee
```



# Semaphore 

**是什么**

主要用于多个共享资源的互斥使用(抢购/秒杀)，并发线程数的控制

**主要方法:**

```java
//构造方法1----permits为共享资源数 
public Semaphore(int permits) {}

//构造方法2----------fair指是否为公平锁，synchronized和lock默认为非公平锁，非公平锁效率更高
 public Semaphore(int permits, boolean fair) {}

//线程抢占一个资源
 public void acquire() throws InterruptedException {}

//线程释放一个资源
 public void release() {}
```



代码展示:

```java
/**
 * 信号量
 * @author Administrator
 *
 */
public class SemaphoreStudy {

	public static void main(String[] args) {
		Semaphore semaphore=new Semaphore(2);
		
		for(int i=1;i<=4;i++){
			new Thread(()->{
				try {
					//抢占一个资源
					semaphore.acquire();
					System.out.println(Thread.currentThread().getName()+"抢占了一个资源");
					//模拟某个线程对该资源持有时间为3秒
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}finally{
					//该线程释放占有的资源
					semaphore.release();
				}
			}).start();
		}
	}
}
```

