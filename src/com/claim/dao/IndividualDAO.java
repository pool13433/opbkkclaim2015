package com.claim.dao;

import com.claim.connection.ConnectionDB;
import com.claim.object.HospitalService;
import com.claim.object.ObjRptIndividual;
import com.claim.object.ObjRptIndividualSum;
import com.claim.object.OppReport;
import com.claim.object.ResultBean;
import com.claim.support.Console;
import com.claim.constants.ConstantMessage;
import com.claim.support.DateUtil;
import com.claim.support.StringOpUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IndividualDAO {

    public Connection conn = null;
    public ResultSet rs = null;
    public StringBuilder sql = null;
    public PreparedStatement pstm = null;
    static final int CHECK_DUPISNULL = 6; // > 6 คือ 7 ขึ้นไป
    static final String STMP_57_FIX = "201400-0";
    public static final String APPROVED = "99";   //op indv  1 visit ใน 1 วัน// จ่าย 
    public static final String APPROVED_INDV = "88"; //  op indv มากกว่า 1 visit ใน 1 วัน//ไม่จ่าย
    /*
    *
    sql.append(" ,(case when approved in (6,5,8,91,92) then 'OPRefer/AEในบัญชีเครือข่าย' ");
    sql.append(" when approved in (14,17,93,94) then 'OPบัตรรพ.เบิก197รายการ'");
    sql.append(" when approved in (21,23,96) then 'AEนอกบช.เครือข่าย/OPผู้พิการ'");
    sql.append(" when approved in (26,29,97,98) then'ClearingHouseศบส.'");
    sql.append(" when approved in (31,99) then 'OP Individual'");
    */
    final static String APPROVED_IN_REFER = "6,5,8,81,91,92";  //'OPRefer/AEในบัญชีเครือข่าย'
    final static String APPROVED_IN_TYPE5 = "14,17,83,93,94";  //OPบัตรรพ.เบิก197รายการ
    final static String APPROVED_IN_OPAE = "21,23,86,96";  //AEนอกบช.เครือข่าย/OPผู้พิการ
    final static String APPROVED_IN_CLH = "26,29,62,66,80,87,90,97";  //ClearingHouseศบส.
    final static String APPROVED_IN_INDV = "31,89,99";  //OP Individual
    final static String APPROVED_IN_NONI = "34,85,95";  //OP Individual

    public IndividualDAO() {
        //conn = getConOracle();
    }

    public void setConnection(Connection connection) {
        this.conn = connection;
    }

    public Connection getConOracle() {
        Connection vConn = null;
        ConnectionDB connec = new ConnectionDB("True", "", "", "", "", "", "", "1");
        vConn = connec.getConnectionInf();
        return vConn;
    }

    public List<HospitalService> getHospitalService(OppReport report) {
        List<HospitalService> listHS = null;
        try {

            sql = new StringBuilder("SELECT");
            sql.append(" DISTINCT(HCODE) AS HCODE,HCODENAME AS HNAME");
            sql.append(" FROM RPT_OPBKK_INDV WHERE 1=1");
            // > 6 <= 8
            if (STMP_57_FIX.equals(report.getYearMonth() + "-" + report.getNo())) {
                sql.append(" AND approved = '").append(APPROVED).append("' ");
            } else {
                if (new DateUtil().checkDateOpdCurrentBudgetYear(report.getBudget_date(), CHECK_DUPISNULL)) {
                    if (Integer.parseInt(report.getMonth()) > 6 && Integer.parseInt(report.getMonth()) <= 8) {
                        sql.append(" AND dup is null ");
                    } else {
                        sql.append(" AND approved = '").append(APPROVED).append("' ");
                    }
                } else {
                    /*
                     * 19-08-2015 
                     */
                    System.out.println(" 201400-1");
                    sql.append(" AND approved <> '").append(APPROVED_INDV).append("' ");
                    /*
                     * 19-08-2015 
                     */
                }
            }
            if (report.sizeListServiceCode() > 0) {
                sql.append("  AND HCODE in (");
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
            /*
             Modified by poolsawat 02-06-2015
             */
            if (!report.getIndv_late().equals("")) {
                sql.append(" AND CASE INDV_LATE");
                sql.append(" WHEN 'ทันเวลา' THEN '0'");
                sql.append(" WHEN 'ล่าช้า' THEN '1'");
                sql.append(" END = '").append(report.getIndv_late()).append("' ");
            }
            sql.append(" ORDER BY HCODE");

            System.out.println("sql ::==" + sql.toString());

            pstm = conn.prepareStatement(sql.toString());
            rs = pstm.executeQuery();

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
            try {
//                if(rs!=null){
//                    rs.close();                
//                }
                if (pstm != null) {
                    pstm.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listHS;
    }

    public List<ObjRptIndividual> getReportIndiviList(OppReport report) throws SQLException {
        List<ObjRptIndividual> listIndivi = null;
        try {

            sql = new StringBuilder(" select");
            sql.append(" rownum no,hcode,hcodename");
            sql.append(" ,PID,PNAME,HN,");
            sql.append(" DATEOPD,SENDDATE,INDV_LATE");
            sql.append(" ,to_char(DATEOPD,'dd monyyyy','NLS_CALENDAR=''Thai Buddha'' NLS_DATE_LANGUAGE=THAI') dateopd_thai_buddha");
            sql.append(" ,to_char(SENDDATE,'dd monyyyy','NLS_CALENDAR=''Thai Buddha'' NLS_DATE_LANGUAGE=THAI') SENDDATE_thai_buddha");
            /*sql.append(" ,(case when approved in (91,92) then 'OPRefer/AEในบัญชีเครือข่าย' ");
             sql.append(" when approved in (93,94) then 'OPบัตรรพ.เบิก197รายการ'");
             sql.append(" when approved in (96) then 'OPบัตรรพ.เบิก197รายการ'");
             sql.append(" when approved in (97,98) then'ClearingHouseศบส.'");
             sql.append(" when approved in (99) then'OP Individual'");
             sql.append(" end");*/
            /* 
            * modified by poolsawat apin 20-08-2015
            */
            /*sql.append(" ,(case when approved in (6,5,8,91,92) then 'OPRefer/AEในบัญชีเครือข่าย' ");
            sql.append(" when approved in (14,17,93,94) then 'OPบัตรรพ.เบิก197รายการ'");
            sql.append(" when approved in (21,23,96) then 'AEนอกบช.เครือข่าย/OPผู้พิการ'");
            sql.append(" when approved in (26,29,97,98) then'ClearingHouseศบส.'");
            sql.append(" when approved in (31,99) then 'OP Individual'");
            sql.append(" end");*/
            
            sql.append(" ,(case when approved in ("+APPROVED_IN_REFER+") then 'OPRefer/AEในบัญชีเครือข่าย' ");
            sql.append(" when approved in ("+APPROVED_IN_TYPE5+") then 'OPบัตรรพ.เบิก197รายการ'");
            sql.append(" when approved in ("+APPROVED_IN_OPAE+") then 'AEนอกบช.เครือข่าย/OPผู้พิการ'");
            sql.append(" when approved in ("+APPROVED_IN_CLH+") then'ClearingHouseศบส.'");
            sql.append(" when approved in ("+APPROVED_IN_INDV+") then 'OP Individual'");
            sql.append(" when approved in ("+APPROVED_IN_NONI+") then 'OP Noni กึ่งผู้ป่วยนอก/ผู้ป่วยใน' ");
            sql.append(" end");
             /* 
            * modified by poolsawat apin 20-08-2015
            */
            sql.append(" ) optypename,");   // optypename
            sql.append(" nvl(MAININSCL,'สิทธิ์ว่าง') MAININSCL,");
            sql.append(" TOTALREIMBURSE");
            sql.append(" ,txid");

            /*
             modified 01-07-2015 by poolsawat
             */
            sql.append(" ,(select INVOICE_NO from opbkk_service s where s.txid = r.txid) as INVOICE_NO");
            /*
             modified 01-07-2015 by poolsawat
             */

            sql.append(" from RPT_OPBKK_INDV r");
            sql.append(" where 1=1");

            // > 6 <= 8
            if (STMP_57_FIX.equals(report.getYearMonth() + "-" + report.getNo())) {
                sql.append(" AND approved = '").append(APPROVED).append("' ");
            } else {
                if (new DateUtil().checkDateOpdCurrentBudgetYear(report.getBudget_date(), CHECK_DUPISNULL)) {
                    if (Integer.parseInt(report.getMonth()) > 6 && Integer.parseInt(report.getMonth()) <= 8) {
                        sql.append(" AND dup is null ");
                    } else {
                        sql.append(" AND approved = '").append(APPROVED).append("' ");
                    }
                } else {
                    /*
                     * 19-08-2015 
                     */
                    System.out.println(" 201400-1");
                    sql.append(" AND approved <> '").append(APPROVED_INDV).append("' ");
                    /*
                     * 19-08-2015 
                     */
                }
            }
            // sql.append(" --and INDV_LATE ='ล่าช้า'");
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
            /*
             Modified by poolsawat 02-06-2015
             */
            if (!report.getIndv_late().equals("")) {
                sql.append(" AND CASE INDV_LATE");
                sql.append(" WHEN 'ทันเวลา' THEN '0'");
                sql.append(" WHEN 'ล่าช้า' THEN '1'");
                sql.append(" END = '").append(report.getIndv_late()).append("' ");
            }

            //sql.append(" group by rollup(PID,PNAME,HN,DATEOPD,SENDDATE,INDV_LATE,MAININSCL,approved)");
            //////////sql.append(" -- total row");
            //sql.append(" having (grouping(PID)=1 and grouping(PNAME)=1 and grouping(HN)=1 and grouping(DATEOPD)=1 and grouping(SENDDATE)=1 and grouping(INDV_LATE)=1 and grouping(MAININSCL)=1 and grouping(approved)=1)");
            //sql.append(" or ");
            //////////sql.append(" -- row etc");
            //sql.append(" (grouping(PID)+grouping(PNAME)+grouping(HN)+grouping(DATEOPD)+grouping(SENDDATE)+grouping(INDV_LATE)+grouping(MAININSCL)+grouping(approved)=0)");
            if (report.getSql_orderBy().equals("")) {
                // code update date 30-04-2015
                sql.append(" order by pid,dateopd ASC");
            } else {
                sql.append(" ").append(report.getSql_orderBy());
            }

            System.out.println("sql ::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstm = conn.prepareStatement(sql.toString());
            rs = pstm.executeQuery();

            listIndivi = new ArrayList<ObjRptIndividual>();
            while (rs.next()) {
                listIndivi.add(setObjectIndv(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstm != null) {
                pstm.close();
            }
        }
        return listIndivi;
    }

    public List<ObjRptIndividualSum> getIndividualSum(OppReport report) {
        List<ObjRptIndividualSum> listData = null;
        ObjRptIndividualSum objData = null;
        try {
            sql = new StringBuilder("SELECT ");
            sql.append(" row_number() OVER (ORDER BY hcode) NO,HCODE ,HCODENAME ");
            sql.append(" ,count(distinct case when INDV_LATE = 'ทันเวลา' then pid end)   AS INTIME_MAN");
            sql.append(" ,count(distinct case when INDV_LATE = 'ทันเวลา' then txid end)  AS INTIME_VISIT ");
            sql.append(" ,sum(case when INDV_LATE = 'ทันเวลา' then TOTALREIMBURSE else 0 end) as INTIME_TOTALREIMBURSE");
            sql.append(" ,count(distinct case when INDV_LATE = 'ล่าช้า' then pid end)  AS LATE_MAN");
            sql.append(" ,count(distinct case when INDV_LATE = 'ล่าช้า' then txid end)  AS LATE_VISIT");
            sql.append(" ,sum(case when INDV_LATE = 'ล่าช้า' then TOTALREIMBURSE else 0 end) as LATE_TOTALREIMBURSE");
            sql.append(" ,count(distinct pid)TOTALALL_MAN");
            sql.append(" ,count(txid)TOTALALL_VISIT");
            sql.append(" ,sum(TOTALREIMBURSE) as TOTALALL_TOTALREIMBURSE");
            sql.append(" FROM RPT_OPBKK_INDV ");
            sql.append(" where 1=1 ");
            // > 6 <= 8
            if (STMP_57_FIX.equals(report.getYearMonth() + "-" + report.getNo())) {
                sql.append(" AND approved = '").append(APPROVED).append("' ");
            } else {
                if (new DateUtil().checkDateOpdCurrentBudgetYear(report.getBudget_date(), CHECK_DUPISNULL)) {
                    if (Integer.parseInt(report.getMonth()) > 6 && Integer.parseInt(report.getMonth()) <= 8) {
                        sql.append(" AND dup is null ");
                    } else {
                        sql.append(" AND approved = '").append(APPROVED).append("' ");
                    }
                } else {
                    /*
                     * 19-08-2015 
                     */
                    System.out.println(" 201400-1");
                    sql.append(" AND approved <> '").append(APPROVED_INDV).append("' ");
                    /*
                     * 19-08-2015 
                     */
                }
            }
            sql.append(" AND STMP = '").append(report.getYearMonth()).append("-").append(report.getNo()).append("'");
            if (!report.getBudget_year().equals("")) {
                int budgetYear = Integer.parseInt(report.getBudget_year());
                int minBudgetYear = (budgetYear - 1);
                int maxBudgetYear = budgetYear;
                sql.append(" and trunc(dateopd) between to_date('0110").append(minBudgetYear).append("','ddmmyyyy')");
                sql.append(" and to_date('3009").append(maxBudgetYear).append("','ddmmyyyy')");
            }
            /*
             Modified by poolsawat 02-06-2015
             */
            if (!report.getIndv_late().equals("")) {
                sql.append(" AND CASE INDV_LATE");
                sql.append(" WHEN 'ทันเวลา' THEN '0'");
                sql.append(" WHEN 'ล่าช้า' THEN '1'");
                sql.append(" END = '").append(report.getIndv_late()).append("' ");
            }
            sql.append(" group by HCODE ,HCODENAME ");
            sql.append(" order by HCODE ,HCODENAME ");
            // * หมายเหตุ Query นี้ จะ ต้องใช้ Query Method sum_fromSummaryIndividual() เพื่อ หาผลรวม

            System.out.println("sql ::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstm = conn.prepareStatement(sql.toString());
            rs = pstm.executeQuery();

            listData = new ArrayList<ObjRptIndividualSum>();

            while (rs.next()) {
                objData = new ObjRptIndividualSum();
                objData.setNo(rs.getInt("NO"));
                objData.setHcode(rs.getString("HCODE"));
                objData.setHcodename(rs.getString("HCODENAME"));
                objData.setIntime_man(rs.getDouble("INTIME_MAN"));
                objData.setIntime_visit(rs.getDouble("INTIME_VISIT"));
                objData.setIntime_totalreimburse(rs.getDouble("INTIME_TOTALREIMBURSE"));
                objData.setLate_man(rs.getDouble("LATE_MAN"));
                objData.setLate_visit(rs.getDouble("LATE_VISIT"));
                objData.setLate_totalreimburse(rs.getDouble("LATE_TOTALREIMBURSE"));
                objData.setTotalall_man(rs.getDouble("TOTALALL_MAN"));
                objData.setTotalall_visit(rs.getDouble("TOTALALL_VISIT"));
                objData.setTotalall_totalreimburse(rs.getDouble("TOTALALL_TOTALREIMBURSE"));
                listData.add(objData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    public ResultBean sum_fromSummaryIndividual(OppReport report) {
        ResultBean objectSum = null;
        try {
            //--sum total ของ pid ต้องแยกออกมา ใช้ group by ไม่ได้ เพราะ pid บางคนรักษาสถานพยาบาลหลายที่
            // ##################### QUERY เก่า #################################
            /*
             sql = new StringBuilder("SELECT");
             sql.append(" count(distinct case when INDV_LATE = 'ทันเวลา'then pid end)   AS TOTAL_INTIME_MAN ");
             sql.append(" ,count(distinct case when INDV_LATE = 'ทันเวลา'then txid end)  AS TOTAL_INTIME_VISIT ");
             sql.append(" ,count(distinct case when INDV_LATE = 'ล่าช้า'then pid end)  AS TOTAL_LATE_MAN");
             sql.append(" ,count(distinct case when INDV_LATE = 'ล่าช้า'then txid end)  AS TOTAL_LATE_VISIT");
             sql.append(" ,count(distinct pid)TOTALALL_MAN");
             sql.append(" ,count(distinct txid)TOTALALL_VISIT");
             sql.append(" from RPT_OPBKK_INDV");
             sql.append(" where 1=1  ");
             sql.append(" AND approved != '98' ");
             sql.append(" AND STMP = '").append(report.getYearMonth()).append("-").append(report.getNo()).append("'");
                    
             */
            // ###################### Query ใหม่ #########################
            sql = new StringBuilder();
            sql.append(" SELECT");
            sql.append(" sum(k.TOTAL_INTIME_MAN) as TOTAL_INTIME_MAN,");
            sql.append(" sum(k.TOTAL_INTIME_VISIT) as TOTAL_INTIME_VISIT,");
            sql.append(" sum(k.TOTAL_LATE_MAN) as TOTAL_LATE_MAN,");
            sql.append(" sum(k.TOTAL_LATE_VISIT) as TOTAL_LATE_VISIT,");
            sql.append(" sum(k.TOTALALL_MAN) as TOTALALL_MAN,");
            sql.append(" sum(k.TOTALALL_VISIT) as TOTALALL_VISIT");
            sql.append(" from (SELECT COUNT(DISTINCT CASE");
            sql.append(" WHEN INDV_LATE = 'ทันเวลา' THEN");
            sql.append(" pid");
            sql.append(" END) AS TOTAL_INTIME_MAN,");
            sql.append(" COUNT(DISTINCT CASE");
            sql.append(" WHEN INDV_LATE = 'ทันเวลา' THEN");
            sql.append(" txid");
            sql.append(" END) AS TOTAL_INTIME_VISIT,");
            sql.append(" COUNT(DISTINCT CASE");
            sql.append(" WHEN INDV_LATE = 'ล่าช้า' THEN");
            sql.append(" pid");
            sql.append(" END) AS TOTAL_LATE_MAN,");
            sql.append(" COUNT(DISTINCT CASE");
            sql.append(" WHEN INDV_LATE = 'ล่าช้า' THEN");
            sql.append(" txid");
            sql.append(" END) AS TOTAL_LATE_VISIT,");
            sql.append(" COUNT(DISTINCT pid) TOTALALL_MAN,");
            sql.append(" COUNT(txid) TOTALALL_VISIT");
            sql.append(" FROM RPT_OPBKK_INDV");
            sql.append(" WHERE 1 = 1");
            //sql.append(" AND dup IS NULL");
            // > 6 <= 8
            if (STMP_57_FIX.equals(report.getYearMonth() + "-" + report.getNo())) {
                sql.append(" AND approved = '").append(APPROVED).append("' ");
            } else {
                if (new DateUtil().checkDateOpdCurrentBudgetYear(report.getBudget_date(), CHECK_DUPISNULL)) {
                    System.out.println("report.getNo ::==" + report.getNo());
                    if (Integer.parseInt(report.getMonth()) > 6 && Integer.parseInt(report.getMonth()) <= 8) {
                        sql.append(" AND dup is null ");
                    } else {
                        sql.append(" AND approved = '").append(APPROVED).append("' ");
                    }
                } else {
                    /*
                     * 19-08-2015 
                     */
                    System.out.println(" 201400-1");
                    sql.append(" AND approved <> '").append(APPROVED_INDV).append("' ");
                    /*
                     * 19-08-2015 
                     */
                }
            }
            //sql.append(" AND STMP = '201408-1'");
            sql.append(" AND STMP = '").append(report.getYearMonth()).append("-").append(report.getNo()).append("'");
            if (!report.getBudget_year().equals("")) {
                int budgetYear = Integer.parseInt(report.getBudget_year());
                int minBudgetYear = (budgetYear - 1);
                int maxBudgetYear = budgetYear;
                sql.append(" and trunc(dateopd) between to_date('0110").append(minBudgetYear).append("','ddmmyyyy')");
                sql.append(" and to_date('3009").append(maxBudgetYear).append("','ddmmyyyy')");
            }
            if (!report.getIndv_late().equals("")) {
                sql.append(" AND CASE INDV_LATE");
                sql.append(" WHEN 'ทันเวลา' THEN '0'");
                sql.append(" WHEN 'ล่าช้า' THEN '1'");
                sql.append(" END = '").append(report.getIndv_late()).append("' ");
            }
            sql.append(" GROUP BY HCODE, HCODENAME");
            sql.append(" ORDER BY HCODE, HCODENAME) k");
            // ###################### Query ใหม่ #########################

            System.out.println("sql ::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstm = conn.prepareStatement(sql.toString());
            rs = pstm.executeQuery();

            objectSum = new ResultBean();

            while (rs.next()) {

                objectSum.setObject1(rs.getInt("TOTAL_INTIME_MAN"));
                objectSum.setObject2(rs.getInt("TOTAL_INTIME_VISIT"));
                objectSum.setObject3(rs.getInt("TOTAL_LATE_MAN"));
                objectSum.setObject4(rs.getInt("TOTAL_LATE_VISIT"));
                objectSum.setObject5(rs.getInt("TOTALALL_MAN"));
                objectSum.setObject6(rs.getInt("TOTALALL_VISIT"));
            }

            return objectSum;
        } catch (SQLException e) {
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
        return objectSum;
    }

    public int getNopayment(String stmp) {
        Integer no = null;
        try {
            sql = new StringBuilder();
            sql.append(" select t2.r from(");
            sql.append(" select rownum r,t1.stmp,t1.RPT_DATE");
            sql.append(" from(");
            sql.append(" select r.stmp,R.RPT_DATE from RPT_OPBKK_INDV r");
            sql.append(" where 1=1 ");
            sql.append(" group by r.stmp,R.RPT_DATE");
            sql.append(" order by R.RPT_DATE");
            sql.append(" )t1");
            sql.append(" )t2");
            sql.append(" where t2.stmp=?");

            System.out.println("sql ::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstm = conn.prepareStatement(sql.toString());
            pstm.setString(1, stmp);
            rs = pstm.executeQuery();
            while (rs.next()) {
                no = rs.getInt("r");
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
        return no;
    }

    public String getMonthPayment(String stmp) throws SQLException {
        String monthBetweenPayment = null;
        try {
            sql = new StringBuilder();
            sql.append(" SELECT STMP_DESC FROM RPT_HEADER");
            sql.append(" WHERE stmp = '").append(stmp).append("'");
            sql.append(" AND OPTYPE = 'ID'");

            System.out.println("sql ::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }
            pstm = conn.prepareStatement(sql.toString());
            rs = pstm.executeQuery();
            while (rs.next()) {
                monthBetweenPayment = rs.getString("STMP_DESC");
            }

            if (monthBetweenPayment == null) {
                monthBetweenPayment = "-";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return monthBetweenPayment;
    }

    private ObjRptIndividual setObjectIndv(ResultSet rs) {
        ObjRptIndividual objIndi = null;
        try {
            objIndi = new ObjRptIndividual();
            objIndi.setNo(rs.getInt("no"));
            objIndi.setHcode(rs.getString("hcode"));
            objIndi.setHcodename(rs.getString("hcodename"));
            objIndi.setPid(rs.getString("PID"));
            objIndi.setPname(rs.getString("PNAME"));
            objIndi.setHn(rs.getString("HN"));
            objIndi.setDateopd(rs.getString("DATEOPD"));
            objIndi.setDateopd_thai_buddha(rs.getString("dateopd_thai_buddha"));
            objIndi.setSenddate_thai_buddha(rs.getString("SENDDATE_thai_buddha"));
            objIndi.setOptypename(rs.getString("optypename"));
            objIndi.setSenddate(rs.getString("SENDDATE"));
            objIndi.setIndv_late(rs.getString("INDV_LATE"));
            objIndi.setMaininscl(rs.getString("MAININSCL"));
            objIndi.setTotalreimburse(rs.getDouble("TOTALREIMBURSE"));
            objIndi.setTxid(rs.getString("txid"));
            objIndi.setInvoice_no(rs.getString("INVOICE_NO"));
        } catch (SQLException ex) {
            Logger.getLogger(IndividualDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return objIndi;
    }
}
