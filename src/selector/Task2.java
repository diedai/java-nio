package selector;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 客户端单线程
 * <pre>
 * @author gzh
 * @date 2019年3月20日 下午1:35:51
 * </pre>
 */
public class Task2 implements Runnable {
	Selector selector = null;

	@Override
	public void run() {
		try {
			//创建客户端套接字通道
			SocketChannel socketChannel = SocketChannel.open();
			//非阻塞模式
			socketChannel.configureBlocking(false);
			//连接服务器
			boolean b = socketChannel.connect(new InetSocketAddress(8080)); //因为异步，所以两种结果:1.立即返回true 2.暂时false
			
			//创建多路选择器
			selector = Selector.open();
			
			
			//1.处理立即成功的情况
			//判断连接是否成功
			if (b) { //连接成功，读事件
				//读事件
				socketChannel.register(selector, SelectionKey.OP_READ);
				//写数据到服务器
				doWrite(socketChannel);
			} else { //不成功，连接事件
				socketChannel.register(selector, SelectionKey.OP_CONNECT);
			}
			
			//2.处理暂时false的情况
			//一直循环轮询
			while (true) {
				//遍历事件
				try {
					selector.select(1000);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Set set = selector.selectedKeys();
				Iterator iterator = set.iterator();
				while (iterator.hasNext()) {
					//取事件
					SelectionKey selectionKey = (SelectionKey) iterator.next();
					
					//处理事件
					handleEvent(selectionKey);
				}
			}	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 写数据到服务器
	 * <pre>
	 * @author gzh
	 * @param socketChannel 
	 * @date 2019年3月20日 下午1:45:05
	 * </pre>
	 */
	private void doWrite(SocketChannel socketChannel) {
		//写数据到服务器
	}

	/**
	 * 处理事件
	 * <pre>
	 * @author gzh
	 * @date 2019年3月20日 下午1:38:53
	 * @param selectionKey
	 * </pre>
	 */
	private void handleEvent(SelectionKey selectionKey) {
		SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
		
		//连接成功事件
		if (selectionKey.isConnectable()) { 
			try {
				if (socketChannel.finishConnect()) { //连接成功
					//读事件
					socketChannel.register(selector, SelectionKey.OP_READ);
					//写数据到服务器
					doWrite(socketChannel);
				}else { //程序退出
					System.exit(1);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		
		//读事件
		if (selectionKey.isReadable()) { 
			//读服务器发送的数据
		}
	}

}
