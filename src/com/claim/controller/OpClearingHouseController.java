package com.claim.controller;

import com.claim.xls.ExcellBaseUtil;
import com.claim.dao.ClearingHouseDAO;
import com.claim.object.HospitalService;
import com.claim.object.OppReport;
import com.claim.support.FunctionUtil;
import com.claim.support.Console;
import com.claim.constants.ConstantMessage;
import com.claim.support.Message;
import com.claim.object.ProgrameStatus;
import com.claim.support.StringOpUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

public class OpClearingHouseController extends ExcellBaseUtil {

    private FunctionUtil function;
    private ClearingHouseDAO clearingHouseDAO;

    static String TITLE_DEDUCT_DETAIL = "หน่วยบริการประจำ: ";

    public OpClearingHouseController() {
        function = new FunctionUtil();
        clearingHouseDAO = new ClearingHouseDAO();
    }

    /**
     * ### single file mutisheet##############################
     */
    public ProgrameStatus clearing_detailDedug_byHCode(OppReport report) {
        FileOutputStream out = null;
        FileInputStream file = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        int col_last = 26;
        int row_start = 6; // index row
        int row_formula_start = row_start + 1;
        int col_txtid_width = col_last + 1;
        try {
            //readTemplate 
            file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"clearing_deduct_detailByHcode.xls"));

            // style Excell
            HSSFWorkbook wbClearing = new HSSFWorkbook(file);
            super.loadStyle(wbClearing);

            List<HospitalService> listHospital = clearingHouseDAO.getHospitalService(report);

            System.out.println("ListSize==>>" + listHospital.size());

