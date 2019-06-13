package threadsafe;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/11  20:13
 * @Description
 *
 * 使用synchronized保证线程安全，synchronized默认为非公平锁，有同步方法与同步代码块两种
 */
public class SafeThreadWithSync {

	public static void main(String[] args) {

		TicketWithSyn ticketWithSyn=new TicketWithSyn();

		for (int i = 0; i <10 ; i++) {
			new Thread(()->{
				for (int j = 0; j <10 ; j++) {
					ticketWithSyn.saleTicket();
				}
			}).start();
		}
	}
}

class TicketWithSyn{

	/**
	 * 总票数
	 */
	private int count=100;

	/**
	 * 同步方法,同步方法的锁对象为this,静态同步方法的锁对象为类的class对象
	 */
	public synchronized void saleTicket(){

		//同步代码块，同步代码块的锁对象可以是任意对象，但是必须保证多个线程使用的锁对象为同一个锁对象
//		synchronized (this){
//			System.out.println(Thread.currentThread().getName()+"出售了第"+count+"张票");
//			count--;
//		}
		System.out.println(Thread.currentThread().getName()+"出售了第"+count+"张票");
		count--;
	}
}