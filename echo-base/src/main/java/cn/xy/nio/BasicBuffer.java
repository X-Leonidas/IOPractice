package cn.xy.nio;

import java.nio.IntBuffer;

/**
 * @author XiangYu
 * @create2021-04-03-16:10
 */
public class BasicBuffer {
    public static void main(String[] args) {


        //capacity = 5;limit = 5;
        //
        IntBuffer intBuffer = IntBuffer.allocate(5);


        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i*2);
        }

        //读写反转，
        intBuffer.flip();
        while(intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }

    }
}
