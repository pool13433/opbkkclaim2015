/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.dao;

import com.claim.constants.ConstantMessage;
import com.claim.object.ObjFileTransfer;
import com.claim.support.Console;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author POOL_LAPTOP
 */
public class FileTransferDao {

    Connection connection = null;
    StringBuilder sql = null;
    PreparedStatement pstm = null;
    ResultSet rs = null;

    static String SEQ_ID = "SEQ_FILE_TRANSFER_ID";
    static String RUN_SEQ_ID = "SEQ_FILE_TRANSFER_ID.nextval";
    static String SQL_SELECT = "";

    public void setConnection(Connection conn) {
        this.connection = conn;
    }

    public int createLog(ObjFileTransfer ftpObj) {
        int exec = 0;
        try {
            sql = new StringBuilder();
            sql.append(" ");
            sql.append(" INSERT INTO RPT_PROGRAM_FILE_TRANSFER (");
            sql.append(" FTP_ID, FTP_NAME, FTP_DESC, ");
            sql.append(" FTP_STMP, FTP_CREATEDATE, FTP_REPORT_TYPE, ");
            sql.append(" FTP_STATUS,FTP_CLIENT_IP) ");
            sql.append(" VALUES (").append(RUN_SEQ_ID).append(",?,?,?,SYSDATE,?,?,?)");

            System.out.println("sql ::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstm = connection.prepareStatement(sql.toString());
            pstm.setString(1, ftpObj.getFtp_name());
            pstm.setString(2, ftpObj.getFtp_desc());
            pstm.setString(3, ftpObj.getFtp_stmp());
            pstm.setString(4, ftpObj.getFtp_report_type()); 
            pstm.setInt(5, ftpObj.getFtp_status()); 
            pstm.setString(6, ftpObj.getFtp_client_ip());
            
            exec = pstm.executeUpdate();

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
        return exec;
    }

    public List<ObjFileTransfer> readFileList(String reportType) {
        List<ObjFileTransfer> listData = null;
        ObjFileTransfer objData = null;
        try {
            sql = new StringBuilder();
            sql.append(" SELECT R.FTP_ID, R.FTP_NAME, R.FTP_DESC, ");
            sql.append(" R.FTP_STMP, R.FTP_CREATEDATE, R.FTP_REPORT_TYPE, ");
            sql.append(" R.FTP_STATUS");
            sql.append(" FROM RPT_PROGRAM_FILE_TRANSFER R");
            sql.append(" WHERE FTP_REPORT_TYPE = ?");
            sql.append(" ORDER BY FTP_STMP,FTP_CREATEDATE");

            System.out.println("sql ::==" + sql.toString());

            pstm = connection.prepareStatement(sql.toString());
            pstm.setString(1, reportType);

            rs = pstm.executeQuery();

            listData = new ArrayList<>();
            while (rs.next()) {
                objData = new ObjFileTransfer();
                objData.setFtp_createdate(rs.getString("FTP_CREATEDATE"));
                objData.setFtp_id(rs.getString("FTP_ID"));
                objData.setFtp_name(rs.getString("FTP_NAME"));
                objData.setFtp_desc(rs.getString("FTP_DESC"));
                objData.setFtp_stmp(rs.getString("FTP_STMP"));
                objData.setFtp_report_type(rs.getString("FTP_REPORT_TYPE"));
                objData.setFtp_status(rs.getInt("FTP_STATUS"));
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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return listData;
    }
}
