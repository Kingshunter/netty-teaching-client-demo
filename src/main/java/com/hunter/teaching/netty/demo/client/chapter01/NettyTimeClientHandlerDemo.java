package com.hunter.teaching.netty.demo.client.chapter01;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyTimeClientHandlerDemo extends ChannelInboundHandlerAdapter {
    
    private static final String CLIENT_COMMAND = "Query Time Order";
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        byte[] byteArr = CLIENT_COMMAND.getBytes();
        ByteBuf byteBuf = Unpooled.copiedBuffer(byteArr);
        ctx.writeAndFlush(byteBuf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
       ByteBuf byteBuf = (ByteBuf) msg;
       byte[] byteArr = new byte[byteBuf.readableBytes()];
       byteBuf.readBytes(byteArr);
       String message = new String(byteArr, Charset.defaultCharset());
       System.out.println("Now is : " + message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.print("Unexpected exception from downstream : " + cause.getMessage());
        ctx.close();
    }

    

}
