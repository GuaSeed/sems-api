package cool.zzy.sems.rpc.server.core;

import cool.zzy.sems.rpc.common.util.ServiceUtils;
import cool.zzy.sems.rpc.common.util.ThreadPoolUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/1/11 14:57
 * @since 1.0
 */
public class NettyServer implements Server {
    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);
    private final String serverAddress;
    private Map<String, Object> serviceMap = new HashMap<>();
    private Thread thread;

    public NettyServer(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void addService(String interfaceName, int version, Object interfaceBean) {
        logger.info("Adding service, interface: {}, version: {}, bean：{}", interfaceName, version, interfaceBean);
        this.serviceMap.put(ServiceUtils.generateKey(interfaceName, version), interfaceBean);
    }

    @Override
    public void start() {
        ThreadPoolExecutor threadPoolExecutor = ThreadPoolUtils.makeServerThreadPool(
                NettyServer.class.getSimpleName(), 16, 32);
        this.thread = new Thread(() -> {
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap bootstrap = new ServerBootstrap()
                        .channel(NioServerSocketChannel.class)
                        .group(bossGroup, workGroup)
                        .option(ChannelOption.SO_BACKLOG, 128)
                        .childHandler(new RpcServerInitializer(serviceMap, threadPoolExecutor))
                        .childOption(ChannelOption.SO_KEEPALIVE, true);
                String[] array = serverAddress.split(":");
                String host = array[0];
                int port = Integer.parseInt(array[1]);
                ChannelFuture future = bootstrap.bind(host, port).sync();
                logger.info("Server started on port {}", port);
                future.channel().closeFuture().sync();
            } catch (Exception e) {
                if (e instanceof InterruptedException) {
                    logger.info("Rpc server remoting server stop");
                } else {
                    logger.error("Rpc server remoting server error", e);
                }
            } finally {
                bossGroup.shutdownGracefully();
                workGroup.shutdownGracefully();
            }
        });
        this.thread.start();
    }

    @Override
    public void stop() {
        // destroy server thread
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
            logger.info("Rpc server stop.");
        }
    }
}
