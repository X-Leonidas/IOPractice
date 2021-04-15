package cn.xy.nio.GroupChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author XiangYu
 * @create2021-04-05-11:35
 */
public class GroupChatServer {
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private final int PORT = 7788;

    public GroupChatServer() {
        try {
            listenChannel = ServerSocketChannel.open();
            listenChannel.bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            selector = Selector.open();
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            System.out.println("socketchannel建立失败");
            e.printStackTrace();
        }


    }

    private void listen() {
        System.out.println("监听线程: " + Thread.currentThread().getName());


        while (true) {
            try {
                int select = selector.select();

                if (select > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        if (key.isAcceptable()) {
                            SocketChannel channel = listenChannel.accept();
                            channel.configureBlocking(false);
                            channel.register(selector, SelectionKey.OP_READ);

                            SocketAddress acceptName = channel.getRemoteAddress();
                            System.out.println("用户" + acceptName + "接入");
                        }


                        if (key.isReadable()) {
                            readData(key);
                        }

                        keyIterator.remove();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void readData(SelectionKey key) {

        SocketChannel channel = null;
        try {
            channel = (SocketChannel) key.channel();

            ByteBuffer buffer = ByteBuffer.allocate(10);

            int read;
            StringBuilder stringBuffer = new StringBuilder();
            while ((read = channel.read(buffer)) > 0) {
                stringBuffer.append(new String(buffer.array(), 0, read));
                buffer.clear();
            }
            //输出该消息
            System.out.println("form 客户端---"  + stringBuffer.toString());
            if ("exit".equals(stringBuffer.toString())) {
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();
                System.out.println(channel.getRemoteAddress() + " 离线了..");
            }
            //广播到其他客户端
            sendInfoToOtherClients(stringBuffer.toString(), channel);
        } catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress() + " 离线了..");
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();
            } catch (IOException e2) {
                e2.printStackTrace();
                ;
            }
        }

    }

    private void sendInfoToOtherClients(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中...");
        System.out.println("服务器转发数据给客户端线程: " + Thread.currentThread().getName());
        for (SelectionKey key : selector.keys()) {
            //通过 key  取出对应的 SocketChannel
            Channel targetChannel = key.channel();

            if (targetChannel instanceof SocketChannel && targetChannel != self) {
                //转型
                SocketChannel dest = (SocketChannel) targetChannel;
                //将msg 存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //将buffer 的数据写入 通道
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();

        groupChatServer.listen();
    }

}
