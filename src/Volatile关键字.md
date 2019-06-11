# Volatile关键字

## 简介

volatile是JVM提供的轻量级的同步机制,volatile与synchronized相比具有更高的效率

## 特性

保证可见性

不保证原子性 

保证有序性(禁止指令重排)

## 可见性分析

**是什么**

可见性是当某个线程修改自己工作空间的值并写回主内存后通知其他线程值已经发生改变

### JMM内存模型

- 内存模型基本介绍：

  JMM运行程序的实体是线程，而每个线程创建时jvm都会为其创建一个工作内存(栈内存)，工作内存是每个线程的私有数据区域，而Java内存模型中规定所有变量存储在主内存(内存条)中，主内存是共享内存区域，所有线程都可以访问，但**线程对变量的操作必须在工作内存中完成，首先要将内存从主内存拷贝到自己的工作内存中，然后对变量进行操作，操作完成后再将变量写会主内存，不能直接操作主内存中的变量**，各个线程中的工作内存中存储着主内存中变量的拷贝，因此不同的线程无法访问对方的工作内存，线程间的通信必须通过主内存来完成

- JMM(Java Memory Model)即Java内存模型是一种抽象的概念并不真实存在，它描述的是一组规则或规范，通过规范定义了程序中各个变量(包括实例字段，静态字段和构成数组对象的元素)的访问方式。

- JMM关于同步的规定:

  1.线程解锁前，必须把共享变量的值刷新回主内存

  2.线程加锁前，必须读取主内存的最新值到自己的工作内存

  3.加锁解锁是同一把锁

  

  ```java
  public class VolatileStudy {
  	public static void main(String [] args) throws InterruptedException{
  		VolatileDemo v=new VolatileDemo();
  		Thread thread=new Thread(v);
  		thread.start();
  		Thread.sleep(1000);
  	    v.flag=false;
  	}
  }
  class VolatileDemo implements Runnable{
  	/*volatile*/ boolean flag=true;
  	@Override
  	public void run() {
  		System.out.println("start");
  		while(flag){
              //这里不能使用system.out.println()，因为println()方法中包含了同步关键字synchronized，synchronized保证了可见性以及原子性和有序性，在释放锁时会将工作内存中的变量刷新到主内存中
  		}
  		System.out.println("end");
  	}
  }
  ```

  

## 不保证原子性分析

不能保证原子性**代码演示**：

```java
/**
 * 测试volatile的原子性
 * @author zhangkaiqiang
 *
 */
class AtomicVolatile{
    //不保证原子性
	volatile int num=0;
    //AtomicInteger保证原子性操作
    volatile AtomicInteger number=new AtomicInteger();
	public void incrNumber(){
		for(int i=0;i<1000;i++){
			num++;
            //getAndIncrement()即先i++
            //incrementAndGet()即++i
            number.getAndIncrement();
		}
	}
}

public class VolatileStudy {

	public static void main(String [] args) throws InterruptedException{
		AtomicVolatile a=new AtomicVolatile();
//创建20个线程，每个线程都执行incrNumber()方法，如果能保证原子性，最终结果应该为20000
		for(int i=0;i<20;i++){
			new Thread(a::incrNumber).start();
		}	
 //后台线程包括main线程和gc线程，当判断条件成立时表示用户线程未结束，main线程礼让用户线程。直到用户线程结束，main线程才继续执行
	    while(Thread.activeCount()>2){
	        Thread.yield();
	    }
		System.out.println(a.num);
        System.out.println(a.number);
	}
}
```

**原理分析**(num++)

num++在JVM中的操作步骤是什么样的，我们通过字节码文件看看num++在JVM中都干了什么

```java
//代码
public class VolatileChangeToByteCode {

	volatile int n=0;
	public void add(){
		n++;
	}
	public static void main(String [] args){
		VolatileChangeToByteCode v=new VolatileChangeToByteCode();
		v.add();
	}
}
//对上面的代码使用javap -c VolatileChangeToByteCode.class命令得到add()方法的字节码文件如下:
  public void add();
    Code:
       0: aload_0
       1: dup
       //获得初始值
       2: getfield      #12                 // Field n:I
       //执行iadd,进行加1操作
       5: iconst_1
       6: iadd
       //写回主内存
       7: putfield      #12                 // Field n:I
      10: return
```

