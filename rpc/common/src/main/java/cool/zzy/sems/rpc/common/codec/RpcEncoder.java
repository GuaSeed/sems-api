package cool.zzy.sems.rpc.common.codec;

import cool.zzy.sems.rpc.common.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/1/11 16:21
 * @since 1.0
 */
public class RpcEncoder extends MessageToByteEncoder {
    private static final Logger logger = LoggerFactory.getLogger(RpcEncoder.class);
    private final Class<?> genericClass;
    private final Serializer serializer;

    public RpcEncoder(Class<?> genericClass, Serializer serializer) {
        this.genericClass = genericClass;
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object in, ByteBuf out) throws Exception {
        if (genericClass.isInstance(in)) {
            try {
                byte[] data = serializer.serialize(in);
                out.writeInt(data.length);
                out.writeBytes(data);
            } catch (Exception e) {
                logger.error("Encode error: " + e.toString());
            }
        }
    }
}
