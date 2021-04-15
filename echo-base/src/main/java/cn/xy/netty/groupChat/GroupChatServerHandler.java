package cn.xy.netty.groupChat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;

/**
 * @author XiangYu
 * @create2021-04-15-18:02
 */
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    //定义一个channle 组，管理所有的channel
    //GlobalEventExecutor.INSTANCE) 是全局的事件执行器，是一个单例
    private  static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    LocalDateTime date = LocalDateTime.now();


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

        channelGroup.writeAndFlush("[客户端]"+channel.remoteAddress() + "加入聊天" + date);
        channelGroup.add(channel);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 上线了~");
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        System.out.println(ctx.channel().remoteAddress() + " 离线了~");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.forEach(ch ->{
            if(ch != channel){
                ch.writeAndFlush("[客户]" + channel.remoteAddress() + " 发送了消息" + msg + "\n");
            }else{
                ch.writeAndFlush("[我]发送了消息" + msg + "\n");
            }
        });


    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //关闭通道
        ctx.close();
    }
}
