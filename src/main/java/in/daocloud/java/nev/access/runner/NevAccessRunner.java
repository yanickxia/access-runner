package in.daocloud.java.nev.access.runner;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
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

    static Bootstrap bossGroup = new Bootstrap();

    public static void main(String[] args) {
        /*
         * Log4f2 replace the sfl4
         */
        InternalLoggerFactory.setDefaultFactory(Log4J2LoggerFactory.INSTANCE);

        /*
         * Netty Server Service
         */
        EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() - 2);

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

            for (int i = 0; i < 1000; i++) {
                log.info("make connect to access id {}", i);
                connect("10.99.5.100", 15005);
            }

            // Start the client.
            ChannelFuture f = connect("10.99.5.100", 15005).sync();

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();

        } catch (Exception e) {
            log.error("server catch something error, will close soon... ", e);
            workerGroup.shutdownGracefully();
        }
    }


    public static ChannelFuture connect(String host, int port) {
        return bossGroup.connect(host, port);
    }

}
