package com.hunter.teaching.netty.demo.client.chapter03;

import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyTimeClientHandlerDemo extends ChannelInboundHandlerAdapter {

    private static final String CLIENT_COMMAND = "Query Time Order\r\n";

    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * send 1000 messages to the server
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 1000; i++) {
            byte[] byteArr = CLIENT_COMMAND.getBytes();
            ByteBuf byteBuf = Unpooled.copiedBuffer(byteArr);
            ctx.writeAndFlush(byteBuf);
        }
    }

    /**
     * It will not appear the tcp stick package and tcp unpacking issues.
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] byteArr = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(byteArr);
        String message = new String(byteArr, Charset.defaultCharset());
        int count = atomicInteger.incrementAndGet();
        System.out.println("Now is : " + message + ", count = " + count);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.print("Unexpected exception from downstream : " + cause.getMessage());
        ctx.close();
    }

}
