/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.dao;

import com.claim.connection.ConnectionDB;
import com.claim.support.Console;
import com.claim.constants.ConstantMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author poon__000
 */
public class Center16AndChulaDAO {

    Connection conn = null;
    StringBuilder sql = null;
    PreparedStatement pstm = null;
    ResultSet rs = null;

    private Connection getConOracle() throws SQLException {
        Connection inConn = null;
        try {
            ConnectionDB connec = new ConnectionDB("True", "", "", "", "", "", "", "1");
            inConn = connec.getConnectionInf();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inConn;
    }

    public void openConnection() {
        try {
            this.conn = getConOracle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (this.conn != null) {
                this.conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet genReportChula(String stmp) {
        try {

            sql = new StringBuilder();
            sql.append(" select pid,hn,pname,hcode,hmain||': '||hmainname as hmain");
            sql.append(" ,dateopd");// -- วันที่รับบริการ
            sql.append(" ,to_char(dateopd,'dd monyyyy','NLS_CALENDAR=''Thai Buddha''') as dateopd_th");// -- วันที่รับบริการ
            sql.append(" ,pdxcode");// -- วินิจฉัยโรคหลัก
            sql.append(" ,chrg_car,chrg_rehab_inst,chrg_ophc,chrg_197,chrg_stditem,chrg_other,chrg_total");
            sql.append(" ,paid_car,paid_rehab_inst,paid_ophc");
            sql.append(" ,(paid_car+paid_rehab_inst+paid_ophc) paid_car_rehabinst_ophc_total");
            sql.append(" ,paid_197,paid_stditem,paid_other");
            sql.append(" ,(paid_197+paid_stditem+paid_other)paid_197_stditem_other_total");
            sql.append(" ,0 PAID_CAL_POINT");// -- คำนวนจ่ายตาม point (user กำหนดเอง)
            sql.append(" ,paid_total paid_total");// -- รวมปรับจ่ายตามเงื่อนไข
            sql.append(" ,0 COMPENSATION_FEE_TOTAL");// -- รวมจ่ายชดเชยค่าบริการ(บาท) (user กำหนดเอง)
            sql.append(" ,remark");//--หมายเหตุ 
            sql.append(" ,txid");  // modifide by 2014-08-04
                        
             /*
             modified 01-07-2015 by poolsawat
             */
            sql.append(" ,(select INVOICE_NO from opbkk_service s where s.txid = r.txid) as INVOICE_NO");
            /*
             modified 01-07-2015 by poolsawat
             */
            //--,STMP
            //--,STMP_DESC
            //--,(select rh.STMP_DESC from rpt_header rh where rh.stmp=r.stmp and rh.OPTYPE='AE')STMP_DESC
            sql.append(" from RPT_OPBKK_HC16CHULA r");
            sql.append(" where 1=1");
            sql.append(" and stmp=? ");
            sql.append(" and hcode='13756'"); //-- รพ.จุฬาฯ
            sql.append(" order by dateopd,pid");

            System.out.println("sql: " + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstm = conn.prepareStatement(sql.toString());
            pstm.setString(1, stmp);
            rs = pstm.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public ResultSet genReportHC16(String stmp) {
        try {
            sql = new StringBuilder();
            sql.append(" select pid,hn,pname,hcode,hmain||': '||hmainname as hmain");
            sql.append(" ,dateopd");// -- วันที่รับบริการ
            sql.append(" ,to_char(dateopd,'dd monyyyy','NLS_CALENDAR=''Thai Buddha''') as dateopd_th");// -- วันที่รับบริการ
            sql.append(" ,pdxcode");// -- วินิจฉัยโรคหลัก
            sql.append(" ,chrg_car,chrg_rehab_inst,chrg_ophc,chrg_197,chrg_stditem,chrg_other,chrg_total");
            sql.append(" ,paid_car,paid_rehab_inst,paid_ophc");
            sql.append(" ,(paid_car+paid_rehab_inst+paid_ophc) paid_car_rehabinst_ophc_total");
            sql.append(" ,paid_197,paid_stditem,paid_other");
            sql.append(" ,(paid_197+paid_stditem+paid_other)paid_197_stditem_other_total");
            sql.append(" ,0 PAID_CAL_POINT"); // -- คำนวนจ่ายตาม point (user กำหนดเอง)
            sql.append(" ,paid_total paid_total");// -- รวมปรับจ่ายตามเงื่อนไข
            sql.append(" ,0 COMPENSATION_FEE_TOTAL");// -- รวมจ่ายชดเชยค่าบริการ(บาท) (user กำหนดเอง)
            sql.append(" ,remark");// -- หมายเหตุ
            sql.append(" ,txid");  // modifide by 2014-08-04

            /*
             modified 01-07-2015 by poolsawat
             */
            sql.append(" ,(select INVOICE_NO from opbkk_service s where s.txid = r.txid) as INVOICE_NO");
            /*
             modified 01-07-2015 by poolsawat
             */

            //--,STMP
            //--,STMP_DESC
            //--,(select rh.STMP_DESC from rpt_header rh where rh.stmp=r.stmp and rh.OPTYPE='AE')STMP_DESC
            sql.append(" from RPT_OPBKK_HC16CHULA r");
            sql.append(" where 1=1");
            sql.append(" and stmp=? ");
            sql.append(" and hcode='13661'");// -- ศูนย์ 16
            sql.append(" order by dateopd,pid");

            System.out.println("sql: " + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstm = conn.prepareStatement(sql.toString());
            pstm.setString(1, stmp);
            rs = pstm.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public String genDateOpd(String stmp) {
        String dateAD = null;
        try {
            sql = new StringBuilder();
//            sql.append(" select ");
//            sql.append(" case when to_char(min(r.dateopd),'yyyy')=to_char(max(r.dateopd),'yyyy') then");
//            sql.append(" substr(to_char(min(r.dateopd),'dd monyyyy','NLS_CALENDAR=''Thai Buddha'''),4,4)||'-'||");
//            sql.append(" substr(to_char(max(r.dateopd),'dd monyyyy','NLS_CALENDAR=''Thai Buddha'''),4,9)");
//            sql.append(" else  ");
//            sql.append(" substr(to_char(min(r.dateopd),'dd monyyyy','NLS_CALENDAR=''Thai Buddha'''),4,9)||'-'||");
//            sql.append(" substr(to_char(max(r.dateopd),'dd monyyyy','NLS_CALENDAR=''Thai Buddha'''),4,9)");
//            sql.append(" end dateopd");
//            sql.append("  from RPT_OPBKK_HC16CHULA r");
//            sql.append(" where 1=1");
//            sql.append(" and r.stmp=?");

            sql.append(" select distinct");
            sql.append(" min(r.dateopd),max(r.dateopd),"); //for min max dateopd            
            sql.append(" case when to_char(min(r.dateopd),'mmyyyy')=to_char(max(r.dateopd),'mmyyyy') then ");//-- MM YYYY
            sql.append(" to_char(max(r.dateopd),'month yyyy','NLS_CALENDAR=''Thai Buddha''')");
            sql.append(" when to_char(min(r.dateopd),'yyyy')=to_char(max(r.dateopd),'yyyy') then ");//-- fromMM-toMM YYYY
            sql.append(" to_char(min(r.dateopd),'month','NLS_CALENDAR=''Thai Buddha''')||'-'||");
            sql.append(" to_char(max(r.dateopd),'month','NLS_CALENDAR=''Thai Buddha''')||");
            sql.append(" to_char(min(r.dateopd),'yyyy','NLS_CALENDAR=''Thai Buddha''') ");
            sql.append(" else ");//-- fromMM fromYYYY-toMM toYYYY
            sql.append(" to_char(min(r.dateopd),'month yyyy','NLS_CALENDAR=''Thai Buddha''')||'-'||");
            sql.append(" to_char(max(r.dateopd),'month yyyy','NLS_CALENDAR=''Thai Buddha''')");
            sql.append(" end dateopd");
            sql.append(" from RPT_OPBKK_HC16CHULA r");
            sql.append(" where 1=1");
            sql.append(" and r.stmp in(?)");

            System.out.println("sql: dateOPD: " + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }

            pstm = conn.prepareStatement(sql.toString());
            pstm.setString(1, stmp);
            rs = pstm.executeQuery();
            while (rs.next()) {
                dateAD = rs.getString("dateopd");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateAD;
    }

    public String getMonthPayment(String stmp) throws SQLException {
        String monthBetweenPayment = null;
        try {
            sql = new StringBuilder();
            sql.append(" SELECT STMP_DESC FROM RPT_HEADER");
            sql.append(" WHERE stmp = '").append(stmp).append("'");
            sql.append(" AND OPTYPE = 'HC'");

            System.out.println("sb: " + sql.toString());

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
}
