package com.sanil.source.rpc.core.compress;

import com.sanil.source.rpc.core.extension.SPI;

/**
 * @author zhangpj
 * @date 2025/5/29
 */
@SPI
public interface Compress {

    byte[] compress(byte[] bytes);

    byte[] decompress(byte[] bytes);

}
