/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.controller;

import com.claim.xls.ExcellBaseUtil;
import com.claim.connection.DBManage;
import com.claim.dao.Noni2015DAO;
import com.claim.object.HospitalService;
import com.claim.object.ObjRptNoniDetail;
import com.claim.object.ObjRptNoniSum;
import com.claim.object.OppReport;
import com.claim.support.Console;
import com.claim.constants.ConstantMessage;
import com.claim.support.DateUtil;
import com.claim.support.FileUtil;
import com.claim.object.ProgrameStatus;
import com.claim.support.StringOpUtil;
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
 * @author Poolsawat.a
 */
public class Noni2015Controller extends ExcellBaseUtil {

    //############# POI ###############
    FileInputStream file = null;
    FileOutputStream out = null;
    HSSFCell cell = null;
    HSSFRow row = null;
    //############# POI ###############

    //############# JDBC ###############
    Connection connection = null;
    //############# JDBC ###############

    //############# Variable ###############
    String EXCELL_HEADER1 = null;
    String EXCELL_HEADER2 = null;
    String EXCELL_HOSPITAL = null;
    String Budget_Year = "รวม 2014 และ 2015";

    public static String HEADER_DETAIL = "รายละเอียดการจ่ายชดเชยค่าบริการ  กรณีกึ่งผู้ป่วยนอกกึ่งผู้ป่วยใน (NONI)  ปีงบประมาณ 2557";
    public static String HEADER_SUM = "สรุปการจ่ายชดเชยค่าบริการ กรณีกึ่งผู้ป่วยนอกกึ่งผู้ป่วยใน (NONI)  ปีงบประมาณ 2557";
    //############# Variable ###############

    public ProgrameStatus noniDetail(OppReport report) {
        int col_last = 13;
        int row_start = 6; // index row
        int row_formula_start = 7;
        ProgrameStatus programeStatus = new ProgrameStatus();
        List<ObjRptNoniDetail> listData = new ArrayList<ObjRptNoniDetail>();
        String stmp = StringOpUtil.removeNull(report.getYearMonth()) + "-" + report.getNo();
        String pathDirectory = report.getPathFile() + ""+File.separator+"noni"+File.separator+"" + stmp + ""+File.separator+"";
        try {
            connection = new DBManage().open();
            Noni2015DAO noni2015DAO = new Noni2015DAO();
            noni2015DAO.setConnection(connection);

            //readTemplate 
            file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"NONI_detail.xls"));

            //EXCELL_HEADER1 = "รายละเอียดการจ่ายชดเชยค่าบริการ  กรณีกึ่งผู้ป่วยนอกกึ่งผู้ป่วยใน (NONI)  ปีงบประมาณ 2557";
            EXCELL_HEADER1 = report.getTitle1();
            /*if (report.getBudget_year().equals("2014")) {
             EXCELL_HEADER2 = "งวดที่ 01 (ล่าช้า)";
             } else {
             EXCELL_HEADER2 = "งวดที่ " + new DateUtil().convertStmpToString(report.getStmp());
             }*/
            EXCELL_HEADER2 = new DateUtil().convertStmpToNoniString(report.getStmp());
            EXCELL_HOSPITAL = "หน่วยบริการ: " + StringOpUtil.removeNull(report.getServiceName()) + " (" + StringOpUtil.removeNull(report.getServiceCode()) + ")";

            // style Excell
            HSSFWorkbook wbNoni = new HSSFWorkbook(file);
            this.loadStyle(wbNoni);

            // Start sheet 1 
            HSSFSheet sheet = workbookBase.getSheetAt(0);
            sheet.createFreezePane(5, row_start); // col[F],row[index 6 = 7]
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

