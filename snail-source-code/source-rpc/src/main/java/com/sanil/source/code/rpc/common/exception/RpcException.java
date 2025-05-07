package com.sanil.source.code.rpc.common.exception;

/**
 * @author zhangpengjun
 * @date 2025/5/7
 */
public class RpcException extends RuntimeException {

    private static final long serialVersionUID = -6057268930492847740L;

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(Throwable cause) {
        super(cause);
    }

    public RpcException(String message) {
        super(message);
    }

}
