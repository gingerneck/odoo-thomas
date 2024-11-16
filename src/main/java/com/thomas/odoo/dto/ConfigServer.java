package com.thomas.odoo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ConfigServer {
    private String host;
    private String database;
    private String user;
    private Integer uid;
    private String password;


    public ConfigServer(Map<String, String> data) {
        this.host = data.get("host");
        this.database = data.get("database");
        this.user = data.get("user");
        this.password = data.get("password");
    }

}
