package cn.xy.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author XiangYu
 * @create2021-04-03-21:04
 * mmap  与虚拟内存建立映射
 */
public class MyMappedByteBuffer {
    public static void main(String[] args) {
        try(RandomAccessFile randomAccessFile = new RandomAccessFile("", "rw")){
            FileChannel channel = randomAccessFile.getChannel();

            /**
             * 5： 映射到内存的大小
             * 映射到内存的大小就是可以直接修改的范围
             */
            MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);


            mappedByteBuffer.put(0,(byte)'！');
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
