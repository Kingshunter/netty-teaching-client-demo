package com.hunter.teaching.nio.demo.client;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public abstract class AbstractNIOTimeClientDemo {

    protected Selector selector;

    protected SocketChannel socketChannel;

    // initial the time server
    protected void init() throws IOException {
        selector();
        socketChannel = SocketChannel.open();
        configureBlocking();
        configureSocketChannel();
        register();
    }

    protected abstract void selector();

    protected abstract void configureBlocking();

    protected abstract void configureSocketChannel() throws IOException;

    protected void register() throws ClosedChannelException {
        if (selector != null) {
            socketChannel.register(selector, SelectionKey.OP_ACCEPT);
        }
    }

}
