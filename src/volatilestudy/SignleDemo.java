package volatilestudy;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/11  15:02
 * @Description
 *
 * volatile在单例设计模式的应用
 *
 * 1.在volatile文档中关于单例模式的连接讲解个人觉得非常清晰，这里只说volatile禁止指令重排
 */
public class SignleDemo {

	/**
	 * 私有构造方法
	 */
	private SignleDemo(){}

	private static volatile SignleDemo SIGNLE_DEMO=null;

	private static SignleDemo getInstance(){

		if(SIGNLE_DEMO==null){
			synchronized (SignleDemo.class){
				if(SIGNLE_DEMO==null){
					SIGNLE_DEMO=new SignleDemo();
				}
			}
		}
		return SIGNLE_DEMO;
	}

	public static void main(String[] args) {
		SignleDemo signleDemo=SignleDemo.getInstance();
		SignleDemo signleDemo1=SignleDemo.getInstance();

		System.out.println(signleDemo==signleDemo1);
	}
}
