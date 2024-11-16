package com.thomas.odoo.service;


import com.thomas.odoo.dto.ConfigServer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

@Service
@RequiredArgsConstructor
public class DemoConnect {
    private final XmlRpcClient client;

    public ConfigServer connect() throws XmlRpcException, IOException {
        final XmlRpcClientConfigImpl start_config = new XmlRpcClientConfigImpl();

        start_config.setServerURL(new URL("https://demo.odoo.com/start"));

        ConfigServer config = new ConfigServer(
                (Map<String, String>) client.execute(start_config, "start", emptyList())
        );

        System.out.println(config);
        return config;
    }

    @SneakyThrows
    public void checkConnection(String host){
        final XmlRpcClientConfigImpl common_config = new XmlRpcClientConfigImpl();
        common_config.setServerURL(new URL(String.format("%s/xmlrpc/2/common", host)));
        var data = (Map<String, Object>) client.execute(common_config, "version", emptyList());
        System.out.println(data);
    }

    @SneakyThrows
    public int authenticate(ConfigServer config){
        final XmlRpcClientConfigImpl common_config = new XmlRpcClientConfigImpl();
        common_config.setServerURL(new URL(String.format("%s/xmlrpc/2/common", config.getHost())));
        int uid = (int)client.execute(common_config, "authenticate",
                asList(config.getDatabase(), config.getUser(), config.getPassword(), emptyMap()));


        return uid;
    }

}
