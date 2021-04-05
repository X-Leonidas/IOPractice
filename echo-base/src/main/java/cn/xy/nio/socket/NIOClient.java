package cn.xy.nio.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author XiangYu
 * @create2021-04-04-16:07
 */
public class NIOClient {
    public static void main(String[] args) {

        try (SocketChannel socketChannel = SocketChannel.open()) {
            socketChannel.configureBlocking(false);
            InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 7788);


            //连接服务器
            if (!socketChannel.connect(inetSocketAddress)) {

                while (!socketChannel.finishConnect()) {
                    System.out.println("因为连接需要时间，客户端不会阻塞，可以做其它工作..");
                }
            }
            String str = "hello,socket1";

            ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());
            socketChannel.write(byteBuffer);
            socketChannel.close();
            //System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
