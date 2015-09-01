/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.object;

import com.claim.support.StringOpUtil;

/**
 *
 * @author Poolsawat.a
 */
public class ObjRptOpae74 {

    private String pid;
    private String hcode;
    private String hcodename;
    private String p_name;
    private String hn;
    private String hmain;
    private String dateopd;
    private String dateopd_th;
    private String optype;
    private double chrg_ophc;
    private double chrg_stditem;
    private double chrg_197;
    private double chrg_car;
    private double chrg_rehab_inst;
    private double chrg_other;
    private double chrg_total;
    private double chrg_total_final;
    private double payrate;
    private double paid_ophc;
    private double paid_stditem;
    private double paid_197;
    private double paid_car;
    private double paid_rehab_inst;
    private double paid_other;
    private double paid_total;
    private double paid_total_final;
    private String txid;
    private String invoice_no;

    public String getPid() {
        return pid;
    }

    public String getHcode() {
        return hcode;
    }

    public void setHcode(String hcode) {
        this.hcode = hcode;
    }

    public String getHcodename() {
        return hcodename;
    }

    public void setHcodename(String hcodename) {
        this.hcodename = hcodename;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getHn() {
        return hn;
    }

    public void setHn(String hn) {
        this.hn = hn;
    }

    public String getHmain() {
        return hmain;
    }

    public void setHmain(String hmain) {
        this.hmain = hmain;
    }

    public String getDateopd() {
        return dateopd;
    }

    public void setDateopd(String dateopd) {
        this.dateopd = dateopd;
    }

    public String getDateopd_th() {
        return dateopd_th;
    }

    public void setDateopd_th(String dateopd_th) {
        this.dateopd_th = dateopd_th;
    }

    public String getOptype() {
        return optype;
    }

    public void setOptype(String optype) {
        this.optype = optype;
    }

    public double getChrg_ophc() {
        return chrg_ophc;
    }

    public void setChrg_ophc(double chrg_ophc) {
        this.chrg_ophc = chrg_ophc;
    }

    public double getChrg_stditem() {
        return chrg_stditem;
    }

    public void setChrg_stditem(double chrg_stditem) {
        this.chrg_stditem = chrg_stditem;
    }

    public double getChrg_197() {
        return chrg_197;
    }

    public void setChrg_197(double chrg_197) {
        this.chrg_197 = chrg_197;
    }

    public double getChrg_car() {
        return chrg_car;
    }

    public void setChrg_car(double chrg_car) {
        this.chrg_car = chrg_car;
    }

    public double getChrg_rehab_inst() {
        return chrg_rehab_inst;
    }

    public void setChrg_rehab_inst(double chrg_rehab_inst) {
        this.chrg_rehab_inst = chrg_rehab_inst;
    }

    public double getChrg_other() {
        return chrg_other;
    }

    public void setChrg_other(double chrg_other) {
        this.chrg_other = chrg_other;
    }

    public double getChrg_total() {
        return chrg_total;
    }

    public void setChrg_total(double chrg_total) {
        this.chrg_total = chrg_total;
    }

    public double getChrg_total_final() {
        return chrg_total_final;
    }

    public void setChrg_total_final(double chrg_total_final) {
        this.chrg_total_final = chrg_total_final;
    }

    public double getPayrate() {
        return payrate;
    }

    public void setPayrate(double payrate) {
        this.payrate = payrate;
    }

    public double getPaid_ophc() {
        return paid_ophc;
    }

    public void setPaid_ophc(double paid_ophc) {
        this.paid_ophc = paid_ophc;
    }

    public double getPaid_stditem() {
        return paid_stditem;
    }

    public void setPaid_stditem(double paid_stditem) {
        this.paid_stditem = paid_stditem;
    }

    public double getPaid_197() {
        return paid_197;
    }

    public void setPaid_197(double paid_197) {
        this.paid_197 = paid_197;
    }

    public double getPaid_car() {
        return paid_car;
    }

    public void setPaid_car(double paid_car) {
        this.paid_car = paid_car;
    }

    public double getPaid_rehab_inst() {
        return paid_rehab_inst;
    }

    public void setPaid_rehab_inst(double paid_rehab_inst) {
        this.paid_rehab_inst = paid_rehab_inst;
    }

    public double getPaid_other() {
        return paid_other;
    }

    public void setPaid_other(double paid_other) {
        this.paid_other = paid_other;
    }

    public double getPaid_total() {
        return paid_total;
    }

    public void setPaid_total(double paid_total) {
        this.paid_total = paid_total;
    }

    public double getPaid_total_final() {
        return paid_total_final;
    }

    public void setPaid_total_final(double paid_total_final) {
        this.paid_total_final = paid_total_final;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getInvoice_no() {
        return StringOpUtil.removeNull(invoice_no);
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }
    
}
