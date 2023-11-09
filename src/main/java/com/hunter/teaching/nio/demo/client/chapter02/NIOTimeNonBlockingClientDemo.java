package com.hunter.teaching.nio.demo.client.chapter02;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

public class NIOTimeNonBlockingClientDemo {

    private static final String HOST_NAME = "127.0.0.1";

    private static final int PORT = 6666;

    private static final String MSG = "Query Time Order";
    
    private static int LOOP = 10;

    public static void main(String[] args) throws IOException, InterruptedException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        SocketAddress socketAddress = new InetSocketAddress(HOST_NAME, PORT);
        do {
            if (!socketChannel.isConnectionPending())
                socketChannel.connect(socketAddress);
        } while (!socketChannel.isConnected() && !socketChannel.finishConnect());

        do {
            send(socketChannel, MSG);
            read(socketChannel);
            LOOP--;
            TimeUnit.SECONDS.sleep(5);
        } while (LOOP > 0);
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