            // row 1 HOSPITAL
            row = sheet.createRow(2);
            row.setHeight((short) 390);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_HOSPITAL);
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, col_last));
            cell.setCellStyle(csHead);

            int curRow = row_start;
            int i = 1;
            listData = noni2015DAO.getListNoniDetail(report);
            for (int j = 0; j < listData.size(); j++) {
                ObjRptNoniDetail data = listData.get(j);
                int col1 = i;

                row = sheet.createRow(curRow);
                row.setHeight((short) 340);

                /*PoiHssfUtil stylePoi = new PoiHssfUtil(wb, row, cell);
                 stylePoi.setStyleText(0, String.valueOf(col1), PoiHssfUtil.CENTER);*/
                cell = row.createCell(0);
                cell.setCellValue(col1);
                cell.setCellStyle(csNum4);

                cell = row.createCell(1);
                cell.setCellValue(data.getPid());
                cell.setCellStyle(csStringPid);

                cell = row.createCell(2);
                cell.setCellValue(data.getPname());
                cell.setCellStyle(csStringLeft);

                cell = row.createCell(3);
                cell.setCellValue(data.getHn());
                cell.setCellStyle(csStringLeft);

                cell = row.createCell(4);
                cell.setCellValue(data.getHmainname());
                cell.setCellStyle(csStringLeft);

                cell = row.createCell(5);
                cell.setCellValue(data.getDateopd_thai_buddha());
                cell.setCellStyle(csStringLeft);

                cell = row.createCell(6);
                cell.setCellValue(data.getNoniclass());
                cell.setCellStyle(csStringLeft);

                cell = row.createCell(7);
                cell.setCellValue(data.getChrg_middle_priced_items());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(8);
                cell.setCellValue(data.getChrg_other());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(9);
                cell.setCellValue(data.getChrg_total());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(10);
                cell.setCellValue(data.getPaid_middle_priced_items());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(11);
                cell.setCellValue(data.getPaid_other());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(12);
                cell.setCellValue(data.getPaid_total());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(13);
                cell.setCellValue(data.getInvoice_no());
                cell.setCellStyle(csStringLeft);

                cell = row.createCell(14);
                cell.setCellValue(data.getTxid());
                cell.setCellStyle(csStringtxid);

                curRow++;
                i++;
            }

            // สรุป
            row = sheet.createRow(curRow);
            cell = row.createCell(0);
            cell.setCellValue("รวม");
            sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 6));
            cell.setCellStyle(csNum4B);

            cell = row.createCell(1);
            cell.setCellStyle(csNum4B);
            cell = row.createCell(2);
            cell.setCellStyle(csNum4B);
            cell = row.createCell(3);
            cell.setCellStyle(csNum4B);
            cell = row.createCell(4);
            cell.setCellStyle(csNum4B);
            cell = row.createCell(5);
            cell.setCellStyle(csNum4B);
            cell = row.createCell(6);
            cell.setCellStyle(csNum4B);

            cell = row.createCell(7);
            cell.setCellFormula(builderFormulaSum(7, row_formula_start, curRow));            
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(8);
            cell.setCellFormula(builderFormulaSum(8, row_formula_start, curRow));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(9);
            cell.setCellFormula(builderFormulaSum(9, row_formula_start, curRow));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(10);
            cell.setCellFormula(builderFormulaSum(10, row_formula_start, curRow));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(11);
            cell.setCellFormula(builderFormulaSum(11, row_formula_start, curRow));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(12);
            cell.setCellFormula(builderFormulaSum(12, row_formula_start, curRow));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(13);
            cell.setCellStyle(csDouble2B);

            workbookBase.setSheetName(0, report.getServiceCode());// + "  " + report.getServiceName());
            /// int sheetIndex, int startColumn, int endColumn, int startRow, int endRow)                    
            sheet.setColumnHidden(13, true);
            workbookBase.setPrintArea(0, "$A$1:$M$" + (curRow + 1));
            // file out 
            // ############# mkdir ############          
            pathDirectory = new FileUtil().mkdirDir(pathDirectory, report.getBudget_year(), "noni");
            // ############# mkdir ############            
            out = new FileOutputStream(pathDirectory + ""+File.separator+"noni_" + StringOpUtil.removeNull(report.getServiceCode()) + "_" + report.getStmp() + ".xls");
            workbookBase.write(out);

            out.close();
            file.close();

            Console.LOG("ออกรายงาน " + report.getServiceName() + " งวด: " + report.getYearMonth() + "-" + report.getNo() + " เรียบร้อยแล้วครับ", 1);
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

    public ProgrameStatus noniDetail_byHcode(OppReport report) {
        ProgrameStatus programeStatus = new ProgrameStatus();
        List<HospitalService> listData = new ArrayList<>();
        try {
            connection = new DBManage().open();
            Noni2015DAO noni2015DAO = new Noni2015DAO();
            noni2015DAO.setConnection(connection); // connection = new DBManage().open();
            listData = noni2015DAO.getHospitalService(report);

            for (int i = 0; i < listData.size(); i++) {
                HospitalService objData = listData.get(i);
                report.setServiceCode(objData.getHosCode());
                report.setServiceName(objData.getHosCodeName());
                programeStatus = this.noniDetail(report);
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

    public ProgrameStatus noniSum(OppReport report) {
        int col_last = 9;
        int row_start = 5; // index_row
        int row_formula_start = 6;
        ProgrameStatus programeStatus = new ProgrameStatus();
        List<ObjRptNoniSum> listData = new ArrayList<>();
        String stmp = StringOpUtil.removeNull(report.getYearMonth()) + "-" + report.getNo();
        String pathDirectory = report.getPathFile() + ""+File.separator+"noni"+File.separator+"" + stmp + ""+File.separator+"";
        try {
            connection = new DBManage().open();
            Noni2015DAO noni2015DAO = new Noni2015DAO();
            noni2015DAO.setConnection(connection);

            file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"NONI_summary.xls"));

            // Top Excell Sheet1
            EXCELL_HEADER1 = report.getTitle1();
            // String EXCELL_SERVICE1 = "งวดที่  Clearing houseจากงบเหมาจ่ายรายหัว " + dateReport;
            /*if (report.getBudget_year().equals("2014")) {
             EXCELL_HEADER2 = "งวดที่ 01 (ล่าช้า)";
             } else {
             EXCELL_HEADER2 = "งวดที่ " + new DateUtil().convertStmpToString(report.getStmp());
             }*/
            EXCELL_HEADER2 = new DateUtil().convertStmpToNoniString(report.getStmp());

            HSSFWorkbook wbNoni = new HSSFWorkbook(file);
            this.loadStyle(wbNoni);

            HSSFSheet sheet = workbookBase.getSheetAt(0);
            sheet.createFreezePane(3, 5);

            // row 0 Header
            row = sheet.createRow(0);
            row.setHeight((short) 390);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_HEADER1);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col_last));
            cell.setCellStyle(csHead);

            // row 2 Header
            row = sheet.createRow(1);
            row.setHeight((short) 390);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_HEADER2);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col_last));
            cell.setCellStyle(csHead);

            int curRow = row_start;
            int i = 1;

            listData = noni2015DAO.getListNoniSum(report);
            System.out.println("listData.size() :" + listData.size());
            for (int j = 0; j < listData.size(); j++) {
                ObjRptNoniSum objData = listData.get(j);

                row = sheet.createRow(curRow);
                row.setHeight((short) 360);
                cell = row.createCell(0);
                cell.setCellValue(i);
                cell.setCellStyle(csNum4);

                cell = row.createCell(1);
                cell.setCellValue(objData.getHcode());
                cell.setCellStyle(csStringCenter);
                
                 cell = row.createCell(2);
                cell.setCellValue(objData.getHcodename());
                cell.setCellStyle(csStringLeft);

                cell = row.createCell(3);
                cell.setCellValue(objData.getCount_visit());
                cell.setCellStyle(csNum4);

                cell = row.createCell(4);
                cell.setCellValue(objData.getSum_chrg_middle_priced_items());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(5);
                cell.setCellValue(objData.getSum_chrg_other());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(6);
                cell.setCellValue(objData.getSum_chrg_total());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(7);
                cell.setCellValue(objData.getSum_paid_middle_priced_items());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(8);
                cell.setCellValue(objData.getSum_paid_other());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(9);
                cell.setCellValue(objData.getSum_paid_total());
                cell.setCellStyle(csDouble2);

                curRow++;
                i++;
            }

            /**
             * footer summary total
             */
            row = sheet.createRow(curRow);
            row.setHeight((short) 450);
            cell = row.createCell(0);
            cell.setCellValue("รวม");
            sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 1));
            cell.setCellStyle(csNum4B);

            cell = row.createCell(1);
            cell.setCellStyle(csHeadTab);
            cell = row.createCell(2);
            cell.setCellStyle(csHeadTab);

            cell = row.createCell(3);
            cell.setCellFormula(builderFormulaSumRound(3, row_formula_start, curRow, 0));
            cell.setCellStyle(csNum4B);

            cell = row.createCell(4);            
            cell.setCellFormula(builderFormulaSumRound(4, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);
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

            // ############# mkdir ############       
            pathDirectory = new FileUtil().mkdirDir(pathDirectory, report.getBudget_year(), "noni");
            // ############# mkdir ############                

            //write file Excell
            out = new FileOutputStream(pathDirectory + ""+File.separator+"noni_summary_" + report.getYearMonth() + "-" + report.getNo() + ".xls");
            workbookBase.write(out);
            out.close();
            file.close();
            Console.LOG("noni_summary ถูกออกเรียบร้อยแล้ว", 1);

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

    // ###################### for web site claim######################
    // ###################### for web site claim######################
    public ProgrameStatus noni_genReportAllFunction(OppReport report) {
        ProgrameStatus programeStatus = new ProgrameStatus();
        try {

            if (report.getFor_use() == 0) {  //  ทั่วไป
                // ######### noni detail #####
                report.setTitle1(HEADER_DETAIL);
                programeStatus = this.noniDetail_byHcode(report);

                // ######### noni sum ######
                report.setTitle1(HEADER_SUM);
                programeStatus = this.noniSum(report);

            } else {
                // ######### noni detail #####
                report.setTitle1(HEADER_DETAIL);
                report.setBudget_year("2015");
                programeStatus = this.noniDetail_byHcode(report);

                // ######### noni sum ######
                report.setTitle1(HEADER_SUM);
                programeStatus = this.noniSum(report);

                report.setBudget_year("2014");
                programeStatus = this.noniDetail_byHcode(report);

                // ######### noni sum ######
                report.setTitle1(HEADER_SUM);
                programeStatus = this.noniSum(report);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return programeStatus;
    }

    public String getNoniStmpGroupBy(Object obYM, Object obN) {
        String titleTimeStmp = "-";
        try {
            connection = new DBManage().open();
            Noni2015DAO noni2015DAO = new Noni2015DAO();
            noni2015DAO.setConnection(connection);

            if (obYM != "-" && obN != null && obN != "-") {
                titleTimeStmp = noni2015DAO.getNoniStmpDescDistinct(obYM.toString(), obN.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return titleTimeStmp;
    }
}