            if (!listHospital.isEmpty()) {

                if (report.sizeListServiceCode() == 1) { // กรณี 1 หน่วย
                    out = new FileOutputStream(report.getPathFile() + ""+File.separator+"opbkk_clh_deduct_detail_byHcode_" + report.getYearMonth() + "-" + report.getNo() + " " + report.getListServiceCode().get(0).getHosCode() + ".xls");
                } else { // กรณี มากกว่า 1 หน่วย
                    //write file Excell
                    out = new FileOutputStream(report.getPathFile() + ""+File.separator+"opbkk_clh_deduct_detail_byHcode_" + report.getYearMonth() + "-" + report.getNo() + ".xls");
                }

                for (int j = 0; j < listHospital.size(); j++) {  //HospitalService hospitalService : listHospital
                    HospitalService objHospital = listHospital.get(j);

                    System.out.println("hospitalService.getHosCode===>>" + objHospital.getHosCode());

                    //global varable
                    String hospital = objHospital.getHosHmain() + " : " + objHospital.getHosHmainName();

                    // Top Excell Sheet1
                    String EXCELL_HEADER1 = "รายละเอียดการหักเงินค่าบริการผู้ป่วยนอก กรณี Clearing house ศูนย์บริการสาธารณสุข";
                    String EXCELL_SERVICE1 = TITLE_DEDUCT_DETAIL + hospital;
                    String EXCELL_MONTH1 = " งวดการจ่ายเงิน " + report.getYearMonth() + "-" + report.getNo();

                    // Start sheet 1 *******************************************************************************
                    workbookBase.getSheetAt(0);
                    HSSFSheet sheet1 = workbookBase.cloneSheet(0);

                    sheet1.createFreezePane(7, 6);
                    sheet1.setColumnWidth(col_txtid_width, WIDTH_TXID);

                    HSSFCell cell = null;
                    HSSFRow row = null;

                    // row 0 Header
                    row = sheet1.createRow(0);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_HEADER1);
                    sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, col_last));
                    cell.setCellStyle(csHead);

                    // row 1 Hospital
                    row = sheet1.createRow(1);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_SERVICE1);
                    sheet1.addMergedRegion(new CellRangeAddress(1, 1, 0, col_last));
                    cell.setCellStyle(csHead);

                    // row 1 Month
                    row = sheet1.createRow(2);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_MONTH1);
                    sheet1.addMergedRegion(new CellRangeAddress(1, 1, 0, col_last));
                    cell.setCellStyle(csHead);

                    int curRow1 = row_start;
                    int i = 1;

                    report.setServiceCode(listHospital.get(j).getHosHmain());

                    ResultSet rs1 = clearingHouseDAO.getReportByHcode(report);
                    try {

                        while (rs1.next()) {

                            String vPID = rs1.getString("pid");
                            String vHN = rs1.getString("hn");
                            String vPNAME = rs1.getString("pname");
                            String vSEX = rs1.getString("sex");
                            double vAGE = rs1.getDouble("age");
                            String vDateToChar = rs1.getString("dateopd");
                            String vvDATEOPD = rs1.getString("dateopd2");
                            String vHcode = rs1.getString("hcode");
                            String vPAID_MODEL = rs1.getString("paid_model");
                            String vPDXCODE = rs1.getString("pdxcode");
                            double vCHA21 = rs1.getDouble("CHA21");
                            double vCHA31 = rs1.getDouble("CHA31");
                            double vCHA51 = rs1.getDouble("CHA51");
                            double vCHA61 = rs1.getDouble("CHA61");
                            double vCHA71 = rs1.getDouble("CHA71");
                            double vCHA81 = rs1.getDouble("CHA81");
                            double vCHA91 = rs1.getDouble("CHA91");
                            double vCHAA1 = rs1.getDouble("CHAA1");
                            double vCHAB1 = rs1.getDouble("CHAB1");
                            double vCHAC1 = rs1.getDouble("CHAC1");
                            double vCHAD1 = rs1.getDouble("CHAD1");
                            double vCHAE1 = rs1.getDouble("CHAE1");
                            double vCHAH1 = rs1.getDouble("CHAH1");
                            double vCHAJ1 = rs1.getDouble("CHAJ1");
                            double vCHATOTAL = rs1.getDouble("CHATOTAL");
                            double vREIMBURSE = rs1.getDouble("REIMBURSE");
                            String vTxid = rs1.getString("txid");
                            String vInvoice_no = rs1.getString("invoice_no");

                            row = sheet1.createRow(curRow1);
                            row.setHeight((short) 360);
                            cell = row.createCell(0);
                            cell.setCellValue(i);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(1);
                            cell.setCellValue(function.subStringPid(vPID));
                            cell.setCellStyle(csStringPid);

                            cell = row.createCell(2);
                            cell.setCellValue(vHN);
                            cell.setCellStyle(csNumH);

                            cell = row.createCell(3);
                            cell.setCellValue(vPNAME);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(4);
                            cell.setCellValue(vSEX);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(5);
                            cell.setCellValue(vAGE);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(6);
                            cell.setCellValue(vDateToChar);
                            cell.setCellStyle(csStringCenter);

                            cell = row.createCell(7);
                            cell.setCellValue(vHcode);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(8);
                            cell.setCellValue(vPAID_MODEL);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(9);
                            cell.setCellValue(vPDXCODE);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(10);
                            cell.setCellValue(vCHA21);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(11);
                            cell.setCellValue(vCHA31);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(12);
                            cell.setCellValue(vCHA51);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(13);
                            cell.setCellValue(vCHA61);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(14);
                            cell.setCellValue(vCHA71);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(15);
                            cell.setCellValue(vCHA81);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(16);
                            cell.setCellValue(vCHA91);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(17);
                            cell.setCellValue(vCHAA1);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(18);
                            cell.setCellValue(vCHAB1);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(19);
                            cell.setCellValue(vCHAC1);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(20);
                            cell.setCellValue(vCHAD1);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(21);
                            cell.setCellValue(vCHAE1);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(22);
                            cell.setCellValue(vCHAH1);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(23);
                            cell.setCellValue(vCHAJ1);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(24);
                            cell.setCellValue(vCHATOTAL);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(25);
                            cell.setCellValue(vREIMBURSE);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(26);
                            cell.setCellValue(vInvoice_no);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(27);
                            cell.setCellValue(vTxid);
                            cell.setCellStyle(csStringtxid);

                            curRow1++;
                            i++;

                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                        Console.LOG(e.getMessage(), 0);
                    } finally {
                        this.clearingHouseDAO.closeConnection();
                    }

                    /**
                     * footer summary total
                     */
                    row = sheet1.createRow(curRow1);
                    row.setHeight((short) 450);
                    cell = row.createCell(0);
                    cell.setCellValue("รวม");
                    sheet1.addMergedRegion(new CellRangeAddress(curRow1, curRow1, 0, 9));
                    cell.setCellStyle(csNum4B);

                    row.createCell(1).setCellStyle(csNum4B);
                    row.createCell(2).setCellStyle(csNum4B);
                    row.createCell(3).setCellStyle(csNum4B);
                    row.createCell(4).setCellStyle(csNum4B);
                    row.createCell(5).setCellStyle(csNum4B);
                    row.createCell(6).setCellStyle(csNum4B);
                    row.createCell(7).setCellStyle(csNum4B);
                    row.createCell(8).setCellStyle(csNum4B);
                    row.createCell(9).setCellStyle(csNum4B);

                    cell = row.createCell(10);
                    cell.setCellFormula(builderFormulaSum(10, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(11);
                    cell.setCellFormula(builderFormulaSum(11, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(12);
                    cell.setCellFormula(builderFormulaSum(12, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(13);
                    cell.setCellFormula(builderFormulaSum(13, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(14);
                    cell.setCellFormula(builderFormulaSum(14, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(15);
                    cell.setCellFormula(builderFormulaSum(15, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(16);
                    cell.setCellFormula(builderFormulaSum(16, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(17);
                    cell.setCellFormula(builderFormulaSum(17, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(18);
                    cell.setCellFormula(builderFormulaSum(18, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(19);
                    cell.setCellFormula(builderFormulaSum(19, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(20);
                    cell.setCellFormula(builderFormulaSum(20, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(21);
                    cell.setCellFormula(builderFormulaSum(21, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(22);
                    cell.setCellFormula(builderFormulaSum(22, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(23);
                    cell.setCellFormula(builderFormulaSum(23, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(24);
                    cell.setCellFormula(builderFormulaSum(24, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(25);
                    cell.setCellFormula(builderFormulaSum(25, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    workbookBase.setSheetName(j + 1, objHospital.getHosHmain());
                    Console.LOG(Message.exportSuccess(objHospital.getHosHmain()), 1);
                }
                workbookBase.removeSheetAt(0);
                workbookBase.write(out);

                out.close();
                file.close();
                programeStatus.setMessage(ConstantMessage.MSG_REPORT_SUCCESS);
                programeStatus.setTitle(ConstantMessage.MSG_REPORT_COMPLETE);
                programeStatus.setProcessStatus(true);
            } else {
                programeStatus.setMessage(ConstantMessage.MSG_REPORT_NO_DATA);
                programeStatus.setTitle(ConstantMessage.MSG_REPORT_NO_DATA);
                programeStatus.setProcessStatus(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
            programeStatus.setMessage(ConstantMessage.MSG_PROCESS_FAILS);
            programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
            programeStatus.setProcessStatus(false);
        }
        return programeStatus;
    }

    public ProgrameStatus clearing_detailDedug_hCodeByHmain(OppReport report) {
        FileOutputStream out = null;
        FileInputStream file = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        List<HospitalService> listHospital = new ArrayList<HospitalService>();
        try {
            //readTemplate 
            file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"clearing_deduct_detailHcodeByHmain.xls"));

            HSSFWorkbook wbClearing = new HSSFWorkbook(file);
            super.loadStyle(wbClearing);

            listHospital = clearingHouseDAO.getHospitalService(report);

            if (!listHospital.isEmpty()) {

                if (report.sizeListServiceCode() == 1) {
                    out = new FileOutputStream(report.getPathFile() + ""+File.separator+"opbkk_clh_deduct_detail_hcodeByHmain_" + report.getYearMonth() + "-" + report.getNo() + " " + report.getListServiceCode().get(0).getHosCode() + ".xls");
                } else {
                    out = new FileOutputStream(report.getPathFile() + ""+File.separator+"opbkk_clh_deduct_detail_hcodeByHmain_" + report.getYearMonth() + "-" + report.getNo() + ".xls");
                }

                for (int i = 0; i < listHospital.size(); i++) {  //HospitalService hospitalService : listHospital
                    HospitalService objHospital = listHospital.get(i);
                    System.out.println("hospitalService.getHosCode===>>" + objHospital.getHosCode());

                    //global varable
                    String hospital = listHospital.get(i).getHosHmain() + " : " + listHospital.get(i).getHosHmainName();

                    // Top Excell Sheet2
                    String EXCELL_HEADER2 = "สรุปการหักเงินค่าบริการผู้ป่วยนอก กรณี Clearing house ให้ศูนย์บริการสาธารณสุข";
                    String EXCELL_SERVICE2 = TITLE_DEDUCT_DETAIL + hospital;
                    String EXCELL_MONHT2 = "  งวดการจ่ายเงิน " + report.getYearMonth() + "-" + report.getNo();

                    // Start sheet 1 *******************************************************************************
                    workbookBase.getSheetAt(0);
                    HSSFSheet sheet2 = workbookBase.cloneSheet(0);
                    sheet2.createFreezePane(3, 5);

                    HSSFCell cell = null;
                    HSSFRow row = null;

                    // row 0 Header
                    row = sheet2.createRow(0);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_HEADER2);
                    sheet2.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
                    cell.setCellStyle(csHead);

                    // row 1 Hospital
                    row = sheet2.createRow(1);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_SERVICE2);
                    sheet2.addMergedRegion(new CellRangeAddress(1, 1, 0, 6));
                    cell.setCellStyle(csHead);

                    // row 1 Month
                    row = sheet2.createRow(2);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_MONHT2);
                    sheet2.addMergedRegion(new CellRangeAddress(2, 2, 0, 6));
                    cell.setCellStyle(csHead);

                    int curRow2 = 5;
                    int j = 1;

                    report.setServiceCode(listHospital.get(i).getHosHmain().toString());

                    report.setReportTypeId(99); // case special

                    ResultSet rs2 = clearingHouseDAO.genReportHCodeByHmain(report);
                    try {

                        while (rs2.next()) {

                            String vHcode = rs2.getString("hcode");
                            String vHcodename = rs2.getString("hcodename");
                            int vNpid = rs2.getInt("npid");
                            int vNvisit = rs2.getInt("nvisit");
                            double vCharge = rs2.getDouble("charge");
                            double vReimburse = rs2.getDouble("reimburse");

                            row = sheet2.createRow(curRow2);
                            row.setHeight((short) 360);
                            cell = row.createCell(0);
                            cell.setCellValue(j);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(1);
                            cell.setCellValue(vHcode);
                            cell.setCellStyle(csNumH);

                            cell = row.createCell(2);
                            cell.setCellValue(vHcodename);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(3);
                            cell.setCellValue(vNpid);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(4);
                            cell.setCellValue(vNvisit);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(5);
                            cell.setCellValue(vCharge);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(6);
                            cell.setCellValue(vReimburse);
                            cell.setCellStyle(csDouble2);

                            curRow2++;
                            j++;

                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        this.clearingHouseDAO.closeConnection();
                    }

                    /**
                     * footer summary total
                     */
                    row = sheet2.createRow(curRow2);
                    row.setHeight((short) 450);
                    cell = row.createCell(0);
                    cell.setCellValue("รวม");
                    sheet2.addMergedRegion(new CellRangeAddress(curRow2, curRow2, 0, 2));
                    cell.setCellStyle(csNum4B);

                    cell = row.createCell(1);
                    cell.setCellStyle(csNum4B);

                    cell = row.createCell(2);
                    cell.setCellStyle(csNum4B);

                    cell = row.createCell(3);
                    cell.setCellFormula("SUM(D6:D" + (curRow2) + ")");
                    cell.setCellStyle(csNum4B);

                    cell = row.createCell(4);
                    cell.setCellFormula("SUM(E6:E" + (curRow2) + ")");
                    cell.setCellStyle(csNum4B);

                    cell = row.createCell(5);
                    cell.setCellFormula("SUM(F6:F" + (curRow2) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(6);
                    cell.setCellFormula("SUM(G6:G" + (curRow2) + ")");
                    cell.setCellStyle(csDouble2B);

                    // Stop Sheet 2 ******************************************************************************
                    workbookBase.setSheetName(i + 1, objHospital.getHosHmain().toString());
                    Console.LOG("รายงาน " + objHospital.getHosHmain().toString() + "ถูกออกเรียบร้อยแล้วครับ", 1);
                }
                workbookBase.removeSheetAt(0);
                workbookBase.write(out);
                out.close();

                programeStatus.setMessage(ConstantMessage.MSG_REPORT_SUCCESS);
                programeStatus.setTitle(ConstantMessage.MSG_REPORT_COMPLETE);
                programeStatus.setProcessStatus(true);
            } else {
                programeStatus.setMessage(ConstantMessage.MSG_REPORT_NO_DATA);
                programeStatus.setTitle(ConstantMessage.MSG_NO_DATA);
                programeStatus.setProcessStatus(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
            programeStatus.setMessage(ConstantMessage.MSG_PROCESS_FAILS + e.toString());
            programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
            programeStatus.setProcessStatus(false);
        }
        return programeStatus;
    }

    public ProgrameStatus clearing_detailPay_byHcode(OppReport report) throws SQLException {
        FileInputStream file = null;
        FileOutputStream out = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        int col_last = 26;
        int row_start = 6; // index row
        int row_formula_start = row_start + 1;
        int col_txtid_width = col_last + 1;
        try {
            //readTemplate 
            file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"clearing_pay_detailByHcode.xls"));

            // style Excell
            HSSFWorkbook wbClearing = new HSSFWorkbook(file);
            super.loadStyle(wbClearing);

            List<HospitalService> listHospital = clearingHouseDAO.getHospitalService(report);

            System.out.println("ListSize==>>" + listHospital.size());

            if (!listHospital.isEmpty()) {

                if (report.sizeListServiceCode() == 1) {
                    out = new FileOutputStream(report.getPathFile() + ""+File.separator+"opbkk_clh_pay_detail_byHcode_" + report.getYearMonth() + "-" + report.getNo() + " " + report.getListServiceCode().get(0).getHosCode() + ".xls");
                } else {
                    out = new FileOutputStream(report.getPathFile() + ""+File.separator+"opbkk_clh_pay_detail_byHcode_" + report.getYearMonth() + "-" + report.getNo() + ".xls");
                }

                for (int j = 0; j < listHospital.size(); j++) {  //HospitalService hospitalService : listHospital
                    HospitalService objService = listHospital.get(j);
                    System.out.println("hospitalService.getHosCode===>>" + objService.getHosCode());

                    //global varable
                    String hospital = listHospital.get(j).getHosCode() + " : " + listHospital.get(j).getHosCodeName();
                    String dateReport = report.getMonthFullTh() + " " + report.getYearFull();

                    // Top Excell Sheet1
                    String EXCELL_HEADER1 = "รายละเอียดการจ่ายชดเชยค่าบริการผู้ป่วยนอก กรณี Clearing house ศูนย์บริการสาธารณสุข";
                    String EXCELL_SERVICE1 = "หน่วยบริการที่รักษา: " + hospital;
                    String EXCELL_MONTH = StringOpUtil.removeNull(report.getTitle2());
                    EXCELL_MONTH += "  งวดการจ่ายเงิน " + report.getYearMonth() + "-" + report.getNo();

                    // Start sheet 1 *******************************************************************************
                    workbookBase.getSheetAt(0);
                    HSSFSheet sheet1 = workbookBase.cloneSheet(0);

                    sheet1.createFreezePane(7, 6);
                    sheet1.setColumnWidth(col_txtid_width, WIDTH_TXID);

                    HSSFCell cell = null;
                    HSSFRow row = null;

                    // row 0 Header
                    row = sheet1.createRow(0);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_HEADER1);
                    sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, col_last));
                    cell.setCellStyle(csHead);

                    // row 1 Hospital
                    row = sheet1.createRow(1);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_SERVICE1);
                    sheet1.addMergedRegion(new CellRangeAddress(1, 1, 0, col_last));
                    cell.setCellStyle(csHead);

                    // row 1 Month
                    row = sheet1.createRow(2);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_MONTH);
                    sheet1.addMergedRegion(new CellRangeAddress(1, 1, 0, col_last));
                    cell.setCellStyle(csHead);

                    int curRow1 = 6;
                    int i = 1;

                    report.setServiceCode(listHospital.get(j).getHosCode());

                    ResultSet rs1 = clearingHouseDAO.getReportByHcode(report);
                    try {

                        while (rs1.next()) {

                            String vPID = rs1.getString("pid");
                            String vHN = rs1.getString("hn");
                            String vPNAME = rs1.getString("pname");
                            String vSEX = rs1.getString("sex");
                            double vAGE = rs1.getDouble("age");
                            String vDateToChar = rs1.getString("dateopd");
                            String vvDATEOPD = rs1.getString("dateopd2");
                            String vHMAIN = rs1.getString("hmain");
                            String vPAID_MODEL = rs1.getString("paid_model");
                            String vPDXCODE = rs1.getString("pdxcode");
                            double vCHA21 = rs1.getDouble("CHA21");
                            double vCHA31 = rs1.getDouble("CHA31");
                            double vCHA51 = rs1.getDouble("CHA51");
                            double vCHA61 = rs1.getDouble("CHA61");
                            double vCHA71 = rs1.getDouble("CHA71");
                            double vCHA81 = rs1.getDouble("CHA81");
                            double vCHA91 = rs1.getDouble("CHA91");
                            double vCHAA1 = rs1.getDouble("CHAA1");
                            double vCHAB1 = rs1.getDouble("CHAB1");
                            double vCHAC1 = rs1.getDouble("CHAC1");
                            double vCHAD1 = rs1.getDouble("CHAD1");
                            double vCHAE1 = rs1.getDouble("CHAE1");
                            double vCHAH1 = rs1.getDouble("CHAH1");
                            double vCHAJ1 = rs1.getDouble("CHAJ1");
                            double vCHATOTAL = rs1.getDouble("CHATOTAL");
                            double vREIMBURSE = rs1.getDouble("REIMBURSE");
                            String vTxid = rs1.getString("txid");
                            String vInvoice_no = rs1.getString("invoice_no");

                            row = sheet1.createRow(curRow1);
                            row.setHeight((short) 360);
                            cell = row.createCell(0);
                            cell.setCellValue(i);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(1);
                            cell.setCellValue(function.subStringPid(vPID));
                            cell.setCellStyle(csStringPid);

                            cell = row.createCell(2);
                            cell.setCellValue(vHN);
                            cell.setCellStyle(csNumH);

                            cell = row.createCell(3);
                            cell.setCellValue(vPNAME);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(4);
                            cell.setCellValue(vSEX);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(5);
                            cell.setCellValue(vAGE);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(6);
                            cell.setCellValue(vDateToChar);
                            cell.setCellStyle(csStringCenter);

                            cell = row.createCell(7);
                            cell.setCellValue(vHMAIN);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(8);
                            cell.setCellValue(vPAID_MODEL);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(9);
                            cell.setCellValue(vPDXCODE);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(10);
                            cell.setCellValue(vCHA21);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(11);
                            cell.setCellValue(vCHA31);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(12);
                            cell.setCellValue(vCHA51);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(13);
                            cell.setCellValue(vCHA61);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(14);
                            cell.setCellValue(vCHA71);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(15);
                            cell.setCellValue(vCHA81);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(16);
                            cell.setCellValue(vCHA91);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(17);
                            cell.setCellValue(vCHAA1);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(18);
                            cell.setCellValue(vCHAB1);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(19);
                            cell.setCellValue(vCHAC1);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(20);
                            cell.setCellValue(vCHAD1);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(21);
                            cell.setCellValue(vCHAE1);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(22);
                            cell.setCellValue(vCHAH1);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(23);
                            cell.setCellValue(vCHAJ1);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(24);
                            cell.setCellValue(vCHATOTAL);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(25);
                            cell.setCellValue(vREIMBURSE);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(26);
                            cell.setCellValue(vInvoice_no);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(27);
                            cell.setCellValue(vTxid);
                            cell.setCellStyle(csStringtxid);

                            curRow1++;
                            i++;

                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        this.clearingHouseDAO.closeConnection();
                    }

                    /**
                     * footer summary total
                     */
                    row = sheet1.createRow(curRow1);
                    row.setHeight((short) 450);
                    cell = row.createCell(0);
                    cell.setCellValue("รวม");
                    sheet1.addMergedRegion(new CellRangeAddress(curRow1, curRow1, 0, 9));
                    cell.setCellStyle(csNum4B);

                    row.createCell(1).setCellStyle(csNum4B);
                    row.createCell(2).setCellStyle(csNum4B);
                    row.createCell(3).setCellStyle(csNum4B);
                    row.createCell(4).setCellStyle(csNum4B);
                    row.createCell(5).setCellStyle(csNum4B);
                    row.createCell(6).setCellStyle(csNum4B);
                    row.createCell(7).setCellStyle(csNum4B);
                    row.createCell(8).setCellStyle(csNum4B);
                    row.createCell(9).setCellStyle(csNum4B);

                    cell = row.createCell(10);
                    cell.setCellFormula(builderFormulaSum(10, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(11);
                    cell.setCellFormula(builderFormulaSum(11, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(12);
                    cell.setCellFormula(builderFormulaSum(12, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(13);
                    cell.setCellFormula(builderFormulaSum(13, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(14);
                    cell.setCellFormula(builderFormulaSum(14, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(15);
                    cell.setCellFormula(builderFormulaSum(15, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(16);
                    cell.setCellFormula(builderFormulaSum(16, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(17);
                    cell.setCellFormula(builderFormulaSum(17, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(18);
                    cell.setCellFormula(builderFormulaSum(18, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(19);
                    cell.setCellFormula(builderFormulaSum(19, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(20);
                    cell.setCellFormula(builderFormulaSum(20, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(21);
                    cell.setCellFormula(builderFormulaSum(21, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(22);
                    cell.setCellFormula(builderFormulaSum(22, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(23);
                    cell.setCellFormula(builderFormulaSum(23, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(24);
                    cell.setCellFormula(builderFormulaSum(24, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(25);
                    cell.setCellFormula(builderFormulaSum(25, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    // Stop sheet 1 *******************************************************************************
                    workbookBase.setSheetName(j + 1, objService.getHosCode());
                }
                workbookBase.removeSheetAt(0);
                workbookBase.write(out);

                out.close();
                file.close();

                Console.LOG("clearing_detailPay_byHcode ออกรายงานเรียบร้อบแล้ว", 1);
                programeStatus.setMessage(ConstantMessage.MSG_REPORT_SUCCESS);
                programeStatus.setTitle(ConstantMessage.MSG_REPORT_COMPLETE);
                programeStatus.setProcessStatus(true);
            } else {
                programeStatus.setMessage(ConstantMessage.MSG_REPORT_NO_DATA);
                programeStatus.setTitle(ConstantMessage.MSG_NO_DATA);
                programeStatus.setProcessStatus(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
            programeStatus.setMessage(ConstantMessage.MSG_PROCESS_FAILS + e.toString());
            programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
            programeStatus.setProcessStatus(false);
        }
        return programeStatus;
    }

    public ProgrameStatus clearing_detailPay_hcodeByHmain(OppReport report) throws SQLException {
        FileInputStream file = null;
        FileOutputStream out = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        try {
            //readTemplate 
            file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"clearing_pay_detailHcodeByHmain.xls"));

            // style Excell
            HSSFWorkbook wbClearing = new HSSFWorkbook(file);
            super.loadStyle(wbClearing);

            List<HospitalService> listHospital = clearingHouseDAO.getHospitalService(report);

            System.out.println("ListSize==>>" + listHospital.size());

            if (!listHospital.isEmpty()) {

                if (report.sizeListServiceCode() == 1) {
                    out = new FileOutputStream(report.getPathFile() + ""+File.separator+"opbkk_clh_pay_detail_hcodeByHmain_" + report.getYearMonth() + "-" + report.getNo() + " " + report.getListServiceCode().get(0).getHosCode() + ".xls");
                } else {
                    out = new FileOutputStream(report.getPathFile() + ""+File.separator+"opbkk_clh_pay_detail_hcodeByHmain_" + report.getYearMonth() + "-" + report.getNo() + ".xls");
                }

                for (int i = 0; i < listHospital.size(); i++) {  //HospitalService hospitalService : listHospital
                    HospitalService objService = listHospital.get(i);
                    System.out.println("hospitalService.getHosCode===>>" + listHospital.get(i).getHosCode());

                    //global varable
                    String hospital = listHospital.get(i).getHosCode() + " : " + listHospital.get(i).getHosCodeName();
                    String dateReport = report.getMonthFullTh() + " " + report.getYearFull();

                    // Top Excell Sheet2
                    String EXCELL_HEADER2 = "สรุปการจ่ายชดเชยค่าบริการผู้ป่วยนอก กรณี Clearing house ให้ศูนย์บริการสาธารณสุข";
                    String EXCELL_SERVICE2 = "หน่วยบริการที่รักษา: " + hospital;
                    //String EXCELL_MONHT2 = " งวดที่     Clearing houseจากงบเหมาจ่ายรายหัว " + dateReport;
                    String EXCELL_MONHT2 = StringOpUtil.removeNull(report.getTitle2());//listHospital.get(i).getRemark();
                    EXCELL_MONHT2 += "  งวดการจ่ายเงิน " + report.getYearMonth() + "-" + report.getNo();

                    // Start sheet 2 *******************************************************************************
                    workbookBase.getSheetAt(0);
                    HSSFSheet sheet2 = workbookBase.cloneSheet(0);

                    HSSFCell cell = null;
                    HSSFRow row = null;

                    // row 0 Header
                    row = sheet2.createRow(0);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_HEADER2);
                    sheet2.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
                    cell.setCellStyle(csHead);

                    // row 1 Hospital Service
                    row = sheet2.createRow(1);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_SERVICE2);
                    sheet2.addMergedRegion(new CellRangeAddress(1, 1, 0, 6));
                    cell.setCellStyle(csHead);
                    // row 1 Month
                    row = sheet2.createRow(2);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_MONHT2);
                    sheet2.addMergedRegion(new CellRangeAddress(2, 2, 0, 6));
                    cell.setCellStyle(csHead);

                    int curRow2 = 5;
                    int j = 1;

                    report.setServiceCode(listHospital.get(i).getHosCode().toString());

                    report.setReportTypeId(0); // case special

                    ResultSet rs2 = clearingHouseDAO.genReportHCodeByHmain(report);
                    try {

                        while (rs2.next()) {

                            String vHmain = rs2.getString("hmain");
                            String vHmainname = rs2.getString("hmainname");
                            int vNpid = rs2.getInt("npid");
                            int vNvisit = rs2.getInt("nvisit");
                            double vCharge = rs2.getDouble("charge");
                            double vReimburse = rs2.getDouble("reimburse");

                            row = sheet2.createRow(curRow2);
                            row.setHeight((short) 360);
                            cell = row.createCell(0);
                            cell.setCellValue(j);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(1);
                            cell.setCellValue(vHmain);
                            cell.setCellStyle(csNumH);

                            cell = row.createCell(2);
                            cell.setCellValue(vHmainname);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(3);
                            cell.setCellValue(vNpid);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(4);
                            cell.setCellValue(vNvisit);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(5);
                            cell.setCellValue(vCharge);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(6);
                            cell.setCellValue(vReimburse);
                            cell.setCellStyle(csDouble2);

                            curRow2++;
                            j++;

                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        this.clearingHouseDAO.closeConnection();
                    }

                    /**
                     * footer summary total
                     */
                    row = sheet2.createRow(curRow2);
                    row.setHeight((short) 450);
                    cell = row.createCell(0);
                    cell.setCellValue("รวม");
                    sheet2.addMergedRegion(new CellRangeAddress(curRow2, curRow2, 0, 2));
                    cell.setCellStyle(csNum4B);

                    cell = row.createCell(1);
                    cell.setCellStyle(csNum4B);

                    cell = row.createCell(2);
                    cell.setCellStyle(csNum4B);

                    cell = row.createCell(3);
                    cell.setCellFormula("SUM(D6:D" + (curRow2) + ")");
                    cell.setCellStyle(csNum4B);

                    cell = row.createCell(4);
                    cell.setCellFormula("SUM(E6:E" + (curRow2) + ")");
                    cell.setCellStyle(csNum4B);

                    cell = row.createCell(5);
                    cell.setCellFormula("SUM(F6:F" + (curRow2) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(6);
                    cell.setCellFormula("SUM(G6:G" + (curRow2) + ")");
                    cell.setCellStyle(csDouble2B);

                    // Stop Sheet 2 ******************************************************************************                    
                    workbookBase.setSheetName(i + 1, objService.getHosCode().toString());
                    Console.LOG("รายงาน: " + listHospital.get(i).getHosCode().toString() + "ออกเรียบร้อยแล้ว", 1);
                }
                workbookBase.removeSheetAt(0);
                workbookBase.write(out);

                out.close();
                file.close();

                programeStatus.setMessage(ConstantMessage.MSG_REPORT_SUCCESS);
                programeStatus.setTitle(ConstantMessage.MSG_REPORT_COMPLETE);
                programeStatus.setProcessStatus(true);
            } else {
                programeStatus.setMessage(ConstantMessage.MSG_REPORT_NO_DATA);
                programeStatus.setTitle(ConstantMessage.MSG_NO_DATA);
                programeStatus.setProcessStatus(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
            programeStatus.setMessage(ConstantMessage.MSG_PROCESS_FAILS + e.toString());
            programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
            programeStatus.setProcessStatus(false);
        }
        return programeStatus;
    }

    /*
     * ###  end single file mutisheet##############################################
     */
    /*
     *  Summary ********************************************************************************************
     */
    public ProgrameStatus clearing_sumPay(OppReport report) {
        FileOutputStream out = null;
        FileInputStream file = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        try {
            //global variable

            String dateReport = report.getMonthFullTh() + " " + report.getYearFull();
            //readTemplate 
            file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"clearing_paysum.xls"));

            //write file Excell
            out = new FileOutputStream(report.getPathFile() + ""+File.separator+"opbkk_clh_pay_summary_" + report.getYearMonth() + "-" + report.getNo() + ".xls");

            // Top Excell Sheet1
            String EXCELL_HEADER1 = "สรุปการจ่ายชดเชยค่าบริการผู้ป่วยนอก กรณี Clearing house ให้ศูนย์บริการสาธารณสุข";
            // String EXCELL_SERVICE1 = "งวดที่  Clearing houseจากงบเหมาจ่ายรายหัว " + dateReport;
            String EXCELL_SERVICE1 = "";
            String EXCELL_MONTH1 = StringOpUtil.removeNull(report.getTitle2());
            EXCELL_MONTH1 += "  งวดการจ่ายเงิน " + report.getYearMonth() + "-" + report.getNo();

            HSSFWorkbook wbClearing = new HSSFWorkbook(file);
            this.loadStyle(wbClearing);

            HSSFSheet sheet = workbookBase.getSheetAt(0);
            sheet.createFreezePane(3, 5);

            HSSFCell cell = null;
            HSSFRow row = null;

            // row 0 Header
            row = sheet.createRow(0);
            row.setHeight((short) 390);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_HEADER1);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
            cell.setCellStyle(csHead);

            // row 2 Header
            row = sheet.createRow(2);
            row.setHeight((short) 390);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_MONTH1);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
            cell.setCellStyle(csHead);

            int curRow = 5;
            int i = 1;

            ResultSet rs = null;

            rs = clearingHouseDAO.getReportByHcode(report);

            try {

                while (rs.next()) {

                    String vhcode = rs.getString("hcode");
                    String vhcodename = rs.getString("hcodename");
                    int vnpid = rs.getInt("npid");
                    int vnvisit = rs.getInt("nvisit");
                    double vcharge = rs.getDouble("charge");
                    double vreimburse = rs.getDouble("reimburse");

                    row = sheet.createRow(curRow);
                    row.setHeight((short) 360);
                    cell = row.createCell(0);
                    cell.setCellValue(i);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(1);
                    cell.setCellValue(vhcode);
                    cell.setCellStyle(csNumH);

                    cell = row.createCell(2);
                    cell.setCellValue(vhcodename);
                    cell.setCellStyle(csStringLeft);

                    cell = row.createCell(3);
                    cell.setCellValue(vnpid);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(4);
                    cell.setCellValue(vnvisit);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(5);
                    cell.setCellValue(vcharge);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(6);
                    cell.setCellValue(vreimburse);
                    cell.setCellStyle(csDouble2);

                    if (i == 1) {
                        EXCELL_SERVICE1 = "";
                    }

                    curRow++;
                    i++;

                }

            } catch (SQLException e) {
                e.printStackTrace();
                Console.LOG(e.getMessage(), 0);
                programeStatus.setMessage(ConstantMessage.MSG_PROCESS_FAILS + e.toString());
                programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
                programeStatus.setProcessStatus(false);
            } finally {
                this.clearingHouseDAO.closeConnection();
            }
            /**
             * footer summary total
             */
            row = sheet.createRow(curRow);
            row.setHeight((short) 450);
            cell = row.createCell(0);
            cell.setCellValue("รวม");
            sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 2));
            cell.setCellStyle(csNum4B);

            cell = row.createCell(1);
            cell.setCellStyle(csHeadTab);
            cell = row.createCell(2);
            cell.setCellStyle(csHeadTab);
            cell = row.createCell(3);
            cell.setCellFormula("SUM(D5:D" + (curRow) + ")");
            cell.setCellStyle(csNum4B);
            cell = row.createCell(4);
            cell.setCellFormula("SUM(E5:E" + (curRow) + ")");
            cell.setCellStyle(csNum4B);
            cell = row.createCell(5);
            cell.setCellFormula("SUM(F5:F" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);
            cell = row.createCell(6);
            cell.setCellFormula("SUM(G5:G" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

//
//            // row 1 Month
            row = sheet.createRow(1);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_SERVICE1);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 6));
            cell.setCellStyle(csHead);

            workbookBase.write(out);
            out.close();
            Console.LOG("clearing_sumPay ถูกออกเรียบร้อยแล้ว", 1);

            programeStatus.setMessage(ConstantMessage.MSG_REPORT_SUCCESS);
            programeStatus.setTitle(ConstantMessage.MSG_REPORT_COMPLETE);
            programeStatus.setProcessStatus(true);

        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
            programeStatus.setMessage(ConstantMessage.MSG_PROCESS_FAILS + e.toString());
            programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
            programeStatus.setProcessStatus(false);
        }
        return programeStatus;
    }

    public ProgrameStatus clearing_sumDeduct(OppReport report) {
        FileOutputStream out = null;
        FileInputStream file = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        try {
            //global varable

            //readTemplate 
            file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"clearing_deductsum.xls"));

            //write file Excell
            out = new FileOutputStream(report.getPathFile() + ""+File.separator+"opbkk_clh_deduct_summary_" + report.getYearMonth() + "-" + report.getNo() + ".xls");

            // Top Excell Sheet1
            String EXCELL_HEADER1 = "สรุปการหักเงินค่าบริการผู้ป่วยนอก กรณี Clearing house ให้ศูนย์บริการสาธารณสุข";
            // String EXCELL_SERVICE1 = "งวดที่  Clearing houseจากงบเหมาจ่ายรายหัว " + dateReport;
            String EXCELL_SERVICE1 = "";
            String EXCELL_MONTH1 = StringOpUtil.removeNull(report.getTitle2());
            EXCELL_MONTH1 += "  งวดการจ่ายเงิน " + report.getYearMonth() + "-" + report.getNo();

            HSSFWorkbook wbClearing = new HSSFWorkbook(file);
            this.loadStyle(wbClearing);

            HSSFSheet sheet = workbookBase.getSheetAt(0);
            sheet.createFreezePane(3, 5);

            HSSFCell cell = null;
            HSSFRow row = null;

            // row 0 Header
            row = sheet.createRow(0);
            row.setHeight((short) 390);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_HEADER1);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
            cell.setCellStyle(csHead);

            // row 2 Month
            row = sheet.createRow(2);
            row.setHeight((short) 390);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_MONTH1);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
            cell.setCellStyle(csHead);

            int curRow = 5;
            int i = 1;

            ResultSet rs = null;

            rs = clearingHouseDAO.getReportByHcode(report);

            try {

                while (rs.next()) {

                    String vhmain = rs.getString("hmain");
                    String vhmainname = rs.getString("hmainname");
                    int vnpid = rs.getInt("npid");
                    int vnvisit = rs.getInt("nvisit");
                    double vcharge = rs.getDouble("charge");
                    double vreimburse = rs.getDouble("reimburse");

                    row = sheet.createRow(curRow);
                    row.setHeight((short) 360);
                    cell = row.createCell(0);
                    cell.setCellValue(i);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(1);
                    cell.setCellValue(vhmain);
                    cell.setCellStyle(csNumH);

                    cell = row.createCell(2);
                    cell.setCellValue(vhmainname);
                    cell.setCellStyle(csStringLeft);

                    cell = row.createCell(3);
                    cell.setCellValue(vnpid);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(4);
                    cell.setCellValue(vnvisit);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(5);
                    cell.setCellValue(vcharge);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(6);
                    cell.setCellValue(vreimburse);
                    cell.setCellStyle(csDouble2);

                    if (i == 1) {
                        EXCELL_SERVICE1 = "";
                    }
                    curRow++;
                    i++;

                }

            } catch (SQLException e) {
                e.printStackTrace();
                Console.LOG(e.getMessage(), 0);
                programeStatus.setMessage(ConstantMessage.MSG_PROCESS_FAILS + e.toString());
                programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
                programeStatus.setProcessStatus(false);
            } finally {
                this.clearingHouseDAO.closeConnection();
            }

            /**
             * footer summary total
             */
            row = sheet.createRow(curRow);
            row.setHeight((short) 450);
            cell = row.createCell(0);
            cell.setCellValue("รวม");
            sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 2));
            cell.setCellStyle(csNum4B);

            cell = row.createCell(1);
            cell.setCellStyle(csNum4B);
            cell = row.createCell(2);
            cell.setCellStyle(csNum4B);
            cell = row.createCell(3);
            cell.setCellFormula("SUM(D5:D" + (curRow) + ")");
            cell.setCellStyle(csNum4B);
            cell = row.createCell(4);
            cell.setCellFormula("SUM(E5:E" + (curRow) + ")");
            cell.setCellStyle(csNum4B);
            cell = row.createCell(5);
            cell.setCellFormula("SUM(F5:F" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);
            cell = row.createCell(6);
            cell.setCellFormula("SUM(G5:G" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            // row 1 Month
            row = sheet.createRow(1);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_SERVICE1);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 6));
            cell.setCellStyle(csHead);

            workbookBase.write(out);
            out.close();
            Console.LOG("clearing_sumDeduct ออกรายงานเรียบร้อยแล้ว", 1);

            programeStatus.setMessage(ConstantMessage.MSG_REPORT_SUCCESS);
            programeStatus.setTitle(ConstantMessage.MSG_REPORT_COMPLETE);
            programeStatus.setProcessStatus(true);
        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
            programeStatus.setMessage(ConstantMessage.MSG_PROCESS_FAILS + e.toString());
            programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
            programeStatus.setProcessStatus(false);
        }
        return programeStatus;
    }

    /*
     *  Single File Single Sheet ********************************************************************************
     */
    public boolean clearing_detailDedug_hCodeByHmain_1file1sheet(OppReport report) {
        FileOutputStream out = null;
        FileInputStream file = null;
        boolean status = false;
        try {

            List<HospitalService> listHospital = clearingHouseDAO.getHospitalService(report);

            if (!listHospital.isEmpty()) {

                for (int i = 0; i < listHospital.size(); i++) {  //HospitalService hospitalService : listHospital
                    HospitalService objHospital = listHospital.get(i);
                    System.out.println("hospitalService.getHosCode===>>" + listHospital.get(i).getHosCode());

                    //readTemplate 
                    file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"clearing_deduct_detailHcodeByHmain.xls"));

                    new FunctionUtil().createFolder(report.getPathFile() + ""+File.separator+"opbkk_clh_deduct_detail_hcodeByHmain_" + report.getYearMonth() + "-" + report.getNo());
                    out = new FileOutputStream(report.getPathFile() + ""+File.separator+"opbkk_clh_deduct_detail_hcodeByHmain_" + report.getYearMonth() + "-" + report.getNo() + ""+File.separator+""
                            + listHospital.get(i).getHosHmain().toString()
                            + "_" + report.getYearMonth() + "-" + report.getNo() + ".xls");

                    HSSFWorkbook wbClearing = new HSSFWorkbook(file);
                    this.loadStyle(wbClearing);

                    //global varable
                    String hospital = listHospital.get(i).getHosHmain() + " : " + listHospital.get(i).getHosHmainName();

                    // Top Excell Sheet2
                    String EXCELL_HEADER2 = "สรุปการหักเงินค่าบริการผู้ป่วยนอก กรณี Clearing house ให้ศูนย์บริการสาธารณสุข";
                    String EXCELL_SERVICE2 = TITLE_DEDUCT_DETAIL + hospital;
                    String EXCELL_MONHT2 = "  งวดการจ่ายเงิน " + report.getYearMonth() + "-" + report.getNo();

                    // Start sheet 1 *******************************************************************************
                    HSSFSheet sheet2 = workbookBase.getSheetAt(0);
                    sheet2.createFreezePane(3, 5);

                    HSSFCell cell = null;
                    HSSFRow row = null;

                    // row 0 Header
                    row = sheet2.createRow(0);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_HEADER2);
                    sheet2.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
                    cell.setCellStyle(csHead);

                    // row 1 Hospital
                    row = sheet2.createRow(1);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_SERVICE2);
                    sheet2.addMergedRegion(new CellRangeAddress(1, 1, 0, 6));
                    cell.setCellStyle(csHead);

                    // row 1 Month
                    row = sheet2.createRow(2);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_MONHT2);
                    sheet2.addMergedRegion(new CellRangeAddress(2, 2, 0, 6));
                    cell.setCellStyle(csHead);

                    int curRow2 = 5;
                    int j = 1;

                    report.setServiceCode(listHospital.get(i).getHosHmain().toString());

                    report.setReportTypeId(99); // case special

                    ResultSet rs2 = clearingHouseDAO.genReportHCodeByHmain(report);
                    try {

                        while (rs2.next()) {

                            String vHcode = rs2.getString("hcode");
                            String vHcodename = rs2.getString("hcodename");
                            int vNpid = rs2.getInt("npid");
                            int vNvisit = rs2.getInt("nvisit");
                            double vCharge = rs2.getDouble("charge");
                            double vReimburse = rs2.getDouble("reimburse");

                            row = sheet2.createRow(curRow2);
                            row.setHeight((short) 360);
                            cell = row.createCell(0);
                            cell.setCellValue(j);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(1);
                            cell.setCellValue(vHcode);
                            cell.setCellStyle(csNumH);

                            cell = row.createCell(2);
                            cell.setCellValue(vHcodename);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(3);
                            cell.setCellValue(vNpid);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(4);
                            cell.setCellValue(vNvisit);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(5);
                            cell.setCellValue(vCharge);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(6);
                            cell.setCellValue(vReimburse);
                            cell.setCellStyle(csDouble2);

                            curRow2++;
                            j++;

                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        this.clearingHouseDAO.closeConnection();
                    }

                    /**
                     * footer summary total
                     */
                    row = sheet2.createRow(curRow2);
                    row.setHeight((short) 450);
                    cell = row.createCell(0);
                    cell.setCellValue("รวม");
                    sheet2.addMergedRegion(new CellRangeAddress(curRow2, curRow2, 0, 2));
                    cell.setCellStyle(csNum4B);

                    cell = row.createCell(1);
                    cell.setCellStyle(csNum4B);

                    cell = row.createCell(2);
                    cell.setCellStyle(csNum4B);

                    cell = row.createCell(3);
                    cell.setCellFormula("SUM(D6:D" + (curRow2) + ")");
                    cell.setCellStyle(csNum4B);

                    cell = row.createCell(4);
                    cell.setCellFormula("SUM(E6:E" + (curRow2) + ")");
                    cell.setCellStyle(csNum4B);

                    cell = row.createCell(5);
                    cell.setCellFormula("SUM(F6:F" + (curRow2) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(6);
                    cell.setCellFormula("SUM(G6:G" + (curRow2) + ")");
                    cell.setCellStyle(csDouble2B);

                    // Stop Sheet 2 ******************************************************************************
                    // wb.setSheetName(i + 1, listHospital.get(i).getHosHmainCode().toString());
                    Console.LOG(Message.exportSuccess(listHospital.get(i).getHosHmain()), 1);
                    workbookBase.write(out);
                    out.close();
                    status = true;
                }
                //wb.removeSheetAt(0);

            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
        }
        return status;
    }

    public boolean clearing_detailPay_hcodeByHmain_1file1sheet(OppReport report) throws SQLException {
        FileInputStream file = null;
        FileOutputStream out = null;
        boolean status = false;
        try {

            List<HospitalService> listHospital = clearingHouseDAO.getHospitalService(report);

            System.out.println("ListSize==>>" + listHospital.size());

            if (!listHospital.isEmpty()) {

                for (int i = 0; i < listHospital.size(); i++) {  //HospitalService hospitalService : listHospital
                    System.out.println("hospitalService.getHosCode===>>" + listHospital.get(i).getHosCode());

                    //readTemplate 
                    file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"clearing_pay_detailHcodeByHmain.xls"));

                    new FunctionUtil().createFolder(report.getPathFile() + ""+File.separator+"opbkk_clh_pay_detail_hcodeByHmain_" + report.getYearMonth() + "-" + report.getNo());
                    out = new FileOutputStream(report.getPathFile() + ""+File.separator+"opbkk_clh_pay_detail_hcodeByHmain_" + report.getYearMonth() + "-" + report.getNo() + ""+File.separator+""
                            + listHospital.get(i).getHosCode()
                            + "_" + report.getYearMonth() + "-" + report.getNo() + ".xls");

                    // style Excell
                    HSSFWorkbook wbClearing = new HSSFWorkbook(file);
                    this.loadStyle(wbClearing);

                    //global varable
                    String hospital = listHospital.get(i).getHosCode() + " : " + listHospital.get(i).getHosCodeName();

                    // Top Excell Sheet2
                    String EXCELL_HEADER2 = "สรุปการจ่ายชดเชยค่าบริการผู้ป่วยนอก กรณี Clearing house ให้ศูนย์บริการสาธารณสุข";
                    String EXCELL_SERVICE2 = "หน่วยบริการที่รักษา: " + hospital;
                    //String EXCELL_MONHT2 = " งวดที่     Clearing houseจากงบเหมาจ่ายรายหัว " + dateReport;
                    String EXCELL_MONHT2 = StringOpUtil.removeNull(report.getTitle2());//listHospital.get(i).getRemark();
                    EXCELL_MONHT2 += "  งวดการจ่ายเงิน " + report.getYearMonth() + "-" + report.getNo();

                    // Start sheet 2 *******************************************************************************
                    HSSFSheet sheet2 = workbookBase.getSheetAt(0);

                    HSSFCell cell = null;
                    HSSFRow row = null;

                    // row 0 Header
                    row = sheet2.createRow(0);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_HEADER2);
                    sheet2.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
                    cell.setCellStyle(csHead);

                    // row 1 Hospital Service
                    row = sheet2.createRow(1);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_SERVICE2);
                    sheet2.addMergedRegion(new CellRangeAddress(1, 1, 0, 6));
                    cell.setCellStyle(csHead);
                    // row 1 Month
                    row = sheet2.createRow(2);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_MONHT2);
                    sheet2.addMergedRegion(new CellRangeAddress(2, 2, 0, 6));
                    cell.setCellStyle(csHead);

                    int curRow2 = 5;
                    int j = 1;

                    report.setServiceCode(listHospital.get(i).getHosCode().toString());

                    report.setReportTypeId(0); // case special

                    ResultSet rs2 = clearingHouseDAO.genReportHCodeByHmain(report);
                    try {

                        while (rs2.next()) {

                            String vHmain = rs2.getString("hmain");
                            String vHmainname = rs2.getString("hmainname");
                            int vNpid = rs2.getInt("npid");
                            int vNvisit = rs2.getInt("nvisit");
                            double vCharge = rs2.getDouble("charge");
                            double vReimburse = rs2.getDouble("reimburse");

                            row = sheet2.createRow(curRow2);
                            row.setHeight((short) 360);
                            cell = row.createCell(0);
                            cell.setCellValue(j);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(1);
                            cell.setCellValue(vHmain);
                            cell.setCellStyle(csNumH);

                            cell = row.createCell(2);
                            cell.setCellValue(vHmainname);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(3);
                            cell.setCellValue(vNpid);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(4);
                            cell.setCellValue(vNvisit);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(5);
                            cell.setCellValue(vCharge);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(6);
                            cell.setCellValue(vReimburse);
                            cell.setCellStyle(csDouble2);

                            curRow2++;
                            j++;

                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        this.clearingHouseDAO.closeConnection();
                    }

                    /**
                     * footer summary total
                     */
                    row = sheet2.createRow(curRow2);
                    row.setHeight((short) 450);
                    cell = row.createCell(0);
                    cell.setCellValue("รวม");
                    sheet2.addMergedRegion(new CellRangeAddress(curRow2, curRow2, 0, 2));
                    cell.setCellStyle(csNum4B);

                    cell = row.createCell(1);
                    cell.setCellStyle(csNum4B);

                    cell = row.createCell(2);
                    cell.setCellStyle(csNum4B);

                    cell = row.createCell(3);
                    cell.setCellFormula("SUM(D6:D" + (curRow2) + ")");
                    cell.setCellStyle(csNum4B);

                    cell = row.createCell(4);
                    cell.setCellFormula("SUM(E6:E" + (curRow2) + ")");
                    cell.setCellStyle(csNum4B);

                    cell = row.createCell(5);
                    cell.setCellFormula("SUM(F6:F" + (curRow2) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(6);
                    cell.setCellFormula("SUM(G6:G" + (curRow2) + ")");
                    cell.setCellStyle(csDouble2B);

                    // Stop Sheet 2 ******************************************************************************
                    //wb.setSheetName(i + 1, listHospital.get(i).getHosCode().toString());
                    Console.LOG("รายงาน: " + listHospital.get(i).getHosCode().toString() + "ออกเรียบร้อยแล้ว", 1);
                    workbookBase.write(out);

                    out.close();
                    file.close();
                    status = true;
                }
                // wb.removeSheetAt(0);

            } else {
                System.out.println(" ไม่มีข้อมูลให้ออกรายงาน");
                Console.LOG("ไม่มีข้อมูลให้ออกรายงาน", 2);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
        }
        return status;
    }

    /*
     *  ######################## For Web site  claim ########################
     */
    public boolean clearing_detailDedug_byHCode_1file1sheet(OppReport report) {
        FileOutputStream out = null;
        FileInputStream file = null;
        boolean status = false;
        String stmp = StringOpUtil.removeNull(report.getYearMonth()) + "-" + report.getNo();
        String pathDirectory = report.getPathFile() + ""+File.separator+"clh"+File.separator+"" + stmp;
        int col_last = 26;
        int row_start = 6; // index row
        int row_formula_start = row_start + 1;
        int col_txtid_width = col_last + 1;
        try {

            List<HospitalService> listHospital = clearingHouseDAO.getHospitalService(report);

            if (!listHospital.isEmpty()) {

                for (int j = 0; j < listHospital.size(); j++) {  //HospitalService hospitalService : listHospital
                    HospitalService objHospital = listHospital.get(j);
                    System.out.println("hospitalService.getHosCode===>>" + objHospital.getHosCode());

                    //readTemplate 
                    file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"clearing_deduct_detailByHcode.xls"));
                    if (report.getFor_use() == 0) { // 0 =  for genaral use 
                        new FunctionUtil().createFolder(report.getPathFile() + ""+File.separator+"opbkk_clh_deduct_detail_byHcode_" + report.getYearMonth() + "-" + report.getNo());
                        out = new FileOutputStream(report.getPathFile() + ""+File.separator+"opbkk_clh_deduct_detail_byHcode_" + report.getYearMonth() + "-" + report.getNo() + ""+File.separator+""
                                + objHospital.getHosHmain()
                                + "_" + report.getYearMonth() + "-" + report.getNo() + ".xls");
                    } else { // 1=  for web site claim
                        new FunctionUtil().createMutiDirectory(new File(pathDirectory));
                        out = new FileOutputStream(pathDirectory + ""+File.separator+"clh_deduct_detail_" + objHospital.getHosHmain() + "_" + stmp + ".xls");
                    }
                    // style Excell
                    HSSFWorkbook wbClearing = new HSSFWorkbook(file);
                    this.loadStyle(wbClearing);

                    //global varable
                    String hospital = listHospital.get(j).getHosHmain() + " : " + listHospital.get(j).getHosHmainName();

                    // Top Excell Sheet1
                    String EXCELL_HEADER1 = "รายละเอียดการหักเงินค่าบริการผู้ป่วยนอก กรณี Clearing house ศูนย์บริการสาธารณสุข";
                    String EXCELL_SERVICE1 = TITLE_DEDUCT_DETAIL + hospital;
                    String EXCELL_MONTH = "  งวดการจ่ายเงิน " + report.getYearMonth() + "-" + report.getNo();

                    // Start sheet 1 *******************************************************************************
                    HSSFSheet sheet1 = workbookBase.getSheetAt(0);

                    sheet1.createFreezePane(7, 6);
                    sheet1.setColumnWidth(col_txtid_width, WIDTH_TXID);

                    HSSFCell cell = null;
                    HSSFRow row = null;

                    // row 0 Header
                    row = sheet1.createRow(0);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_HEADER1);
                    sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, col_last));
                    cell.setCellStyle(csHead);

                    // row 1 Hospital
                    row = sheet1.createRow(1);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_SERVICE1);
                    sheet1.addMergedRegion(new CellRangeAddress(1, 1, 0, col_last));
                    cell.setCellStyle(csHead);

                    // row 2 Month
                    row = sheet1.createRow(2);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_MONTH);
                    sheet1.addMergedRegion(new CellRangeAddress(1, 1, 0, col_last));
                    cell.setCellStyle(csHead);

                    int curRow1 = 6;
                    int i = 1;

                    report.setServiceCode(listHospital.get(j).getHosHmain());

                    ResultSet rs1 = clearingHouseDAO.getReportByHcode(report);
                    try {

                        while (rs1.next()) {

                            String vPID = rs1.getString("pid");
                            String vHN = rs1.getString("hn");
                            String vPNAME = rs1.getString("pname");
                            String vSEX = rs1.getString("sex");
                            double vAGE = rs1.getDouble("age");
                            String vDateToChar = rs1.getString("dateopd");
                            String vvDATEOPD = rs1.getString("dateopd2");
                            String vHcode = rs1.getString("hcode");
                            String vPAID_MODEL = rs1.getString("paid_model");
                            String vPDXCODE = rs1.getString("pdxcode");
                            double vCHA21 = rs1.getDouble("CHA21");
                            double vCHA31 = rs1.getDouble("CHA31");
                            double vCHA51 = rs1.getDouble("CHA51");
                            double vCHA61 = rs1.getDouble("CHA61");
                            double vCHA71 = rs1.getDouble("CHA71");
                            double vCHA81 = rs1.getDouble("CHA81");
                            double vCHA91 = rs1.getDouble("CHA91");
                            double vCHAA1 = rs1.getDouble("CHAA1");
                            double vCHAB1 = rs1.getDouble("CHAB1");
                            double vCHAC1 = rs1.getDouble("CHAC1");
                            double vCHAD1 = rs1.getDouble("CHAD1");
                            double vCHAE1 = rs1.getDouble("CHAE1");
                            double vCHAH1 = rs1.getDouble("CHAH1");
                            double vCHAJ1 = rs1.getDouble("CHAJ1");
                            double vCHATOTAL = rs1.getDouble("CHATOTAL");
                            double vREIMBURSE = rs1.getDouble("REIMBURSE");
                            String vTxid = rs1.getString("txid");

                            String vInvoice_no = rs1.getString("invoice_no");

                            row = sheet1.createRow(curRow1);
                            row.setHeight((short) 360);
                            cell = row.createCell(0);
                            cell.setCellValue(i);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(1);
                            cell.setCellValue(function.subStringPid(vPID));
                            cell.setCellStyle(csStringPid);

                            cell = row.createCell(2);
                            cell.setCellValue(vHN);
                            cell.setCellStyle(csNumH);

                            cell = row.createCell(3);
                            cell.setCellValue(vPNAME);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(4);
                            cell.setCellValue(vSEX);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(5);
                            cell.setCellValue(vAGE);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(6);
                            cell.setCellValue(vDateToChar);
                            cell.setCellStyle(csStringCenter);

                            cell = row.createCell(7);
                            cell.setCellValue(vHcode);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(8);
                            cell.setCellValue(vPAID_MODEL);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(9);
                            cell.setCellValue(vPDXCODE);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(10);
                            cell.setCellValue(vCHA21);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(11);
                            cell.setCellValue(vCHA31);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(12);
                            cell.setCellValue(vCHA51);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(13);
                            cell.setCellValue(vCHA61);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(14);
                            cell.setCellValue(vCHA71);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(15);
                            cell.setCellValue(vCHA81);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(16);
                            cell.setCellValue(vCHA91);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(17);
                            cell.setCellValue(vCHAA1);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(18);
                            cell.setCellValue(vCHAB1);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(19);
                            cell.setCellValue(vCHAC1);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(20);
                            cell.setCellValue(vCHAD1);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(21);
                            cell.setCellValue(vCHAE1);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(22);
                            cell.setCellValue(vCHAH1);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(23);
                            cell.setCellValue(vCHAJ1);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(24);
                            cell.setCellValue(vCHATOTAL);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(25);
                            cell.setCellValue(vREIMBURSE);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(26);
                            cell.setCellValue(vInvoice_no);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(27);
                            cell.setCellValue(vTxid);
                            cell.setCellStyle(csStringtxid);

                            curRow1++;
                            i++;

                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                        Console.LOG(e.getMessage(), 0);
                    } finally {
                        this.clearingHouseDAO.closeConnection();
                    }

                    /**
                     * footer summary total
                     */
                    row = sheet1.createRow(curRow1);
                    row.setHeight((short) 450);
                    cell = row.createCell(0);
                    cell.setCellValue("รวม");
                    sheet1.addMergedRegion(new CellRangeAddress(curRow1, curRow1, 0, 9));
                    cell.setCellStyle(csNum4B);

                    row.createCell(1).setCellStyle(csNum4B);
                    row.createCell(2).setCellStyle(csNum4B);
                    row.createCell(3).setCellStyle(csNum4B);
                    row.createCell(4).setCellStyle(csNum4B);
                    row.createCell(5).setCellStyle(csNum4B);
                    row.createCell(6).setCellStyle(csNum4B);
                    row.createCell(7).setCellStyle(csNum4B);
                    row.createCell(8).setCellStyle(csNum4B);
                    row.createCell(9).setCellStyle(csNum4B);

                    cell = row.createCell(10);
                    cell.setCellFormula(builderFormulaSum(10, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(11);
                    cell.setCellFormula(builderFormulaSum(11, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(12);
                    cell.setCellFormula(builderFormulaSum(12, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(13);
                    cell.setCellFormula(builderFormulaSum(13, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(14);
                    cell.setCellFormula(builderFormulaSum(14, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(15);
                    cell.setCellFormula(builderFormulaSum(15, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(16);
                    cell.setCellFormula(builderFormulaSum(16, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(17);
                    cell.setCellFormula(builderFormulaSum(17, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(18);
                    cell.setCellFormula(builderFormulaSum(18, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(19);
                    cell.setCellFormula(builderFormulaSum(19, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(20);
                    cell.setCellFormula(builderFormulaSum(20, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(21);
                    cell.setCellFormula(builderFormulaSum(21, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(22);
                    cell.setCellFormula(builderFormulaSum(22, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(23);
                    cell.setCellFormula(builderFormulaSum(23, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(24);
                    cell.setCellFormula(builderFormulaSum(24, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(25);
                    cell.setCellFormula(builderFormulaSum(25, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    //wb.setSheetName(j + 1, listHospital.get(j).getHosHmainCode().toString());
                    Console.LOG(Message.exportSuccess(listHospital.get(j).getHosHmain()), 1);

                    //wb.removeSheetAt(0);
                    workbookBase.write(out);

                    out.close();
                    file.close();
                    status = true;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
            return false;
        }
        return true;
    }

    public boolean clearing_detailPay_byHcode_1file1sheet(OppReport report) throws SQLException {
        FileInputStream file = null;
        FileOutputStream out = null;
        boolean status = false;
        String stmp = StringOpUtil.removeNull(report.getYearMonth()) + "-" + report.getNo();
        String pathDirectory = report.getPathFile() + ""+File.separator+"clh"+File.separator+"" + stmp;
        int col_last = 26;
        int row_start = 6; // index row
        int row_formula_start = row_start + 1;
        int col_txtid_width = col_last + 1;
        try {

            List<HospitalService> listHospital = clearingHouseDAO.getHospitalService(report);

            if (!listHospital.isEmpty()) {

                for (int j = 0; j < listHospital.size(); j++) {  //HospitalService hospitalService : listHospital
                    HospitalService objService = listHospital.get(j);

                    //readTemplate 
                    file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"clearing_pay_detailByHcode.xls"));
                    if (report.getFor_use() == 0) { // 0 = for Ganaral Use ใช้ทั่วไป
                        new FunctionUtil().createFolder(report.getPathFile() + ""+File.separator+"opbkk_clh_pay_detail_byHcode_" + report.getYearMonth() + "-" + report.getNo());
                        out = new FileOutputStream(report.getPathFile() + ""+File.separator+"opbkk_clh_pay_detail_byHcode_" + report.getYearMonth() + "-" + report.getNo() + ""+File.separator+""
                                + objService.getHosCode().toString()
                                + "_" + report.getYearMonth() + "-" + report.getNo() + ".xls");
                    } else {  //  1= for upload to web site claim
                        new FunctionUtil().createMutiDirectory(new File(pathDirectory));
                        out = new FileOutputStream(pathDirectory + ""+File.separator+"clh_paid_detail_" + objService.getHosCode() + "_" + stmp + ".xls");
                    }
                    // style Excell
                    HSSFWorkbook wbClearing = new HSSFWorkbook(file);
                    this.loadStyle(wbClearing);

                    //global varable
                    String hospital = listHospital.get(j).getHosCode() + " : " + listHospital.get(j).getHosCodeName();

                    // Top Excell Sheet1
                    String EXCELL_HEADER1 = "รายละเอียดการจ่ายชดเชยค่าบริการผู้ป่วยนอก กรณี Clearing house ศูนย์บริการสาธารณสุข";
                    String EXCELL_SERVICE1 = "หน่วยบริการที่รักษา: " + hospital;
                    String EXCELL_MONTH1 = StringOpUtil.removeNull(report.getTitle2());
                    EXCELL_MONTH1 += "  งวดการจ่ายเงิน " + report.getYearMonth() + "-" + report.getNo();

                    //String EXCELL_SERVICE1 = "หน่วยบริการที่รักษา: " + hospital + "  งวดที่  " + listHospital.get(j).getRemark();
                    // Start sheet 1 *******************************************************************************
                    HSSFSheet sheet1 = workbookBase.getSheetAt(0);

                    sheet1.createFreezePane(7, 6);
                    sheet1.setColumnWidth(col_txtid_width, WIDTH_TXID);

                    HSSFCell cell = null;
                    HSSFRow row = null;

                    // row 0 Header
                    row = sheet1.createRow(0);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_HEADER1);
                    sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, col_last));
                    cell.setCellStyle(csHead);

                    // row 1 Hospital
                    row = sheet1.createRow(1);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_SERVICE1);
                    sheet1.addMergedRegion(new CellRangeAddress(1, 1, 0, col_last));
                    cell.setCellStyle(csHead);

                    // row 2 Month
                    row = sheet1.createRow(2);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_MONTH1);
                    sheet1.addMergedRegion(new CellRangeAddress(1, 1, 0, col_last));
                    cell.setCellStyle(csHead);

                    int curRow1 = row_start;
                    int i = 1;

                    report.setServiceCode(listHospital.get(j).getHosCode());

                    ResultSet rs1 = clearingHouseDAO.getReportByHcode(report);
                    try {

                        while (rs1.next()) {

                            String vPID = rs1.getString("pid");
                            String vHN = rs1.getString("hn");
                            String vPNAME = rs1.getString("pname");
                            String vSEX = rs1.getString("sex");
                            double vAGE = rs1.getDouble("age");
                            String vDateToChar = rs1.getString("dateopd");
                            String vvDATEOPD = rs1.getString("dateopd2");
                            String vHMAIN = rs1.getString("hmain");
                            String vPAID_MODEL = rs1.getString("paid_model");
                            String vPDXCODE = rs1.getString("pdxcode");
                            double vCHA21 = rs1.getDouble("CHA21");
                            double vCHA31 = rs1.getDouble("CHA31");
                            double vCHA51 = rs1.getDouble("CHA51");
                            double vCHA61 = rs1.getDouble("CHA61");
                            double vCHA71 = rs1.getDouble("CHA71");
                            double vCHA81 = rs1.getDouble("CHA81");
                            double vCHA91 = rs1.getDouble("CHA91");
                            double vCHAA1 = rs1.getDouble("CHAA1");
                            double vCHAB1 = rs1.getDouble("CHAB1");
                            double vCHAC1 = rs1.getDouble("CHAC1");
                            double vCHAD1 = rs1.getDouble("CHAD1");
                            double vCHAE1 = rs1.getDouble("CHAE1");
                            double vCHAH1 = rs1.getDouble("CHAH1");
                            double vCHAJ1 = rs1.getDouble("CHAJ1");
                            double vCHATOTAL = rs1.getDouble("CHATOTAL");
                            double vREIMBURSE = rs1.getDouble("REIMBURSE");
                            String vTxid = rs1.getString("txid");
                            String vInvoice_no = rs1.getString("invoice_no");

                            row = sheet1.createRow(curRow1);
                            row.setHeight((short) 360);
                            cell = row.createCell(0);
                            cell.setCellValue(i);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(1);
                            cell.setCellValue(function.subStringPid(vPID));
                            cell.setCellStyle(csStringPid);

                            cell = row.createCell(2);
                            cell.setCellValue(vHN);
                            cell.setCellStyle(csNumH);

                            cell = row.createCell(3);
                            cell.setCellValue(vPNAME);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(4);
                            cell.setCellValue(vSEX);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(5);
                            cell.setCellValue(vAGE);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(6);
                            cell.setCellValue(vDateToChar);
                            cell.setCellStyle(csStringCenter);

                            cell = row.createCell(7);
                            cell.setCellValue(vHMAIN);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(8);
                            cell.setCellValue(vPAID_MODEL);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(9);
                            cell.setCellValue(vPDXCODE);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(10);
                            cell.setCellValue(vCHA21);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(11);
                            cell.setCellValue(vCHA31);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(12);
                            cell.setCellValue(vCHA51);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(13);
                            cell.setCellValue(vCHA61);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(14);
                            cell.setCellValue(vCHA71);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(15);
                            cell.setCellValue(vCHA81);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(16);
                            cell.setCellValue(vCHA91);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(17);
                            cell.setCellValue(vCHAA1);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(18);
                            cell.setCellValue(vCHAB1);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(19);
                            cell.setCellValue(vCHAC1);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(20);
                            cell.setCellValue(vCHAD1);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(21);
                            cell.setCellValue(vCHAE1);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(22);
                            cell.setCellValue(vCHAH1);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(23);
                            cell.setCellValue(vCHAJ1);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(24);
                            cell.setCellValue(vCHATOTAL);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(25);
                            cell.setCellValue(vREIMBURSE);
                            cell.setCellStyle(csDouble2);
                            
                            cell = row.createCell(26);
                            cell.setCellValue(vInvoice_no);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(27);
                            cell.setCellValue(vTxid);
                            cell.setCellStyle(csStringtxid);

                            curRow1++;
                            i++;

                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        this.clearingHouseDAO.closeConnection();
                    }

                    /**
                     * footer summary total
                     */
                    row = sheet1.createRow(curRow1);
                    row.setHeight((short) 450);
                    cell = row.createCell(0);
                    cell.setCellValue("รวม");
                    sheet1.addMergedRegion(new CellRangeAddress(curRow1, curRow1, 0, 9));
                    cell.setCellStyle(csNum4B);

                    row.createCell(1).setCellStyle(csNum4B);
                    row.createCell(2).setCellStyle(csNum4B);
                    row.createCell(3).setCellStyle(csNum4B);
                    row.createCell(4).setCellStyle(csNum4B);
                    row.createCell(5).setCellStyle(csNum4B);
                    row.createCell(6).setCellStyle(csNum4B);
                    row.createCell(7).setCellStyle(csNum4B);
                    row.createCell(8).setCellStyle(csNum4B);
                    row.createCell(9).setCellStyle(csNum4B);

                    cell = row.createCell(10);
                    cell.setCellFormula(builderFormulaSum(10, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(11);
                    cell.setCellFormula(builderFormulaSum(11, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(12);
                    cell.setCellFormula(builderFormulaSum(12, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(13);
                    cell.setCellFormula(builderFormulaSum(13, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(14);
                    cell.setCellFormula(builderFormulaSum(14, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(15);
                    cell.setCellFormula(builderFormulaSum(15, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(16);
                    cell.setCellFormula(builderFormulaSum(16, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(17);
                    cell.setCellFormula(builderFormulaSum(17, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(18);
                    cell.setCellFormula(builderFormulaSum(18, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(19);
                    cell.setCellFormula(builderFormulaSum(19, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(20);
                    cell.setCellFormula(builderFormulaSum(20, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(21);
                    cell.setCellFormula(builderFormulaSum(21, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(22);
                    cell.setCellFormula(builderFormulaSum(22, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(23);
                    cell.setCellFormula(builderFormulaSum(23, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(24);
                    cell.setCellFormula(builderFormulaSum(24, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(25);
                    cell.setCellFormula(builderFormulaSum(25, row_formula_start, curRow1));
                    cell.setCellStyle(csDouble2B);

                    // Stop sheet 1 *******************************************************************************
                    //wb.setSheetName(j + 1, listHospital.get(j).getHosCode().toString());
                    workbookBase.write(out);

                    out.close();
                    file.close();

                    status = true;
                }
                //wb.removeSheetAt(0);
                Console.LOG("clearing_detailPay_byHcode ออกรายงานเรียบร้อบแล้ว", 1);
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
            return false;
        }
        return status;
    }

    public ProgrameStatus clearing_sumPay_ByHcode_1file1sheet(OppReport report) {
        FileOutputStream out = null;
        FileInputStream file = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        String stmp = StringOpUtil.removeNull(report.getYearMonth()) + "-" + report.getNo();
        String pathDirectory = report.getPathFile() + ""+File.separator+"clh"+File.separator+"" + stmp;
        try {
            //global variable
            List<HospitalService> listHospital = clearingHouseDAO.getHospitalService(report);
            for (int j = 0; j < listHospital.size(); j++) {
                HospitalService objService = listHospital.get(j);
                // ############## FileInputStream ###################
                //readTemplate 
                file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"clearing_paysum.xls"));

                //write file Excell
                new FunctionUtil().createMutiDirectory(new File(pathDirectory));
                out = new FileOutputStream(pathDirectory + ""+File.separator+"clh_paid_sum_" + objService.getHosCode() + "_" + stmp + ".xls");

                // ############## FileInputStream ###################
                // Top Excell Sheet1
                String EXCELL_HEADER1 = "สรุปการจ่ายชดเชยค่าบริการผู้ป่วยนอก กรณี Clearing house ให้ศูนย์บริการสาธารณสุข";
                // String EXCELL_SERVICE1 = "งวดที่  Clearing houseจากงบเหมาจ่ายรายหัว " + dateReport;
                String EXCELL_SERVICE1 = "";
                String EXCELL_MONTH1 = StringOpUtil.removeNull(report.getTitle2());
                EXCELL_MONTH1 += "  งวดการจ่ายเงิน " + report.getYearMonth() + "-" + report.getNo();

                HSSFWorkbook wbClearing = new HSSFWorkbook(file);
                this.loadStyle(wbClearing);

                HSSFSheet sheet = workbookBase.getSheetAt(0);
                sheet.createFreezePane(3, 5);

                HSSFCell cell = null;
                HSSFRow row = null;

                // row 0 Header
                row = sheet.createRow(0);
                row.setHeight((short) 390);
                cell = row.createCell(0);
                cell.setCellValue(EXCELL_HEADER1);
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
                cell.setCellStyle(csHead);

                // row 2 Header
                row = sheet.createRow(2);
                row.setHeight((short) 390);
                cell = row.createCell(0);
                cell.setCellValue(EXCELL_MONTH1);
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
                cell.setCellStyle(csHead);

                int curRow = 5;
                int i = 1;

                ResultSet rs = null;

                report.setServiceCode(objService.getHosCode());
                rs = clearingHouseDAO.getReportByHcode(report);

                try {

                    while (rs.next()) {

                        String vhmain = rs.getString("hmain");
                        String vhmainname = rs.getString("hmainname");
                        int vnpid = rs.getInt("npid");
                        int vnvisit = rs.getInt("nvisit");
                        double vcharge = rs.getDouble("charge");
                        double vreimburse = rs.getDouble("reimburse");

                        row = sheet.createRow(curRow);
                        row.setHeight((short) 360);
                        cell = row.createCell(0);
                        cell.setCellValue(i);
                        cell.setCellStyle(csNum4);

                        cell = row.createCell(1);
                        cell.setCellValue(vhmain);
                        cell.setCellStyle(csNumH);

                        cell = row.createCell(2);
                        cell.setCellValue(vhmainname);
                        cell.setCellStyle(csStringLeft);

                        cell = row.createCell(3);
                        cell.setCellValue(vnpid);
                        cell.setCellStyle(csNum4);

                        cell = row.createCell(4);
                        cell.setCellValue(vnvisit);
                        cell.setCellStyle(csNum4);

                        cell = row.createCell(5);
                        cell.setCellValue(vcharge);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(6);
                        cell.setCellValue(vreimburse);
                        cell.setCellStyle(csDouble2);

                        if (i == 1) {
                            EXCELL_SERVICE1 = "";
                        }

                        curRow++;
                        i++;

                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    Console.LOG(e.getMessage(), 0);
                    programeStatus.setMessage(ConstantMessage.MSG_PROCESS_FAILS + e.toString());
                    programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
                    programeStatus.setProcessStatus(false);
                } finally {
                    this.clearingHouseDAO.closeConnection();
                }
                /**
                 * footer summary total
                 */
                row = sheet.createRow(curRow);
                row.setHeight((short) 450);
                cell = row.createCell(0);
                cell.setCellValue("รวม");
                sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 2));
                cell.setCellStyle(csNum4B);

                cell = row.createCell(1);
                cell.setCellStyle(csHeadTab);
                cell = row.createCell(2);
                cell.setCellStyle(csHeadTab);
                cell = row.createCell(3);
                cell.setCellFormula("SUM(D5:D" + (curRow) + ")");
                cell.setCellStyle(csNum4B);
                cell = row.createCell(4);
                cell.setCellFormula("SUM(E5:E" + (curRow) + ")");
                cell.setCellStyle(csNum4B);
                cell = row.createCell(5);
                cell.setCellFormula("SUM(F5:F" + (curRow) + ")");
                cell.setCellStyle(csDouble2B);
                cell = row.createCell(6);
                cell.setCellFormula("SUM(G5:G" + (curRow) + ")");
                cell.setCellStyle(csDouble2B);

//
//            // row 1 Month
                row = sheet.createRow(1);
                cell = row.createCell(0);
                cell.setCellValue(EXCELL_SERVICE1);
                sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 6));
                cell.setCellStyle(csHead);

                workbookBase.write(out);
                out.close();
                Console.LOG("clearing_sumPay หน่วย : " + objService.getHosCode() + " ถูกออกเรียบร้อยแล้ว", 1);

            }
            programeStatus.setMessage(ConstantMessage.MSG_REPORT_SUCCESS);
            programeStatus.setTitle(ConstantMessage.MSG_REPORT_COMPLETE);
            programeStatus.setProcessStatus(true);

        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
            programeStatus.setMessage(ConstantMessage.MSG_PROCESS_FAILS + e.toString());
            programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
            programeStatus.setProcessStatus(false);
        }
        return programeStatus;
    }

    public ProgrameStatus clearing_sumDeduct_ByHcode_1file1sheet(OppReport report) {
        FileOutputStream out = null;
        FileInputStream file = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        String stmp = StringOpUtil.removeNull(report.getYearMonth()) + "-" + report.getNo();
        String pathDirectory = report.getPathFile() + ""+File.separator+"clh"+File.separator+"" + stmp;
        try {
            //global varable
            List<HospitalService> listHospital = clearingHouseDAO.getHospitalService(report);

            for (int j = 0; j < listHospital.size(); j++) {
                HospitalService objService = listHospital.get(j);
                // ############### FileInputStream #############
                //readTemplate 
                file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"clearing_deductsum.xls"));

                //write file Excell
                new FunctionUtil().createMutiDirectory(new File(pathDirectory));
                out = new FileOutputStream(pathDirectory + ""+File.separator+"clh_deduct_sum_" + objService.getHosHmain() + "_" + stmp + ".xls");

                // ############### FileInputStream #############
                // Top Excell Sheet1
                String EXCELL_HEADER1 = "สรุปการหักเงินค่าบริการผู้ป่วยนอก กรณี Clearing house ให้ศูนย์บริการสาธารณสุข";
                // String EXCELL_SERVICE1 = "งวดที่  Clearing houseจากงบเหมาจ่ายรายหัว " + dateReport;
                String EXCELL_SERVICE1 = "";
                String EXCELL_MONTH1 = StringOpUtil.removeNull(report.getTitle2());
                EXCELL_MONTH1 += "  งวดการจ่ายเงิน " + report.getYearMonth() + "-" + report.getNo();

                HSSFWorkbook wbClearing = new HSSFWorkbook(file);
                this.loadStyle(wbClearing);

                HSSFSheet sheet = workbookBase.getSheetAt(0);
                sheet.createFreezePane(3, 5);

                HSSFCell cell = null;
                HSSFRow row = null;

                // row 0 Header
                row = sheet.createRow(0);
                row.setHeight((short) 390);
                cell = row.createCell(0);
                cell.setCellValue(EXCELL_HEADER1);
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
                cell.setCellStyle(csHead);

                // row 2 Month
                row = sheet.createRow(2);
                row.setHeight((short) 390);
                cell = row.createCell(0);
                cell.setCellValue(EXCELL_MONTH1);
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
                cell.setCellStyle(csHead);

                int curRow = 5;
                int i = 1;

                ResultSet rs = null;

                report.setServiceCode(objService.getHosHmain());
                rs = clearingHouseDAO.getReportByHcode(report);

                try {

                    while (rs.next()) {

                        String vhcode = rs.getString("hcode");
                        String vhcodename = rs.getString("hcodename");
                        int vnpid = rs.getInt("npid");
                        int vnvisit = rs.getInt("nvisit");
                        double vcharge = rs.getDouble("charge");
                        double vreimburse = rs.getDouble("reimburse");

                        row = sheet.createRow(curRow);
                        row.setHeight((short) 360);
                        cell = row.createCell(0);
                        cell.setCellValue(i);
                        cell.setCellStyle(csNum4);

                        cell = row.createCell(1);
                        cell.setCellValue(vhcode);
                        cell.setCellStyle(csNumH);

                        cell = row.createCell(2);
                        cell.setCellValue(vhcodename);
                        cell.setCellStyle(csStringLeft);

                        cell = row.createCell(3);
                        cell.setCellValue(vnpid);
                        cell.setCellStyle(csNum4);

                        cell = row.createCell(4);
                        cell.setCellValue(vnvisit);
                        cell.setCellStyle(csNum4);

                        cell = row.createCell(5);
                        cell.setCellValue(vcharge);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(6);
                        cell.setCellValue(vreimburse);
                        cell.setCellStyle(csDouble2);

                        if (i == 1) {
                            EXCELL_SERVICE1 = "";
                        }
                        curRow++;
                        i++;

                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    Console.LOG(e.getMessage(), 0);
                    programeStatus.setMessage(ConstantMessage.MSG_PROCESS_FAILS + e.toString());
                    programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
                    programeStatus.setProcessStatus(false);
                } finally {
                    this.clearingHouseDAO.closeConnection();
                }

                /**
                 * footer summary total
                 */
                row = sheet.createRow(curRow);
                row.setHeight((short) 450);
                cell = row.createCell(0);
                cell.setCellValue("รวม");
                sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 2));
                cell.setCellStyle(csNum4B);

                cell = row.createCell(1);
                cell.setCellStyle(csNum4B);
                cell = row.createCell(2);
                cell.setCellStyle(csNum4B);
                cell = row.createCell(3);
                cell.setCellFormula("SUM(D5:D" + (curRow) + ")");
                cell.setCellStyle(csNum4B);
                cell = row.createCell(4);
                cell.setCellFormula("SUM(E5:E" + (curRow) + ")");
                cell.setCellStyle(csNum4B);
                cell = row.createCell(5);
                cell.setCellFormula("SUM(F5:F" + (curRow) + ")");
                cell.setCellStyle(csDouble2B);
                cell = row.createCell(6);
                cell.setCellFormula("SUM(G5:G" + (curRow) + ")");
                cell.setCellStyle(csDouble2B);

                // row 1 Month
                row = sheet.createRow(1);
                cell = row.createCell(0);
                cell.setCellValue(EXCELL_SERVICE1);
                sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 6));
                cell.setCellStyle(csHead);

                workbookBase.write(out);
                out.close();
                Console.LOG("clearing_sumDeduct หน่วย : " + objService.getHosHmain() + " ออกรายงานเรียบร้อยแล้ว", 1);

            }

            programeStatus.setMessage(ConstantMessage.MSG_REPORT_SUCCESS);
            programeStatus.setTitle(ConstantMessage.MSG_REPORT_COMPLETE);
            programeStatus.setProcessStatus(true);
        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
            programeStatus.setMessage(ConstantMessage.MSG_PROCESS_FAILS + e.toString());
            programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
            programeStatus.setProcessStatus(false);
        }
        return programeStatus;
    }

    /*
     *  ########################END For Web site  claim ########################
     */
    /*
     *  End Single File Single Sheet *********************************************************
     */
    public boolean clearing_genReportAllFunction(OppReport report) {
        boolean status = false;
        ProgrameStatus pStatus = null;
        //String serviceCode = null;
        try {
            if (report.getFor_use() == 0) {
                //*******************************************Detail*********************************************************
                // รายละเอียด 
                report.setReportType(0);
                //serviceCode = report.getServiceCode();
                // หัก
                report.setCategory('D');
                //report.setServiceCode(serviceCode);
                pStatus = this.clearing_detailDedug_byHCode(report);
                //report.setServiceCode(serviceCode);
                this.clearing_detailDedug_byHCode_1file1sheet(report);

                //report.setServiceCode(serviceCode);
                this.clearing_detailDedug_hCodeByHmain(report);
                //report.setServiceCode(serviceCode);
                status = this.clearing_detailDedug_hCodeByHmain_1file1sheet(report);

                // จ่าย
                report.setCategory('P');
                //report.setServiceCode(serviceCode);
                this.clearing_detailPay_byHcode(report);
                //report.setServiceCode(serviceCode);
                this.clearing_detailPay_byHcode_1file1sheet(report);

                //report.setServiceCode(serviceCode);
                this.clearing_detailPay_hcodeByHmain(report);
                //report.setServiceCode(serviceCode);
                this.clearing_detailPay_hcodeByHmain_1file1sheet(report);

                //*******************************************Summary*********************************************************
                //สรุป
                report.setReportType(1);

                // หัก
                report.setCategory('D');
                //report.setServiceCode(serviceCode);
                this.clearing_sumDeduct(report);
                // จ่าย
                report.setCategory('P');
                //report.setServiceCode(serviceCode);
                this.clearing_sumPay(report);
            } else { // 1 = for web site claim
                //******************************************* Detail *********************************************************
                // รายละเอียด 
                report.setReportType(0);
                report.setCategory('D');
                this.clearing_detailDedug_byHCode_1file1sheet(report);
                report.setCategory('P');
                this.clearing_detailPay_byHcode_1file1sheet(report);
                //*******************************************End Detail ****************************************************

                //******************************************* Summary ****************************************************
                // สรุป 
                report.setReportType(1);
                report.setCategory('D');
                this.clearing_sumDeduct_ByHcode_1file1sheet(report);
                report.setCategory('P');
                status = this.clearing_sumPay_ByHcode_1file1sheet(report).getProcessStatus();
                //*******************************************End Summary ************************************************
            }
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
        }
        return status;
    }

    public String getClearingHouseStmpGroupBy(Object obYM, Object obN) {
        String titleTimeStmp = "-";
        if (obYM != "-" && obN != null && obN != "-") {
            titleTimeStmp = clearingHouseDAO.getClearingHouseStmpDescDistinct(obYM.toString(), obN.toString());
        }
        return titleTimeStmp;
    }
}
