package com.claim.dao;

import com.claim.object.Obj_log_stamp_payment;
import com.claim.object.ObjPaymentResult;
import com.claim.object.ObjPaymentStatus;
import com.claim.support.Console;
import com.claim.constants.ConstantMessage;
import com.claim.constants.ConstantStampPayment;
import com.claim.object.ProgrameStatus;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author poon__000
 */
public class PaymentStatusDao {

    Connection conn = null;
    StringBuilder sql = null;
    PreparedStatement pstm = null;
    ResultSet rs = null;
    static final int MaxMonthBudget = 9;
    static final String INDV_57 = "'201402-1','201403-1','201404-1','201404-2','201405-1','201406-1'";
    static final String INDV_SPECIAL_57 = "'201400-0'";
    static final String INDV_57_DUPISNULL = "'201407-1','201408-1'";
    static final String INDV_58 = "";
    static final String DATABASE_NAME = "opbkkclaim";
    static final String PACKAGE_NAME = "pkg_rpt_opbkk_2015"; //PKG_RPT_OPBKK_2015

    static final String QUERY_INDV_JOIN_PAYMENT = "SELECT r.stmp stmp_report, DECODE(st.stmp, NULL, '', 'แจ้งสถานะการจ่ายเงินแล้ว') status, st.DATEOPD_MIN, st.DATEOPD_MAX, st.D_SENDDATE_MIN, st.D_SENDDATE_MAX, TO_CHAR(SUM(r.TOTALREIMBURSE), '999,999,990.00') PAYMENT, TO_CHAR(COUNT(DISTINCT r.txid), '999,999,990.00') TOTALALL_VISIT, St.NUMBER_OF_TRANSFER NUMBER_OF_TRANSFER, St.DATE_OF_TRANSFER DATE_OF_TRANSFER, St.CREATE_DATE CREATE_DATE FROM rpt_opbkk_indv r LEFT JOIN OPBKK_STAMP_PAYMENT st ON (r.stmp = st.stmp AND st.optype = '"+ConstantStampPayment.OPTYPE_INDV+"')";


    public void setConnection(Connection connection) {
        this.conn = connection;
    }

    

    public int setProcessJobStart() {
        //http://www.dba-oracle.com/tips_oracle_dbms_job.htm      
        int exec = 0;
        int next_date = 1;
        try {
            String sql = " "
                    + " BEGIN \n"
                    + "    DBMS_JOB.SUBMIT (job         => ?,\n"
                    + "                     what        => 'OPPPINV.PKG_MAPPING43.MAIN;',\n" // .ใช้งานจริง
                    //+ "                     what        => 'PKG_OPBKK_POOLSAWAT.PROC_TEST_SETTIME_JOB;',\n" // สำหรับทดสอบ
                    + "                     next_date   => SYSDATE+" + String.valueOf(next_date) + ", \n" // TO_DATE ('25/05/2015 14:55:00', 'dd/mm/yyyy hh24:mi:ss'),
                    //+ "                     interval    => 'TRUNC(SYSDATE+1) + 4/24',\n"
                    + "                     no_parse    => FALSE);\n"
                    //+ "    COMMIT;\n"
                    + " END;\n";
            System.out.println("sql.toString() ::==" + sql);
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, "1");
            //pstm.setString(2, new DateUtil().getDateTimeForward(1));
            exec = pstm.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.commit();
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return exec;
    }

    public static void main(String[] args) {
        PaymentStatusDao payDao = new PaymentStatusDao();
        //payDao.openConnection();
        //payDao.setProcessJobStart();
        //new DateUtil().getDateTimeForward(1);
    }

