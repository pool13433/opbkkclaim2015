/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.object;

import com.claim.support.StringOpUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author poon__000
 */
public class OppReport {

    private Integer reportTypeId;
    private String reportTypeName;
    private String no;
    private String titleReport;
    private String month;
    private String monthFullTh;
    private String yearFull;
    private String year;
    private String serviceCode;
    private List<HospitalService> listServiceCode = new ArrayList<HospitalService>();
    private int countListServiceCode;
    private String serviceName;
    private String pathFile;
    private String yearMonth; // ex  201402
    private Integer reportType; // 0 = detail,1 = summary
    private char category; // D = deduct , P=Payment
    private char attribute; // S = Single , A = AllClinic
    private String vajira; //{11535,RE,HC,ALL}
    private String is197;
    private String title1;
    private String title2;
    private String title3;
    private Integer reportFormat; // 1 = 1sheet1file, 2 = Mutisheet1file,3 = All format (1sheet1file,Mutisheet1file)
    private String dateopd;
    private String sql_orderBy = "";
    private int for_use; // ใช้เพื่อ 0 = ทั่วไป , 1 = web site claim
    private boolean web_type5 = false; // true = all (197 + hc) , false = 197 หรือ hc
    private String budget_year;
    private String budget_date;
    private String budget_stmp;
    private String tmdServiceType; // แพทย์แผนไทย ACT = กิจกรรมนวด ประคบสมุนไพร อบสมุนไพร และจ่ายยาสมุนไพร , MOM  =กิจกรรมบริการมารดาหลังคลอด
    private String tmdTableName;
    /*
     For Individual 
     */
    private String indv_late;  // 0 = ทันเวลา ,  1 = ไม่ทันเวลา , null  = ทั้งทันเวลา และ ไม่ทันเวลา

    private String stmp;

    public Integer getReportFormat() {
        return reportFormat;
    }

    public void setReportFormat(Integer reportFormat) {
        this.reportFormat = reportFormat;
    }

    public String getTitle1() {
        return title1;
    }

    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public String getTitle3() {
        return title3;
    }

    public void setTitle3(String title3) {
        this.title3 = title3;
    }

    public char getCategory() {
        return category;
    }

    public void setCategory(char category) {
        this.category = category;
    }

    public char getAttribute() {
        return attribute;
    }

    public void setAttribute(char attribute) {
        this.attribute = attribute;
    }

    public String getIs197() {
        return is197;
    }

    public void setIs197(String is197) {
        this.is197 = is197;
    }

    public String getMonthFullTh() {
        return monthFullTh;
    }

    public void setMonthFullTh(String monthFullTh) {
        this.monthFullTh = monthFullTh;
    }

    public String getYearFull() {
        return yearFull;
    }

    public void setYearFull(String yearFull) {
        this.yearFull = yearFull;
    }

    public Integer getReportTypeId() {
        return reportTypeId;
    }

    public void setReportTypeId(Integer reportTypeId) {
        this.reportTypeId = reportTypeId;
    }

    public String getReportTypeName() {
        return reportTypeName;
    }

    public void setReportTypeName(String reportTypeName) {
        this.reportTypeName = reportTypeName;
    }

    public String getNo() {
        return StringOpUtil.removeNull(no);
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getTitleReport() {
        return titleReport;
    }

    public void setTitleReport(String titleReport) {
        this.titleReport = titleReport;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getServiceCode() {
        return StringOpUtil.removeNull(serviceCode);
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getPathFile() {
        return pathFile;
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }

    public Integer getReportType() {
        return reportType;
    }

    public void setReportType(Integer reportType) {
        this.reportType = reportType;
    }

    public String getYearMonth() {
        return StringOpUtil.removeNull(yearMonth);
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public List<HospitalService> getListServiceCode() {
        return listServiceCode;
    }

    public void setListServiceCode(List<HospitalService> listServiceCode) {
        this.listServiceCode = listServiceCode;
    }

    public String getDateopd() {
        return dateopd;
    }

    public void setDateopd(String dateopd) {
        this.dateopd = dateopd;
    }

    public String getSql_orderBy() {
        return StringOpUtil.removeNull(sql_orderBy);
    }

    public void setSql_orderBy(String sql_orderBy) {
        this.sql_orderBy = sql_orderBy;
    }

    public int getFor_use() {
        return for_use;
    }

    public void setFor_use(int for_use) {
        this.for_use = for_use;
    }

    public boolean getWeb_type5() {
        return web_type5;
    }

    public void setWeb_type5(boolean web_type5) {
        this.web_type5 = web_type5;
    }

    public int sizeListServiceCode() {
//        if (getListServiceCode().equals(null)) {
//            return 0;
//        } else {
        return getListServiceCode().size();
        //}        
    }

    public String getStmp() {
        return StringOpUtil.removeNull(stmp);
    }

    public void setStmp(String stmp) {
        this.stmp = stmp;
    }

    public String getBudget_year() {
        return StringOpUtil.removeNull(budget_year);
    }

    public void setBudget_year(String budget_year) {
        this.budget_year = budget_year;
    }

    public String getBudget_date() {
        return StringOpUtil.removeNull(budget_date);
    }

    public void setBudget_date(String budget_date) {
        this.budget_date = budget_date;
    }

    public String getBudget_stmp() {
        return StringOpUtil.removeNull(budget_stmp);
    }

    public void setBudget_stmp(String budget_stmp) {
        this.budget_stmp = budget_stmp;
    }

    public String getIndv_late() {
        return StringOpUtil.removeNull(indv_late);
    }

    public void setIndv_late(String indv_late) {
        this.indv_late = indv_late;
    }

    public String getTmdServiceType() {
        return StringOpUtil.removeNull(tmdServiceType);
    }

    public void setTmdServiceType(String tmdServiceType) {
        this.tmdServiceType = tmdServiceType;
    }

    public String getTmdTableName() {
        return tmdTableName;
    }

    public void setTmdTableName(String tmdTableName) {
        this.tmdTableName = tmdTableName;
    }

    public String getVajira() {
        return vajira;
    }

    public void setVajira(String vajira) {
        this.vajira = vajira;
    }

    
    
    
}
