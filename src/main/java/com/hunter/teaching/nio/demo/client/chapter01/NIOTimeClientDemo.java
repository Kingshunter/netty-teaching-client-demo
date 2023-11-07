package com.hunter.teaching.nio.demo.client.chapter01;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.hunter.teaching.nio.demo.client.AbstractNIOTimeClientDemo;

public class NIOTimeClientDemo extends AbstractNIOTimeClientDemo {

    private static final String HOST_NAME = "127.0.0.1";

    private static final int PORT = 6666;

    private SocketAddress socketAddress = new InetSocketAddress(HOST_NAME, PORT);

    public NIOTimeClientDemo() throws IOException {
        init();
    }

    @Override
    protected void selector() {

    }

    @Override
    protected void configureBlocking() {

    }

    @Override
    protected void configureSocketChannel() throws IOException {
        do {
            socketChannel.connect(socketAddress);
        } while (!socketChannel.isConnected());
    }

    public void send() {
        ByteBuffer byteBuffer = ByteBuffer.wrap("Query Time Order".getBytes());
        try {
            socketChannel.write(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read() {
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
        }
    }

    public static void main(String[] args) {
        try {
            NIOTimeClientDemo nioTimeClientDemo = new NIOTimeClientDemo();
            NIODeamonThread nioDeamonThread = new NIODeamonThread(nioTimeClientDemo);
            ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            scheduledExecutorService.scheduleAtFixedRate(nioDeamonThread, 0, 3, TimeUnit.SECONDS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class NIODeamonThread implements Runnable {

        private NIOTimeClientDemo nioTimeClientDemo;

        public NIODeamonThread(NIOTimeClientDemo nioTimeClientDemo) {
            this.nioTimeClientDemo = nioTimeClientDemo;
        }

        @Override
        public void run() {
            nioTimeClientDemo.send();
            nioTimeClientDemo.read();
        }

    }

}
