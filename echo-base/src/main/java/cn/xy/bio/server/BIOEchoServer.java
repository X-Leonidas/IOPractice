package cn.xy.bio.server;

import com.xy.info.HostInfo;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author XiangYu
 * @create2020-09-22-15:10
 */
public class BIOEchoServer {
    //使用线程池提高bio的性能
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(HostInfo.port);
        System.out.println("服务监听线程已经启动端口为" + HostInfo.port);
        //使用固定线程池提高bio的性能
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        try {
            while (true) {
                Socket client = serverSocket.accept();
                executorService.submit(new EchoClientHandler(client));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
            serverSocket.close();
        }
    }


    /**
     * 成员内部类
     * 结束客户端发送的消息并处理
     */
    private static class EchoClientHandler implements Runnable {
        private Socket client;
        private Scanner scanner;
        private PrintStream out;


        EchoClientHandler(Socket client) {
            this.client = client;
            try {
                this.scanner = new Scanner(client.getInputStream());
                //设置换行符号
                scanner.useDelimiter("\n");
                this.out = new PrintStream(client.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            boolean flag = true;
            while (flag) {
                if (scanner.hasNext()) {
                    String input = scanner.next();
                    System.out.println("server端接收的消息为"+input);
                    if (input.equalsIgnoreCase("exit")) {
                        out.println("【echo】:ByeBye!");
                        flag = false;
                    } else {
                        out.println("【echo】:" + input);
                    }
                }
            }
            this.out.close();
            this.scanner.close();
        }
    }
}


