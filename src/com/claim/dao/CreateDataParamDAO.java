/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.dao;

import com.claim.object.ObjCreateDataParam;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Poolsawat.a
 */
public class CreateDataParamDAO {

    Connection connection = null;
    PreparedStatement pstm = null;
    ResultSet rs = null;
    StringBuilder sql = null;

    static final String SEQ_ID = "SEQ_CREATEDATA_PARAM_ID";
    static final String RUN_SEQ_ID = "SEQ_CREATEDATA_PARAM_ID.NEXTVAL";

    public void setConnection(Connection conn) {
        this.connection = conn;
    }

    public int createDataParam(ObjCreateDataParam objParam) {
        int exe = 0;
        try {
            sql = new StringBuilder();
            sql.append("  INSERT INTO RPT_OPBKK_CREATEDATA_PARAM (");
            sql.append("  PARAM_ID, PARAM_TABLE, PARAM_DESC, ");
            sql.append("  PARAM_DATEOPD_BEGIN, PARAM_DATEOPD_END, PARAM_DATESEND_BEGIN, ");
            sql.append("  PARAM_DATESEND_END, PARAM_STMPLIST, PARAM_STMP, ");
            sql.append("  PARAM_ROUND, PARAM_NO, PARAM_PAYRATE, ");
            sql.append("  PARAM_INDV_RATE, PARAM_CREATEDATE, PARAM_BY_IP) ");
            sql.append("  VALUES (");
            sql.append("  ").append(RUN_SEQ_ID).append(",?,?,");
            sql.append("  ?,?,?,");
            sql.append("  ?,?,?,");
            sql.append("  ?,?,?,");
            sql.append("  ?,SYSDATE,?");
            sql.append("  )");
            System.out.println("sql ::==" + sql.toString());
            pstm = connection.prepareStatement(sql.toString());
            pstm.setString(1, objParam.getParam_table());
            pstm.setString(2, objParam.getParam_desc());
            pstm.setString(3, objParam.getParam_dateopd_begin());
            pstm.setString(4, objParam.getParam_dateopd_end());
            pstm.setString(5, objParam.getParam_datesend_begin());
            pstm.setString(6, objParam.getParam_datesend_end());
            pstm.setString(7, objParam.getParam_stmplist());
            pstm.setString(8, objParam.getParam_stmp());
            pstm.setString(9, objParam.getParam_round());
            pstm.setString(10, objParam.getParam_no());
            pstm.setString(11, objParam.getParam_payrate());
            pstm.setString(12, objParam.getParam_indv_rate());
            pstm.setString(13, objParam.getParam_by_ip());
            exe = pstm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstm != null) {
                    pstm.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return exe;
    }

    public List<ObjCreateDataParam> getListDataParam() {
        List<ObjCreateDataParam> listData = null;
        ObjCreateDataParam objData = null;
        try {
            sql = new StringBuilder();
            sql.append(" SELECT R.PARAM_ID, R.PARAM_TABLE, R.PARAM_DESC, ");
            sql.append(" R.PARAM_DATEOPD_BEGIN, R.PARAM_DATEOPD_END, R.PARAM_DATESEND_BEGIN, ");
            sql.append(" R.PARAM_DATESEND_END, R.PARAM_STMPLIST, R.PARAM_STMP, ");
            sql.append(" R.PARAM_ROUND, R.PARAM_NO, R.PARAM_PAYRATE, ");
            sql.append(" R.PARAM_INDV_RATE, R.PARAM_CREATEDATE, R.PARAM_BY_IP");
            sql.append(" FROM RPT_OPBKK_CREATEDATA_PARAM R");
            System.out.println("sql ::==" + sql.toString());
            pstm = connection.prepareStatement(sql.toString());
            rs = pstm.executeQuery();
            listData = new ArrayList<>();
            while (rs.next()) {
                objData = new ObjCreateDataParam();                
                objData.setParam_id(rs.getInt("PARAM_ID"));
                objData.setParam_table(rs.getString("PARAM_TABLE"));
                objData.setParam_desc(rs.getString("PARAM_DESC"));
                objData.setParam_dateopd_begin(rs.getString("PARAM_DATEOPD_BEGIN"));
                objData.setParam_dateopd_end(rs.getString("PARAM_DATEOPD_END"));
                objData.setParam_datesend_begin(rs.getString("PARAM_DATESEND_BEGIN"));
                objData.setParam_datesend_end(rs.getString("PARAM_DATESEND_END"));
                objData.setParam_stmplist(rs.getString("PARAM_STMPLIST"));
                objData.setParam_stmp(rs.getString("PARAM_STMP"));
                objData.setParam_round(rs.getString("PARAM_ROUND"));
                objData.setParam_no(rs.getString("PARAM_NO"));
                objData.setParam_payrate(rs.getString("PARAM_PAYRATE"));
                objData.setParam_indv_rate(rs.getString("PARAM_INDV_RATE"));
                objData.setParam_createdate(rs.getString("PARAM_CREATEDATE"));
                objData.setParam_by_ip(rs.getString("PARAM_BY_IP"));
                listData.add(objData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstm != null) {
                    pstm.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listData;
    }
   
}
