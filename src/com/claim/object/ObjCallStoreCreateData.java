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
public class ObjCallStoreCreateData {

    private String tablename;
    private String opd_date_start;
    private String opd_date_stop;
    private String opd_send_start;
    private String opd_send_stop;
    private String stmplist;
    private String stmp;
    private String stmp_round;
    private Integer index_table;
    private String stmp_no;
    private String pay_rate;
    private Integer opae_option;
    private String indv_late;
    private String stmpyyyymm_no;
    private String stmpyyyymmno;
    private int process_create; // เก็บ รูปแบบการออกสร้างข้อมูล 0 = ธรรมดา กับ  1 = ปลายปี
    private int refer_option; // กรณีสร้าง ข้อมูล กวาด ข้อมูล กับ ไม่กวาดข้อมูล 1= เรียกหมด 2 = แยกเรียก
    private int group_report_type; // 1 = {} , 2 = {vajira_11535,vajira_rf,vajira_hc}
    
    private int global_budget = 0;
    
    
    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String getOpd_date_start() {
        return StringOpUtil.removeNull(opd_date_start);
    }

    public void setOpd_date_start(String opd_date_start) {
        this.opd_date_start = opd_date_start;
    }

    public String getOpd_date_stop() {
        return StringOpUtil.removeNull(opd_date_stop);
    }

    public void setOpd_date_stop(String opd_date_stop) {
        this.opd_date_stop = opd_date_stop;
    }

    public String getOpd_send_start() {
        return StringOpUtil.removeNull(opd_send_start);
    }

    public void setOpd_send_start(String opd_send_start) {
        this.opd_send_start = opd_send_start;
    }

    public String getOpd_send_stop() {
        return StringOpUtil.removeNull(opd_send_stop);
    }

    public void setOpd_send_stop(String opd_send_stop) {
        this.opd_send_stop = opd_send_stop;
    }

    public String getStmplist() {
        return StringOpUtil.removeNull(stmplist);
    }

    public void setStmplist(String stmplist) {
        this.stmplist = stmplist;
    }

    public String getStmp() {
        return StringOpUtil.removeNull(stmp);
    }

    public void setStmp(String stmp) {
        this.stmp = stmp;
    }

    public String getStmp_round() {
        return StringOpUtil.removeNull(stmp_round);
    }

    public void setStmp_round(String stmp_round) {
        this.stmp_round = stmp_round;
    }

    public Integer getIndex_table() {
        return index_table;
    }

    public void setIndex_table(Integer index_table) {
        this.index_table = index_table;
    }

    public String getStmp_no() {
        return StringOpUtil.removeNull(stmp_no);
    }

    public void setStmp_no(String stmp_no) {
        this.stmp_no = stmp_no;
    }

    public String getPay_rate() {
        return StringOpUtil.removeNull(pay_rate);
    }

    public void setPay_rate(String pay_rate) {
        this.pay_rate = pay_rate;
    }

    public Integer getOpae_option() {
        return opae_option;
    }

    public void setOpae_option(Integer opae_option) {
        this.opae_option = opae_option;
    }

    public String getIndv_late() {
        return StringOpUtil.removeNull(indv_late);
    }

    public void setIndv_late(String indv_late) {
        this.indv_late = indv_late;
    }

    public String getStmpyyyymm_no() {
        return StringOpUtil.removeNull(stmpyyyymm_no);
    }

    public void setStmpyyyymm_no(String stmpyyyymm_no) {
        this.stmpyyyymm_no = stmpyyyymm_no;
    }

    public String getStmpyyyymmno() {
        return StringOpUtil.removeNull(stmpyyyymmno);
    }

    public void setStmpyyyymmno(String stmpyyyymmno) {
        this.stmpyyyymmno = stmpyyyymmno;
    }

    public int getProcess_create() {
        return process_create;
    }

    public void setProcess_create(int process_create) {
        this.process_create = process_create;
    }

    public int getRefer_option() {
        return refer_option;
    }

    public void setRefer_option(int refer_option) {
        this.refer_option = refer_option;
    }

    public int getGlobal_budget() {
        return global_budget;
    }

    public void setGlobal_budget(int global_budget) {
        this.global_budget = global_budget;
    }

    public int getGroup_report_type() {
        return group_report_type;
    }

    public void setGroup_report_type(int group_report_type) {
        this.group_report_type = group_report_type;
    }
    
    @Override
    public String toString() {
        return "ค่าที่ส่งเข้าไป ประมวลผล  [" +
                //"tablename=" + tablename +
                ", opd_date_start=" + opd_date_start +
                ", opd_date_stop=" + opd_date_stop +
                ", opd_send_start=" + opd_send_start +
                ", opd_send_stop=" + opd_send_stop +
                //", stmplist=" + stmplist +
                ", stmp =" + stmp +"-"+stmp_round+
                ", stmp_round=" + stmp_round +
                //", index_table=" + index_table +
                ", stmp_no =" + stmp_no + 
                ", pay_rate=" + pay_rate +
                ", opae_option=" + opae_option +
                ", indv_late=" + indv_late +
                //", stmpyyyymm_no=" + stmpyyyymm_no +
                //", stmpyyyymmno=" + stmpyyyymmno +
                //", process_create=" + process_create + 
                ", refer_option=" + refer_option +
                ", global_budget =" + global_budget +
                ']';
    }
    
    

}
