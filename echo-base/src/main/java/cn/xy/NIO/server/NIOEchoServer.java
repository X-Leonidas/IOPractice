package cn.xy.NIO.server;

import com.xy.info.HostInfo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author XiangYu
 * @create2020-09-24-10:50
 */
public class NIOEchoServer {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

            //设置通道为非阻塞模式。
            serverSocketChannel.configureBlocking(false);
            //绑定端口
            serverSocketChannel.bind(new InetSocketAddress(HostInfo.port));
            //开启并获得选择器
            Selector selector = Selector.open();

            //将选择器注册到通道上，并将模式设置为监听到了连接就开始连接
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("服务启动成功，监听端口为" + HostInfo.port);


            int selectKeys = selector.select();
            while (selectKeys > 0) {
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey select = iterator.next();
                    if (select.isAcceptable()) {
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        executorService.submit(new EchoClientHandler(socketChannel));
                    }
                    iterator.remove();
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private static class EchoClientHandler implements Runnable {
        private SocketChannel serverSocket;
        private boolean flag = true;

        public EchoClientHandler(SocketChannel serverSocket) {
            this.serverSocket = serverSocket;
            System.out.println("服务器端连接成功，可以与服务器端进行数据的交互操作...");
        }

        @Override
        public void run() {
            ByteBuffer buffer = ByteBuffer.allocate(50);
            try {
                while (flag) {
                    buffer.clear() ;    // 清空缓冲区
                    int length = serverSocket.read(buffer);
                    String clientMessage = new String(buffer.array(), 0, length).trim();
                    System.out.println("【服务器端接收消息】" + clientMessage); // 输出一下提示信息
                    String serverMessage = "[echo]" + clientMessage + "\n";
                    if ("exit".equalsIgnoreCase(clientMessage)) {
                        serverMessage = "byebye!!!!";
                        flag = false;
                    }
                    buffer.clear();
                    buffer.put(serverMessage.getBytes());
                    //写模式- 》读模式
                    buffer.flip();
                    this.serverSocket.write(buffer);
                }
                this.serverSocket.close();
            } catch (IOException e) {

            }
        }
    }
}


