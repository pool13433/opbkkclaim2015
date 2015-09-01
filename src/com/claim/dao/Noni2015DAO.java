/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the edito
 */
package com.claim.dao;

import com.claim.object.HospitalService;
import com.claim.object.ObjRptNoniDetail;
import com.claim.object.ObjRptNoniSum;
import com.claim.object.OppReport;
import com.claim.support.Console;
import com.claim.constants.ConstantMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Poolsawat.a
 */
public class Noni2015DAO {

    // ######### JDBC #########
    Connection connection = null;
    PreparedStatement pstm = null;
    ResultSet rs = null;
    StringBuilder sql = null;
    int exec = 0;
    // ######### JDBC #########

    public void setConnection(Connection conn) {
        this.connection = conn;
    }

    public List<HospitalService> getHospitalService(OppReport report) {
        List<HospitalService> listData = null;
        HospitalService data = null;
        try {
            sql = new StringBuilder();
            sql.append(" SELECT HCODE,HCODENAME FROM RPT_OPBKK_NONI");
            sql.append(" WHERE STMP  = '").append(report.getStmp()).append("' ");
            if (!report.getServiceCode().equals("")) {
                sql.append(" AND HCODE = '").append(report.getServiceCode()).append("' ");
            }
            if (!report.getBudget_year().equals("")) {
                int budgetYear = Integer.parseInt(report.getBudget_year());
                int minBudgetYear = (budgetYear - 1);
                int maxBudgetYear = budgetYear;
                sql.append(" and trunc(dateopd) between to_date('0110").append(minBudgetYear).append("','ddmmyyyy')");
                sql.append(" and to_date('3009").append(maxBudgetYear).append("','ddmmyyyy')");
            }
            sql.append(" GROUP BY HCODE,HCODENAME");
            sql.append(" ORDER BY HCODE ASC");
            
            System.out.println("sql ::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }
            
            pstm = connection.prepareStatement(sql.toString());
            rs = pstm.executeQuery();
            listData = new ArrayList<HospitalService>();
            while (rs.next()) {
                data = new HospitalService();
                data.setHosCode(rs.getString("HCODE"));
                data.setHosCodeName(rs.getString("HCODENAME"));
                listData.add(data);
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

    public List<ObjRptNoniDetail> getListNoniDetail(OppReport report) {
        List<ObjRptNoniDetail> listData = null;
        ObjRptNoniDetail data = null;
        try {
            sql = new StringBuilder();
            sql.append(" select ");
            sql.append(" r.txid, r.hcode, r.hcodename, ");
            sql.append(" r.hmain, r.hmainname, r.pid, ");
            sql.append(" r.hn, r.pname, r.dateopd, ");
            sql.append(" to_char(r.DATEOPD,'dd monyyyy','NLS_CALENDAR=''Thai Buddha'' NLS_DATE_LANGUAGE=THAI') dateopd_thai_buddha,");
            sql.append(" r.noniclass, r.icd10, r.icd9, ");
            sql.append(" r.chrg_ophc, r.chrg_197, r.chrg_stditem, ");
            sql.append(" (r.CHRG_OPHC + r.CHRG_197 + r.CHRG_STDITEM) as chrg_middle_priced_items,");
            sql.append(" r.chrg_other, r.chrg_total, r.payrate, ");
            sql.append(" r.paid_ophc, r.paid_197, r.paid_stditem, ");
            sql.append(" (r.PAID_OPHC +r.PAID_197 +r.PAID_STDITEM) as paid_middle_priced_items,");
            sql.append(" r.paid_other, r.paid_total, r.remark, ");
            sql.append(" r.stmp, r.rpt_date");
            
            /*
             modified 01-07-2015 by poolsawat
             */
            sql.append(" ,(select INVOICE_NO from opbkk_service s where s.txid = r.txid) as INVOICE_NO");
            /*
             modified 01-07-2015 by poolsawat
             */
            
            sql.append(" from rpt_opbkk_noni r");
            sql.append(" WHERE r.STMP = '").append(report.getStmp()).append("' ");
            if (!report.getServiceCode().equals("")) {
                sql.append(" AND r.HCODE = '").append(report.getServiceCode()).append("' ");
            }
            if (!report.getBudget_year().equals("")) {
                int budgetYear = Integer.parseInt(report.getBudget_year());
                int minBudgetYear = (budgetYear - 1);
                int maxBudgetYear = budgetYear;
                sql.append(" and trunc(dateopd) between to_date('0110").append(minBudgetYear).append("','ddmmyyyy')");
                sql.append(" and to_date('3009").append(maxBudgetYear).append("','ddmmyyyy')");
            }
            sql.append(" ORDER BY r.hmain,r.dateopd");
            
            System.out.println("sql ::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstm = connection.prepareStatement(sql.toString());
            rs = pstm.executeQuery();
            listData = new ArrayList<>();
            while (rs.next()) {
                data = new ObjRptNoniDetail();
                data.setTxid(rs.getString("txid"));
                data.setHcode(rs.getString("hcode"));
                data.setHcodename(rs.getString("hcodename"));
                data.setHmain(rs.getString("hmain"));
                data.setHmainname(rs.getString("hmainname"));
                data.setPid(rs.getString("pid"));
                data.setHn(rs.getString("hn"));
                data.setPname(rs.getString("pname"));
                data.setDateopd(rs.getString("dateopd"));
                data.setDateopd_thai_buddha(rs.getString("dateopd_thai_buddha"));
                data.setNoniclass(rs.getString("noniclass"));
                data.setIcd10(rs.getString("icd10"));
                data.setIcd9(rs.getString("icd9"));
                data.setChrg_ophc(rs.getDouble("chrg_ophc"));
                data.setChrg_197(rs.getDouble("chrg_197"));
                data.setChrg_stditem(rs.getDouble("chrg_stditem"));
                data.setChrg_middle_priced_items(rs.getDouble("chrg_middle_priced_items"));
                data.setChrg_other(rs.getDouble("chrg_other"));
                data.setChrg_total(rs.getDouble("chrg_total"));
                data.setPayrate(rs.getDouble("payrate"));
                data.setPaid_ophc(rs.getDouble("paid_ophc"));
                data.setPaid_197(rs.getDouble("paid_197"));
                data.setPaid_stditem(rs.getDouble("paid_stditem"));
                data.setPaid_middle_priced_items(rs.getDouble("paid_middle_priced_items"));
                data.setPaid_other(rs.getDouble("paid_other"));
                data.setPaid_total(rs.getDouble("paid_total"));
                data.setRemark(rs.getString("remark"));
                data.setStmp(rs.getString("stmp"));
                data.setRpt_date(rs.getString("rpt_date"));
                data.setInvoice_no(rs.getString("INVOICE_NO"));
                listData.add(data);
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
                Logger.getLogger(Noni2015DAO.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return listData;
    }

    public List<ObjRptNoniSum> getListNoniSum(OppReport report) {
        List<ObjRptNoniSum> listData = null;
        ObjRptNoniSum data = null;
        try {
            sql = new StringBuilder();
            sql.append(" SELECT HCODE,HCODENAME,COUNT(*) AS COUNT_VISIT_ALL,");
            sql.append(" SUM(CHRG_OPHC+CHRG_197+CHRG_STDITEM) AS SUM_CHRG_STD,");
            sql.append(" SUM(CHRG_OTHER) AS SUM_CHRG_OTHER,SUM(CHRG_TOTAL) AS SUM_CHRG_TOTAL,");
            sql.append(" SUM(PAID_OPHC+PAID_197+PAID_STDITEM) AS SUM_PAID_STD,");
            sql.append(" SUM(PAID_OTHER) AS SUM_PAID_OTHER,SUM(PAID_TOTAL) AS SUM_PAID_TOTAL");
            sql.append(" FROM RPT_OPBKK_NONI");
            sql.append(" WHERE STMP = ?");
            if (!report.getBudget_year().equals("")) {
                int budgetYear = Integer.parseInt(report.getBudget_year());
                int minBudgetYear = (budgetYear - 1);
                int maxBudgetYear = budgetYear;
                sql.append(" and trunc(dateopd) between to_date('0110").append(minBudgetYear).append("','ddmmyyyy')");
                sql.append(" and to_date('3009").append(maxBudgetYear).append("','ddmmyyyy')");
            }
            sql.append(" GROUP BY HCODE,HCODENAME");
            sql.append(" ORDER BY HCODE");
            System.out.println("stmp :" + report.getStmp());
            
            System.out.println("sql ::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstm = connection.prepareStatement(sql.toString());
            pstm.setString(1, report.getStmp());
            rs = pstm.executeQuery();
            listData = new ArrayList<>();
            while (rs.next()) {
                data = new ObjRptNoniSum();
                data.setHcode(rs.getString("hcode"));
                data.setHcodename(rs.getString("hcodename"));
                data.setCount_visit(rs.getInt("count_visit_all"));
                data.setSum_chrg_middle_priced_items(rs.getDouble("SUM_CHRG_STD"));
                data.setSum_chrg_other(rs.getDouble("sum_chrg_other"));
                data.setSum_chrg_total(rs.getDouble("sum_chrg_total"));
                data.setSum_paid_middle_priced_items(rs.getDouble("SUM_PAID_STD"));
                data.setSum_paid_other(rs.getDouble("sum_paid_other"));
                data.setSum_paid_total(rs.getDouble("sum_paid_total"));
                listData.add(data);
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
                Logger.getLogger(Noni2015DAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listData;
    }

    public String getNoniStmpDescDistinct(String obYM, String obN) {
        String titleTimeStmpDesc = "";
        StringBuilder sql = null;
        ResultSet rs = null;
        PreparedStatement pstm = null;
        try {
            sql = new StringBuilder(" SELECT");
            sql.append(" distinct(select rh.STMP_DESC ");
            sql.append(" from rpt_header rh where rh.stmp=r.stmp and rh.OPTYPE='NO') STMP_DESC");
            sql.append("  from rpt_opbkk_noni r");

            sql.append(" WHERE r.STMP = '").append(obYM).append("-").append(obN).append("'");

            System.out.println("sql ::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }
            
            pstm = connection.prepareStatement(sql.toString());
            rs = pstm.executeQuery();

            while (rs.next()) {
                titleTimeStmpDesc = rs.getString("STMP_DESC");
            }

            return titleTimeStmpDesc;
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
                Logger.getLogger(Noni2015DAO.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return titleTimeStmpDesc;
    }
}
