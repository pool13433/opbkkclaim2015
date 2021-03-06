/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.controller;

import com.claim.connection.DBManage;
import com.claim.dao.InitProgramDAO;
import com.claim.object.ObjProgramVersion;
import com.claim.support.Console;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Poolsawat.a
 */
public class InitProgramController {

    Connection connection = null;
    private Timer timer = new Timer();
    private static final int MUNUTE_TIME = 10;

    public boolean getVersionProgram(String version) {
        boolean is_ver = false;
        try {

            connection = new DBManage().open();

            InitProgramDAO initProgramDAO = new InitProgramDAO();
            initProgramDAO.setConnection(connection);

            ObjProgramVersion objVersion = initProgramDAO.checkVersionProgram(version);
            if (objVersion != null) { // แสดงว่า เวอชั่นตรงกัน
                is_ver = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(InitProgramController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return is_ver;
    }

    public ObjProgramVersion getLatVersionProgram(String version) {
        ObjProgramVersion objVesion = null;
        try {

            connection = new DBManage().open();

            InitProgramDAO initProgramDAO = new InitProgramDAO();
            initProgramDAO.setConnection(connection);

            objVesion = initProgramDAO.checkVersionProgram(version);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(InitProgramController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return objVesion;
    }

    public int updateVersionProgram(ObjProgramVersion obj) {
        int exec = 0;
        try {
            connection = new DBManage().open();
            InitProgramDAO initProgramDAO = new InitProgramDAO();
            initProgramDAO.setConnection(connection);

            exec = initProgramDAO.create(obj);

            if (exec == 1) {
                Console.LOG("อัพเดท เวอชั่น เป็น [" + obj.getVersion_name() + "] ของโปรแกรมเรียบร้อยแล้ว", 1);
            } else {
                Console.LOG("ไม่สามารถ อัพเดทโปรแกรมได้", 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
        } finally {
            if (connection != null) {
                try {
                    connection.commit();
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(InitProgramController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return exec;
    }

    public void checkDatabaseConnection() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {                
                Connection isConn = null;
                try {
                    //Console.LOG("############# ตรวจสอบการติดต่อฐานข้อมูล ทุก "+MUNUTE_TIME+" นาที#############", 1);
                    System.out.println("############# ตรวจสอบการติดต่อฐานข้อมูล ทุก "+MUNUTE_TIME+" นาที#############");
                    isConn = new DBManage().open();
                    if(isConn == null){
                        Console.LOG("###### ขาดการติดต่อจากฐานข้อมูล กรุณาติดต่อเจ้าหน้าที่ ######", 0);
                    }else{
                        System.out.println("####### โปรแกรมทำงานปกติ#########");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(InitProgramController.class.getName()).log(Level.SEVERE, null, ex);
                }finally{
                    if(isConn!=null){
                        try {
                            isConn.close();
                        } catch (SQLException ex) {
                            Logger.getLogger(InitProgramController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        };
        int initialDelay = 100; // start after 5 minute
        int period = MUNUTE_TIME * 60 * 1000; // repeat every 30 minute 30*60*1000

        timer.scheduleAtFixedRate(task, initialDelay, period);
    }

}
