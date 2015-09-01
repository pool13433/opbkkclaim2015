/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.dao;

import com.claim.object.HospitalService;
import com.claim.object.ObjRptVajiraRfDetail;
import com.claim.object.ObjRptVajiraRfSummary;
import com.claim.object.OppReport;
import com.claim.support.Console;
import com.claim.constants.ConstantMessage;
import com.claim.support.StringOpUtil;
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
public class VajiraREDao {

    // ######### JDBC #########
    Connection connection = null;
    PreparedStatement pstm = null;
    ResultSet rs = null;
    StringBuilder sql = null;
    int exec = 0;
    // ######### JDBC #########

    private static String INVOICE_NO = "(select INVOICE_NO from opbkk_service s where s.txid = hc.txid) as INVOICE_NO";

    public void setConnection(Connection conn) {
        this.connection = conn;
    }

    public List<HospitalService> getHospitalServiceWithHmain(OppReport report) {
        List<HospitalService> listData = null;
        HospitalService data = null;
        try {
            sql = new StringBuilder();
            sql.append(" SELECT DISTINCT(HMAIN) as HMAIN,HMAIN_NAME FROM RPT_OPBKK_VAJIRA_RF ");
            sql.append(" WHERE 1=1 ");
            if (!report.getYearMonth().equals("") && !report.getNo().equals("")) {
                sql.append(" AND STMP = '").append(report.getYearMonth()).append("-").append(report.getNo()).append("'");
            }
            if (report.sizeListServiceCode() > 0) {
                sql.append("  AND HMAIN in (");
                for (HospitalService hospitalService : report.getListServiceCode()) {
                    sql.append("'").append(hospitalService.getHosCode()).append("',");
                }
                sql.append(" '')");
            }
            if (!report.getBudget_year().equals("")) {
                int budgetYear = Integer.parseInt(report.getBudget_year());
                int minBudgetYear = (budgetYear - 1);
                int maxBudgetYear = budgetYear;
                sql.append(" and trunc(dateopd) between to_date('0110").append(minBudgetYear).append("','ddmmyyyy')");
                sql.append(" and to_date('3009").append(maxBudgetYear).append("','ddmmyyyy')");
            }
            sql.append(" ORDER BY HMAIN");

            pstm = connection.prepareStatement(sql.toString());
            rs = pstm.executeQuery();
            listData = new ArrayList<HospitalService>();
            while (rs.next()) {
                data = new HospitalService();
                data.setHosHmain(rs.getString("HMAIN"));
                data.setHosHmainName(rs.getString("HMAIN_NAME"));
                listData.add(data);
            }
            /*
             SQL Stagement
             */
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

    public List<HospitalService> getHospitalServiceWithHcode(OppReport report) {
        List<HospitalService> listData = null;
        HospitalService data = null;
        try {
            sql = new StringBuilder();
            sql.append(" SELECT DISTINCT(HCODE) as HCODE,HCODE_NAME FROM RPT_OPBKK_VAJIRA_RF ");
            sql.append(" WHERE 1=1 ");
            if (!report.getYearMonth().equals("") && !report.getNo().equals("")) {
                sql.append(" AND STMP = '").append(report.getYearMonth()).append("-").append(report.getNo()).append("'");
            }
            if (report.sizeListServiceCode() > 0) {
                sql.append("  AND HCODE in (");
                for (HospitalService hospitalService : report.getListServiceCode()) {
                    sql.append("'").append(hospitalService.getHosCode()).append("',");
                }
                sql.append(" '')");
            }
            if (!report.getBudget_year().equals("")) {
                int budgetYear = Integer.parseInt(report.getBudget_year());
                int minBudgetYear = (budgetYear - 1);
                int maxBudgetYear = budgetYear;
                sql.append(" and trunc(dateopd) between to_date('0110").append(minBudgetYear).append("','ddmmyyyy')");
                sql.append(" and to_date('3009").append(maxBudgetYear).append("','ddmmyyyy')");
            }
            sql.append(" ORDER BY HCODE");

            pstm = connection.prepareStatement(sql.toString());
            rs = pstm.executeQuery();
            listData = new ArrayList<HospitalService>();
            while (rs.next()) {
                data = new HospitalService();
                data.setHosCode(rs.getString("HCODE"));
                data.setHosCodeName(rs.getString("HCODE_NAME"));
                listData.add(data);
            }
            /*
             SQL Stagement
             */
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

    public List<ObjRptVajiraRfDetail> getListVajiraDetail(OppReport report) {
        List<ObjRptVajiraRfDetail> listData = null;
        ObjRptVajiraRfDetail objData = null;
        try {

            sql = new StringBuilder();
            sql.append(" SELECT ");
            sql.append(" hc.AGE, hc.APPROVED, hc.CHRG_202, hc.CHRG_HC, hc.CHRG_OTHER, hc.CHRG_STDITEM, ");
            sql.append(" hc.CHRG_TOTAL, hc.DATEOPD, hc.HCODE, hc.HCODE_NAME, hc.HMAIN, hc.HMAIN_NAME, ");
            sql.append(" to_char(DATEOPD,'dd monyyyy','NLS_CALENDAR=''Thai Buddha'' NLS_DATE_LANGUAGE=THAI') dateopd_thai_buddha,");
            sql.append(" hc.HMAINIP, hc.HMAINIP_NAME, hc.HN, hc.PAID_202, hc.PAID_MODEL, hc.PAID_OTHER, ");
            sql.append(" hc.PAID_STDITEM, hc.PAID_TOTAL, hc.PDXCODE, hc.PAID_HC, hc.PID, hc.PNAME, ");
            sql.append(" hc.POINT, hc.REIMBURSE, hc.REMARK, hc.RPTDATE, hc.SEX, hc.STMP, ");
            sql.append(" hc.TOTALREIMBURSE, hc.TXID");
            /*
             Modified by poolsawat 01-07-2015
             */
            sql.append(" ,").append(INVOICE_NO);
            sql.append(" FROM RPT_OPBKK_VAJIRA_RF hc");
            sql.append(" WHERE STMP = '").append(report.getYearMonth()).append("-").append(report.getNo()).append("'");
            if (!report.getBudget_year().equals("")) {
                int budgetYear = Integer.parseInt(report.getBudget_year());
                int minBudgetYear = (budgetYear - 1);
                int maxBudgetYear = budgetYear;
                sql.append(" and trunc(dateopd) between to_date('0110").append(minBudgetYear).append("','ddmmyyyy')");
                sql.append(" and to_date('3009").append(maxBudgetYear).append("','ddmmyyyy')");
            }
            if (!report.getServiceCode().equals("")) {
                sql.append(" AND HCODE = '").append(report.getServiceCode()).append("' ");
            }
            sql.append(" ORDER BY PID,DATEOPD ASC");

            System.out.println("sql ::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstm = connection.prepareStatement(sql.toString());
            rs = pstm.executeQuery();
            listData = new ArrayList<ObjRptVajiraRfDetail>();
            while (rs.next()) {
                objData = new ObjRptVajiraRfDetail();
                objData.setAge(rs.getInt("AGE"));
                objData.setApproved(rs.getString("APPROVED"));
                objData.setChrg_202(rs.getDouble("CHRG_202"));
                objData.setChrg_hc(rs.getDouble("CHRG_HC"));
                objData.setChrg_other(rs.getDouble("CHRG_OTHER"));
                objData.setChrg_stditem(rs.getDouble("CHRG_STDITEM"));
                objData.setChrg_total(rs.getDouble("CHRG_TOTAL"));
                objData.setDateopd(rs.getString("DATEOPD"));
                objData.setHcode(rs.getString("HCODE"));
                objData.setHcode_name(rs.getString("HCODE_NAME"));
                objData.setHmain(rs.getString("HMAIN"));
                objData.setHmain_name(rs.getString("HMAIN_NAME"));
                objData.setHmainip(rs.getString("HMAINIP"));
                objData.setHmainip_name(rs.getString("HMAINIP_NAME"));
                objData.setHn(rs.getString("HN"));
                objData.setPaid_202(rs.getDouble("PAID_202"));
                objData.setPaid_model(rs.getDouble("PAID_MODEL"));
                objData.setPaid_other(rs.getDouble("PAID_OTHER"));
                objData.setPaid_stditem(rs.getDouble("PAID_STDITEM"));
                objData.setPaid_total(rs.getDouble("PAID_TOTAL"));
                objData.setPdxcode(rs.getString("PDXCODE"));
                objData.setPaid_hc(rs.getString("PAID_HC"));
                objData.setPid(rs.getString("PID"));
                objData.setPname(rs.getString("PNAME"));
                objData.setPoint(rs.getDouble("POINT"));
                objData.setReimburse(rs.getDouble("REIMBURSE"));
                objData.setRemark(rs.getString("REMARK"));
                objData.setRptdate(rs.getString("RPTDATE"));
                objData.setSex(rs.getString("SEX"));
                objData.setStmp(rs.getString("STMP"));
                objData.setTotalreimburse(rs.getDouble("TOTALREIMBURSE"));
                objData.setTxid(rs.getString("TXID"));
                objData.setInvoice_no(rs.getString("INVOICE_NO"));
                objData.setDateopd_th(rs.getString("dateopd_thai_buddha"));
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

    public List<ObjRptVajiraRfSummary> getListVajiraSumGroupHcode(OppReport report) {
        List<ObjRptVajiraRfSummary> listData = null;
        ObjRptVajiraRfSummary objData = null;
        try {
            sql = new StringBuilder();
            sql.append(" SELECT HCODE,");
            sql.append(" HCODE_NAME,");
            sql.append(" COUNT (PID) AS TOTAL_PID,");
            sql.append(" COUNT (TXID) AS TOTAL_TXID,");
            sql.append(" SUM (CHRG_HC) SUM_CHRG_HC,");
            sql.append(" SUM (CHRG_202) SUM_CHRG_202,");
            sql.append(" SUM (CHRG_STDITEM) SUM_CHRG_STDITEM,");
            sql.append(" SUM (CHRG_OTHER) SUM_CHRG_OTHER,");
            sql.append(" SUM (CHRG_TOTAL) SUM_CHRG_TOTAL,");
            sql.append(" SUM (PAID_HC) SUM_PAID_HC,");
            sql.append(" SUM (PAID_202) SUM_PAID_202,");
            sql.append(" SUM (PAID_STDITEM) SUM_PAID_STDITEM,");
            sql.append(" SUM (PAID_OTHER) SUM_PAID_OTHER,");
            sql.append(" SUM (PAID_TOTAL) SUM_PAID_TOTAL,");
            sql.append(" SUM (POINT) SUM_POINT,");
            sql.append(" SUM (REIMBURSE) SUM_REIMBURSE,");
            sql.append(" SUM (TOTALREIMBURSE) SUM_TOTALREIMBURSE");
            sql.append(" FROM RPT_OPBKK_VAJIRA_RF");

            sql.append(" WHERE 1=1 ");
            sql.append(" AND STMP = '").append(report.getYearMonth()).append("-").append(report.getNo()).append("'");
            if (!report.getBudget_year().equals("")) {
                int budgetYear = Integer.parseInt(report.getBudget_year());
                int minBudgetYear = (budgetYear - 1);
                int maxBudgetYear = budgetYear;
                sql.append(" and trunc(dateopd) between to_date('0110").append(minBudgetYear).append("','ddmmyyyy')");
                sql.append(" and to_date('3009").append(maxBudgetYear).append("','ddmmyyyy')");
            }

            sql.append(" GROUP BY HCODE, HCODE_NAME");
            sql.append(" ORDER BY HCODE");

            System.out.println("sql ::==" + sql.toString());

            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstm = connection.prepareStatement(sql.toString());
            rs = pstm.executeQuery();
            listData = new ArrayList<ObjRptVajiraRfSummary>();
            while (rs.next()) {
                objData = new ObjRptVajiraRfSummary();
                objData.setCount_pid(rs.getInt("TOTAL_PID"));
                objData.setCount_txid(rs.getInt("TOTAL_TXID"));
                objData.setHcode(rs.getString("HCODE"));
                objData.setHcode_name(rs.getString("HCODE_NAME"));
                objData.setSum_chrg_202(rs.getDouble("SUM_CHRG_202"));
                objData.setSum_chrg_hc(rs.getDouble("SUM_CHRG_HC"));
                objData.setSum_chrg_other(rs.getDouble("SUM_CHRG_OTHER"));
                objData.setSum_chrg_stditem(rs.getDouble("SUM_CHRG_STDITEM"));
                objData.setSum_chrg_total(rs.getDouble("SUM_CHRG_TOTAL"));
                objData.setSum_paid_202(rs.getDouble("SUM_PAID_202"));
                objData.setSum_paid_hc(rs.getDouble("SUM_PAID_HC"));
                objData.setSum_paid_other(rs.getDouble("SUM_PAID_OTHER"));
                objData.setSum_paid_stditem(rs.getDouble("SUM_PAID_STDITEM"));
                objData.setSum_paid_total(rs.getDouble("SUM_PAID_TOTAL"));
                objData.setSum_point(rs.getDouble("SUM_POINT"));
                objData.setSum_reimburse(rs.getDouble("SUM_REIMBURSE"));
                objData.setSum_totalreimburse(rs.getDouble("SUM_TOTALREIMBURSE"));
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

    public List<ObjRptVajiraRfSummary> getListVajiraSumGroupHmain(OppReport report) {
        List<ObjRptVajiraRfSummary> listData = null;
        ObjRptVajiraRfSummary objData = null;
        try {
            sql = new StringBuilder();
            sql.append(" SELECT HMAIN,");
            sql.append(" HMAIN_NAME,");
            sql.append(" COUNT (PID) AS TOTAL_PID,");
            sql.append(" COUNT (TXID) AS TOTAL_TXID,");
            sql.append(" SUM (CHRG_HC) SUM_CHRG_HC,");
            sql.append(" SUM (CHRG_202) SUM_CHRG_202,");
            sql.append(" SUM (CHRG_STDITEM) SUM_CHRG_STDITEM,");
            sql.append(" SUM (CHRG_OTHER) SUM_CHRG_OTHER,");
            sql.append(" SUM (CHRG_TOTAL) SUM_CHRG_TOTAL,");
            sql.append(" SUM (PAID_HC) SUM_PAID_HC,");
            sql.append(" SUM (PAID_202) SUM_PAID_202,");
            sql.append(" SUM (PAID_STDITEM) SUM_PAID_STDITEM,");
            sql.append(" SUM (PAID_OTHER) SUM_PAID_OTHER,");
            sql.append(" SUM (PAID_TOTAL) SUM_PAID_TOTAL,");
            sql.append(" SUM (POINT) SUM_POINT,");
            sql.append(" SUM (REIMBURSE) SUM_REIMBURSE,");
            sql.append(" SUM (TOTALREIMBURSE) SUM_TOTALREIMBURSE");
            sql.append(" FROM RPT_OPBKK_VAJIRA_RF");

            sql.append(" WHERE 1=1 ");
            sql.append(" AND STMP = '").append(report.getYearMonth()).append("-").append(report.getNo()).append("'");
            if (!report.getBudget_year().equals("")) {
                int budgetYear = Integer.parseInt(report.getBudget_year());
                int minBudgetYear = (budgetYear - 1);
                int maxBudgetYear = budgetYear;
                sql.append(" and trunc(dateopd) between to_date('0110").append(minBudgetYear).append("','ddmmyyyy')");
                sql.append(" and to_date('3009").append(maxBudgetYear).append("','ddmmyyyy')");
            }
            if (!StringOpUtil.removeNull(report.getServiceCode()).equals("")) {
                sql.append(" AND HCODE = '").append(report.getServiceCode()).append("' ");
            }
            sql.append(" GROUP BY HMAIN,HMAIN_NAME");
            sql.append(" ORDER BY HMAIN");

            System.out.println("sql ::==" + sql.toString());

            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstm = connection.prepareStatement(sql.toString());
            rs = pstm.executeQuery();
            listData = new ArrayList<ObjRptVajiraRfSummary>();
            while (rs.next()) {
                objData = new ObjRptVajiraRfSummary();
                objData.setCount_pid(rs.getInt("TOTAL_PID"));
                objData.setCount_txid(rs.getInt("TOTAL_TXID"));
                objData.setHmain(rs.getString("HMAIN"));
                objData.setHmain_name(rs.getString("HMAIN_NAME"));
                objData.setSum_chrg_202(rs.getDouble("SUM_CHRG_202"));
                objData.setSum_chrg_hc(rs.getDouble("SUM_CHRG_HC"));
                objData.setSum_chrg_other(rs.getDouble("SUM_CHRG_OTHER"));
                objData.setSum_chrg_stditem(rs.getDouble("SUM_CHRG_STDITEM"));
                objData.setSum_chrg_total(rs.getDouble("SUM_CHRG_TOTAL"));
                objData.setSum_paid_202(rs.getDouble("SUM_PAID_202"));
                objData.setSum_paid_hc(rs.getDouble("SUM_PAID_HC"));
                objData.setSum_paid_other(rs.getDouble("SUM_PAID_OTHER"));
                objData.setSum_paid_stditem(rs.getDouble("SUM_PAID_STDITEM"));
                objData.setSum_paid_total(rs.getDouble("SUM_PAID_TOTAL"));
                objData.setSum_point(rs.getDouble("SUM_POINT"));
                objData.setSum_reimburse(rs.getDouble("SUM_REIMBURSE"));
                objData.setSum_totalreimburse(rs.getDouble("SUM_TOTALREIMBURSE"));
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
