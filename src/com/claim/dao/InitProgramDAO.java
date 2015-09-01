/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.dao;

import com.claim.connection.DBManage;
import com.claim.object.ObjProgramVersion;
import com.claim.support.Console;
import com.claim.constants.ConstantMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Poolsawat.a
 */
public class InitProgramDAO {

    static String SEQ_ID = "SEQ_RPT_PROGRAM_VERSION_ID";
    static String RUN_SEQ_ID = "SEQ_RPT_PROGRAM_VERSION_ID.NEXTVAL";
    Connection connection = null;
    StringBuilder sql = null;
    PreparedStatement psmt = null;
    ResultSet rs = null;

    public void setConnection(Connection conn) {
        this.connection = conn;
    }

    public ObjProgramVersion getLastVersion() throws SQLException {
        ObjProgramVersion objData = null;
        try {
            sql = new StringBuilder();
            sql.append(" SELECT R.VERSION_ID,");
            sql.append(" R.VERSION_NAME,");
            sql.append(" R.VERSION_DESC,");
            sql.append(" R.VERSION_UPDATEDATE,");
            sql.append(" TO_CHAR(R.VERSION_UPDATEDATE,'DD-MM-YYYY') update_dateDMY,");
            sql.append(" R.VERSION_UPDATEBY");
            sql.append(" FROM RPT_PROGRAM_VERSION R");
            sql.append(" WHERE VERSION_ID IN (SELECT MAX(VERSION_ID) FROM RPT_PROGRAM_VERSION)");

            System.out.println("sql ::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            psmt = connection.prepareStatement(sql.toString());
            rs = psmt.executeQuery();
            if (rs.next()) {
                objData = new ObjProgramVersion();
                objData.setVersion_id(rs.getInt("VERSION_ID"));
                objData.setVersion_name(rs.getString("VERSION_NAME"));
                objData.setVersion_desc(rs.getString("VERSION_DESC"));
                objData.setVersion_updateby("VERSION_UPDATEBY");
                objData.setVersion_updatedate(rs.getString("update_dateDMY"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (psmt != null) {
                    psmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return objData;
    }

    public ObjProgramVersion checkVersionProgram(String version) {
        ObjProgramVersion objData = null;
        try {
            sql = new StringBuilder();
            sql.append(" SELECT R.VERSION_ID,");
            sql.append(" R.VERSION_NAME,");
            sql.append(" R.VERSION_DESC,");
            sql.append(" R.VERSION_UPDATEDATE,");
            sql.append(" TO_CHAR(R.VERSION_UPDATEDATE,'DD-MM-YYYY') update_dateDMY,");
            sql.append(" R.VERSION_UPDATEBY");
            sql.append(" FROM RPT_PROGRAM_VERSION R");
            sql.append(" WHERE 1=1  ");
            sql.append(" AND VERSION_ID IN (SELECT MAX(VERSION_ID) FROM RPT_PROGRAM_VERSION )");
            sql.append(" AND  VERSION_NAME = ? ");

            System.out.println("sql ::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            psmt = connection.prepareStatement(sql.toString());
            psmt.setString(1, version);
            rs = psmt.executeQuery();
            if (rs.next()) {
                objData = new ObjProgramVersion();
                objData.setVersion_id(rs.getInt("VERSION_ID"));
                objData.setVersion_name(rs.getString("VERSION_NAME"));
                objData.setVersion_desc(rs.getString("VERSION_DESC"));
                objData.setVersion_updateby("VERSION_UPDATEBY");
                objData.setVersion_updatedate(rs.getString("update_dateDMY"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (psmt != null) {
                    psmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return objData;
    }

    public int create(ObjProgramVersion version) {
        int exec = 0;
        try {
            sql = new StringBuilder();
            sql.append(" ");
            sql.append(" INSERT INTO RPT_PROGRAM_VERSION (VERSION_ID,");
            sql.append(" VERSION_NAME,");
            sql.append(" VERSION_DESC,");
            sql.append(" VERSION_UPDATEDATE,");
            sql.append(" VERSION_UPDATEBY)");
            sql.append(" VALUES (").append(RUN_SEQ_ID).append(",");
            sql.append(" ?,");
            sql.append(" ?,");
            sql.append(" SYSDATE,");
            sql.append(" ?)");

            psmt = connection.prepareStatement(sql.toString());
            psmt.setString(1, version.getVersion_name());
            psmt.setString(2, version.getVersion_desc());
            psmt.setString(3, version.getVersion_updateby()); // poolsawat

            exec = psmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (psmt != null) {
                    psmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return exec;
    }

    
}
