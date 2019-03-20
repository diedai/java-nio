package selector;

/**
 * 客户端入口：启动客户端单线程
 * <pre>
 * @author gongzhihao
 * @date 2019年3月20日 下午1:35:36
 * </pre>
 */
public class Client {

	public static void main(String[] args) {
		new Thread(new Task2());
		
		
	}

}
