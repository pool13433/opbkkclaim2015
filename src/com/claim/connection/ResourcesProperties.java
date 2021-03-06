/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.connection;

import com.claim.controller.FileTransferController;
import com.claim.object.FtpProperties;
import com.claim.object.ServerProperties;
import com.claim.support.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author POOL_LAPTOP
 */
public class ResourcesProperties {
    
    static Properties props;
    static final String PROPERTIES_SERVER = "properties" + File.separator + "datasource.properties";
    static final String PROPERTIES_FTP = "properties" + File.separator + "ftp.properties";
    
    public ServerProperties loadServerProperties() {
        props = new Properties();
        ServerProperties properties = new ServerProperties();
        try {
            props.load(new FileInputStream(PROPERTIES_SERVER));
            properties.setServer_ip(props.getProperty("server_ip").trim());
            properties.setServer_service(props.getProperty("server_service_name").trim());
            properties.setServer_type_db(props.getProperty("server_driver_db").trim());
            properties.setServer_port(props.getProperty("server_port").trim());
            properties.setServer_username(props.getProperty("server_username").trim());
            properties.setServer_password(props.getProperty("server_password").trim());            
        } catch (IOException ex) {
            Console.LOG("file not found :: properties/datasource.properties ", 0);
            Logger.getLogger(FileTransferController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return properties;
    }
    
    public FtpProperties loadFTPProperties() {
        props = new Properties();
        FtpProperties properties = new FtpProperties();
        try {
            props.load(new FileInputStream(PROPERTIES_FTP));
            properties.setFtp_server(props.getProperty("serverAddress").trim());
            properties.setFtp_port(Integer.parseInt(props.getProperty("port").trim()));
            properties.setFtp_username(props.getProperty("userId").trim());
            properties.setFtp_password(props.getProperty("password").trim());
            properties.setFtp_remote_directory(props.getProperty("remoteDirectory").trim());
            properties.setFtp_protocal(props.getProperty("protocol").trim());
            properties.setFtp_impicit(Boolean.parseBoolean(props.getProperty("impicit").trim()));
            
        } catch (IOException ex) {
            Console.LOG("file not found :: properties/ftp.properties ", 0);
            Logger.getLogger(FileTransferController.class.getName()).log(Level.SEVERE, null, ex);
        }catch (NumberFormatException ex) {
            Console.LOG("NumberFormatException :: Can not convert port to numbers ", 0);
            Logger.getLogger(FileTransferController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return properties;
    }
}
