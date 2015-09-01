/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.claim.controller;

import com.claim.connection.DBManage;
import com.claim.dao.CreateDataParamDAO;
import com.claim.object.ObjCreateDataParam;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Poolsawat.a
 */
public class CreateDataParamController {
    Connection connection = null;
    
    public int createDataParam(ObjCreateDataParam objParam){
        int exe = 0;
        try {
            connection = new DBManage().open();
            CreateDataParamDAO paramDAO = new CreateDataParamDAO();
            paramDAO.setConnection(connection);
            
            exe = paramDAO.createDataParam(objParam);
        } catch (Exception e) {
            e.printStackTrace();
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
    
    public List<ObjCreateDataParam> getListCreateData(){
        List<ObjCreateDataParam> listData = new ArrayList<>();
        try {
            connection = new DBManage().open();
            CreateDataParamDAO paramDAO = new CreateDataParamDAO();
            paramDAO.setConnection(connection);
            
            listData = paramDAO.getListDataParam();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
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
}
