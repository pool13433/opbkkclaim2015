/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.dao;

import com.claim.object.HospitalService;
import com.claim.object.ObjRptTmdActDetail;
import com.claim.object.ObjRptTmdActSummary;
import com.claim.object.ObjRptTmdMomDetail;
import com.claim.object.ObjRptTmdMomSummary;
import com.claim.object.OppReport;
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
 * @author Poolsawat
 */
public class ThaiMedicineDao {

    // ######### JDBC #########
    Connection connection = null;
    PreparedStatement pstm = null;
    ResultSet rs = null;
    StringBuilder sql = null;
    int exec = 0;
    // ######### JDBC #########
    
    private static String INVOICE_NO = "(select INVOICE_NO from opbkk_service s where s.txid = tmd.txid) as INVOICE_NO";
    
    public ThaiMedicineDao() {

    }

    public void setConnection(Connection conn) {
        this.connection = conn;
    }

    public HospitalService getDateOpdMinMax(OppReport report) {
        HospitalService data = null;
        try {
            sql = new StringBuilder();
            sql.append(" SELECT TRIM (TO_CHAR (MIN (DATEOPD),'DD','NLS_CALENDAR=''Thai Buddha'' NLS_DATE_LANGUAGE=THAI'))|| ' ' || ");
            sql.append(" TRIM (TO_CHAR (MIN (DATEOPD),'Month','NLS_CALENDAR=''Thai Buddha'' NLS_DATE_LANGUAGE=THAI'))|| ' ' ||  ");
            sql.append(" TRIM (TO_CHAR (MIN (DATEOPD),'YYYY','NLS_CALENDAR=''Thai Buddha'' NLS_DATE_LANGUAGE=THAI'))");
            sql.append(" dateopd_begin_th,");
            sql.append(" TRIM (TO_CHAR (MAX (DATEOPD),'DD','NLS_CALENDAR=''Thai Buddha'' NLS_DATE_LANGUAGE=THAI'))|| ' ' || ");
            sql.append(" TRIM (TO_CHAR (MAX (DATEOPD),'Month','NLS_CALENDAR=''Thai Buddha'' NLS_DATE_LANGUAGE=THAI'))|| ' ' ||  ");
            sql.append(" TRIM (TO_CHAR (MAX (DATEOPD),'YYYY','NLS_CALENDAR=''Thai Buddha'' NLS_DATE_LANGUAGE=THAI'))");
            sql.append(" dateopd_end_th");
            sql.append(" FROM ").append(report.getTmdTableName());

            sql.append(" WHERE 1=1 ");
            if (!report.getYearMonth().equals("") && !report.getNo().equals("")) {
                sql.append(" AND STMP = '" + report.getYearMonth() + "-" + report.getNo() + "'");
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
            System.out.println("sql ::==" + sql.toString());
            pstm = connection.prepareStatement(sql.toString());
            rs = pstm.executeQuery();
            if (rs.next()) {
                data = new HospitalService();
                data.setDateopd_begin(rs.getString("dateopd_begin_th"));
                data.setDateopd_end(rs.getString("dateopd_end_th"));
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
        return data;
    }

    public List<HospitalService> getHospitalService(OppReport report) {
        List<HospitalService> listData = null;
        HospitalService data = null;
        try {
            sql = new StringBuilder();
            sql.append(" SELECT DISTINCT(HCODE) as HCODE,HCODENAME FROM ").append(report.getTmdServiceType());
            sql.append(" WHERE 1=1 ");
            if (!report.getYearMonth().equals("") && !report.getNo().equals("")) {
                sql.append(" AND STMP = '" + report.getYearMonth() + "-" + report.getNo() + "'");
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
                data.setHosCodeName(rs.getString("HCODENAME"));
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

    public List<ObjRptTmdActDetail> getListTmdAct(OppReport report) {
        List<ObjRptTmdActDetail> listData = null;
        ObjRptTmdActDetail data = null;
        try {
            sql = new StringBuilder();
            sql.append(" SELECT DENSE_RANK() OVER (PARTITION BY hcode ORDER BY pid) rn_hcode,");
            sql.append(" DATEOPD, PID, ");
            sql.append(" AGE_YEAR,HCODE, ");
            sql.append(" to_char(DATEOPD,'dd monyyyy','NLS_CALENDAR=''Thai Buddha'' NLS_DATE_LANGUAGE=THAI') dateopd_thai_buddha,");
            sql.append(" HCODENAME, HMAIN, HMAINNAME, ");
            sql.append(" HN, ITEM_CODE, ITEM_DESC, ");
            sql.append(" ITEM_TYPE, PNAME, ");
            sql.append(" POINT, QTY, RATEPAY, ");
            sql.append(" REMARK, RPT_DATE, SEX, ");
            /*
            Modified By Poolsawat 06-08-2015
            */
            //sql.append(" STMP, ROUND(TOTALCHRG) TOTALCHRG, ROUND(TOTALPAY) TOTALPAY, ");
            sql.append(" STMP, TOTALCHRG TOTALCHRG, TOTALPAY TOTALPAY, ");
            sql.append(" TXID, UNITPRICE");
            
            /*
             Modified by poolsawat 01-07-2015
            */
            sql.append(" ,").append(INVOICE_NO);
            
            sql.append(" FROM RPT_OPBKK_TMD_ACT tmd");                                    
            sql.append(" WHERE 1=1 ");
            sql.append(" AND STMP = '").append(report.getYearMonth()).append("-").append(report.getNo()).append("'");
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
            sql.append(" ORDER BY PID,DATEOPD,ITEM_TYPE ASC");

            if (ConstantMessage.IS_SHOW_QUERY) {
                System.out.println("sql ::==" + sql.toString());
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstm = connection.prepareStatement(sql.toString());
            rs = pstm.executeQuery();
            listData = new ArrayList<ObjRptTmdActDetail>();
            while (rs.next()) {
                data = new ObjRptTmdActDetail();
                data.setRank_hcode(rs.getInt("rn_hcode"));
                data.setAge_year(rs.getInt("age_year"));
                data.setDateopd(rs.getString("dateopd"));
                data.setDateopd_th(rs.getString("dateopd_thai_buddha"));
                data.setHcode(rs.getString("hcode"));
                data.setHcodename(rs.getString("hcodename"));
                data.setHmain(rs.getString("hmain"));
                data.setHmainname(rs.getString("hmainname"));
                data.setHn(rs.getString("hn"));
                data.setItem_code(rs.getString("item_code"));
                data.setItem_desc(rs.getString("item_desc"));
                data.setItem_type(rs.getString("item_type"));
                data.setPid(rs.getString("pid"));
                data.setPname(rs.getString("pname"));
                data.setPoint(rs.getDouble("point"));
                data.setQty(rs.getInt("qty"));
                data.setRatepay(rs.getInt("ratepay"));
                data.setRemark(rs.getString("remark"));
                data.setRpt_date(rs.getString("rpt_date"));
                data.setSex(rs.getInt("sex"));
                //data.setSex_name(rs.getString(""));
                data.setStmp(rs.getString("stmp"));
                data.setTotalchrg(rs.getDouble("totalchrg"));
                data.setTotalpay(rs.getDouble("totalpay"));
                data.setTxid(rs.getString("txid"));
                data.setUnitprice(rs.getInt("unitprice"));
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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listData;
    }

    public List<ObjRptTmdMomDetail> getListTmdMom(OppReport report) {
        List<ObjRptTmdMomDetail> listData = null;
        ObjRptTmdMomDetail data = null;
        try {
            sql = new StringBuilder();
            sql.append(" SELECT ");
            sql.append(" AGE_YEAR, DATEOPD, HCODE,");
            sql.append(" to_char(DATEOPD,'dd monyyyy','NLS_CALENDAR=''Thai Buddha'' NLS_DATE_LANGUAGE=THAI') dateopd_thai_buddha,");
            sql.append(" HCODENAME, HMAIN, HMAINNAME, ");
            sql.append(" HN, ITEM_CODE, ITEM_DESC, ");
            sql.append(" ITEM_TYPE, PID, PNAME, ");
            sql.append(" POINT, QTY, RATEPAY, ");
            sql.append(" REMARK, RPT_DATE, SEX,");
            /*
            Modified By poolsawat
            */
            //sql.append(" STMP, ROUND(TOTALCHRG) TOTALCHRG, ROUND(TOTALPAY) TOTALPAY, ");
            sql.append(" STMP, TOTALCHRG TOTALCHRG, TOTALPAY TOTALPAY, ");
            /*
            Modified By poolsawat
            */
                       
            sql.append(" TXID, UNITPRICE,");
            sql.append(" CASE  PLACE");
            sql.append(" WHEN '1' THEN 'ในหน่วยบริการ'"); //  --1 ในหน่วยบริการ  2 นอกหน่วยบริการ   default=1
            sql.append(" WHEN '2' THEN 'นอกหน่วยบริการ'");
            sql.append(" ELSE 'ในหน่วยบริการ'");
            sql.append(" END ");
            sql.append(" CASE_PLACE");
            
             /*
             Modified by poolsawat 01-07-2015
            */
            sql.append(" ,").append(INVOICE_NO);
            
            sql.append(" FROM RPT_OPBKK_TMD_MOM tmd");
            sql.append(" WHERE 1=1");
            sql.append(" AND STMP = '").append(report.getYearMonth()).append("-").append(report.getNo()).append("'");
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
            if (ConstantMessage.IS_SHOW_QUERY) {
                System.out.println("sql ::==" + sql.toString());
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }
            pstm = connection.prepareStatement(sql.toString());
            rs = pstm.executeQuery();
            listData = new ArrayList<ObjRptTmdMomDetail>();
            while (rs.next()) {
                data = new ObjRptTmdMomDetail();
                data.setAge_year(rs.getInt("age_year"));
                data.setDateopd(rs.getString("dateopd"));
                data.setDateopd_th(rs.getString("dateopd_thai_buddha"));
                data.setHcode(rs.getString("hcode"));
                data.setHcodename(rs.getString("hcodename"));
                data.setHmain(rs.getString("hmain"));
                data.setHmainname(rs.getString("hmainname"));
                data.setHn(rs.getString("hn"));
                data.setItem_code(rs.getString("item_code"));
                data.setItem_desc(rs.getString("item_desc"));
                data.setItem_type(rs.getString("item_type"));
                data.setPid(rs.getString("pid"));
                data.setPname(rs.getString("pname"));
                data.setPoint(rs.getDouble("point"));
                data.setQty(rs.getInt("qty"));
                data.setRatepay(rs.getInt("ratepay"));
                data.setRemark(rs.getString("remark"));
                data.setRpt_date(rs.getString("rpt_date"));
                data.setSex(rs.getInt("sex"));
                //data.setSex_name(rs.getString(""));
                data.setStmp(rs.getString("stmp"));
                data.setTotalchrg(rs.getDouble("totalchrg"));
                data.setTotalpay(rs.getDouble("totalpay"));
                data.setTxid(rs.getString("txid"));
                data.setUnitprice(rs.getInt("unitprice"));
                data.setCase_place(rs.getString("CASE_PLACE"));
                
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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listData;
    }

    public List<ObjRptTmdMomSummary> getListSummaryTmdMom(OppReport report) {
        List<ObjRptTmdMomSummary> listData = null;
        ObjRptTmdMomSummary data = null;
        try {
            
            String stmp = report.getYearMonth()+"-"+report.getNo();
            sql = new StringBuilder();
            sql.append(" SELECT HCODE,");
            sql.append(" HCODENAME,");
            sql.append(" (SELECT COUNT (DISTINCT TXID)");
            sql.append(" FROM RPT_OPBKK_TMD_MOM RTM");
            sql.append(" WHERE place = 1 and RTM.hcode = RA.hcode AND RTM.STMP = '").append(stmp).append("')");
            sql.append(" countinhosp,");
            sql.append(" (SELECT ROUND(SUM(POINT))");
            sql.append(" FROM RPT_OPBKK_TMD_MOM RTM");
            sql.append(" WHERE place = 1 and RTM.hcode = RA.hcode AND RTM.STMP = '").append(stmp).append("')");
            sql.append(" suminhosp,");
            sql.append(" (SELECT COUNT (DISTINCT TXID)");
            sql.append(" FROM RPT_OPBKK_TMD_MOM RTM");
            sql.append(" WHERE place = 2 and RTM.hcode = RA.hcode AND RTM.STMP = '").append(stmp).append("')");
            sql.append(" countouthosp,");
            sql.append(" (SELECT ROUND(SUM (POINT))");
            sql.append(" FROM RPT_OPBKK_TMD_MOM RTM");
            sql.append(" WHERE place = 2 and RTM.hcode = RA.hcode AND RTM.STMP = '").append(stmp).append("')");
            sql.append(" sumouthosp,");
            sql.append(" ROUND(SUM (TOTALPAY)) sumtotalpay");
            
            sql.append(" FROM RPT_OPBKK_TMD_MOM RA");
            //sql.append(" FROM aer_opbkk_tmd_mom RA"); // for test
            
            sql.append(" WHERE 1=1 ");
            sql.append(" AND STMP = '").append(report.getYearMonth()).append("-").append(report.getNo()).append("'");
            if (!report.getBudget_year().equals("")) {
                int budgetYear = Integer.parseInt(report.getBudget_year());
                int minBudgetYear = (budgetYear - 1);
                int maxBudgetYear = budgetYear;
                sql.append(" and trunc(dateopd) between to_date('0110").append(minBudgetYear).append("','ddmmyyyy')");
                sql.append(" and to_date('3009").append(maxBudgetYear).append("','ddmmyyyy')");
            }
            sql.append(" GROUP BY HCODE, HCODENAME");
            sql.append(" ORDER BY HCODE ASC");

            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }
            System.out.println("sql ::=="+sql.toString());
            
            pstm = connection.prepareStatement(sql.toString());
            rs = pstm.executeQuery();
            listData = new ArrayList<ObjRptTmdMomSummary>();
            while (rs.next()) {
                data = new ObjRptTmdMomSummary();
                data.setHcode(rs.getString("hcode"));
                data.setHcodename(rs.getString("hcodename"));
                data.setCount_in_hosp(rs.getInt("countinhosp"));
                data.setSum_in_hosp(rs.getDouble("suminhosp"));
                data.setCount_out_hosp(rs.getInt("countouthosp"));
                data.setSum_out_hosp(rs.getDouble("sumouthosp"));
                data.setSum_totalpay(rs.getDouble("sumtotalpay"));
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

    public List<ObjRptTmdActSummary> getListSummaryTmdAct(OppReport report) {
        List<ObjRptTmdActSummary> listData = null;
        ObjRptTmdActSummary data = null;
        try {
            
            String stmp = report.getYearMonth()+"-"+report.getNo();
            sql = new StringBuilder();
            sql.append(" SELECT HCODE,");
            sql.append(" HCODENAME,");
            
            sql.append(" (SELECT COUNT(DISTINCT TXID)");
            sql.append(" FROM RPT_OPBKK_TMD_ACT rta");
            sql.append(" WHERE ITEM_TYPE = 'นวด' AND ra.hcode = rta.hcode AND rta.STMP = '").append(stmp).append("')");
            sql.append(" COUNT_DIS_TXID1,");// COUNT_DIS_TXID1
            sql.append(" (SELECT ROUND(SUM (POINT))");
            sql.append(" FROM RPT_OPBKK_TMD_ACT rta");
            sql.append(" WHERE ITEM_TYPE = 'นวด' AND ra.hcode = rta.hcode AND rta.STMP = '").append(stmp).append("')");
            sql.append(" SUM_POINT1,");  //SUM_POTN1
            sql.append(" (SELECT ROUND(SUM (TOTALPAY))");
            sql.append(" FROM RPT_OPBKK_TMD_ACT rta");
            sql.append(" WHERE ITEM_TYPE = 'นวด' AND ra.hcode = rta.hcode AND rta.STMP = '").append(stmp).append("')");
            sql.append(" SUM_TOTALPAY1,");  //SUM_TOTALPAY1
            
            sql.append(" (SELECT COUNT (DISTINCT TXID)");
            sql.append(" FROM RPT_OPBKK_TMD_ACT rta");
            sql.append(" WHERE ITEM_TYPE = 'ประคบ' AND ra.hcode = rta.hcode AND rta.STMP = '").append(stmp).append("')");
            sql.append(" COUNT_DIS_TXID2,");  //COUNT_DIS_TXID2
            sql.append(" (SELECT ROUND(SUM (POINT))");
            sql.append(" FROM RPT_OPBKK_TMD_ACT rta");
            sql.append(" WHERE ITEM_TYPE = 'ประคบ' AND ra.hcode = rta.hcode AND rta.STMP = '").append(stmp).append("')");
            sql.append(" SUM_POINT2,"); //SUM_POINT2
            sql.append(" (SELECT ROUND(SUM (TOTALPAY))");
            sql.append(" FROM RPT_OPBKK_TMD_ACT rta");
            sql.append(" WHERE ITEM_TYPE = 'ประคบ' AND ra.hcode = rta.hcode AND rta.STMP = '").append(stmp).append("')");
            sql.append(" SUM_TOTALPAY2,");  //SUM_TOTALPAY2
            
            sql.append(" (SELECT COUNT (DISTINCT TXID)");
            sql.append(" FROM RPT_OPBKK_TMD_ACT rta");
            sql.append(" WHERE ITEM_TYPE = 'นวดและประคบสมุนไพร' ");
            sql.append(" AND ra.hcode = rta.hcode AND rta.STMP = '").append(stmp).append("')");
            sql.append(" COUNT_DIS_TXID3,");  //COUNT_DIS_TXID3
            sql.append(" (SELECT ROUND(SUM (POINT))");
            sql.append(" FROM RPT_OPBKK_TMD_ACT rta");
            sql.append(" WHERE ITEM_TYPE = 'นวดและประคบสมุนไพร' ");
            sql.append(" AND ra.hcode = rta.hcode AND rta.STMP = '").append(stmp).append("')");
            sql.append(" SUM_POINT3,");  //SUM_POINT3
            sql.append(" (SELECT ROUND(SUM (TOTALPAY))");
            sql.append(" FROM RPT_OPBKK_TMD_ACT rta");
            sql.append(" WHERE ITEM_TYPE = 'นวดและประคบสมุนไพร' ");
            sql.append(" AND ra.hcode = rta.hcode AND rta.STMP = '").append(stmp).append("')");
            sql.append(" SUM_TOTALPAY3,");  //SUM_TOTALPAY3
            
            sql.append(" (SELECT COUNT (DISTINCT TXID)");
            sql.append(" FROM RPT_OPBKK_TMD_ACT rta");
            sql.append(" WHERE ITEM_TYPE = 'อบสมุนไพร'");
            sql.append(" AND ra.hcode = rta.hcode AND rta.STMP = '").append(stmp).append("')");
            sql.append(" COUNT_DIS_TXID4,");  //COUNT_DIS_TXID4
            sql.append(" (SELECT ROUND(SUM (POINT))");
            sql.append(" FROM RPT_OPBKK_TMD_ACT rta");
            sql.append(" WHERE ITEM_TYPE = 'อบสมุนไพร'");
            sql.append(" AND ra.hcode = rta.hcode AND rta.STMP = '").append(stmp).append("')");
            sql.append(" SUM_POINT4,");  //SUM_POINT4
            sql.append(" (SELECT ROUND(SUM (totalpay))");
            sql.append(" FROM RPT_OPBKK_TMD_ACT rta");
            sql.append(" WHERE ITEM_TYPE = 'อบสมุนไพร'");
            sql.append(" AND ra.hcode = rta.hcode AND rta.STMP = '").append(stmp).append("')");
            sql.append(" SUM_TOTALPAY4,");  //SUM_TOTALPAY4
            /*
             modified by poolsawat 21-07-2015 chang [COUNT (DISTINCT txid) => COUNT (txid)]
            */
            sql.append(" (SELECT COUNT (TXID)");
            sql.append(" FROM RPT_OPBKK_TMD_ACT RTA");
            sql.append(" WHERE ITEM_TYPE = 'ยาสมุนไพร' AND ra.hcode = rta.hcode AND rta.STMP = '").append(stmp).append("')");
            sql.append(" COUNT_DIS_TXID5,");  //COUNT_DIS_TXID5
             /*
             modified by poolsawat 21-07-2015
            */
            
            sql.append(" (SELECT ROUND(SUM (POINT))");
            sql.append(" FROM RPT_OPBKK_TMD_ACT RTA");
            sql.append(" WHERE ITEM_TYPE = 'ยาสมุนไพร' AND ra.hcode = rta.hcode AND rta.STMP = '").append(stmp).append("')");
            sql.append(" SUM_POINT5,");   //SUM_POINT5
            sql.append(" (SELECT ROUND(SUM (TOTALPAY))");
            sql.append(" FROM RPT_OPBKK_TMD_ACT RTA");
            sql.append(" WHERE ITEM_TYPE = 'ยาสมุนไพร' AND ra.hcode = rta.hcode AND rta.STMP = '").append(stmp).append("')");
            sql.append(" SUM_TOTALPAY5,");   //SUM_TOTALPAY5
            sql.append(" ROUND(SUM(TOTALPAY)) AS SUM_TOTALPAYALL");  //SUM_TOTALPAYALL
            //sql.append(" --,(SELECT SUM(TOTALPAY) FROM RPT_OPBKK_TMD_ACT rta WHERE rta.hcode = ra.hcode)           ");
                        
            sql.append(" FROM RPT_OPBKK_TMD_ACT RA");
            //sql.append(" FROM aer_opbkk_tmd_act RA"); for test
            
            sql.append(" WHERE 1=1 ");
            sql.append(" AND STMP = '").append(report.getYearMonth()).append("-").append(report.getNo()).append("'");
            if (!report.getBudget_year().equals("")) {
                int budgetYear = Integer.parseInt(report.getBudget_year());
                int minBudgetYear = (budgetYear - 1);
                int maxBudgetYear = budgetYear;
                sql.append(" and trunc(dateopd) between to_date('0110").append(minBudgetYear).append("','ddmmyyyy')");
                sql.append(" and to_date('3009").append(maxBudgetYear).append("','ddmmyyyy')");
            }
            sql.append(" GROUP BY HCODE, HCODENAME");
            sql.append(" ORDER BY HCODE ASC");
            if (ConstantMessage.IS_SHOW_QUERY) {
                System.out.println("sql ::==" + sql.toString());
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }
            pstm = connection.prepareStatement(sql.toString());
            rs = pstm.executeQuery();
            listData = new ArrayList<ObjRptTmdActSummary>();
            while (rs.next()) {
                data = new ObjRptTmdActSummary();
                data.setHcode(rs.getString("hcode"));
                data.setHcodename(rs.getString("hcodename"));
                data.setCount_dis_txid1(rs.getInt("COUNT_DIS_TXID1"));
                data.setSum_point1(rs.getDouble("SUM_POINT1"));
                data.setSum_totalpay1(rs.getDouble("SUM_TOTALPAY1"));

                data.setCount_dis_txid2(rs.getInt("COUNT_DIS_TXID2"));
                data.setSum_point2(rs.getDouble("SUM_POINT2"));
                data.setSum_totalpay2(rs.getDouble("SUM_TOTALPAY2"));

                data.setCount_dis_txid3(rs.getInt("COUNT_DIS_TXID3"));
                data.setSum_point3(rs.getDouble("SUM_POINT3"));
                data.setSum_totalpay3(rs.getDouble("SUM_TOTALPAY3"));

                data.setCount_dis_txid4(rs.getInt("COUNT_DIS_TXID4"));
                data.setSum_point4(rs.getDouble("SUM_POINT4"));
                data.setSum_totalpay4(rs.getDouble("SUM_TOTALPAY4"));

                data.setCount_dis_txid5(rs.getInt("COUNT_DIS_TXID5"));
                data.setSum_point5(rs.getDouble("SUM_POINT5"));
                data.setSum_totalpay5(rs.getDouble("SUM_TOTALPAY5"));

                data.setSum_totalpay_all(rs.getDouble("SUM_TOTALPAYALL"));

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

}
