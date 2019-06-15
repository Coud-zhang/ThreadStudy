package threadpool;





import java.util.Random;
import java.util.concurrent.*;


/**
 * @Author zhangkaiqiang
 * @Date 2019/6/15  9:44
 * @Description ForkJoinPool
 *
 * ForkJoinPool可以把大人物分解为一个一个的小任务进行计算，然后再把计算结果进行合并
 */
public class ForkJoinPoolTest {
	public static void main(String[] args) {
		/**
		 * 先对List进行数据填充
		 */
		Random random=new Random();
		int [] list=new int [10000];
		for (int i = 0; i <10000; i++) {
			//System.out.println("正在进行数据填充");
			list[i]=random.nextInt(100);
		}

		int sum=0;
		long start=System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			sum=sum+list[i];
		}
		System.out.println("for循环计算时间为"+(System.currentTimeMillis()-start));
		System.out.println(sum);
		start=System.currentTimeMillis();
		PrintTask printTask=new PrintTask(list,0,list.length);
		ForkJoinPool pool = new ForkJoinPool();
		//提交分解的SumTask 任务
		pool.execute(printTask);
		int result= printTask.join();
		System.out.println("使用forkjoin计算时间为:"+(System.currentTimeMillis()-start));
		System.out.println(result);
		//pool.shutdown(); //关闭线程池
	}
}

class PrintTask  extends RecursiveTask<Integer> {

	private int start,end;
	private static final int THRESHOLD = 5000;
	private int [] array;
	private int sum=0;

	protected PrintTask(int [] array,int start,int end){
		this.array=array;
		this.start=start;
		this.end=end;
	}

	@Override
	protected Integer compute() {

		if(end-start<=THRESHOLD){
			for (int i = start; i < end; i++) {
				sum=sum+array[i];
			}
			return sum;
		}
		//>>右移运算符，相当于/2
		int middle=(end-start)>>1;
		PrintTask left=new PrintTask(array,start,middle);
		PrintTask right=new PrintTask(array,middle,end);
		//并行执行两个 小任务
		left.fork();
		right.fork();
		//把两个小任务累加的结果合并起来
		 return left.join()+right.join();
	}
}
