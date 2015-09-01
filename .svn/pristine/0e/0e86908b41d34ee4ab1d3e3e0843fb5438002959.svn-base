package com.claim.connection;

import com.claim.object.ServerProperties;
import com.claim.support.Console;
import com.claim.constants.ConstantMessage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionDB {

    public Connection conn = null;
    public static String URL_ORACLE = "jdbc:oracle:thin:@";
    public String url;

    private String SERVER_IP = null;
    private String SERVER_PORT  = null;
    private String SERVER_DATABASE  = null;
    private String SERVER_TYPE_DB  = null;
    private String SERVER_USERNAME  = null;
    private String SERVER_PASSWORD  = null;


    public ConnectionDB(String CaseDB, String IPserver, String port, String database, String typeDB, String username, String password, String Vcase) {
        if (CaseDB.equals("Test")) {
            this.SERVER_IP = IPserver;
            this.SERVER_PORT = port;
            this.SERVER_DATABASE = database;
            this.SERVER_TYPE_DB = typeDB;
            this.SERVER_USERNAME = username;
            this.SERVER_PASSWORD = password;
            System.err.println("Test");
        } else {
            ResourcesProperties resourcesProperties = new ResourcesProperties();
            ServerProperties properties = resourcesProperties.loadServerProperties();
             this.SERVER_IP = properties.getServer_ip();
            this.SERVER_PORT = properties.getServer_port();
            this.SERVER_DATABASE = properties.getServer_service();
            this.SERVER_TYPE_DB = properties.getServer_type_db();
            this.SERVER_USERNAME = properties.getServer_username();
            this.SERVER_PASSWORD = properties.getServer_password();
        }
    }

    public Connection getConnectionInf() {
        try {
            // System.out.println("public Connection getConnectionInf()" + getURL());
            if (this.SERVER_DATABASE == null) {
                return null;
            }
            try {
                Class.forName(this.SERVER_TYPE_DB);
            } catch (Exception e) {
                e.printStackTrace();
            }
            conn = DriverManager.getConnection(getURL(), this.SERVER_USERNAME, this.SERVER_PASSWORD);
            conn.setAutoCommit(false);

            if (conn == null) {
                Console.LOG(ConstantMessage.MSG_CONNECTION_FAIL, 0);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionDB.class.getName()).log(Level.SEVERE, null, ex);
            Console.LOG(ex.getMessage(), 0);
        }

        return conn;
    }

    public String getURL() {
        url = url = URL_ORACLE + this.SERVER_IP + ":" + this.SERVER_PORT + ":" + this.SERVER_DATABASE;
        //System.out.println("url:>"+url);
        return url;
    }

}
