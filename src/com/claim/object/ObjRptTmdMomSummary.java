/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.object;

import com.claim.support.StringOpUtil;

/**
 *
 * @author Poolsawat
 */
public class ObjRptTmdMomSummary {
    private String hcode;
    private String hcodename;
    private String hmain;
    private String hmainname;
    private int count_in_hosp;
    private double sum_in_hosp;
    private int count_out_hosp;
    private double sum_out_hosp;
    private double sum_totalpay;

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

    public int getCount_in_hosp() {
        return count_in_hosp;
    }

    public void setCount_in_hosp(int count_in_hosp) {
        this.count_in_hosp = count_in_hosp;
    }

    public double getSum_in_hosp() {
        return sum_in_hosp;
    }

    public void setSum_in_hosp(double sum_in_hosp) {
        this.sum_in_hosp = sum_in_hosp;
    }

    public int getCount_out_hosp() {
        return count_out_hosp;
    }

    public void setCount_out_hosp(int count_out_hosp) {
        this.count_out_hosp = count_out_hosp;
    }

    public double getSum_out_hosp() {
        return sum_out_hosp;
    }

    public void setSum_out_hosp(double sum_out_hosp) {
        this.sum_out_hosp = sum_out_hosp;
    }

    public double getSum_totalpay() {
        return sum_totalpay;
    }

    public void setSum_totalpay(double sum_totalpay) {
        this.sum_totalpay = sum_totalpay;
    }
    
}
