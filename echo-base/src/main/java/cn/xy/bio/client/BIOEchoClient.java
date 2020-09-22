package cn.xy.bio.client;

import com.xy.info.HostInfo;
import com.xy.util.InputUtil;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author XiangYu
 * @create2020-09-22-15:47
 */
public class BIOEchoClient {

    private AtomicInteger i = new AtomicInteger(1);

    public static void main(String[] args) throws IOException {
        new BIOEchoClient().multiClient();
    }

    public void singleClient() throws IOException {
        Socket socket = null;
        Scanner scanner = null;
        PrintStream out = null;
        try {
            socket = new Socket(HostInfo.HOST_NAME, HostInfo.port);
            //获取服务端相应的数据
            scanner = new Scanner(socket.getInputStream());
            scanner.useDelimiter("\n");
            //向服务端发送的数据内容
            out = new PrintStream(socket.getOutputStream());
            boolean flag = true;
            while (flag) {
                String inputdata = InputUtil.getString("输入要发送的内容");
                out.println(inputdata);
                if (scanner.hasNext()) {
                    String str = scanner.next().trim();
                    System.out.println(str);
                }
                if (inputdata.equalsIgnoreCase("exit")) {
                    flag = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
            scanner.close();
            socket.close();

        }
    }

    /**
     * 模拟多个客户端访问，了解bio的同步阻塞特性
     */
    private void multiClient() {
        for (; i.intValue() < 20; ) {
            new Thread(() -> {
                Socket socket = null;
                Scanner scanner = null;
                PrintStream out = null;
                try {
                    socket = new Socket(HostInfo.HOST_NAME, HostInfo.port);
                    //获取服务端相应的数据
                    scanner = new Scanner(socket.getInputStream());
                    scanner.useDelimiter("\n");
                    //向服务端发送的数据内容
                    out = new PrintStream(socket.getOutputStream());

                    int j = i.getAndAdd(1);
                    String inputdata = "当前连接为" + j + "个线程";
                    out.println(inputdata);

                    if (scanner.hasNext()) {
                        String str = scanner.next().trim();
                        System.out.println(str);
                    }
                    inputdata = "exit";
                    out.println(inputdata);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    out.close();
                    scanner.close();
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).run();

        }

    }


}
