package io.daocloud.java.nev.access.runner;

import io.daocloud.java.nev.access.runner.handle.DataSendHandler;
import io.daocloud.java.nev.access.runner.utils.ConfigProperties;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Log4J2LoggerFactory;
import lombok.extern.log4j.Log4j2;

/**
 * Created by yann on 2017/6/23.
 */

@Log4j2
public class NevAccessRunner {

    private static Bootstrap bossGroup = new Bootstrap();
    private static ConfigProperties configProperties = ConfigProperties.configProperties();

    public static void main(String[] args) {
        /*
         * Log4f2 replace the sfl4
         */
        InternalLoggerFactory.setDefaultFactory(Log4J2LoggerFactory.INSTANCE);

        /*
         * Netty Server Service
         */
        EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());

        try {
            bossGroup.group(workerGroup);
            bossGroup.channel(NioSocketChannel.class);
            bossGroup.option(ChannelOption.SO_KEEPALIVE, true);
            bossGroup.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new DataSendHandler());
                }
            });

            for (int i = 0; i < configProperties.connectNumber(); i++) {
                log.info("make {} connect  ", i);
                connect(configProperties.connectIp(), configProperties.connectPort());
            }

            // Start the client.
            ChannelFuture f = connect(configProperties.connectIp(), configProperties.connectPort()).sync();

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();

        } catch (Exception e) {
            log.error("server catch something error, will close soon... ", e);
            workerGroup.shutdownGracefully();
        }
    }


    private static ChannelFuture connect(String host, int port) {
        return bossGroup.connect(host, port).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                System.out.println("Connection established");
            } else {
                System.err.println("Connection attempt failed");
                future.cause().printStackTrace();
            }
        });
    }

}
