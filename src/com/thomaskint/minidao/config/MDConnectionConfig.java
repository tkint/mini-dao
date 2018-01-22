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

    static MDConnectionConfig config = null;

    public static MDConnectionConfig getInstance() throws NamingException {
        if (config == null) {
            config = (MDConnectionConfig) ((new InitialContext()).lookup("JDBC-Config"));
        }
        return config;
    }

    private String url;
    private String port;
    private String login;
    private String password;
    private String database;

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
