/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.claim.object;

import com.claim.support.StringOpUtil;

/**
 *
 * @author Poolsawat.a
 */
public class ObjRptNoniSum {
    private String hcode;
    private String hcodename;
    private int count_visit;
    private double sum_chrg_middle_priced_items;
    private double sum_chrg_other;
    private double sum_chrg_total;
    private double sum_paid_middle_priced_items;
    private double sum_paid_other;
    private double sum_paid_total;

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

    public int getCount_visit() {
        return count_visit;
    }

    public void setCount_visit(int count_visit) {
        this.count_visit = count_visit;
    }

    public double getSum_chrg_middle_priced_items() {
        return sum_chrg_middle_priced_items;
    }

    public void setSum_chrg_middle_priced_items(double sum_chrg_middle_priced_items) {
        this.sum_chrg_middle_priced_items = sum_chrg_middle_priced_items;
    }

    public double getSum_chrg_other() {
        return sum_chrg_other;
    }

    public void setSum_chrg_other(double sum_chrg_other) {
        this.sum_chrg_other = sum_chrg_other;
    }

    public double getSum_chrg_total() {
        return sum_chrg_total;
    }

    public void setSum_chrg_total(double sum_chrg_total) {
        this.sum_chrg_total = sum_chrg_total;
    }

    public double getSum_paid_middle_priced_items() {
        return sum_paid_middle_priced_items;
    }

    public void setSum_paid_middle_priced_items(double sum_paid_middle_priced_items) {
        this.sum_paid_middle_priced_items = sum_paid_middle_priced_items;
    }

    public double getSum_paid_other() {
        return sum_paid_other;
    }

    public void setSum_paid_other(double sum_paid_other) {
        this.sum_paid_other = sum_paid_other;
    }

    public double getSum_paid_total() {
        return sum_paid_total;
    }

    public void setSum_paid_total(double sum_paid_total) {
        this.sum_paid_total = sum_paid_total;
    }
    
}
