package com.sanil.source.code.rpc.client.domain;

import com.sanil.source.code.rpc.common.message.RequestMessage;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;


/**
 * @author zhangpengjun
 * @date 2025/5/8
 */
@Slf4j
public class RpcClientChannel {

    private final Channel channel;

    public RpcClientChannel(Channel channel) {
        this.channel = channel;
    }

    public void sendMessage(RequestMessage message) {
        if (channel == null || !channel.isOpen() || !channel.isActive()) {
            return;
        }
        channel.writeAndFlush(message);
    }

}
