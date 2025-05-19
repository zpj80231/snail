package com.sanil.source.code.rpc.core.codec;

import com.sanil.source.code.rpc.core.config.RpcConfig;
import com.sanil.source.code.rpc.core.factory.MessageTypeFactory;
import com.sanil.source.code.rpc.core.factory.SerializerFactory;
import com.sanil.source.code.rpc.core.message.Message;
import com.sanil.source.code.rpc.core.serialize.Serializer;
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
        // 获取序列化方式
        String serializerName = RpcConfig.getSerializer();
        int serializerType = SerializerFactory.getSerializerType(serializerName);

        int magicNum = 8023;
        byte version = 1;
        int messageType = msg.getMessageType().getType();
        long sequenceId = msg.getSequenceId();

        ByteBuf buf = ctx.alloc().buffer();
        // 4个字节的魔数
        buf.writeInt(magicNum);
        // 1个字节的版本号
        buf.writeByte(version);
        // 1个字节的序列化方式（jdk：0，json：1）
        buf.writeByte(serializerType);
        // 2个填充字节，补齐长度
        buf.writeBytes(new byte[]{0, 0});
        // 4个字节的消息指令类型
        buf.writeInt(messageType);
        // 8个字节的消息序号
        buf.writeLong(sequenceId);
        // 4个字节的消息体长度
        Serializer serializer = SerializerFactory.getSerializer(serializerType);
        byte[] bytes = serializer.serialize(msg);
        buf.writeInt(bytes.length);
        // 消息体
        buf.writeBytes(bytes);

        // log.debug("magicNum: {}, version: {}, serializerType: {}, messageType: {}, sequenceId: {}, length: {}",
        //         magicNum, version, serializerType, messageType, sequenceId, bytes.length);
        // log.debug("ByteBuf: {}", ByteBufUtil.hexDump(buf));
        out.add(buf);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        // 4个字节的魔数
        int magicNum = buf.readInt();
        // 1个字节的版本号
        byte version = buf.readByte();
        // 1个字节的序列化方式（jdk：0，json：1）
        byte serializerType = buf.readByte();
        // 2个填充字节，补齐长度
        buf.readBytes(2);
        // 4个字节的消息指令类型
        int messageType = buf.readInt();
        // 8个字节的消息序号
        long sequenceId = buf.readLong();
        // 4个字节的消息体长度
        int length = buf.readInt();
        // 消息体
        byte[] bytes = new byte[length];
        buf.readBytes(bytes, 0, length);
        // log.debug("ByteBuf: {}", ByteBufUtil.hexDump(buf));
        // log.debug("magicNum: {}, version: {}, serializerType: {}, messageType: {}, sequenceId: {}, length: {}",
        //         magicNum, version, serializerType, messageType, sequenceId, length);

        // 根据序列化方式反序列化获得消息体
        Serializer serializer = SerializerFactory.getSerializer(serializerType);
        Class<? extends Message> messageClass = MessageTypeFactory.getInstance().getMessageType(messageType);
        Message message = serializer.deserialize(bytes, messageClass);

        out.add(message);
    }

}
