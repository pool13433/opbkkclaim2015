package com.claim.controller;

import com.claim.xls.ExcellBaseUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.claim.dao.Opae74DAO;
import com.claim.support.FunctionUtil;
import com.claim.object.HospitalService;
import com.claim.object.ObjRptOpae74;
import com.claim.object.OppReport;
import com.claim.support.Console;
import com.claim.constants.ConstantMessage;
import com.claim.constants.ConstantVariable;
import com.claim.support.DateUtil;
import com.claim.support.Message;
import com.claim.object.ProgrameStatus;
import com.claim.support.StringOpUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

public class Opae74Controller extends ExcellBaseUtil {

    static final int BUDGET_MONTH = 9;
    private Opae74DAO opae74DAO;
    private FunctionUtil function;

    public Opae74Controller() {
        opae74DAO = new Opae74DAO();
        function = new FunctionUtil();
    }

    public ProgrameStatus opae74_detail(OppReport report) throws SQLException {
        FileOutputStream out = null;
        FileInputStream file = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        String EXCELL_HEADER = "";
        int col_last = 24;
        int row_start = 6; // index row
        int row_formula_start = row_start + 1;
        int col_txtid_width = col_last + 1;
        try {

            List<HospitalService> listHospital = opae74DAO.getHospitalService(report);
            System.out.println("listHospital.size()==>>" + listHospital.size());

            for (int h = 0; h < listHospital.size(); h++) { // getHospital เพื่อ สร้าง sheetName

                file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"OPAE74_detail.xls"));

                out = new FileOutputStream(report.getPathFile() + ""+File.separator+"opae74_detail_" + report.getYearMonth() + "-" + report.getNo() + "_" + listHospital.get(h).getHosCode() + ".xls");

                HSSFWorkbook wb74 = new HSSFWorkbook(file);
                this.loadStyle(wb74);
                if (report.getNo().equals("0")) { // กรณี กวาดข้อมูลหมดก็ให้เอาหัวรายงานจากการตั้งค่าเอง
                    EXCELL_HEADER = report.getTitle1();
                } else {
                    EXCELL_HEADER = "รายละเอียดการจ่ายชดเชยค่าบริการ  กรณีอุบัติเหตุ/ฉุกเฉินต่างบัญชีเครือข่าย(OPAE)  กรณีผู้พิการ(Opพิการ)  กรณีฟื้นฟูสมรรถภาพ และอุปกรณ์เครื่องช่วยคนพิการ ปีงบประมาณ " + new DateUtil().getBudgeMonthYear_543(report.getBudget_stmp(),ConstantVariable.BUDGET_MONTH);
                }
                String EXCELL_MONTH = report.getTitle2();
                String EXCELL_SERVICE = "หน่วยบริการ: " + listHospital.get(h).getHosCodeName() + " (" + listHospital.get(h).getHosCode() + ")";

                HSSFSheet sheet = workbookBase.getSheetAt(0);

                sheet.createFreezePane(6, 6);
                sheet.setColumnWidth(col_txtid_width, WIDTH_TXID);

                HSSFCell cell = null;
                HSSFRow row = null;

                // row 0 Header
                row = sheet.createRow(0);
                row.setHeight((short) 390);
                cell = row.createCell(0);
                cell.setCellValue(EXCELL_HEADER);
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col_last));
                cell.setCellStyle(csHead);

                // row 1 Month
                row = sheet.createRow(1);
                row.setHeight((short) 390);
                cell = row.createCell(0);
                cell.setCellValue(EXCELL_MONTH);
                sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, col_last));
                cell.setCellStyle(csHead);

                // row 1 Month
                row = sheet.createRow(2);
                row.setHeight((short) 390);
                cell = row.createCell(0);
                cell.setCellValue(EXCELL_SERVICE);
                sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, col_last));
                cell.setCellStyle(csHead);

                int curRow = 6;
                int i = 1;

                try {
                    report.setServiceCode(listHospital.get(h).getHosCode());

                    ResultSet rs = opae74DAO.genReportOpae74(report);

                    while (rs.next()) {

                        String vPid = rs.getString("pid");
                        String vPname = rs.getString("pname");
                        String vHn = rs.getString("hn");
                        String vHmain = rs.getString("hmain");
                        String vDateopd = rs.getString("dateopd");
                        String vdateopdTh = rs.getString("dateopdTh");
                        String vOptype = rs.getString("optype");
                        double vchrg_ophc = rs.getDouble("chrg_ophc");
                        double vchrg_stditem = rs.getDouble("chrg_stditem");
                        double vchrg_197 = rs.getDouble("chrg_197");
                        double vchrg_car = rs.getDouble("chrg_car");
                        double vchrg_rehab_inst = rs.getDouble("chrg_rehab_inst");
                        double vchrg_other = rs.getDouble("chrg_other");
                        double vchrg_total = rs.getDouble("chrg_total");
                        double vchrg_total_final = rs.getDouble("chrg_total_final");
                        double vpayrate = rs.getDouble("payrate");
                        double vpaid_ophc = rs.getDouble("paid_ophc");
                        double vpaid_stditem = rs.getDouble("paid_stditem");
                        double vpaid_197 = rs.getDouble("paid_197");
                        double vpaid_car = rs.getDouble("paid_car");
                        double vpaid_rehab_inst = rs.getDouble("paid_rehab_inst");
                        double vpaid_other = rs.getDouble("paid_other");
                        double vpaid_total = rs.getDouble("paid_total");
                        double vpaid_total_final = rs.getDouble("paid_total_final");
                        String vTxid = rs.getString("txid");
                        String vInvoice_no = rs.getString("invoice_no");

                        row = sheet.createRow(curRow);
                        row.setHeight((short) 360);
                        cell = row.createCell(0);
                        cell.setCellValue(i);
                        cell.setCellStyle(csNum4);

                        cell = row.createCell(1);
                        cell.setCellValue(function.subStringPid(vPid));
                        cell.setCellStyle(csStringPid);

                        cell = row.createCell(2);
                        cell.setCellValue(vPname);
                        cell.setCellStyle(csStringLeft);

                        cell = row.createCell(3);
                        cell.setCellValue(vHn);
                        cell.setCellStyle(csStringCenter);

                        cell = row.createCell(4);
                        cell.setCellValue(vHmain);
                        cell.setCellStyle(csStringLeft);

                        cell = row.createCell(5);
                        cell.setCellValue(vdateopdTh);
                        cell.setCellStyle(csStringCenter);

                        cell = row.createCell(6);
                        cell.setCellValue(vOptype);
                        cell.setCellStyle(csStringCenter);

                        cell = row.createCell(7);
                        cell.setCellValue(vchrg_ophc);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(8);
                        cell.setCellValue(vchrg_stditem);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(9);
                        cell.setCellValue(vchrg_197);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(10);
                        cell.setCellValue(vchrg_car);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(11);
                        cell.setCellValue(vchrg_rehab_inst);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(12);
                        cell.setCellValue(vchrg_other);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(13);
                        cell.setCellValue(vchrg_total);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(14);
                        cell.setCellValue(vchrg_total_final);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(15);
                        cell.setCellValue(vpayrate);
                        cell.setCellStyle(csDouble2Center);

                        cell = row.createCell(16);
                        cell.setCellValue(vpaid_ophc);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(17);
                        cell.setCellValue(vpaid_stditem);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(18);
                        cell.setCellValue(vpaid_197);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(19);
                        cell.setCellValue(vpaid_car);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(20);
                        cell.setCellValue(vpaid_rehab_inst);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(21);
                        cell.setCellValue(vpaid_other);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(22);
                        cell.setCellValue(vpaid_total);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(23);
                        cell.setCellValue(vpaid_total_final);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(24);
                        cell.setCellValue(vInvoice_no);
                        cell.setCellStyle(csStringLeft);

                        cell = row.createCell(25);
                        cell.setCellValue(vTxid);
                        cell.setCellStyle(csStringtxid);

                        curRow++;
                        i++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Console.LOG(e.getMessage(), 0);
                    programeStatus.setMessage(ConstantMessage.MSG_PROCESS_FAILS + e.toString());
                    programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
                    programeStatus.setProcessStatus(false);
                } finally {
                    this.opae74DAO.closeConnection();
                }

                /*
                 * Sumary
                 */
                row = sheet.createRow(curRow);
                row.setHeight((short) 450);
                cell = row.createCell(0);
                cell.setCellValue("รวม");
                sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 6));
                cell.setCellStyle(csHeadTab);

                row.createCell(1).setCellStyle(csHeadTab);
                row.createCell(2).setCellStyle(csHeadTab);
                row.createCell(3).setCellStyle(csHeadTab);
                row.createCell(4).setCellStyle(csHeadTab);
                row.createCell(5).setCellStyle(csHeadTab);
                row.createCell(6).setCellStyle(csHeadTab);

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
                cell.setCellFormula(builderFormulaSum(13, row_formula_start, curRow));
                cell.setCellStyle(csDouble2B);

                cell = row.createCell(14);
                cell.setCellFormula(builderFormulaSum(14, row_formula_start, curRow));
                cell.setCellStyle(csDouble2B);

                cell = row.createCell(15);
                //cell.setCellFormula("SUM(P7:P" + (curRow) + ")");
                cell.setCellStyle(csNum4B);

                cell = row.createCell(16);
                cell.setCellFormula(builderFormulaSum(16, row_formula_start, curRow));
                cell.setCellStyle(csDouble2B);

                cell = row.createCell(17);
                cell.setCellFormula(builderFormulaSum(17, row_formula_start, curRow));
                cell.setCellStyle(csDouble2B);

                cell = row.createCell(18);
                cell.setCellFormula(builderFormulaSum(18, row_formula_start, curRow));
                cell.setCellStyle(csDouble2B);

                cell = row.createCell(19);
                cell.setCellFormula(builderFormulaSum(19, row_formula_start, curRow));
                cell.setCellStyle(csDouble2B);

                cell = row.createCell(20);
                cell.setCellFormula(builderFormulaSum(20, row_formula_start, curRow));
                cell.setCellStyle(csDouble2B);

                cell = row.createCell(21);
                cell.setCellFormula(builderFormulaSum(21, row_formula_start, curRow));
                cell.setCellStyle(csDouble2B);

                cell = row.createCell(22);
                cell.setCellFormula(builderFormulaSum(22, row_formula_start, curRow));
                cell.setCellStyle(csDouble2B);

                cell = row.createCell(23);
                cell.setCellFormula(builderFormulaSum(23, row_formula_start, curRow));
                cell.setCellStyle(csDouble2B);

                workbookBase.setSheetName(0, listHospital.get(h).getHosCode());
                workbookBase.write(out);
                Console.LOG(Message.exportSuccess(listHospital.get(h).getHosCode()), 1);
                out.close();
                file.close();

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

    // ######################### web site ######################
    public ProgrameStatus opae74_detail_byHcode_1sheet1file(OppReport report) throws SQLException {
        FileOutputStream out = null;
        FileInputStream file = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        String stmp = StringOpUtil.removeNull(report.getYearMonth()) + "-" + report.getNo();
        String pathDirectory = report.getPathFile() + ""+File.separator+"ae74"+File.separator+"" + stmp;
        String EXCELL_HEADER = "";
        int col_last = 24;
        int row_start = 6; // index row
        int row_formula_start = row_start + 1;
        int col_txtid_width = col_last + 1;
        try {

            List<HospitalService> listHospital = opae74DAO.getHospitalService(report);

            for (int h = 0; h < listHospital.size(); h++) { // getHospital เพื่อ สร้าง sheetName
                HospitalService objService = listHospital.get(h);

                file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"OPAE74_detail.xls"));

                if (report.getFor_use() == 0) { // for genaral use
                    out = new FileOutputStream(report.getPathFile() + ""+File.separator+"opae74_detail_" + report.getYearMonth() + "-" + report.getNo() + "_" + listHospital.get(h).getHosCode() + ".xls");
                } else { // for web site                    
                    new FunctionUtil().createMutiDirectory(new File(pathDirectory));
                    out = new FileOutputStream(pathDirectory + ""+File.separator+"ae74_" + objService.getHosCode() + "_" + stmp + ".xls");
                }
                HSSFWorkbook wb74 = new HSSFWorkbook(file);
                this.loadStyle(wb74);
                if (report.getNo().equals("0")) { // กรณี กวาดข้อมูลหมดก็ให้เอาหัวรายงานจากการตั้งค่าเอง
                    EXCELL_HEADER = report.getTitle1();
                } else {
                    EXCELL_HEADER = "รายละเอียดการจ่ายชดเชยค่าบริการ  กรณีอุบัติเหตุ/ฉุกเฉินต่างบัญชีเครือข่าย(OPAE)  กรณีผู้พิการ(Opพิการ)  กรณีฟื้นฟูสมรรถภาพ และอุปกรณ์เครื่องช่วยคนพิการ ปีงบประมาณ " + new DateUtil().getBudgeMonthYear_543(report.getBudget_stmp(),ConstantVariable.BUDGET_MONTH);
                }
                String EXCELL_MONTH = report.getTitle2();
                String EXCELL_SERVICE = "หน่วยบริการ: " + listHospital.get(h).getHosCodeName() + " (" + listHospital.get(h).getHosCode() + ")";

                HSSFSheet sheet = workbookBase.getSheetAt(0);

                sheet.createFreezePane(6, 6);
                sheet.setColumnWidth(col_txtid_width, WIDTH_TXID);

                HSSFCell cell = null;
                HSSFRow row = null;

                // row 0 Header
                row = sheet.createRow(0);
                row.setHeight((short) 390);
                cell = row.createCell(0);
                cell.setCellValue(EXCELL_HEADER);
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col_last));
                cell.setCellStyle(csHead);

                // row 1 Month
                row = sheet.createRow(1);
                row.setHeight((short) 390);
                cell = row.createCell(0);
                cell.setCellValue(EXCELL_MONTH);
                sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, col_last));
                cell.setCellStyle(csHead);

                // row 1 Month
                row = sheet.createRow(2);
                row.setHeight((short) 390);
                cell = row.createCell(0);
                cell.setCellValue(EXCELL_SERVICE);
                sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, col_last));
                cell.setCellStyle(csHead);

                int curRow = row_start;
                int i = 1;

                try {
                    report.setServiceCode(listHospital.get(h).getHosCode());

                    ResultSet rs = opae74DAO.genReportOpae74(report);

                    while (rs.next()) {

                        String vPid = rs.getString("pid");
                        String vPname = rs.getString("pname");
                        String vHn = rs.getString("hn");
                        String vHmain = rs.getString("hmain");
                        String vDateopd = rs.getString("dateopd");
                        String vdateopdTh = rs.getString("dateopdTh");
                        String vOptype = rs.getString("optype");
                        double vchrg_ophc = rs.getDouble("chrg_ophc");
                        double vchrg_stditem = rs.getDouble("chrg_stditem");
                        double vchrg_197 = rs.getDouble("chrg_197");
                        double vchrg_car = rs.getDouble("chrg_car");
                        double vchrg_rehab_inst = rs.getDouble("chrg_rehab_inst");
                        double vchrg_other = rs.getDouble("chrg_other");
                        double vchrg_total = rs.getDouble("chrg_total");
                        double vchrg_total_final = rs.getDouble("chrg_total_final");
                        double vpayrate = rs.getDouble("payrate");
                        double vpaid_ophc = rs.getDouble("paid_ophc");
                        double vpaid_stditem = rs.getDouble("paid_stditem");
                        double vpaid_197 = rs.getDouble("paid_197");
                        double vpaid_car = rs.getDouble("paid_car");
                        double vpaid_rehab_inst = rs.getDouble("paid_rehab_inst");
                        double vpaid_other = rs.getDouble("paid_other");
                        double vpaid_total = rs.getDouble("paid_total");
                        double vpaid_total_final = rs.getDouble("paid_total_final");
                        String vTxid = rs.getString("txid");
                        String vInvoice_no = rs.getString("invoice_no");

                        row = sheet.createRow(curRow);
                        row.setHeight((short) 360);
                        cell = row.createCell(0);
                        cell.setCellValue(i);
                        cell.setCellStyle(csNum4);

                        cell = row.createCell(1);
                        cell.setCellValue(function.subStringPid(vPid));
                        cell.setCellStyle(csStringLeft);

                        cell = row.createCell(2);
                        cell.setCellValue(vPname);
                        cell.setCellStyle(csStringLeft);

                        cell = row.createCell(3);
                        cell.setCellValue(vHn);
                        cell.setCellStyle(csStringCenter);

                        cell = row.createCell(4);
                        cell.setCellValue(vHmain);
                        cell.setCellStyle(csStringLeft);

                        cell = row.createCell(5);
                        cell.setCellValue(vdateopdTh);
                        cell.setCellStyle(csStringCenter);

                        cell = row.createCell(6);
                        cell.setCellValue(vOptype);
                        cell.setCellStyle(csStringCenter);

                        cell = row.createCell(7);
                        cell.setCellValue(vchrg_ophc);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(8);
                        cell.setCellValue(vchrg_stditem);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(9);
                        cell.setCellValue(vchrg_197);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(10);
                        cell.setCellValue(vchrg_car);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(11);
                        cell.setCellValue(vchrg_rehab_inst);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(12);
                        cell.setCellValue(vchrg_other);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(13);
                        cell.setCellValue(vchrg_total);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(14);
                        cell.setCellValue(vchrg_total_final);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(15);
                        cell.setCellValue(vpayrate);
                        cell.setCellStyle(csDouble2Center);

                        cell = row.createCell(16);
                        cell.setCellValue(vpaid_ophc);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(17);
                        cell.setCellValue(vpaid_stditem);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(18);
                        cell.setCellValue(vpaid_197);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(19);
                        cell.setCellValue(vpaid_car);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(20);
                        cell.setCellValue(vpaid_rehab_inst);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(21);
                        cell.setCellValue(vpaid_other);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(22);
                        cell.setCellValue(vpaid_total);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(23);
                        cell.setCellValue(vpaid_total_final);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(24);
                        cell.setCellValue(vInvoice_no);
                        cell.setCellStyle(csStringLeft);

                        cell = row.createCell(25);
                        cell.setCellValue(vTxid);
                        cell.setCellStyle(csStringtxid);

                        curRow++;
                        i++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Console.LOG(e.getMessage(), 0);
                    programeStatus.setMessage(ConstantMessage.MSG_PROCESS_FAILS + e.toString());
                    programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
                    programeStatus.setProcessStatus(false);
                } finally {
                    this.opae74DAO.closeConnection();
                }

                /*
                 * Sumary
                 */
                row = sheet.createRow(curRow);
                row.setHeight((short) 450);
                cell = row.createCell(0);
                cell.setCellValue("รวม");
                sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 6));
                cell.setCellStyle(csHeadTab);

                row.createCell(1).setCellStyle(csHeadTab);
                row.createCell(2).setCellStyle(csHeadTab);
                row.createCell(3).setCellStyle(csHeadTab);
                row.createCell(4).setCellStyle(csHeadTab);
                row.createCell(5).setCellStyle(csHeadTab);
                row.createCell(6).setCellStyle(csHeadTab);

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
                cell.setCellFormula(builderFormulaSum(13, row_formula_start, curRow));
                cell.setCellStyle(csDouble2B);

                cell = row.createCell(14);
                cell.setCellFormula(builderFormulaSum(14, row_formula_start, curRow));
                cell.setCellStyle(csDouble2B);

                cell = row.createCell(15);
                //cell.setCellFormula(builderFormula(15, row_formula_start, curRow));
                cell.setCellStyle(csNum4B);

                cell = row.createCell(16);
                cell.setCellFormula(builderFormulaSum(16, row_formula_start, curRow));
                cell.setCellStyle(csDouble2B);

                cell = row.createCell(17);
                cell.setCellFormula(builderFormulaSum(17, row_formula_start, curRow));
                cell.setCellStyle(csDouble2B);

                cell = row.createCell(18);
                cell.setCellFormula(builderFormulaSum(18, row_formula_start, curRow));
                cell.setCellStyle(csDouble2B);

                cell = row.createCell(19);
                cell.setCellFormula(builderFormulaSum(19, row_formula_start, curRow));
                cell.setCellStyle(csDouble2B);

                cell = row.createCell(20);
                cell.setCellFormula(builderFormulaSum(20, row_formula_start, curRow));
                cell.setCellStyle(csDouble2B);

                cell = row.createCell(21);
                cell.setCellFormula(builderFormulaSum(21, row_formula_start, curRow));
                cell.setCellStyle(csDouble2B);

                cell = row.createCell(22);
                cell.setCellFormula(builderFormulaSum(22, row_formula_start, curRow));
                cell.setCellStyle(csDouble2B);

                cell = row.createCell(23);
                cell.setCellFormula(builderFormulaSum(23, row_formula_start, curRow));
                cell.setCellStyle(csDouble2B);

                workbookBase.setSheetName(0, listHospital.get(h).getHosCode());
                workbookBase.write(out);
                Console.LOG(Message.exportSuccess(listHospital.get(h).getHosCode()), 1);
                out.close();
                file.close();

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
    // #########################end web site ###################

    public ProgrameStatus opae74_detail_all_hcode_spliteFile_65500(OppReport report) throws SQLException {
        FileOutputStream out = null;
        FileInputStream file = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        //Obj_row_summary obj_row = null;
        String EXCELL_HEADER = "";
        int col_last = 25;
        int row_start = 6; // index row
        int row_formula_start = row_start + 1;
        int col_txtid_width = col_last + 1;
        try {
            int row_content = 1;
            int file_no = 0;
            int row_data = 1;
            HSSFWorkbook wb74 = null;
            HSSFSheet sheet = null;
            int curRow = 0;

            //obj_row = new Obj_row_summary();
            // ################ get TotalSum ##################
            ObjRptOpae74 objTotal = opae74DAO.getReportOpae74Detail_Totalsum(report);
            // ################ get TotalSum ##################
            // set criteria 
            report.setSql_orderBy(" ORDER BY HCODE,HMAIN,PID ASC");

            List<ObjRptOpae74> listopae74 = opae74DAO.genReportOpae74_ListObject(report);

            for (int i = 0; i < listopae74.size(); i++) {
                ObjRptOpae74 objOpae74 = listopae74.get(i);

                // #################### variable ###################
                HSSFCell cell = null;
                HSSFRow row = null;
                // #################### end  variable ##############

                if (row_content >= 1 && row_content <= ROW_LIMIT) {  // limit excell row 65,536 rows by 256 columns

                    // #################### header report ############
                    if (row_content == 1) {
                        System.out.println("Header report ");
                        file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"OPAE74_detail_hcode.xls"));

                        out = new FileOutputStream(report.getPathFile() + ""+File.separator+"opae74_detail_" + report.getYearMonth() + "-" + report.getNo() + "_file_" + (file_no + 1) + "" + ".xls");
                        if (report.getNo().equals("0")) { // กรณี กวาดข้อมูลหมดก็ให้เอาหัวรายงานจากการตั้งค่าเอง
                            EXCELL_HEADER = report.getTitle1();
                        } else {
                            EXCELL_HEADER = "รายละเอียดการจ่ายชดเชยค่าบริการ  กรณีอุบัติเหตุ/ฉุกเฉินต่างบัญชีเครือข่าย(OPAE)  กรณีผู้พิการ(Opพิการ)  กรณีฟื้นฟูสมรรถภาพ และอุปกรณ์เครื่องช่วยคนพิการ ปีงบประมาณ " + new DateUtil().getBudgeMonthYear_543(report.getBudget_stmp(),ConstantVariable.BUDGET_MONTH);
                        }
                        String EXCELL_MONTH = report.getTitle2();

                        // ################### valiable #################
                        wb74 = new HSSFWorkbook(file);
                        this.loadStyle(wb74);

                        sheet = wb74.getSheetAt(0);
                        sheet.createFreezePane(6, 6);
                        sheet.setColumnWidth(col_txtid_width, WIDTH_TXID);

                        // ################### valiable #################
                        // row 0 Header
                        row = sheet.createRow(0);
                        row.setHeight((short) 390);
                        cell = row.createCell(0);
                        cell.setCellValue(EXCELL_HEADER);
                        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col_last));
                        cell.setCellStyle(csHead);

                        // row 1 Month
                        row = sheet.createRow(1);
                        row.setHeight((short) 390);
                        cell = row.createCell(0);
                        cell.setCellValue(EXCELL_MONTH);
                        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, col_last));
                        cell.setCellStyle(csHead);

                        //row 4 ตัวเนื้อหา
                        curRow = 6;
                    }
                    // ####################end header report #########

                    // #################### content report ############
                    System.out.println("content report row_content : " + row_content);
                    System.out.println("content report row_data : " + row_data);
                    row = sheet.createRow(curRow);
                    row.setHeight((short) 360);
                    cell = row.createCell(0);
                    cell.setCellValue(row_data);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(1);
                    cell.setCellValue(function.subStringPid(objOpae74.getPid()));
                    cell.setCellStyle(csStringLeft);

                    cell = row.createCell(2);
                    cell.setCellValue(objOpae74.getP_name());
                    cell.setCellStyle(csStringLeft);

                    cell = row.createCell(3);
                    cell.setCellValue(objOpae74.getHn());
                    cell.setCellStyle(csStringCenter);

                    // ############# hcode ###########
                    cell = row.createCell(4);
                    cell.setCellValue(objOpae74.getHcode() + " : " + objOpae74.getHcodename());
                    cell.setCellStyle(csStringLeft);
                    // ############# hcode ###########

                    cell = row.createCell(5);
                    cell.setCellValue(objOpae74.getHmain());
                    cell.setCellStyle(csStringLeft);

                    cell = row.createCell(6);
                    cell.setCellValue(objOpae74.getDateopd_th());
                    cell.setCellStyle(csStringCenter);

                    cell = row.createCell(7);
                    cell.setCellValue(objOpae74.getOptype());
                    cell.setCellStyle(csStringCenter);

                    cell = row.createCell(8);
                    cell.setCellValue(objOpae74.getChrg_ophc());
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(9);
                    cell.setCellValue(objOpae74.getChrg_stditem());
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(10);
                    cell.setCellValue(objOpae74.getChrg_197());
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(11);
                    cell.setCellValue(objOpae74.getChrg_car());
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(12);
                    cell.setCellValue(objOpae74.getChrg_rehab_inst());
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(13);
                    cell.setCellValue(objOpae74.getChrg_other());
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(14);
                    cell.setCellValue(objOpae74.getChrg_total());
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(15);
                    cell.setCellValue(objOpae74.getChrg_total_final());
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(16);
                    cell.setCellValue(objOpae74.getPayrate());
                    cell.setCellStyle(csDouble2Center);

                    cell = row.createCell(17);
                    cell.setCellValue(objOpae74.getPaid_ophc());
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(18);
                    cell.setCellValue(objOpae74.getPaid_stditem());
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(19);
                    cell.setCellValue(objOpae74.getPaid_197());
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(20);
                    cell.setCellValue(objOpae74.getPaid_car());
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(21);
                    cell.setCellValue(objOpae74.getPaid_rehab_inst());
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(22);
                    cell.setCellValue(objOpae74.getPaid_other());
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(23);
                    cell.setCellValue(objOpae74.getPaid_total());
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(24);
                    cell.setCellValue(objOpae74.getPaid_total_final());
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(25);
                    cell.setCellValue(objOpae74.getInvoice_no());
                    cell.setCellStyle(csStringLeft);

                    cell = row.createCell(26);
                    cell.setCellValue(objOpae74.getTxid());
                    cell.setCellStyle(csStringtxid);

                    // ###################### sum total ##########
                    /*obj_row.setCell_I(obj_row.getCell_I() + objOpae74.getChrg_197());
                     obj_row.setCell_J(obj_row.getCell_J() + objOpae74.getChrg_ophc());
                     obj_row.setCell_K(obj_row.getCell_K() + objOpae74.getChrg_stditem());
                     obj_row.setCell_L(obj_row.getCell_L() + objOpae74.getChrg_197());
                     obj_row.setCell_M(obj_row.getCell_M() + objOpae74.getChrg_car());
                     obj_row.setCell_N(obj_row.getCell_N() + objOpae74.getChrg_rehab_inst());
                     obj_row.setCell_O(obj_row.getCell_O() + objOpae74.getChrg_other());
                     obj_row.setCell_P(obj_row.getCell_P() + objOpae74.getChrg_total());
                     obj_row.setCell_Q(obj_row.getCell_Q() + objOpae74.getChrg_total_final());
                     //cell_H = cell_Q + objOpae74.getPayrate();
                     obj_row.setCell_R(obj_row.getCell_R() + objOpae74.getPaid_ophc());
                     obj_row.setCell_S(obj_row.getCell_S() + objOpae74.getPaid_stditem());
                     obj_row.setCell_T(obj_row.getCell_T() + objOpae74.getPaid_197());
                     obj_row.setCell_U(obj_row.getCell_U() + objOpae74.getPaid_car());
                     obj_row.setCell_V(obj_row.getCell_V() + objOpae74.getPaid_rehab_inst());
                     obj_row.setCell_W(obj_row.getCell_W() + objOpae74.getPaid_other());
                     obj_row.setCell_X(obj_row.getCell_X() + objOpae74.getPaid_total());
                     obj_row.setCell_Y(obj_row.getCell_Y() + objOpae74.getPaid_total_final());*/
                    // ###################### sum total ##########
                    // #################### end content report ########
                    if (row_content == ROW_LIMIT) {
                        if (row_data == listopae74.size()) {
                            System.out.println("row_data == listopae74.size()  : ");
                            // #################### footer report ############
                            if (listopae74.size() / ROW_LIMIT == file_no) {
                                System.out.println("listopae74.size() / ROW_LIMIT == file_no :");
                                curRow++;
                                /*
                                 * Sumary
                                 */
                                System.out.println("curRow : " + curRow);

                                row = sheet.createRow(curRow);
                                row.setHeight((short) 450);
                                cell = row.createCell(0);
                                cell.setCellValue("รวม");
                                sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 6));
                                cell.setCellStyle(csHeadTab);

                                row.createCell(1).setCellStyle(csHeadTab);
                                row.createCell(2).setCellStyle(csHeadTab);
                                row.createCell(3).setCellStyle(csHeadTab);
                                row.createCell(4).setCellStyle(csHeadTab);
                                row.createCell(5).setCellStyle(csHeadTab);
                                row.createCell(6).setCellStyle(csHeadTab);
                                row.createCell(7).setCellStyle(csHeadTab);

                                /*
                                 cell = row.createCell(8);
                                 cell.setCellValue(obj_row.getCell_I());
                                 cell.setCellStyle(csNum2B);

                                 cell = row.createCell(9);
                                 cell.setCellValue(obj_row.getCell_J());
                                 cell.setCellStyle(csNum2B);

                                 cell = row.createCell(10);
                                 cell.setCellValue(obj_row.getCell_K());
                                 cell.setCellStyle(csNum2B);

                                 cell = row.createCell(11);
                                 cell.setCellValue(obj_row.getCell_L());
                                 cell.setCellStyle(csNum2B);

                                 cell = row.createCell(12);
                                 cell.setCellValue(obj_row.getCell_M());
                                 cell.setCellStyle(csNum2B);

                                 cell = row.createCell(13);
                                 cell.setCellValue(obj_row.getCell_N());
                                 cell.setCellStyle(csNum2B);

                                 cell = row.createCell(14);
                                 cell.setCellValue(obj_row.getCell_O());
                                 cell.setCellStyle(csNum2B);

                                 cell = row.createCell(15);
                                 cell.setCellValue(obj_row.getCell_P());
                                 cell.setCellStyle(csNum2B);

                                 cell = row.createCell(16);
                                 //cell.setCellFormula("SUM(P7:P" + (curRow) + ")");
                                 cell.setCellStyle(csNum4B);

                                 cell = row.createCell(17);
                                 cell.setCellValue(obj_row.getCell_R());
                                 cell.setCellStyle(csNum2B);

                                 cell = row.createCell(18);
                                 cell.setCellValue(obj_row.getCell_S());
                                 cell.setCellStyle(csNum2B);

                                 cell = row.createCell(19);
                                 cell.setCellValue(obj_row.getCell_T());
                                 cell.setCellStyle(csNum2B);

                                 cell = row.createCell(20);
                                 cell.setCellValue(obj_row.getCell_U());
                                 cell.setCellStyle(csNum2B);

                                 cell = row.createCell(21);
                                 cell.setCellValue(obj_row.getCell_V());
                                 cell.setCellStyle(csNum2B);

                                 cell = row.createCell(22);
                                 cell.setCellValue(obj_row.getCell_W());
                                 cell.setCellStyle(csNum2B);

                                 cell = row.createCell(23);
                                 cell.setCellValue(obj_row.getCell_X());
                                 cell.setCellStyle(csNum2B);

                                 cell = row.createCell(24);
                                 cell.setCellValue(obj_row.getCell_Y());
                                 cell.setCellStyle(csNum2B);*/
                                cell = row.createCell(8);
                                cell.setCellValue(objTotal.getChrg_ophc());
                                cell.setCellStyle(csDouble2B);

                                cell = row.createCell(9);
                                cell.setCellValue(objTotal.getChrg_stditem());
                                cell.setCellStyle(csDouble2B);

                                cell = row.createCell(10);
                                cell.setCellValue(objTotal.getChrg_197());
                                cell.setCellStyle(csDouble2B);

                                cell = row.createCell(11);
                                cell.setCellValue(objTotal.getChrg_car());
                                cell.setCellStyle(csDouble2B);

                                cell = row.createCell(12);
                                cell.setCellValue(objTotal.getChrg_rehab_inst());
                                cell.setCellStyle(csDouble2B);

                                cell = row.createCell(13);
                                cell.setCellValue(objTotal.getChrg_other());
                                cell.setCellStyle(csDouble2B);

                                cell = row.createCell(14);
                                cell.setCellValue(objTotal.getChrg_total());
                                cell.setCellStyle(csDouble2B);

                                cell = row.createCell(15);
                                cell.setCellValue(objTotal.getChrg_total_final());
                                cell.setCellStyle(csDouble2B);

                                cell = row.createCell(16);
                                //cell.setCellFormula("SUM(P7:P" + (curRow) + ")");
                                cell.setCellStyle(csNum4B);

                                cell = row.createCell(17);
                                cell.setCellValue(objTotal.getPaid_ophc());
                                cell.setCellStyle(csDouble2B);

                                cell = row.createCell(18);
                                cell.setCellValue(objTotal.getPaid_stditem());
                                cell.setCellStyle(csDouble2B);

                                cell = row.createCell(19);
                                cell.setCellValue(objTotal.getPaid_197());
                                cell.setCellStyle(csDouble2B);

                                cell = row.createCell(20);
                                cell.setCellValue(objTotal.getPaid_car());
                                cell.setCellStyle(csDouble2B);

                                cell = row.createCell(21);
                                cell.setCellValue(objTotal.getPaid_rehab_inst());
                                cell.setCellStyle(csDouble2B);

                                cell = row.createCell(22);
                                cell.setCellValue(objTotal.getPaid_other());
                                cell.setCellStyle(csDouble2B);

                                cell = row.createCell(23);
                                cell.setCellValue(objTotal.getPaid_total());
                                cell.setCellStyle(csDouble2B);

                                cell = row.createCell(24);
                                cell.setCellValue(objTotal.getPaid_total_final());
                                cell.setCellStyle(csDouble2B);

                            }
                            // #################### end footer report #########

                            // write file 
                            workbookBase.write(out);

                            out.close();
                            file.close();

                            // เริ่ม รัน ค่าไหม่
                            row_content = 1;
                            // เริ่ม row ของเอกสารไหม่ ที่ 5
                            curRow = 6;
                            // บวก จำนวนไฟล์ เพิ่ม 1
                            file_no = file_no + 1;

                            Console.LOG("อออกรายงานไฟล์ ที่ :" + file_no + " สำเร็จเรียบร้อยแล้ว", 1);
                        } else {
                            // write file 
                            workbookBase.write(out);

                            out.close();
                            file.close();

                            // เริ่ม รัน ค่าไหม่
                            row_content = 1;
                            // เริ่ม row ของเอกสารไหม่ ที่ 5
                            curRow = 6;
                            // บวก จำนวนไฟล์ เพิ่ม 1
                            file_no = file_no + 1;

                            System.out.println(" new file ");
                            Console.LOG("อออกรายงานไฟล์ ที่ :" + file_no + " สำเร็จเรียบร้อยแล้ว", 1);
                            row_data++;
                        }
                    } else {

                        if (row_data == listopae74.size()) {
                            System.out.println("row_data == listopae74.size() :");

                            // #################### footer report ############
                            if (listopae74.size() / ROW_LIMIT == file_no) {
                                System.out.println("listopae74.size() / ROW_LIMIT == file_no :");
                                curRow++;
                                /*
                                 * Sumary
                                 */
                                System.out.println("curRow : " + curRow);

                                row = sheet.createRow(curRow);
                                row.setHeight((short) 450);
                                cell = row.createCell(0);
                                cell.setCellValue("รวม");
                                sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 6));
                                cell.setCellStyle(csHeadTab);

                                row.createCell(1).setCellStyle(csHeadTab);
                                row.createCell(2).setCellStyle(csHeadTab);
                                row.createCell(3).setCellStyle(csHeadTab);
                                row.createCell(4).setCellStyle(csHeadTab);
                                row.createCell(5).setCellStyle(csHeadTab);
                                row.createCell(6).setCellStyle(csHeadTab);
                                row.createCell(7).setCellStyle(csHeadTab);

                                /*cell = row.createCell(8);
                                 cell.setCellValue(obj_row.getCell_I());
                                 cell.setCellStyle(csNum2B);

                                 cell = row.createCell(9);
                                 cell.setCellValue(obj_row.getCell_J());
                                 cell.setCellStyle(csNum2B);

                                 cell = row.createCell(10);
                                 cell.setCellValue(obj_row.getCell_K());
                                 cell.setCellStyle(csNum2B);

                                 cell = row.createCell(11);
                                 cell.setCellValue(obj_row.getCell_L());
                                 cell.setCellStyle(csNum2B);

                                 cell = row.createCell(12);
                                 cell.setCellValue(obj_row.getCell_M());
                                 cell.setCellStyle(csNum2B);

                                 cell = row.createCell(13);
                                 cell.setCellValue(obj_row.getCell_N());
                                 cell.setCellStyle(csNum2B);

                                 cell = row.createCell(14);
                                 cell.setCellValue(obj_row.getCell_O());
                                 cell.setCellStyle(csNum2B);

                                 cell = row.createCell(15);
                                 cell.setCellValue(obj_row.getCell_P());
                                 cell.setCellStyle(csNum2B);

                                 cell = row.createCell(16);
                                 //cell.setCellFormula("SUM(P7:P" + (curRow) + ")");
                                 cell.setCellStyle(csNum4B);

                                 cell = row.createCell(17);
                                 cell.setCellValue(obj_row.getCell_R());
                                 cell.setCellStyle(csNum2B);

                                 cell = row.createCell(18);
                                 cell.setCellValue(obj_row.getCell_S());
                                 cell.setCellStyle(csNum2B);

                                 cell = row.createCell(19);
                                 cell.setCellValue(obj_row.getCell_T());
                                 cell.setCellStyle(csNum2B);

                                 cell = row.createCell(20);
                                 cell.setCellValue(obj_row.getCell_U());
                                 cell.setCellStyle(csNum2B);

                                 cell = row.createCell(21);
                                 cell.setCellValue(obj_row.getCell_V());
                                 cell.setCellStyle(csNum2B);

                                 cell = row.createCell(22);
                                 cell.setCellValue(obj_row.getCell_W());
                                 cell.setCellStyle(csNum2B);

                                 cell = row.createCell(23);
                                 cell.setCellValue(obj_row.getCell_X());
                                 cell.setCellStyle(csNum2B);

                                 cell = row.createCell(24);
                                 cell.setCellValue(obj_row.getCell_Y());
                                 cell.setCellStyle(csNum2B);*/
                                cell = row.createCell(8);
                                cell.setCellValue(objTotal.getChrg_ophc());
                                cell.setCellStyle(csDouble2B);

                                cell = row.createCell(9);
                                cell.setCellValue(objTotal.getChrg_stditem());
                                cell.setCellStyle(csDouble2B);

                                cell = row.createCell(10);
                                cell.setCellValue(objTotal.getChrg_197());
                                cell.setCellStyle(csDouble2B);

                                cell = row.createCell(11);
                                cell.setCellValue(objTotal.getChrg_car());
                                cell.setCellStyle(csDouble2B);

                                cell = row.createCell(12);
                                cell.setCellValue(objTotal.getChrg_rehab_inst());
                                cell.setCellStyle(csDouble2B);

                                cell = row.createCell(13);
                                cell.setCellValue(objTotal.getChrg_other());
                                cell.setCellStyle(csDouble2B);

                                cell = row.createCell(14);
                                cell.setCellValue(objTotal.getChrg_total());
                                cell.setCellStyle(csDouble2B);

                                cell = row.createCell(15);
                                cell.setCellValue(objTotal.getChrg_total_final());
                                cell.setCellStyle(csDouble2B);

                                cell = row.createCell(16);
                                //cell.setCellFormula("SUM(P7:P" + (curRow) + ")");
                                cell.setCellStyle(csNum4B);

                                cell = row.createCell(17);
                                cell.setCellValue(objTotal.getPaid_ophc());
                                cell.setCellStyle(csDouble2B);

                                cell = row.createCell(18);
                                cell.setCellValue(objTotal.getPaid_stditem());
                                cell.setCellStyle(csDouble2B);

                                cell = row.createCell(19);
                                cell.setCellValue(objTotal.getPaid_197());
                                cell.setCellStyle(csDouble2B);

                                cell = row.createCell(20);
                                cell.setCellValue(objTotal.getPaid_car());
                                cell.setCellStyle(csDouble2B);

                                cell = row.createCell(21);
                                cell.setCellValue(objTotal.getPaid_rehab_inst());
                                cell.setCellStyle(csDouble2B);

                                cell = row.createCell(22);
                                cell.setCellValue(objTotal.getPaid_other());
                                cell.setCellStyle(csDouble2B);

                                cell = row.createCell(23);
                                cell.setCellValue(objTotal.getPaid_total());
                                cell.setCellStyle(csDouble2B);

                                cell = row.createCell(24);
                                cell.setCellValue(objTotal.getPaid_total_final());
                                cell.setCellStyle(csDouble2B);

                            }
                            // #################### end footer report #########

                            // write file 
                            workbookBase.write(out);

                            out.close();
                            file.close();

                            // เริ่ม รัน ค่าไหม่
                            row_content = 1;
                            // เริ่ม row ของเอกสารไหม่ ที่ 5
                            curRow = 5;
                            // บวก จำนวนไฟล์ เพิ่ม 1
                            file_no = file_no + 1;

                            System.out.println(" new file ");
                            Console.LOG("อออกรายงานไฟล์ ที่ :" + file_no + " สำเร็จเรียบร้อยแล้ว", 1);
                        }

                        curRow++;
                        row_content++;
                        row_data++;
                    }

                }

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

    public ProgrameStatus opae74_detail_byDateopd(OppReport report) {
        FileOutputStream out = null;
        FileInputStream file = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        String EXCELL_HEADER = "";
        int col_last = 24;
        int row_start = 6; // index row
        int row_formula_start = row_start + 1;
        int col_txtid_width = col_last + 1;
        try {

            List<HospitalService> listHospital = opae74DAO.getHospitalService(report);
            System.out.println("listHospital.size()==>>" + listHospital.size());

            if (!listHospital.isEmpty()) {
                for (int h = 0; h < listHospital.size(); h++) { // getHospital เพื่อ สร้าง sheetName
                    HospitalService objService = listHospital.get(h);

                    report.setServiceCode(objService.getHosCode());
                    List<HospitalService> listDateopd = opae74DAO.getDateopdGroupService(report);

                    for (int g = 0; g < listDateopd.size(); g++) {
                        HospitalService objDateopd = listDateopd.get(g);

                        file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"OPAE74_detail.xls"));

                        out = new FileOutputStream(report.getPathFile() + ""+File.separator+"opae74_detail_" + report.getYearMonth() + "-" + report.getNo() + "_" + objService.getHosCode() + "_" + objDateopd.getDateopd() + ".xls");

                        HSSFWorkbook wb74 = new HSSFWorkbook(file);
                        this.loadStyle(wb74);

                        if (report.getNo().equals("0")) { // กรณี กวาดข้อมูลหมดก็ให้เอาหัวรายงานจากการตั้งค่าเอง
                            EXCELL_HEADER = report.getTitle1();
                        } else {
                            EXCELL_HEADER = "รายละเอียดการจ่ายชดเชยค่าบริการ  กรณีอุบัติเหตุ/ฉุกเฉินต่างบัญชีเครือข่าย(OPAE)  กรณีผู้พิการ(Opพิการ)  กรณีฟื้นฟูสมรรถภาพ และอุปกรณ์เครื่องช่วยคนพิการ ปีงบประมาณ " + new DateUtil().getBudgeMonthYear_543(report.getBudget_stmp(),ConstantVariable.BUDGET_MONTH);
                        }
                        String EXCELL_MONTH = report.getTitle2();
                        String EXCELL_SERVICE = "หน่วยบริการ: " + objService.getHosCodeName() + " (" + objService.getHosCode() + ") เดือน: " + objDateopd.getDateopd_th();

                        HSSFSheet sheet = workbookBase.getSheetAt(0);

                        sheet.createFreezePane(6, 6);
                        sheet.setColumnWidth(col_txtid_width, WIDTH_TXID);

                        HSSFCell cell = null;
                        HSSFRow row = null;

                        // row 0 Header
                        row = sheet.createRow(0);
                        row.setHeight((short) 390);
                        cell = row.createCell(0);
                        cell.setCellValue(EXCELL_HEADER);
                        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col_last));
                        cell.setCellStyle(csHead);

                        // row 1 Month
                        row = sheet.createRow(1);
                        row.setHeight((short) 390);
                        cell = row.createCell(0);
                        cell.setCellValue(EXCELL_MONTH);
                        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, col_last));
                        cell.setCellStyle(csHead);

                        // row 1 Month
                        row = sheet.createRow(2);
                        row.setHeight((short) 390);
                        cell = row.createCell(0);
                        cell.setCellValue(EXCELL_SERVICE);
                        sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, col_last));
                        cell.setCellStyle(csHead);

                        int curRow = 6;
                        int i = 1;

                        try {
                            report.setServiceCode(objService.getHosCode());
                            report.setDateopd(objDateopd.getDateopd());

                            ResultSet rs = opae74DAO.genReportOpae74(report);

                            while (rs.next()) {

                                String vPid = rs.getString("pid");
                                String vPname = rs.getString("pname");
                                String vHn = rs.getString("hn");
                                String vHmain = rs.getString("hmain");
                                String vDateopd = rs.getString("dateopd");
                                String vdateopdTh = rs.getString("dateopdTh");
                                String vOptype = rs.getString("optype");
                                double vchrg_ophc = rs.getDouble("chrg_ophc");
                                double vchrg_stditem = rs.getDouble("chrg_stditem");
                                double vchrg_197 = rs.getDouble("chrg_197");
                                double vchrg_car = rs.getDouble("chrg_car");
                                double vchrg_rehab_inst = rs.getDouble("chrg_rehab_inst");
                                double vchrg_other = rs.getDouble("chrg_other");
                                double vchrg_total = rs.getDouble("chrg_total");
                                double vchrg_total_final = rs.getDouble("chrg_total_final");
                                double vpayrate = rs.getDouble("payrate");
                                double vpaid_ophc = rs.getDouble("paid_ophc");
                                double vpaid_stditem = rs.getDouble("paid_stditem");
                                double vpaid_197 = rs.getDouble("paid_197");
                                double vpaid_car = rs.getDouble("paid_car");
                                double vpaid_rehab_inst = rs.getDouble("paid_rehab_inst");
                                double vpaid_other = rs.getDouble("paid_other");
                                double vpaid_total = rs.getDouble("paid_total");
                                double vpaid_total_final = rs.getDouble("paid_total_final");
                                String vTxid = rs.getString("txid");
                                String vInvoice_no = rs.getString("invoice_no");

                                row = sheet.createRow(curRow);
                                row.setHeight((short) 360);
                                cell = row.createCell(0);
                                cell.setCellValue(i);
                                cell.setCellStyle(csNum4);

                                cell = row.createCell(1);
                                cell.setCellValue(function.subStringPid(vPid));
                                cell.setCellStyle(csStringLeft);

                                cell = row.createCell(2);
                                cell.setCellValue(vPname);
                                cell.setCellStyle(csStringLeft);

                                cell = row.createCell(3);
                                cell.setCellValue(vHn);
                                cell.setCellStyle(csStringCenter);

                                cell = row.createCell(4);
                                cell.setCellValue(vHmain);
                                cell.setCellStyle(csStringLeft);

                                cell = row.createCell(5);
                                cell.setCellValue(vdateopdTh);
                                cell.setCellStyle(csStringCenter);

                                cell = row.createCell(6);
                                cell.setCellValue(vOptype);
                                cell.setCellStyle(csStringCenter);

                                cell = row.createCell(7);
                                cell.setCellValue(vchrg_ophc);
                                cell.setCellStyle(csDouble2);

                                cell = row.createCell(8);
                                cell.setCellValue(vchrg_stditem);
                                cell.setCellStyle(csDouble2);

                                cell = row.createCell(9);
                                cell.setCellValue(vchrg_197);
                                cell.setCellStyle(csDouble2);

                                cell = row.createCell(10);
                                cell.setCellValue(vchrg_car);
                                cell.setCellStyle(csDouble2);

                                cell = row.createCell(11);
                                cell.setCellValue(vchrg_rehab_inst);
                                cell.setCellStyle(csDouble2);

                                cell = row.createCell(12);
                                cell.setCellValue(vchrg_other);
                                cell.setCellStyle(csDouble2);

                                cell = row.createCell(13);
                                cell.setCellValue(vchrg_total);
                                cell.setCellStyle(csDouble2);

                                cell = row.createCell(14);
                                cell.setCellValue(vchrg_total_final);
                                cell.setCellStyle(csDouble2);

                                cell = row.createCell(15);
                                cell.setCellValue(vpayrate);
                                cell.setCellStyle(csDouble2Center);

                                cell = row.createCell(16);
                                cell.setCellValue(vpaid_ophc);
                                cell.setCellStyle(csDouble2);

                                cell = row.createCell(17);
                                cell.setCellValue(vpaid_stditem);
                                cell.setCellStyle(csDouble2);

                                cell = row.createCell(18);
                                cell.setCellValue(vpaid_197);
                                cell.setCellStyle(csDouble2);

                                cell = row.createCell(19);
                                cell.setCellValue(vpaid_car);
                                cell.setCellStyle(csDouble2);

                                cell = row.createCell(20);
                                cell.setCellValue(vpaid_rehab_inst);
                                cell.setCellStyle(csDouble2);

                                cell = row.createCell(21);
                                cell.setCellValue(vpaid_other);
                                cell.setCellStyle(csDouble2);

                                cell = row.createCell(22);
                                cell.setCellValue(vpaid_total);
                                cell.setCellStyle(csDouble2);

                                cell = row.createCell(23);
                                cell.setCellValue(vpaid_total_final);
                                cell.setCellStyle(csDouble2);

                                cell = row.createCell(24);
                                cell.setCellValue(vTxid);
                                cell.setCellStyle(csStringtxid);

                                curRow++;
                                i++;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Console.LOG(e.getMessage(), 0);
                            programeStatus.setMessage(ConstantMessage.MSG_PROCESS_FAILS + e.toString());
                            programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
                            programeStatus.setProcessStatus(false);
                        } finally {
                            this.opae74DAO.closeConnection();
                        }

                        /*
                         * Sumary
                         */
                        row = sheet.createRow(curRow);
                        row.setHeight((short) 450);
                        cell = row.createCell(0);
                        cell.setCellValue("รวม");
                        sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 6));
                        cell.setCellStyle(csHeadTab);

                        row.createCell(1).setCellStyle(csHeadTab);
                        row.createCell(2).setCellStyle(csHeadTab);
                        row.createCell(3).setCellStyle(csHeadTab);
                        row.createCell(4).setCellStyle(csHeadTab);
                        row.createCell(5).setCellStyle(csHeadTab);
                        row.createCell(6).setCellStyle(csHeadTab);

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
                        cell.setCellFormula(builderFormulaSum(13, row_formula_start, curRow));
                        cell.setCellStyle(csDouble2B);

                        cell = row.createCell(14);
                        cell.setCellFormula(builderFormulaSum(14, row_formula_start, curRow));
                        cell.setCellStyle(csDouble2B);

                        cell = row.createCell(15);
                        //cell.setCellFormula(builderFormula(15, row_formula_start, curRow));
                        cell.setCellStyle(csNum4B);

                        cell = row.createCell(16);
                        cell.setCellFormula(builderFormulaSum(16, row_formula_start, curRow));
                        cell.setCellStyle(csDouble2B);

                        cell = row.createCell(17);
                        cell.setCellFormula(builderFormulaSum(17, row_formula_start, curRow));
                        cell.setCellStyle(csDouble2B);

                        cell = row.createCell(18);
                        cell.setCellFormula(builderFormulaSum(18, row_formula_start, curRow));
                        cell.setCellStyle(csDouble2B);

                        cell = row.createCell(19);
                        cell.setCellFormula(builderFormulaSum(19, row_formula_start, curRow));
                        cell.setCellStyle(csDouble2B);

                        cell = row.createCell(20);
                        cell.setCellFormula(builderFormulaSum(20, row_formula_start, curRow));
                        cell.setCellStyle(csDouble2B);

                        cell = row.createCell(21);
                        cell.setCellFormula(builderFormulaSum(21, row_formula_start, curRow));
                        cell.setCellStyle(csDouble2B);

                        cell = row.createCell(22);
                        cell.setCellFormula(builderFormulaSum(22, row_formula_start, curRow));
                        cell.setCellStyle(csDouble2B);

                        cell = row.createCell(23);
                        cell.setCellFormula(builderFormulaSum(23, row_formula_start, curRow));
                        cell.setCellStyle(csDouble2B);

                        workbookBase.setSheetName(0, objService.getHosCode() + "_" + objDateopd.getDateopd());
                        workbookBase.write(out);
                        Console.LOG(Message.exportSuccess(objService.getHosCode()), 1);
                        out.close();
                        file.close();
                    } // end for loop dateopd

                }// end for loop hcode

                programeStatus.setMessage(ConstantMessage.MSG_REPORT_SUCCESS);
                programeStatus.setTitle(ConstantMessage.MSG_REPORT_COMPLETE);
                programeStatus.setProcessStatus(true);
            } else {  // ไม่พบข้อมูลที่ออกรายงาน
                Console.LOG("ไม่มีข้อมูลให้ออกรายงาน", 0);
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

    public ProgrameStatus opae74_sum(OppReport report) {
        FileOutputStream out = null;
        FileInputStream file = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        try {
            file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"OPAE74_summary.xls"));

            out = new FileOutputStream(report.getPathFile() + ""+File.separator+"opae74_summary" + report.getYearMonth() + "-" + report.getNo() + ".xls");

            HSSFWorkbook wb74 = new HSSFWorkbook(file);
            this.loadStyle(wb74);

            /**
             * End Style
             */
            String EXCELL_HEADER = "กรณีอุบัติเหตุ/ฉุกเฉินต่างบัญชีเครือข่าย(OPAE)  กรณีผู้พิการ(Opพิการ)  กรณีฟื้นฟูสมรรถภาพ และอุปกรณ์เครื่องช่วยคนพิการ ปีงบประมาณ " + new DateUtil().getBudgeMonthYear_543(report.getBudget_stmp(),ConstantVariable.BUDGET_MONTH);
            String EXCELL_MONTH = report.getTitle2();

            HSSFSheet sheet = workbookBase.getSheetAt(0);

            sheet.createFreezePane(2, 5);

            HSSFCell cell = null;
            HSSFRow row = null;

            // row 0
            row = sheet.createRow(0);
            row.setHeight((short) 390);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_HEADER);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
            cell.setCellStyle(csHead);

            // row 1
            row = sheet.createRow(1);
            row.setHeight((short) 390);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_MONTH);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));
            cell.setCellStyle(csHead);

            //row 4 ตัวเนื้อหา
            int curRow = 5;

            ResultSet rs = null;

            try {
                rs = opae74DAO.genReportOpae74(report);

                while (rs.next()) {

                    int vCol0 = rs.getInt("no");
                    String vCol1 = rs.getString("hcode");
                    //String vCol2 = rs.getString("PROVINCE_NAME");
                    int vCol3 = rs.getInt("n_AE");
                    int vCol4 = rs.getInt("n_74");
                    int vCol5 = rs.getInt("n_total");
                    double vCol6 = rs.getDouble("chrg_ophc");
                    double vCol7 = rs.getDouble("chrg_stditem");
                    double vCol8 = rs.getDouble("chrg_197");
                    double vCol9 = rs.getDouble("chrg_car");
                    double vCol10 = rs.getDouble("chrg_other");
                    double vCol11 = rs.getDouble("chrg_total");
                    double vCol12 = rs.getDouble("chrg_ophc_74");
                    double vCol13 = rs.getDouble("chrg_stditem_74");
                    double vCol14 = rs.getDouble("chrg_197_74");
                    double vCol15 = rs.getDouble("chrg_car_74");
                    double vCol16 = rs.getDouble("chrg_rehab_inst_74");
                    double vCol17 = rs.getDouble("chrg_other_74");
                    double vCol18 = rs.getDouble("chrg_total_74");
                    double vCol19 = rs.getDouble("chrg_totalAE74");
                    double vCol20 = rs.getDouble("paid_ophc");
                    double vCol21 = rs.getDouble("paid_stditem");
                    double vCol22 = rs.getDouble("paid_197");
                    double vCol23 = rs.getDouble("paid_car");
                    double vCol24 = rs.getDouble("paid_other");
                    double vCol25 = rs.getDouble("paid_total");
                    double vCol26 = rs.getDouble("paid_ophc_74");
                    double vCol27 = rs.getDouble("paid_stditem_74");
                    double vCol28 = rs.getDouble("paid_197_74");
                    double vCol29 = rs.getDouble("paid_car_74");
                    double vCol30 = rs.getDouble("paid_rehab_inst_74");
                    double vCol31 = rs.getDouble("paid_other_74");
                    double vCol32 = rs.getDouble("paid_total_74");
                    double vCol33 = rs.getDouble("paid_totalAE74");

                    row = sheet.createRow(curRow);
                    row.setHeight((short) 360);
                    cell = row.createCell(0);
                    cell.setCellValue(vCol0);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(1);
                    cell.setCellValue(vCol1);
                    cell.setCellStyle(csStringLeft);

                    /*cell = row.createCell(2);
                     cell.setCellValue(vCol2);
                     cell.setCellStyle(csDetail);*/
                    cell = row.createCell(2);
                    cell.setCellValue(vCol3);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(3);
                    cell.setCellValue(vCol4);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(4);
                    cell.setCellValue(vCol5);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(5);
                    cell.setCellValue(vCol6);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(6);
                    cell.setCellValue(vCol7);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(7);
                    cell.setCellValue(vCol8);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(8);
                    cell.setCellValue(vCol9);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(9);
                    cell.setCellValue(vCol10);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(10);
                    cell.setCellValue(vCol11);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(11);
                    cell.setCellValue(vCol12);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(12);
                    cell.setCellValue(vCol13);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(13);
                    cell.setCellValue(vCol14);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(14);
                    cell.setCellValue(vCol15);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(15);
                    cell.setCellValue(vCol16);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(16);
                    cell.setCellValue(vCol17);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(17);
                    cell.setCellValue(vCol18);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(18);
                    cell.setCellValue(vCol19);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(19);
                    cell.setCellValue(vCol20);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(20);
                    cell.setCellValue(vCol21);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(21);
                    cell.setCellValue(vCol22);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(22);
                    cell.setCellValue(vCol23);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(23);
                    cell.setCellValue(vCol24);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(24);
                    cell.setCellValue(vCol25);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(25);
                    cell.setCellValue(vCol26);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(26);
                    cell.setCellValue(vCol27);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(27);
                    cell.setCellValue(vCol28);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(28);
                    cell.setCellValue(vCol29);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(29);
                    cell.setCellValue(vCol30);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(30);
                    cell.setCellValue(vCol31);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(31);
                    cell.setCellValue(vCol32);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(32);
                    cell.setCellValue(vCol33);
                    cell.setCellStyle(csDouble2);

                    curRow++;

                }

            } catch (SQLException e) {
                e.printStackTrace();
                Console.LOG(e.getMessage(), 0);
                programeStatus.setMessage(ConstantMessage.MSG_PROCESS_FAILS + e.toString());
                programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
                programeStatus.setProcessStatus(false);
            } finally {
                this.opae74DAO.closeConnection();
            }

            row = sheet.createRow(curRow);
            row.setHeight((short) 450);
            cell = row.createCell(0);
            cell.setCellValue("รวม");
            sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 1));
            cell.setCellStyle(csNum4B);

            cell = row.createCell(1);
            cell.setCellStyle(csNum4);

            /*cell = row.createCell(2);
             cell.setCellStyle(csNum4B);*/
            cell = row.createCell(2);
            cell.setCellFormula("SUM(C6:C" + (curRow) + ")");
            cell.setCellStyle(csNum4B);

            cell = row.createCell(3);
            cell.setCellFormula("SUM(D6:D" + (curRow) + ")");
            cell.setCellStyle(csNum4B);

            cell = row.createCell(4);
            cell.setCellFormula("SUM(E6:E" + (curRow) + ")");
            cell.setCellStyle(csNum4B);

            cell = row.createCell(5);
            cell.setCellFormula("SUM(F6:F" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(6);
            cell.setCellFormula("SUM(G6:G" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(7);
            cell.setCellFormula("SUM(H6:H" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(8);
            cell.setCellFormula("SUM(I6:I" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(9);
            cell.setCellFormula("SUM(J6:J" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(10);
            cell.setCellFormula("SUM(K6:K" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(11);
            cell.setCellFormula("SUM(L6:L" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(12);
            cell.setCellFormula("SUM(M6:M" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(13);
            cell.setCellFormula("SUM(N6:N" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(14);
            cell.setCellFormula("SUM(O6:O" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(15);
            cell.setCellFormula("SUM(P6:P" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(16);
            cell.setCellFormula("SUM(Q6:Q" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(17);
            cell.setCellFormula("SUM(R6:R" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(18);
            cell.setCellFormula("SUM(S6:S" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(19);
            cell.setCellFormula("SUM(T6:T" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(20);
            cell.setCellFormula("SUM(U6:U" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(21);
            cell.setCellFormula("SUM(V6:V" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(22);
            cell.setCellFormula("SUM(W6:W" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(23);
            cell.setCellFormula("SUM(X6:X" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(24);
            cell.setCellFormula("SUM(Y6:Y" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(25);
            cell.setCellFormula("SUM(Z6:Z" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(26);
            cell.setCellFormula("SUM(AA6:AA" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(27);
            cell.setCellFormula("SUM(AB6:AB" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(28);
            cell.setCellFormula("SUM(AC6:AC" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(29);
            cell.setCellFormula("SUM(AD6:AD" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(30);
            cell.setCellFormula("SUM(AE6:AE" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(31);
            cell.setCellFormula("SUM(AF6:AF" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(32);
            cell.setCellFormula("SUM(AG6:AG" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            Console.LOG("ออกรายงานสรุปเรียบร้อยแล้ว", 1);
            workbookBase.write(out);
            out.close();

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

    public ProgrameStatus opae74_sum_ByDateopd(OppReport report) {
        FileOutputStream out = null;
        FileInputStream file = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        try {

            List<HospitalService> listDateOpd = opae74DAO.getDateopdGroupService(report);

            if (!listDateOpd.isEmpty()) { // มีข้อมูล ออกรายงาน

                for (int j = 0; j < listDateOpd.size(); j++) {
                    HospitalService objDateopd = listDateOpd.get(j);

                    file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"OPAE74_summary.xls"));

                    out = new FileOutputStream(report.getPathFile() + ""+File.separator+"opae74_summary" + report.getYearMonth() + "-" + report.getNo() + " [" + objDateopd.getDateopd() + "] .xls");

                    HSSFWorkbook wb74 = new HSSFWorkbook(file);
                    this.loadStyle(wb74);

                    /**
                     * End Style
                     */
                    String EXCELL_HEADER = "กรณีอุบัติเหตุ/ฉุกเฉินต่างบัญชีเครือข่าย(OPAE)  กรณีผู้พิการ(Opพิการ)  กรณีฟื้นฟูสมรรถภาพ และอุปกรณ์เครื่องช่วยคนพิการ ปีงบประมาณ " + new DateUtil().getBudgeMonthYear_543(report.getBudget_stmp(),ConstantVariable.BUDGET_MONTH);
                    String EXCELL_MONTH = report.getTitle2() + " เดือน: " + objDateopd.getDateopd_th();

                    HSSFSheet sheet = workbookBase.getSheetAt(0);

                    sheet.createFreezePane(2, 5);

                    HSSFCell cell = null;
                    HSSFRow row = null;

                    // row 0
                    row = sheet.createRow(0);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_HEADER);
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
                    cell.setCellStyle(csHead);

                    // row 1
                    row = sheet.createRow(1);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_MONTH);
                    sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));
                    cell.setCellStyle(csHead);

                    //row 4 ตัวเนื้อหา
                    int curRow = 5;

                    ResultSet rs = null;

                    try {
                        report.setDateopd(objDateopd.getDateopd());
                        rs = opae74DAO.genReportOpae74(report);

                        while (rs.next()) {

                            int vCol0 = rs.getInt("no");
                            String vCol1 = rs.getString("hcode");
                            //String vCol2 = rs.getString("PROVINCE_NAME");
                            int vCol3 = rs.getInt("n_AE");
                            int vCol4 = rs.getInt("n_74");
                            int vCol5 = rs.getInt("n_total");
                            double vCol6 = rs.getDouble("chrg_ophc");
                            double vCol7 = rs.getDouble("chrg_stditem");
                            double vCol8 = rs.getDouble("chrg_197");
                            double vCol9 = rs.getDouble("chrg_car");
                            double vCol10 = rs.getDouble("chrg_other");
                            double vCol11 = rs.getDouble("chrg_total");
                            double vCol12 = rs.getDouble("chrg_ophc_74");
                            double vCol13 = rs.getDouble("chrg_stditem_74");
                            double vCol14 = rs.getDouble("chrg_197_74");
                            double vCol15 = rs.getDouble("chrg_car_74");
                            double vCol16 = rs.getDouble("chrg_rehab_inst_74");
                            double vCol17 = rs.getDouble("chrg_other_74");
                            double vCol18 = rs.getDouble("chrg_total_74");
                            double vCol19 = rs.getDouble("chrg_totalAE74");
                            double vCol20 = rs.getDouble("paid_ophc");
                            double vCol21 = rs.getDouble("paid_stditem");
                            double vCol22 = rs.getDouble("paid_197");
                            double vCol23 = rs.getDouble("paid_car");
                            double vCol24 = rs.getDouble("paid_other");
                            double vCol25 = rs.getDouble("paid_total");
                            double vCol26 = rs.getDouble("paid_ophc_74");
                            double vCol27 = rs.getDouble("paid_stditem_74");
                            double vCol28 = rs.getDouble("paid_197_74");
                            double vCol29 = rs.getDouble("paid_car_74");
                            double vCol30 = rs.getDouble("paid_rehab_inst_74");
                            double vCol31 = rs.getDouble("paid_other_74");
                            double vCol32 = rs.getDouble("paid_total_74");
                            double vCol33 = rs.getDouble("paid_totalAE74");

                            row = sheet.createRow(curRow);
                            row.setHeight((short) 360);
                            cell = row.createCell(0);
                            cell.setCellValue(vCol0);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(1);
                            cell.setCellValue(vCol1);
                            cell.setCellStyle(csStringLeft);

                            /*cell = row.createCell(2);
                             cell.setCellValue(vCol2);
                             cell.setCellStyle(csDetail);*/
                            cell = row.createCell(2);
                            cell.setCellValue(vCol3);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(3);
                            cell.setCellValue(vCol4);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(4);
                            cell.setCellValue(vCol5);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(5);
                            cell.setCellValue(vCol6);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(6);
                            cell.setCellValue(vCol7);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(7);
                            cell.setCellValue(vCol8);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(8);
                            cell.setCellValue(vCol9);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(9);
                            cell.setCellValue(vCol10);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(10);
                            cell.setCellValue(vCol11);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(11);
                            cell.setCellValue(vCol12);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(12);
                            cell.setCellValue(vCol13);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(13);
                            cell.setCellValue(vCol14);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(14);
                            cell.setCellValue(vCol15);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(15);
                            cell.setCellValue(vCol16);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(16);
                            cell.setCellValue(vCol17);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(17);
                            cell.setCellValue(vCol18);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(18);
                            cell.setCellValue(vCol19);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(19);
                            cell.setCellValue(vCol20);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(20);
                            cell.setCellValue(vCol21);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(21);
                            cell.setCellValue(vCol22);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(22);
                            cell.setCellValue(vCol23);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(23);
                            cell.setCellValue(vCol24);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(24);
                            cell.setCellValue(vCol25);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(25);
                            cell.setCellValue(vCol26);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(26);
                            cell.setCellValue(vCol27);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(27);
                            cell.setCellValue(vCol28);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(28);
                            cell.setCellValue(vCol29);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(29);
                            cell.setCellValue(vCol30);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(30);
                            cell.setCellValue(vCol31);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(31);
                            cell.setCellValue(vCol32);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(32);
                            cell.setCellValue(vCol33);
                            cell.setCellStyle(csDouble2);

                            curRow++;

                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                        Console.LOG(e.getMessage(), 0);
                        programeStatus.setMessage(ConstantMessage.MSG_PROCESS_FAILS + e.toString());
                        programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
                        programeStatus.setProcessStatus(false);
                    } finally {
                        this.opae74DAO.closeConnection();
                    }

                    row = sheet.createRow(curRow);
                    row.setHeight((short) 450);
                    cell = row.createCell(0);
                    cell.setCellValue("รวม");
                    sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 1));
                    cell.setCellStyle(csNum4B);

                    cell = row.createCell(1);
                    cell.setCellStyle(csNum4);

                    /*cell = row.createCell(2);
                     cell.setCellStyle(csNum4B);*/
                    cell = row.createCell(2);
                    cell.setCellFormula("SUM(C6:C" + (curRow) + ")");
                    cell.setCellStyle(csNum4B);

                    cell = row.createCell(3);
                    cell.setCellFormula("SUM(D6:D" + (curRow) + ")");
                    cell.setCellStyle(csNum4B);

                    cell = row.createCell(4);
                    cell.setCellFormula("SUM(E6:E" + (curRow) + ")");
                    cell.setCellStyle(csNum4B);

                    cell = row.createCell(5);
                    cell.setCellFormula("SUM(F6:F" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(6);
                    cell.setCellFormula("SUM(G6:G" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(7);
                    cell.setCellFormula("SUM(H6:H" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(8);
                    cell.setCellFormula("SUM(I6:I" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(9);
                    cell.setCellFormula("SUM(J6:J" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(10);
                    cell.setCellFormula("SUM(K6:K" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(11);
                    cell.setCellFormula("SUM(L6:L" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(12);
                    cell.setCellFormula("SUM(M6:M" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(13);
                    cell.setCellFormula("SUM(N6:N" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(14);
                    cell.setCellFormula("SUM(O6:O" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(15);
                    cell.setCellFormula("SUM(P6:P" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(16);
                    cell.setCellFormula("SUM(Q6:Q" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(17);
                    cell.setCellFormula("SUM(R6:R" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(18);
                    cell.setCellFormula("SUM(S6:S" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(19);
                    cell.setCellFormula("SUM(T6:T" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(20);
                    cell.setCellFormula("SUM(U6:U" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(21);
                    cell.setCellFormula("SUM(V6:V" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(22);
                    cell.setCellFormula("SUM(W6:W" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(23);
                    cell.setCellFormula("SUM(X6:X" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(24);
                    cell.setCellFormula("SUM(Y6:Y" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(25);
                    cell.setCellFormula("SUM(Z6:Z" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(26);
                    cell.setCellFormula("SUM(AA6:AA" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(27);
                    cell.setCellFormula("SUM(AB6:AB" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(28);
                    cell.setCellFormula("SUM(AC6:AC" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(29);
                    cell.setCellFormula("SUM(AD6:AD" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(30);
                    cell.setCellFormula("SUM(AE6:AE" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(31);
                    cell.setCellFormula("SUM(AF6:AF" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(32);
                    cell.setCellFormula("SUM(AG6:AG" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    Console.LOG("ออกรายงานสรุปเรียบร้อยแล้ว", 1);
                    workbookBase.setSheetName(0, "summary " + objDateopd.getDateopd());
                    workbookBase.write(out);
                    out.close();
                }
            } else { //ไม่ม้ข้อมูลที่ออก
                Console.LOG("ไม่มีข้อมูลให้ออกรายงาน", 0);
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

    public ProgrameStatus opae74_sumRehap_byDateopd(OppReport report) {
        FileOutputStream out = null;
        FileInputStream file = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        try {
            List<HospitalService> listDateOpd = opae74DAO.getDateopdGroupService(report);

            if (listDateOpd != null) { // มีข้อมูล ออกรายงาน

                for (int j = 0; j < listDateOpd.size(); j++) {
                    HospitalService objDateopd = listDateOpd.get(j);

                    file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"OPAE74_split rehap_summary.xls"));

                    out = new FileOutputStream(report.getPathFile() + ""+File.separator+"OPAE74_summary_split_rehap" + report.getYearMonth() + "-" + report.getNo() + " [" + objDateopd.getDateopd() + "] .xls");

                    HSSFWorkbook wb74 = new HSSFWorkbook(file);
                    this.loadStyle(wb74);

                    /**
                     * End Style
                     */
                    String EXCELL_HEADER = "กรณีอุบัติเหตุ/ฉุกเฉินต่างบัญชีเครือข่าย(OPAE)/ผู้พิการ/ค่าพาหนะ และ กรณีฟื้นฟูสมรรถภาพและอุปกรณ์เครื่องช่วยคนพิการ ปีงบประมาณ " + new DateUtil().getBudgeMonthYear_543(report.getBudget_stmp(),ConstantVariable.BUDGET_MONTH);
                    String EXCELL_MONTH = report.getTitle2() + " เดือน: " + objDateopd.getDateopd_th();

                    HSSFSheet sheet = workbookBase.getSheetAt(0);

                    sheet.createFreezePane(3, 6);

                    HSSFCell cell = null;
                    HSSFRow row = null;

                    // row 0
                    row = sheet.createRow(0);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_HEADER);
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
                    cell.setCellStyle(csHead);

                    // row 1
                    row = sheet.createRow(1);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_MONTH);
                    sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));
                    cell.setCellStyle(csHead);

                    //row 4 ตัวเนื้อหา
                    int curRow = 6;

                    ResultSet rs = null;

                    try {
                        report.setDateopd(objDateopd.getDateopd());
                        rs = opae74DAO.genReportOpae74(report);

                        while (rs.next()) {

                            int vCol0 = rs.getInt("no");
                            String vCol1 = rs.getString("HCODE");
                            String vCol2 = rs.getString("HCODENAME");

                            int vCol3 = rs.getInt("N_AE");
                            int vCol4 = rs.getInt("N_74");
                            int vCol5 = rs.getInt("N_TOTAL");

                            double vCol6 = rs.getDouble("CHRG_TOTALAE74_NONREHAP");
                            double vCol7 = rs.getDouble("CHRG_REHAB_INST_74_BKK");
                            double vCol8 = rs.getDouble("CHRG_REHAB_INST_74_NONBKK");
                            double vCol9 = rs.getDouble("CHRG_TOTALAE74");

                            double vCol10 = rs.getDouble("PAID_TOTALAE74_NONREHAP");
                            double vCol11 = rs.getDouble("PAID_REHAB_INST_74_BKK");
                            double vCol12 = rs.getDouble("PAID_REHAB_INST_74_NONBKK");
                            double vCol13 = rs.getDouble("PAID_TOTALAE74");
                            //double vCol14 = rs.getDouble("SUM(CHRG_TOTAL+PAID_TOTAL)");

                            row = sheet.createRow(curRow);
                            row.setHeight((short) 360);
                            cell = row.createCell(0);
                            cell.setCellValue(vCol0);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(1);
                            cell.setCellValue(vCol1);
                            cell.setCellStyle(csStringCenter);

                            cell = row.createCell(2);
                            cell.setCellValue(vCol2);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(3);
                            cell.setCellValue(vCol3);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(4);
                            cell.setCellValue(vCol4);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(5);
                            cell.setCellValue(vCol5);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(6);
                            cell.setCellValue(vCol6);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(7);
                            cell.setCellValue(vCol7);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(8);
                            cell.setCellValue(vCol8);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(9);
                            cell.setCellValue(vCol9);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(10);
                            cell.setCellValue(vCol10);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(11);
                            cell.setCellValue(vCol11);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(12);
                            cell.setCellValue(vCol12);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(13);
                            cell.setCellValue(vCol13);
                            cell.setCellStyle(csDouble2);

//                    cell = row.createCell(14);
//                    cell.setCellValue(vCol14);
//                    cell.setCellStyle(csNum2);
                            curRow++;

                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                        Console.LOG(e.getMessage(), 0);
                        programeStatus.setMessage(ConstantMessage.MSG_PROCESS_FAILS + e.toString());
                        programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
                        programeStatus.setProcessStatus(false);
                    } finally {
                        rs.close();
                        this.opae74DAO.closeConnection();
                    }

                    row = sheet.createRow(curRow);
                    row.setHeight((short) 450);
                    cell = row.createCell(0);
                    cell.setCellValue("รวม");
                    sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 2));
                    cell.setCellStyle(csNum4B);

//            cell = row.createCell(1);
//            cell.setCellStyle(csNum4);
//            cell = row.createCell(3);
//            cell.setCellFormula("SUM(C7:C" + (curRow) + ")");
//            cell.setCellStyle(csNum4B);
                    cell = row.createCell(3);
                    cell.setCellFormula("SUM(D7:D" + (curRow) + ")");
                    cell.setCellStyle(csNum4B);

                    cell = row.createCell(4);
                    cell.setCellFormula("SUM(E7:E" + (curRow) + ")");
                    cell.setCellStyle(csNum4B);

                    cell = row.createCell(5);
                    cell.setCellFormula("SUM(F7:F" + (curRow) + ")");
                    cell.setCellStyle(csNum4B);

                    cell = row.createCell(6);
                    cell.setCellFormula("SUM(G7:G" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(7);
                    cell.setCellFormula("SUM(H7:H" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(8);
                    cell.setCellFormula("SUM(I7:I" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(9);
                    cell.setCellFormula("SUM(J7:J" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(10);
                    cell.setCellFormula("SUM(K7:K" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(11);
                    cell.setCellFormula("SUM(L7:L" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(12);
                    cell.setCellFormula("SUM(M7:M" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(13);
                    cell.setCellFormula("SUM(N7:N" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

//            cell = row.createCell(14);
//            cell.setCellFormula("SUM(O7:O" + (curRow) + ")");
//            cell.setCellStyle(csNum2B);
                    Console.LOG("ออกรายงานสรุปเรียบร้อยแล้ว", 1);
                    workbookBase.setSheetName(0, "summary rehap " + objDateopd.getDateopd());
                    workbookBase.write(out);
                    out.close();
                }
            } else { //ไม่ม้ข้อมูลที่ออก
                Console.LOG("ไม่มีข้อมูลให้ออกรายงาน", 0);
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

    public ProgrameStatus opae74_itemDetail(OppReport report) {
        FileOutputStream out = null;
        FileInputStream file = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        int col_last = 39;
        int row_start = 4; // index row
        int row_formula_start = row_start + 1;
        int col_txtid_width = col_last + 1;
        try {
            file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"OPAE74_item_detail.xls"));

            out = new FileOutputStream(report.getPathFile() + ""+File.separator+"OPAE74_item_detail" + report.getYearMonth() + "-" + report.getNo() + ".xls");

            HSSFWorkbook wb74 = new HSSFWorkbook(file);
            this.loadStyle(wb74);

            /**
             * End Style
             */
            String EXCELL_HEADER = "รายงานรายละเอียด (Item Detail)";

            HSSFSheet sheet = workbookBase.getSheetAt(0);

            sheet.createFreezePane(0, 4); //sheet.createFreezePane(colSplit, rowSplit);
            sheet.setColumnWidth(col_txtid_width, WIDTH_TXID);

            HSSFCell cell = null;
            HSSFRow row = null;

            // row 0
            row = sheet.createRow(1);
            row.setHeight((short) 390);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_HEADER);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col_last));
            cell.setCellStyle(csHead);

            //row 4 ตัวเนื้อหา
            int curRow = row_start;

            ResultSet rs = null;

            try {
                rs = opae74DAO.genReportOpae74(report);

                while (rs.next()) {
                    String vCol0 = rs.getString("pid");
                    String vCol1 = rs.getString("txid");
                    String vCol2 = rs.getString("pname");
                    String vCol3 = rs.getString("hn");
                    String vCol4 = rs.getString("hmain");
                    String vCol5 = rs.getString("hcode");
                    String vCol6 = rs.getString("dateopd");

                    String vCol7 = rs.getString("optype");
                    String vCol8 = rs.getString("item_code");
                    double vCol9 = rs.getDouble("item_src");

                    double vCol10 = rs.getDouble("chrg_ophc");
                    double vCol11 = rs.getDouble("chrg_stditem");
                    double vCol12 = rs.getDouble("chrg_197");
                    double vCol13 = rs.getDouble("chrg_car");
                    double vCol14 = rs.getDouble("chrg_rehab_inst");
                    double vCol15 = rs.getDouble("chrg_other");
                    double vCol16 = rs.getDouble("chrg_total");
                    double vCol17 = rs.getDouble("chrg_total_final");
                    double vCol18 = rs.getDouble("payrate");
                    double vCol19 = rs.getDouble("paid_ophc");
                    double vCol20 = rs.getDouble("paid_stditem");
                    double vCol21 = rs.getDouble("paid_197");
                    double vCol22 = rs.getDouble("paid_car");
                    double vCol23 = rs.getDouble("paid_rehab_inst");
                    double vCol24 = rs.getDouble("paid_other");
                    double vCol25 = rs.getDouble("paid_total");
                    double vCol26 = rs.getDouble("paid_total_final");
                    String vCol27 = rs.getString("STMP");
                    String vCol28 = rs.getString("STMP_DESC");
                    int vCol29 = rs.getInt("clinic");
                    int vCol30 = rs.getInt("item_type");
                    double vCol31 = rs.getDouble("qty");
                    double vCol32 = rs.getDouble("price_total");
                    double vCol33 = rs.getDouble("price_ext");

                    double vCol34 = rs.getDouble("unitprice");
                    double vCol35 = rs.getDouble("stdprice");
                    double vCol36 = rs.getDouble("reimburse");
                    double vCol37 = rs.getDouble("is197");
                    String vTxid = rs.getString("txid");
                    String vInvoice_no = rs.getString("invoice_no");

                    row = sheet.createRow(curRow);
                    row.setHeight((short) 360);
                    cell = row.createCell(0);
                    cell.setCellValue(vCol0);
                    cell.setCellStyle(csStringPid);

                    cell = row.createCell(1);
                    cell.setCellValue(vCol1);
                    cell.setCellStyle(csStringLeft);

                    cell = row.createCell(2);
                    cell.setCellValue(vCol2);
                    cell.setCellStyle(csStringLeft);

                    cell = row.createCell(3);
                    cell.setCellValue(vCol3);
                    cell.setCellStyle(csStringLeft);

                    cell = row.createCell(4);
                    cell.setCellValue(vCol4);
                    cell.setCellStyle(csStringLeft);

                    cell = row.createCell(5);
                    cell.setCellValue(vCol5);
                    cell.setCellStyle(csStringLeft);

                    cell = row.createCell(6);
                    cell.setCellValue(vCol6);
                    cell.setCellStyle(csStringLeft);

                    cell = row.createCell(7);
                    cell.setCellValue(vCol7);
                    cell.setCellStyle(csStringLeft);

                    cell = row.createCell(8);
                    cell.setCellValue(vCol8);
                    cell.setCellStyle(csStringLeft);

                    cell = row.createCell(9);
                    cell.setCellValue(vCol9);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(10);
                    cell.setCellValue(vCol10);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(11);
                    cell.setCellValue(vCol11);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(12);
                    cell.setCellValue(vCol12);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(13);
                    cell.setCellValue(vCol13);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(14);
                    cell.setCellValue(vCol14);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(15);
                    cell.setCellValue(vCol15);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(16);
                    cell.setCellValue(vCol16);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(17);
                    cell.setCellValue(vCol17);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(18);
                    cell.setCellValue(vCol18);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(19);
                    cell.setCellValue(vCol19);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(20);
                    cell.setCellValue(vCol20);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(21);
                    cell.setCellValue(vCol21);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(22);
                    cell.setCellValue(vCol22);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(23);
                    cell.setCellValue(vCol23);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(24);
                    cell.setCellValue(vCol24);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(25);
                    cell.setCellValue(vCol25);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(26);
                    cell.setCellValue(vCol26);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(27);
                    cell.setCellValue(vCol27);
                    cell.setCellStyle(csStringLeft);

                    cell = row.createCell(28);
                    cell.setCellValue(vCol28);
                    cell.setCellStyle(csStringLeft);

                    cell = row.createCell(29);
                    cell.setCellValue(vCol29);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(30);
                    cell.setCellValue(vCol30);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(31);
                    cell.setCellValue(vCol31);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(32);
                    cell.setCellValue(vCol32);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(33);
                    cell.setCellValue(vCol33);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(34);
                    cell.setCellValue(vCol34);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(35);
                    cell.setCellValue(vCol35);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(36);
                    cell.setCellValue(vCol36);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(37);
                    cell.setCellValue(vCol37);
                    cell.setCellStyle(csNum3);

                    
                    
                    cell = row.createCell(38);
                    cell.setCellValue(vTxid);
                    cell.setCellStyle(csStringtxid);

                    curRow++;

                }

            } catch (SQLException e) {
                e.printStackTrace();
                Console.LOG(e.getMessage(), 0);
                programeStatus.setMessage(ConstantMessage.MSG_PROCESS_FAILS + e.toString());
                programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
                programeStatus.setProcessStatus(false);
            } finally {
                this.opae74DAO.closeConnection();
            }

            /*row = sheet.createRow(curRow);
             row.setHeight((short) 450);
             cell = row.createCell(0);
             cell.setCellValue("รวม");
             sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 1));
             cell.setCellStyle(csNum4B);

             cell = row.createCell(1);
             cell.setCellStyle(csNum4);

             //            cell = row.createCell(2);
             //             cell.setCellStyle(csNum4B);

             cell = row.createCell(2);
             cell.setCellFormula("SUM(C6:C" + (curRow) + ")");
             cell.setCellStyle(csNum4B);

             cell = row.createCell(3);
             cell.setCellFormula("SUM(D6:D" + (curRow) + ")");
             cell.setCellStyle(csNum4B);

             cell = row.createCell(4);
             cell.setCellFormula("SUM(E6:E" + (curRow) + ")");
             cell.setCellStyle(csNum4B);

             cell = row.createCell(5);
             cell.setCellFormula("SUM(F6:F" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(6);
             cell.setCellFormula("SUM(G6:G" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(7);
             cell.setCellFormula("SUM(H6:H" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(8);
             cell.setCellFormula("SUM(I6:I" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(9);
             cell.setCellFormula("SUM(J6:J" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(10);
             cell.setCellFormula("SUM(K6:K" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(11);
             cell.setCellFormula("SUM(L6:L" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(12);
             cell.setCellFormula("SUM(M6:M" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(13);
             cell.setCellFormula("SUM(N6:N" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(14);
             cell.setCellFormula("SUM(O6:O" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(15);
             cell.setCellFormula("SUM(P6:P" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(16);
             cell.setCellFormula("SUM(Q6:Q" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(17);
             cell.setCellFormula("SUM(R6:R" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(18);
             cell.setCellFormula("SUM(S6:S" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(19);
             cell.setCellFormula("SUM(T6:T" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(20);
             cell.setCellFormula("SUM(U6:U" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(21);
             cell.setCellFormula("SUM(V6:V" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(22);
             cell.setCellFormula("SUM(W6:W" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(23);
             cell.setCellFormula("SUM(X6:X" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(24);
             cell.setCellFormula("SUM(Y6:Y" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(25);
             cell.setCellFormula("SUM(Z6:Z" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(26);
             cell.setCellFormula("SUM(AA6:AA" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(27);
             cell.setCellFormula("SUM(AB6:AB" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(28);
             cell.setCellFormula("SUM(AC6:AC" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(29);
             cell.setCellFormula("SUM(AD6:AD" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(30);
             cell.setCellFormula("SUM(AE6:AE" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(31);
             cell.setCellFormula("SUM(AF6:AF" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(32);
             cell.setCellFormula("SUM(AG6:AG" + (curRow) + ")");
             cell.setCellStyle(csNum2B);*/
            Console.LOG("ออกรายงานสรุปเรียบร้อยแล้ว", 1);
            workbookBase.write(out);
            out.close();

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

    public ProgrameStatus opae74_carDetail(OppReport report) {
        FileOutputStream out = null;
        FileInputStream file = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        try {
            file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"OPAE74_car_detail.xls"));

            out = new FileOutputStream(report.getPathFile() + ""+File.separator+"OPAE74_car_detail" + report.getYearMonth() + "-" + report.getNo() + ".xls");

            HSSFWorkbook wb74 = new HSSFWorkbook(file);
            this.loadStyle(wb74);

            /**
             * End Style
             */
            String EXCELL_HEADER = "รายงานรายละเอียด (Car Detail)";

            HSSFSheet sheet = workbookBase.getSheetAt(0);

            sheet.createFreezePane(0, 4);
            sheet.setColumnWidth(33, WIDTH_TXID);
            HSSFCell cell = null;
            HSSFRow row = null;

            // row 0
            row = sheet.createRow(1);
            row.setHeight((short) 390);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_HEADER);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
            cell.setCellStyle(csHead);

            //row 4 ตัวเนื้อหา
            int curRow = 4;

            ResultSet rs = null;

            try {
                rs = opae74DAO.genReportOpae74(report);

                while (rs.next()) {

                    String vCol0 = rs.getString("HCODE");
                    String vCol1 = rs.getString("HCODENAME");
                    String vCol2 = rs.getString("PID");
                    String vCol3 = rs.getString("HN");
                    String vCol4 = rs.getString("PNAME");

                    String vCol5 = rs.getString("dateopd_thai_buddha");  //
                    String vCol6 = rs.getString("AGE_YY");
                    String vCol7 = rs.getString("PDXCODE");
                    double vCol8 = rs.getDouble("CHRG_OPHC");

                    double vCol9 = rs.getDouble("CHRG_197");
                    double vCol10 = rs.getDouble("CHRG_STDITEM");
                    double vCol11 = rs.getDouble("CHRG_CAR");
                    double vCol12 = rs.getDouble("CHRG_REHAB_INST");
                    double vCol13 = rs.getDouble("PAYRATE");

                    double vCol14 = rs.getDouble("CHRG_OTHER");
                    double vCol15 = rs.getDouble("CHRG_TOTAL");
                    double vCol16 = rs.getDouble("PAID_OPHC");
                    double vCol17 = rs.getDouble("PAID_197");
                    double vCol18 = rs.getDouble("PAID_STDITEM");

                    double vCol19 = rs.getDouble("PAID_CAR");
                    double vCol20 = rs.getDouble("PAID_REHAB_INST");
                    double vCol21 = rs.getDouble("PAID_OTHER");
                    String vCol22 = rs.getString("PAID_TOTAL");
                    String vCol23 = rs.getString("REMARK");

                    String vCol24 = rs.getString("STMP");
                    String vCol25 = rs.getString("RPT_DATE");
                    String vCol26 = rs.getString("SUBINSCL");
                    String vCol27 = rs.getString("PROVINCE");
                    int vCol28 = rs.getInt("RFCAR_FROM");

                    int vCol29 = rs.getInt("RFCAR_TO");
                    int vCol30 = rs.getInt("RFCAR_DISTANCE");
                    int vCol31 = rs.getInt("RFCAR_CHARGE");
                    int vCol32 = rs.getInt("RFCAR_REIMBURSE");
                    String vTxid = rs.getString("txid");

                    row = sheet.createRow(curRow);
                    row.setHeight((short) 360);
                    cell = row.createCell(0);
                    cell.setCellValue(vCol0);
                    cell.setCellStyle(csStringLeft);

                    cell = row.createCell(1);
                    cell.setCellValue(vCol1);
                    cell.setCellStyle(csStringLeft);

                    cell = row.createCell(2);
                    cell.setCellValue(vCol2);
                    cell.setCellStyle(csStringPid);

                    cell = row.createCell(3);
                    cell.setCellValue(vCol3);
                    cell.setCellStyle(csStringLeft);

                    cell = row.createCell(4);
                    cell.setCellValue(vCol4);
                    cell.setCellStyle(csStringLeft);

                    cell = row.createCell(5);
                    cell.setCellValue(vCol5);
                    cell.setCellStyle(csStringLeft);

                    cell = row.createCell(6);
                    cell.setCellValue(vCol6);
                    cell.setCellStyle(csNum3);

                    cell = row.createCell(7);
                    cell.setCellValue(vCol7);
                    cell.setCellStyle(csNum3);

                    cell = row.createCell(8);
                    cell.setCellValue(vCol8);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(9);
                    cell.setCellValue(vCol9);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(10);
                    cell.setCellValue(vCol10);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(11);
                    cell.setCellValue(vCol11);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(12);
                    cell.setCellValue(vCol12);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(13);
                    cell.setCellValue(vCol13);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(14);
                    cell.setCellValue(vCol14);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(15);
                    cell.setCellValue(vCol15);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(16);
                    cell.setCellValue(vCol16);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(17);
                    cell.setCellValue(vCol17);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(18);
                    cell.setCellValue(vCol18);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(19);
                    cell.setCellValue(vCol19);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(20);
                    cell.setCellValue(vCol20);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(21);
                    cell.setCellValue(vCol21);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(22);
                    cell.setCellValue(vCol22);
                    cell.setCellStyle(csStringLeft);

                    cell = row.createCell(23);
                    cell.setCellValue(vCol23);
                    cell.setCellStyle(csStringLeft);

                    cell = row.createCell(24);
                    cell.setCellValue(vCol24);
                    cell.setCellStyle(csStringLeft);

                    cell = row.createCell(25);
                    cell.setCellValue(vCol25);
                    cell.setCellStyle(csStringLeft);

                    cell = row.createCell(26);
                    cell.setCellValue(vCol26);
                    cell.setCellStyle(csNum3);

                    cell = row.createCell(27);
                    cell.setCellValue(vCol27);
                    cell.setCellStyle(csStringLeft);

                    cell = row.createCell(28);
                    cell.setCellValue(vCol28);
                    cell.setCellStyle(csNum3);

                    cell = row.createCell(29);
                    cell.setCellValue(vCol29);
                    cell.setCellStyle(csNum3);

                    cell = row.createCell(30);
                    cell.setCellValue(vCol30);
                    cell.setCellStyle(csNum3);

                    cell = row.createCell(31);
                    cell.setCellValue(vCol31);
                    cell.setCellStyle(csNum3);

                    cell = row.createCell(32);
                    cell.setCellValue(vCol32);
                    cell.setCellStyle(csNum3);

                    cell = row.createCell(33);
                    cell.setCellValue(vTxid);
                    cell.setCellStyle(csStringtxid);

                    curRow++;

                }

            } catch (SQLException e) {
                e.printStackTrace();
                Console.LOG(e.getMessage(), 0);
                programeStatus.setMessage(ConstantMessage.MSG_PROCESS_FAILS + e.toString());
                programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
                programeStatus.setProcessStatus(false);
            } finally {
                this.opae74DAO.closeConnection();
            }

            /* row = sheet.createRow(curRow);
             row.setHeight((short) 450);
             cell = row.createCell(0);
             cell.setCellValue("รวม");
             sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 1));
             cell.setCellStyle(csNum4B);

             cell = row.createCell(1);
             cell.setCellStyle(csNum4);

             //            cell = row.createCell(2);
             //             cell.setCellStyle(csNum4B);

             cell = row.createCell(2);
             cell.setCellFormula("SUM(C6:C" + (curRow) + ")");
             cell.setCellStyle(csNum4B);

             cell = row.createCell(3);
             cell.setCellFormula("SUM(D6:D" + (curRow) + ")");
             cell.setCellStyle(csNum4B);

             cell = row.createCell(4);
             cell.setCellFormula("SUM(E6:E" + (curRow) + ")");
             cell.setCellStyle(csNum4B);

             cell = row.createCell(5);
             cell.setCellFormula("SUM(F6:F" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(6);
             cell.setCellFormula("SUM(G6:G" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(7);
             cell.setCellFormula("SUM(H6:H" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(8);
             cell.setCellFormula("SUM(I6:I" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(9);
             cell.setCellFormula("SUM(J6:J" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(10);
             cell.setCellFormula("SUM(K6:K" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(11);
             cell.setCellFormula("SUM(L6:L" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(12);
             cell.setCellFormula("SUM(M6:M" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(13);
             cell.setCellFormula("SUM(N6:N" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(14);
             cell.setCellFormula("SUM(O6:O" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(15);
             cell.setCellFormula("SUM(P6:P" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(16);
             cell.setCellFormula("SUM(Q6:Q" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(17);
             cell.setCellFormula("SUM(R6:R" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(18);
             cell.setCellFormula("SUM(S6:S" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(19);
             cell.setCellFormula("SUM(T6:T" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(20);
             cell.setCellFormula("SUM(U6:U" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(21);
             cell.setCellFormula("SUM(V6:V" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(22);
             cell.setCellFormula("SUM(W6:W" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(23);
             cell.setCellFormula("SUM(X6:X" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(24);
             cell.setCellFormula("SUM(Y6:Y" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(25);
             cell.setCellFormula("SUM(Z6:Z" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(26);
             cell.setCellFormula("SUM(AA6:AA" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(27);
             cell.setCellFormula("SUM(AB6:AB" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(28);
             cell.setCellFormula("SUM(AC6:AC" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(29);
             cell.setCellFormula("SUM(AD6:AD" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(30);
             cell.setCellFormula("SUM(AE6:AE" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(31);
             cell.setCellFormula("SUM(AF6:AF" + (curRow) + ")");
             cell.setCellStyle(csNum2B);

             cell = row.createCell(32);
             cell.setCellFormula("SUM(AG6:AG" + (curRow) + ")");
             cell.setCellStyle(csNum2B);*/
            Console.LOG("ออกรายงานสรุปเรียบร้อยแล้ว", 1);
            workbookBase.write(out);
            out.close();

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

    public ProgrameStatus opae74_sumRehap(OppReport report) {
        FileOutputStream out = null;
        FileInputStream file = null;
        int startWriteExl = 6;
        ProgrameStatus programeStatus = new ProgrameStatus();
        try {
            file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"OPAE74_split rehap_summary.xls"));

            out = new FileOutputStream(report.getPathFile() + ""+File.separator+"OPAE74_summary_split_rehap" + report.getYearMonth() + "-" + report.getNo() + ".xls");

            HSSFWorkbook wb74 = new HSSFWorkbook(file);
            this.loadStyle(wb74);

            /**
             * End Style
             */
            String EXCELL_HEADER = "กรณีอุบัติเหตุ/ฉุกเฉินต่างบัญชีเครือข่าย(OPAE)/ผู้พิการ/ค่าพาหนะ และ กรณีฟื้นฟูสมรรถภาพและอุปกรณ์เครื่องช่วยคนพิการ " + new DateUtil().getBudgeMonthYear_543(report.getBudget_stmp(),ConstantVariable.BUDGET_MONTH);
            String EXCELL_MONTH = report.getTitle2();

            HSSFSheet sheet = workbookBase.getSheetAt(0);

            sheet.createFreezePane(3, startWriteExl);

            HSSFCell cell = null;
            HSSFRow row = null;

            // row 0
            row = sheet.createRow(0);
            row.setHeight((short) 390);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_HEADER);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
            cell.setCellStyle(csHead);

            // row 1
            row = sheet.createRow(1);
            row.setHeight((short) 390);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_MONTH);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));
            cell.setCellStyle(csHead);

            //row 4 ตัวเนื้อหา
            int curRow = startWriteExl;

            ResultSet rs = null;

            try {
                rs = opae74DAO.genReportOpae74(report);

                while (rs.next()) {

                    int vCol0 = rs.getInt("no");
                    String vCol1 = rs.getString("HCODE");
                    String vCol2 = rs.getString("HCODENAME");

                    int vCol3 = rs.getInt("N_AE");
                    int vCol4 = rs.getInt("N_74");
                    int vCol5 = rs.getInt("N_TOTAL");

                    double vCol6 = rs.getDouble("CHRG_TOTALAE74_NONREHAP");
                    double vCol7 = rs.getDouble("CHRG_REHAB_INST_74_BKK");
                    double vCol8 = rs.getDouble("CHRG_REHAB_INST_74_NONBKK");
                    double vCol9 = rs.getDouble("CHRG_TOTALAE74");

                    double vCol10 = rs.getDouble("PAID_TOTALAE74_NONREHAP");
                    double vCol11 = rs.getDouble("PAID_REHAB_INST_74_BKK");
                    double vCol12 = rs.getDouble("PAID_REHAB_INST_74_NONBKK");
                    double vCol13 = rs.getDouble("PAID_TOTALAE74");
                    //double vCol14 = rs.getDouble("SUM(CHRG_TOTAL+PAID_TOTAL)");

                    row = sheet.createRow(curRow);
                    row.setHeight((short) 360);
                    cell = row.createCell(0);
                    cell.setCellValue(vCol0);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(1);
                    cell.setCellValue(vCol1);
                    cell.setCellStyle(csStringCenter);

                    cell = row.createCell(2);
                    cell.setCellValue(vCol2);
                    cell.setCellStyle(csStringLeft);

                    cell = row.createCell(3);
                    cell.setCellValue(vCol3);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(4);
                    cell.setCellValue(vCol4);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(5);
                    cell.setCellValue(vCol5);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(6);
                    cell.setCellValue(vCol6);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(7);
                    cell.setCellValue(vCol7);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(8);
                    cell.setCellValue(vCol8);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(9);
                    cell.setCellValue(vCol9);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(10);
                    cell.setCellValue(vCol10);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(11);
                    cell.setCellValue(vCol11);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(12);
                    cell.setCellValue(vCol12);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(13);
                    cell.setCellValue(vCol13);
                    cell.setCellStyle(csDouble2);

//                    cell = row.createCell(14);
//                    cell.setCellValue(vCol14);
//                    cell.setCellStyle(csNum2);
                    curRow++;

                }

            } catch (SQLException e) {
                e.printStackTrace();
                Console.LOG(e.getMessage(), 0);
                programeStatus.setMessage(ConstantMessage.MSG_PROCESS_FAILS + e.toString());
                programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
                programeStatus.setProcessStatus(false);
            } finally {
                rs.close();
                this.opae74DAO.closeConnection();
            }

            row = sheet.createRow(curRow);
            row.setHeight((short) 450);
            cell = row.createCell(0);
            cell.setCellValue("รวม");
            sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 2));
            cell.setCellStyle(csNum4B);

//            cell = row.createCell(1);
//            cell.setCellStyle(csNum4);
//            cell = row.createCell(3);
//            cell.setCellFormula("SUM(C7:C" + (curRow) + ")");
//            cell.setCellStyle(csNum4B);
            cell = row.createCell(3);
            cell.setCellFormula("SUM(D7:D" + (curRow) + ")");
            cell.setCellStyle(csNum4B);

            cell = row.createCell(4);
            cell.setCellFormula("SUM(E7:E" + (curRow) + ")");
            cell.setCellStyle(csNum4B);

            cell = row.createCell(5);
            cell.setCellFormula("SUM(F7:F" + (curRow) + ")");
            cell.setCellStyle(csNum4B);

            cell = row.createCell(6);
            cell.setCellFormula("SUM(G7:G" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(7);
            cell.setCellFormula("SUM(H7:H" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(8);
            cell.setCellFormula("SUM(I7:I" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(9);
            cell.setCellFormula("SUM(J7:J" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(10);
            cell.setCellFormula("SUM(K7:K" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(11);
            cell.setCellFormula("SUM(L7:L" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(12);
            cell.setCellFormula("SUM(M7:M" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(13);
            cell.setCellFormula("SUM(N7:N" + (curRow) + ")");
            cell.setCellStyle(csDouble2B);

//            cell = row.createCell(14);
//            cell.setCellFormula("SUM(O7:O" + (curRow) + ")");
//            cell.setCellStyle(csNum2B);
            Console.LOG("ออกรายงานสรุปเรียบร้อยแล้ว", 1);
            workbookBase.write(out);
            out.close();

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

    public ProgrameStatus opae74_genReportAllFunction(OppReport report) {
        boolean status = false;
        //String serviceCode = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        try {
            if (report.getFor_use() == 0) {
                //serviceCode = report.getServiceCode();
                //*************************************** Detail***************************************
                report.setReportType(0);
                //report.setServiceCode(serviceCode);
                //programeStatus = this.opae74_detail_byDateopd(report);
                programeStatus = this.opae74_detail(report);

                //*************************************** Summary***************************************
                report.setReportType(1);
                //report.setServiceCode(serviceCode);
                programeStatus = this.opae74_sum(report);

                report.setAttribute('R');
                programeStatus = this.opae74_sumRehap(report);
            } else {
                report.setReportType(0);
                programeStatus = this.opae74_detail_byHcode_1sheet1file(report);
            }
        } catch (Exception e) {
            Console.LOG(e.getMessage(), 0);
        }
        return programeStatus;
    }

    public String getOpae74StmpGroupBy(Object obYM, Object obN) {
        String titleTimeStmp = "-";
        if (obYM != "-" && obN != null && obN != "-") {
            titleTimeStmp = opae74DAO.getOpae74StmpDescDistinct(obYM.toString(), obN.toString());
        }
        System.out.print(" STMP_DESC==>>" + titleTimeStmp);
        return titleTimeStmp;
    }

    public Map spliteRecordData(List<ObjRptOpae74> datas) {
        List<ObjRptOpae74> listObject1 = new ArrayList<ObjRptOpae74>();
        List<ObjRptOpae74> listObject2 = new ArrayList<ObjRptOpae74>();
        List<ObjRptOpae74> listObject3 = new ArrayList<ObjRptOpae74>();
        List<ObjRptOpae74> listObject4 = new ArrayList<ObjRptOpae74>();

        Map<Integer, List<ObjRptOpae74>> mapList = new HashMap<Integer, List<ObjRptOpae74>>();

        for (int i = 0; i < datas.size(); i++) {
            if (i <= 65500) {  // limit excell row 65,536 rows by 256 columns
                listObject1.add(datas.get(i));
            } else if (datas.size() > 65500 && datas.size() <= 131000) {
                listObject2.add(datas.get(i));
            } else if (datas.size() > 131000 && datas.size() <= 196500) {
                listObject3.add(datas.get(i));
            } else {
                listObject4.add(datas.get(i));
            }
        }

        mapList.put(1, listObject1);
        mapList.put(2, listObject2);
        mapList.put(3, listObject3);
        mapList.put(4, listObject4);
        return mapList;
    }
}
