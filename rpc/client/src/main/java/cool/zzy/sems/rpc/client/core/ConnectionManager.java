package cool.zzy.sems.rpc.client.core;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/1/12 17:20
 * @since 1.0
 */
public class ConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 8,
            600L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(1000));
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);
    private RpcClientHandler handler;
    private ReentrantLock lock = new ReentrantLock();
    private Condition connected = lock.newCondition();
    private long waitTimeout = 5000;
    private InetSocketAddress remotePeer;

    public RpcClientHandler getHandler() {
        while (this.handler == null) {
            try {
                waitingHandler();
                if (remotePeer != null) {
                    connectServerNode(remotePeer);
                }
            } catch (InterruptedException e) {
                logger.error("Waiting for available service is interrupted!", e);
            }
        }
        return this.handler;
    }

    private void signalAvailableHandler() {
        lock.lock();
        try {
            connected.signalAll();
        } finally {
            lock.unlock();
        }
    }

    private boolean waitingHandler() throws InterruptedException {
        lock.lock();
        try {
            logger.warn("Waiting for available service");
            return connected.await(this.waitTimeout, TimeUnit.MILLISECONDS);
        } finally {
            lock.unlock();
        }
    }

    public void stop() {
        getHandler().close(channelFuture -> {
            logger.info("finish the peer {}", handler.remotePeer);
            handler = null;
        });
        signalAvailableHandler();
    }

    private static class SingletonHandler {
        private static final ConnectionManager INSTANCE = new ConnectionManager();
    }

    public static ConnectionManager getInstance() {
        return SingletonHandler.INSTANCE;
    }

    public void connectServerNode(String host, int port) {
        connectServerNode(new InetSocketAddress(host, port));
    }

    public void connectServerNode(InetSocketAddress address) {
        this.remotePeer = address;
        threadPoolExecutor.submit(() -> {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new RpcClientInitializer());

            ChannelFuture channelFuture = bootstrap.connect(remotePeer);
            channelFuture.addListener((ChannelFutureListener) cf -> {
                if (cf.isSuccess()) {
                    logger.info("Successfully connect to remote server, remote peer = " + remotePeer);
                    this.handler = channelFuture.channel().pipeline().get(RpcClientHandler.class);
                    signalAvailableHandler();
                } else {
                    logger.error("Can not connect to remote server, remote peer = " + remotePeer);
                }
            });
        });
    }
}
