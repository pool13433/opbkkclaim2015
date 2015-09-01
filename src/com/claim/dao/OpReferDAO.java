package com.claim.dao;

import com.claim.connection.ConnectionDB;
import com.claim.object.HospitalService;
import com.claim.object.OppReport;
import com.claim.support.Console;
import com.claim.constants.ConstantMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OpReferDAO {

    static final String APPROVE_NORMAL = "('91','5')";
    static final String APPROVE_APPEAL = "('92','8')";

    public Connection conn = null;

    public OpReferDAO() {
        //conn = getConOracle();
    }

    public Connection getConOracle() {
        Connection vConn = null;
        ConnectionDB connec = new ConnectionDB("True", "", "", "", "", "", "", "1");
        vConn = connec.getConnectionInf();
        return vConn;
    }

    public String findBudgetYear(String year) {
        String budgetYear = "10-" + (Integer.parseInt(year) - 1);
        return budgetYear;
    }

    public List<HospitalService> getHospitalService(OppReport report) {
        StringBuilder sql = null;
        List<HospitalService> listHS = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {

            this.openConnection();

            //if (report.getReportType() == 0) { // detail
            if (report.getCategory() == 'D') { // deduct
                if (report.getAttribute() == 'S') { // Single
                    sql = new StringBuilder(" SELECT");
                    sql.append(" DISTINCT(HMAIN),HMAINNAME  FROM RPT_OPBKK_OPREFER WHERE 1=1");
                    sql.append("  and exists(select 'x' from L_SINGLE_CLINIC b where b.hmainop=hmain)  ");

                    if (report.sizeListServiceCode() > 0) {
                        sql.append("  AND HMAIN in (");
                        for (HospitalService hospitalService : report.getListServiceCode()) {
                            sql.append("'").append(hospitalService.getHosCode()).append("',");
                        }
                        sql.append(" '')");
                    }
                    if (!report.getYearMonth().equals("") && !report.getNo().equals("")) {
                        sql.append("  AND STMP = '").append(report.getYearMonth()).append("-").append(report.getNo()).append("'");
                    }
                    if (!report.getBudget_year().equals("")) {
                        int budgetYear = Integer.parseInt(report.getBudget_year());
                        int minBudgetYear = (budgetYear - 1);
                        int maxBudgetYear = budgetYear;
                        //sql.append(" AND EXTRACT(YEAR FROM dateopd) = '").append(report.getLookup_year()).append("'");
//                        sql.append(" AND  to_char(dateopd,'MM-YYYY') >= '10-").append(minBudgetYear).append("'");
//                        sql.append(" AND  to_char(dateopd,'MM-YYYY') < '10-").append(maxBudgetYear).append("'");

                        sql.append(" and trunc(dateopd) between to_date('0110").append(minBudgetYear).append("','ddmmyyyy')");
                        sql.append(" and to_date('3009").append(maxBudgetYear).append("','ddmmyyyy')");

                    }
                    sql.append(" ORDER BY HMAIN ASC");
                } else if (report.getAttribute() == 'A') { // A = All 
                    sql = new StringBuilder(" SELECT");
                    sql.append(" DISTINCT(HMAIN),HMAINNAME  FROM RPT_OPBKK_OPREFER  WHERE 1=1");
                    sql.append("  and not exists(select 'x' from L_SINGLE_CLINIC b where b.hmainop=hmain)  ");

                    if (report.sizeListServiceCode() > 0) {
                        sql.append("  AND HMAIN in (");
                        for (HospitalService hospitalService : report.getListServiceCode()) {
                            sql.append("'" + hospitalService.getHosCode() + "',");
                        }
                        sql.append(" '')");
                    }
                    if (!report.getYearMonth().equals("") && !report.getNo().equals("")) {
                        sql.append(" AND STMP = '" + report.getYearMonth() + "-" + report.getNo() + "'");
                    }
                    if (!report.getBudget_year().equals("")) {
                        int budgetYear = Integer.parseInt(report.getBudget_year());
                        int minBudgetYear = (budgetYear - 1);
                        int maxBudgetYear = budgetYear;
                        sql.append(" and trunc(dateopd) between to_date('0110").append(minBudgetYear).append("','ddmmyyyy')");
                        sql.append(" and to_date('3009").append(maxBudgetYear).append("','ddmmyyyy')");
                    }
                    sql.append(" ORDER BY HMAIN ASC");
                } else {
                    sql = new StringBuilder(" SELECT");
                    sql.append(" DISTINCT(HMAIN),HMAINNAME  FROM RPT_OPBKK_OPREFER  WHERE 1=1");

                    if (report.sizeListServiceCode() > 0) {
                        sql.append("  AND HMAIN in (");
                        for (HospitalService hospitalService : report.getListServiceCode()) {
                            sql.append("'" + hospitalService.getHosCode() + "',");
                        }
                        sql.append(" '')");
                    }
                    if (!report.getYearMonth().equals("") && !report.getNo().equals("")) {
                        sql.append(" AND STMP = '" + report.getYearMonth() + "-" + report.getNo() + "'");
                    }
                    if (!report.getBudget_year().equals("")) {
                        int budgetYear = Integer.parseInt(report.getBudget_year());
                        int minBudgetYear = (budgetYear - 1);
                        int maxBudgetYear = budgetYear;
                        sql.append(" and trunc(dateopd) between to_date('0110").append(minBudgetYear).append("','ddmmyyyy')");
                        sql.append(" and to_date('3009").append(maxBudgetYear).append("','ddmmyyyy')");
                    }
                    sql.append(" ORDER BY HMAIN ASC");
                }
            } else { // payment 
                    /*if (report.getAttribute() == 'S') {  // single
                 sql = new StringBuilder(" SELECT");
                 sql.append(" DISTINCT(HCODE),HCODENAME FROM rpt_opbkk_oprefer WHERE 1=1");
                 sql.append(" AND is_single = 1 ");

                 if (report.getServiceCode() != null) {
                 sql.append(" AND HCODE = '" + report.getServiceCode() + "'");
                 }
                 if (report.getYearMonth() != "" && report.getNo() != null) {
                 sql.append(" AND STMP = '" + report.getYearMonth() + "-" + report.getNo() + "'");
                 }
                 } else { // A = All Clinic
                 sql = new StringBuilder(" SELECT");
                 sql.append(" DISTINCT(HCODE),HCODENAME FROM rpt_opbkk_oprefer WHERE 1=1");
                 sql.append(" AND is_single = 0");

                 if (report.getServiceCode() != null) {
                 sql.append(" AND HCODE = '" + report.getServiceCode() + "'");
                 }
                 if (report.getYearMonth() != "" && report.getNo() != null) {
                 sql.append(" AND STMP = '" + report.getYearMonth() + "-" + report.getNo() + "'");
                 }
                 }*/
                sql = new StringBuilder(" SELECT");
                sql.append(" DISTINCT(HCODE),HCODENAME FROM rpt_opbkk_oprefer WHERE 1=1");

                if (report.sizeListServiceCode() > 0) {
                    sql.append("  AND HCODE in (");
                    for (HospitalService hospitalService : report.getListServiceCode()) {
                        sql.append("'").append(hospitalService.getHosCode()).append("',");
                    }
                    sql.append(" '')");
                }
                if (!report.getYearMonth().equals("") && !report.getNo().equals("")) {
                    sql.append(" AND STMP = '" + report.getYearMonth() + "-" + report.getNo() + "'");
                }
                if (!report.getBudget_year().equals("")) {
                    int budgetYear = Integer.parseInt(report.getBudget_year());
                    int minBudgetYear = (budgetYear - 1);
                    int maxBudgetYear = budgetYear;
                    sql.append(" and trunc(dateopd) between to_date('0110").append(minBudgetYear).append("','ddmmyyyy')");
                    sql.append(" and to_date('3009").append(maxBudgetYear).append("','ddmmyyyy')");
                }
                sql.append(" ORDER BY HCODE ASC");
            }
            //}
            System.out.println("sql ::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstmt = conn.prepareCall(sql.toString());
            rs = pstmt.executeQuery();

            listHS = new ArrayList<HospitalService>();
            while (rs.next()) {
                HospitalService service = new HospitalService();
                if (report.getCategory() == 'D') {  // deduct 
                    service.setHosHmain(rs.getString("HMAIN"));
                    service.setHosHmainName(rs.getString("HMAINNAME"));
                } else { // payment
                    service.setHosCode(rs.getString("HCODE"));
                    service.setHosCodeName(rs.getString("HCODENAME"));
                }
                listHS.add(service);
            }
            return listHS;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }
        return listHS;
    }

    public ResultSet getReportOpRefer(OppReport report) {
        ResultSet rs = null;
        StringBuilder sql = null;
        PreparedStatement pstmt = null;
        try {
            this.openConnection();
            System.out.println("report : " + report.toString());

            if (report.getReportType() == 0) { // 0 = detail
                if (report.getCategory() == 'D') { // D = deduct 
                    System.out.println("report.getAttribute()==>>" + report.getAttribute());
                    if (report.getAttribute() == 'S') { // S=  SingleClinic 
                        sql = new StringBuilder(" SELECT");
                        sql.append(" rownum no,PID,HN,HCODE,PNAME,HCODENAME");
                        sql.append(" , dateopd,to_char(dateopd,'dd monyyyy','NLS_CALENDAR=''Thai Buddha'' NLS_DATE_LANGUAGE=THAI')dateopd_thai_buddha");
                        sql.append(" , pdxcode, ");
                        sql.append(" nvl(chrg_hc,0)chrg_hc, nvl(chrg_197,0)chrg_197, nvl(chrg_drug,0)chrg_drug, nvl(chrg_other,0)chrg_other,nvl(chrg_total,0)chrg_total,");
                        sql.append(" nvl(paid_hc,0)paid_hc, nvl(paid_197,0)paid_197, nvl(paid_drug,0)paid_drug, nvl(paid_other,0)paid_other, nvl(paid_total,0)paid_total, ");
                        sql.append(" nvl(deduct_ophc,0)deduct_ophc, nvl(deduct_oprf,0)deduct_oprf, nvl(deduct_hmain,0)deduct_hmain, nvl(deduct_total,0)deduct_total,nvl(deduct_hmain,0) deduct_hmain_final,");
                        sql.append(" remark,is_single, stmp");
                        sql.append(" ,txid");  // modifide by 2014-08-04

                        /*
                         modified 01-07-2015 by poolsawat
                         */
                        sql.append(" ,(select INVOICE_NO from opbkk_service s where s.txid = refer.txid) as INVOICE_NO");
                        /*
                         modified 01-07-2015 by poolsawat
                         */

                        sql.append(" from RPT_OPBKK_OPREFER refer");
                        sql.append(" where 1=1");
                        sql.append(" and exists(select 'x' from L_SINGLE_CLINIC b where b.hmainop=refer.hmain) ");
                        if (!report.getServiceCode().equals("")) {
                            sql.append(" AND HMAIN = '").append(report.getServiceCode()).append("'");
                        }
                        if (!report.getYearMonth().equals("") && !report.getNo().equals("")) {
                            sql.append(" AND STMP = '").append(report.getYearMonth()).append("-").append(report.getNo()).append("'");
                        }
                        if (!report.getBudget_year().equals("")) {
                            int budgetYear = Integer.parseInt(report.getBudget_year());
                            int minBudgetYear = (budgetYear - 1);
                            int maxBudgetYear = budgetYear;
                            sql.append(" and trunc(dateopd) between to_date('0110").append(minBudgetYear).append("','ddmmyyyy')");
                            sql.append(" and to_date('3009").append(maxBudgetYear).append("','ddmmyyyy')");
                        }
                        sql.append(" ORDER BY rpt_date,dateopd ASC");

                        System.out.println("หัก เดี่ยว");
                    } else if (report.getAttribute() == 'A') { // A = All clinic
                        sql = new StringBuilder(" SELECT");
                        sql.append(" rownum no,PID,HN,HCODE,PNAME,HCODENAME");
                        sql.append(" , dateopd,to_char(dateopd,'dd monyyyy','NLS_CALENDAR=''Thai Buddha'' NLS_DATE_LANGUAGE=THAI')dateopd_thai_buddha");
                        sql.append(" , pdxcode, ");
                        sql.append(" nvl(chrg_hc,0)chrg_hc, nvl(chrg_197,0)chrg_197, nvl(chrg_drug,0)chrg_drug, nvl(chrg_other,0)chrg_other,nvl(chrg_total,0)chrg_total,");
                        sql.append(" nvl(paid_hc,0)paid_hc, nvl(paid_197,0)paid_197, nvl(paid_drug,0)paid_drug, nvl(paid_other,0)paid_other, nvl(paid_total,0)paid_total, ");
                        sql.append(" nvl(deduct_ophc,0)deduct_ophc, nvl(deduct_oprf,0)deduct_oprf, nvl(deduct_hmain,0)deduct_hmain, nvl(deduct_total,0)deduct_total,nvl(deduct_hmain,0) deduct_hmain_final,");
                        sql.append(" remark,is_single, stmp");
                        sql.append(" ,txid");  // modifide by 2014-08-04
                        
                        /*
                         modified 01-07-2015 by poolsawat
                         */
                        sql.append(" ,(select INVOICE_NO from opbkk_service s where s.txid = refer.txid) as INVOICE_NO");
                        /*
                         modified 01-07-2015 by poolsawat
                         */
                        
                        sql.append(" from RPT_OPBKK_OPREFER refer");
                        sql.append(" where 1=1");
                        sql.append("  and not exists(select 'x' from L_SINGLE_CLINIC b where b.hmainop=refer.hmain)  ");
                        if (!report.getServiceCode().equals("")) {
                            sql.append(" AND HMAIN = '" + report.getServiceCode() + "'");
                        }
                        if (!report.getYearMonth().equals("") && !report.getNo().equals("")) {
                            sql.append(" AND STMP = '").append(report.getYearMonth()).append("-").append(report.getNo()).append("'");
                        }
                        if (!report.getBudget_year().equals("")) {
                            int budgetYear = Integer.parseInt(report.getBudget_year());
                            int minBudgetYear = (budgetYear - 1);
                            int maxBudgetYear = budgetYear;
                            sql.append(" and trunc(dateopd) between to_date('0110").append(minBudgetYear).append("','ddmmyyyy')");
                            sql.append(" and to_date('3009").append(maxBudgetYear).append("','ddmmyyyy')");
                        }
                        sql.append(" ORDER BY rpt_date,dateopd ASC");

                        System.out.println("หัก เครือ");
                    } else {
                        sql = new StringBuilder(" SELECT");
                        sql.append(" rownum no,PID,HN,HCODE,PNAME,HCODENAME");
                        sql.append(" , dateopd,to_char(dateopd,'dd monyyyy','NLS_CALENDAR=''Thai Buddha'' NLS_DATE_LANGUAGE=THAI')dateopd_thai_buddha");
                        sql.append(" , pdxcode, ");
                        sql.append(" nvl(chrg_hc,0)chrg_hc, nvl(chrg_197,0)chrg_197, nvl(chrg_drug,0)chrg_drug, nvl(chrg_other,0)chrg_other,nvl(chrg_total,0)chrg_total,");
                        sql.append(" nvl(paid_hc,0)paid_hc, nvl(paid_197,0)paid_197, nvl(paid_drug,0)paid_drug, nvl(paid_other,0)paid_other, nvl(paid_total,0)paid_total, ");
                        sql.append(" nvl(deduct_ophc,0)deduct_ophc, nvl(deduct_oprf,0)deduct_oprf, nvl(deduct_hmain,0)deduct_hmain, nvl(deduct_total,0)deduct_total,nvl(deduct_hmain,0) deduct_hmain_final,");
                        sql.append(" remark,is_single, stmp");
                        sql.append(" ,txid");  // modifide by 2014-08-04
                        
                        
                        /*
                         modified 01-07-2015 by poolsawat
                         */
                        sql.append(" ,(select INVOICE_NO from opbkk_service s where s.txid = refer.txid) as INVOICE_NO");
                        /*
                         modified 01-07-2015 by poolsawat
                         */
                        
                        sql.append(" from RPT_OPBKK_OPREFER refer");
                        sql.append(" where 1=1");
                        if (!report.getServiceCode().equals("")) {
                            sql.append(" AND HMAIN = '").append(report.getServiceCode()).append("'");
                        }
                        if (!report.getYearMonth().equals("") && !report.getNo().equals("")) {
                            sql.append(" AND STMP = '").append(report.getYearMonth()).append("-").append(report.getNo()).append("'");
                        }
                        if (!report.getBudget_year().equals("")) {
                            int budgetYear = Integer.parseInt(report.getBudget_year());
                            int minBudgetYear = (budgetYear - 1);
                            int maxBudgetYear = budgetYear;
                            sql.append(" and trunc(dateopd) between to_date('0110").append(minBudgetYear).append("','ddmmyyyy')");
                            sql.append(" and to_date('3009").append(maxBudgetYear).append("','ddmmyyyy')");
                        }
                        sql.append(" ORDER BY rpt_date,dateopd ASC");

                        System.out.println("หัก ไม่แยก เครื่อ เดี่ยว");
                    }
                } else { // P =  payment 
                   /* if (report.getAttribute() == 'S') { // S = SingleClinci
                     sql = new StringBuilder(" SELECT");
                     sql.append(" PID,HN, hmainop_n, hmainop_name,FNAME||' '||LNAME FLNAME,HNAME, dateopd, pdxcode,");
                     sql.append(" to_char(dateopd,'dd monyyyy','NLS_CALENDAR=''Thai Buddha'' NLS_DATE_LANGUAGE=THAI') as dateopdTh,");
                     sql.append(" chrg_hc, chrg_197, chrg_drug, chrg_other, chrg_total,");
                     sql.append(" paid_hc, paid_197, paid_drug, paid_other, paid_total,");
                     sql.append(" remark,");
                     sql.append(" is_single, stmp");
                     sql.append(" from RPT_OPBKK_OPREFER refer WHERE 1=1");
                     sql.append(" AND is_single = 1");
                     sql.append(" ");
                     sql.append(" ");
                     if (report.getServiceCode() != "") {
                     sql.append(" AND HCODE = '" + report.getServiceCode() + "'");
                     }
                     if (report.getYearMonth() != "" && report.getNo() != null) {
                     sql.append(" AND STMP = '" + report.getYearMonth() + "-" + report.getNo() + "'");
                     }
                     System.out.println("จ่าย เดี่ยว");
                     } else { // A = AllClinic
                     sql = new StringBuilder(" SELECT");
                     sql.append(" PID,HN, hmainop_n, hmainop_name,FNAME||' '||LNAME FLNAME,HNAME, dateopd, pdxcode, ");
                     sql.append(" to_char(dateopd,'dd monyyyy','NLS_CALENDAR=''Thai Buddha'' NLS_DATE_LANGUAGE=THAI') as dateopdTh,");
                     sql.append(" chrg_hc, chrg_197, chrg_drug, chrg_other, chrg_total,");
                     sql.append(" paid_hc, paid_197, paid_drug, paid_other, paid_total, ");
                     sql.append(" remark,");
                     sql.append(" is_single, stmp");
                     sql.append(" from RPT_OPBKK_OPREFER refer WHERE 1=1");
                     sql.append(" AND is_single = 0 ");

                     if (report.getServiceCode() != "") {
                     sql.append(" AND HCODE = '" + report.getServiceCode() + "'");
                     }
                     if (report.getYearMonth() != "" && report.getNo() != null) {
                     sql.append(" AND STMP = '" + report.getYearMonth() + "-" + report.getNo() + "'");
                     }
                     System.out.println("จ่าย เครือ");
                     }*/
                    sql = new StringBuilder("SELECT ");
                    sql.append(" rownum no,PID,HN, hmain, hmainname,PNAME,hcodeNAME, pdxcode,");
                    sql.append(" dateopd,to_char(dateopd,'dd monyyyy','NLS_CALENDAR=''Thai Buddha'' NLS_DATE_LANGUAGE=THAI')dateopd_thai_buddha,");
                    sql.append(" chrg_hc, chrg_197, chrg_drug, chrg_other, chrg_total,");
                    sql.append(" paid_hc, paid_197, paid_drug, paid_other, paid_total, ");
                    sql.append(" remark,");
                    sql.append(" is_single, stmp");
                    sql.append(" ,txid");  // modifide by 2014-08-04
                    
                    
                    /*
                         modified 01-07-2015 by poolsawat
                         */
                        sql.append(" ,(select INVOICE_NO from opbkk_service s where s.txid = refer.txid) as INVOICE_NO");
                        /*
                         modified 01-07-2015 by poolsawat
                         */
                    
                    
                    sql.append(" from RPT_OPBKK_OPREFER refer");
                    sql.append(" where 1=1");

                    if (!report.getServiceCode().equals("")) {
                        sql.append(" AND HCODE = '").append(report.getServiceCode()).append("'");
                    }
                    if (!report.getYearMonth().equals("") && !report.getNo().equals("")) {
                        sql.append(" AND STMP = '").append(report.getYearMonth()).append("-").append(report.getNo()).append("'");
                    }
                    if (!report.getBudget_year().equals("")) {
                        int budgetYear = Integer.parseInt(report.getBudget_year());
                        int minBudgetYear = (budgetYear - 1);
                        int maxBudgetYear = budgetYear;
                        sql.append(" and trunc(dateopd) between to_date('0110").append(minBudgetYear).append("','ddmmyyyy')");
                        sql.append(" and to_date('3009").append(maxBudgetYear).append("','ddmmyyyy')");
                    }
                    sql.append(" ORDER BY rpt_date,dateopd ASC");
                }
            } else { // 1 = summary
                if (report.getCategory() == 'D') { // D = Duduct Summary
                    if (report.getAttribute() == 'S') { // S = SingleClinic
                        sql = new StringBuilder(" SELECT");
                        sql.append(" row_number() OVER (ORDER BY HMAIN) no,HMAIN, HMAINNAME,");
                        // ############ แก้ไขเมื่อวันที่ 09-12-2014 เพิ่ม approve เข้าไป######## 
                        sql.append(" count(case when approved in " + APPROVE_NORMAL + " THEN HCODE end)pid_normal_count,");
                        sql.append(" count(case when approved in " + APPROVE_APPEAL + " THEN HCODE end)pid_appeal_count,");
                        sql.append(" count(case when approved in " + APPROVE_NORMAL + " OR approved in " + APPROVE_APPEAL + " THEN HMAIN end)pid_normal_appeal_total,");
                        sql.append(" sum(nvl(chrg_hc,0))sum_chrg_hc, sum(nvl(chrg_197,0))sum_chrg_197, sum(nvl(chrg_drug,0))sum_chrg_drug, sum(nvl(chrg_other,0))sum_chrg_other, sum(nvl(chrg_total,0))sum_chrg_total,");
                        sql.append(" sum(nvl(deduct_ophc,0))sum_deduct_ophc, sum(nvl(deduct_oprf,0))sum_deduct_oprf, sum(nvl(deduct_hmain,0))sum_deduct_hmain, sum(nvl(deduct_total,0))sum_deduct_total, sum(nvl(deduct_hmain,0)) deduct_hmain_final");
                        sql.append(" from RPT_OPBKK_OPREFER refer");
                        sql.append(" where 1=1");
                        sql.append("  and  exists(select 'x' from L_SINGLE_CLINIC b where b.hmainop=refer.hmain)  ");
                        if (!report.getYearMonth().equals("") && !report.getNo().equals("")) {   // sql += " AND stmp='2014011";
                            sql.append(" AND STMP = '" + report.getYearMonth() + "-" + report.getNo() + "'");
                        }
                        if (!report.getBudget_year().equals("")) {
                            int budgetYear = Integer.parseInt(report.getBudget_year());
                            int minBudgetYear = (budgetYear - 1);
                            int maxBudgetYear = budgetYear;
                            sql.append(" and trunc(dateopd) between to_date('0110").append(minBudgetYear).append("','ddmmyyyy')");
                            sql.append(" and to_date('3009").append(maxBudgetYear).append("','ddmmyyyy')");
                        }
                        sql.append(" group by HMAIN, HMAINNAME");

                    } else if (report.getAttribute() == 'A') { // A = AllClinic
                        sql = new StringBuilder(" SELECT");
                        sql.append(" row_number() OVER (ORDER BY HMAIN) no,HMAIN, HMAINNAME,");
                        sql.append(" count(case when approved in " + APPROVE_NORMAL + " THEN HCODE end)pid_normal_count,");
                        sql.append(" count(case when approved in " + APPROVE_APPEAL + " THEN HCODE end)pid_appeal_count,");
                        sql.append(" count(case when approved in " + APPROVE_NORMAL + " OR approved in " + APPROVE_APPEAL + " THEN HMAIN end)pid_normal_appeal_total,");
                        sql.append(" sum(nvl(chrg_hc,0))sum_chrg_hc, sum(nvl(chrg_197,0))sum_chrg_197, sum(nvl(chrg_drug,0))sum_chrg_drug, sum(nvl(chrg_other,0))sum_chrg_other, sum(nvl(chrg_total,0))sum_chrg_total,");
                        sql.append(" sum(nvl(deduct_ophc,0))sum_deduct_ophc, sum(nvl(deduct_oprf,0))sum_deduct_oprf, sum(nvl(deduct_hmain,0))sum_deduct_hmain, sum(nvl(deduct_total,0))sum_deduct_total, sum(nvl(deduct_hmain,0)) deduct_hmain_final");
                        sql.append(" from RPT_OPBKK_OPREFER refer");
                        sql.append(" where 1=1");
                        sql.append("  and not exists(select 'x' from L_SINGLE_CLINIC b where b.hmainop=refer.hmain)  ");
                        if (!report.getYearMonth().equals("") && !report.getNo().equals("")) {   // sql += " AND stmp='2014011";
                            sql.append(" AND STMP = '").append(report.getYearMonth()).append("-").append(report.getNo()).append("'");
                        }
                        if (!report.getBudget_year().equals("")) {
                            int budgetYear = Integer.parseInt(report.getBudget_year());
                            int minBudgetYear = (budgetYear - 1);
                            int maxBudgetYear = budgetYear;
                            sql.append(" and trunc(dateopd) between to_date('0110").append(minBudgetYear).append("','ddmmyyyy')");
                            sql.append(" and to_date('3009").append(maxBudgetYear).append("','ddmmyyyy')");
                        }
                        sql.append(" group by HMAIN, HMAINNAME");
                    } else { // รวม หัก
                        if (report.getFor_use() == 0) { // for genaral use 
                            sql = new StringBuilder(" SELECT");
                            sql.append(" row_number() OVER (ORDER BY HMAIN) no,HMAIN, HMAINNAME,");
                            sql.append(" count(case when approved in " + APPROVE_NORMAL + " THEN HCODE end)pid_normal_count,");
                            sql.append(" count(case when approved in " + APPROVE_APPEAL + " THEN HCODE end)pid_appeal_count,");
                            sql.append(" count(case when approved in " + APPROVE_NORMAL + " OR approved in " + APPROVE_APPEAL + " THEN HMAIN end)pid_normal_appeal_total,");
                            sql.append(" sum(nvl(chrg_hc,0))sum_chrg_hc, sum(nvl(chrg_197,0))sum_chrg_197, sum(nvl(chrg_drug,0))sum_chrg_drug, sum(nvl(chrg_other,0))sum_chrg_other, sum(nvl(chrg_total,0))sum_chrg_total,");
                            sql.append(" sum(nvl(deduct_ophc,0))sum_deduct_ophc, sum(nvl(deduct_oprf,0))sum_deduct_oprf, sum(nvl(deduct_hmain,0))sum_deduct_hmain, sum(nvl(deduct_total,0))sum_deduct_total, sum(nvl(deduct_hmain,0)) deduct_hmain_final");
                            sql.append(" from RPT_OPBKK_OPREFER refer");
                            sql.append(" where 1=1");
                            if (!report.getYearMonth().equals("") && !report.getNo().equals("")) {   // sql += " AND stmp='2014011";
                                sql.append(" AND STMP = '").append(report.getYearMonth()).append("-").append(report.getNo()).append("'");
                            }
                            if (!report.getServiceCode().equals("")) {
                                sql.append(" AND HMAIN = '").append(report.getServiceCode()).append("'");
                            }
                            if (!report.getBudget_year().equals("")) {
                                int budgetYear = Integer.parseInt(report.getBudget_year());
                                int minBudgetYear = (budgetYear - 1);
                                int maxBudgetYear = budgetYear;
                                sql.append(" and trunc(dateopd) between to_date('0110").append(minBudgetYear).append("','ddmmyyyy')");
                                sql.append(" and to_date('3009").append(maxBudgetYear).append("','ddmmyyyy')");
                            }
                            sql.append(" group by HMAIN,HMAINNAME");
                        } else {// for web site claim
                            /* Formatted on 10/13/2014 2:03:03 PM (QP5 v5.139.911.3011) */
                            sql = new StringBuilder(" SELECT");
                            sql.append(" row_number() OVER (ORDER BY HCODE) no,HCODE, HCODENAME,");
                            sql.append(" count(case when approved in " + APPROVE_NORMAL + " THEN HCODE end)pid_normal_count,");
                            sql.append(" count(case when approved in " + APPROVE_APPEAL + " THEN HCODE end)pid_appeal_count,");
                            sql.append(" count(case when approved in " + APPROVE_NORMAL + " OR approved in " + APPROVE_APPEAL + " THEN HMAIN end)pid_normal_appeal_total,");
                            sql.append(" sum(nvl(chrg_hc,0))sum_chrg_hc, sum(nvl(chrg_197,0))sum_chrg_197, sum(nvl(chrg_drug,0))sum_chrg_drug, sum(nvl(chrg_other,0))sum_chrg_other, sum(nvl(chrg_total,0))sum_chrg_total,");
                            sql.append(" sum(nvl(deduct_ophc,0))sum_deduct_ophc, sum(nvl(deduct_oprf,0))sum_deduct_oprf, sum(nvl(deduct_hmain,0))sum_deduct_hmain, sum(nvl(deduct_total,0))sum_deduct_total, sum(nvl(deduct_hmain,0)) deduct_hmain_final");
                            sql.append(" from RPT_OPBKK_OPREFER refer");
                            sql.append(" where 1=1");
                            if (!report.getYearMonth().equals("") && !report.getNo().equals("")) {   // sql += " AND stmp='2014011";
                                sql.append(" AND STMP = '").append(report.getYearMonth()).append("-").append(report.getNo()).append("'");
                            }
                            if (!report.getServiceCode().equals("")) {
                                sql.append(" AND HMAIN = '").append(report.getServiceCode()).append("'");
                            }
                            if (!report.getBudget_year().equals("")) {
                                int budgetYear = Integer.parseInt(report.getBudget_year());
                                int minBudgetYear = (budgetYear - 1);
                                int maxBudgetYear = budgetYear;
                                sql.append(" and trunc(dateopd) between to_date('0110").append(minBudgetYear).append("','ddmmyyyy')");
                                sql.append(" and to_date('3009").append(maxBudgetYear).append("','ddmmyyyy')");
                            }
                            sql.append(" group by HCODE,HCODENAME");
                        }
                    }
                } else { // P = Payment Summary
                    /*if (report.getAttribute() == 'S') { // Single Clinic
                     sql = new StringBuilder(" SELECT");
                     sql.append(" HCODE, hname,");
                     sql.append(" count(case when approved='91' THEN 5 end)pid_normal_count,");
                     sql.append(" count(case when approved='92' THEN 8 end)pid_appeal_count,");
                     sql.append(" count(case when approved='91' OR approved='92' THEN HCODE end)pid_normal_appeal_total,");
                     sql.append(" sum(chrg_hc) sum_chrg_hc, sum(chrg_197) sum_chrg_197, sum(chrg_drug) sum_chrg_drug, sum(chrg_other) sum_chrg_other, sum(chrg_total)sum_chrg_total,");
                     sql.append(" sum(paid_hc) sum_paid_hc, sum(paid_197) sum_paid_197, sum(paid_drug) sum_paid_drug, sum(paid_other) sum_paid_other, sum(paid_total) sum_paid_total,sum(paid_total) sum_paid_final");
                     // sql += " --sum(deduct_ophc)sum_deduct_ophc, sum(deduct_oprf)sum_deduct_oprf, sum(deduct_hmain)sum_deduct_hmain, sum(deduct_total)sum_deduct_total, sum(deduct_hmain) deduct_hmain_final";
                     sql.append(" from RPT_OPBKK_OPREFER refer");
                     sql.append(" where 1=1");
                     sql.append(" and is_single=1  ");
                     if (report.getYearMonth() != "" && report.getNo() != null) { // sql += " and stmp='2014011'";
                     sql.append(" AND STMP = '" + report.getYearMonth() + "-" + report.getNo() + "'");
                     }
                     sql.append(" group by HCODE, hname");
                     } else { // A = All Clinic
                     sql = new StringBuilder(" SELECT");
                     sql.append(" HCODE, hname,");
                     sql.append(" count(case when approved='91' THEN HCODE end)pid_normal_count,");
                     sql.append(" count(case when approved='92' THEN HCODE end)pid_appeal_count,");
                     sql.append(" count(case when approved='91' OR approved='92' THEN HCODE end)pid_normal_appeal_total,");
                     sql.append(" sum(chrg_hc) sum_chrg_hc, sum(chrg_197)sum_chrg_197, sum(chrg_drug)sum_chrg_drug, sum(chrg_other)sum_chrg_other, sum(chrg_total)sum_chrg_total,");
                     sql.append(" sum(paid_hc) sum_paid_hc, sum(paid_197) sum_paid_197, sum(paid_drug) sum_paid_drug, sum(paid_other) sum_paid_other, sum(paid_total) sum_paid_total, sum(paid_total) sum_paid_final");
                     //sql += " --sum(deduct_ophc)sum_deduct_ophc, sum(deduct_oprf)sum_deduct_oprf, sum(deduct_hmain)sum_deduct_hmain, sum(deduct_total)sum_deduct_total, sum(deduct_hmain) deduct_hmain_final";
                     sql.append(" from RPT_OPBKK_OPREFER refer");
                     sql.append(" where 1=1");
                     sql.append(" and is_single=0  "); // เมื่อ is_single มีค่า ให้เปลี่ยน เป็น is_single = 0

                     if (report.getYearMonth() != "" && report.getNo() != null) { // sql += " and stmp='2014011'";
                     sql.append(" AND STMP = '" + report.getYearMonth() + "-" + report.getNo() + "'");
                     }
                     sql.append(" group by HCODE, hname");
                     }*/

                    // รวม จ่าย สรุป
                    if (report.getFor_use() == 0) { // for genaral use
                        sql = new StringBuilder(" SELECT");
                        sql.append(" row_number() OVER (ORDER BY HCODE) no,HCODE, hcodename,");
                        sql.append(" count(case when approved in " + APPROVE_NORMAL + " THEN HCODE end)pid_normal_count,");
                        sql.append(" count(case when approved in " + APPROVE_APPEAL + " THEN HCODE end)pid_appeal_count,");
                        sql.append(" count(case when approved in " + APPROVE_NORMAL + " OR approved in " + APPROVE_APPEAL + " THEN HCODE end)pid_normal_appeal_total,");
                        sql.append(" sum(nvl(chrg_hc,0))sum_chrg_hc, sum(nvl(chrg_197,0))sum_chrg_197, sum(nvl(chrg_drug,0))sum_chrg_drug, sum(nvl(chrg_other,0))sum_chrg_other, sum(nvl(chrg_total,0))sum_chrg_total,");
                        sql.append(" sum(nvl(paid_hc,0))sum_paid_hc, sum(nvl(paid_197,0))sum_paid_197, sum(nvl(paid_drug,0))sum_paid_drug, sum(nvl(paid_other,0))sum_paid_other, sum(nvl(paid_total,0))sum_paid_total,sum(nvl(paid_total,0))sum_paid_final");
                        sql.append(" from RPT_OPBKK_OPREFER refer");
                        sql.append(" where 1=1");
                        if (!report.getYearMonth().equals("") && !report.getNo().equals("")) {
                            sql.append(" AND STMP = '").append(report.getYearMonth()).append("-").append(report.getNo()).append("'");
                        }
                        if (!report.getServiceCode().equals("")) {
                            sql.append(" AND HCODE = '").append(report.getServiceCode()).append("'");
                        }
                        if (!report.getBudget_year().equals("")) {
                            int budgetYear = Integer.parseInt(report.getBudget_year());
                            int minBudgetYear = (budgetYear - 1);
                            int maxBudgetYear = budgetYear;
                            sql.append(" and trunc(dateopd) between to_date('0110").append(minBudgetYear).append("','ddmmyyyy')");
                            sql.append(" and to_date('3009").append(maxBudgetYear).append("','ddmmyyyy')");
                        }
                        sql.append(" group by HCODE,Hcodename");
                    } else if (report.getFor_use() == 1) { // for  web site claim
                        /* Formatted on 10/13/2014 2:03:03 PM (QP5 v5.139.911.3011) */
                        sql = new StringBuilder(" SELECT ROW_NUMBER () OVER (ORDER BY hmain) no,");
                        sql.append(" hmain,");
                        sql.append(" hmainname,");
                        sql.append(" COUNT (CASE WHEN approved in " + APPROVE_NORMAL + " THEN HCODE END) pid_normal_count,");
                        sql.append(" COUNT (CASE WHEN approved in " + APPROVE_APPEAL + " THEN HCODE END) pid_appeal_count,");
                        sql.append(" COUNT (CASE WHEN approved in " + APPROVE_NORMAL + " OR approved in " + APPROVE_APPEAL + " THEN HCODE END)");
                        sql.append(" pid_normal_appeal_total,");
                        sql.append(" SUM (NVL (chrg_hc, 0)) sum_chrg_hc,");
                        sql.append(" SUM (NVL (chrg_197, 0)) sum_chrg_197,");
                        sql.append(" SUM (NVL (chrg_drug, 0)) sum_chrg_drug,");
                        sql.append(" SUM (NVL (chrg_other, 0)) sum_chrg_other,");
                        sql.append(" SUM (NVL (chrg_total, 0)) sum_chrg_total,");
                        sql.append(" SUM (NVL (paid_hc, 0)) sum_paid_hc,");
                        sql.append(" SUM (NVL (paid_197, 0)) sum_paid_197,");
                        sql.append(" SUM (NVL (paid_drug, 0)) sum_paid_drug,");
                        sql.append(" SUM (NVL (paid_other, 0)) sum_paid_other,");
                        sql.append(" SUM (NVL (paid_total, 0)) sum_paid_total,");
                        sql.append(" SUM (NVL (paid_total, 0)) sum_paid_final");
                        sql.append(" FROM RPT_OPBKK_OPREFER refer");
                        sql.append(" WHERE 1 = 1");
                        if (!report.getYearMonth().equals("") && !report.getNo().equals("")) {
                            sql.append(" AND STMP = '").append(report.getYearMonth()).append("-").append(report.getNo()).append("'");
                        }
                        if (!report.getServiceCode().equals("")) {
                            sql.append(" AND HCODE = '").append(report.getServiceCode()).append("'");
                        }
                        if (!report.getBudget_year().equals("")) {
                            int budgetYear = Integer.parseInt(report.getBudget_year());
                            int minBudgetYear = (budgetYear - 1);
                            int maxBudgetYear = budgetYear;
                            sql.append(" and trunc(dateopd) between to_date('0110").append(minBudgetYear).append("','ddmmyyyy')");
                            sql.append(" and to_date('3009").append(maxBudgetYear).append("','ddmmyyyy')");
                        }
                        sql.append(" GROUP BY hmain,hmainname");
                    }

                }
            }

            System.out.println("sql ::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstmt = conn.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();

            return rs;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection openConnection() {
        try {
            conn = getConOracle();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return conn;
        }
    }
}