package com.thomas.odoo.config;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OdooConfiguration {

    @Bean
    public XmlRpcClient getXmlRpcClient() {
        return new XmlRpcClient();
    }
}
