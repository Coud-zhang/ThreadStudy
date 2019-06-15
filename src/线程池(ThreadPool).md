# 线程池(ThreadPool)

## Callable<V>接口

- 通过实现该接口来开启一个线程，线程在执行完Callable<V>接口实现类中指定的任务时会有返回值。

- 当多个线程去执行相同的任务即通过同一个FutureTask类的实例去创建多个线程时，该任务只会执行一次，想要多个线程执行同一个FutureTask任务时，可以通过同一个Callable<V>创建多个FutureTask实例，通过多个实例开启多个线程

- 主要方法:

  ```java
  //创建一个 FutureTask ，它将在运行时执行给定的 Callable 
  FutureTask(Callable<V> callable) 
  //返回 true如果任务已完成。  
   boolean isDone() 
  //等待计算完成，然后检索其结果。 
  V get()
  ```

  代码展示:

```java
//Thread类的构造方法只能接受Runnable接口的实现类，那么如何通过Callable接口开启一个线程哪
//Runnable--子接口-->RunnableFuture<V>--实现类-->FutureTask---构造方法--->FutureTask(Callable<V> callable) 

public class CallableInfterfaceStudy {
	public static void main(String[] args) {
		
		System.out.println("最原始的实现方式");
         CallableThread callableThread=new CallableThread();
		FutureTask<Integer> futureTask=new FutureTask<Integer>(callableThread);
		new Thread(futureTask).start();
        
        
		System.out.println("使用lambda的实现方式");
		FutureTask<Integer> futureTask1=new FutureTask<Integer>(()->{
			return 111;
		});
		new Thread(futureTask1).start();

		
		//得到完成Callable接口中指定任务的返回值，如果这个线程还没有计算完成会造成main线程的阻塞，建议放在最后
		while(!futureTask.isDone()){
			//没有执行完Callable接口中的任务时进入该循环
		}
		
		try {
			System.out.println("futureTask执行Callable的返回值为"+futureTask.get());
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	
		while(!futureTask1.isDone()){
			//没有执行完Callable接口中的任务时进入该循环
		}
			try {
				System.out.println("futureTask执行Callable的返回值为"+futureTask1.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
	}
}

class CallableThread implements Callable<Integer>{
	@Override
	public Integer call() throws Exception {
		return 1314;
	}
}

```



## 概述

一个可以容纳多个线程的容器，其中的线程可以反复使用，省去了频繁创建线程的操作，在需要线程时从容器中得到一个线程，线程执行完相应任务后，将线程归还会容器中

## 架构分析

![](E:\Java相关文档\javaee基础知识\线程\线程池体系结构.png)

## 原理与使用

- JDK1.5之后提供了线程池，在java.util.concurrent.Executors为线程池的工具,java.util.concurrent.ThreadPoolExecutor为线程池关键类

- 创建线程池主要方法

```java
//在生产中不通过这三个方法来创建线程池，一般自定义线程池
//阿里开发手册为什么不使用下面的这三种方式?
LinkedBlockingQueue<>阻塞队列的最大长度为Integer.MAX_VALUE，可能会造成oom

//用来产生一个固定线程数量的线程池，执行长期任务性能较好
Executors.newFixedThreadPool(int nThreads)
    public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory) {
    return new ThreadPoolExecutor(nThreads, nThreads,
                                  0L, TimeUnit.MILLISECONDS,
                                  new LinkedBlockingQueue<Runnable>(),
                                  threadFactory);
}
//创建一个根据需要创建新线程的线程池，但在可用时将重新使用以前构造的线程   
Executors.newCachedThreadPool() 
    
 public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                      60L, TimeUnit.SECONDS,
                                      new SynchronousQueue<Runnable>());
    }
//创建一个使用从无界队列运行的单个工作线程的执行程序。即该线程池中只有一个线程
newSingleThreadExecutor() 
    
public static ExecutorService newSingleThreadExecutor() {
        return new FinalizableDelegatedExecutorService
            (new ThreadPoolExecutor(1, 1,
                                    0L, TimeUnit.MILLISECONDS,
                                    new LinkedBlockingQueue<Runnable>()));
    }
```

### ThreadPoolExecutor

从上面可以看出上面三个方法都调用了同一个构造方法即ThreadPoolExecutor()

```java
//corePoolSize(核心线程数)是指线程池中常驻线程数
//maximumPoolSize(最大线程数):线程池中能够容纳执行的最大线程数
//keepAliveTime是指非核心线程的存活时间，当前线程池中线程数超过corePoolSize时，当非核心线程的空闲时间达到keepAliveTime时会被销毁，直到线程池中线程个数等于corePoolSize
//unit即keepAliveTime的单位
//workQueue即阻塞队列，当到来的任务较多，线程数不足以处理所有的任务时，为被处理的任务堵塞在该队列中
//threadFactory表示生产当前线程成中工作线程的工程类，即用来创建线程的工程
//handler即拒绝策略，当阻塞队列满了且工作线程数大于等于最大线程数时采取的拒绝措施
public ThreadPoolExecutor(int corePoolSize,int maximumPoolSize,long keepAliveTime,TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,ThreadFactory threadFactory,
                              RejectedExecutionHandler handler) {
    }
```

工作原理

```java

```

**拒绝策略**

![](E:\Java相关文档\javaee基础知识\线程\线程池拒绝策略体系结构.png)

```
AbortPolicy(默认):直接抛出RejectedExecutionException异常，阻止系统正常运行
DiscardOldestPolicy:丢弃队列中等待最久的任务，然后把当前任务加入到队列中尝试再次提交当前任务
CallerRunsPolicy:调用者运行一种调节机制，即不拒绝也不抛出异常，而是把任务退回给调用者
DiscardPolicy:直接丢弃任务，不予处理 ，也不抛出异常
```

自定义线程池

```java
//CPU密集型业务        线程数=CPU核数+1

//IO密集型业务
(1) cpu线程数*2
(2) cpu线程数/1-阻塞系数(0.8-0.9)
```