    public Obj_log_stamp_payment getlastLogStampPayment() {
        Obj_log_stamp_payment objData = null;
        try {
            sql = new StringBuilder();
            sql.append(" SELECT L.LOGDATE, L.RPT_NAME, L.STMP,");
            sql.append(" L.N_RECORD, L.RESULT, L.DESCRIPTION ");
            sql.append(" from log_stamp_payment l");
            sql.append(" WHERE LOGDATE = (SELECT MAX(LOGDATE) FROM LOG_STAMP_PAYMENT)");
            pstm = conn.prepareStatement(sql.toString());
            rs = pstm.executeQuery();
            if (rs.next()) {
                objData = new Obj_log_stamp_payment();
                objData.setStmp(rs.getString("STMP"));
                objData.setLogdate(rs.getString("LOGDATE"));
                objData.setRpt_name(rs.getString("RPT_NAME"));
                objData.setN_record(rs.getInt("N_RECORD"));
                objData.setResult(rs.getString("RESULT"));
                objData.setDescription(rs.getString("DESCRIPTION"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstm != null) {
                    pstm.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return objData;
    }

    public ProgrameStatus callOracleStoredProc(ObjPaymentStatus objpaysta) {
        CallableStatement callableStatement = null;
        String storeProce = null;
        String table = "'";
        String is197 = "";
        int status = 0;
        ProgrameStatus programeStatus = new ProgrameStatus();
        try {
            if (objpaysta.getPayment_type() == 3 || objpaysta.getPayment_type() == 4) {
                switch (objpaysta.getPayment_type()) {
                    case 3: //op 197
                        table = "proc_type5_stampp";
                        is197 = "1";
                        break;
                    case 4: //op hightcost
                        table = "proc_type5_stampp";
                        is197 = "3";
                        break;
                }

                storeProce = "{call " + DATABASE_NAME + "." + PACKAGE_NAME + "." + table + "(?,?,?,?)} ";

                System.out.println("objpaysta.getStmp(): " + objpaysta.getStmp());
                System.out.println("objpaysta.getDate_opd(): " + objpaysta.getDate_opd());
                System.out.println("objpaysta.getNumber_of_transfer(): " + objpaysta.getNumber_of_transfer());
                System.out.println("objpaysta.is197(): " + is197);

                callableStatement = conn.prepareCall(storeProce);

                callableStatement.setString(1, objpaysta.getStmp());
                callableStatement.setString(2, objpaysta.getDate_opd());
                callableStatement.setString(3, objpaysta.getNumber_of_transfer());
                callableStatement.setString(4, is197);

            } else if (objpaysta.getPayment_type() == 8) { // noni

                storeProce = "{call " + DATABASE_NAME + "." + PACKAGE_NAME + ".proc_noni_stampp(?,?,?)} ";

                System.out.println("objpaysta.getStmp():" + objpaysta.getStmp());
                System.out.println("objpaysta.getDate_opd(): " + objpaysta.getDate_opd());
                System.out.println("objpaysta.getNumber_of_transfer():" + objpaysta.getNumber_of_transfer());

                callableStatement = conn.prepareCall(storeProce);

                callableStatement.setString(1, objpaysta.getStmp());
                callableStatement.setString(2, objpaysta.getDate_opd());
                callableStatement.setString(3, objpaysta.getNumber_of_transfer());
            } else {

                switch (objpaysta.getPayment_type()) {
                    case 1: // clearing
                        table = "proc_clearinghouse_stampp";
                        break;
                    case 2: // opae
                        table = "proc_ae74_stampp";
                        break;
                    case 3: //op ตัวเอง
                        //table = "proc_type5_stampp";  /
                        break;
                    case 4: //op ตัวเอง
                        //table = "proc_type5_stampp"; /
                        break;
                    case 5: // individual
                        table = "proc_individual_stampp";
                        break;
                    case 6: //refer
                        table = "proc_refer_stampp";
                        break;
                    case 7: // ว่างไว้ก่อน
                        //table = "proc_refer_stampp";
                        Console.LOG(" ######### ยังไม่มีรูปฟังช์ชันการทำงานนี้ #########", 0);
                        break;
                    case 8: //noni แยกเรียก package 58
                        //table = "proc_noni_stampp";          
                        Console.LOG(" ######### ยังไม่มีรูปฟังช์ชันการทำงานนี้ #########", 0);
                        break;
                    case 9: // ThaiMedicine Act
                        //table = "proc_tmd_act_stampp";         
                        Console.LOG(" ######### ยังไม่มีรูปฟังช์ชันการทำงานนี้ #########", 0);
                        break;
                    case 10: //ThaiMedicine Mom
                        //table = "proc_tmd_mom_stampp";   
                        Console.LOG(" ######### ยังไม่มีรูปฟังช์ชันการทำงานนี้ #########", 0);
                        break;
                    default:
                        System.out.println("Case TableName Fail");
                        Console.LOG("op type: Case TableName Fail", 0);
                        break;
                }

                storeProce = "{call " + DATABASE_NAME + "." + PACKAGE_NAME + "." + table + "(?,?,?)} "; //PKG_RPT_OPBKK_2015

                System.out.println("objpaysta.getStmp():" + objpaysta.getStmp());
                System.out.println("objpaysta.getDate_opd(): " + objpaysta.getDate_opd());
                System.out.println("objpaysta.getNumber_of_transfer():" + objpaysta.getNumber_of_transfer());

                if (!table.equals("")) {

                    callableStatement = conn.prepareCall(storeProce);

                    callableStatement.setString(1, objpaysta.getStmp());
                    callableStatement.setString(2, objpaysta.getDate_opd());
                    callableStatement.setString(3, objpaysta.getNumber_of_transfer());
                }
            }

            System.out.println("sql==>>" + storeProce);
            System.out.println(" table ::==" + table);
            if (!table.equals("")) {
                status = callableStatement.executeUpdate();
            }
            //MainBkkTabReport.textArea_payment_result.append("\n sql result: " + status);
            if (status == 1) {

                /*status = this.setProcessJobStart();
                
                 if (status == 1) {
                 programeStatus.setMessage("ประมวลผล และ ตั้ง Job การทำงานเรียบร้อย เสร็จสิ้น");
                 programeStatus.setTitle("Message Information");
                 programeStatus.setProcessStatus(true);
                 } else {
                 programeStatus.setMessage("การสร้างข้อมูลเกิดข้อผิดพลาด ในการ ตั้งค่าการทำงานของ Job ใน Database");
                 programeStatus.setTitle("Message Error");
                 programeStatus.setProcessStatus(false);
                 }*/
                programeStatus.setMessage("ประมวลผล การทำงานเรียบร้อย เสร็จสิ้น");
                programeStatus.setTitle("Message Information");
                programeStatus.setProcessStatus(true);
            } else {
                programeStatus.setMessage("การสร้างข้อมูลเกิดข้อผิดพลาด");
                programeStatus.setTitle("Message Error");
                programeStatus.setProcessStatus(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
            programeStatus.setMessage(ConstantMessage.MSG_PROCESS_FAILS + e.toString());
            programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
            programeStatus.setProcessStatus(false);
        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
            programeStatus.setMessage(ConstantMessage.MSG_PROCESS_FAILS + e.toString());
            programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
            programeStatus.setProcessStatus(false);
        }
        return programeStatus;
    }

    public List<ObjPaymentResult> getPaymentResult_refer(String stmp, int status) throws SQLException {
        List<ObjPaymentResult> listpayment = null;
        try {
            sql = new StringBuilder();
            sql.append(" ");
            sql.append(" select decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n)stmp,r.stmp stmp_report");
            sql.append(" ,decode(s.stmp,null,'','แจ้งสถานะการจ่ายเงินแล้ว') status"); //  -- สถานะ null คือ ยังไม่ได้แจ้งสถานะการจ่ายเงิน
            sql.append(" ,case when st.DATEOPD_MIN is not null then st.DATEOPD_MIN");
            sql.append(" else min(s.dateopd)");
            sql.append(" end DATEOPD_MIN");
            sql.append(" ,case when st.DATEOPD_MAX is not null then st.DATEOPD_MAX");
            sql.append(" else max(s.dateopd) ");
            sql.append(" end DATEOPD_MAX");
            sql.append(" ,case when st.D_SENDDATE_MIN is not null then st.D_SENDDATE_MIN");
            sql.append(" else min(s.d_senddate) ");
            sql.append(" end D_SENDDATE_MIN");
            sql.append(" ,case when st.D_SENDDATE_MAX is not null then st.D_SENDDATE_MAX");
            sql.append(" else max(s.d_senddate)");
            sql.append(" end D_SENDDATE_MAX");

            sql.append(" ,case when st.PAYMENT is not null then TO_char(st.PAYMENT,'999,999,990.00')");
            sql.append(" else TO_char(sum(r.paid_total),'999,999,990.00')");
            sql.append(" end PAYMENT");
            sql.append(" ,case when St.TOTAL_CASE is not null then TO_char(St.TOTAL_CASE,'999,999,990.00')");
            sql.append(" else TO_char(count(distinct s.txid) ,'999,999,990.00')");
            sql.append(" end TOTALALL_VISIT");

            sql.append(" ,St.NUMBER_OF_TRANSFER NUMBER_OF_TRANSFER");
            sql.append(" ,St.DATE_OF_TRANSFER DATE_OF_TRANSFER");
            sql.append(" ,St.CREATE_DATE CREATE_DATE");
            sql.append(" from opbkk_service s join rpt_opbkk_oprefer r on s.txid=r.txid");
            sql.append(" left join OPBKK_STAMP_PAYMENT st on (r.stmp=st.stmp and st.optype='"+ConstantStampPayment.OPTYPE_OPREFER+"')");
            sql.append(" WHERE 1=1 ");
            // เอาไว้ ตรวจสอบตอนสร้าง ข้อมูล tab แรก
            if (stmp != null) {
                sql.append(" AND r.stmp = '").append(stmp).append("'");
            }
            if (status == 1) { // จ่ายแล้ว
                sql.append(" AND st.stmp IS NOT null");
            } else if (status == 2) { // ยังไม่ได้จ่าย
                sql.append(" AND st.stmp IS null");
            }
            sql.append(" group by decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n),s.stmp,r.stmp,st.DATEOPD_MIN,st.DATEOPD_MAX,st.D_SENDDATE_MIN,st.D_SENDDATE_MAX");
            sql.append(" ,St.NUMBER_OF_TRANSFER,St.DATE_OF_TRANSFER,St.CREATE_DATE,st.PAYMENT,St.TOTAL_CASE");
            sql.append(" order by decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n),s.stmp,r.stmp");

            System.out.println("sql refer ::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstm = conn.prepareStatement(sql.toString());

            rs = pstm.executeQuery();

            listpayment = new ArrayList<ObjPaymentResult>();

            while (rs.next()) {
                listpayment.add(getSetObject(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG("refer " + e.getMessage(), 0);
        } finally {
            if (pstm != null) {
                pstm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listpayment;
    }

    public List<ObjPaymentResult> getPaymentResult_noni(String stmp, int status) throws SQLException {
        List<ObjPaymentResult> listpayment = null;
        try {
            sql = new StringBuilder();
            ///
            sql.append(" select decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n)stmp,r.stmp stmp_report");
            sql.append(" ,decode(s.stmp,null,'','แจ้งสถานะการจ่ายเงินแล้ว') status"); //  -- สถานะ null คือ ยังไม่ได้แจ้งสถานะการจ่ายเงิน
            sql.append(" ,case when st.DATEOPD_MIN is not null then st.DATEOPD_MIN");
            sql.append(" else min(s.dateopd)");
            sql.append(" end DATEOPD_MIN");
            sql.append(" ,case when st.DATEOPD_MAX is not null then st.DATEOPD_MAX");
            sql.append(" else max(s.dateopd) ");
            sql.append(" end DATEOPD_MAX");
            sql.append(" ,case when st.D_SENDDATE_MIN is not null then st.D_SENDDATE_MIN");
            sql.append(" else min(s.d_senddate) ");
            sql.append(" end D_SENDDATE_MIN");
            sql.append(" ,case when st.D_SENDDATE_MAX is not null then st.D_SENDDATE_MAX");
            sql.append(" else max(s.d_senddate)");
            sql.append(" end D_SENDDATE_MAX");

            sql.append(" ,case when st.PAYMENT is not null then TO_char(st.PAYMENT,'999,999,990.00')");
            sql.append(" else TO_char(sum(r.paid_total),'999,999,990.00')");
            sql.append(" end PAYMENT");
            sql.append(" ,case when St.TOTAL_CASE is not null then TO_char(St.TOTAL_CASE,'999,999,990.00')");
            sql.append(" else TO_char(count(distinct s.txid),'999,999,990.00')");
            sql.append(" end TOTALALL_VISIT");

            sql.append(" ,St.NUMBER_OF_TRANSFER NUMBER_OF_TRANSFER");
            sql.append(" ,St.DATE_OF_TRANSFER DATE_OF_TRANSFER");
            sql.append(" ,St.CREATE_DATE CREATE_DATE");
            sql.append(" from opbkk_service s join RPT_OPBKK_NONI r on s.txid=r.txid");
            sql.append(" left join OPBKK_STAMP_PAYMENT st on (r.stmp=st.stmp and st.optype='"+ConstantStampPayment.OPTYPE_NONI+"')");
            sql.append(" where 1=1");
            // เอาไว้ ตรวจสอบตอนสร้าง ข้อมูล tab แรก
            if (stmp != null) {
                sql.append(" AND r.stmp = '").append(stmp).append("'");
            }
            if (status == 1) { // จ่ายแล้ว
                sql.append(" AND st.stmp IS NOT null");
            } else if (status == 2) { // ยังไม่ได้จ่าย
                sql.append(" AND st.stmp IS null");
            }
            sql.append(" AND PAID_TOTAL > 0");
            sql.append(" group by decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n),s.stmp,r.stmp,st.DATEOPD_MIN,st.DATEOPD_MAX,st.D_SENDDATE_MIN,st.D_SENDDATE_MAX");
            sql.append(" ,St.NUMBER_OF_TRANSFER,St.DATE_OF_TRANSFER,St.CREATE_DATE,st.PAYMENT,St.TOTAL_CASE");
            sql.append(" order by decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n),s.stmp,r.stmp");

            System.out.println("sql noni::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }
            pstm = conn.prepareStatement(sql.toString());

            rs = pstm.executeQuery();

            listpayment = new ArrayList<ObjPaymentResult>();

            while (rs.next()) {
                listpayment.add(getSetObject(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG("refer " + e.getMessage(), 0);
        } finally {
            if (pstm != null) {
                pstm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listpayment;
    }

    public List<ObjPaymentResult> getPaymentResult_tmdAct(String stmp, int status) throws SQLException {
        List<ObjPaymentResult> listpayment = null;
        try {
            sql = new StringBuilder();
            ///
            sql.append(" select decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n)stmp,r.stmp stmp_report");
            sql.append(" ,decode(s.stmp,null,'','แจ้งสถานะการจ่ายเงินแล้ว') status"); //  -- สถานะ null คือ ยังไม่ได้แจ้งสถานะการจ่ายเงิน
            sql.append(" ,case when st.DATEOPD_MIN is not null then st.DATEOPD_MIN");
            sql.append(" else min(s.dateopd)");
            sql.append(" end DATEOPD_MIN");
            sql.append(" ,case when st.DATEOPD_MAX is not null then st.DATEOPD_MAX");
            sql.append(" else max(s.dateopd) ");
            sql.append(" end DATEOPD_MAX");
            sql.append(" ,case when st.D_SENDDATE_MIN is not null then st.D_SENDDATE_MIN");
            sql.append(" else min(s.d_senddate) ");
            sql.append(" end D_SENDDATE_MIN");
            sql.append(" ,case when st.D_SENDDATE_MAX is not null then st.D_SENDDATE_MAX");
            sql.append(" else max(s.d_senddate)");
            sql.append(" end D_SENDDATE_MAX");

            sql.append(" ,case when st.PAYMENT is not null then TO_char(st.PAYMENT,'999,999,990.00')");
            sql.append(" else TO_char(sum(r.totalpay),'999,999,990.00')");
            sql.append(" end PAYMENT");
            sql.append(" ,case when St.TOTAL_CASE is not null then TO_char(St.TOTAL_CASE,'999,999,990.00')");
            sql.append(" else TO_char(count(distinct s.txid),'999,999,990.00')");
            sql.append(" end TOTALALL_VISIT");

            sql.append(" ,St.NUMBER_OF_TRANSFER NUMBER_OF_TRANSFER");
            sql.append(" ,St.DATE_OF_TRANSFER DATE_OF_TRANSFER");
            sql.append(" ,St.CREATE_DATE CREATE_DATE");
            sql.append(" from opbkk_service s join RPT_OPBKK_TMD_ACT r on s.txid=r.txid");
            sql.append(" left join OPBKK_STAMP_PAYMENT st on (r.stmp=st.stmp and st.optype='").append(ConstantStampPayment.OPTYPE_TMD_ACT).append("')");
            sql.append(" where 1=1");
            // เอาไว้ ตรวจสอบตอนสร้าง ข้อมูล tab แรก
            if (stmp != null) {
                sql.append(" AND r.stmp = '").append(stmp).append("'");
            }
            if (status == 1) { // จ่ายแล้ว
                sql.append(" AND st.stmp IS NOT null");
            } else if (status == 2) { // ยังไม่ได้จ่าย
                sql.append(" AND st.stmp IS null");
            }
            sql.append(" group by decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n),s.stmp,r.stmp,st.DATEOPD_MIN,st.DATEOPD_MAX,st.D_SENDDATE_MIN,st.D_SENDDATE_MAX");
            sql.append(" ,St.NUMBER_OF_TRANSFER,St.DATE_OF_TRANSFER,St.CREATE_DATE,st.PAYMENT,St.TOTAL_CASE");
            sql.append(" order by decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n),s.stmp,r.stmp");

            System.out.println("sql noni::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }
            pstm = conn.prepareStatement(sql.toString());

            rs = pstm.executeQuery();

            listpayment = new ArrayList<ObjPaymentResult>();

            while (rs.next()) {
                listpayment.add(getSetObject(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG("refer " + e.getMessage(), 0);
        } finally {
            if (pstm != null) {
                pstm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listpayment;
    }

    public List<ObjPaymentResult> getPaymentResult_tmdMom(String stmp, int status) throws SQLException {
        List<ObjPaymentResult> listpayment = null;
        try {
            sql = new StringBuilder();
            ///
            sql.append(" select decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n)stmp,r.stmp stmp_report");
            sql.append(" ,decode(s.stmp,null,'','แจ้งสถานะการจ่ายเงินแล้ว') status"); //  -- สถานะ null คือ ยังไม่ได้แจ้งสถานะการจ่ายเงิน
            sql.append(" ,case when st.DATEOPD_MIN is not null then st.DATEOPD_MIN");
            sql.append(" else min(s.dateopd)");
            sql.append(" end DATEOPD_MIN");
            sql.append(" ,case when st.DATEOPD_MAX is not null then st.DATEOPD_MAX");
            sql.append(" else max(s.dateopd) ");
            sql.append(" end DATEOPD_MAX");
            sql.append(" ,case when st.D_SENDDATE_MIN is not null then st.D_SENDDATE_MIN");
            sql.append(" else min(s.d_senddate) ");
            sql.append(" end D_SENDDATE_MIN");
            sql.append(" ,case when st.D_SENDDATE_MAX is not null then st.D_SENDDATE_MAX");
            sql.append(" else max(s.d_senddate)");
            sql.append(" end D_SENDDATE_MAX");

            sql.append(" ,case when st.PAYMENT is not null then TO_char(st.PAYMENT,'999,999,990.00')");
            sql.append(" else TO_char(sum(r.totalpay),'999,999,990.00')");
            sql.append(" end PAYMENT");
            sql.append(" ,case when St.TOTAL_CASE is not null then TO_char(St.TOTAL_CASE,'999,999,990.00')");
            sql.append(" else TO_char(count(distinct s.txid),'999,999,990.00')");
            sql.append(" end TOTALALL_VISIT");

            sql.append(" ,St.NUMBER_OF_TRANSFER NUMBER_OF_TRANSFER");
            sql.append(" ,St.DATE_OF_TRANSFER DATE_OF_TRANSFER");
            sql.append(" ,St.CREATE_DATE CREATE_DATE");
            sql.append(" from opbkk_service s join RPT_OPBKK_TMD_MOM r on s.txid=r.txid");
            sql.append(" left join OPBKK_STAMP_PAYMENT st on (r.stmp=st.stmp and st.optype='").append(ConstantStampPayment.OPTYPE_TMD_MOM).append("')");
            sql.append(" where 1=1");
            // เอาไว้ ตรวจสอบตอนสร้าง ข้อมูล tab แรก
            if (stmp != null) {
                sql.append(" AND r.stmp = '").append(stmp).append("'");
            }
            if (status == 1) { // จ่ายแล้ว
                sql.append(" AND st.stmp IS NOT null");
            } else if (status == 2) { // ยังไม่ได้จ่าย
                sql.append(" AND st.stmp IS null");
            }
            sql.append(" group by decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n),s.stmp,r.stmp,st.DATEOPD_MIN,st.DATEOPD_MAX,st.D_SENDDATE_MIN,st.D_SENDDATE_MAX");
            sql.append(" ,St.NUMBER_OF_TRANSFER,St.DATE_OF_TRANSFER,St.CREATE_DATE,st.PAYMENT,St.TOTAL_CASE");
            sql.append(" order by decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n),s.stmp,r.stmp");

            System.out.println("sql noni::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }
            pstm = conn.prepareStatement(sql.toString());

            rs = pstm.executeQuery();

            listpayment = new ArrayList<ObjPaymentResult>();

            while (rs.next()) {
                listpayment.add(getSetObject(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG("refer " + e.getMessage(), 0);
        } finally {
            if (pstm != null) {
                pstm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listpayment;
    }

    public List<ObjPaymentResult> getPaymentResult_ae74(String stmp, int status) throws SQLException {
        List<ObjPaymentResult> listpayment = null;
        try {
            sql = new StringBuilder();
            sql.append(" ");

            sql.append(" select decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n)stmp,r.stmp stmp_report");
            sql.append(" ,decode(s.stmp,null,'','แจ้งสถานะการจ่ายเงินแล้ว') status"); //  -- สถานะ null คือ ยังไม่ได้แจ้งสถานะการจ่ายเงิน
            sql.append(" ,case when st.DATEOPD_MIN is not null then st.DATEOPD_MIN");
            sql.append(" else min(s.dateopd)");
            sql.append(" end DATEOPD_MIN");
            sql.append(" ,case when st.DATEOPD_MAX is not null then st.DATEOPD_MAX");
            sql.append(" else max(s.dateopd) ");
            sql.append(" end DATEOPD_MAX");
            sql.append(" ,case when st.D_SENDDATE_MIN is not null then st.D_SENDDATE_MIN");
            sql.append(" else min(s.d_senddate) ");
            sql.append(" end D_SENDDATE_MIN");
            sql.append(" ,case when st.D_SENDDATE_MAX is not null then st.D_SENDDATE_MAX");
            sql.append(" else max(s.d_senddate)");
            sql.append(" end D_SENDDATE_MAX");

            sql.append(" ,case when st.PAYMENT is not null then TO_char(st.PAYMENT,'999,999,990.00')");
            sql.append(" else TO_char(sum(r.paid_total),'999,999,990.00')");
            sql.append(" end PAYMENT");
            sql.append(" ,case when St.TOTAL_CASE is not null then TO_char(St.TOTAL_CASE,'999,999,990.00')");
            sql.append(" else TO_char(count(distinct s.txid),'999,999,990.00')");
            sql.append(" end TOTALALL_VISIT");

            sql.append(" ,St.NUMBER_OF_TRANSFER NUMBER_OF_TRANSFER");
            sql.append(" ,St.DATE_OF_TRANSFER DATE_OF_TRANSFER");
            sql.append(" ,St.CREATE_DATE CREATE_DATE");
            sql.append(" from opbkk_service s join rpt_opbkk_opae74 r on s.txid=r.txid");
            sql.append(" left join OPBKK_STAMP_PAYMENT st on (r.stmp=st.stmp and st.optype='").append(ConstantStampPayment.OPTYPE_OPAE74).append("')");
            sql.append(" where 1=1");
            // เอาไว้ ตรวจสอบตอนสร้าง ข้อมูล tab แรก
            if (stmp != null) {
                sql.append(" AND r.stmp = '").append(stmp).append("'");
            }
            if (status == 1) { // จ่ายแล้ว
                sql.append(" AND st.stmp IS NOT null");
            } else if (status == 2) { // ยังไม่ได้จ่าย
                sql.append(" AND st.stmp IS null");
            }
            sql.append(" AND PAID_TOTAL > 0");
            sql.append(" group by decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n),s.stmp,r.stmp,st.DATEOPD_MIN,st.DATEOPD_MAX,st.D_SENDDATE_MIN,st.D_SENDDATE_MAX");
            sql.append(" ,St.NUMBER_OF_TRANSFER,St.DATE_OF_TRANSFER,St.CREATE_DATE,st.PAYMENT,St.TOTAL_CASE");
            sql.append(" order by decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n),s.stmp,r.stmp");

            System.out.println("sql ae74:" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstm = conn.prepareStatement(sql.toString());

            rs = pstm.executeQuery();

            listpayment = new ArrayList<ObjPaymentResult>();

            while (rs.next()) {
                listpayment.add(getSetObject(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG("Ae74 " + e.getMessage(), 0);
        } finally {
            if (pstm != null) {
                pstm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listpayment;
    }

    public List<ObjPaymentResult> getPaymentResult_chula16(String stmp, int status) throws SQLException {
        List<ObjPaymentResult> listpayment = null;
        try {
            sql = new StringBuilder();

            sql.append(" select decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n)stmp,r.stmp stmp_report");
            sql.append(" ,decode(s.stmp,null,'','แจ้งสถานะการจ่ายเงินแล้ว') status"); //  -- สถานะ null คือ ยังไม่ได้แจ้งสถานะการจ่ายเงิน
            sql.append(" ,case when st.DATEOPD_MIN is not null then st.DATEOPD_MIN");
            sql.append(" else min(s.dateopd)");
            sql.append(" end DATEOPD_MIN");
            sql.append(" ,case when st.DATEOPD_MAX is not null then st.DATEOPD_MAX");
            sql.append(" else max(s.dateopd) ");
            sql.append(" end DATEOPD_MAX");
            sql.append(" ,case when st.D_SENDDATE_MIN is not null then st.D_SENDDATE_MIN");
            sql.append(" else min(s.d_senddate) ");
            sql.append(" end D_SENDDATE_MIN");
            sql.append(" ,case when st.D_SENDDATE_MAX is not null then st.D_SENDDATE_MAX");
            sql.append(" else max(s.d_senddate)");
            sql.append(" end D_SENDDATE_MAX");

            sql.append(" ,case when st.PAYMENT is not null then TO_char(st.PAYMENT,'999,999,990.00')");
            sql.append(" else TO_char(sum(r.paid_total),'999,999,990.00')");
            sql.append(" end PAYMENT");
            sql.append(" ,case when St.TOTAL_CASE is not null then TO_char(St.TOTAL_CASE,'999,999,990.00')");
            sql.append(" else TO_char(count(distinct s.txid),'999,999,990.00')");
            sql.append(" end TOTALALL_VISIT");

            sql.append(" ,St.NUMBER_OF_TRANSFER NUMBER_OF_TRANSFER");
            sql.append(" ,St.DATE_OF_TRANSFER DATE_OF_TRANSFER");
            sql.append(" ,St.CREATE_DATE CREATE_DATE");
            sql.append(" from opbkk_service s join RPT_OPBKK_HC16CHULA r on s.txid=r.txid");
            sql.append(" left join OPBKK_STAMP_PAYMENT st on (r.stmp=st.stmp and st.optype='").append(ConstantStampPayment.OPTYPE_CHULA).append("')");
            sql.append(" where 1=1");
            // เอาไว้ ตรวจสอบตอนสร้าง ข้อมูล tab แรก
            if (stmp != null) {
                sql.append(" AND r.stmp = '").append(stmp).append("'");
            }
            if (status == 1) { // จ่ายแล้ว
                sql.append(" AND st.stmp IS NOT null");
            } else if (status == 2) { // ยังไม่ได้จ่าย
                sql.append(" AND st.stmp IS null");
            }
            sql.append(" group by decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n),s.stmp,r.stmp,st.DATEOPD_MIN,st.DATEOPD_MAX,st.D_SENDDATE_MIN,st.D_SENDDATE_MAX");
            sql.append(" ,St.NUMBER_OF_TRANSFER,St.DATE_OF_TRANSFER,St.CREATE_DATE,st.PAYMENT,St.TOTAL_CASE");
            sql.append(" order by decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n),s.stmp,r.stmp");

            System.out.println("sql Chula :" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstm = conn.prepareStatement(sql.toString());

            rs = pstm.executeQuery();

            listpayment = new ArrayList<ObjPaymentResult>();

            while (rs.next()) {
               listpayment.add(getSetObject(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG("Ae74 " + e.getMessage(), 0);
        } finally {
            if (pstm != null) {
                pstm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listpayment;
    }

    public List<ObjPaymentResult> getPaymentResult_type5_197(String stmp, int status, String fix197) throws SQLException {
        List<ObjPaymentResult> listpayment = null;
        try {
            /* sql = new StringBuilder();
             sql.append(" ");
             sql.append(" select decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n)stmp,r.stmp stmp_report");
             sql.append(" ,decode(s.stmp,null,'','แจ้งสถานะการจ่ายเงินแล้ว') status");//  -- สถานะ null คือ ยังไม่ได้แจ้งสถานะการจ่ายเงิน
             sql.append(" ,case when st.DATEOPD_MIN is not null then st.DATEOPD_MIN");
             sql.append(" else min(s.dateopd)");
             sql.append(" end DATEOPD_MIN");
             sql.append(" ,case when st.DATEOPD_MAX is not null then st.DATEOPD_MAX");
             sql.append(" else max(s.dateopd) ");
             sql.append(" end DATEOPD_MAX");
             sql.append(" ,case when st.D_SENDDATE_MIN is not null then st.D_SENDDATE_MIN");
             sql.append(" else min(s.d_senddate) ");
             sql.append(" end D_SENDDATE_MIN");
             sql.append(" ,case when st.D_SENDDATE_MAX is not null then st.D_SENDDATE_MAX");
             sql.append(" else max(s.d_senddate)");
             sql.append(" end D_SENDDATE_MAX");

             sql.append(" ,case when st.PAYMENT is not null then TO_char(st.PAYMENT,'999,999,990.00')");
             sql.append(" else TO_char(sum(r.TOTALREIMBURSE),'999,999,990.00')");
             sql.append(" end PAYMENT");
             sql.append(" ,case when St.TOTAL_CASE is not null then TO_char(St.TOTAL_CASE,'999,999,990.00')");
             sql.append(" else TO_char(count(distinct s.txid),'999,999,990.00')");
             sql.append(" end TOTALALL_VISIT");

             sql.append(" ,St.NUMBER_OF_TRANSFER NUMBER_OF_TRANSFER");
             sql.append(" ,St.DATE_OF_TRANSFER DATE_OF_TRANSFER");
             sql.append(" ,St.CREATE_DATE CREATE_DATE");
             sql.append(" from opbkk_service s join rpt_opbkk_type5 r on s.txid=r.txid");
             sql.append(" left join OPBKK_STAMP_PAYMENT st on (r.stmp=st.stmp and st.optype='").append(OPTYPE).append("')");
             sql.append(" where 1=1");
             sql.append(" and R.IS197 IN (1,5)"); // modified at 04-06-2015
             // เอาไว้ ตรวจสอบตอนสร้าง ข้อมูล tab แรก
             if (stmp != null) {
             sql.append(" AND r.stmp = '").append(stmp).append("'");
             }
             if (status == 1) { // จ่ายแล้ว
             sql.append(" AND s.stmp IS NOT null");
             } else if (status == 2) { // ยังไม่ได้จ่าย
             sql.append(" AND s.stmp IS null");
             }
             sql.append(" group by decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n),s.stmp,r.stmp,st.DATEOPD_MIN,st.DATEOPD_MAX,st.D_SENDDATE_MIN,st.D_SENDDATE_MAX");
             sql.append(" ,St.NUMBER_OF_TRANSFER,St.DATE_OF_TRANSFER,St.CREATE_DATE,st.PAYMENT,St.TOTAL_CASE");
             sql.append(" order by decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n),s.stmp,r.stmp");*/

            sql = new StringBuilder();
            sql.append(" select");
            //sql.append(" decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n)stmp,");
            sql.append(" r.stmp stmp_report");
            sql.append(" ,decode(st.stmp,null,'','แจ้งสถานะการจ่ายเงินแล้ว') status");//  -- สถานะ null คือ ยังไม่ได้แจ้งสถานะการจ่ายเงิน
            sql.append(" ,case when st.DATEOPD_MIN is not null then st.DATEOPD_MIN");
            sql.append(" else min(s.dateopd)");
            sql.append(" end DATEOPD_MIN");
            sql.append(" ,case when st.DATEOPD_MAX is not null then st.DATEOPD_MAX");
            sql.append(" else max(s.dateopd) ");
            sql.append(" end DATEOPD_MAX");
            sql.append(" ,case when st.D_SENDDATE_MIN is not null then st.D_SENDDATE_MIN");
            sql.append(" else min(s.d_senddate) ");
            sql.append(" end D_SENDDATE_MIN");
            sql.append(" ,case when st.D_SENDDATE_MAX is not null then st.D_SENDDATE_MAX");
            sql.append(" else max(s.d_senddate)");
            sql.append(" end D_SENDDATE_MAX");

            sql.append(" ,case when st.PAYMENT is not null then TO_char(st.PAYMENT,'999,999,990.00')");
            sql.append(" else TO_char(sum(r.TOTALREIMBURSE),'999,999,990.00')");
            sql.append(" end PAYMENT");
            sql.append(" ,case when St.TOTAL_CASE is not null then TO_char(St.TOTAL_CASE,'999,999,990')");
            sql.append(" else TO_char(count(distinct s.txid) ,'999,999,990')");
            sql.append(" end TOTALALL_VISIT");

            sql.append(" ,St.NUMBER_OF_TRANSFER NUMBER_OF_TRANSFER");
            sql.append(" ,St.DATE_OF_TRANSFER DATE_OF_TRANSFER");
            sql.append(" ,St.CREATE_DATE CREATE_DATE");
            sql.append(" from opbkk_service s join rpt_opbkk_type5 r on s.txid=r.txid");
            sql.append(" left join OPBKK_STAMP_PAYMENT st on (r.stmp=st.stmp and st.optype='").append(ConstantStampPayment.OPTYPE_TYPE5_197).append("')");
            sql.append(" where 1=1");

            /*
             Modefied 09-06-2015
             */
            sql.append(" and R.IS197 IN (").append(fix197).append(")"); // modified at 04-06-2015  //(1,5)
            if (fix197.equals("1")) {
                sql.append(" AND r.stmp IN ('201405-1','201408-1','201409-1','201501-1')");
            } else {  // 3,4                
                sql.append(" AND r.stmp NOT IN ('201405-1','201408-1','201409-1','201501-1')");
            }
            /*
             Modefied 09-06-2015
             */

            // เอาไว้ ตรวจสอบตอนสร้าง ข้อมูล tab แรก
            if (stmp != null) {
                sql.append(" AND rt.stmp = '").append(stmp).append("'");
            }
            if (status == 1) { // จ่ายแล้ว
                sql.append(" AND st.stmp IS NOT null");
            } else if (status == 2) { // ยังไม่ได้จ่าย
                sql.append(" AND st.stmp IS null");
            }
            sql.append(" group by ");
            //sql.append(" decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n),");
            sql.append(" st.stmp,r.stmp,st.DATEOPD_MIN,st.DATEOPD_MAX,st.D_SENDDATE_MIN,st.D_SENDDATE_MAX");
            sql.append(" ,St.NUMBER_OF_TRANSFER,St.DATE_OF_TRANSFER,St.CREATE_DATE,st.PAYMENT,St.TOTAL_CASE");
            sql.append(" order by ");
            //sql.append(" decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n),s.stmp,");
            sql.append(" r.stmp");

            System.out.println("sql type5:" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstm = conn.prepareStatement(sql.toString());

            rs = pstm.executeQuery();

            listpayment = new ArrayList<ObjPaymentResult>();

            while (rs.next()) {
                listpayment.add(getSetObject(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG("Type5 " + e.getMessage(), 0);
        } finally {
            if (pstm != null) {
                pstm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listpayment;
    }

    public List<ObjPaymentResult> getPaymentResult_type5_HightCost(String stmp, int status, String fix197) throws SQLException {
        List<ObjPaymentResult> listpayment = null;        
        // fix197 = 
        try {
            sql = new StringBuilder();
            sql.append(" select");
            //sql.append(" decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n)stmp,");
            sql.append(" r.stmp stmp_report");
            sql.append(" ,decode(st.stmp,null,'','แจ้งสถานะการจ่ายเงินแล้ว') status");//  -- สถานะ null คือ ยังไม่ได้แจ้งสถานะการจ่ายเงิน
            sql.append(" ,case when st.DATEOPD_MIN is not null then st.DATEOPD_MIN");
            sql.append(" else min(s.dateopd)");
            sql.append(" end DATEOPD_MIN");
            sql.append(" ,case when st.DATEOPD_MAX is not null then st.DATEOPD_MAX");
            sql.append(" else max(s.dateopd) ");
            sql.append(" end DATEOPD_MAX");
            sql.append(" ,case when st.D_SENDDATE_MIN is not null then st.D_SENDDATE_MIN");
            sql.append(" else min(s.d_senddate) ");
            sql.append(" end D_SENDDATE_MIN");
            sql.append(" ,case when st.D_SENDDATE_MAX is not null then st.D_SENDDATE_MAX");
            sql.append(" else max(s.d_senddate)");
            sql.append(" end D_SENDDATE_MAX");

            sql.append(" ,case when st.PAYMENT is not null then TO_char(st.PAYMENT,'999,999,990.00')");
            sql.append(" else TO_char(sum(r.TOTALREIMBURSE),'999,999,990.00')");
            sql.append(" end PAYMENT");
            sql.append(" ,case when St.TOTAL_CASE is not null then TO_char(St.TOTAL_CASE,'999,999,990')");
            sql.append(" else TO_char(count(distinct s.txid) ,'999,999,990')");
            sql.append(" end TOTALALL_VISIT");

            sql.append(" ,St.NUMBER_OF_TRANSFER NUMBER_OF_TRANSFER");
            sql.append(" ,St.DATE_OF_TRANSFER DATE_OF_TRANSFER");
            sql.append(" ,St.CREATE_DATE CREATE_DATE");
            sql.append(" from opbkk_service s join rpt_opbkk_type5 r on s.txid=r.txid");
            sql.append(" left join OPBKK_STAMP_PAYMENT st on (r.stmp=st.stmp and st.optype='").append(ConstantStampPayment.OPTYPE_TYPE5_HC).append("')");
            sql.append(" where 1=1");

            /*
             Modefied 09-06-2015
             */
            sql.append(" and R.IS197 IN (").append(fix197).append(")"); // modified at 04-06-2015  //(3,4)
            if (fix197.equals("3")) {
                sql.append(" AND r.stmp IN ('201405-1','201408-1','201409-1','201501-1')");
            } else {  // 3,4                
                sql.append(" AND r.stmp NOT IN ('201405-1','201408-1','201409-1','201501-1')");
            }
            /*
             Modefied 09-06-2015
             */

            // เอาไว้ ตรวจสอบตอนสร้าง ข้อมูล tab แรก
            if (stmp != null) {
                sql.append(" AND st.stmp = '").append(stmp).append("'");
            }
            if (status == 1) { // จ่ายแล้ว
                sql.append(" AND st.stmp IS NOT null");
            } else if (status == 2) { // ยังไม่ได้จ่าย
                sql.append(" AND st.stmp IS null");
            }
            sql.append(" group by ");
            //sql.append(" decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n),");
            sql.append(" st.stmp,r.stmp,st.DATEOPD_MIN,st.DATEOPD_MAX,st.D_SENDDATE_MIN,st.D_SENDDATE_MAX");
            sql.append(" ,St.NUMBER_OF_TRANSFER,St.DATE_OF_TRANSFER,St.CREATE_DATE,st.PAYMENT,St.TOTAL_CASE");
            sql.append(" order by ");
            //sql.append(" decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n),s.stmp,");
            sql.append(" r.stmp");

            System.out.println("sql type5 ::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }
            pstm = conn.prepareStatement(sql.toString());

            rs = pstm.executeQuery();

            listpayment = new ArrayList<ObjPaymentResult>();

            while (rs.next()) {
                listpayment.add(getSetObject(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG("Type5 " + e.getMessage(), 0);
        } finally {
            if (pstm != null) {
                pstm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listpayment;
    }
    
    public List<ObjPaymentResult> getPaymentResult_vajira11535(String stmp, int status) throws SQLException {
        List<ObjPaymentResult> listpayment = null;
        try {
            sql = new StringBuilder();
            sql.append(" select decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n)stmp,r.stmp stmp_report");
            sql.append(" ,decode(s.stmp,null,'','แจ้งสถานะการจ่ายเงินแล้ว') status"); //  -- สถานะ null คือ ยังไม่ได้แจ้งสถานะการจ่ายเงิน
            sql.append(" ,case when st.DATEOPD_MIN is not null then st.DATEOPD_MIN");
            sql.append(" else min(s.dateopd)");
            sql.append(" end DATEOPD_MIN");
            sql.append(" ,case when st.DATEOPD_MAX is not null then st.DATEOPD_MAX");
            sql.append(" else max(s.dateopd) ");
            sql.append(" end DATEOPD_MAX");
            sql.append(" ,case when st.D_SENDDATE_MIN is not null then st.D_SENDDATE_MIN");
            sql.append(" else min(s.d_senddate) ");
            sql.append(" end D_SENDDATE_MIN");
            sql.append(" ,case when st.D_SENDDATE_MAX is not null then st.D_SENDDATE_MAX");
            sql.append(" else max(s.d_senddate)");
            sql.append(" end D_SENDDATE_MAX");

            sql.append(" ,case when st.PAYMENT is not null then TO_char(st.PAYMENT,'999,999,990.00')");
            sql.append(" else TO_char(sum(r.paid_total),'999,999,990.00')");
            sql.append(" end PAYMENT");
            sql.append(" ,case when St.TOTAL_CASE is not null then TO_char(St.TOTAL_CASE,'999,999,990.00')");
            sql.append(" else TO_char(count(distinct s.txid),'999,999,990.00')");
            sql.append(" end TOTALALL_VISIT");

            sql.append(" ,St.NUMBER_OF_TRANSFER NUMBER_OF_TRANSFER");
            sql.append(" ,St.DATE_OF_TRANSFER DATE_OF_TRANSFER");
            sql.append(" ,St.CREATE_DATE CREATE_DATE");
            sql.append(" from opbkk_service s join RPT_OPBKK_VAJIRA_HC11535 r on s.txid=r.txid");
            sql.append(" left join OPBKK_STAMP_PAYMENT st on (r.stmp=st.stmp and st.optype='"+ConstantStampPayment.OPTYPE_VAJIRA_11535+"')");
            sql.append(" where 1=1");
            // เอาไว้ ตรวจสอบตอนสร้าง ข้อมูล tab แรก
            if (stmp != null) {
                sql.append(" AND r.stmp = '").append(stmp).append("'");
            }
            if (status == 1) { // จ่ายแล้ว
                sql.append(" AND st.stmp IS NOT null");
            } else if (status == 2) { // ยังไม่ได้จ่าย
                sql.append(" AND st.stmp IS null");
            }
            sql.append(" group by decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n),s.stmp,r.stmp,st.DATEOPD_MIN,st.DATEOPD_MAX,st.D_SENDDATE_MIN,st.D_SENDDATE_MAX");
            sql.append(" ,St.NUMBER_OF_TRANSFER,St.DATE_OF_TRANSFER,St.CREATE_DATE,st.PAYMENT,St.TOTAL_CASE");
            sql.append(" order by decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n),s.stmp,r.stmp");

            System.out.println("sql VAJIRA_HC11535 :" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstm = conn.prepareStatement(sql.toString());

            rs = pstm.executeQuery();

            listpayment = new ArrayList<ObjPaymentResult>();

            while (rs.next()) {
               listpayment.add(getSetObject(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG("RPT_OPBKK_VAJIRA_HC11535 " + e.getMessage(), 0);
        } finally {
            if (pstm != null) {
                pstm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listpayment;
    }
    
    
    public List<ObjPaymentResult> getPaymentResult_vajiraRf(String stmp, int status) throws SQLException {
        List<ObjPaymentResult> listpayment = null;        
        try {
            sql = new StringBuilder();
            sql.append(" select decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n)stmp,r.stmp stmp_report");
            sql.append(" ,decode(s.stmp,null,'','แจ้งสถานะการจ่ายเงินแล้ว') status"); //  -- สถานะ null คือ ยังไม่ได้แจ้งสถานะการจ่ายเงิน
            sql.append(" ,case when st.DATEOPD_MIN is not null then st.DATEOPD_MIN");
            sql.append(" else min(s.dateopd)");
            sql.append(" end DATEOPD_MIN");
            sql.append(" ,case when st.DATEOPD_MAX is not null then st.DATEOPD_MAX");
            sql.append(" else max(s.dateopd) ");
            sql.append(" end DATEOPD_MAX");
            sql.append(" ,case when st.D_SENDDATE_MIN is not null then st.D_SENDDATE_MIN");
            sql.append(" else min(s.d_senddate) ");
            sql.append(" end D_SENDDATE_MIN");
            sql.append(" ,case when st.D_SENDDATE_MAX is not null then st.D_SENDDATE_MAX");
            sql.append(" else max(s.d_senddate)");
            sql.append(" end D_SENDDATE_MAX");

            sql.append(" ,case when st.PAYMENT is not null then TO_char(st.PAYMENT,'999,999,990.00')");
            sql.append(" else TO_char(sum(r.paid_total),'999,999,990.00')");
            sql.append(" end PAYMENT");
            sql.append(" ,case when St.TOTAL_CASE is not null then TO_char(St.TOTAL_CASE,'999,999,990.00')");
            sql.append(" else TO_char(count(distinct s.txid),'999,999,990.00')");
            sql.append(" end TOTALALL_VISIT");

            sql.append(" ,St.NUMBER_OF_TRANSFER NUMBER_OF_TRANSFER");
            sql.append(" ,St.DATE_OF_TRANSFER DATE_OF_TRANSFER");
            sql.append(" ,St.CREATE_DATE CREATE_DATE");
            sql.append(" from opbkk_service s join RPT_OPBKK_VAJIRA_RF r on s.txid=r.txid");
            sql.append(" left join OPBKK_STAMP_PAYMENT st on (r.stmp=st.stmp and st.optype='"+ConstantStampPayment.OPTYPE_VAJIRA_RF+"')");
            sql.append(" where 1=1");
            // เอาไว้ ตรวจสอบตอนสร้าง ข้อมูล tab แรก
            if (stmp != null) {
                sql.append(" AND r.stmp = '").append(stmp).append("'");
            }
            if (status == 1) { // จ่ายแล้ว
                sql.append(" AND st.stmp IS NOT null");
            } else if (status == 2) { // ยังไม่ได้จ่าย
                sql.append(" AND st.stmp IS null");
            }
            sql.append(" group by decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n),s.stmp,r.stmp,st.DATEOPD_MIN,st.DATEOPD_MAX,st.D_SENDDATE_MIN,st.D_SENDDATE_MAX");
            sql.append(" ,St.NUMBER_OF_TRANSFER,St.DATE_OF_TRANSFER,St.CREATE_DATE,st.PAYMENT,St.TOTAL_CASE");
            sql.append(" order by decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n),s.stmp,r.stmp");

            System.out.println("sql VAJIRA_RF :" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstm = conn.prepareStatement(sql.toString());

            rs = pstm.executeQuery();

            listpayment = new ArrayList<ObjPaymentResult>();

            while (rs.next()) {
               listpayment.add(getSetObject(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG("RPT_OPBKK_VAJIRA_RF " + e.getMessage(), 0);
        } finally {
            if (pstm != null) {
                pstm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listpayment;
    }
    
    
    public List<ObjPaymentResult> getPaymentResult_vajira(String stmp, int status) throws SQLException {
        List<ObjPaymentResult> listpayment = null;
        try {
            sql = new StringBuilder();
            sql.append(" select decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n)stmp,r.stmp stmp_report");
            sql.append(" ,decode(s.stmp,null,'','แจ้งสถานะการจ่ายเงินแล้ว') status"); //  -- สถานะ null คือ ยังไม่ได้แจ้งสถานะการจ่ายเงิน
            sql.append(" ,case when st.DATEOPD_MIN is not null then st.DATEOPD_MIN");
            sql.append(" else min(s.dateopd)");
            sql.append(" end DATEOPD_MIN");
            sql.append(" ,case when st.DATEOPD_MAX is not null then st.DATEOPD_MAX");
            sql.append(" else max(s.dateopd) ");
            sql.append(" end DATEOPD_MAX");
            sql.append(" ,case when st.D_SENDDATE_MIN is not null then st.D_SENDDATE_MIN");
            sql.append(" else min(s.d_senddate) ");
            sql.append(" end D_SENDDATE_MIN");
            sql.append(" ,case when st.D_SENDDATE_MAX is not null then st.D_SENDDATE_MAX");
            sql.append(" else max(s.d_senddate)");
            sql.append(" end D_SENDDATE_MAX");

            sql.append(" ,case when st.PAYMENT is not null then TO_char(st.PAYMENT,'999,999,990.00')");
            sql.append(" else TO_char(sum(r.paid_total),'999,999,990.00')");
            sql.append(" end PAYMENT");
            sql.append(" ,case when St.TOTAL_CASE is not null then TO_char(St.TOTAL_CASE,'999,999,990.00')");
            sql.append(" else TO_char(count(distinct s.txid),'999,999,990.00')");
            sql.append(" end TOTALALL_VISIT");

            sql.append(" ,St.NUMBER_OF_TRANSFER NUMBER_OF_TRANSFER");
            sql.append(" ,St.DATE_OF_TRANSFER DATE_OF_TRANSFER");
            sql.append(" ,St.CREATE_DATE CREATE_DATE");
            sql.append(" from opbkk_service s join RPT_OPBKK_VAJIRA_HC r on s.txid=r.txid");
            sql.append(" left join OPBKK_STAMP_PAYMENT st on (r.stmp=st.stmp and st.optype='"+ConstantStampPayment.OPTYPE_VAJIRA+"')");
            sql.append(" where 1=1");
            // เอาไว้ ตรวจสอบตอนสร้าง ข้อมูล tab แรก
            if (stmp != null) {
                sql.append(" AND r.stmp = '").append(stmp).append("'");
            }
            if (status == 1) { // จ่ายแล้ว
                sql.append(" AND st.stmp IS NOT null");
            } else if (status == 2) { // ยังไม่ได้จ่าย
                sql.append(" AND st.stmp IS null");
            }
            sql.append(" group by decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n),s.stmp,r.stmp,st.DATEOPD_MIN,st.DATEOPD_MAX,st.D_SENDDATE_MIN,st.D_SENDDATE_MAX");
            sql.append(" ,St.NUMBER_OF_TRANSFER,St.DATE_OF_TRANSFER,St.CREATE_DATE,st.PAYMENT,St.TOTAL_CASE");
            sql.append(" order by decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n),s.stmp,r.stmp");

            System.out.println("sql VAJIRA_HC :" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstm = conn.prepareStatement(sql.toString());

            rs = pstm.executeQuery();

            listpayment = new ArrayList<ObjPaymentResult>();

            while (rs.next()) {
               listpayment.add(getSetObject(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG("RPT_OPBKK_VAJIRA_HC " + e.getMessage(), 0);
        } finally {
            if (pstm != null) {
                pstm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listpayment;
    }

    public List<ObjPaymentResult> getPaymentResult_clearinghouse(String stmp, int status) throws SQLException {
        List<ObjPaymentResult> listpayment = null;
        try {
            sql = new StringBuilder();
            sql.append(" ");
            sql.append(" select decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n)stmp,r.stmp stmp_report");
            sql.append(" ,decode(s.stmp,null,'','แจ้งสถานะการจ่ายเงินแล้ว') status"); //  -- สถานะ null คือ ยังไม่ได้แจ้งสถานะการจ่ายเงิน
            sql.append(" ,case when st.DATEOPD_MIN is not null then st.DATEOPD_MIN");
            sql.append(" else min(s.dateopd)");
            sql.append(" end DATEOPD_MIN");
            sql.append(" ,case when st.DATEOPD_MAX is not null then st.DATEOPD_MAX");
            sql.append(" else max(s.dateopd) ");
            sql.append(" end DATEOPD_MAX");
            sql.append(" ,case when st.D_SENDDATE_MIN is not null then st.D_SENDDATE_MIN");
            sql.append(" else min(s.d_senddate) ");
            sql.append(" end D_SENDDATE_MIN");
            sql.append(" ,case when st.D_SENDDATE_MAX is not null then st.D_SENDDATE_MAX");
            sql.append(" else max(s.d_senddate)");
            sql.append(" end D_SENDDATE_MAX");

            sql.append(" ,case when st.PAYMENT is not null then TO_char(st.PAYMENT,'999,999,990.00')");
            sql.append(" else TO_char(sum(r.REIMBURSE),'999,999,990.00')");
            sql.append(" end PAYMENT");
            sql.append(" ,case when St.TOTAL_CASE is not null then TO_char(St.TOTAL_CASE,'999,999,990.00')");
            sql.append(" else TO_char(count(distinct s.txid),'999,999,990.00')");
            sql.append(" end TOTALALL_VISIT");

            sql.append(" ,St.NUMBER_OF_TRANSFER NUMBER_OF_TRANSFER");
            sql.append(" ,St.DATE_OF_TRANSFER DATE_OF_TRANSFER");
            sql.append(" ,St.CREATE_DATE CREATE_DATE");
            sql.append(" from opbkk_service s join rpt_opbkk_type6 r on s.txid=r.txid");
            sql.append(" left join OPBKK_STAMP_PAYMENT st on (r.stmp=st.stmp and st.optype='"+ConstantStampPayment.OPTYPE_TYPE6+"')");
            sql.append(" where 1=1");
            // เอาไว้ ตรวจสอบตอนสร้าง ข้อมูล tab แรก
            if (stmp != null) {
                sql.append(" AND r.stmp = '").append(stmp).append("'");
            }
            if (status == 1) { // จ่ายแล้ว
                sql.append(" AND st.stmp IS NOT null");
            } else if (status == 2) { // ยังไม่ได้จ่าย
                sql.append(" AND st.stmp IS null");
            }
            sql.append(" group by decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n),s.stmp,r.stmp,st.DATEOPD_MIN,st.DATEOPD_MAX,st.D_SENDDATE_MIN,st.D_SENDDATE_MAX");
            sql.append(" ,St.NUMBER_OF_TRANSFER,St.DATE_OF_TRANSFER,St.CREATE_DATE,st.PAYMENT,St.TOTAL_CASE");
            sql.append(" order by decode(s.stmp,null,null,'20'||s.stmp||'-'||s.stmp_n),s.stmp,r.stmp");

            System.out.println("sql clearinghouse ::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }
            pstm = conn.prepareStatement(sql.toString());

            rs = pstm.executeQuery();

            listpayment = new ArrayList<ObjPaymentResult>();

            while (rs.next()) {
                listpayment.add(getSetObject(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG("ClearingHouse " + e.getMessage(), 0);
        } finally {
            if (pstm != null) {
                pstm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listpayment;
    }

    public List<ObjPaymentResult> getPaymentResult_individual(String stmp, int status) throws SQLException {
        List<ObjPaymentResult> listpayment = null;        
        try {
            listpayment = new ArrayList<ObjPaymentResult>();

            /*
             *** ใช้ 2 query เพราะ เราจะเพิ่ม ข้อมูลเข้า list เดียวกันเลย สร้าง 2 query 
             เพราะ คนละเงื่อนไข
             */
            // ##################### งวดพิเศษ ปลายปี จะเป็น xxxx00-0 ##############
            // ******** เพิ่มเข้า list เดียวกัน เลย
            sql = new StringBuilder();
            sql.append(QUERY_INDV_JOIN_PAYMENT);
            sql.append(" WHERE 1 = 1");
            if (status == 1) { // จ่ายแล้ว
                sql.append(" AND st.stmp IS NOT null");
            } else if (status == 2) { // ยังไม่ได้จ่าย
                sql.append(" AND st.stmp IS null");
            }
            sql.append(" AND approved <> '"+IndividualDAO.APPROVED_INDV+"' ");
            sql.append(" AND DUP is null ");
            sql.append(" and r.stmp in (" + INDV_SPECIAL_57 + ")");
            sql.append(" GROUP BY r.stmp,");
            sql.append(" st.stmp,");
            sql.append(" st.DATEOPD_MIN,");
            sql.append(" st.DATEOPD_MAX,");
            sql.append(" st.D_SENDDATE_MIN,");
            sql.append(" st.D_SENDDATE_MAX,");
            sql.append(" st.NUMBER_OF_TRANSFER,");
            sql.append(" st.DATE_OF_TRANSFER,");
            sql.append(" st.CREATE_DATE,");
            sql.append(" st.PAYMENT,");
            sql.append(" st.TOTAL_CASE");
            sql.append(" ORDER BY st.stmp, r.stmp");
            System.out.println("sql (201400-1) ::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY (201400-1) ::==" + sql.toString(), 1);
            }
            pstm = conn.prepareStatement(sql.toString());
            rs = pstm.executeQuery();
            while (rs.next()) {
                listpayment.add(getSetObject(rs));
            }
            // ##################### งวดพิเศษ ปลายปี จะเป็น xxxx00-0 ##############

            // ##################### น้อยกว่า 201406-1 ##############
            // ******** เพิ่มเข้า list เดียวกัน เลย
            sql = new StringBuilder();
            sql.append(QUERY_INDV_JOIN_PAYMENT);
            sql.append(" WHERE 1 = 1");
            if (status == 1) { // จ่ายแล้ว
                sql.append(" AND st.stmp IS NOT null");
            } else if (status == 2) { // ยังไม่ได้จ่าย
                sql.append(" AND st.stmp IS null");
            }
            sql.append(" and r.stmp in (" + INDV_57 + ")");
            sql.append(" GROUP BY r.stmp,");
            sql.append(" st.stmp,");
            sql.append(" st.DATEOPD_MIN,");
            sql.append(" st.DATEOPD_MAX,");
            sql.append(" st.D_SENDDATE_MIN,");
            sql.append(" st.D_SENDDATE_MAX,");
            sql.append(" st.NUMBER_OF_TRANSFER,");
            sql.append(" st.DATE_OF_TRANSFER,");
            sql.append(" st.CREATE_DATE,");
            sql.append(" st.PAYMENT,");
            sql.append(" st.TOTAL_CASE");
            sql.append(" ORDER BY st.stmp, r.stmp");
            System.out.println("sql (201403 - 201406) ::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY (201403 - 201406) ::==" + sql.toString(), 1);
            }
            pstm = conn.prepareStatement(sql.toString());
            rs = pstm.executeQuery();
            while (rs.next()) {
                listpayment.add(getSetObject(rs));
            }
            // ##################### น้อยกว่า 201406-1 ##############

            // ##################### มากกว่า 201406-1 ##############
            // ******** เพิ่มเข้า list เดียวกัน เลย
            sql = new StringBuilder();
            sql.append(QUERY_INDV_JOIN_PAYMENT);
            sql.append(" WHERE 1 = 1");
            sql.append(" and r.stmp in (" + INDV_57_DUPISNULL + ")");

            sql.append(" and dup is null"); // ความแตกต่าง

            if (status == 1) { // จ่ายแล้ว
                sql.append(" AND st.stmp IS NOT null");
            } else if (status == 2) { // ยังไม่ได้จ่าย
                sql.append(" AND st.stmp IS null");
            }
            sql.append(" GROUP BY r.stmp,");
            sql.append(" st.stmp,");
            sql.append(" st.DATEOPD_MIN,");
            sql.append(" st.DATEOPD_MAX,");
            sql.append(" st.D_SENDDATE_MIN,");
            sql.append(" st.D_SENDDATE_MAX,");
            sql.append(" st.NUMBER_OF_TRANSFER,");
            sql.append(" st.DATE_OF_TRANSFER,");
            sql.append(" st.CREATE_DATE,");
            sql.append(" st.PAYMENT,");
            sql.append(" st.TOTAL_CASE");
            sql.append(" ORDER BY st.stmp, r.stmp");
            System.out.println("sql (201407,201408) ::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY (201407,201408) ::==" + sql.toString(), 1);
            }
            pstm = conn.prepareStatement(sql.toString());
            rs = pstm.executeQuery();
            while (rs.next()) {
                listpayment.add(getSetObject(rs));
            }
            // ##################### มากกว่า 201406-1 ##############

            // ##################### มากกว่า 201406-1 2015##############
            // ******** เพิ่มเข้า list เดียวกัน เลย
            sql = new StringBuilder();
            sql.append(QUERY_INDV_JOIN_PAYMENT);
            sql.append(" WHERE 1 = 1");
            if (status == 1) { // จ่ายแล้ว
                sql.append(" AND st.stmp IS NOT null");
            } else if (status == 2) { // ยังไม่ได้จ่าย
                sql.append(" AND st.stmp IS null");
            }
            sql.append(" AND r.stmp NOT IN");
            sql.append(" ('201402-1','201403-1','201404-1','201404-2','201405-1','201406-1','201407-1','201408-1','201400-0')");
            sql.append(" and approved <> '").append(IndividualDAO.APPROVED_INDV).append("' ");
            sql.append(" GROUP BY r.stmp,");
            sql.append(" st.stmp,");
            sql.append(" st.DATEOPD_MIN,");
            sql.append(" st.DATEOPD_MAX,");
            sql.append(" st.D_SENDDATE_MIN,");
            sql.append(" st.D_SENDDATE_MAX,");
            sql.append(" st.NUMBER_OF_TRANSFER,");
            sql.append(" st.DATE_OF_TRANSFER,");
            sql.append(" st.CREATE_DATE,");
            sql.append(" st.PAYMENT,");
            sql.append(" st.TOTAL_CASE");
            sql.append(" ORDER BY st.stmp, r.stmp");

            System.out.println("sql 2015 ::==" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY (2015) ::==" + sql.toString(), 1);
            }
            pstm = conn.prepareStatement(sql.toString());
            rs = pstm.executeQuery();
            while (rs.next()) {
                listpayment.add(getSetObject(rs));
            }
            // ##################### มากกว่า 201406-1 2015##############
        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG("Individual " + e.getMessage(), 0);
        } finally {
            if (pstm != null) {
                pstm.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listpayment;
    }

    private ObjPaymentResult getSetObject(ResultSet rs) throws SQLException {
        ObjPaymentResult objPayresult = new ObjPaymentResult();
        objPayresult.setStmp_report(rs.getString("stmp_report"));  //stmp_report
        objPayresult.setDateopd_min(rs.getString("DATEOPD_MIN"));
        objPayresult.setDateopd_max(rs.getString("DATEOPD_MAX"));
        objPayresult.setD_senddate_min(rs.getString("D_SENDDATE_MIN"));
        objPayresult.setD_senddate_max(rs.getString("D_SENDDATE_MAX"));
        objPayresult.setTotal_visit(rs.getString("TOTALALL_VISIT"));
        objPayresult.setPayment_money(rs.getString("payment"));
        objPayresult.setDate_of_tranfer(rs.getString("date_of_transfer"));
        objPayresult.setNumber_of_tranfer(rs.getString("number_of_transfer"));
        objPayresult.setCreate_date(rs.getString("create_date"));
        objPayresult.setStatus(rs.getString("status"));  //status
        return objPayresult;
    }
    // ################################## ปีงบประมาณ 2558 ###########################
    // ################################## ปีงบประมาณ 2558 ###########################
}
