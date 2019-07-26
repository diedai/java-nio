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
 * 单线程处理连接
 * <pre>
 * @author gzh
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
			serverSocketChannel = ServerSocketChannel.open(); //jdk nio基于通道通信。channel和socket通信的区别是什么？socket同时只能读或写，单向通信；而channel可以同时读和写。底层是硬件-网线就支持双向通信，有的线专门读，有的线专门写。总结，相同点，作用都是通信；不同点是同一时刻一个是只能单向通信一个可以是双向通信，除此之外，没有其他不同的地方。
		    //这里得到(其实是创建)一个channel对象，和socket里的创建socket对象是一模一样的
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//监听客户端连接的端口
		try {
			serverSocketChannel.socket().bind(new InetSocketAddress(8080),1024); //服务器监听固定port 8080
		    //为什么要根据channel获取socket对象？那channel和socket到底是什么关系？既然最终还是要获取socket对象，为什么要多搞一个channel出来？
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
				selector.select(1000); //阻塞轮询，直到有新的事件到来
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Set set = selector.selectedKeys(); //处理事件
			Iterator iterator = set.iterator();
			while (iterator.hasNext()) { //遍历处理事件集合
				//取事件
				SelectionKey selectionKey = (SelectionKey) iterator.next();
				
				//处理事件
				handleEvent(selectionKey); //也可以使用XXXHandler处理事件
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
			SocketChannel socketChannel = (SocketChannel)selectionKey.channel(); //获取当前事件的channel(这里其实和socket的思路是完全一致的，如果是socket，服务器接受客户端连接之后也是创建一个专门的socket和对应客户端通信，这个是一一对应的，即客户端连接(也是socket，有自己的端口)和服务器端的socket，有自己的端口)一一对应，基于这个原理才能保证服务器和客户端一对一的正常通信。
			ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
			try {
				int n = socketChannel.read(byteBuffer); //读客户端写过来的数据
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
			socketChannel.write(byteBuffer); //写数据到客户端(因为服务器是同一个channel，所以写和刚才的读都是和同一个客户端socket通信)
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
