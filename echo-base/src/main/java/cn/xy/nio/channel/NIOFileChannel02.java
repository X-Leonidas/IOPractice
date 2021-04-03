package cn.xy.nio.channel;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author XiangYu
 * @create2021-04-03-16:57 在本地文件读取数据
 */
public class NIOFileChannel02 {
    public static void main(String[] args) {
        try (FileInputStream fileInputStream = new FileInputStream("G:\\file01.txt")) {
            FileChannel fileChannel = fileInputStream.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            int read;
            while ((read = fileChannel.read(byteBuffer)) != -1) {
                String s = new String(byteBuffer.array(), 0, read);
                System.out.println(s);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
