package threadcommunication;

import java.util.LinkedList;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/14  13:51
 * @Description
 *
 * 线程间通信---使用synchronized+wait()+notifyAll()
 *
 * 题目:一个同步容器，拥有put get getcount方法，能够支持两个生产者线程和消费者线程的阻塞调用
 */
public class ProducerAndCustomer {

	static MyContainer my=new MyContainer();


	public static void main(String[] args) {

		//消费者者线程
		for (int i = 0; i <2 ; i++) {
			new Thread(()->{
				System.out.println("消费者线程..."+Thread.currentThread().getName()+"启动");
				for (int j = 0; j <20 ; j++) {
					my.get();
				}
			}).start();
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		//生产者线程
		for (int i = 0; i <2 ; i++) {
			new Thread(()->{
				System.out.println("生产者线程...."+Thread.currentThread().getName()+"启动");
				for (int j = 0; j < 20; j++) {
					my.put(j);
				}
			}).start();
		}

	}
}

class MyContainer{

	/**
	 * 容器总容量
	 */
	static final  int CAPACITY=10;
	private int count=0;

	 LinkedList<Object> list=new LinkedList<>();

	/**
	 * 向容器中添加元素的方法
	 * @param o
	 */
	 public synchronized void put(Object o){
		 /**
		  * 这里要使用while()而不是使用if，当消费者从容器中拿走一个元素，调用notifyAll()唤醒所有的线程，线程会从wait()继续向下执行，如果不使用while()再判断一次，
		  * 可能会发生一个线程向容器中写入了数据，另一个线程又向容器中写入数据，造成数组下班越界
		  */
		 while(this.getCount()==CAPACITY){
			 try {
				 System.out.println(Thread.currentThread().getName()+"容器中满了，不能再继续添加");
				 this.wait();
			 } catch (InterruptedException e) {
				 e.printStackTrace();
			 }
		 }
		 list.add(o);
		 System.out.println(Thread.currentThread().getName()+"正在添加......"+o);
		 count++;
		 //唤醒消费者线程进行消费
		 /**
		  *  这里不能使用notify()，使用notify()唤醒的可能又是生产者线程，造成死锁的发生
		  *  建议永远不要使用notify()方法而是使用notifyAll()方法
		  */
		 this.notifyAll();
	 }

	/**
	 * 从容器中取元素的方法
	 * @return
	 */
	public synchronized Object get(){
	 	while(this.getCount()==0){
			try {
				System.out.println(Thread.currentThread().getName()+"容器中空了，需要等待");
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Object result=list.removeFirst();
		System.out.println(Thread.currentThread().getName()+"从容器中取出元素...."+result);
		count--;
		//唤醒生产者线程进行生产
		this.notifyAll();
		return result;
	 }

	 public int getCount(){
	 	return list.size();
	 }

}