**解决办法**

- volatile不保证原子性，最简单的解决办法是添加synchronized

- 使用Java.util.concurrent包下的AtomicInteger(AtomicInteger底层使用CAS实现，CAS的原理分析)

## 指令重排

内存屏障

## 应用场景

- 单利设计模式--懒汉式

  https://mp.weixin.qq.com/s/EpgMuDuOQjWYuUJTqpRjnw

  版本1-----存在线程安全问题

  ```java
  public class SinglteonDemo{
      //懒汉式即神马时候用到改实例在进行创建
      private static volatile SinglteonDemo instance=null;
      //私有的构造方法
      private SinglteonDemo(){}
      
      public static SinglteonDemo getInsteance(){
          if(instance==null){
              instance=new SinglteonDemo();
          }
             return instance;
      }
  }
  ```

  版本2--使用synchronized进行多线程多线程的控制,但是效率极低

  ```java
  public class SinglteonDemo{
      //懒汉式即神马时候用到改实例在进行创建
      private static volatile SinglteonDemo instance=null;
      //私有的构造方法
      private SinglteonDemo(){}
      
      //使用synchronized进行线程安全的保证
      public static synchronized SinglteonDemo getInsteance(){
          if(instance==null){
              instance=new SinglteonDemo();
          }
             return instance;
      }
  }
  ```

  版本三------双重校验锁(DCL)

  因为有指令重排的存在，该版本不一定线程安全，可以加入volatile禁止指令重排

```java
public class SinglteonDemo{
    //懒汉式即神马时候用到改实例在进行创建
    private static volatile SinglteonDemo instance=null;
    //私有的构造方法
    private SinglteonDemo(){}
    
    public static SinglteonDemo getInsteance(){
        //第一个if判断为了提高程序的效率
        if(instance==null){
            synchronized(SinglteonDemo.class){
                //如果没有第二次校验，假设线程t1执行了第一次校验后，判断为null，这时t1失去了CPU执行权，t2获取了CPU执行权，也执行了第一次校验，判断也为null。接下来t2获得锁，创建实例。这时t1又获得CPU执行权，由于之前已经进行了第一次校验，结果为null（不会再次判断），获得锁后，直接创建实例。结果就会导致创建多个实例。所以需要在同步代码里面进行第二次校验，如果实例为空，则进行创建。
                if(instance==null){
                      instance=new SinglteonDemo();
                }
            }
        }
           return instance;
    }
}
```

版本四-----------添加volatile解决指令重排

```java
public class SinglteonDemo{
    //懒汉式即神马时候用到改实例在进行创建
    private static volatile SinglteonDemo instance=null;
    //私有的构造方法
    private SinglteonDemo(){}
    
    public static SinglteonDemo getInsteance(){
        //第一个if判断为了提高程序的效率
        if(instance==null){
            synchronized(SinglteonDemo.class){
                //如果没有第二次校验，假设线程t1执行了第一次校验后，判断为null，这时t1失去了CPU执行权，t2获取了CPU执行权，也执行了第一次校验，判断也为null。接下来t2获得锁，创建实例。这时t1又获得CPU执行权，由于之前已经进行了第一次校验，结果为null（不会再次判断），获得锁后，直接创建实例。结果就会导致创建多个实例。所以需要在同步代码里面进行第二次校验，如果实例为空，则进行创建。
                if(instance==null){
                      instance=new SinglteonDemo();
                }
            }
        }
           return instance;
    }
}
```

静态内部类形式:

```java
public class Singleton {
   private static class SingletonHolder {
       private static final Singleton INSTANCE = new Singleton();
   }
   private Singleton (){}
   public static final Singleton getInstance() {
       return SingletonHolder.INSTANCE;
   }
}
```



  

练习1：有一个容器，存在两个方法，add() size()

现在有两个线程，线程1向容器中添加1-10，线程二监视容器，当容器中元素个数为5时推出

思路分析:需要使用volatile关键字保证list的可见性

版本一:使用volatile保证list的可见性

