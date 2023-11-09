package com.hunter.teaching.nio.demo.client.chapter01;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOTimeBlockingClientDemo {

    private static final int PORT = 6666;

    private static final String MSG = "Query Time Order";

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        SocketAddress socketAddress = new InetSocketAddress(PORT);
        do {
            if (!socketChannel.isConnectionPending())
                socketChannel.connect(socketAddress);
        } while (!socketChannel.isConnected() && !socketChannel.finishConnect());

        send(socketChannel, MSG);
        read(socketChannel);
        closeSocketChannel(socketChannel);
    }

    private static void read(SocketChannel socketChannel) throws IOException {
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int readBytes = socketChannel.read(byteBuffer);
            if (readBytes > 0) {
                byte[] byteArr = new byte[readBytes];
                byteBuffer.flip();
                byteBuffer.get(byteArr);
                String receiveInfo = new String(byteArr);
                System.out.println("receiveInfo=" + receiveInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static void closeSocketChannel(SocketChannel socketChannel) {
        try {
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void send(SocketChannel socketChannel, String msg) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
        try {
            socketChannel.write(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

}
