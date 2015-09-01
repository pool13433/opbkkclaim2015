/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.controller;

import com.claim.connection.DBManage;
import com.claim.dao.VajiraREDao;
import com.claim.object.HospitalService;
import com.claim.object.ObjRptVajiraRfDetail;
import com.claim.object.ObjRptVajiraRfSummary;
import com.claim.object.OppReport;
import com.claim.support.Console;
import com.claim.constants.ConstantMessage;
import com.claim.constants.ConstantVariable;
import com.claim.support.DateUtil;
import com.claim.support.FileUtil;
import com.claim.object.ProgrameStatus;
import com.claim.support.StringOpUtil;
import com.claim.xls.ExcellBaseUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author POOL_LAPTOP
 */
public class VajiraRFController extends ExcellBaseUtil {

    //############## POI ##############
    FileInputStream file = null;
    FileOutputStream out = null;
    HSSFCell cell = null;
    HSSFRow row = null;
    //############## POI ##############

    //############# JDBC ###############
    Connection connection = null;
    //############# JDBC ###############

    String EXCELL_HEADER1 = null;
    String EXCELL_HEADER2 = null;
    String EXCELL_HOSPITAL = null;
    static String HEADER_DETAIL = "รายละเอียดรายงานการจ่ายชดเชยค่าบริการกรณี Opศูนย์บริการสาธารณสุข ส่งต่อคณะแพทย์ศาสตร์วชิรพยาบาล ปีงบประมาณ {YEAR}";
    static String HEADER_SUMMARY = "สรุปรายงานการจ่ายชดเชยค่าบริการกรณี Opศูนย์บริการสาธารณสุข ส่งต่อคณะแพทย์ศาสตร์วชิรพยาบาล ปีงบประมาณ {YEAR}";
    static String SUB_HEADER = "เดือน/ปี {DATE} งวดการจ่ายเงิน {STMP}";

    static String FILE_NAME_DETAIL = "DetailPay_RF1_";
    static String FILE_NAME_SUM = "SumPay_RF1_";
    static String FILE_NAME_SUM_REPORT = "SumReport_RF1_";
    
    public static void main(String[] args) {
        VajiraRFController vajira = new VajiraRFController();
        OppReport report = new OppReport();
        report.setYearMonth("201507");
        report.setNo("1");
        report.setPathFile("C:\\excell\\claim\\");
        report.setServiceCode("13814");
        report.setServiceName("TEST 13814 TEST");
        report.setStmp("201507-1");

//        List<HospitalService> serviceList = new ArrayList<>();
//        serviceList.add(new HospitalService("13814", "TEST TEST"));
//        report.setListServiceCode(serviceList);
        vajira.hcREByHcode(report);
        vajira.hcRFSummary(report);

    }

