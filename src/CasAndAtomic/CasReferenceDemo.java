package CasAndAtomic;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author zhangkaiqiang
 * @Date 2019/6/14  15:37
 * @Description
 *
 * CAS存在的问题:
 * 只能保证一个共享变量的原子操作(但是通过使用原子引用，吧多个变量封装在一个对象中，不就相当于保证多个变量的原子性)
 */
public class CasReferenceDemo {

	public static void main(String[] args) {

		Student zhang=new Student("zhangsan",10);
		Student li=new Student("lisi",9);

		/**
		 * 一个Student类型的原子引用
		 */
		AtomicReference<Student> atomicReference=new AtomicReference<>(zhang);

		boolean flag=atomicReference.compareAndSet(zhang,li);

		System.out.println(flag);

		System.out.println(atomicReference.get().toString());

	}
}


class Student{

	private String name;
	private Integer age;

	public Student(){}

	public Student(String name,Integer age){
		this.age=age;
		this.name=name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Student{" +
				"name='" + name + '\'' +
				", age=" + age +
				'}';
	}
}
