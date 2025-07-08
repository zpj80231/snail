package com.sanil.source.code.rpc.core.registry;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.InetSocketAddress;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangpengjun
 * @date 2025/5/7
 */
@Slf4j
public class LocalServerRegistry implements ServerRegistry {

    private static final String BASE_DIR = "/tmp/snail-rpc/";

    @Override
    public void register(String serviceName, InetSocketAddress serverAddress) {
        saveToFile(serviceName, serverAddress);
    }

    @Override
    public InetSocketAddress unregister(String serviceName, InetSocketAddress serverAddress) {
        return removeToFile(serviceName, serverAddress);
    }

    @Override
    public Set<InetSocketAddress> getServerAddress(String serviceName) {
        return getServers().get(serviceName);
    }

    @Override
    public Map<String, Set<InetSocketAddress>> getServers() {
        return loadFromFile();
    }

    private void saveToFile(String serviceName, InetSocketAddress serverAddress) {
        String addrPath = getAddrPath(serviceName, serverAddress);
        FileUtil.touch(addrPath);
    }

    private InetSocketAddress removeToFile(String serviceName, InetSocketAddress serverAddress) {
        String addrPath = getAddrPath(serviceName, serverAddress);
        File addrDir = FileUtil.getParent(FileUtil.file(addrPath), 1);
        boolean success = FileUtil.del(addrPath);
        if (addrDir != null && FileUtil.isDirEmpty(addrDir)) {
            try {
                FileUtil.del(addrDir);
            } catch (Exception e) {
                // nothing
            }
        }
        return success ? serverAddress : null;
    }

    private static Map<String, Set<InetSocketAddress>> loadFromFile() {
        File[] serviceDirs = FileUtil.ls(BASE_DIR);
        if (ArrayUtil.isEmpty(serviceDirs)) {
            return new ConcurrentHashMap<>();
        }
        Map<String, Set<InetSocketAddress>> caches = new java.util.HashMap<>();
        Arrays.stream(serviceDirs).filter(File::isDirectory).forEach(serviceNameFile -> {
            List<String> servers = FileUtil.listFileNames(serviceNameFile.getAbsolutePath());
            Set<InetSocketAddress> inetSocketAddresses = CollUtil.newHashSet();
            servers.forEach(server -> {
                String[] split = server.split(":");
                inetSocketAddresses.add(new InetSocketAddress(split[0], Integer.parseInt(split[1])));
            });
            caches.put(serviceNameFile.getName(), inetSocketAddresses);
        });
        return caches;
    }

    private String getAddrPath(String serviceName, InetSocketAddress serverAddress) {
        String servicePath = getServicePath(serviceName);
        String serverAddressStr  = serverAddress.getHostString() + ":" + serverAddress.getPort();
        return Paths.get(servicePath, serverAddressStr).toString();
    }

    private String getServicePath(String serviceName) {
        return Paths.get(BASE_DIR, serviceName).toString();
    }

}
