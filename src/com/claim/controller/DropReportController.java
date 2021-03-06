/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.controller;

import com.claim.connection.DBManage;
import com.claim.dao.DropReportDAO;
import com.claim.object.ObjPaymentResult;
import com.claim.support.Console;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Poolsawat.a
 */
public class DropReportController {

    Connection connection = null;

    public List<ObjPaymentResult> getSearchResult(int indexTable) {
        List<ObjPaymentResult> listData = null;
        try {
            connection = new DBManage().open();            
            DropReportDAO dropDao = new DropReportDAO();
            dropDao.setConnection(connection);
            
            listData = dropDao.getListRptReportByTable(indexTable);
        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
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
    
    public int deleteRptReport(int indexTable,String stmp){
        int exe = 0;
        try {
            connection  = new DBManage().open();
            DropReportDAO dropDao = new DropReportDAO();
            dropDao.setConnection(connection);
            exe = dropDao.deleteRptReport(indexTable, stmp);
            Console.LOG("ลบล้างข้อมูลเรียบร้อยแล้ว", 1);
        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
        }finally {
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
}
