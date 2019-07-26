package socket3;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

/**
 * 最简单的客户端socket
 * <pre>
 * @author gzh
 * @date 2019年7月25日 下午10:20:47
 * </pre>
 */
public class ClientMain {

	public static void main(String[] args) {
		Socket socket;
		
		while(true) {
			try {
				//创建客户端socket
				socket = new Socket("127.0.0.1", 8080); //客户端socket的port是随机分配的，而且每次随机分配的port值不一样
				
				//连接服务器socket
//				InetSocketAddress address = new InetSocketAddress(8080);
//				socket.connect(address); //连接到服务器port8080 //这样写连接不上，必须显式写ip地址
				
				//写数据
				OutputStream write = socket.getOutputStream();
				write.write(1);
				
				//读数据
				InputStream read = socket.getInputStream();
				int data_read = read.read(); //只读一个字节
				System.out.println(data_read);
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
