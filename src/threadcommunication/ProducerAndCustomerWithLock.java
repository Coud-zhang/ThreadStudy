package threadcommunication;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/14  14:59
 * @Description
 *
 * 线程间通信---Lock+condition+await()+signal()
 */
public class ProducerAndCustomerWithLock {

	static MyContainer1 myContainer1=new MyContainer1();
	public static void main(String[] args) {

		//消费者线程
		for(int i=0;i<2;i++){
			new Thread(()->{
				for(int j=0;j<20;j++){
					myContainer1.get();
				}
			},"消费者线程"+i).start();
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//生产者线程
		for(int i=0;i<2;i++){
			new Thread(()->{
				System.out.println(Thread.currentThread().getName()+"生产者线程启动");
				for(int k=0;k<20;k++){
					myContainer1.put(k);
				}
			},"生产者线程"+i).start();
		}
	}
}

class MyContainer1{
	/**
	 * 容器总容量
	 */
	static final  int CAPACITY=10;
	private int count=0;

	LinkedList<Object> list=new LinkedList<>();


	Lock lock=new ReentrantLock();
	Condition producer=lock.newCondition();
	Condition customer=lock.newCondition();

	/**
	 * 向容器中添加元素的方法
	 * @param o
	 */
	public void put(Object o){
		lock.lock();
		try {
			/**
			 * 这里要使用while()而不是使用if，当消费者从容器中拿走一个元素，调用notifyAll()唤醒所有的线程，线程会从wait()继续向下执行，如果不使用while()再判断一次，
			 * 可能会发生一个线程向容器中写入了数据，另一个线程又向容器中写入数据，造成数组下班越界
			 */
			while(this.getCount()==CAPACITY){
				try {
					System.out.println(Thread.currentThread().getName()+".........容器中满了，不能再继续添加");
					producer.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			list.add(o);
			System.out.println(Thread.currentThread().getName()+"正在添加......"+o);
			count++;
			//唤醒消费者线程进行消费
			customer.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 从容器中取元素的方法
	 * @return
	 */
	public Object get(){
		lock.lock();
		Object result=null;
		try {
			while(this.getCount()==0){
				try {
					System.out.println(Thread.currentThread().getName()+".......容器中空了，需要等待");
					customer.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			result=list.removeFirst();
			System.out.println(Thread.currentThread().getName()+"从容器中取出元素...."+result);
			count--;
			//唤醒生产者线程进行生产
			producer.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		return result;
	}

	public int getCount(){
		return list.size();
	}

}
