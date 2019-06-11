package threadbase;

import static java.lang.Thread.*;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/10  14:41
 * @Description
 * Java中对线程状态的定义在与public enum State {}枚举类中，包括NEW RUNNABLE TIMED_WAITING  TERMINATED等
 */
public class AllState {

	public static void main(String[] args) {

		Thread thread=new Thread(()->{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		//NEW
		State state=thread.getState();
		System.out.println(state.toString());

		//RUNNABLE
		thread.start();
		state=thread.getState();
		System.out.println(state.toString());


		//TIMED_WAITING
		while(state!= State.TERMINATED){
			//得到当前的线程数
			int num= activeCount();
			System.out.println(num);
			try {
				sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			state=thread.getState();
			System.out.println(state.toString());
		}
	}
}
