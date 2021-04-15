package cn.xy.netty.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 单reactor多线程处理
 */
public class MThreadHandler implements Runnable {
    private SocketChannel channel;

    private SelectionKey selectionKey;

    ByteBuffer input = ByteBuffer.allocate(1024);
    ByteBuffer output = ByteBuffer.allocate(1024);
    static final int READING = 0, SENDING = 1;
    int state = READING;


    ExecutorService pool = Executors.newFixedThreadPool(2);
    static final int PROCESSING = 3;

    public MThreadHandler(Selector selector, SocketChannel c) throws IOException {
        this.channel = c;
        c.configureBlocking(false);
        // Optionally try first read now
        this.selectionKey = this.channel.register(selector, 0);

        //将Handler作为callback对象
        this.selectionKey.attach(this);

        //第二步,注册Read就绪事件
        this.selectionKey.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    private boolean inputIsComplete() {
        /* ... */
        return false;
    }

    private boolean outputIsComplete() {
        /* ... */
        return false;
    }

    private void process() {
        /* ... */
        return;
    }

    @Override
    public void run() {
        try {
            if (state == READING) {
                read();
            } else if (state == SENDING) {
                send();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private synchronized void read() throws IOException {
        // ...
        channel.read(input);
        if (inputIsComplete()) {
            state = PROCESSING;
            //使用线程pool异步执行
            pool.execute(new Processer());
        }
    }

    private void send() throws IOException {
        channel.write(output);

        //write完就结束了, 关闭select key
        if (outputIsComplete()) {
            selectionKey.cancel();
        }
    }

    private synchronized void processAndHandOff() {
        process();
        state = SENDING;
        // or rebind attachment
        //process完,开始等待write就绪
        selectionKey.interestOps(SelectionKey.OP_WRITE);
    }

    private class Processer implements Runnable {
        @Override
        public void run() {
            processAndHandOff();
        }
    }
}