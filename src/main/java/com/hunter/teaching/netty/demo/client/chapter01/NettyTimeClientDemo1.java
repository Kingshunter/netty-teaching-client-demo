package com.hunter.teaching.netty.demo.client.chapter01;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * This Demo is not consider the tcp stick package or tcp unpacking
 */
public class NettyTimeClientDemo1 {

    public void connect(String ip, int port) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
            .option(ChannelOption.TCP_NODELAY, true)
            .handler(new NettyHandlerClientDemo1());
            // async connect operation
            ChannelFuture f = b.connect(ip, port).sync();
            // wait the client close the channel
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
    
    private class NettyHandlerClientDemo1 extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel sc) throws Exception {
            sc.pipeline().addLast(new NettyTimeClientHandlerDemo1());
        }
        
    }
    
    public static void main(String[] args) throws Exception {
        NettyTimeClientDemo1 nettyTimeClientDemo = new NettyTimeClientDemo1();
        nettyTimeClientDemo.connect("127.0.0.1", 10080);
    }
    
}
