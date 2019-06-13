package threadsafe;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/11  19:21
 * @Description
 *
 * 线程安全问题(锁机制):多个线程访问类中的共享变量，在不加限制的情况下可能存在被意外修改的情况
 *
 * 下面这段售票代码，存在10个线程销售100张票，存在重票以及错票的情况
 *
 * 1.如何解决：加锁 synchronized  Lock等
 */

public class ThreadSafeDemo {
	public static void main(String[] args) {

		//线程操作资源类
		Ticket ticket=new Ticket();

		for (int i = 0; i <10 ; i++) {
			new Thread(()->{
				for (int j = 0; j < 10; j++) {
					ticket.saleTicket();
				}
			}).start();
		}
	}
}

class Ticket{

	/**
	 * 总票数
	 */
	private int count=100;

	/**
	 * 售票
	 */
	public void saleTicket(){
		System.out.println(Thread.currentThread().getName()+"出售了第"+count+"张票");
		count--;
	}
}
