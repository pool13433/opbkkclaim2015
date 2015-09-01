/*
 * To change this license header; choose License Headers in Project Properties.
 * To change this template file; choose Tools | Templates
 * and open the template in the editoprivate String 
 */
package com.claim.object;

import com.claim.support.StringOpUtil;

/**
 *
 * @author Poolsawat
 */
public class ObjRptTmdMomDetail {

    private String txid;
    private String hcode;
    private String hcodename;
    private String hmain;
    private String hmainname;
    private String pid;
    private String pname;
    private String hn;
    private String sex_name;
    private int sex;
    private int age_year;
    private String dateopd;
    private String dateopd_th;
    private String item_type;
    private String item_code;
    private String item_desc;
    private int qty;
    private double unitprice;
    private double totalchrg;
    private double point;
    private double ratepay;
    private double totalpay;
    private String stmp;
    private String rpt_date;
    private String remark;
    private String case_place;
    
    private String invoice_no;
    
    public String getTxid() {
        return StringOpUtil.removeNull(txid);
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getHcode() {
        return StringOpUtil.removeNull(hcode);
    }

    public void setHcode(String hcode) {
        this.hcode = hcode;
    }

    public String getHcodename() {
        return StringOpUtil.removeNull(hcodename);
    }

    public void setHcodename(String hcodename) {
        this.hcodename = hcodename;
    }

    public String getHmain() {
        return StringOpUtil.removeNull(hmain);
    }

    public void setHmain(String hmain) {
        this.hmain = hmain;
    }

    public String getHmainname() {
        return StringOpUtil.removeNull(hmainname);
    }

    public void setHmainname(String hmainname) {
        this.hmainname = hmainname;
    }

    public String getPid() {
        return StringOpUtil.removeNull(pid);
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return StringOpUtil.removeNull(pname);
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getHn() {
        return StringOpUtil.removeNull(hn);
    }

    public void setHn(String hn) {
        this.hn = hn;
    }

    public String getSex_name() {
        return StringOpUtil.removeNull(sex_name);
    }

    public void setSex_name(String sex_name) {
        this.sex_name = sex_name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

  

    public int getAge_year() {
        return age_year;
    }

    public void setAge_year(int age_year) {
        this.age_year = age_year;
    }

    public String getDateopd() {
        return StringOpUtil.removeNull(dateopd);
    }

    public void setDateopd(String dateopd) {
        this.dateopd = dateopd;
    }

    public String getItem_type() {
        return StringOpUtil.removeNull(item_type);
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getItem_code() {
        return StringOpUtil.removeNull(item_code);
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public String getItem_desc() {
        return StringOpUtil.removeNull(item_desc);
    }

    public void setItem_desc(String item_desc) {
        this.item_desc = item_desc;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(double unitprice) {
        this.unitprice = unitprice;
    }

    public double getTotalchrg() {
        return totalchrg;
    }

    public void setTotalchrg(double totalchrg) {
        this.totalchrg = totalchrg;
    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }

    public double getRatepay() {
        return ratepay;
    }

    public void setRatepay(double ratepay) {
        this.ratepay = ratepay;
    }

    public double getTotalpay() {
        return totalpay;
    }

    public void setTotalpay(double totalpay) {
        this.totalpay = totalpay;
    }

    public String getStmp() {
        return StringOpUtil.removeNull(stmp);
    }

    public void setStmp(String stmp) {
        this.stmp = stmp;
    }

    public String getRpt_date() {
        return StringOpUtil.removeNull(rpt_date);
    }

    public void setRpt_date(String rpt_date) {
        this.rpt_date = rpt_date;
    }

    public String getRemark() {
        return StringOpUtil.removeNull(remark);
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDateopd_th() {
        return StringOpUtil.removeNull(dateopd_th);
    }

    public void setDateopd_th(String dateopd_th) {
        this.dateopd_th = dateopd_th;
    }

    public String getCase_place() {
        return StringOpUtil.removeNull(case_place);
    }

    public void setCase_place(String case_place) {
        this.case_place = case_place;
    }

    public String getInvoice_no() {
        return StringOpUtil.removeNull(invoice_no);
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }
    
    
}
