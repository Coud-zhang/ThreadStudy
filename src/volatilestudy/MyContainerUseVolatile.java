package volatilestudy;

import com.sun.org.apache.bcel.internal.generic.NEW;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/11  15:32
 * @Description
 *
 * 题目:有一个容器，存在两个方法，add() size()
 * 现在有两个线程，线程1向容器中添加1-10，线程二监视容器，当容器中元素个数为5时推出
 *
 * 思路分析:需要使用volatile关键字保证list的可见性
 */
public class MyContainerUseVolatile {
	public static void main(String[] args) {

		MyContainer<String> my=new MyContainer();


		//监控线程
		new Thread(()->{
			System.out.println("监控线程开始执行");
			while(true){
				if(my.size()==5){
					break;
				}
			}
			System.out.println("监控线程终止");
		}).start();

		//添加线程
		new Thread(()->{
			for (int i = 1; i <+10 ; i++) {
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				my.add(String.valueOf(i));
			}
		},"生产者").start();
	}
}

class MyContainer<T>{

	private volatile List<T> list=new ArrayList<>();

	/**
	 * 向容器中添加元素的方法
	 */
	public void add(T o){
		System.out.println(Thread.currentThread().getName()+"正在添加....."+o);
		list.add(o);
	}


	public int size(){
		return list.size();
	}
}


