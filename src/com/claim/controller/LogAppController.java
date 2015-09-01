/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.controller;

import com.claim.connection.DBManage;
import com.claim.dao.LogApplicationDAO;
import com.claim.object.ObjLogApplication;
import com.claim.support.DateUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Poolsawat.a
 */
public class LogAppController {

    Connection connection = null;

    public int createLog(String name, String desc, String ip, int status) {
        int exe = 0;
        try {
            connection = new DBManage().open();
            LogApplicationDAO logDao = new LogApplicationDAO();
            logDao.setConnection(connection);

            ObjLogApplication objLog = new ObjLogApplication();
            objLog.setLog_name(name);
            objLog.setLog_desc(desc);
            objLog.setLog_ip(ip);
            objLog.setLog_status(status);
            exe = logDao.createLog(objLog);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.commit();
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return exe;
    }

    public List<ObjLogApplication> getListLogLimit(int limit) {
        List<ObjLogApplication> listData = new ArrayList<>();
        try {
            connection = new DBManage().open();
            
            LogApplicationDAO logDao = new LogApplicationDAO();
            logDao.setConnection(connection);
            listData = logDao.getListLogLimit(limit);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listData;
    }
    
    public List<ObjLogApplication> getListHistoryVersion(){
        List<ObjLogApplication> listData = new ArrayList<>();
        try {
            connection = new DBManage().open();
            
            LogApplicationDAO logDao = new LogApplicationDAO();
            logDao.setConnection(connection);
            listData = logDao.getListHistoryVersion();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listData;
    }
    
    public List<ObjLogApplication> getListLogReport(String rptname){
        List<ObjLogApplication> listData = new ArrayList<>();
        try {
            connection = new DBManage().open();
            
            LogApplicationDAO logDao = new LogApplicationDAO();
            logDao.setConnection(connection);
            listData = logDao.getListLogReport(rptname);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listData;
    }
    public List<String> getRptNameInLogReport(){
        List<String> listData = new ArrayList<>();
        try {
            connection = new DBManage().open();
            
            LogApplicationDAO logDao = new LogApplicationDAO();
            logDao.setConnection(connection);
            listData = logDao.getRptNameInLogReport();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listData;
    }

    public static void main(String[] args) {
       
    }
}
