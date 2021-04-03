package cn.xy.nio.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author XiangYu
 * @create2021-04-03-17:43
 *  使用transferFrom完成拷贝   实现原理 sendfile
 */
public class NIOFileChannel04 {
    public static void main(String[] args) {
        try (FileInputStream fileInputStream = new FileInputStream("G:\\file01.txt");
             FileOutputStream fileOutputStream = new FileOutputStream("G:\\file02.txt")) {

            FileChannel inputChannel = fileInputStream.getChannel();
            FileChannel outputChannel = fileOutputStream.getChannel();

            outputChannel.transferFrom(inputChannel,0,inputChannel.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
