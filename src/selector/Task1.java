package selector;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 单线程处理任务
 * <pre>
 * @author gongzhihao
 * @date 2019年3月20日 上午11:22:42
 * </pre>
 */
public class Task1 implements Runnable {
	private Selector selector = null;

	@Override
	public void run() {
		//创建服务器端套接字通道
		ServerSocketChannel serverSocketChannel = null;
		try {
			serverSocketChannel = ServerSocketChannel.open();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//监听客户端连接的端口
		try {
			serverSocketChannel.socket().bind(new InetSocketAddress(8080),1024);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//非阻塞
		try {
			serverSocketChannel.configureBlocking(false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//创建多路选择器
		try {
			selector = Selector.open();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//关联服务器套接字通道和多路选择器
		try {
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); //监听连接事件
		} catch (ClosedChannelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	}

	/**
	 * 处理事件
	 * <pre>
	 * 1.连接
	 * 2.读
	 * 3.写
	 * @author gzh
	 * @date 2019年3月20日 上午10:49:23
	 * @param selectionKey
	 * </pre>
	 */
	private void handleEvent(SelectionKey selectionKey) {
		if (selectionKey.isAcceptable()) { //连接事件
			ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
			try {
				SocketChannel socketChannel = serverSocketChannel.accept();
				socketChannel.configureBlocking(false);
				socketChannel.register(selector, SelectionKey.OP_READ); //监听可读事件
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (selectionKey.isReadable()) { //读事件
			SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
			ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
			try {
				int n = socketChannel.read(byteBuffer);
				byte[] bytes = new byte[n];
				byteBuffer.get(bytes);
				String string = new String(bytes, "utf-8");
				System.out.println(string);
				
				//写数据到客户端
				doWrite(socketChannel);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
	}

	/**
	 * 写数据到客户端
	 * <pre>
	 * @author gzh
	 * @date 2019年3月20日 上午11:13:15
	 * @param socketChannel
	 * </pre>
	 */
	private void doWrite(SocketChannel socketChannel) {
		String string = "服务器端写数据到客户端";
		
		byte[] bytes = null;
		try {
			bytes = string.getBytes("utf-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
		byteBuffer.put(bytes);
		try {
			socketChannel.write(byteBuffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
