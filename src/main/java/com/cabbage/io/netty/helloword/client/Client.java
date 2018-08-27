package com.cabbage.io.netty.helloword.client;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    public static void main(String[] args) {
        ClientBootstrap bootstrap = new ClientBootstrap();

        ExecutorService boss = Executors.newCachedThreadPool();
        ExecutorService worker = Executors.newCachedThreadPool();

        bootstrap.setFactory(new NioClientSocketChannelFactory(boss,worker));
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline =  Channels.pipeline();
                pipeline.addLast("decoder",new StringDecoder());
                pipeline.addLast("encoder",new StringEncoder());
                pipeline.addLast("helloHandler",new HelloClientHandler());
                return  pipeline;
            }
        });

        ChannelFuture connect = bootstrap.connect(new InetSocketAddress("localhost",10101));
        Channel channel = connect.getChannel();


        System.out.println("client start");

        Scanner scanner  = new Scanner(System.in);

        while (true){
            System.out.println("请输入~~~");
            channel.write(scanner.next());
        }

    }
}
