package threadbase;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/11  11:12
 * @Description TODO
 * 3、yield()会使该线程让出CPU的使用权，从运行状态进入就绪状态，等待CPU的重新调度
 */

public class YieldThread {
	public static void main(String[] args) throws InterruptedException {
		/**
		 * yield()
		 */
		System.out.println("-------------------yield()方法-------------------------");
		new Thread(()->{
			for (int i = 0; i <10 ; i++) {
				if(i==3){
					Thread.yield();
				}
				System.out.println(Thread.currentThread().getName()+"............"+i);
			}
		}).start();


		for (int i = 0; i <10 ; i++) {
			System.out.println(Thread.currentThread().getName()+"......"+i);
		}
	}
}
