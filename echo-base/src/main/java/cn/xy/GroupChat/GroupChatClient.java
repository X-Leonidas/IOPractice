package cn.xy.GroupChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author XiangYu
 * @create2021-04-05-15:59
 */
public class GroupChatClient {
    private String username;
    private SocketChannel socketChannel;
    private Selector selector;
    /**
     * 服务器的ip
     */
    private final String HOST = "127.0.0.1";
    /**
     * 服务器端口
     */
    private final int PORT = 7788;


    public GroupChatClient() {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(HOST, PORT);
        try {
            socketChannel = SocketChannel.open(inetSocketAddress);
            selector = Selector.open();
            username = socketChannel.getLocalAddress().toString().substring(1);
            socketChannel.configureBlocking(false);
            //监听读事件
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            System.out.println("初始化连接失败");
            e.printStackTrace();
        }
    }

    private void readInfo() {
        while (true) {
            try {

                int select = selector.select();

                if (select > 0) {
                    Iterator<SelectionKey> ketIterator = selector.selectedKeys().iterator();
                    while (ketIterator.hasNext()) {
                        SelectionKey key = ketIterator.next();
                        if (key.isReadable()) {
                            SocketChannel socketChannel = (SocketChannel) key.channel();
                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                            int read;
                            StringBuilder stringBuilder = new StringBuilder();

                            while ((read = socketChannel.read(byteBuffer)) > 0) {
                                stringBuilder.append(new String(byteBuffer.array(), 0, read));
                                byteBuffer.clear();
                            }
                            System.out.println(stringBuilder.toString().trim());

                        }

                        ketIterator.remove();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }


    }


    private void sendInfo(String info) {
        if (info.length() > 1024 * 100) {
            System.out.println("请减小消息内容");
            return;
        } else {
            try {

                System.out.println("我:"+info);
                info = username + " 说:" + info;
                socketChannel.write(ByteBuffer.wrap(info.getBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        //启动我们客户端
        GroupChatClient chatClient = new GroupChatClient();

        new Thread(() -> {
            chatClient.readInfo();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ).start();

        //发送数据给服务器端
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String s = scanner.nextLine();
            chatClient.sendInfo(s);
        }
    }


}
