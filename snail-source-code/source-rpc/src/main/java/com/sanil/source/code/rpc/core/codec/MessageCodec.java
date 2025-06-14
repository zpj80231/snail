package com.sanil.source.code.rpc.core.codec;

import com.sanil.source.code.rpc.core.compress.Compress;
import com.sanil.source.code.rpc.core.config.RpcConfig;
import com.sanil.source.code.rpc.core.enums.CompressEnum;
import com.sanil.source.code.rpc.core.enums.SerializerEnum;
import com.sanil.source.code.rpc.core.exception.RpcException;
import com.sanil.source.code.rpc.core.extension.ExtensionLoader;
import com.sanil.source.code.rpc.core.factory.MessageTypeFactory;
import com.sanil.source.code.rpc.core.message.Message;
import com.sanil.source.code.rpc.core.serialize.Serializer;
import com.sanil.source.code.rpc.core.util.NettyAttrUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 消息编解码器
 *
 * @author zhangpengjun
 * @date 2025/5/7
 */
@Slf4j
@ChannelHandler.Sharable
public class MessageCodec extends MessageToMessageCodec<ByteBuf, Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        RpcConfig config = NettyAttrUtil.getRpcConfig(ctx.channel());
        try {
            // 压缩方式
            String compressName = config.getCompress();
            byte compressType = CompressEnum.getType(compressName);
            Compress compress = ExtensionLoader.getExtensionLoader(Compress.class).getExtension(compressName);
            // 获取序列化方式
            String serializerName = config.getSerializer();
            byte serializerType = SerializerEnum.getType(serializerName);
            Serializer serializer = ExtensionLoader.getExtensionLoader(Serializer.class).getExtension(serializerName);

            int magicNum = config.getMagicNum();
            byte version = config.getVersion();
            int messageType = msg.getMessageType().getType();
            long sequenceId = msg.getSequenceId();

            ByteBuf buf = ctx.alloc().buffer();
            // 4个字节的魔数
            buf.writeInt(magicNum);
            // 1个字节的版本号
            buf.writeByte(version);
            // 1个字节的序列化方式
            buf.writeByte(serializerType);
            // 1个字节的压缩算法
            buf.writeByte(compressType);
            // 1个填充字节，补齐长度
            buf.writeBytes(new byte[]{0});
            // 4个字节的消息指令类型
            buf.writeInt(messageType);
            // 8个字节的消息序号
            buf.writeLong(sequenceId);
            // 4个字节的消息体长度
            byte[] bytes = serializer.serialize(msg);
            bytes = compress.compress(bytes);
            buf.writeInt(bytes.length);
            // 消息体
            buf.writeBytes(bytes);

            // log.debug("magicNum: {}, version: {}, serializerType: {}, messageType: {}, sequenceId: {}, length: {}",
            //         magicNum, version, serializerType, messageType, sequenceId, bytes.length);
            // log.debug("ByteBuf encode: {}", ByteBufUtil.prettyHexDump(buf));

            out.add(buf);
        } catch (Exception e) {
            throw new RpcException("编码过程中发生异常，请检查相关配置和数据。", e);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        // log.debug("ByteBuf decode: {}", ByteBufUtil.prettyHexDump(buf));
        try {
            // 4个字节的魔数
            int magicNum = buf.readInt();
            // 1个字节的版本号
            byte version = buf.readByte();
            // 1个字节的序列化方式
            byte serializerType = buf.readByte();
            String serializerName = SerializerEnum.getName(serializerType);
            // 1个字节，压缩算法
            byte compressType = buf.readByte();
            String compressName = CompressEnum.getName(compressType);
            // 1个填充字节，补齐长度
            buf.readBytes(1);
            // 4个字节的消息指令类型
            int messageType = buf.readInt();
            // 8个字节的消息序号
            long sequenceId = buf.readLong();
            // 4个字节的消息体长度
            int length = buf.readInt();
            // 消息体
            byte[] bytes = new byte[length];
            buf.readBytes(bytes, 0, length);

            // log.debug("magicNum: {}, version: {}, serializerType: {}, messageType: {}, sequenceId: {}, length: {}",
            //         magicNum, version, serializerType, messageType, sequenceId, length);

            // 解压缩
            Compress compress = ExtensionLoader.getExtensionLoader(Compress.class).getExtension(compressName);
            bytes = compress.decompress(bytes);
            // 反序列化
            Serializer serializer = ExtensionLoader.getExtensionLoader(Serializer.class).getExtension(serializerName);
            Class<? extends Message> messageClass = MessageTypeFactory.getInstance().getMessageType(messageType);
            Message message = serializer.deserialize(bytes, messageClass);

            out.add(message);
        } catch (Exception e) {
            throw new RpcException("解码过程中发生异常，请检查相关配置和数据。", e);
        }
    }

}
