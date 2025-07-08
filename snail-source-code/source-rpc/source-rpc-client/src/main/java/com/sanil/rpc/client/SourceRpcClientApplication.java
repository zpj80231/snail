package com.sanil.rpc.client;

import com.sanil.source.code.rpc.spring.EnableRpcClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhangpj
 * @date 2025/7/8
 */
@EnableRpcClient
@SpringBootApplication
public class SourceRpcClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SourceRpcClientApplication.class, args);
    }

}
