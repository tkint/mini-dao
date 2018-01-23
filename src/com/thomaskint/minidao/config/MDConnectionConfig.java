/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thomaskint.minidao.config;

import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author tkint
 */
public class MDConnectionConfig {

    private String url;
    private String port;
    private String login;
    private String password;
    private String database;

    public MDConnectionConfig(String url, String port, String login, String password, String database) {
        this.url = url;
        this.port = port;
        this.login = login;
        this.password = password;
        this.database = database;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }
}
