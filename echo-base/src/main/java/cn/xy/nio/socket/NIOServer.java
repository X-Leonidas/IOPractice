package cn.xy.nio.socket;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author XiangYu
 * @create2021-04-04-15:24
 */
public class NIOServer {

    public static void main(String[] args) {

        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {

            serverSocketChannel.socket().bind(new InetSocketAddress(7788));
            //非阻塞
            serverSocketChannel.configureBlocking(false);

            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("注册后的selectionkey 数量=" + selector.keys().size());

            while (true) {
                if (selector.select(3000) == 0) {
                    System.out.println("服务器等待了3秒，无连接");
                    continue;
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                System.out.println("selectionKeys 数量 = " + selectionKeys.size());

                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isAcceptable()) {
                        SocketChannel serverSocket = serverSocketChannel.accept();
                        System.out.println("客户端连接成功 生成了一个 socketChannel " + serverSocket.hashCode());
                        serverSocket.configureBlocking(false);
                        serverSocket.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                        System.out.println("客户端连接后 ，注册的selectionkey 数量=" + selector.keys().size());
                    }

                    if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer byteBuffer = (ByteBuffer) key.attachment();

                       if(channel.read(byteBuffer) != -1) {
                           System.out.println("form 客户端 " + new String(byteBuffer.array()));
                       }

                    }
                }
                selectionKeys.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
