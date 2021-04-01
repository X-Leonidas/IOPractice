package cn.xy.echo.NIO.client;

import com.xy.info.HostInfo;
import com.xy.util.InputUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author XiangYu
 * @create2020-09-24-17:15
 */
public class NIOEchoClient {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(50);
        SocketChannel socketChannel = null;

        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(HostInfo.HOST_NAME, HostInfo.port));
            boolean flag = true;
            while (flag) {
                buffer.clear();
                String clientMsg = InputUtil.getString("NIO:请输入");
                buffer.put(clientMsg.getBytes());
                //写模式-》读模式
                buffer.flip();
                socketChannel.write(buffer);
                buffer.clear();
                int length = socketChannel.read(buffer);
                buffer.flip();
                String serverMsg = new String(buffer.array(), 0, length);
                System.out.println("服务端发送的消息为"+serverMsg);
                if ("exit".equalsIgnoreCase(clientMsg)) {
                    flag = false;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(socketChannel != null){
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
