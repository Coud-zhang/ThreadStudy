package threadlocal;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/15  8:50
 * @Description
 *
 * ThreadLocal示例程序
 *
 * 1.ThreadLocal是什么
 *
 * ThreadLocal提供了线程内部的局部变量，当前线程可以通过set()和get()来对这个局部变量进行操作，
 * 往ThreadLocal中填充的变量属于当前线程，因此该变量对其他线程而言是隔离的，不会和其他线程的局部变量产生冲突。
 *
 * 2.ThreadLocal原理解析
 *
 * 3.内存泄露问题
 *
 */
public class ThreadLocalDemo {
	public static void main(String[] args) {

		String name="zhngsan";

		ThreadLocal<String> threadLocal=new ThreadLocal<>();

		new Thread(()->{
			/**
			 * 设置name变量为A线程的局部变量
			 */
			threadLocal.set(name);
		},"A").start();


		new Thread(()->{
			System.out.println(threadLocal.get());
		}).start();
	}
}
