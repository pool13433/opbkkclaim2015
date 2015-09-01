/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.dao;

import com.claim.connection.DBManage;
import com.claim.object.HospitalService;
import com.claim.object.Stmp;
import com.claim.support.FunctionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Poolsawat
 */
public class UtilDao {

    public Connection conn = null;
    public ResultSet rs = null;
    public StringBuilder sql = null;
    public PreparedStatement pstm = null;

    public String getThaiBath(double numberBath) {
        String thaiBath = "";
        try {
            conn = new DBManage().open();
            sql = new StringBuilder();
            sql.append(" select BahtText(?) AS thaibath from DUAL");  //1231567890.12
            System.out.println("sql bathtext ::==" + sql.toString());
            System.out.println("numberBath ::==" + String.valueOf(numberBath));
            pstm = conn.prepareStatement(sql.toString());
            pstm.setDouble(1, numberBath);
            rs = pstm.executeQuery();
            if (rs.next()) {
                thaiBath = rs.getString("thaibath");
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
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return thaiBath;
    }

    public String getCurrentYearFromServer() {
        StringBuilder sql = null;
        ResultSet rs = null;
        PreparedStatement pstm = null;
        Connection conn = null;
        try {
            conn = new DBManage().open();
            sql = new StringBuilder();
            sql.append(" SELECT to_char(sysdate,'yyyy') current_year from dual ");
            pstm = conn.prepareStatement(sql.toString());
            rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getString("current_year");
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
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }
    
    public String getCurrentDayMonthYearFromServer(String format) {
        StringBuilder sql = null;
        ResultSet rs = null;
        PreparedStatement pstm = null;
        Connection conn = null;
        try {
            conn = new DBManage().open();
            sql = new StringBuilder();
            sql.append(" SELECT to_char(sysdate,'").append(format).append("') current_date from dual ");
            pstm = conn.prepareStatement(sql.toString());
            rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getString("current_date");
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
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public int getDifferenceBetween2date(String dateMax, String dateMin) {
        StringBuilder sql = null;
        ResultSet rs = null;
        PreparedStatement pstm = null;
        Connection conn = null;
        try {
            conn = new DBManage().open();
            sql = new StringBuilder();
            sql.append(" SELECT (TO_DATE (?, 'yyyymmdd')) - (TO_DATE (?, 'yyyymmdd')) datediff  FROM DUAL");
            pstm = conn.prepareStatement(sql.toString());
            pstm.setString(1, dateMax); // 201501  
            pstm.setString(2, dateMin); // 201501  
            rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getInt("datediff");
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
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public List<Stmp> getStmpDistinct(String tbName, String fieldName) throws SQLException {
        List<Stmp> listStmp = new ArrayList<Stmp>();
        try {

            conn = new DBManage().open();
            sql = new StringBuilder();
            
            sql.append(" SELECT ");
            sql.append(" DISTINCT(").append(fieldName).append(") AS stmp ");
            sql.append(" FROM ");
            sql.append(tbName);
            sql.append(" WHERE stmp IS NOT NULL");
            sql.append(" ORDER BY '").append(fieldName).append("'");

            System.out.println("sql ::==" + sql.toString());
            pstm = this.conn.prepareStatement(sql.toString());
            
            rs = pstm.executeQuery();
            while (rs.next()) {
                Stmp stmp = new Stmp();
                // System.out.println("rs.getString(\"stmp\")"+rs.getString("stmp"));
                String[] arrStmp = new FunctionUtil().subStringStmp(rs.getString("stmp"));
                stmp.setStmp(rs.getString("stmp"));
                //System.out.println("stmp===>>" + rs.getString("stmp"));
                stmp.setStmpY(arrStmp[0]);
                stmp.setStmpM(arrStmp[1]);
                stmp.setStmpN(arrStmp[2]);
                stmp.setStmpYM(arrStmp[0] + arrStmp[1]);
                listStmp.add(stmp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pstm != null) {
                pstm.close();
            }
            if (rs != null) {
                rs.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return listStmp;
    }

    public List<HospitalService> getServiceAll(String serviceCode) throws SQLException {

        PreparedStatement pstmt = null;
        StringBuilder sql = null;
        ResultSet rs = null;
        List<HospitalService> listHosp = new ArrayList<HospitalService>();
        try {
            conn = new DBManage().open();

            System.out.println("serviceCode : " + serviceCode);

            sql = new StringBuilder();
            sql.append(" SELECT * FROM MASTER_HOSPITAL");
            sql.append(" WHERE PROVINCE_ID = '").append(1000).append("'");
            sql.append("  AND (HCODE LIKE '%").append(String.valueOf(serviceCode)).append("%'");
            sql.append("  OR HNAME LIKE '%").append(String.valueOf(serviceCode)).append("%'");
            sql.append(" )");
            sql.append("  ORDER BY HCODE ASC");

            System.out.println("sql ==>>" + sql);
            pstmt = conn.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                HospitalService service = new HospitalService();
                service.setHosCode(rs.getString("HCODE"));
                service.setHosCodeName(rs.getString("HNAME"));
                listHosp.add(service);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (rs != null) {
                rs.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return listHosp;
    }

    public static void main(String[] args) {
        System.out.println("new UtilDao().getThaiBath(99999); ::==" + new UtilDao().getThaiBath(99999));
    }
}
