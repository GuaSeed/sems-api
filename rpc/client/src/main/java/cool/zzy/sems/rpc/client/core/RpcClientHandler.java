package cool.zzy.sems.rpc.client.core;

import cool.zzy.sems.rpc.client.proxy.RpcFuture;
import cool.zzy.sems.rpc.common.codec.Beat;
import cool.zzy.sems.rpc.common.codec.RpcRequest;
import cool.zzy.sems.rpc.common.codec.RpcResponse;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/1/12 17:01
 * @since 1.0
 */
public class RpcClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    private static final Logger logger = LoggerFactory.getLogger(RpcClientHandler.class);
    private ConcurrentHashMap<String, RpcFuture> pendingRPC = new ConcurrentHashMap<>();
    private volatile Channel channel;
    public SocketAddress remotePeer;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.remotePeer = this.channel.remoteAddress();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse rpcResponse) throws Exception {
        String requestId = rpcResponse.getRequestId();
        logger.debug("Receive response: " + requestId);
        RpcFuture rpcFuture = pendingRPC.get(requestId);
        if (rpcFuture != null) {
            pendingRPC.remove(requestId);
            rpcFuture.done(rpcResponse);
        } else {
            logger.warn("Can not get pending response for request id: " + requestId);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("Client caught exception: " + cause.getMessage());
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateHandler) {
            sendRequest(Beat.BEAT_PING);
            logger.debug("Client send beat-ping to {}", remotePeer);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    public void close(ChannelFutureListener channelFutureListener) {
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListeners(ChannelFutureListener.CLOSE, channelFutureListener);
    }

    public RpcFuture sendRequest(RpcRequest request) {
        RpcFuture rpcFuture = new RpcFuture(request);
        pendingRPC.put(request.getRequestId(), rpcFuture);
        try {
            ChannelFuture channelFuture = channel.writeAndFlush(request).sync();
            if (!channelFuture.isSuccess()) {
                logger.error("Send request {} error", request.getRequestId());
            }
        } catch (InterruptedException e) {
            logger.error("Send request exception {}", e.getMessage());
        }
        return rpcFuture;
    }
}
