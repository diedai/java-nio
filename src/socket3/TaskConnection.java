package socket3;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TaskConnection implements Runnable {
	Socket socket;
	
	TaskConnection(Socket socket){
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
		//读数据
		InputStream read = socket.getInputStream(); //基于socket，既可以读数据(即可以获取读流)，又可以写数据(即可以获取写流)
		int data_read = read.read(); //读一个字节
		System.out.println(data_read + ", Thread.currentThread().getName():" + Thread.currentThread().getName());
		
		//写数据
		OutputStream write = socket.getOutputStream();
		int data_write = 2;
		write.write(data_write);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

/*
 * Runnable 怎么获取线程的名字？
 * 获取线程名字这件事情本质上和Runnable是没有关系的。一个Runnable可以给多个线程去运行，所以如果在这个概念上你有误解的话，希望重新考虑一下。
 * 另外，在任何时候，你都可以用Thread.currentThread().getName()来获取当前线程的名字
 */

