package threadbase;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/11  14:12
 * @Description
 *
 * 在某个线程中调用join()方法会造成当前线程阻塞，插入的线程先执行，直到插入的线程执行完毕原线程才开始执行
 */
public class JoinThtead {
	public static void main(String[] args) {
		Thread thread=new Thread(()->{
			for(int i=0;i<10;i++){
				System.out.println(Thread.currentThread().getName()+"..."+i);
			}
		},"Join线程");

		thread.start();

		for(int i=0;i<10;i++){
			if(i==3){
				try {
					//在这里调用join()方法会造成main线程阻塞，直到thread线程执行完，main线程才能继续执行
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println(Thread.currentThread().getName()+"...."+i);
		}
	}
}
