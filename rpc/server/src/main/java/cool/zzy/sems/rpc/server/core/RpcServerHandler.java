package cool.zzy.sems.rpc.server.core;

import cool.zzy.sems.rpc.common.codec.Beat;
import cool.zzy.sems.rpc.common.codec.RpcRequest;
import cool.zzy.sems.rpc.common.codec.RpcResponse;
import cool.zzy.sems.rpc.common.util.ServiceUtils;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/1/11 15:51
 * @since 1.0
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private static final Logger logger = LoggerFactory.getLogger(RpcServerHandler.class);

    private final Map<String, Object> handlerMap;
    private final ThreadPoolExecutor serverHandlerPool;

    public RpcServerHandler(Map<String, Object> handlerMap, ThreadPoolExecutor serverHandlerPool) {
        this.handlerMap = handlerMap;
        this.serverHandlerPool = serverHandlerPool;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest rpcRequest) throws Exception {
        if (Beat.BEAT_ID.equalsIgnoreCase(rpcRequest.getRequestId())) {
            logger.info("Server read heartbeat ping {}", ctx.channel().remoteAddress());
            return;
        }

        serverHandlerPool.execute(() -> {
            logger.info("Receive request " + rpcRequest.getRequestId());
            RpcResponse rpcResponse = new RpcResponse();
            rpcResponse.setRequestId(rpcRequest.getRequestId());
            try {
                Object result = handle(rpcRequest);
                rpcResponse.setResult(result);
            } catch (Throwable t) {
                Throwable throwable = t;
                while (throwable.getMessage() == null) {
                    throwable = t.getCause();
                    rpcResponse.setError(throwable.getMessage());
                }
                logger.error("RPC Server handle request error: {}", rpcResponse.getError());
            }
            ctx.writeAndFlush(rpcResponse).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    logger.info("Send response for request " + rpcRequest.getRequestId());
                }
            });
        });
    }

    private Object handle(RpcRequest rpcRequest) throws Throwable {
        String className = rpcRequest.getClassName();
        int version = rpcRequest.getVersion();
        String serviceKey = ServiceUtils.generateKey(className, version);
        Object serviceBean = handlerMap.get(serviceKey);
        if (serviceBean == null) {
            logger.error("Can not find service implement with interface name: {} and version: {}", className, version);
            return null;
        }
        Class<?> serviceClass = serviceBean.getClass();
        String methodName = rpcRequest.getMethodName();
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        Object[] parameters = rpcRequest.getParameters();
        logger.debug(serviceClass.getName());
        logger.debug(methodName);
        for (Class<?> parameterType : parameterTypes) {
            logger.debug(parameterType.getName());
        }
        for (Object parameter : parameters) {
            logger.debug(parameter.toString());
        }
        Method method = serviceClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(serviceBean, parameters);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn("Server caught exception: " + cause.getMessage());
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            ctx.channel().close();
            logger.warn("Channel idle in last {} seconds, close it", Beat.BEAT_TIMEOUT);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
