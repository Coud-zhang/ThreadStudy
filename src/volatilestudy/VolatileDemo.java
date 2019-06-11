package volatilestudy;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/11  14:38
 * @Description
 *
 * 在java基础部分停止线程时使用了volatile关键字来保证flag在线程间的可见性，那么volatile具有哪些特性
 *  1.保证可见性
 *  2.不保证原子性
 *  2.保证有序性(禁止指令重排)
 *
 *  可见性直接参考threadbase/StopThread.java，假如不添加volatile时，另一个线程不能停止
 */

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 原子性分析
 * 通过下面的代码可以发现volatile不能保证原子性，如果只是简单的自增或自减可以使用AtomicInteger类
 * Atomic##类底层通过CAS原理时间原子性，将在后面整理
 */
public class VolatileDemo {

	public static void main(String[] args) {

		MyShareData myShareData=new MyShareData();

		/**
		 * 开启20个线程去执行add()方法，如果volatile能够保证原子性，则结果应该为20000
		 */
		for (int i = 0; i <20 ; i++) {
			new Thread(()->{
				myShareData.add();
			}).start();
		}

		for (int i = 0; i <20 ; i++) {

			new Thread(()->{
				myShareData.AtomicAdd();
			}).start();
		}
		// 后台线程包括main线程和gc线程，当判断条件成立时表示用户线程未结束，main线程礼让用户线程。直到用户线程结束，main线程才继续执行
		while(Thread.activeCount()>2){
			Thread.yield();
		}
		System.out.println(myShareData.getCount());
		System.out.println(myShareData.getNum());
	}
}

class MyShareData{

	private volatile int count=0;

	private AtomicInteger num=new AtomicInteger();

	public void add(){
		for(int i=0;i<1000;i++){
			count++;
		}
	}

	public void AtomicAdd(){
		for(int i=0;i<1000;i++){
			//getAndIncrement()方法相当于i++
			num.getAndIncrement();
		}
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public AtomicInteger getNum() {
		return num;
	}

	public void setNum(AtomicInteger num) {
		this.num = num;
	}
}