    public ProgrameStatus hcRfDetail(OppReport report) {
        ProgrameStatus programeStatus = new ProgrameStatus();
        int col_last = 20;
        int row_start = 7; // index row
        int row_formula_start = row_start + 1;
        List<ObjRptVajiraRfDetail> listData = new ArrayList<ObjRptVajiraRfDetail>();

        String stmp = StringOpUtil.removeNull(report.getYearMonth()) + "-" + report.getNo();
        String pathDirectory = report.getPathFile() + "" + File.separator + "" + stmp + "" + File.separator + "";
        try {
            connection = new DBManage().open();

            VajiraREDao vajiraREDao = new VajiraREDao();
            vajiraREDao.setConnection(connection);

            //readTemplate 
            file = new FileInputStream(new File("." + File.separator + "xls" + File.separator + "HC_RF_DETAIL.xls"));

            EXCELL_HEADER1 = HEADER_DETAIL.replace("{YEAR}", new DateUtil().getBudgeMonthYear_543(stmp, ConstantVariable.BUDGET_MONTH));
            //EXCELL_HEADER2 = new DateUtil().convertStmpToString(report.getStmp()) + getTitleDateOpd(report);
            EXCELL_HEADER2 = report.getTitle2();
            EXCELL_HOSPITAL = "หน่วยบริการ: " + StringOpUtil.removeNull(report.getServiceName()) + " (" + StringOpUtil.removeNull(report.getServiceCode()) + ")";

            // style Excell
            this.setFontFamily("TH SarabunPSK");
            //this.setColorCell(HSSFColor.LIGHT_GREEN.index);
            this.loadStyle(new HSSFWorkbook(file));

            // Start sheet 1 
            HSSFSheet sheet = workbookBase.getSheetAt(0);
            sheet.setColumnWidth((col_last + 1), WIDTH_TXID);

            // row 0 HEADER0
            row = sheet.createRow(0);
            row.setHeight((short) 390);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_HEADER1);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col_last));
            cell.setCellStyle(csHead);

            // row 1 HEADER2
            row = sheet.createRow(1);
            row.setHeight((short) 390);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_HEADER2);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, col_last));
            cell.setCellStyle(csHead);

            // row 2 HOSPITAL
            row = sheet.createRow(2);
            row.setHeight((short) 390);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_HOSPITAL);
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, col_last));
            cell.setCellStyle(csHead);

            int curRow = row_start;
            int autoNumber = 1;
            listData = vajiraREDao.getListVajiraDetail(report);

            for (int i = 0; i < listData.size(); i++) {
                ObjRptVajiraRfDetail objData = listData.get(i);

                row = sheet.createRow(curRow);
                row.setHeight((short) 400);

                cell = row.createCell(0);
                cell.setCellValue(autoNumber);
                cell.setCellStyle(csNum3);

                cell = row.createCell(1);
                cell.setCellValue(objData.getPid());
                cell.setCellStyle(csStringLeft);

                cell = row.createCell(2);
                cell.setCellValue(objData.getHn());
                cell.setCellStyle(csNum4);

                cell = row.createCell(3);
                cell.setCellValue(objData.getPname());
                cell.setCellStyle(csStringLeft);

                cell = row.createCell(4);
                cell.setCellValue(objData.getHmain() + " : " + objData.getHmain_name());
                cell.setCellStyle(csStringLeft);

                cell = row.createCell(5);
                cell.setCellValue(objData.getHmainip() + " : " + objData.getHmainip_name());
                cell.setCellStyle(csStringLeft);

                cell = row.createCell(6);
                cell.setCellValue(objData.getDateopd_th());
                cell.setCellStyle(csStringLeft);

                cell = row.createCell(7);
                cell.setCellValue(objData.getPdxcode());
                cell.setCellStyle(csStringCenter);

                cell = row.createCell(8);
                cell.setCellValue(objData.getChrg_hc());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(9);
                cell.setCellValue(objData.getChrg_202());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(10);
                cell.setCellValue(objData.getChrg_stditem());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(11);
                cell.setCellValue(objData.getChrg_other());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(12);
                cell.setCellValue(objData.getChrg_total());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(13);
                cell.setCellValue(objData.getPaid_model());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(14);
                cell.setCellValue(objData.getPaid_202());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(15);
                cell.setCellValue(objData.getPaid_stditem());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(16);
                cell.setCellValue(objData.getPaid_other());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(17);
                cell.setCellValue(objData.getTotalreimburse());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(18);
                cell.setCellValue(objData.getInvoice_no());
                cell.setCellStyle(csString2);

                cell = row.createCell(19);
                cell.setCellValue(objData.getTxid());
                cell.setCellStyle(csStringtxid);

                curRow++;
                autoNumber++;
            }

            //mergeRowLimit(sheet, curRow, count_limit, indexsCol, col1);
            // สรุป
            row = sheet.createRow(curRow);
            row.setHeight((short) 450);
            cell = row.createCell(0);
            cell.setCellValue("รวม");
            sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 7));
            cell.setCellStyle(csNum4B);

            row.createCell(1).setCellStyle(csNum4B);
            row.createCell(2).setCellStyle(csNum4B);
            row.createCell(3).setCellStyle(csNum4B);
            row.createCell(4).setCellStyle(csNum4B);
            row.createCell(5).setCellStyle(csNum4B);
            row.createCell(6).setCellStyle(csNum4B);
            row.createCell(7).setCellStyle(csNum4B);

            cell = row.createCell(8);
            cell.setCellFormula(builderFormulaSumRound(8, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(9);
            cell.setCellFormula(builderFormulaSumRound(9, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(10);
            cell.setCellFormula(builderFormulaSumRound(10, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(11);
            cell.setCellFormula(builderFormulaSumRound(11, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(12);
            cell.setCellFormula(builderFormulaSumRound(12, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(13);
            cell.setCellFormula(builderFormulaSumRound(13, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(14);
            cell.setCellFormula(builderFormulaSumRound(14, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(15);
            cell.setCellFormula(builderFormulaSumRound(15, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(16);
            cell.setCellFormula(builderFormulaSumRound(16, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(17);
            cell.setCellFormula(builderFormulaSumRound(17, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            /*
             ############ วันที่ออกรายงาน ###############
             */
            int rowDateTimeCurrent = curRow + 1;
            // BathText
            row = sheet.createRow((rowDateTimeCurrent));
            cell = row.createCell(0);
            cell.setCellValue("ออกรายงานเมื่อวันที่ _" + new DateUtil().getDateTimeCurrent());
            sheet.addMergedRegion(new CellRangeAddress(rowDateTimeCurrent, rowDateTimeCurrent, 0, col_last));
            cell.setCellStyle(csStringB);


            /*
             ############ วันที่ออกรายงาน ###############
             */
            workbookBase.setSheetName(0, report.getServiceCode());// + "  " + report.getServiceName());

            new FileUtil().mkdirMutiDirectory(pathDirectory);

            out = new FileOutputStream(pathDirectory + "" + File.separator + FILE_NAME_DETAIL + StringOpUtil.removeNull(report.getServiceCode()) + "_" + report.getStmp() + ".xls");
            workbookBase.write(out);

            out.close();
            file.close();
            Console.LOG("ออกรายงาน " + StringOpUtil.removeNull(report.getServiceName()) + " งวด: "
                    + report.getYearMonth() + "-"
                    + report.getNo() + " เรียบร้อยแล้วครับ", 1);
            programeStatus.setMessage(ConstantMessage.MSG_REPORT_SUCCESS);
            programeStatus.setTitle(ConstantMessage.MSG_REPORT_COMPLETE);
            programeStatus.setProcessStatus(true);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return programeStatus;
    }

    public ProgrameStatus hcREByHcode(OppReport report) {
        ProgrameStatus programeStatus = new ProgrameStatus();
        List<HospitalService> listData = new ArrayList<>();
        try {
            connection = new DBManage().open();
            VajiraREDao vajira11535Dao = new VajiraREDao();
            vajira11535Dao.setConnection(connection); // connection = new DBManage().open();
            listData = vajira11535Dao.getHospitalServiceWithHcode(report);

            for (int i = 0; i < listData.size(); i++) {
                HospitalService objData = listData.get(i);
                report.setServiceCode(objData.getHosCode());
                report.setServiceName(objData.getHosCodeName());

                programeStatus = hcRfDetail(report);
                programeStatus = hcRfSummarySpliteWithHcode(report);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return programeStatus;
    }

    public ProgrameStatus hcRFSummary(OppReport report) {
        ProgrameStatus programeStatus = new ProgrameStatus();
        int col_last = 17;
        int row_start = 8; // index row
        int row_formula_start = row_start + 1;
        List<ObjRptVajiraRfSummary> listData = new ArrayList<ObjRptVajiraRfSummary>();

        String stmp = StringOpUtil.removeNull(report.getYearMonth()) + "-" + report.getNo();
        String pathDirectory = report.getPathFile() + "" + File.separator + "" + stmp + "" + File.separator + "";
        try {
            connection = new DBManage().open();

            VajiraREDao vajiraREDao = new VajiraREDao();
            vajiraREDao.setConnection(connection);

            //readTemplate 
            file = new FileInputStream(new File("." + File.separator + "xls" + File.separator + "HC_RF_SUM.xls"));

            EXCELL_HEADER1 = HEADER_DETAIL.replace("{YEAR}", new DateUtil().getBudgeMonthYear_543(stmp, ConstantVariable.BUDGET_MONTH));
            //EXCELL_HEADER2 = new DateUtil().convertStmpToString(report.getStmp()) + getTitleDateOpd(report);
            EXCELL_HEADER2 = report.getTitle2();
            EXCELL_HOSPITAL = "หน่วยบริการ: " + StringOpUtil.removeNull(report.getServiceName()) + " (" + StringOpUtil.removeNull(report.getServiceCode()) + ")";

            // style Excell
            this.setFontFamily("TH SarabunPSK");
            //this.setColorCell(HSSFColor.LIGHT_GREEN.index);
            this.loadStyle(new HSSFWorkbook(file));

            // Start sheet 1 
            HSSFSheet sheet = workbookBase.getSheetAt(0);
            sheet.setColumnWidth((col_last + 1), WIDTH_TXID);

            // row 0 HEADER0
            row = sheet.createRow(0);
            row.setHeight((short) 390);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_HEADER1);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col_last));
            cell.setCellStyle(csHead);

            // row 1 HEADER2
            row = sheet.createRow(1);
            row.setHeight((short) 390);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_HEADER2);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, col_last));
            cell.setCellStyle(csHead);

            // row 2 HOSPITAL
            row = sheet.createRow(2);
            row.setHeight((short) 390);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_HOSPITAL);
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, col_last));
            cell.setCellStyle(csHead);

            int curRow = row_start;
            int autoNumber = 1;
            listData = vajiraREDao.getListVajiraSumGroupHcode(report);

            for (int i = 0; i < listData.size(); i++) {
                ObjRptVajiraRfSummary objData = listData.get(i);

                row = sheet.createRow(curRow);
                row.setHeight((short) 400);

                cell = row.createCell(0);
                cell.setCellValue(autoNumber);
                cell.setCellStyle(csNum3);

                cell = row.createCell(1);
                cell.setCellValue(objData.getHcode());
                cell.setCellStyle(csStringLeft);

                cell = row.createCell(2);
                cell.setCellValue(objData.getHcode_name());
                cell.setCellStyle(csStringLeft);

                cell = row.createCell(3);
                cell.setCellValue(objData.getCount_pid());
                cell.setCellStyle(csNum3);

                cell = row.createCell(4);
                cell.setCellValue(objData.getCount_txid());
                cell.setCellStyle(csNum3);

                cell = row.createCell(5);
                cell.setCellValue(objData.getSum_chrg_hc());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(6);
                cell.setCellValue(objData.getSum_chrg_202());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(7);
                cell.setCellValue(objData.getSum_chrg_stditem());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(8);
                cell.setCellValue(objData.getSum_chrg_other());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(9);
                cell.setCellValue(objData.getSum_chrg_total());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(10);
                cell.setCellValue(objData.getSum_paid_hc());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(11);
                cell.setCellValue(objData.getSum_paid_202());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(12);
                cell.setCellValue(objData.getSum_paid_stditem());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(13);
                cell.setCellValue(objData.getSum_paid_other());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(14);
                cell.setCellValue(objData.getSum_paid_total());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(15);
                cell.setCellValue(objData.getSum_point());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(16);
                cell.setCellValue(objData.getSum_reimburse());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(17);
                cell.setCellValue(objData.getSum_totalreimburse());
                cell.setCellStyle(csDouble2R);

                curRow++;
                autoNumber++;
            }

            //mergeRowLimit(sheet, curRow, count_limit, indexsCol, col1);
            // สรุป
            row = sheet.createRow(curRow);
            row.setHeight((short) 450);
            cell = row.createCell(0);
            cell.setCellValue("รวม");
            sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 2));
            cell.setCellStyle(csNum4B);

            row.createCell(1).setCellStyle(csString2Center);
            row.createCell(2).setCellStyle(csString2Center);

            cell = row.createCell(3);
            cell.setCellFormula(builderFormulaSumRound(3, row_formula_start, curRow, 0));
            cell.setCellStyle(csNum4BCenter);

            cell = row.createCell(4);
            cell.setCellFormula(builderFormulaSumRound(4, row_formula_start, curRow, 0));
            cell.setCellStyle(csNum4BCenter);

            cell = row.createCell(5);
            cell.setCellFormula(builderFormulaSumRound(5, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(6);
            cell.setCellFormula(builderFormulaSumRound(6, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(7);
            cell.setCellFormula(builderFormulaSumRound(7, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(8);
            cell.setCellFormula(builderFormulaSumRound(8, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(9);
            cell.setCellFormula(builderFormulaSumRound(9, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(10);
            cell.setCellFormula(builderFormulaSumRound(10, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(11);
            cell.setCellFormula(builderFormulaSumRound(11, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(12);
            cell.setCellFormula(builderFormulaSumRound(12, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(13);
            cell.setCellFormula(builderFormulaSumRound(13, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(14);
            cell.setCellFormula(builderFormulaSumRound(14, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(15);
            cell.setCellFormula(builderFormulaSumRound(15, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(16);
            cell.setCellFormula(builderFormulaSumRound(16, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(17);
            cell.setCellFormula(builderFormulaSumRound(17, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            /*
             ############ วันที่ออกรายงาน ###############
             */
            int rowDateTimeCurrent = curRow + 1;
            // BathText
            row = sheet.createRow((rowDateTimeCurrent));
            cell = row.createCell(0);
            cell.setCellValue("ออกรายงานเมื่อวันที่ _" + new DateUtil().getDateTimeCurrent());
            sheet.addMergedRegion(new CellRangeAddress(rowDateTimeCurrent, rowDateTimeCurrent, 0, col_last));
            cell.setCellStyle(csStringB);


            /*
             ############ วันที่ออกรายงาน ###############
             */
            workbookBase.setSheetName(0, report.getServiceCode());// + "  " + report.getServiceName());

            new FileUtil().mkdirMutiDirectory(pathDirectory);

            out = new FileOutputStream(pathDirectory + "" + File.separator + FILE_NAME_SUM + report.getYearMonth() + "-" + report.getNo() + ".xls");

            workbookBase.write(out);
            out.close();
            file.close();
            Console.LOG("รายงานสรุปรายงานการจ่ายชดเชยค่าบริการกรณี Opศูนย์บริการสาธารณสุข ส่งต่อคณะแพทย์ศาสตร์วชิรพยาบาล ถูกออกเรียบร้อยแล้ว", 1);
           
            programeStatus.setMessage(ConstantMessage.MSG_REPORT_SUCCESS);
            programeStatus.setTitle(ConstantMessage.MSG_REPORT_COMPLETE);
            programeStatus.setProcessStatus(true);

        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
            programeStatus.setMessage(ConstantMessage.MSG_PROCESS_FAILS + e.toString());
            programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
            programeStatus.setProcessStatus(false);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return programeStatus;
    }

    public ProgrameStatus hcRfSummarySpliteWithHcode(OppReport report) {
        ProgrameStatus programeStatus = new ProgrameStatus();
        int col_last = 17;
        int row_start = 8; // index row
        int row_formula_start = row_start + 1;
        List<ObjRptVajiraRfSummary> listData = new ArrayList<ObjRptVajiraRfSummary>();

        String stmp = StringOpUtil.removeNull(report.getYearMonth()) + "-" + report.getNo();
        String pathDirectory = report.getPathFile() + "" + File.separator + "" + stmp + "" + File.separator + "";
        try {
            connection = new DBManage().open();

            VajiraREDao vajiraREDao = new VajiraREDao();
            vajiraREDao.setConnection(connection);

            //readTemplate 
            file = new FileInputStream(new File("." + File.separator + "xls" + File.separator + "HC_RF_SUM_SPLITE.xls"));

            EXCELL_HEADER1 = HEADER_DETAIL.replace("{YEAR}", new DateUtil().getBudgeMonthYear_543(stmp, ConstantVariable.BUDGET_MONTH));
            //EXCELL_HEADER2 = new DateUtil().convertStmpToString(report.getStmp()) + getTitleDateOpd(report);
            EXCELL_HEADER2 = report.getTitle2();
            EXCELL_HOSPITAL = "หน่วยบริการ: " + StringOpUtil.removeNull(report.getServiceName()) + " (" + StringOpUtil.removeNull(report.getServiceCode()) + ")";

            // style Excell
            this.setFontFamily("TH SarabunPSK");
            //this.setColorCell(HSSFColor.LIGHT_GREEN.index);
            this.loadStyle(new HSSFWorkbook(file));

            // Start sheet 1 
            HSSFSheet sheet = workbookBase.getSheetAt(0);
            sheet.setColumnWidth((col_last + 1), WIDTH_TXID);

            // row 0 HEADER0
            row = sheet.createRow(0);
            row.setHeight((short) 390);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_HEADER1);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col_last));
            cell.setCellStyle(csHead);

            // row 1 HEADER2
            row = sheet.createRow(1);
            row.setHeight((short) 390);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_HEADER2);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, col_last));
            cell.setCellStyle(csHead);

            // row 2 HOSPITAL
            row = sheet.createRow(2);
            row.setHeight((short) 390);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_HOSPITAL);
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, col_last));
            cell.setCellStyle(csHead);

            int curRow = row_start;
            int autoNumber = 1;
            listData = vajiraREDao.getListVajiraSumGroupHmain(report);

            for (int i = 0; i < listData.size(); i++) {
                ObjRptVajiraRfSummary objData = listData.get(i);

                row = sheet.createRow(curRow);
                row.setHeight((short) 400);

                cell = row.createCell(0);
                cell.setCellValue(autoNumber);
                cell.setCellStyle(csNum3);

                cell = row.createCell(1);
                cell.setCellValue(objData.getHmain());
                cell.setCellStyle(csStringLeft);

                cell = row.createCell(2);
                cell.setCellValue(objData.getHmain_name());
                cell.setCellStyle(csStringLeft);

                cell = row.createCell(3);
                cell.setCellValue(objData.getCount_pid());
                cell.setCellStyle(csNum3);

                cell = row.createCell(4);
                cell.setCellValue(objData.getCount_txid());
                cell.setCellStyle(csNum3);

                cell = row.createCell(5);
                cell.setCellValue(objData.getSum_chrg_hc());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(6);
                cell.setCellValue(objData.getSum_chrg_202());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(7);
                cell.setCellValue(objData.getSum_chrg_stditem());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(8);
                cell.setCellValue(objData.getSum_chrg_other());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(9);
                cell.setCellValue(objData.getSum_chrg_total());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(10);
                cell.setCellValue(objData.getSum_paid_hc());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(11);
                cell.setCellValue(objData.getSum_paid_202());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(12);
                cell.setCellValue(objData.getSum_paid_stditem());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(13);
                cell.setCellValue(objData.getSum_paid_other());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(14);
                cell.setCellValue(objData.getSum_paid_total());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(15);
                cell.setCellValue(objData.getSum_point());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(16);
                cell.setCellValue(objData.getSum_reimburse());
                cell.setCellStyle(csDouble2R);

                cell = row.createCell(17);
                cell.setCellValue(objData.getSum_totalreimburse());
                cell.setCellStyle(csDouble2R);

                curRow++;
                autoNumber++;
            }

            //mergeRowLimit(sheet, curRow, count_limit, indexsCol, col1);
            // สรุป
            row = sheet.createRow(curRow);
            row.setHeight((short) 450);
            cell = row.createCell(0);
            cell.setCellValue("รวม");
            sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 2));
            cell.setCellStyle(csNum4B);

            row.createCell(1).setCellStyle(csString2Center);
            row.createCell(2).setCellStyle(csString2Center);

            cell = row.createCell(3);
            cell.setCellFormula(builderFormulaSumRound(3, row_formula_start, curRow, 0));
            cell.setCellStyle(csNum4BCenter);

            cell = row.createCell(4);
            cell.setCellFormula(builderFormulaSumRound(4, row_formula_start, curRow, 0));
            cell.setCellStyle(csNum4BCenter);

            cell = row.createCell(5);
            cell.setCellFormula(builderFormulaSumRound(5, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(6);
            cell.setCellFormula(builderFormulaSumRound(6, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(7);
            cell.setCellFormula(builderFormulaSumRound(7, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(8);
            cell.setCellFormula(builderFormulaSumRound(8, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(9);
            cell.setCellFormula(builderFormulaSumRound(9, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(10);
            cell.setCellFormula(builderFormulaSumRound(10, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(11);
            cell.setCellFormula(builderFormulaSumRound(11, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(12);
            cell.setCellFormula(builderFormulaSumRound(12, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(13);
            cell.setCellFormula(builderFormulaSumRound(13, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(14);
            cell.setCellFormula(builderFormulaSumRound(14, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(15);
            cell.setCellFormula(builderFormulaSumRound(15, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(16);
            cell.setCellFormula(builderFormulaSumRound(16, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(17);
            cell.setCellFormula(builderFormulaSumRound(17, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            /*
             ############ วันที่ออกรายงาน ###############
             */
            int rowDateTimeCurrent = curRow + 1;
            // BathText
            row = sheet.createRow((rowDateTimeCurrent));
            cell = row.createCell(0);
            cell.setCellValue("ออกรายงานเมื่อวันที่ _" + new DateUtil().getDateTimeCurrent());
            sheet.addMergedRegion(new CellRangeAddress(rowDateTimeCurrent, rowDateTimeCurrent, 0, col_last));
            cell.setCellStyle(csStringB);


            /*
             ############ วันที่ออกรายงาน ###############
             */
            workbookBase.setSheetName(0, report.getServiceCode());// + "  " + report.getServiceName());

            new FileUtil().mkdirMutiDirectory(pathDirectory);

            out = new FileOutputStream(pathDirectory + "" + File.separator + FILE_NAME_SUM_REPORT + StringOpUtil.removeNull(report.getServiceCode()) + "_" + report.getStmp() + ".xls");
            workbookBase.write(out);

            out.close();
            file.close();
            Console.LOG("ออกรายงาน " + StringOpUtil.removeNull(report.getServiceName()) + " งวด: "
                    + report.getYearMonth() + "-"
                    + report.getNo() + " เรียบร้อยแล้วครับ", 1);
            programeStatus.setMessage(ConstantMessage.MSG_REPORT_SUCCESS);
            programeStatus.setTitle(ConstantMessage.MSG_REPORT_COMPLETE);
            programeStatus.setProcessStatus(true);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return programeStatus;
    }

    public ProgrameStatus hc_genReportAllFunction(OppReport report) {
        ProgrameStatus programeStatus = new ProgrameStatus();
        try {
            programeStatus = this.hcREByHcode(report);
            programeStatus = this.hcRFSummary(report);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return programeStatus;
    }
}
