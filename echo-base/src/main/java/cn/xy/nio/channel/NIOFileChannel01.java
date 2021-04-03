package cn.xy.nio.channel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author XiangYu
 * @create2021-04-03-16:45
 * 本地文件写
 *
 */
public class NIOFileChannel01 {
    public static void main(String[] args) {
        String str = "hello,channel01";

        try(  FileOutputStream fileOutputStream = new FileOutputStream("G:\\file01.txt")){
            FileChannel fileChannel = fileOutputStream.getChannel();

            //申请一个1024字节的byteBuffer
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            byteBuffer.put(str.getBytes());
            byteBuffer.flip();
            fileChannel.write(byteBuffer);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
