package com.hunter.teaching.nio.demo.client.chapter06;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import io.netty.channel.unix.Socket;

public class NIOTimeClientDemo {
    
    private Selector selector;
    
    private SocketChannel socketChannel;
    
    private InetSocketAddress inetSocketAddress = InetSocketAddress.createUnresolved("127.0.0.1", 6666);

    public NIOTimeClientDemo() {
        init();
    }

    private void init() {
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public void connect(SocketChannel socketChannel) {
        if (socketChannel.isOpen()) {
            try {
                if (!socketChannel.isConnected() && !socketChannel.finishConnect()) {
                    socketChannel.configureBlocking(false);
                    boolean connected = socketChannel.connect(inetSocketAddress);
                    if (!connected) {
                        while (socketChannel.isConnectionPending()) {
                            socketChannel.connect(inetSocketAddress);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            closeSocketChannel(socketChannel);
        }
        
    }
    
    private void closeSocketChannel(SocketChannel socketChannel) {
        if (socketChannel != null) {
            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
