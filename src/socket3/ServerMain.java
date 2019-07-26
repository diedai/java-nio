package socket3;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 最简单的socket程序-循环接受客户端连接，但是这次使用多线程处理每个客户端连接，目的是并发连接可以被多线程同时执行，而不是一个连接一个连接的执行，后面的连接必须等待前面的连接执行完成。
 * 
 * 怎么实现？就是把socket对象丢到线程里去即可。
 * <pre>
 * @author gzh
 * @date 2019年7月25日 下午9:06:13
 * </pre>
 */
public class ServerMain {

	public static void main(String[] args) {
		//创建服务器socket
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(8080); //服务器socket只需要创建一次即可，也就是说，只需要创建一个服务器socket对象，该socket对象始终监听某个固定端口
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} //一个socket对应一个ip/port，计算机io主要是两块1.磁盘io，即文件io 2.网络io，即socket io。socket io和磁盘文件io的区别是什么？socket io需要提供端口，磁盘文件io不需要port，因为磁盘文件io是在本地，而socket io是网络通信，也就是所谓的网络编程。当前服务器端程序在port 8080上监听。

		
		while(true) {
			try {
				
				
				//接受客户端连接
				Socket socket = serverSocket.accept(); //接受客户端连接成功之后，返回(其实是创建)一个新的socket。这个socket又占了一个新的port，不信你可以打印出来看下。如何打印socket port?有API方法。但是没有设置port啊，port从哪里来的？随机设置了一个，而且你会发现随机设置的一般都是几万，目的是为了避免和其他端口冲突。
				
				Thread thread = new Thread(new TaskConnection(socket));
				thread.start();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		
	
	}

}
