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
public class ObjRptIndividualSum {
        private int no;
        private String hcode;
        private String hcodename;
        private double intime_man;
        private double intime_visit;
        private double intime_totalreimburse;
        private double late_man;
        private double late_visit;
        private double late_totalreimburse;
        private double totalall_man;
        private double totalall_visit;
        private double totalall_totalreimburse;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
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

    public double getIntime_man() {
        return intime_man;
    }

    public void setIntime_man(double intime_man) {
        this.intime_man = intime_man;
    }

    public double getIntime_visit() {
        return intime_visit;
    }

    public void setIntime_visit(double intime_visit) {
        this.intime_visit = intime_visit;
    }

    public double getIntime_totalreimburse() {
        return intime_totalreimburse;
    }

    public void setIntime_totalreimburse(double intime_totalreimburse) {
        this.intime_totalreimburse = intime_totalreimburse;
    }

    public double getLate_man() {
        return late_man;
    }

    public void setLate_man(double late_man) {
        this.late_man = late_man;
    }

    public double getLate_visit() {
        return late_visit;
    }

    public void setLate_visit(double late_visit) {
        this.late_visit = late_visit;
    }

    public double getLate_totalreimburse() {
        return late_totalreimburse;
    }

    public void setLate_totalreimburse(double late_totalreimburse) {
        this.late_totalreimburse = late_totalreimburse;
    }

    public double getTotalall_man() {
        return totalall_man;
    }

    public void setTotalall_man(double totalall_man) {
        this.totalall_man = totalall_man;
    }

    public double getTotalall_visit() {
        return totalall_visit;
    }

    public void setTotalall_visit(double totalall_visit) {
        this.totalall_visit = totalall_visit;
    }

    public double getTotalall_totalreimburse() {
        return totalall_totalreimburse;
    }

    public void setTotalall_totalreimburse(double totalall_totalreimburse) {
        this.totalall_totalreimburse = totalall_totalreimburse;
    }
        
}