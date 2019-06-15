# ThreadLocal

**什么是ThreadLocal?**

ThreadLocal提供了**线程内部的局部变量**，当前线程可以通过set()和get()来对这个局部变量进行操作，往ThreadLocal中填充的变量属于**当前**线程，因此该变量对其他线程而言是隔离的，不会和其他线程的局部变量产生冲突。

```java
public class ThreadLocalStudy {

	public static void main(String [] args){
		
		String name="zhangsan";
		ThreadLocal th=new ThreadLocal();
		
		//第一个线程
		new Thread(()->{
			th.set(name);
		}).start();
		
		//第二个线程
		new Thread(()->{
			System.out.println(th.get());
		}).start();
	}
}
```



**为什么要使用ThreadLocal?**

(1)管理Connection

使用ThreadLocal来管理Connection,能够实现**当前线程的操作都是用同一个Connection，保证了事务的ACID！**

(2)避免参数的传递

可以将数据保存在ThreadLocal中，通过其中的get()方法得到数据从而进行数据的传递。

**原理解析**

网上有各种博客可以参考，简单来说就是:

**ThreadLocal本身并不存储任何数据，数据真正存储在ThreadLocal中静态内部类ThreadLocalMap的entry中，每个Thread维护一个ThreadLocalMap映射表，这个映射表的key是ThreadLocal实例本身，value是真正需要存储的Object。** 



```java
//ThreadLocal
public class ThreadLocal<T> {
 public void set(T value) {
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null)
            map.set(this, value);
        else
            createMap(t, value);
    }
}
```

**内存泄露问题**



<http://www.importnew.com/22039.html>