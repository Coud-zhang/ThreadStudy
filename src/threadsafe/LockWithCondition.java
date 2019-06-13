package threadsafe;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/13  15:46
 * @Description
 *
 * Lock+Condition与synchronized+wait+notify一样，可以实现线程间通信
 * Lock+Condition具备精确唤醒的特性，可以指定唤醒那个/那些线程
 *
 *
 * condition的使用练习
 * 题目:多个线程间按顺序调用，实现a->b->c
 * a打印5次，b打印10次，c打印15次
 * 往复打印10轮
 *
 *
 *小结:在线程A处添加如下代码System.out.println("第"+(i+1)+"次打印");你就会这个程序存在小问题，下面进行运行分析
 *
 * 1.A线程首先获得CPU执行权，执行第一次for循环操作即执行一次printA()方法，
 * 2.当printA()方法执行后改变type的值，并唤醒B线程，但是此时A线程没有释放锁，B线程只能等待获取锁，C线程发现type值并不是C，会被阻塞
 * 3，此时A线程执行第二次for循环，执行printA()方法时判断出type值不再为A，A线程调用await(）阻塞，B线程获取锁得以执行
 */
public class LockWithCondition {
	public static void main(String[] args) {

		ConditionTest conditionTest=new ConditionTest();

		//线程A
		new Thread(()->{
			for (int i = 0; i <10 ; i++) {
				//System.out.println("第"+(i+1)+"次打印");
				conditionTest.printA();
			}
		},"A").start();

		try {
			TimeUnit.MILLISECONDS.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//线程B
		new Thread(()->{
			for (int i = 0; i <10 ; i++) {
				conditionTest.printB();
			}
		},"B").start();

		//线程C
		new Thread(()->{
			for (int i = 0; i <10 ; i++) {
				conditionTest.printC();
			}
		},"C").start();
	}
}

class ConditionTest{

	private Lock lock=new ReentrantLock();

	private Condition A=lock.newCondition();

	private Condition B=lock.newCondition();

	private Condition C=lock.newCondition();


	//为了防止虚假唤醒添加的标志位
	static String type = "A";
	/**
	 * 打印方法
	 * @param
	 */
	public void printA(){
		lock.lock();
		while(type!="A"){
			try {
				A.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			for (int i = 0; i <5 ; i++) {
				System.out.println(Thread.currentThread().getName()+"...."+i);
			}
			type="B";
			B.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public void printB(){
		lock.lock();
		while(type!="B"){
			try {
				B.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			for(int i=0;i<10;i++){
				System.out.println(Thread.currentThread().getName()+"....."+i);
			}
			type="C";
			C.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public void printC(){
		lock.lock();
		while(type!="C"){
			try {
				C.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			for (int i = 0; i <15 ; i++) {
				System.out.println(Thread.currentThread().getName()+"....."+i);
			}
			type="A";
			A.signal();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
}
