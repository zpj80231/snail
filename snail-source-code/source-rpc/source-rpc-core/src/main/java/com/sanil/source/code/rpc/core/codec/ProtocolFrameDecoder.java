package com.sanil.source.code.rpc.core.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 协议帧解码器，配合自定义协议解析使用
 *
 * @author zhangpengjun
 * @date 2025/5/7
 */
public class ProtocolFrameDecoder extends LengthFieldBasedFrameDecoder {

    /**
     * 默认构造函数，使用默认的参数初始化ProtocolFrameDecoder
     * <p>
     * <table style="border:1px solid black; border-collapse:collapse;">
     *   <tr style="border:1px solid black;"><th style="border:1px solid black; padding: 10px;">参数名</th><th style="border:1px solid black; padding: 10px;">值</th><th style="border:1px solid black; padding: 10px;">含义</th></tr>
     *   <tr style="border:1px solid black;"><td style="border:1px solid black; padding: 6px;">maxFrameLength</td><td style="border:1px solid black; padding: 6px;">1024</td><td style="border:1px solid black; padding: 6px;">协议帧的最大长度，防止解码时占用过多内存。</td></tr>
     *   <tr style="border:1px solid black;"><td style="border:1px solid black; padding: 6px;">lengthFieldOffset</td><td style="border:1px solid black; padding: 6px;">24</td><td style="border:1px solid black; padding: 6px;">长度字段在协议帧中的起始偏移量（单位是字节）。</td></tr>
     *   <tr style="border:1px solid black;"><td style="border:1px solid black; padding: 6px;">lengthFieldLength</td><td style="border:1px solid black; padding: 6px;">4</td><td style="border:1px solid black; padding: 6px;">长度字段的字节数。这里为 4 字节，表示使用 32 位整数存储帧长度。</td></tr>
     *   <tr style="border:1px solid black;"><td style="border:1px solid black; padding: 6px;">lengthAdjustment</td><td style="border:1px solid black; padding: 6px;">0</td><td style="border:1px solid black; padding: 6px;">在计算实际帧长度时对读取到的长度值进行调整。</td></tr>
     *   <tr style="border:1px solid black;"><td style="border:1px solid black; padding: 6px;">initialBytesToStrip</td><td style="border:1px solid black; padding: 6px;">0</td><td style="border:1px solid black; padding: 6px;">解码后从缓冲区中移除的初始字节数。设置为 0 表示不解码后不移除任何字节。</td></tr>
     * </table>
     */
    public ProtocolFrameDecoder() {
        this(2 * 1024 * 1024, 20, 4, 0, 0);
    }

    /**
     * 构造函数，允许用户指定帧解码的参数
     *
     * @param maxFrameLength          期望解码的最大帧长度
     * @param lengthFieldOffset       长度字段的偏移量，即长度字段在帧中的位置
     * @param lengthFieldLength       长度字段的长度，以字节为单位
     * @param lengthAdjustment        长度调整值，用于计算帧长度
     * @param initialBytesToStrip     解码后初始需要移除的字节数
     */
    public ProtocolFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

}
