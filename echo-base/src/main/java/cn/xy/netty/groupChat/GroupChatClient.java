package cn.xy.netty.groupChat;

import cn.xy.netty.ConfigInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

/**
 * @author XiangYu
 * @create2021-04-15-18:16
 */
public class GroupChatClient {

    private final String host;
    private final int port;

    public GroupChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }


    public void run() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //加入相关handler
                            pipeline.addLast("decoder", new StringDecoder());
                            pipeline.addLast("encoder", new StringEncoder());
                            //加入自定义的handler
                            pipeline.addLast(new GroupChatClientHandler());
                        }
                    });


            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            Channel channel = channelFuture.channel();

            System.out.println("-------" + channel.localAddress() + "--------");

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String msg = scanner.nextLine();
                channel.writeAndFlush(msg);
            }
        }finally {
            group.shutdownGracefully();
        }
    }


    public static void main(String[] args) throws InterruptedException {

        new GroupChatClient(ConfigInfo.HOST, ConfigInfo.PORT).run();
    }

}
