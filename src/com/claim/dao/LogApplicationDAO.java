/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.dao;

import com.claim.object.ObjLogApplication;
import com.claim.support.Console;
import com.claim.constants.ConstantMessage;
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
public class LogApplicationDAO {

    static String SEQ_ID = "SEQ_LOG_APPLICATION_ID";
    static String RUN_SEQ_ID = "SEQ_LOG_APPLICATION_ID.nextval";
    static String SQL_SELECT = "";

    Connection connection = null;
    PreparedStatement pstm = null;
    ResultSet rs = null;
    StringBuilder sql = null;

    public void setConnection(Connection conn) {
        this.connection = conn;
    }

    public int createLog(ObjLogApplication objLog) {
        int exec = 0;
        try {
            sql = new StringBuilder();
            sql.append(" INSERT INTO RPT_OPBKK_LOG_APPLICATION (");
            sql.append(" LOG_ID, LOG_NAME, LOG_DESC, ");
            sql.append(" LOG_IP, LOG_CREATEDATE, LOG_STATUS) ");
            sql.append(" VALUES (").append(RUN_SEQ_ID).append(",?,?,?,SYSDATE,?)");

            System.out.println("sql ::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstm = connection.prepareStatement(sql.toString());
            pstm.setString(1, objLog.getLog_name());
            pstm.setString(2, objLog.getLog_desc());
            pstm.setString(3, objLog.getLog_ip());
            pstm.setInt(4, objLog.getLog_status()); // 1= ok ,2 = fail

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

    public List<ObjLogApplication> getListLogLimit(int limit) {
        List<ObjLogApplication> listData = null;
        ObjLogApplication objData = null;
        try {
            sql = new StringBuilder();
            sql.append(" SELECT * FROM (SELECT r.log_id,");
            sql.append(" r.log_name,r.log_desc,");
            sql.append(" r.log_ip,TO_CHAR(r.log_createdate,'dd-mm-yyyy hh:mm:ss') log_createdate,");
            sql.append(" r.log_status");
            sql.append(" FROM rpt_opbkk_log_application r");
            sql.append(" ORDER BY r.log_id DESC");
            sql.append(" ) WHERE ROWNUM < ").append(limit);
            
            System.out.println("sql::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstm = connection.prepareStatement(sql.toString());
            rs = pstm.executeQuery();
            listData = new ArrayList<>();
            while (rs.next()) {
                objData = new ObjLogApplication();
                objData.setLog_id(rs.getInt("LOG_ID"));
                objData.setLog_name(rs.getString("LOG_NAME"));
                objData.setLog_desc(rs.getString("LOG_DESC"));
                objData.setLog_createdate(rs.getString("LOG_CREATEDATE"));
                objData.setLog_ip(rs.getString("LOG_IP"));
                objData.setLog_status(rs.getInt("LOG_STATUS"));
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

    public List<ObjLogApplication> getListHistoryVersion() {
        List<ObjLogApplication> listData = null;
        ObjLogApplication objData = null;
        try {
            sql = new StringBuilder();
            sql.append(" SELECT  R.VERSION_ID, R.VERSION_NAME, R.VERSION_DESC, ");
            sql.append(" TO_CHAR(R.VERSION_UPDATEDATE,'dd-mm-yyyy hh:mm:ss') as VERSION_UPDATEDATE, R.VERSION_UPDATEBY");
            sql.append(" FROM RPT_PROGRAM_VERSION R ORDER BY VERSION_ID DESC");

            System.out.println("sql::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstm = connection.prepareStatement(sql.toString());
            rs = pstm.executeQuery();
            listData = new ArrayList<>();
            while (rs.next()) {
                objData = new ObjLogApplication();
                objData.setLog_id(rs.getInt("VERSION_ID"));
                objData.setLog_name(rs.getString("VERSION_NAME"));
                objData.setLog_desc(rs.getString("VERSION_DESC"));
                objData.setLog_createdate(rs.getString("VERSION_UPDATEDATE"));
                objData.setLog_status_desc(rs.getString("VERSION_UPDATEBY"));
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

    public List<String> getRptNameInLogReport() {
        List<String> arrayRptname = new ArrayList<String>();
        try {
            sql = new StringBuilder();
            sql.append(" SELECT DISTINCT(RPT_NAME) as RPT_NAME");
            sql.append(" FROM OPBKKCLAIM.LOG_REPORT L");
            sql.append(" ORDER BY RPT_NAME DESC");

            System.out.println("sql::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstm = connection.prepareStatement(sql.toString());
            rs = pstm.executeQuery();
            while (rs.next()) {
                arrayRptname.add(rs.getString("RPT_NAME"));
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
        return arrayRptname;
    }

    public List<ObjLogApplication> getListLogReport(String rptname) {
        List<ObjLogApplication> listData = null;
        ObjLogApplication objData = null;
        try {
            sql = new StringBuilder();
            sql.append(" SELECT ");
            sql.append(" L.LOGDATE, L.RPT_NAME, L.STMP, ");
            sql.append(" L.N_RECORD, L.RESULT, L.DESCRIPTION,");
            sql.append(" TO_CHAR(L.LOGDATE,'dd-mm-yyyy hh:mm:ss') as logdate_tochar");
            sql.append(" FROM LOG_REPORT L");
            if (!rptname.equals("")) {
                sql.append(" WHERE RPT_NAME = '").append(rptname).append("' ");
            }
            sql.append(" ORDER BY LOGDATE DESC");

            System.out.println("sql::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstm = connection.prepareStatement(sql.toString());
            rs = pstm.executeQuery();
            listData = new ArrayList<>();
            while (rs.next()) {
                objData = new ObjLogApplication();
                objData.setLog_name(rs.getString("RPT_NAME"));
                objData.setLog_desc(rs.getString("DESCRIPTION"));
                objData.setLog_createdate(rs.getString("logdate_tochar"));
                objData.setResult(rs.getString("RESULT"));
                objData.setRecord(rs.getInt("N_RECORD"));
                objData.setStmp(rs.getString("STMP"));
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
