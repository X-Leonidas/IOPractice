package cn.xy.nio.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author XiangYu
 * @create2021-04-03-17:06 使用一个buffer完成文件的读取
 */
public class NIOFileChannel03 {

    public static void main(String[] args) {
        try (FileInputStream fileInputStream = new FileInputStream("G:\\file01.txt");
             FileOutputStream fileOutputStream = new FileOutputStream("G:\\file02.txt")) {

            FileChannel inputChannel = fileInputStream.getChannel();
            FileChannel outputChannel = fileOutputStream.getChannel();

            ByteBuffer byteBuffer = ByteBuffer.allocate(100);
            while (inputChannel.read(byteBuffer) != -1 ){
                byteBuffer.flip();
                outputChannel.write(byteBuffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
