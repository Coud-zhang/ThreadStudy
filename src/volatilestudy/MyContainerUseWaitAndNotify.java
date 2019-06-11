package volatilestudy;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/11  15:44
 * @Description
 *
 * 针对上一个版本，监控线程使用while(true)循环会造成CPU的浪费，这里使用wait以及notify来改写程序
 */
public class MyContainerUseWaitAndNotify {
	public static void main(String[] args) {

		//继续使用上一个程序中的容器类
		MyContainer myContainer=new MyContainer();

		//监控线程
		new Thread(()->{
			System.out.println("监控线程启动");
			synchronized (MyContainer.class){

				//如果容器中元素个数没有达到五个，该线程阻塞
				if(myContainer.size()!=5){
					try {
						MyContainer.class.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("监控线程终止");
				//唤醒插入线程继续添加元素
				MyContainer.class.notifyAll();
			}
		}).start();


		//插入线程
		new Thread(()->{
			synchronized (MyContainer.class){
				for(int i=1;i<=10;i++){
					myContainer.add(String.valueOf(i));
					if(myContainer.size()==5){
						MyContainer.class.notifyAll();
						try {
							MyContainer.class.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}).start();
	}
}
