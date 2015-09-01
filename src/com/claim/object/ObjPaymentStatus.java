/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.object;

import com.claim.support.StringOpUtil;

/**
 *
 * @author poon__000
 */
public class ObjPaymentStatus {
    
    private int payment_type;
    private String stmp;
    private String date_opd;
    private int payment_status;  // 0 = all , 1= true , 2 = false
    private String number_of_transfer;

    public ObjPaymentStatus() {
    }

    public ObjPaymentStatus(int payment_type) {
        this.payment_type = payment_type;
    }

    public int getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(int payment_type) {
        this.payment_type = payment_type;
    }

    public String getStmp() {
        return stmp;
    }

    public void setStmp(String stmp) {
        this.stmp = stmp;
    }

    public String getDate_opd() {
        return date_opd;
    }

    public void setDate_opd(String date_opd) {
        this.date_opd = date_opd;
    }

    public String getNumber_of_transfer() {
        return number_of_transfer;
    }

    public void setNumber_of_transfer(String number_of_transfer) {
        this.number_of_transfer = number_of_transfer;
    }

    public int getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(int payment_status) {
        this.payment_status = payment_status;
    }

}
