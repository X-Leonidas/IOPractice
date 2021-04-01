package cn.xy.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author XiangYu
 * @create2021-04-01-18:52
 */
public class BIOServer1 {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        try {
            ServerSocket server = new ServerSocket(9090);
            Socket socket = server.accept();
            threadPool.execute(() -> handler(socket));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void handler(Socket socket) {

        BufferedInputStream bis = null;


        try {
            System.out.println("线程信息 id =" + Thread.currentThread().getId() + " 名字=" + Thread.currentThread().getName());

            bis = new BufferedInputStream(socket.getInputStream());
            byte[] bytes = new byte[1024];

            int b = 0;
            while ((b = bis.read(bytes)) != -1) {
                System.out.println("线程信息 id =" + Thread.currentThread().getId() + " 名字=" + Thread.currentThread().getName());
                System.out.println(new String(bytes, 0, b));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
