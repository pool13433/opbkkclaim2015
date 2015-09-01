/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.connection;

import com.claim.object.ServerProperties;
import com.claim.support.Console;
import com.claim.constants.ConstantMessage;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Poolsawat.a
 */
public class DBManage {

    static final String URL_ORACLE = "jdbc:oracle:thin:@";

    private String SERVER_IP = null;
    private String SERVER_PORT = null;
    private String SERVER_DATABASE = null;
    private String SERVER_TYPE_DB = null;
    private String SERVER_USERNAME = null;
    private String SERVER_PASSWORD = null;

    static Properties props;

    static final String RESOURCES_PROPERTIES = "properties" + File.separator + "datasource.properties";

    public DBManage() {
        ResourcesProperties resourcesProperties = new ResourcesProperties();
        ServerProperties properties = resourcesProperties.loadServerProperties();
        this.SERVER_IP = properties.getServer_ip();
        this.SERVER_PORT = properties.getServer_port();
        this.SERVER_DATABASE = properties.getServer_service();
        this.SERVER_TYPE_DB = properties.getServer_type_db();
        this.SERVER_USERNAME = properties.getServer_username();
        this.SERVER_PASSWORD = properties.getServer_password();
    }

    public Connection open() throws SQLException {
        Connection conn = null;
        try {
            Class.forName(SERVER_TYPE_DB);
            conn = DriverManager.getConnection(URL_ORACLE + SERVER_IP + ":" + SERVER_PORT + ":" + SERVER_DATABASE, SERVER_USERNAME, SERVER_PASSWORD);
            if (conn != null) {
                System.out.println("INFO :: #### CONNECTION SUCCESS ####");
            } else {
                System.out.println("INFO :: #### CONNECTION FAIL !!! ####");
                Console.LOG(ConstantMessage.MSG_CONNECTION_FAIL, 0);
            }
            conn.setAutoCommit(false);
        } catch (ClassNotFoundException | SQLException ex) {
            Console.LOG(ConstantMessage.MSG_CONNECTION_FAIL, 0);
            Console.LOG(ex.getMessage(), 0);
            Logger.getLogger(DBManage.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return conn;
        }
    }

}
