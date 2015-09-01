/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.object;

/**
 *
 * @author POOL_LAPTOP
 */
public class FtpProperties {

    private String ftp_server;
    private int ftp_port;
    private String ftp_username;
    private String ftp_password;
    private String ftp_remote_directory;
    private String ftp_protocal;
    private boolean ftp_impicit; // isImpicit = true;
    
    public String getFtp_server() {
        return ftp_server;
    }

    public void setFtp_server(String ftp_server) {
        this.ftp_server = ftp_server;
    }

    public int getFtp_port() {
        return ftp_port;
    }

    public void setFtp_port(int ftp_port) {
        this.ftp_port = ftp_port;
    }


    public String getFtp_username() {
        return ftp_username;
    }

    public void setFtp_username(String ftp_username) {
        this.ftp_username = ftp_username;
    }

    public String getFtp_password() {
        return ftp_password;
    }

    public void setFtp_password(String ftp_password) {
        this.ftp_password = ftp_password;
    }

    public String getFtp_remote_directory() {
        return ftp_remote_directory;
    }

    public void setFtp_remote_directory(String ftp_remote_directory) {
        this.ftp_remote_directory = ftp_remote_directory;
    }

    public String getFtp_protocal() {
        return ftp_protocal;
    }

    public void setFtp_protocal(String server_protocal) {
        this.ftp_protocal = server_protocal;
    }

    public boolean isFtp_impicit() {
        return ftp_impicit;
    }

    public void setFtp_impicit(boolean ftp_impicit) {
        this.ftp_impicit = ftp_impicit;
    }

    @Override
    public String toString() {
        return "FtpProperties{" + "ftp_server=" + ftp_server + ", ftp_port=" + ftp_port + ", ftp_username=" + ftp_username + ", ftp_password=" + ftp_password + ", ftp_remote_directory=" + ftp_remote_directory + ", ftp_protocal=" + ftp_protocal + ", ftp_impicit=" + ftp_impicit + '}';
    }

    

    
    
    
}
