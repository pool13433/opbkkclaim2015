/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.dao;

import com.claim.object.ObjRptChula;
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
public class Center16AndChula2015DAO {

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

    public List<ObjRptChula> getListChulaDetail(String stmp, String hcode) {
        List<ObjRptChula> listData = null;
        ObjRptChula data = null;
        try {
            sql = new StringBuilder();
            sql.append(" ");
            sql.append(" SELECT pid,hn,pname,hcode,hmain || ': ' || hmainname AS hmain,");
            sql.append(" dateopd,TO_CHAR (dateopd, 'dd monyyyy', 'NLS_CALENDAR=''Thai Buddha''') AS dateopd_th,");
            sql.append(" pdxcode,chrg_car,chrg_rehab_inst,chrg_ophc,");
            sql.append(" (chrg_car + chrg_rehab_inst + chrg_ophc) chrg_car_rehabinst_ophc_total,");
            sql.append(" chrg_197 as chrg_202,chrg_stditem,chrg_other,");
            sql.append(" (chrg_197 +chrg_stditem+chrg_other)as chrg_total,");
            sql.append(" (chrg_car + chrg_rehab_inst + chrg_ophc+ chrg_197+chrg_stditem+chrg_other+chrg_total) sum_chrg,");
            sql.append(" paid_car,paid_rehab_inst,paid_ophc,");
            sql.append(" (paid_car + paid_rehab_inst + paid_ophc) paid_car_rehabinst_ophc_total,");
            sql.append(" paid_197 as paid_202,paid_stditem,paid_other,");
            sql.append(" (paid_197 + paid_stditem + paid_other) paid_202_stditem_other_total,");
            sql.append(" null PAID_CAL_POINT,"); //-- ดึงจากฐานข้อมูล
            sql.append(" null PAID_CAL_POINT_TOTAL,");
            sql.append(" paid_total paid_total,");
            sql.append(" null COMPENSATION_FEE_TOTAL,");
            sql.append(" remark,txid");

            /*
             modified 01-07-2015 by poolsawat
             */
            sql.append(" ,(select INVOICE_NO from opbkk_service s where s.txid = r.txid) as INVOICE_NO");
            /*
             modified 01-07-2015 by poolsawat
             */

            sql.append(" FROM RPT_OPBKK_HC16CHULA r");
            sql.append(" WHERE 1 = 1 AND stmp = ? AND hcode = ? ");
            sql.append(" ORDER BY dateopd, pid");

            System.out.println("sql :" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }
            pstm = connection.prepareStatement(sql.toString());
            pstm.setString(1, stmp);
            pstm.setString(2, hcode);
            rs = pstm.executeQuery();
            listData = new ArrayList<ObjRptChula>();
            while (rs.next()) {
                data = new ObjRptChula();
                data.setPid(rs.getString("pid"));
                data.setHn(rs.getString("hn"));
                data.setPname(rs.getString("pname"));
                data.setHcode(rs.getString("hcode"));
                data.setHmain(rs.getString("hmain"));
                data.setDateopd(rs.getString("dateopd"));
                data.setDateopd_th(rs.getString("dateopd_th"));
                data.setPdxcode(rs.getString("pdxcode"));
                data.setChrg_car(rs.getDouble("chrg_car"));
                data.setChrg_rehab_inst(rs.getDouble("chrg_rehab_inst"));
                data.setChrg_ophc(rs.getDouble("chrg_ophc"));
                data.setChrg_car_rehabinst_ophc_total(rs.getDouble("chrg_car_rehabinst_ophc_total"));
                data.setChrg_202(rs.getDouble("chrg_202"));
                data.setChrg_stditem(rs.getDouble("chrg_stditem"));
                data.setChrg_other(rs.getDouble("chrg_other"));
                data.setChrg_total(rs.getDouble("chrg_total"));
                //data.setSum_chrg(rs.getDouble("sum_chrg"));
                data.setSum_chrg(data.getChrg_car_rehabinst_ophc_total() + data.getChrg_total());

                data.setPaid_car(rs.getDouble("paid_car"));
                data.setPaid_rehab_inst(rs.getDouble("paid_rehab_inst"));
                data.setPaid_ophc(rs.getDouble("paid_ophc"));
                data.setPaid_car_rehabinst_ophc_total(rs.getDouble("paid_car_rehabinst_ophc_total"));
                data.setPaid_202(rs.getDouble("paid_202"));
                data.setPaid_stditem(rs.getDouble("paid_stditem"));
                data.setPaid_other(rs.getDouble("paid_other"));
                data.setPaid_202_stditem_other_total(rs.getDouble("paid_202_stditem_other_total"));
                data.setPaid_cal_point(rs.getDouble("PAID_CAL_POINT"));
                data.setPaid_cal_point_total(rs.getDouble("PAID_CAL_POINT_TOTAL"));
                data.setPaid_total(rs.getDouble("paid_total"));
                data.setCompensation_fee_total(rs.getDouble("COMPENSATION_FEE_TOTAL"));
                data.setRemark(rs.getString("remark"));
                data.setTxid(rs.getString("txid"));
                data.setInvoice_no(rs.getString("invoice_no"));
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
                Logger.getLogger(Center16AndChula2015DAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listData;
    }

    public String getMonthPayment(String stmp) {
        String monthBetweenPayment = "-";
        try {
            sql = new StringBuilder();
            sql.append(" SELECT STMP_DESC FROM RPT_HEADER");
            sql.append(" WHERE stmp = '").append(stmp).append("'");
            sql.append(" AND OPTYPE = 'HC'");

            System.out.println("sql :" + sql.toString());
            if (ConstantMessage.IS_SHOW_QUERY) {
                Console.LOG(" QUERY ::==" + sql.toString(), 1);
            }
            pstm = connection.prepareStatement(sql.toString());
            rs = pstm.executeQuery();
            while (rs.next()) {
                monthBetweenPayment = rs.getString("STMP_DESC");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return monthBetweenPayment;
    }
}
