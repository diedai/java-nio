package selector;

/**
 * 服务器入口：启动单线程
 * <pre>
 * @author gzh
 * @date 2019年3月20日 上午10:33:27
 * </pre>
 */
public class Server {

	public static void main(String[] args) {
		// 单线程处理任务
		new Thread(new Task1()).start();
		;

	}

}
