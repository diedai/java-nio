package socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 最简单的socket程序-只接受一次连接
 * <pre>
 * @author gzh
 * @date 2019年7月25日 下午9:06:13
 * </pre>
 */
public class ServerMain {

	public static void main(String[] args) {
		try {
			//创建服务器socket
			ServerSocket serverSocket = new ServerSocket(8080); //一个socket对应一个ip/port，计算机io主要是两块1.磁盘io，即文件io 2.网络io，即socket io。socket io和磁盘文件io的区别是什么？socket io需要提供端口，磁盘文件io不需要port，因为磁盘文件io是在本地，而socket io是网络通信，也就是所谓的网络编程。当前服务器端程序在port 8080上监听。
			
			//接受客户端连接
			Socket socket = serverSocket.accept(); //接受客户端连接成功之后，返回(其实是创建)一个新的socket。这个socket又占了一个新的port，不信你可以打印出来看下。如何打印socket port?有API方法。但是没有设置port啊，port从哪里来的？随机设置了一个，而且你会发现随机设置的一般都是几万，目的是为了避免和其他端口冲突。
			
			//读数据
			InputStream read = socket.getInputStream(); //基于socket，既可以读数据(即可以获取读流)，又可以写数据(即可以获取写流)
			int data_read = read.read(); //读一个字节
			System.out.println(data_read);
			
			//写数据
			OutputStream write = socket.getOutputStream();
			int data_write = 2;
			write.write(data_write);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//程序不关闭，目的是有时间打印读写数据
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	}

}
