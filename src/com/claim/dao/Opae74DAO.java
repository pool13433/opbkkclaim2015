package com.claim.dao;

import com.claim.connection.ConnectionDB;
import com.claim.object.HospitalService;
import com.claim.object.ObjRptOpae74;
import com.claim.object.OppReport;
import com.claim.object.ResultBean;
import com.claim.support.Console;
import com.claim.constants.ConstantMessage;
import com.claim.support.StringOpUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Opae74DAO {

    static final String SQL_CONDITION = " AND PAID_TOTAL > 0 ";
    Connection conn = null;

    private Connection getConOracle() {
        Connection vConn = null;
        ConnectionDB connec = new ConnectionDB("True", "", "", "", "", "", "", "1");
        vConn = connec.getConnectionInf();
        return vConn;
    }

    public void closeConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection openConnection() {
        this.conn = getConOracle();
        return conn;
    }

    public String getOpae74StmpDescDistinct(String obYM, String obN) {
        String titleTimeStmpDesc = "";
        StringBuilder sql = null;
        ResultSet rs = null;
        PreparedStatement pstm = null;
        try {
            this.openConnection();

            sql = new StringBuilder("SELECT");
            sql.append(" distinct(select rh.STMP_DESC from rpt_header rh where rh.stmp=r.stmp and rh.OPTYPE='AE') STMP_DESC");
            sql.append("  from RPT_OPBKK_OPAE74 r");

            sql.append(" WHERE r.STMP = '").append(obYM).append("-").append(obN).append("'");

            // sql.append(" GROUP by r.STMP_DESC");
            System.out.println("sql ::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstm = conn.prepareStatement(sql.toString());
            rs = pstm.executeQuery();

            while (rs.next()) {
                titleTimeStmpDesc = rs.getString("STMP_DESC");
            }

            return titleTimeStmpDesc;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
            return titleTimeStmpDesc;
        }
    }

    public List<HospitalService> getHospitalService(OppReport report) {
        StringBuilder sql = null;
        List<HospitalService> listHS = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            this.openConnection();

            sql = new StringBuilder("SELECT");
            sql.append(" DISTINCT(HCODE) AS HCODE,HCODENAME AS HNAME");
            sql.append(" from rpt_opbkk_opae74");
            sql.append(" WHERE 1=1");

            if (report.sizeListServiceCode() > 0) {
                sql.append("  AND HCODE in (");
                for (HospitalService hospitalService : report.getListServiceCode()) {
                    sql.append("'").append(hospitalService.getHosCode()).append("',");
                }
                sql.append(" '')");
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
            sql.append(SQL_CONDITION);

            System.out.println("sql ::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstmt = conn.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();

            listHS = new ArrayList<HospitalService>();
            while (rs.next()) {
                HospitalService service = new HospitalService();
                service.setHosCode(rs.getString("HCODE"));
                service.setHosCodeName(rs.getString("HNAME"));
                listHS.add(service);
            }
            return listHS;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
            return listHS;
        }
    }

    public List<HospitalService> getDateopdGroupService(OppReport report) {
        StringBuilder sql = null;
        List<HospitalService> listHS = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            this.openConnection();
            sql = new StringBuilder();
            sql.append(" ");
            sql.append(" SELECT to_char(dateopd,'yyyymm') dateopd_ym");
            sql.append(" ,TO_CHAR(dateopd, 'MONTH yyyy', 'NLS_CALENDAR=''THAI BUDDHA'' NLS_DATE_LANGUAGE=THAI') dateopd_th");
            sql.append(" from rpt_opbkk_opae74 WHERE 1=1 ");

            if (!report.getYearMonth().equals("") && !report.getNo().equals("")) {
                sql.append(" AND STMP = '").append(report.getYearMonth()).append("-").append(report.getNo()).append("'");
            }
            // Case Opae74 Detail 
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
            sql.append(SQL_CONDITION);
            sql.append(" group by to_char(dateopd,'yyyymm')");
            sql.append(" ,TO_CHAR(dateopd, 'MONTH yyyy', 'NLS_CALENDAR=''THAI BUDDHA'' NLS_DATE_LANGUAGE=THAI')");
            sql.append(" order by dateopd_ym");

            System.out.println("sql ::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstmt = conn.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();

            listHS = new ArrayList<HospitalService>();
            while (rs.next()) {
                HospitalService service = new HospitalService();
                service.setDateopd(rs.getString("dateopd_ym"));
                service.setDateopd_th(rs.getString("dateopd_th"));
                listHS.add(service);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                this.closeConnection();
            }
        }
        return listHS;
    }

    public ResultSet genReportOpae74(OppReport report) {
        ResultSet rs = null;
        StringBuilder sql = null;
        PreparedStatement pstmt = null;
        try {
            this.openConnection();

            if (report.getReportType() == 0) { // 0 detail

                sql = new StringBuilder("SELECT ");
                sql.append(" hcode,hcodename");
                sql.append(" ,pid,pname,hn,hmain||': '||hmainname as hmain,dateopd,to_char(dateopd,'dd monyyyy','NLS_CALENDAR=''Thai Buddha''') as dateopdTh");
                sql.append(" ,(case when subinscl='74' then 'OP พิการ' else 'OPAE' end) optype");
                sql.append(" ,chrg_ophc,chrg_stditem,chrg_197,chrg_car,chrg_rehab_inst,chrg_other,chrg_total,chrg_total chrg_total_final");
                sql.append(" ,payrate");
                sql.append(" ,paid_ophc,paid_stditem,paid_197,paid_car,paid_rehab_inst,paid_other,paid_total,paid_total paid_total_final");
                sql.append(" ,remark");
                sql.append(" ,txid");  // modifide by 2014-08-04

                /*
                 modified 01-07-2015 by poolsawat
                 */
                sql.append(" ,(select INVOICE_NO from opbkk_service s where s.txid = opae.txid) as INVOICE_NO");
                /*
                 modified 01-07-2015 by poolsawat
                 */

                sql.append(" from rpt_opbkk_opae74 opae");
                sql.append(" WHERE 1=1");

                if (!report.getServiceCode().equals("")) {
                    sql.append(" AND HCODE = '").append(report.getServiceCode()).append("'");
                }

                // แยก สรุปออกตามเดือน  แก้ไขวันที่ 15/08/2557
                if (!StringOpUtil.removeNull(report.getDateopd()).equals("")) {
                    sql.append(" AND  to_char(dateopd,'yyyymm')  = '").append(report.getDateopd()).append("'");
                }

                if (!report.getYearMonth().equals("") && !report.getNo().equals("")) {
                    sql.append(" AND STMP = '").append(report.getYearMonth()).append("-").append(report.getNo()).append("'");
                    //sql.append(" AND STMP in ( '201408-1' ,'201407-1') "); // เอาไว้ทดสอบ ข้อมูล ที่เกิน 65500 แถว
                }
                if (!report.getBudget_year().equals("")) {
                    int budgetYear = Integer.parseInt(report.getBudget_year());
                    int minBudgetYear = (budgetYear - 1);
                    int maxBudgetYear = budgetYear;
                    sql.append(" and trunc(dateopd) between to_date('0110").append(minBudgetYear).append("','ddmmyyyy')");
                    sql.append(" and to_date('3009").append(maxBudgetYear).append("','ddmmyyyy')");

                }
                sql.append(SQL_CONDITION);

                if (report.getSql_orderBy().equals("")) {
                    sql.append(" order by pid, dateopd ");
                } else {
                    sql.append(" ").append(report.getSql_orderBy());
                }

                // เรียก ใช้งาน method listOpae74ColumnsMerge,listOpae74ColumnsCase
            } else if (report.getReportType() == 1) { // 1 sumary
                if (report.getAttribute() == 'R') { // rehap
                    sql = new StringBuilder("SELECT ");
                    sql.append("  row_number() OVER (ORDER BY 1,2) no");
                    //sql.append(" ,hcode||': '||hcodename as hcode");
                    sql.append(" ,hcode,hcodename");
                    sql.append(" ,count(case when subinscl<>'74' then 1 else null end) n_AE");
                    sql.append(" ,count(case when subinscl='74' then 1 else null end) n_74");
                    sql.append(" ,count(*) n_total");

                    //--charg
                    sql.append(" ,sum(chrg_total-case when subinscl='74' then chrg_rehab_inst else 0 end) chrg_totalAE74_nonrehap"); // -- OPAE74 ไม่รวม กรณีฟื้นฟูและอุปกรณ์เครื่องช่วยคนพิการ
                    sql.append(" ,sum(case when subinscl='74' and (select s.PURCHASEPROVINCE from opbkk_service s where r.txid=s.txid)='1000'  then chrg_rehab_inst else 0 end) chrg_rehab_inst_74_bkk "); //-- กรณีฟื้นฟูและอุปกรณ์เครื่องช่วยคนพิการ(บัตรกทม)
                    sql.append(" ,sum(case when subinscl='74' and (select s.PURCHASEPROVINCE from opbkk_service s where r.txid=s.txid)<>'1000'  then chrg_rehab_inst else 0 end) chrg_rehab_inst_74_nonbkk "); // -- กรณีฟื้นฟูและอุปกรณ์เครื่องช่วยคนพิการ(บัตรต่างจังหวัด)
                    sql.append(" ,sum(chrg_total)chrg_totalAE74");

                    //--paid
                    sql.append(" ,sum(paid_total-case when subinscl='74' then paid_rehab_inst else 0 end)paid_totalAE74_nonrehap "); //-- OPAE74 ไม่รวม กรณีฟื้นฟูและอุปกรณ์เครื่องช่วยคนพิการ
                    sql.append(" ,sum(case when subinscl='74' and (select s.PURCHASEPROVINCE from opbkk_service s where r.txid=s.txid)='1000'  then paid_rehab_inst else 0 end) paid_rehab_inst_74_bkk "); //-- กรณีฟื้นฟูและอุปกรณ์เครื่องช่วยคนพิการ(บัตรกทม)
                    sql.append(" ,sum(case when subinscl='74' and (select s.PURCHASEPROVINCE from opbkk_service s where r.txid=s.txid)<>'1000'  then paid_rehab_inst else 0 end) paid_rehab_inst_74_nonbkk "); //-- กรณีฟื้นฟูและอุปกรณ์เครื่องช่วยคนพิการ(บัตรต่างจังหวัด)
                    sql.append(" ,sum(paid_total)paid_totalAE74");

                    sql.append(" ,sum(chrg_total+paid_total)");
                    sql.append(" from rpt_opbkk_opae74 r");
                    sql.append(" where 1=1");
                    // แยก สรุปออกตามเดือน  แก้ไขวันที่ 15/08/2557
                    if (report.getDateopd() != "" && report.getDateopd() != null) {
                        sql.append(" AND  to_char(dateopd,'yyyymm') = '").append(report.getDateopd()).append("'");
                    }
                    sql.append(" AND STMP = '").append(report.getYearMonth()).append("-").append(report.getNo()).append("'");
                    if (!report.getBudget_year().equals("")) {
                        int budgetYear = Integer.parseInt(report.getBudget_year());
                        int minBudgetYear = (budgetYear - 1);
                        int maxBudgetYear = budgetYear;
                        sql.append(" and trunc(dateopd) between to_date('0110").append(minBudgetYear).append("','ddmmyyyy')");
                        sql.append(" and to_date('3009").append(maxBudgetYear).append("','ddmmyyyy')");

                    }
                    sql.append(SQL_CONDITION);
                    //sql.append(" group by rollup(hcode||': '||hcodename)");
                    //sql.append(" and hcode=11470");
                    //sql.append(" and subinscl='74'");
                    //sql.append(" and R.CHRG_REHAB_INST=0");
                    //sql.append(" and to_char(dateopd,'yyyymmdd') between 20131001 and 20131031");
                    sql.append(" group by ");
                    //sql.append(" hcode||': '||hcodename");
                    sql.append(" hcode,hcodename");
                    sql.append(" order by 1,2");
                    System.out.println(" [ summary rehap ]");
                } else {
                    sql = new StringBuilder(" SELECT");
                    sql.append(" row_number() OVER (ORDER BY 1,2) no,");
                    sql.append(" hcode||': '||hcodename as hcode");
                    sql.append(" ,(select mp.PROVINCE_NAME from MASTER_PROVINCE mp where mp.PROVINCE_ID=PROVINCE)PROVINCE_NAME");
                    sql.append(" ,count(case when subinscl<>'74' then 1 else null end) n_AE,");
                    sql.append(" count(case when subinscl='74' then 1 else null end) n_74,");
                    sql.append(" count(*) n_total,");
                    sql.append(" sum(case when subinscl<>'74' then chrg_ophc else 0 end) chrg_ophc,");
                    sql.append(" sum(case when subinscl<>'74' then chrg_stditem else 0 end) chrg_stditem,");
                    sql.append(" sum(case when subinscl<>'74' then chrg_197 else 0 end) chrg_197,");
                    sql.append(" sum(case when subinscl<>'74' then chrg_car else 0 end) chrg_car,");
                    sql.append(" sum(case when subinscl<>'74' then chrg_other else 0 end) chrg_other,");
                    sql.append(" sum(case when subinscl<>'74' then chrg_total else 0 end) chrg_total,");
                    sql.append(" sum(case when subinscl='74' then chrg_ophc else 0 end) chrg_ophc_74,");
                    sql.append(" sum(case when subinscl='74' then chrg_stditem else 0 end) chrg_stditem_74,");
                    sql.append(" sum(case when subinscl='74' then chrg_197 else 0 end) chrg_197_74,");
                    sql.append(" sum(case when subinscl='74' then chrg_car else 0 end) chrg_car_74,");
                    sql.append(" sum(case when subinscl='74' then chrg_rehab_inst else 0 end) chrg_rehab_inst_74,");
                    sql.append(" sum(case when subinscl='74' then chrg_other else 0 end) chrg_other_74,");
                    sql.append(" sum(case when subinscl='74' then chrg_total else 0 end) chrg_total_74,");
                    sql.append(" sum(chrg_total) chrg_totalAE74,");
                    sql.append(" sum(case when subinscl<>'74' then paid_ophc else 0 end) paid_ophc,");
                    sql.append(" sum(case when subinscl<>'74' then paid_stditem else 0 end) paid_stditem,");
                    sql.append(" sum(case when subinscl<>'74' then paid_197 else 0 end) paid_197,");
                    sql.append(" sum(case when subinscl<>'74' then paid_car else 0 end) paid_car,");
                    sql.append(" sum(case when subinscl<>'74' then paid_other else 0 end) paid_other,");
                    sql.append(" sum(case when subinscl<>'74' then paid_total else 0 end) paid_total,");
                    sql.append(" sum(case when subinscl='74' then paid_ophc else 0 end) paid_ophc_74,");
                    sql.append(" sum(case when subinscl='74' then paid_stditem else 0 end) paid_stditem_74,");
                    sql.append(" sum(case when subinscl='74' then paid_197 else 0 end) paid_197_74,");
                    sql.append(" sum(case when subinscl='74' then paid_car else 0 end) paid_car_74,");
                    sql.append(" sum(case when subinscl='74' then paid_rehab_inst else 0 end) paid_rehab_inst_74,");
                    sql.append(" sum(case when subinscl='74' then paid_other else 0 end) paid_other_74,");
                    sql.append(" sum(case when subinscl='74' then paid_total else 0 end) paid_total_74,");
                    sql.append(" sum(paid_total) paid_totalAE74");
                    sql.append(" from rpt_opbkk_opae74 r");
                    sql.append(" WHERE 1=1");
//                sql.append(" AND chrg_rehab_inst = 0");
//                sql.append(" AND subinscl = 74");

                    // แยก สรุปออกตามเดือน  แก้ไขวันที่ 15/08/2557
                    if (report.getDateopd() != "" && report.getDateopd() != null) {
                        sql.append(" AND  to_char(dateopd,'yyyymm') = '").append(report.getDateopd()).append("'");
                    }

                    sql.append(" AND STMP = '").append(report.getYearMonth()).append("-").append(report.getNo()).append("'");
                    if (!report.getBudget_year().equals("")) {
                        int budgetYear = Integer.parseInt(report.getBudget_year());
                        int minBudgetYear = (budgetYear - 1);
                        int maxBudgetYear = budgetYear;
                        sql.append(" and trunc(dateopd) between to_date('0110").append(minBudgetYear).append("','ddmmyyyy')");
                        sql.append(" and to_date('3009").append(maxBudgetYear).append("','ddmmyyyy')");
                    }
                    sql.append(SQL_CONDITION);
                    sql.append(" group by hcode||': '||hcodename,PROVINCE");
                    sql.append(" order by 1,2");
                }
            } else if (report.getReportType() == 2) { // 2 ItemDetail
                sql = new StringBuilder(" select ");
                sql.append(" rpt.pid,rpt.txid,pname,rpt.hn,rpt.hmain||': '||rpt.hmainname as hmain,rpt.hcode||': '||rpt.hcodename hcode,to_char(dateopd,'dd monyyyy','NLS_CALENDAR=''Thai Buddha''') as dateopd");
                sql.append(" ,(case when subinscl='74' then 'OP พิการ' else 'OPAE' end) optype");
                sql.append(" ,cd.item_code,cd.item_src");
                sql.append(" ,chrg_ophc,chrg_stditem,chrg_197,chrg_car,chrg_rehab_inst,chrg_other,chrg_total,chrg_total chrg_total_final");
                sql.append(" ,payrate");
                sql.append(" ,paid_ophc,paid_stditem,paid_197,paid_car,paid_rehab_inst,paid_other,paid_total,paid_total paid_total_final");
                sql.append(" ,STMP,(select rh.STMP_DESC from rpt_header rh where RH.STMP=rpt.stmp and RH.OPTYPE='AE')STMP_DESC ");
                sql.append(" ,cd.clinic,cd.item_type,cd.qty,cd.price_total,cd.price_ext,cd.unitprice,cd.stdprice,cd.reimburse,cd.is197");
                sql.append(" ,rpt.txid");  // modifide by 2014-08-04
                 /*
                 modified 01-07-2015 by poolsawat
                 */
                sql.append(" ,(select INVOICE_NO from opbkk_service s where s.txid = rpt.txid) as INVOICE_NO");
                /*
                 modified 01-07-2015 by poolsawat
                 */
                
                sql.append(" from RPT_OPBKK_OPAE74 rpt,OPBKK_CHADETAIL cd");
                sql.append(" where 1=1");
                sql.append(" and rpt.txid=cd.txid");
                sql.append(" and stmp= '").append(report.getYearMonth()).append("-").append(report.getNo()).append("'");  // /* งวด */                
                sql.append(" and cd.is197=2 and cd.ITEM_TYPE in ('02','14')");
                sql.append(" and cd.STATUS_ID=1");
                sql.append(" order by rpt.stmp,rpt.pid,rpt.txid ");
            } else if (report.getReportType() == 3) { // 3 Car detail
                sql = new StringBuilder(" select ");
                sql.append(" ");
                sql.append(" rpt.HCODE,rpt.HCODENAME,rpt.PID,rpt.HN,rpt.PNAME,rpt.DATEOPD");
                sql.append(" ,to_char(dateopd,'dd monyyyy','NLS_CALENDAR=''Thai Buddha'' NLS_DATE_LANGUAGE=THAI') dateopd_thai_buddha");
                sql.append(" ,rpt.AGE_YY,rpt.PDXCODE");
                sql.append(" ,rpt.CHRG_OPHC,rpt.CHRG_197,rpt.CHRG_STDITEM,rpt.CHRG_CAR,rpt.CHRG_REHAB_INST,rpt.PAYRATE,rpt.CHRG_OTHER,rpt.CHRG_TOTAL");
                sql.append(" ,rpt.PAID_OPHC,rpt.PAID_197,rpt.PAID_STDITEM,rpt.PAID_CAR,rpt.PAID_REHAB_INST,rpt.PAID_OTHER,rpt.PAID_TOTAL");
                sql.append(" ,rpt.REMARK,rpt.STMP,rpt.RPT_DATE,rpt.SUBINSCL,rpt.PROVINCE");
                sql.append(" ,acc.RFCAR_FROMHCODE RFCAR_FROM,acc.RFCAR_TOHCODE RFCAR_TO,acc.RFCAR_DISTANCE,acc.RFCAR_CHARGE,acc.RFCAR_REIMBURSE");
                sql.append(" ,rpt.txid");  // modifide by 2014-08-04
                
                 /*
                 modified 01-07-2015 by poolsawat
                 */
                sql.append(" ,(select INVOICE_NO from opbkk_service s where s.txid = rpt.txid) as INVOICE_NO");
                /*
                 modified 01-07-2015 by poolsawat
                 */
                
                
                sql.append(" from RPT_OPBKK_OPAE74 rpt,OPBKK_ACCIDENT acc");
                sql.append(" where 1=1");
                sql.append(" and rpt.txid=acc.txid");
                sql.append(" and stmp = '").append(report.getYearMonth()).append("-").append(report.getNo()).append("'"); /* งวด */

                sql.append(" and rpt.CHRG_CAR>0");
                sql.append(" and acc.STATUS_ID=1");
                sql.append(" order by rpt.hcode,pid");
            }

            System.out.println("sql ::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstmt = conn.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public ObjRptOpae74 getReportOpae74Detail_Totalsum(OppReport report) {
        ObjRptOpae74 objData = null;
        ResultSet rs = null;
        StringBuilder sql = null;
        PreparedStatement pstmt = null;
        try {
            this.openConnection();

            sql = new StringBuilder();
            sql.append(" SELECT");
            sql.append(" sum(chrg_ophc) chrg_ophc,");
            sql.append(" sum(chrg_stditem) chrg_stditem,");
            sql.append(" sum(chrg_197) chrg_197,");
            sql.append(" sum(chrg_car) chrg_car,");
            sql.append(" sum(chrg_rehab_inst) chrg_rehab_inst,");
            sql.append(" sum(chrg_other) chrg_other,");
            sql.append(" sum(chrg_total) chrg_total,");
            sql.append(" sum(payrate) payrate,");
            sql.append(" sum(paid_ophc) paid_ophc,");
            sql.append(" sum(paid_stditem) paid_stditem,");
            sql.append(" sum(paid_197) paid_197,");
            sql.append(" sum(paid_car) paid_car,");
            sql.append(" sum(paid_rehab_inst) paid_rehab_inst,");
            sql.append(" sum(paid_other) paid_other,");
            sql.append(" sum(paid_total) paid_total");
            sql.append(" FROM rpt_opbkk_opae74");
            sql.append(" WHERE 1 = 1 ");
            sql.append(" and stmp = '").append(report.getYearMonth()).append("-").append(report.getNo()).append("'"); /* งวด */

            if (!report.getBudget_year().equals("")) {
                int budgetYear = Integer.parseInt(report.getBudget_year());
                int minBudgetYear = (budgetYear - 1);
                int maxBudgetYear = budgetYear;
                sql.append(" and trunc(dateopd) between to_date('0110").append(minBudgetYear).append("','ddmmyyyy')");
                sql.append(" and to_date('3009").append(maxBudgetYear).append("','ddmmyyyy')");
            }
            sql.append(SQL_CONDITION);
            sql.append(" ORDER BY HCODE, HMAIN, PID ASC");

            System.out.println("sql ::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstmt = conn.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                objData = new ObjRptOpae74();
                objData.setChrg_197(rs.getDouble("chrg_197"));
                objData.setChrg_car(rs.getDouble("chrg_car"));
                objData.setChrg_ophc(rs.getDouble("chrg_ophc"));
                objData.setChrg_other(rs.getDouble("chrg_other"));
                objData.setChrg_rehab_inst(rs.getDouble("chrg_rehab_inst"));
                objData.setChrg_stditem(rs.getDouble("chrg_stditem"));
                objData.setChrg_total(rs.getDouble("chrg_total"));
                objData.setChrg_total_final(rs.getDouble("chrg_total"));
                objData.setPaid_197(rs.getDouble("paid_197"));
                objData.setPaid_car(rs.getDouble("paid_car"));
                objData.setPaid_ophc(rs.getDouble("paid_ophc"));
                objData.setPaid_other(rs.getDouble("paid_other"));
                objData.setPaid_rehab_inst(rs.getDouble("paid_rehab_inst"));
                objData.setPaid_stditem(rs.getDouble("paid_stditem"));
                objData.setPaid_total(rs.getDouble("paid_total"));
                objData.setPaid_total_final(rs.getDouble("paid_total"));
                objData.setPayrate(rs.getDouble("payrate"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return objData;
    }

    public List<ObjRptOpae74> genReportOpae74_ListObject(OppReport oppReport) {
        ResultSet rs = null;

        List<ObjRptOpae74> list_opae74 = null;
        ObjRptOpae74 objOpae74 = null;

        try {

            rs = genReportOpae74(oppReport);

            list_opae74 = new ArrayList<ObjRptOpae74>();
            while (rs.next()) {
                objOpae74 = new ObjRptOpae74();
                objOpae74.setHcode(rs.getString("hcode"));
                objOpae74.setHcodename(rs.getString("hcodename"));
                objOpae74.setP_name(rs.getString("pname"));
                objOpae74.setChrg_197(rs.getDouble("chrg_197"));
                objOpae74.setChrg_car(rs.getDouble("chrg_car"));
                objOpae74.setChrg_ophc(rs.getDouble("chrg_ophc"));
                objOpae74.setChrg_other(rs.getDouble("chrg_other"));
                objOpae74.setChrg_rehab_inst(rs.getDouble("chrg_rehab_inst"));
                objOpae74.setChrg_stditem(rs.getDouble("chrg_stditem"));
                objOpae74.setChrg_total(rs.getDouble("chrg_total"));
                objOpae74.setChrg_total_final(rs.getDouble("chrg_total_final"));

                objOpae74.setDateopd(rs.getString("dateopd"));
                objOpae74.setDateopd_th(rs.getString("dateopdTh"));

                objOpae74.setHmain(rs.getString("hmain"));
                objOpae74.setHn(rs.getString("hn"));

                objOpae74.setOptype(rs.getString("optype"));

                objOpae74.setPaid_197(rs.getDouble("paid_197"));
                objOpae74.setPaid_car(rs.getDouble("paid_car"));
                objOpae74.setPaid_ophc(rs.getDouble("paid_ophc"));
                objOpae74.setPaid_other(rs.getDouble("paid_other"));
                objOpae74.setPaid_rehab_inst(rs.getDouble("paid_rehab_inst"));
                objOpae74.setPaid_stditem(rs.getDouble("paid_stditem"));
                objOpae74.setPaid_total(rs.getDouble("paid_total"));
                objOpae74.setPaid_total_final(rs.getDouble("paid_total_final"));

                objOpae74.setPayrate(rs.getDouble("payrate"));
                objOpae74.setPid(rs.getString("pid"));
                objOpae74.setTxid(rs.getString("txid"));
                objOpae74.setInvoice_no(rs.getString("invoice_no"));
                list_opae74.add(objOpae74);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Opae74DAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return list_opae74;
    }

    // ไม่ได้ใช้งาน
    public List<ResultBean> listOpae74ColumnsMerge(OppReport report) {
        List<ResultBean> listOpae74Merge = null;
        StringBuilder sql = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {

            this.openConnection();

            sql = new StringBuilder("SELECT");
            sql.append(" DISTINCT(pid) AS pid");
            sql.append(" from RPT_OPBKK_OPAE74");
            sql.append(" WHERE 1=1");
            if (!report.getServiceCode().equals("")) {
                sql.append(" AND HCODE = '" + report.getServiceCode() + "'");
            }
            if (report.getYearMonth() != null && report.getNo() != null) {
                sql.append(" AND STMP = '" + report.getYearMonth() + "-" + report.getNo() + "'");
            }
            sql.append(" ORDER BY pid");
            System.out.println("MergeColumns sql.toString()==>>" + sql.toString());
            pstmt = conn.prepareStatement(sql.toString());
            rs = pstmt.executeQuery();

            listOpae74Merge = new ArrayList<ResultBean>();

            while (rs.next()) {
                ResultBean bean = new ResultBean();
                bean.setObject1(rs.getString("pid"));
                listOpae74Merge.add(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
            return listOpae74Merge;
        }
    }
}

    