```java
public class MyContainer{
    //容器
    volatile List<Integer> list=new ArrayList<>();
    //添加方法
    public void add(int i){
        list.add(i);
    }
    //size()方法
    public int size(){
        return list.size();
    }
    
    public static void main(String [] args){
        MyContainer myContainer=new MyContainer();
        //进行元素添加的线程
        new Thread(()->{
            for(int i=0;i<10;i++){
                myContainer.add(i);
            }
        }).start();
        //监控容器元素个数的线程
        new Thread(()->{
            while(true){
                if(myContainer.size()==5){
                    break;
                }
            }
            System.out.println("线程二退出");
        }).start();
    }
} 
```

版本二:使用wait()/ notify()机制替代线程二中的while(true),该方法相对较复杂

注意:wait()操作会使当前线程释放持有的锁，notify()操作即使唤醒另一个线程但是也不会释放锁

```java
/**
 * 容器的优化:while(true)循环，浪费CPU资源，使用wait()/notify()替代while(true)循环
 * @author zhangkaiqiang
 *
 */
public class MyContainerUseVolatile1 {

	public static void main(String[] args) {
		
		MyContainer1 myContainer1=new MyContainer1();
		
		//第二个线程进行数据的读取
		new Thread(()->{
			synchronized(myContainer1){
				//如果集合中数据没有达到五个，该线程处于等待状态
				if(myContainer1.size()!=5){
					try {
						myContainer1.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("线程二执行完毕");
				//唤醒第一个写入线程继续继续进行数据写入，因为线程二执行完毕会自动释放锁，这里不需要进行wait()操作
				myContainer1.notify();
			}
		}).start();
		

		//第一个线程，进行数据的写入，当数据到达5时等待，唤醒另一个线程
		new Thread(()->{
			synchronized(myContainer1){
				for(int i=0;i<10;i++){
					myContainer1.add(i);
					//写入线程此时容器元素个数为5，该写入线程等待，唤醒监视线程
					System.out.println("add"+i);
					if(myContainer1.size()==5){
						try {
							myContainer1.notify();
							myContainer1.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}).start();
	}
}

/**
 * 容器类
 * @author zhangkaiqiang
 *
 */
class MyContainer1{
	
	//添加volatile,使得该list可见
	volatile List<Object> list=new ArrayList<>();
	/**
	 * 添加元素的方法
	 * @param o
	 */
	public void add(Object o){
		list.add(o);
	}
	
	/**
	 * 得到容器容量的方法
	 * @return
	 */
	public int size(){
		
		return list.size();
	}
}
```

版本三(不推荐):使用倒计时门闩CountDownLatch ,在门闩打开后，两个线程争夺CPU的执行权，只有让写的线程先暂时sleep，该方法才行

```java
/**
 * 容器的优化2:使用CountDownLatch 门闩进行优化
 * @author zhangkaiqiang
 *
 */
public class MyContainerUseVolatileWithCountDownLatch {

	public static void main(String[] args) {
		//容器类
		MyContainer2 myContainer2=new MyContainer2();
		
		//倒计时门闩,当调用countdown()方法时，1会减少1，当减少到0时被门闩堵在门外的线程开始执行
		CountDownLatch latch=new CountDownLatch(1);
		
		//读取线程
		new Thread(()->{
			if(myContainer2.size()!=5){
				try {
					//await()方法相当于wait()方法即使线程处于等待状态，当门闩中变量减小到0时就会开始执行
					latch.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("线程二执行完毕");
		}).start();

		//写入线程
		new Thread(()->{
			for(int i=0;i<10;i++){
				myContainer2.add(i);
				System.out.println("add"+i);
				if(myContainer2.size()==5){
					latch.countDown();
				}
                //如果不使用sleep()操作。线程2不一定能获得CPU的执行权
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}

/**
 * 容器类
 * @author zhangkaiqiang
 *
 */
class MyContainer2{
	
	//添加volatile,使得该list可见
	volatile List<Object> list=new ArrayList<>();
	/**
	 * 添加元素的方法
	 * @param o
	 */
	public void add(Object o){
		list.add(o);
	}
	
	/**
	 * 得到容器容量的方法
	 * @return
	 */
	public int size(){
		
		return list.size();
	}
}
```

