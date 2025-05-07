package com.sanil.source.code.rpc.common.codec;

import com.sanil.source.code.rpc.common.config.RpcConfig;
import com.sanil.source.code.rpc.common.enums.MessageTypeFactory;
import com.sanil.source.code.rpc.common.enums.SerializerFactory;
import com.sanil.source.code.rpc.common.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

/**
 * 消息编解码器
 *
 * @author zhangpengjun
 * @date 2025/5/7
 */
@ChannelHandler.Sharable
public class MessageCodec extends MessageToMessageCodec<ByteBuf, Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        // 获取序列化方式
        String serializerName = RpcConfig.getSerializer();
        int serializerType = SerializerFactory.getSerializerType(serializerName);

        ByteBuf buf = ctx.alloc().buffer();
        // 4个字节的魔数
        buf.writeBytes(new byte[]{8, 0, 2, 3});
        // 1个字节的版本号
        buf.writeByte(1);
        // 1个字节的序列化方式（jdk：0，json：1）
        buf.writeByte(serializerType);
        // 1个字节的消息指令类型
        buf.writeByte(msg.getMessageType().getType());
        // 8个字节的消息序号
        buf.writeLong(msg.getSequenceId());
        // 1个填充字节，补齐长度
        buf.writeByte(0xff);
        // 4个字节的消息体长度
        Serializer serializer = SerializerFactory.getSerializer(serializerType);
        byte[] bytes = serializer.serialize(msg);
        buf.writeInt(bytes.length);
        // 消息体
        buf.writeBytes(bytes);

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
        // 1个字节的消息指令类型
        byte messageType = buf.readByte();
        // 8个字节的消息序号
        long sequenceId = buf.readLong();
        // 1个填充字节，补齐长度
        buf.readByte();
        // 4个字节的消息体长度
        int length = buf.readInt();
        // 消息体
        byte[] bytes = new byte[length];
        buf.readBytes(bytes, 0, length);

        // 根据序列化方式反序列化获得消息体
        Serializer serializer = SerializerFactory.getSerializer(serializerType);
        Class<? extends Message> messageClass = MessageTypeFactory.getInstance().getMessageType(messageType);
        Message message = serializer.deserialize(bytes, messageClass);

        out.add(message);
    }

}
