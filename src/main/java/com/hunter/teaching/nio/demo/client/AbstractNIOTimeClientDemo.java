package com.hunter.teaching.nio.demo.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public abstract class AbstractNIOTimeClientDemo {

    protected Selector selector;

    protected SocketChannel socketChannel;

    private String hostname;

    private int port;

    private boolean blockMode;

    private boolean multiplexMode;

    public AbstractNIOTimeClientDemo(String hostname, int port, boolean blockMode, boolean multiplexMode) {
        this.hostname = hostname;
        this.port = port;
        this.blockMode = blockMode;
        this.multiplexMode = multiplexMode;
    }

    // initial the time server
    protected void init() throws IOException {
        SocketAddress socketAddress = initSocketAddress(hostname, port);
        initSocketChannel(socketAddress);
        configureBlocking(blockMode);
        configureSelector(multiplexMode);
    }

    private SocketAddress initSocketAddress(String hostname, int port) {
        SocketAddress socketAddress = new InetSocketAddress(hostname, port);
        return socketAddress;
    }

    private void initSocketChannel(SocketAddress socketAddress) throws IOException {
        socketChannel = SocketChannel.open();
        do {
            socketChannel.connect(socketAddress);
        } while (!socketChannel.isConnected());
    }

    private void configureBlocking(boolean blockMode) throws IOException {
        socketChannel.configureBlocking(blockMode);
    }

    private void configureSelector(boolean multiplexMode) throws IOException {
        if (multiplexMode) {
            selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    protected void read() throws IOException {
        try {
            if (socketChannel.isConnected() && socketChannel.isOpen()) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                int readBytes = socketChannel.read(byteBuffer);
                if (readBytes > 0) {
                    byte[] byteArr = new byte[readBytes];
                    byteBuffer.flip();
                    byteBuffer.get(byteArr);
                    String receiveInfo = new String(byteArr);
                    System.out.println("receiveInfo=" + receiveInfo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void closeSocketChannel() {
        try {
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void send(String msg) throws Exception {
        if (socketChannel.isConnected() && socketChannel.isOpen()) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
            try {
                socketChannel.write(byteBuffer);
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    public static class NIODeamonThread implements Runnable {

        private AbstractNIOTimeClientDemo abstractNIOTimeClientDemo;

        private String msg;

        public NIODeamonThread(AbstractNIOTimeClientDemo abstractNIOTimeClientDemo) {
            this.abstractNIOTimeClientDemo = abstractNIOTimeClientDemo;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            try {
                abstractNIOTimeClientDemo.send(getMsg());
                abstractNIOTimeClientDemo.read();
            } catch (Exception e) {
                abstractNIOTimeClientDemo.closeSocketChannel();
                throw new RuntimeException("sth wrong");
            }
        }

    }

}
