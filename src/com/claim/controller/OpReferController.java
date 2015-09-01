package com.claim.controller;

import com.claim.xls.ExcellBaseUtil;
import com.claim.dao.OpReferDAO;
import com.claim.object.HospitalService;
import com.claim.object.OppReport;
import com.claim.support.FunctionUtil;
import com.claim.support.Console;
import com.claim.constants.ConstantMessage;
import com.claim.constants.ConstantVariable;
import com.claim.support.DateUtil;
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

public class OpReferController extends ExcellBaseUtil {

    private OpReferDAO referDAO;
    private FunctionUtil function;

    public OpReferController() {
        referDAO = new OpReferDAO();
        function = new FunctionUtil();
    }

    /*
     *  Single File MutiSheet********************************************************************************
     */
    public ProgrameStatus refer_detailDeduct_singleClinic(OppReport report) {
        FileInputStream file = null;
        FileOutputStream out = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        int col_last = 23;
        int row_start = 6; // index row
        int row_formula_start = row_start + 1;
        int col_txtid_width = col_last + 1;
        try {
            //readTemplate 
            file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"refer_deduct_detail_singleclinic.xls"));

            // style Excell
            HSSFWorkbook wbRefer = new HSSFWorkbook(file);
            this.loadStyle(wbRefer);

            List<HospitalService> listHospital = referDAO.getHospitalService(report);
            if (listHospital != null) {

                System.out.println("ListSize==>>" + listHospital.size());

                if (report.sizeListServiceCode() == 1) {
                    out = new FileOutputStream(report.getPathFile() + ""+File.separator+"refer_deduct_detail_singleClinic_" + report.getYearMonth() + "-" + report.getNo() + "_" + report.getListServiceCode().get(0).getHosCode() + ".xls");
                } else {
                    out = new FileOutputStream(report.getPathFile() + ""+File.separator+"refer_deduct_detail_singleClinic_" + report.getYearMonth() + report.getNo() + ".xls");
                }

                for (int j = 0; j < listHospital.size(); j++) {
                    HospitalService objHospitalService = listHospital.get(j);
                    String EXCELL_HEADER = "รายละเอียดการหักเงินค่าบริการ OP Refer และ A/E ในบัญชีเครือข่ายเดียวกัน(กรณีคลินิกเดี่ยว)";
                    String EXCELL_MONTH = report.getTitle2();
                    String EXCELL_SERVICE = "หน่วยบริการประจำ: " + objHospitalService.getHosHmainName() + " (" + objHospitalService.getHosHmain() + ")";

                    workbookBase.setRepeatingRowsAndColumns(j, 0, col_last, 4, 5);

                    // Start sheet 1 *******************************************************************************                    
                    workbookBase.getSheetAt(0);

                    HSSFSheet sheet = workbookBase.cloneSheet(0);

                    sheet.createFreezePane(4, 6);
                    sheet.setColumnWidth(col_txtid_width, WIDTH_TXID);
                    // Set the rows to repeat from row 4 to 5 on the first sheet.

                    HSSFCell cell = null;
                    HSSFRow row = null;

                    // row 0 Header
                    row = sheet.createRow(0);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_HEADER);
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col_last));
                    cell.setCellStyle(csHead);

                    // row 1 Service
                    row = sheet.createRow(1);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_MONTH);
                    sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, col_last));
                    cell.setCellStyle(csHead);

                    // row 1 Service
                    row = sheet.createRow(2);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_SERVICE);
                    sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, col_last));
                    cell.setCellStyle(csHead);

                    int curRow = row_start;
                    int i = 1;
                    ResultSet rs;
                    try {
                        report.setServiceCode(listHospital.get(j).getHosHmain());
                        rs = referDAO.getReportOpRefer(report);

                        while (rs.next()) {
                            int vNo = rs.getInt("no");
                            String vPID = rs.getString("PID");
                            String vHN = rs.getString("HN");
                            String vHCODE = rs.getString("HCODE");
                            String vPNAME = rs.getString("PNAME");
                            String VHCODENAME = rs.getString("HCODENAME");
                            String vDateopdTh = rs.getString("dateopd_thai_buddha");
                            String vdateopd = rs.getString("dateopd");
                            String vpdxcode = rs.getString("pdxcode");
                            Double vchrg_hc = rs.getDouble("chrg_hc");
                            Double vchrg_197 = rs.getDouble("chrg_197");
                            Double vchrg_drug = rs.getDouble("chrg_drug");
                            Double vchrg_other = rs.getDouble("chrg_other");
                            Double vchrg_total = rs.getDouble("chrg_total");
                            Double vpaid_hc = rs.getDouble("paid_hc");
                            Double vpaid_197 = rs.getDouble("paid_197");
                            Double vpaid_drug = rs.getDouble("paid_drug");
                            Double vpaid_other = rs.getDouble("paid_other");
                            Double vpaid_total = rs.getDouble("paid_total");
                            Double vdeduct_ophc = rs.getDouble("deduct_ophc");
                            Double vdeduct_oprf = rs.getDouble("deduct_oprf");
                            Double vdeduct_hmain = rs.getDouble("deduct_hmain");
                            Double vdeduct_total = rs.getDouble("deduct_total");
                            Double vdeduct_hmain_final = rs.getDouble("deduct_hmain_final");

                            String vremark = rs.getString("remark");
                            String vis_single = rs.getString("is_single");
                            String vstmp = rs.getString("stmp");

                            String vtxid = rs.getString("txid");
                            String vinvoice_no = rs.getString("INVOICE_NO");

                            row = sheet.createRow(curRow);
                            row.setHeight((short) 340);
                            cell = row.createCell(0);
                            cell.setCellValue(i);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(1);
                            cell.setCellValue(function.subStringPid(vPID));
                            cell.setCellStyle(csStringPid);

                            cell = row.createCell(2);
                            cell.setCellValue(vHN);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(3);
                            cell.setCellValue(vPNAME);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(4);
                            cell.setCellValue(vHCODE + " : " + VHCODENAME);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(5);
                            cell.setCellValue(vDateopdTh);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(6);
                            cell.setCellValue(vpdxcode);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(7);
                            cell.setCellValue(vchrg_hc);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(8);
                            cell.setCellValue(vchrg_197);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(9);
                            cell.setCellValue(vchrg_drug);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(10);
                            cell.setCellValue(vchrg_other);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(11);
                            cell.setCellValue(vchrg_total);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(12);
                            cell.setCellValue(vpaid_hc);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(13);
                            cell.setCellValue(vpaid_197);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(14);
                            cell.setCellValue(vpaid_drug);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(15);
                            cell.setCellValue(vpaid_other);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(16);
                            cell.setCellValue(vpaid_total);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(17);
                            cell.setCellValue(vdeduct_ophc);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(18);
                            cell.setCellValue(vdeduct_oprf);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(19);
                            cell.setCellValue(vdeduct_hmain);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(20);
                            cell.setCellValue(vdeduct_total);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(21);
                            cell.setCellValue(vdeduct_hmain_final);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(22);
                            cell.setCellValue(vremark);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(23);
                            cell.setCellValue(vinvoice_no);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(24);
                            cell.setCellValue(vtxid);
                            cell.setCellStyle(csStringtxid);

                            curRow++;
                            i++;

                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Console.LOG(e.getMessage(), 0);
                        programeStatus.setMessage(ConstantMessage.MSG_OPREFER_ERROR + ConstantMessage.MSG_PROCESS_FAILS + e.toString());
                        programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
                        programeStatus.setProcessStatus(false);
                    } finally {
                        referDAO.closeConnection();
                    }

                    /**
                     * footer summary total
                     */
                    row = sheet.createRow(curRow);
                    cell = row.createCell(0);
                    cell.setCellValue("รวม");
                    sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 6));
                    cell.setCellStyle(csNum4B);

                    row.createCell(1).setCellStyle(csNum4B);
                    row.createCell(2).setCellStyle(csNum4B);
                    row.createCell(3).setCellStyle(csNum4B);
                    row.createCell(4).setCellStyle(csNum4B);
                    row.createCell(5).setCellStyle(csNum4B);
                    row.createCell(6).setCellStyle(csNum4B);

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
                    cell.setCellFormula(builderFormulaSum(15, row_formula_start, curRow));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(15);
                    cell.setCellFormula(builderFormulaSum(15, row_formula_start, curRow));
                    cell.setCellStyle(csDouble2B);

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
                    cell.setCellStyle(csHeadTab);

                    workbookBase.setSheetName(j + 1, String.valueOf(j) + "_" + objHospitalService.getHosHmain());
                    Console.LOG(Message.exportSuccess(objHospitalService.getHosHmain()), 1);
                }

                workbookBase.removeSheetAt(0);
                workbookBase.write(out);

                out.close();
                file.close();

                programeStatus.setMessage("ละเอียด_หัก_เดี่ยว" + ConstantMessage.MSG_REPORT_SUCCESS);
                programeStatus.setTitle(ConstantMessage.MSG_REPORT_COMPLETE);
                programeStatus.setProcessStatus(true);
            } else {
                programeStatus.setMessage("ละเอียด_หัก_เดี่ยว" + ConstantMessage.MSG_REPORT_NO_DATA);
                programeStatus.setTitle(ConstantMessage.MSG_NO_DATA);
                programeStatus.setProcessStatus(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
            programeStatus.setMessage("ละเอียด_หัก_เดี่ยว" + ConstantMessage.MSG_OPREFER_ERROR + ConstantMessage.MSG_PROCESS_FAILS + e.toString());
            programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
            programeStatus.setProcessStatus(false);
        }
        return programeStatus;
    }

    public ProgrameStatus refer_detailDeduct_clinicAndHospital(OppReport report) {
        FileInputStream file = null;
        FileOutputStream out = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        int col_last = 23;
        int row_start = 6; // index row
        int row_formula_start = row_start + 1;
        int col_txtid_width = col_last + 1;
        try {
            //readTemplate 
            file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"refer_deduct_detail_clinichospital.xls"));
            System.out.println("report.getsErcode====>" + report.getServiceCode());

            // style Excell
            HSSFWorkbook wbRefer = new HSSFWorkbook(file);
            this.loadStyle(wbRefer);

            List<HospitalService> listHospital = referDAO.getHospitalService(report);

            if (listHospital != null) {

                System.out.println("ListSize==>>" + listHospital.size());

                if (report.sizeListServiceCode() == 1) {
                    out = new FileOutputStream(report.getPathFile() + ""+File.separator+"refer_deduct_detail_clinichospital_" + report.getYearMonth() + "-" + report.getNo() + "_" + report.getListServiceCode().get(0).getHosCode() + ".xls");
                } else {
                    out = new FileOutputStream(report.getPathFile() + ""+File.separator+"refer_deduct_detail_clinichospital_" + report.getYearMonth() + report.getNo() + ".xls");
                }

                for (int j = 0; j < listHospital.size(); j++) {

                    String EXCELL_HEADER = "รายละเอียดการหักเงินค่าบริการ OP Refer และ A/E ในบัญชีเครือข่ายเดียวกัน(กรณีคลินิกเครือและโรงพยาบาล)";
                    String EXCELL_MONTH = report.getTitle2();
                    String EXCELL_SERVICE = "หน่วยบริการประจำ: " + listHospital.get(j).getHosHmainName() + " (" + listHospital.get(j).getHosHmain() + ")";

                    // Start sheet 1 *******************************************************************************
                    workbookBase.getSheetAt(0);
                    HSSFSheet sheet = workbookBase.cloneSheet(0);

                    sheet.createFreezePane(4, 6);
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

                    // row 1 Service
                    row = sheet.createRow(1);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_MONTH);
                    sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, col_last));
                    cell.setCellStyle(csHead);

                    // row 1 Service
                    row = sheet.createRow(2);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_SERVICE);
                    sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, col_last));
                    cell.setCellStyle(csHead);

                    int curRow = 6;
                    int i = 1;
                    ResultSet rs;
                    try {
                        report.setServiceCode(listHospital.get(j).getHosHmain());
                        rs = referDAO.getReportOpRefer(report);
                        while (rs.next()) {
                            int vNo = rs.getInt("no");
                            String vPID = rs.getString("PID");
                            String vHN = rs.getString("HN");
                            String vHCODE = rs.getString("HCODE");
                            String vPNAME = rs.getString("PNAME");
                            String vHCODENAME = rs.getString("HCODENAME");
                            String vDateopdTh = rs.getString("dateopd_thai_buddha");
                            String vdateopd = rs.getString("dateopd");
                            String vpdxcode = rs.getString("pdxcode");
                            Double vchrg_hc = rs.getDouble("chrg_hc");
                            Double vchrg_197 = rs.getDouble("chrg_197");
                            Double vchrg_drug = rs.getDouble("chrg_drug");
                            Double vchrg_other = rs.getDouble("chrg_other");
                            Double vchrg_total = rs.getDouble("chrg_total");
                            Double vpaid_hc = rs.getDouble("paid_hc");
                            Double vpaid_197 = rs.getDouble("paid_197");
                            Double vpaid_drug = rs.getDouble("paid_drug");
                            Double vpaid_other = rs.getDouble("paid_other");
                            Double vpaid_total = rs.getDouble("paid_total");
                            Double vdeduct_ophc = rs.getDouble("deduct_ophc");
                            Double vdeduct_oprf = rs.getDouble("deduct_oprf");
                            Double vdeduct_hmain = rs.getDouble("deduct_hmain");
                            Double vdeduct_total = rs.getDouble("deduct_total");
                            Double vdeduct_hmain_final = rs.getDouble("deduct_hmain_final");

                            String vremark = rs.getString("remark");
                            String vis_single = rs.getString("is_single");
                            String vstmp = rs.getString("stmp");
                            String vtxid = rs.getString("txid");
                            String vinvoice_no = rs.getString("invoice_no");

                            row = sheet.createRow(curRow);
                            row.setHeight((short) 340);
                            cell = row.createCell(0);
                            cell.setCellValue(i);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(1);
                            cell.setCellValue(function.subStringPid(vPID));
                            cell.setCellStyle(csStringPid);

                            cell = row.createCell(2);
                            cell.setCellValue(vHN);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(3);
                            cell.setCellValue(vPNAME);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(4);
                            cell.setCellValue(vHCODE + " : " + vHCODENAME);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(5);
                            cell.setCellValue(vDateopdTh);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(6);
                            cell.setCellValue(vpdxcode);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(7);
                            cell.setCellValue(vchrg_hc);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(8);
                            cell.setCellValue(vchrg_197);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(9);
                            cell.setCellValue(vchrg_drug);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(10);
                            cell.setCellValue(vchrg_other);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(11);
                            cell.setCellValue(vchrg_total);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(12);
                            cell.setCellValue(vpaid_hc);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(13);
                            cell.setCellValue(vpaid_197);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(14);
                            cell.setCellValue(vpaid_drug);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(15);
                            cell.setCellValue(vpaid_other);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(16);
                            cell.setCellValue(vpaid_total);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(17);
                            cell.setCellValue(vdeduct_ophc);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(18);
                            cell.setCellValue(vdeduct_oprf);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(19);
                            cell.setCellValue(vdeduct_hmain);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(20);
                            cell.setCellValue(vdeduct_total);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(21);
                            cell.setCellValue(vdeduct_hmain_final);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(22);
                            cell.setCellValue(vremark);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(23);
                            cell.setCellValue(vinvoice_no);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(24);
                            cell.setCellValue(vtxid);
                            cell.setCellStyle(csStringtxid);

                            curRow++;
                            i++;

                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Console.LOG(e.getMessage(), 0);
                        programeStatus.setMessage(ConstantMessage.MSG_OPREFER_ERROR + ConstantMessage.MSG_PROCESS_FAILS + e.toString());
                        programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
                        programeStatus.setProcessStatus(false);
                    } finally {
                        this.referDAO.closeConnection();
                    }

                    row = sheet.createRow(curRow);
                    cell = row.createCell(0);
                    cell.setCellValue("รวม");
                    sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 6));
                    cell.setCellStyle(csNum4B);

                    row.createCell(1).setCellStyle(csNum4B);
                    row.createCell(2).setCellStyle(csNum4B);
                    row.createCell(3).setCellStyle(csNum4B);
                    row.createCell(4).setCellStyle(csNum4B);
                    row.createCell(5).setCellStyle(csNum4B);
                    row.createCell(6).setCellStyle(csNum4B);

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
                    cell.setCellFormula(builderFormulaSum(15, row_formula_start, curRow));
                    cell.setCellStyle(csDouble2B);

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
                    cell.setCellStyle(csHeadTab);

                    workbookBase.setSheetName(j + 1, listHospital.get(j).getHosHmain());
                    Console.LOG(Message.exportSuccess(listHospital.get(j).getHosHmain()), 1);
                }

                workbookBase.removeSheetAt(0);
                workbookBase.write(out);

                out.close();
                file.close();

                programeStatus.setMessage("ละเอียด_หัก_เครือ" + ConstantMessage.MSG_REPORT_SUCCESS);
                programeStatus.setTitle(ConstantMessage.MSG_REPORT_COMPLETE);
                programeStatus.setProcessStatus(true);
            } else {
                programeStatus.setMessage("ละเอียด_หัก_เครือ" + ConstantMessage.MSG_REPORT_NO_DATA);
                programeStatus.setTitle(ConstantMessage.MSG_NO_DATA);
                programeStatus.setProcessStatus(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
            programeStatus.setMessage("ละเอียด_หัก_เครือ" + ConstantMessage.MSG_OPREFER_ERROR + ConstantMessage.MSG_PROCESS_FAILS + e.toString());
            programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
            programeStatus.setProcessStatus(false);
        }
        return programeStatus;
    }

    public ProgrameStatus refer_sumDeduct_singleClinic(OppReport report) {
        FileInputStream file = null;
        FileOutputStream out = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        int col_last = 15;
        int row_start = 5; // index row
        int row_formula_start = row_start + 1;
        int col_txtid_width = col_last + 1;
        try {
            file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"refer_deduct_sum_singleclinic.xls"));

            out = new FileOutputStream(report.getPathFile() + ""+File.separator+"refer_deduct_sum_singleClinic " + report.getYearMonth() + "-" + report.getNo() + ".xls");

            // style Excell
            HSSFWorkbook wbRefer = new HSSFWorkbook(file);
            this.loadStyle(wbRefer);

            String EXCELL_HEADER;
            EXCELL_HEADER = "สรุปการหักเงินค่าบริการ OP Refer และ A/E ในบัญชีเครือข่ายเดียวกัน  ปีงบประมาณ " + new DateUtil().getBudgeMonthYear_543(report.getBudget_stmp(), ConstantVariable.BUDGET_MONTH) + "  กรณีคลินิกเดี่ยว";
            String EXCELL_MONTH = report.getTitle2();

            HSSFSheet sheet = workbookBase.getSheetAt(0);

            sheet.createFreezePane(3, 5);

            HSSFCell cell = null;
            HSSFRow row = null;

            // row 0
            row = sheet.createRow(0);
            row.setHeight((short) 390);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_HEADER);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col_last));
            cell.setCellStyle(csHead);

            // row 1
            row = sheet.createRow(1);
            row.setHeight((short) 390);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_MONTH);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, col_last));
            cell.setCellStyle(csHead);
            /*
             * detail
             * (row
             * 4)
             */
            int curRow = row_start;
            int i = 1;
            ResultSet rs = null;

            try {
                //report.setYear(String.valueOf((Integer.parseInt(report.getYearFull().toString()) - 543)));
                rs = referDAO.getReportOpRefer(report);

                while (rs.next()) {

                    int vNo = rs.getInt("no");
                    String vhmain = rs.getString("hmain");
                    String vhmainname = rs.getString("hmainname");
                    int vpid_normal_count = rs.getInt("pid_normal_count");
                    int vpid_appeal_count = rs.getInt("pid_appeal_count");
                    int vpid_normal_appeal_total = rs.getInt("pid_normal_appeal_total");

                    double vsum_chrg_hc = rs.getDouble("sum_chrg_hc");
                    double vsum_chrg_197 = rs.getDouble("sum_chrg_197");
                    double vsum_chrg_drug = rs.getDouble("sum_chrg_drug");
                    double vsum_chrg_other = rs.getDouble("sum_chrg_other");
                    double vsum_chrg_total = rs.getDouble("sum_chrg_total");

                    double vsum_deduct_ophc = rs.getDouble("sum_deduct_ophc");
                    double vsum_deduct_oprf = rs.getDouble("sum_deduct_oprf");
                    double vsum_deduct_hmain = rs.getDouble("sum_deduct_hmain");
                    double vsum_deduct_total = rs.getDouble("sum_deduct_total");
                    double vdeduct_hmain_final = rs.getDouble("deduct_hmain_final");

                    row = sheet.createRow(curRow);
                    row.setHeight((short) 340);
                    cell = row.createCell(0);
                    cell.setCellValue(vNo);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(1);
                    cell.setCellValue(vhmain);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(2);
                    cell.setCellValue(vhmainname);
                    cell.setCellStyle(csStringLeft);

                    cell = row.createCell(3);
                    cell.setCellValue(vpid_normal_count);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(4);
                    cell.setCellValue(vpid_appeal_count);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(5);
                    cell.setCellValue(vpid_normal_appeal_total);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(6);
                    cell.setCellValue(vsum_chrg_hc);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(7);
                    cell.setCellValue(vsum_chrg_197);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(8);
                    cell.setCellValue(vsum_chrg_drug);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(9);
                    cell.setCellValue(vsum_chrg_other);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(10);
                    cell.setCellValue(vsum_chrg_total);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(11);
                    cell.setCellValue(vsum_deduct_ophc);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(12);
                    cell.setCellValue(vsum_deduct_oprf);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(13);
                    cell.setCellValue(vsum_deduct_hmain);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(14);
                    cell.setCellValue(vsum_deduct_total);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(15);
                    cell.setCellValue(vdeduct_hmain_final);
                    cell.setCellStyle(csDouble2);

                    curRow++;
                    i++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Console.LOG(e.getMessage(), 0);
                programeStatus.setMessage("สรุป_หัก_เดี่ยว" + ConstantMessage.MSG_OPREFER_ERROR + ConstantMessage.MSG_PROCESS_FAILS + e.toString());
                programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
                programeStatus.setProcessStatus(false);
            } finally {
                this.referDAO.closeConnection();
            }

            /**
             * footer summary total
             */
            row = sheet.createRow(curRow);
            cell = row.createCell(0);
            cell.setCellValue("รวม");
            sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 2));
            cell.setCellStyle(csNum4B);

            row.createCell(1).setCellStyle(csNum4B);
            row.createCell(2).setCellStyle(csNum4B);

            cell = row.createCell(3);
            cell.setCellFormula(builderFormulaSum(3, row_formula_start, curRow));
            cell.setCellStyle(csNum4B);

            cell = row.createCell(4);
            cell.setCellFormula(builderFormulaSum(4, row_formula_start, curRow));
            cell.setCellStyle(csNum4B);

            cell = row.createCell(5);
            cell.setCellFormula(builderFormulaSum(5, row_formula_start, curRow));
            cell.setCellStyle(csNum4B);

            cell = row.createCell(6);
            cell.setCellFormula(builderFormulaSum(6, row_formula_start, curRow));
            cell.setCellStyle(csDouble2B);

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
            cell.setCellFormula(builderFormulaSum(15, row_formula_start, curRow));
            cell.setCellStyle(csDouble2B);

            // BAHTTEXT(P58)
           /* row = sheet.createRow(curRow+1);
             cell = row.createCell(15);
             cell.setCellFormula("BAHTTEXT(P" + curRow+ ")");
             cell.setCellStyle(csNum4B);*/
            workbookBase.write(out);
            out.close();

            Console.LOG(ConstantMessage.MSG_REPORT_SUCCESS, 1);
            programeStatus.setMessage("สรุป_หัก_เดี่ยว" + ConstantMessage.MSG_REPORT_SUCCESS);
            programeStatus.setTitle(ConstantMessage.MSG_REPORT_COMPLETE);
            programeStatus.setProcessStatus(true);
        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
            programeStatus.setMessage("สรุป_หัก_เดี่ยว" + ConstantMessage.MSG_OPREFER_ERROR + ConstantMessage.MSG_PROCESS_FAILS + e.toString());
            programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
            programeStatus.setProcessStatus(false);
        }
        return programeStatus;
    }

    public ProgrameStatus refer_sumDeduct_clinicAndHospital(OppReport report) {
        FileInputStream file = null;
        FileOutputStream out = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        try {
            file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"refer_deduct_sum_clinichospital.xls"));

            out = new FileOutputStream(report.getPathFile() + ""+File.separator+"refer_deduct_sum_clinichospital " + report.getYearMonth() + "-" + report.getNo() + ".xls");
            /**
             * style exell
             */
            HSSFWorkbook wbRefer = new HSSFWorkbook(file);
            this.loadStyle(wbRefer);

            String EXCELL_HEADER = "สรุปการหักเงินค่าบริการ OP Refer และ A/E ในบัญชีเครือข่ายเดียวกัน ปีงบประมาณ " + new DateUtil().getBudgeMonthYear_543(report.getBudget_stmp(),ConstantVariable.BUDGET_MONTH)+ "";
            String EXCELL_MONTH = report.getTitle2();

            HSSFSheet sheet = workbookBase.getSheetAt(0);

            sheet.createFreezePane(3, 5);
            /**
             * sheet summary
             */
            HSSFCell cell = null;
            HSSFRow row = null;

            // row 0
            row = sheet.createRow(0);
            row.setHeight((short) 390);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_HEADER);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 15));
            cell.setCellStyle(csHead);

            // row 1
            row = sheet.createRow(1);
            row.setHeight((short) 390);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_MONTH);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 15));
            cell.setCellStyle(csHead);

            int curRow = 5;
            int i = 1;

            ResultSet rs = null;

            try {
                rs = referDAO.getReportOpRefer(report);

                System.out.println("SumDeduct ClinicHospital Is Not Null");
                while (rs.next()) {
                    int vNo = rs.getInt("no");
                    String vhmain = rs.getString("hmain");
                    String vhmainname = rs.getString("hmainname");
                    int vpid_normal_count = rs.getInt("pid_normal_count");
                    int vpid_appeal_count = rs.getInt("pid_appeal_count");
                    int vpid_normal_appeal_total = rs.getInt("pid_normal_appeal_total");

                    double vsum_chrg_hc = rs.getDouble("sum_chrg_hc");
                    double vsum_chrg_197 = rs.getDouble("sum_chrg_197");
                    double vsum_chrg_drug = rs.getDouble("sum_chrg_drug");
                    double vsum_chrg_other = rs.getDouble("sum_chrg_other");
                    double vsum_chrg_total = rs.getDouble("sum_chrg_total");

                    double vsum_deduct_ophc = rs.getDouble("sum_deduct_ophc");
                    double vsum_deduct_oprf = rs.getDouble("sum_deduct_oprf");
                    double vsum_deduct_hmain = rs.getDouble("sum_deduct_hmain");
                    double vsum_deduct_total = rs.getDouble("sum_deduct_total");
                    double vdeduct_hmain_final = rs.getDouble("deduct_hmain_final");

                    row = sheet.createRow(curRow);
                    row.setHeight((short) 340);
                    cell = row.createCell(0);
                    cell.setCellValue(vNo);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(1);
                    cell.setCellValue(vhmain);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(2);
                    cell.setCellValue(vhmainname);
                    cell.setCellStyle(csString2);

                    cell = row.createCell(3);
                    cell.setCellValue(vpid_normal_count);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(4);
                    cell.setCellValue(vpid_appeal_count);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(5);
                    cell.setCellValue(vpid_normal_appeal_total);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(6);
                    cell.setCellValue(vsum_chrg_hc);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(7);
                    cell.setCellValue(vsum_chrg_197);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(8);
                    cell.setCellValue(vsum_chrg_drug);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(9);
                    cell.setCellValue(vsum_chrg_other);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(10);
                    cell.setCellValue(vsum_chrg_total);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(11);
                    cell.setCellValue(vsum_deduct_ophc);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(12);
                    cell.setCellValue(vsum_deduct_oprf);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(13);
                    cell.setCellValue(vsum_deduct_hmain);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(14);
                    cell.setCellValue(vsum_deduct_total);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(15);
                    cell.setCellValue(vdeduct_hmain_final);
                    cell.setCellStyle(csDouble2);

                    curRow++;
                    i++;
                }

            } catch (SQLException e) {
                e.printStackTrace();
                Console.LOG(e.getMessage(), 0);
                programeStatus.setMessage("สรุป_หัก_เครือ" + ConstantMessage.MSG_OPREFER_ERROR + ConstantMessage.MSG_PROCESS_FAILS + e.toString());
                programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
                programeStatus.setProcessStatus(false);
            } finally {
                this.referDAO.closeConnection();
            }
            /**
             * footer summary total
             */
            row = sheet.createRow(curRow);
            cell = row.createCell(0);
            cell.setCellValue("รวม");
            sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 2));
            cell.setCellStyle(csNum4B);

            cell = row.createCell(1);
            cell.setCellStyle(csNum4B);

            cell = row.createCell(2);
            cell.setCellStyle(csNum4B);

            cell = row.createCell(3);
            cell.setCellFormula("SUM(D6:D" + (curRow) + ")");
            cell.setCellStyle(csNum4B);

            cell = row.createCell(4);
            cell.setCellFormula("SUM(E6:E" + (curRow) + ")");
            cell.setCellStyle(csNum4B);

            cell = row.createCell(5);
            cell.setCellFormula("SUM(F6:F" + (curRow) + ")");
            cell.setCellStyle(csNum4B);

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

            workbookBase.write(out);
            out.close();
            Console.LOG("ออกรายงานสรุป เครือโรงพยาบาล เรียบร้อยแล้ว", 1);

            programeStatus.setMessage(ConstantMessage.MSG_REPORT_SUCCESS);
            programeStatus.setTitle(ConstantMessage.MSG_REPORT_COMPLETE);
            programeStatus.setProcessStatus(true);
        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
            programeStatus.setMessage(ConstantMessage.MSG_OPREFER_ERROR + ConstantMessage.MSG_PROCESS_FAILS + e.toString());
            programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
            programeStatus.setProcessStatus(false);
        }
        return programeStatus;
    }

    public ProgrameStatus refer_detailDeduct(OppReport report) {
        FileInputStream file = null;
        FileOutputStream out = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        List<HospitalService> listHospital = new ArrayList<HospitalService>();
        int col_last = 23;
        int row_start = 6; // index row
        int row_formula_start = row_start + 1;
        int col_txtid_width = col_last + 1;
        try {
            //readTemplate 
            file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"refer_deduct_detail.xls"));

            // style Excell
            HSSFWorkbook wbRefer = new HSSFWorkbook(file);
            this.loadStyle(wbRefer);

            listHospital = referDAO.getHospitalService(report);

            if (listHospital != null) {
                System.out.println("ListSize==>>" + listHospital.size());

                if (report.sizeListServiceCode() == 1) {
                    out = new FileOutputStream(report.getPathFile() + ""+File.separator+"refer_deduct_detail_" + report.getYearMonth() + "-" + report.getNo() + "_" + report.getListServiceCode().get(0).getHosCode() + ".xls");
                } else {
                    out = new FileOutputStream(report.getPathFile() + ""+File.separator+"refer_deduct_detail_" + report.getYearMonth() + report.getNo() + ".xls");
                }

                for (int j = 0; j < listHospital.size(); j++) {
                    HospitalService objHospital = listHospital.get(j);

                    String EXCELL_HEADER = "รายละเอียดการหักเงินค่าบริการ OP Refer และ A/E ในบัญชีเครือข่ายเดียวกัน";
                    String EXCELL_MONTH = report.getTitle2();
                    String EXCELL_SERVICE = "หน่วยบริการประจำ: " + objHospital.getHosHmainName() + " (" + objHospital.getHosHmain() + ")";

                    // Start sheet 1 *******************************************************************************
                    workbookBase.getSheetAt(0);
                    HSSFSheet sheet = workbookBase.cloneSheet(0);

                    sheet.createFreezePane(4, 6);
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

                    // row 1 Service
                    row = sheet.createRow(1);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_MONTH);
                    sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, col_last));
                    cell.setCellStyle(csHead);

                    // row 1 Service
                    row = sheet.createRow(2);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_SERVICE);
                    sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, col_last));
                    cell.setCellStyle(csHead);

                    int curRow = 6;
                    int i = 1;
                    ResultSet rs;
                    try {
                        report.setServiceCode(listHospital.get(j).getHosHmain());
                        rs = referDAO.getReportOpRefer(report);
                        while (rs.next()) {
                            int vNo = rs.getInt("no");
                            String vPID = rs.getString("PID");
                            String vHN = rs.getString("HN");
                            String vHCODE = rs.getString("HCODE");
                            String vPNAME = rs.getString("PNAME");
                            String vHCODENAME = rs.getString("HCODENAME");
                            String vDateopdTh = rs.getString("dateopd_thai_buddha");
                            String vdateopd = rs.getString("dateopd");
                            String vpdxcode = rs.getString("pdxcode");
                            Double vchrg_hc = rs.getDouble("chrg_hc");
                            Double vchrg_197 = rs.getDouble("chrg_197");
                            Double vchrg_drug = rs.getDouble("chrg_drug");
                            Double vchrg_other = rs.getDouble("chrg_other");
                            Double vchrg_total = rs.getDouble("chrg_total");
                            Double vpaid_hc = rs.getDouble("paid_hc");
                            Double vpaid_197 = rs.getDouble("paid_197");
                            Double vpaid_drug = rs.getDouble("paid_drug");
                            Double vpaid_other = rs.getDouble("paid_other");
                            Double vpaid_total = rs.getDouble("paid_total");
                            Double vdeduct_ophc = rs.getDouble("deduct_ophc");
                            Double vdeduct_oprf = rs.getDouble("deduct_oprf");
                            Double vdeduct_hmain = rs.getDouble("deduct_hmain");
                            Double vdeduct_total = rs.getDouble("deduct_total");
                            Double vdeduct_hmain_final = rs.getDouble("deduct_hmain_final");

                            String vremark = rs.getString("remark");
                            String vis_single = rs.getString("is_single");
                            String vstmp = rs.getString("stmp");
                            String vtxid = rs.getString("txid");
                            String vinvoice_no = rs.getString("invoice_no");

                            row = sheet.createRow(curRow);
                            row.setHeight((short) 340);
                            cell = row.createCell(0);
                            cell.setCellValue(i);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(1);
                            cell.setCellValue(function.subStringPid(vPID));
                            cell.setCellStyle(csStringPid);

                            cell = row.createCell(2);
                            cell.setCellValue(vHN);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(3);
                            cell.setCellValue(vPNAME);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(4);
                            cell.setCellValue(vHCODE + " : " + vHCODENAME);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(5);
                            cell.setCellValue(vDateopdTh);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(6);
                            cell.setCellValue(vpdxcode);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(7);
                            cell.setCellValue(vchrg_hc);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(8);
                            cell.setCellValue(vchrg_197);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(9);
                            cell.setCellValue(vchrg_drug);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(10);
                            cell.setCellValue(vchrg_other);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(11);
                            cell.setCellValue(vchrg_total);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(12);
                            cell.setCellValue(vpaid_hc);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(13);
                            cell.setCellValue(vpaid_197);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(14);
                            cell.setCellValue(vpaid_drug);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(15);
                            cell.setCellValue(vpaid_other);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(16);
                            cell.setCellValue(vpaid_total);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(17);
                            cell.setCellValue(vdeduct_ophc);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(18);
                            cell.setCellValue(vdeduct_oprf);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(19);
                            cell.setCellValue(vdeduct_hmain);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(20);
                            cell.setCellValue(vdeduct_total);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(21);
                            cell.setCellValue(vdeduct_hmain_final);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(22);
                            cell.setCellValue(vremark);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(23);
                            cell.setCellValue(vinvoice_no);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(24);
                            cell.setCellValue(vtxid);
                            cell.setCellStyle(csStringtxid);

                            curRow++;
                            i++;

                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Console.LOG(e.getMessage(), 0);
                        programeStatus.setMessage("ละเอียด_หัก_เดี่ยว+เครือ" + ConstantMessage.MSG_OPREFER_ERROR + ConstantMessage.MSG_PROCESS_FAILS + e.toString());
                        programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
                        programeStatus.setProcessStatus(false);
                    } finally {
                        this.referDAO.closeConnection();
                    }

                    row = sheet.createRow(curRow);
                    cell = row.createCell(0);
                    cell.setCellValue("รวม");
                    sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 6));
                    cell.setCellStyle(csNum4B);

                    row.createCell(1).setCellStyle(csNum4B);
                    row.createCell(2).setCellStyle(csNum4B);
                    row.createCell(3).setCellStyle(csNum4B);
                    row.createCell(4).setCellStyle(csNum4B);
                    row.createCell(5).setCellStyle(csNum4B);
                    row.createCell(6).setCellStyle(csNum4B);

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
                    cell.setCellFormula(builderFormulaSum(15, row_formula_start, curRow));
                    cell.setCellStyle(csDouble2B);

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
                    cell.setCellStyle(csHeadTab);
                    System.out.println("listHospital.get(j) : " + listHospital.get(j).toString());

                    workbookBase.setSheetName((j + 1), String.valueOf(j) + "_" + listHospital.get(j).getHosHmain());
                    Console.LOG(Message.exportSuccess(listHospital.get(j).getHosHmain()), 1);
                }
                workbookBase.removeSheetAt(0);
                workbookBase.write(out);

                out.close();
                file.close();

                programeStatus.setMessage(ConstantMessage.MSG_REPORT_SUCCESS);
                programeStatus.setTitle(ConstantMessage.MSG_REPORT_COMPLETE);
                programeStatus.setProcessStatus(true);
            } else {
                programeStatus.setMessage("ละเอียด_หัก_เดี่ยว+เครือ" + ConstantMessage.MSG_REPORT_NO_DATA);
                programeStatus.setTitle(ConstantMessage.MSG_NO_DATA);
                programeStatus.setProcessStatus(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
            programeStatus.setMessage("ละเอียด_หัก_เดี่ยว+เครือ" + ConstantMessage.MSG_OPREFER_ERROR + ConstantMessage.MSG_PROCESS_FAILS + e.toString());
            programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
            programeStatus.setProcessStatus(false);
        }
        return programeStatus;
    }

    public ProgrameStatus refer_sumDeduct(OppReport report) {
        FileInputStream file = null;
        FileOutputStream out = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        try {
            file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"refer_deduct_sum.xls"));

            out = new FileOutputStream(report.getPathFile() + ""+File.separator+"refer_deduct_sum " + report.getYearMonth() + "-" + report.getNo() + ".xls");
            // style Excell
            HSSFWorkbook wbRefer = new HSSFWorkbook(file);
            this.loadStyle(wbRefer);

            String EXCELL_HEADER = "สรุปการหักเงินค่าบริการ OP Refer และ A/E ในบัญชีเครือข่ายเดียวกัน  ปีงบประมาณ " + new DateUtil().getBudgeMonthYear_543(report.getBudget_stmp(),ConstantVariable.BUDGET_MONTH)+ " ";
            String EXCELL_MONTH = report.getTitle2();

            HSSFSheet sheet = workbookBase.getSheetAt(0);

            sheet.createFreezePane(3, 5);

            HSSFCell cell = null;
            HSSFRow row = null;

            // row 0
            row = sheet.createRow(0);
            row.setHeight((short) 390);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_HEADER);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 15));
            cell.setCellStyle(csHead);

            // row 1
            row = sheet.createRow(1);
            row.setHeight((short) 390);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_MONTH);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 15));
            cell.setCellStyle(csHead);

            /**
             * data detail (row 4)
             */
            int curRow = 5;
            int i = 1;
            ResultSet rs = null;

            try {
                rs = referDAO.getReportOpRefer(report);

                while (rs.next()) {

                    int vNo = rs.getInt("no");
                    String vhmain = rs.getString("hmain");
                    String vhmainname = rs.getString("hmainname");
                    int vpid_normal_count = rs.getInt("pid_normal_count");
                    int vpid_appeal_count = rs.getInt("pid_appeal_count");
                    int vpid_normal_appeal_total = rs.getInt("pid_normal_appeal_total");

                    double vsum_chrg_hc = rs.getDouble("sum_chrg_hc");
                    double vsum_chrg_197 = rs.getDouble("sum_chrg_197");
                    double vsum_chrg_drug = rs.getDouble("sum_chrg_drug");
                    double vsum_chrg_other = rs.getDouble("sum_chrg_other");
                    double vsum_chrg_total = rs.getDouble("sum_chrg_total");

                    double vsum_deduct_ophc = rs.getDouble("sum_deduct_ophc");
                    double vsum_deduct_oprf = rs.getDouble("sum_deduct_oprf");
                    double vsum_deduct_hmain = rs.getDouble("sum_deduct_hmain");
                    double vsum_deduct_total = rs.getDouble("sum_deduct_total");
                    double vdeduct_hmain_final = rs.getDouble("deduct_hmain_final");

                    row = sheet.createRow(curRow);
                    row.setHeight((short) 340);
                    cell = row.createCell(0);
                    cell.setCellValue(vNo);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(1);
                    cell.setCellValue(vhmain);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(2);
                    cell.setCellValue(vhmainname);
                    cell.setCellStyle(csString2);

                    cell = row.createCell(3);
                    cell.setCellValue(vpid_normal_count);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(4);
                    cell.setCellValue(vpid_appeal_count);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(5);
                    cell.setCellValue(vpid_normal_appeal_total);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(6);
                    cell.setCellValue(vsum_chrg_hc);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(7);
                    cell.setCellValue(vsum_chrg_197);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(8);
                    cell.setCellValue(vsum_chrg_drug);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(9);
                    cell.setCellValue(vsum_chrg_other);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(10);
                    cell.setCellValue(vsum_chrg_total);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(11);
                    cell.setCellValue(vsum_deduct_ophc);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(12);
                    cell.setCellValue(vsum_deduct_oprf);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(13);
                    cell.setCellValue(vsum_deduct_hmain);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(14);
                    cell.setCellValue(vsum_deduct_total);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(15);
                    cell.setCellValue(vdeduct_hmain_final);
                    cell.setCellStyle(csDouble2);

                    curRow++;
                    i++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Console.LOG(e.getMessage(), 0);
                programeStatus.setMessage("สรุป_หัก_เดี่ยว+เครือ" + ConstantMessage.MSG_OPREFER_ERROR + ConstantMessage.MSG_PROCESS_FAILS + e.toString());
                programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
                programeStatus.setProcessStatus(false);
            } finally {
                this.referDAO.closeConnection();
            }

            row = sheet.createRow(curRow);
            cell = row.createCell(0);
            cell.setCellValue("รวม");
            sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 2));
            cell.setCellStyle(csNum4B);

            cell = row.createCell(1);
            cell.setCellStyle(csNum4B);

            cell = row.createCell(2);
            cell.setCellStyle(csNum4B);

            cell = row.createCell(3);
            cell.setCellFormula("SUM(D6:D" + (curRow) + ")");
            cell.setCellStyle(csNum4B);

            cell = row.createCell(4);
            cell.setCellFormula("SUM(E6:E" + (curRow) + ")");
            cell.setCellStyle(csNum4B);

            cell = row.createCell(5);
            cell.setCellFormula("SUM(F6:F" + (curRow) + ")");
            cell.setCellStyle(csNum4B);

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

            workbookBase.write(out);
            out.close();
            Console.LOG("ออกรายงาน สรุปหัก เรียบร้อยแล้ว", 1);

            programeStatus.setMessage("สรุป_หัก_เดี่ยว+เครือ" + ConstantMessage.MSG_REPORT_SUCCESS);
            programeStatus.setTitle(ConstantMessage.MSG_REPORT_COMPLETE);
            programeStatus.setProcessStatus(true);

        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
            programeStatus.setMessage("สรุป_หัก_เดี่ยว+เครือ " + ConstantMessage.MSG_OPREFER_ERROR + ConstantMessage.MSG_PROCESS_FAILS + e.toString());
            programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
            programeStatus.setProcessStatus(false);
        }
        return programeStatus;
    }

    public ProgrameStatus refer_sumPayment(OppReport report) {
        FileOutputStream out;
        FileInputStream file = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        try {
            file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"refer_pay_sum.xls"));

            out = new FileOutputStream(report.getPathFile() + ""+File.separator+"refer_pay_sum" + report.getYearMonth() + "-" + report.getNo() + ".xls");
            /**
             * style exell
             */
            HSSFWorkbook wbRefer = new HSSFWorkbook(file);
            this.loadStyle(wbRefer);

            String EXCELL_HEADER = "สรุปการจ่ายชดเชยค่าบริการ OP Refer และ A/E ในบัญชีเครือข่ายเดียวกัน ปีงบประมาณ " + new DateUtil().getBudgeMonthYear_543(report.getBudget_stmp(),ConstantVariable.BUDGET_MONTH)+ "";
            String EXCELL_MONTH = report.getTitle2();

            HSSFSheet sheet = workbookBase.getSheetAt(0);

            // FreezePane
            sheet.createFreezePane(3, 5);

            HSSFCell cell = null;
            HSSFRow row = null;

            // row 0
            row = sheet.createRow(0);
            row.setHeight((short) 390);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_HEADER);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 15));
            cell.setCellStyle(csHead);

            // row 1
            row = sheet.createRow(1);
            row.setHeight((short) 390);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_MONTH);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 15));
            cell.setCellStyle(csHead);

            /**
             * data detail (row 4)
             */
            int curRow = 5;
            int i = 1;
            ResultSet rs = null;

            try {
                rs = referDAO.getReportOpRefer(report);

                while (rs.next()) {
                    int vNo = rs.getInt("no");
                    String vHCODE = rs.getString("HCODE");
                    String vhname = rs.getString("hcodename");
                    int vpid_normal_count = rs.getInt("pid_normal_count");
                    int vpid_appeal_count = rs.getInt("pid_appeal_count");
                    int vpid_normal_appeal_total = rs.getInt("pid_normal_appeal_total");

                    double vsum_chrg_hc = rs.getDouble("sum_chrg_hc");
                    double vsum_chrg_197 = rs.getDouble("sum_chrg_197");
                    double vsum_chrg_drug = rs.getDouble("sum_chrg_drug");
                    double vsum_chrg_other = rs.getDouble("sum_chrg_other");
                    double vsum_chrg_total = rs.getDouble("sum_chrg_total");

                    double vsum_paid_hc = rs.getDouble("sum_paid_hc");
                    double vsum_paid_197 = rs.getDouble("sum_paid_197");
                    double vsum_paid_drug = rs.getDouble("sum_paid_drug");
                    double vsum_paid_other = rs.getDouble("sum_paid_other");
                    double vsum_paid_total = rs.getDouble("sum_paid_total");
                    double vsum_paid_final = rs.getDouble("sum_paid_final");

                    row = sheet.createRow(curRow);
                    row.setHeight((short) 340);
                    cell = row.createCell(0);
                    cell.setCellValue(vNo);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(1);
                    cell.setCellValue(vHCODE);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(2);
                    cell.setCellValue(vhname);
                    cell.setCellStyle(csString2);

                    cell = row.createCell(3);
                    cell.setCellValue(vpid_normal_count);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(4);
                    cell.setCellValue(vpid_appeal_count);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(5);
                    cell.setCellValue(vpid_normal_appeal_total);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(6);
                    cell.setCellValue(vsum_chrg_hc);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(7);
                    cell.setCellValue(vsum_chrg_197);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(8);
                    cell.setCellValue(vsum_chrg_drug);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(9);
                    cell.setCellValue(vsum_chrg_other);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(10);
                    cell.setCellValue(vsum_chrg_total);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(11);
                    cell.setCellValue(vsum_paid_hc);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(12);
                    cell.setCellValue(vsum_paid_197);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(13);
                    cell.setCellValue(vsum_paid_drug);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(14);
                    cell.setCellValue(vsum_paid_other);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(15);
                    cell.setCellValue(vsum_paid_total);
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(16);
                    cell.setCellValue(vsum_paid_final);
                    cell.setCellStyle(csDouble2);

                    curRow++;
                    i++;
                }

            } catch (SQLException e) {
                e.printStackTrace();
                Console.LOG(e.getMessage(), 0);
                programeStatus.setMessage("สรุป_จ่าย_เดี่ยว+เครือ" + ConstantMessage.MSG_OPREFER_ERROR + ConstantMessage.MSG_PROCESS_FAILS + e.toString());
                programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
                programeStatus.setProcessStatus(false);
            } finally {
                this.referDAO.closeConnection();
            }

            row = sheet.createRow(curRow);
            cell = row.createCell(0);
            cell.setCellValue("รวม");
            sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 2));
            cell.setCellStyle(csNum4B);

            cell = row.createCell(1);
            cell.setCellStyle(csNum4B);

            cell = row.createCell(2);
            cell.setCellStyle(csNum4B);

            cell = row.createCell(3);
            cell.setCellFormula("SUM(D6:D" + (curRow) + ")");
            cell.setCellStyle(csNum4B);

            cell = row.createCell(4);
            cell.setCellFormula("SUM(E6:E" + (curRow) + ")");
            cell.setCellStyle(csNum4B);

            cell = row.createCell(5);
            cell.setCellFormula("SUM(F6:F" + (curRow) + ")");
            cell.setCellStyle(csNum4B);

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

            workbookBase.write(out);
            out.close();
            Console.LOG(ConstantMessage.MSG_REPORT_SUCCESS, 1);

            programeStatus.setMessage("สรุป_จ่าย_เดี่ยว+เครือ" + ConstantMessage.MSG_REPORT_SUCCESS);
            programeStatus.setTitle(ConstantMessage.MSG_REPORT_COMPLETE);
            programeStatus.setProcessStatus(true);

        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
            programeStatus.setMessage("สรุป_จ่าย_เดี่ยว+เครือ " + ConstantMessage.MSG_OPREFER_ERROR + ConstantMessage.MSG_PROCESS_FAILS + e.toString());
            programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
            programeStatus.setProcessStatus(false);
        }
        return programeStatus;
    }

    public ProgrameStatus refer_detailPayment(OppReport report) {
        FileOutputStream out;
        FileInputStream file = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        int col_last = 19;
        int row_start = 6; // index row
        int row_formula_start = row_start + 1;
        int col_txtid_width = col_last + 1;
        try {
            //readTemplate 
            file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"refer_pay_detail.xls"));

            // style Excell
            HSSFWorkbook wbRefer = new HSSFWorkbook(file);
            this.loadStyle(wbRefer);

            List<HospitalService> listHospital = referDAO.getHospitalService(report);

            System.out.println("ListSize==>>" + listHospital.size());

            if (listHospital != null) {

                if (report.sizeListServiceCode() == 1) {
                    out = new FileOutputStream(report.getPathFile() + ""+File.separator+"refer_pay_detai_" + report.getYearMonth() + "-" + report.getNo() + "_" + report.getListServiceCode().get(0).getHosCode() + ".xls");
                } else {
                    out = new FileOutputStream(report.getPathFile() + ""+File.separator+"refer_pay_detail " + report.getYearMonth() + "-" + report.getNo() + ".xls");
                }

                for (int j = 0; j < listHospital.size(); j++) {
                    HospitalService objHospital = listHospital.get(j);

                    String EXCELL_HEADER = "รายละเอียดการจ่ายชดเชยการจ่ายชดเชยค่าบริการ OP Refer และ A/E ในบัญชีเครือข่ายเดียวกัน ปีงบประมาณ " + new DateUtil().getBudgeMonthYear_543(report.getBudget_stmp(),ConstantVariable.BUDGET_MONTH)+ "";
                    String EXCELL_MONTH = report.getTitle2();
                    String EXCELL_SERVICE = "หน่วยบริการที่รักษา: " + objHospital.getHosCodeName() + " (" + objHospital.getHosCode() + ")";

                    // Start sheet 1 *******************************************************************************
                    workbookBase.getSheetAt(0);
                    HSSFSheet sheet = workbookBase.cloneSheet(0);

                    // FreezePane
                    sheet.createFreezePane(4, 6);
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

                    // row 1 Service
                    row = sheet.createRow(1);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_MONTH);
                    sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, col_last));
                    cell.setCellStyle(csHead);

                    // row 1 Service
                    row = sheet.createRow(2);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_SERVICE);
                    sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, col_last));
                    cell.setCellStyle(csHead);

                    int curRow = row_start;
                    int i = 1;
                    ResultSet rs;
                    try {
                        report.setServiceCode(listHospital.get(j).getHosCode());
                        rs = referDAO.getReportOpRefer(report);
                        while (rs.next()) {

                            int vNo = rs.getInt("no");
                            String vPID = rs.getString("PID");
                            String vHN = rs.getString("HN");
                            String vhmainop_n = rs.getString("hmain");
                            String vhmainop_name = rs.getString("hmainname");
                            String vFLNAME = rs.getString("PNAME");
                            String vHNAME = rs.getString("hcodeNAME");
                            String vDATEOPD = rs.getString("DATEOPD");
                            String vdateopdTh = rs.getString("dateopd_thai_buddha");
                            String vPDXCODE = rs.getString("PDXCODE");

                            double vCHRG_HC = rs.getDouble("CHRG_HC");
                            double vCHRG_197 = rs.getDouble("CHRG_197");
                            double vCHRG_DRUG = rs.getDouble("CHRG_DRUG");
                            double vCHRG_OTHER = rs.getDouble("CHRG_OTHER");
                            double vCHRG_TOTAL = rs.getDouble("CHRG_TOTAL");
                            double vPAID_HC = rs.getDouble("PAID_HC");
                            double vPAID_197 = rs.getDouble("PAID_197");
                            double vPAID_DRUG = rs.getDouble("PAID_DRUG");
                            double vPAID_OTHER = rs.getDouble("PAID_OTHER");
                            double vPAID_TOTAL = rs.getDouble("PAID_TOTAL");

                            String vREMARK = rs.getString("REMARK");
                            String vis_single = rs.getString("is_single");
                            String vSTMP = rs.getString("STMP");

                            String vTxid = rs.getString("txid");
                            String vInvoice_no = rs.getString("invoice_no");

                            row = sheet.createRow(curRow);
                            row.setHeight((short) 340);
                            cell = row.createCell(0);
                            cell.setCellValue(i);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(1);
                            cell.setCellValue(function.subStringPid(vPID));
                            cell.setCellStyle(csStringPid);

                            cell = row.createCell(2);
                            cell.setCellValue(vHN);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(3);
                            cell.setCellValue(vFLNAME);
                            cell.setCellStyle(csString2);

                            cell = row.createCell(4);
                            cell.setCellValue(vhmainop_n + " : " + vhmainop_name);
                            cell.setCellStyle(csString2);

                            cell = row.createCell(5);
                            cell.setCellValue(vdateopdTh);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(6);
                            cell.setCellValue(vPDXCODE);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(7);
                            cell.setCellValue(vCHRG_HC);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(8);
                            cell.setCellValue(vCHRG_197);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(9);
                            cell.setCellValue(vCHRG_DRUG);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(10);
                            cell.setCellValue(vCHRG_OTHER);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(11);
                            cell.setCellValue(vCHRG_TOTAL);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(12);
                            cell.setCellValue(vPAID_HC);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(13);
                            cell.setCellValue(vPAID_197);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(14);
                            cell.setCellValue(vPAID_DRUG);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(15);
                            cell.setCellValue(vPAID_OTHER);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(16);
                            cell.setCellValue(vPAID_TOTAL);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(17);
                            cell.setCellValue(vPAID_TOTAL);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(18);
                            cell.setCellValue(vREMARK);
                            cell.setCellStyle(csString2);

                            cell = row.createCell(19);
                            cell.setCellValue(vInvoice_no);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(20);
                            cell.setCellValue(vTxid);
                            cell.setCellStyle(csStringtxid);

                            curRow++;
                            i++;

                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Console.LOG(e.getMessage(), 0);
                        programeStatus.setMessage("ละเอียด_จ่าย_เดี่ยว+เครือ" + ConstantMessage.MSG_OPREFER_ERROR + ConstantMessage.MSG_PROCESS_FAILS + e.toString());
                        programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
                        programeStatus.setProcessStatus(false);
                    } finally {
                        this.referDAO.closeConnection();
                    }
                    /**
                     * footer summary total
                     */
                    row = sheet.createRow(curRow);
                    cell = row.createCell(0);
                    cell.setCellValue("รวม");
                    sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 6));
                    cell.setCellStyle(csNum4B);

                    row.createCell(1).setCellStyle(csNum4B);
                    row.createCell(2).setCellStyle(csNum4B);
                    row.createCell(3).setCellStyle(csNum4B);
                    row.createCell(4).setCellStyle(csNum4B);
                    row.createCell(5).setCellStyle(csNum4B);
                    row.createCell(6).setCellStyle(csNum4B);

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
                    cell.setCellFormula(builderFormulaSum(15, row_formula_start, curRow));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(16);
                    cell.setCellFormula(builderFormulaSum(16, row_formula_start, curRow));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(17);
                    cell.setCellFormula(builderFormulaSum(17, row_formula_start, curRow));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(18);
                    cell.setCellStyle(csHeadTab);

                    workbookBase.setSheetName(j + 1, listHospital.get(j).getHosCode());
                    Console.LOG(Message.exportSuccess(listHospital.get(j).getHosCode()), 1);
                }

                workbookBase.removeSheetAt(0);
                workbookBase.write(out);

                out.close();
                file.close();

                programeStatus.setMessage(ConstantMessage.MSG_REPORT_SUCCESS);
                programeStatus.setTitle(ConstantMessage.MSG_REPORT_COMPLETE);
                programeStatus.setProcessStatus(true);
            } else {
                programeStatus.setMessage("ละเอียด_จ่าย_เดี่ยว+เครือ" + ConstantMessage.MSG_REPORT_NO_DATA);
                programeStatus.setTitle(ConstantMessage.MSG_NO_DATA);
                programeStatus.setProcessStatus(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
            programeStatus.setMessage("ละเอียด_จ่าย_เดี่ยว+เครือ" + ConstantMessage.MSG_OPREFER_ERROR + ConstantMessage.MSG_PROCESS_FAILS + e.toString());
            programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
            programeStatus.setProcessStatus(false);
        }
        return programeStatus;
    }
    /*
     * End Single File MutiSheet**************************************************************************
     */

    /*
     *  Single File Single Sheet ********************************************************************************
     */
    public ProgrameStatus refer_detailDeduct_singleClinic_1sheet1file(OppReport report) {
        FileOutputStream out = null;
        FileInputStream file = null;
        boolean status = false;
        ProgrameStatus programeStatus = new ProgrameStatus();
        int col_last = 23;
        int row_start = 6; // index row
        int row_formula_start = row_start + 1;
        int col_txtid_width = col_last + 1;
        try {

            List<HospitalService> listHospital = referDAO.getHospitalService(report);

            System.out.println("Hospital_size==" + listHospital.size());

            if (listHospital.size() != 0) {

                for (int j = 0; j < listHospital.size(); j++) {
                    HospitalService service = listHospital.get(j);
                    //readTemplate 
                    file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"refer_deduct_detail_singleclinic.xls"));

                    new FunctionUtil().createFolder(report.getPathFile() + ""+File.separator+"refer_deduct_detail_singleclinic");
                    out = new FileOutputStream(report.getPathFile() + ""+File.separator+"refer_deduct_detail_singleclinic"+File.separator+"" + service.getHosHmain().toString() + ".xls");

                    // load style
                    HSSFWorkbook wbRefer = new HSSFWorkbook(file);
                    this.loadStyle(wbRefer);

                    String EXCELL_HEADER = "รายละเอียดการหักเงินค่าบริการ OP Refer และ A/E ในบัญชีเครือข่ายเดียวกัน(กรณีคลินิกเดี่ยว)";
                    String EXCELL_MONTH = report.getTitle2();
                    String EXCELL_SERVICE = "หน่วยบริการประจำ: " + service.getHosHmainName() + " (" + service.getHosHmain() + ")";

//                    wb.setRepeatingRowsAndColumns(j, 0, 22, 4, 5);
//                    System.out.println("wb.setRepeatingRowsAndColumns(j, 0, 22, 4, 5);==>>");
                    // Start sheet 1 *******************************************************************************                    
                    HSSFSheet sheet = workbookBase.getSheetAt(0);

                    sheet.createFreezePane(4, 6);
                    sheet.setColumnWidth(col_txtid_width, WIDTH_TXID);
                    // Set the rows to repeat from row 4 to 5 on the first sheet.

                    HSSFCell cell = null;
                    HSSFRow row = null;

                    // row 0 Header
                    row = sheet.createRow(0);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_HEADER);
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col_last));
                    cell.setCellStyle(csHead);

                    // row 1 Service
                    row = sheet.createRow(1);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_MONTH);
                    sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, col_last));
                    cell.setCellStyle(csHead);

                    // row 1 Service
                    row = sheet.createRow(2);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_SERVICE);
                    sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, col_last));
                    cell.setCellStyle(csHead);

                    int curRow = row_start;
                    int i = 1;
                    ResultSet rs;
                    try {
                        report.setServiceCode(listHospital.get(j).getHosHmain());
                        rs = referDAO.getReportOpRefer(report);

                        while (rs.next()) {
                            int vNo = rs.getInt("no");
                            String vPID = rs.getString("PID");
                            String vHN = rs.getString("HN");
                            String vHCODE = rs.getString("HCODE");
                            String vPNAME = rs.getString("PNAME");
                            String VHCODENAME = rs.getString("HCODENAME");
                            String vDateopdTh = rs.getString("dateopd_thai_buddha");
                            String vdateopd = rs.getString("dateopd");
                            String vpdxcode = rs.getString("pdxcode");
                            Double vchrg_hc = rs.getDouble("chrg_hc");
                            Double vchrg_197 = rs.getDouble("chrg_197");
                            Double vchrg_drug = rs.getDouble("chrg_drug");
                            Double vchrg_other = rs.getDouble("chrg_other");
                            Double vchrg_total = rs.getDouble("chrg_total");
                            Double vpaid_hc = rs.getDouble("paid_hc");
                            Double vpaid_197 = rs.getDouble("paid_197");
                            Double vpaid_drug = rs.getDouble("paid_drug");
                            Double vpaid_other = rs.getDouble("paid_other");
                            Double vpaid_total = rs.getDouble("paid_total");
                            Double vdeduct_ophc = rs.getDouble("deduct_ophc");
                            Double vdeduct_oprf = rs.getDouble("deduct_oprf");
                            Double vdeduct_hmain = rs.getDouble("deduct_hmain");
                            Double vdeduct_total = rs.getDouble("deduct_total");
                            Double vdeduct_hmain_final = rs.getDouble("deduct_hmain_final");

                            String vremark = rs.getString("remark");
                            String vis_single = rs.getString("is_single");
                            String vstmp = rs.getString("stmp");
                            String vtxid = rs.getString("txid");
                            String vinvoice_no = rs.getString("invoice_no");

                            row = sheet.createRow(curRow);
                            row.setHeight((short) 340);
                            cell = row.createCell(0);
                            cell.setCellValue(i);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(1);
                            cell.setCellValue(function.subStringPid(vPID));
                            cell.setCellStyle(csStringPid);

                            cell = row.createCell(2);
                            cell.setCellValue(vHN);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(3);
                            cell.setCellValue(vPNAME);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(4);
                            cell.setCellValue(vHCODE + " : " + VHCODENAME);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(5);
                            cell.setCellValue(vDateopdTh);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(6);
                            cell.setCellValue(vpdxcode);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(7);
                            cell.setCellValue(vchrg_hc);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(8);
                            cell.setCellValue(vchrg_197);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(9);
                            cell.setCellValue(vchrg_drug);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(10);
                            cell.setCellValue(vchrg_other);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(11);
                            cell.setCellValue(vchrg_total);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(12);
                            cell.setCellValue(vpaid_hc);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(13);
                            cell.setCellValue(vpaid_197);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(14);
                            cell.setCellValue(vpaid_drug);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(15);
                            cell.setCellValue(vpaid_other);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(16);
                            cell.setCellValue(vpaid_total);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(17);
                            cell.setCellValue(vdeduct_ophc);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(18);
                            cell.setCellValue(vdeduct_oprf);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(19);
                            cell.setCellValue(vdeduct_hmain);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(20);
                            cell.setCellValue(vdeduct_total);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(21);
                            cell.setCellValue(vdeduct_hmain_final);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(22);
                            cell.setCellValue(vremark);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(23);
                            cell.setCellValue(vinvoice_no);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(24);
                            cell.setCellValue(vtxid);
                            cell.setCellStyle(csStringtxid);

                            curRow++;
                            i++;

                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Console.LOG(e.getMessage(), 0);
                    } finally {
                        referDAO.closeConnection();
                    }

                    /**
                     * footer summary total
                     */
                    row = sheet.createRow(curRow);
                    cell = row.createCell(0);
                    cell.setCellValue("รวม");
                    sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 6));
                    cell.setCellStyle(csNum4B);

                    row.createCell(1).setCellStyle(csNum4B);
                    row.createCell(2).setCellStyle(csNum4B);
                    row.createCell(3).setCellStyle(csNum4B);
                    row.createCell(4).setCellStyle(csNum4B);
                    row.createCell(5).setCellStyle(csNum4B);
                    row.createCell(6).setCellStyle(csNum4B);

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
                    cell.setCellFormula(builderFormulaSum(15, row_formula_start, curRow));
                    cell.setCellStyle(csDouble2B);

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
                    cell.setCellStyle(csHeadTab);

                    //wb.setSheetName(j + 1, listHospital.get(j).getHosHmainCode().toString());
                    Console.LOG(Message.exportSuccess(listHospital.get(j).getHosHmain().toString()), 1);
                    workbookBase.write(out);

                    out.close();
                    file.close();

                    programeStatus.setMessage(ConstantMessage.MSG_REPORT_SUCCESS);
                    programeStatus.setTitle(ConstantMessage.MSG_REPORT_COMPLETE);
                    programeStatus.setProcessStatus(true);
                }

                programeStatus.setMessage(ConstantMessage.MSG_REPORT_SUCCESS);
                programeStatus.setTitle(ConstantMessage.MSG_REPORT_COMPLETE);
                programeStatus.setProcessStatus(true);
            } else {
                programeStatus.setMessage("ละเอียด_หัก_เดี่ยว" + ConstantMessage.MSG_REPORT_NO_DATA);
                programeStatus.setTitle(ConstantMessage.MSG_NO_DATA);
                programeStatus.setProcessStatus(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
            programeStatus.setMessage("ละเอียด_จ่าย_เดี่ยว" + ConstantMessage.MSG_OPREFER_ERROR + ConstantMessage.MSG_PROCESS_FAILS + e.toString());
            programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
            programeStatus.setProcessStatus(false);
        }
        return programeStatus;
    }

    public ProgrameStatus refer_detailDeduct_clinicAndHospital_1sheet1file(OppReport report) {
        FileOutputStream out = null;
        FileInputStream file = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        int col_last = 23;
        int row_start = 6; // index row
        int row_formula_start = row_start + 1;
        int col_txtid_width = col_last + 1;
        try {

            List<HospitalService> listHospital = referDAO.getHospitalService(report);

            System.out.println("Hospital_size==" + listHospital.size());

            if (listHospital != null) {

                for (int j = 0; j < listHospital.size(); j++) {

                    //readTemplate 
                    file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"refer_deduct_detail_clinichospital.xls"));

                    new FunctionUtil().createFolder(report.getPathFile() + ""+File.separator+"refer_deduct_detail_clinichospital");
                    out = new FileOutputStream(report.getPathFile() + ""+File.separator+"refer_deduct_detail_clinichospital"+File.separator+"" + listHospital.get(j).getHosHmain().toString() + ".xls");

                    // load style
                    HSSFWorkbook wbRefer = new HSSFWorkbook(file);
                    this.loadStyle(wbRefer);

                    String EXCELL_HEADER = "รายละเอียดการหักเงินค่าบริการ OP Refer และ A/E ในบัญชีเครือข่ายเดียวกัน(กรณีคลินิกเครือและโรงพยาบาล)";
                    String EXCELL_MONTH = report.getTitle2();
                    String EXCELL_SERVICE = "หน่วยบริการประจำ: " + listHospital.get(j).getHosHmainName() + " (" + listHospital.get(j).getHosHmain() + ")";

                    // Start sheet 1 *******************************************************************************
                    HSSFSheet sheet = workbookBase.getSheetAt(0);

                    sheet.createFreezePane(4, 6);
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

                    // row 1 Service
                    row = sheet.createRow(1);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_MONTH);
                    sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, col_last));
                    cell.setCellStyle(csHead);

                    // row 1 Service
                    row = sheet.createRow(2);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_SERVICE);
                    sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, col_last));
                    cell.setCellStyle(csHead);

                    int curRow = 6;
                    int i = 1;
                    ResultSet rs;
                    try {
                        report.setServiceCode(listHospital.get(j).getHosHmain());
                        rs = referDAO.getReportOpRefer(report);
                        while (rs.next()) {
                            int vNo = rs.getInt("no");
                            String vPID = rs.getString("PID");
                            String vHN = rs.getString("HN");
                            String vHCODE = rs.getString("HCODE");
                            String vPNAME = rs.getString("PNAME");
                            String vHCODENAME = rs.getString("HCODENAME");
                            String vDateopdTh = rs.getString("dateopd_thai_buddha");
                            String vdateopd = rs.getString("dateopd");
                            String vpdxcode = rs.getString("pdxcode");
                            Double vchrg_hc = rs.getDouble("chrg_hc");
                            Double vchrg_197 = rs.getDouble("chrg_197");
                            Double vchrg_drug = rs.getDouble("chrg_drug");
                            Double vchrg_other = rs.getDouble("chrg_other");
                            Double vchrg_total = rs.getDouble("chrg_total");
                            Double vpaid_hc = rs.getDouble("paid_hc");
                            Double vpaid_197 = rs.getDouble("paid_197");
                            Double vpaid_drug = rs.getDouble("paid_drug");
                            Double vpaid_other = rs.getDouble("paid_other");
                            Double vpaid_total = rs.getDouble("paid_total");
                            Double vdeduct_ophc = rs.getDouble("deduct_ophc");
                            Double vdeduct_oprf = rs.getDouble("deduct_oprf");
                            Double vdeduct_hmain = rs.getDouble("deduct_hmain");
                            Double vdeduct_total = rs.getDouble("deduct_total");
                            Double vdeduct_hmain_final = rs.getDouble("deduct_hmain_final");

                            String vremark = rs.getString("remark");
                            String vis_single = rs.getString("is_single");
                            String vstmp = rs.getString("stmp");
                            String vTxid = rs.getString("txid");
                            String vInvoice_no = rs.getString("invoice_no");

                            row = sheet.createRow(curRow);
                            row.setHeight((short) 340);
                            cell = row.createCell(0);
                            cell.setCellValue(i);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(1);
                            cell.setCellValue(function.subStringPid(vPID));
                            cell.setCellStyle(csStringPid);

                            cell = row.createCell(2);
                            cell.setCellValue(vHN);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(3);
                            cell.setCellValue(vPNAME);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(4);
                            cell.setCellValue(vHCODE + " : " + vHCODENAME);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(5);
                            cell.setCellValue(vDateopdTh);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(6);
                            cell.setCellValue(vpdxcode);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(7);
                            cell.setCellValue(vchrg_hc);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(8);
                            cell.setCellValue(vchrg_197);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(9);
                            cell.setCellValue(vchrg_drug);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(10);
                            cell.setCellValue(vchrg_other);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(11);
                            cell.setCellValue(vchrg_total);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(12);
                            cell.setCellValue(vpaid_hc);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(13);
                            cell.setCellValue(vpaid_197);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(14);
                            cell.setCellValue(vpaid_drug);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(15);
                            cell.setCellValue(vpaid_other);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(16);
                            cell.setCellValue(vpaid_total);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(17);
                            cell.setCellValue(vdeduct_ophc);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(18);
                            cell.setCellValue(vdeduct_oprf);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(19);
                            cell.setCellValue(vdeduct_hmain);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(20);
                            cell.setCellValue(vdeduct_total);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(21);
                            cell.setCellValue(vdeduct_hmain_final);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(22);
                            cell.setCellValue(vremark);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(23);
                            cell.setCellValue(vInvoice_no);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(24);
                            cell.setCellValue(vTxid);
                            cell.setCellStyle(csStringtxid);

                            curRow++;
                            i++;

                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Console.LOG(e.getMessage(), 0);
                    } finally {
                        this.referDAO.closeConnection();
                    }

                    row = sheet.createRow(curRow);
                    cell = row.createCell(0);
                    cell.setCellValue("รวม");
                    sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 6));
                    cell.setCellStyle(csNum4B);

                    row.createCell(1).setCellStyle(csNum4B);
                    row.createCell(2).setCellStyle(csNum4B);
                    row.createCell(3).setCellStyle(csNum4B);
                    row.createCell(4).setCellStyle(csNum4B);
                    row.createCell(5).setCellStyle(csNum4B);
                    row.createCell(6).setCellStyle(csNum4B);

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
                    cell.setCellFormula(builderFormulaSum(15, row_formula_start, curRow));
                    cell.setCellStyle(csDouble2B);

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
                    cell.setCellStyle(csHeadTab);

                    //wb.setSheetName(j + 1, listHospital.get(j).getHosHmainCode().toString());
                    Console.LOG(Message.exportSuccess(listHospital.get(j).getHosHmain()), 1);
                    workbookBase.write(out);

                    out.close();
                    file.close();

                    programeStatus.setMessage(ConstantMessage.MSG_REPORT_SUCCESS);
                    programeStatus.setTitle(ConstantMessage.MSG_REPORT_COMPLETE);
                    programeStatus.setProcessStatus(true);
                }

                programeStatus.setMessage(ConstantMessage.MSG_REPORT_SUCCESS);
                programeStatus.setTitle(ConstantMessage.MSG_REPORT_COMPLETE);
                programeStatus.setProcessStatus(true);
            } else {
                programeStatus.setMessage("ละเอียด_หัก_เดี่ยว+เครือ" + ConstantMessage.MSG_REPORT_NO_DATA);
                programeStatus.setTitle(ConstantMessage.MSG_NO_DATA);
                programeStatus.setProcessStatus(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
            programeStatus.setMessage("ละเอียด_จ่าย_เดี่ยว+เครือ" + ConstantMessage.MSG_OPREFER_ERROR + ConstantMessage.MSG_PROCESS_FAILS + e.toString());
            programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
            programeStatus.setProcessStatus(false);
        }
        return programeStatus;
    }

    // ################# web site ###################
    public ProgrameStatus refer_detailDeduct_1sheet1file(OppReport report) {
        FileOutputStream out = null;
        FileInputStream file = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        String stmp = StringOpUtil.removeNull(report.getYearMonth() + "-" + report.getNo());
        String pathDirectory = report.getPathFile() + ""+File.separator+"oprf"+File.separator+"" + stmp;
        int col_last = 23;
        int row_start = 6; // index row
        int row_formula_start = row_start + 1;
        int col_txtid_width = col_last + 1;
        try {

            List<HospitalService> listHospital = referDAO.getHospitalService(report);

            System.out.println("Hospital_size==" + listHospital.size());

            if (listHospital != null) {

                for (int j = 0; j < listHospital.size(); j++) {
                    HospitalService objService = listHospital.get(j);
                    //readTemplate 
                    file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"refer_deduct_detail.xls"));

                    if (report.getFor_use() == 0) { // 0 = ทั่วไป
                        new FunctionUtil().createFolder(report.getPathFile() + ""+File.separator+"refer_deduct_detail_+");
                        out = new FileOutputStream(report.getPathFile() + ""+File.separator+"refer_deduct_detail_+"+File.separator+"" + objService.getHosHmain() + ".xls");
                    } else { // 1= website
                        new FunctionUtil().createMutiDirectory(new File(pathDirectory));
                        out = new FileOutputStream(pathDirectory + ""+File.separator+"oprf_deduct_detail_" + objService.getHosHmain() + "_" + stmp + ".xls");
                    }
                    // load style
                    HSSFWorkbook wbRefer = new HSSFWorkbook(file);
                    this.loadStyle(wbRefer);

                    String EXCELL_HEADER = "รายละเอียดการหักเงินค่าบริการ OP Refer และ A/E ในบัญชีเครือข่ายเดียวกัน";
                    String EXCELL_MONTH = report.getTitle2();
                    String EXCELL_SERVICE = "หน่วยบริการประจำ: " + listHospital.get(j).getHosHmainName() + " (" + listHospital.get(j).getHosHmain() + ")";

                    // Start sheet 1 *******************************************************************************
                    HSSFSheet sheet = workbookBase.getSheetAt(0);

                    sheet.createFreezePane(4, 6);
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

                    // row 1 Service
                    row = sheet.createRow(1);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_MONTH);
                    sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, col_last));
                    cell.setCellStyle(csHead);

                    // row 1 Service
                    row = sheet.createRow(2);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_SERVICE);
                    sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, col_last));
                    cell.setCellStyle(csHead);

                    int curRow = row_start;
                    int i = 1;
                    ResultSet rs;
                    try {
                        report.setServiceCode(listHospital.get(j).getHosHmain());
                        rs = referDAO.getReportOpRefer(report);
                        while (rs.next()) {
                            int vNo = rs.getInt("no");
                            String vPID = rs.getString("PID");
                            String vHN = rs.getString("HN");
                            String vHCODE = rs.getString("HCODE");
                            String vPNAME = rs.getString("PNAME");
                            String vHCODENAME = rs.getString("HCODENAME");
                            String vDateopdTh = rs.getString("dateopd_thai_buddha");
                            String vdateopd = rs.getString("dateopd");
                            String vpdxcode = rs.getString("pdxcode");
                            Double vchrg_hc = rs.getDouble("chrg_hc");
                            Double vchrg_197 = rs.getDouble("chrg_197");
                            Double vchrg_drug = rs.getDouble("chrg_drug");
                            Double vchrg_other = rs.getDouble("chrg_other");
                            Double vchrg_total = rs.getDouble("chrg_total");
                            Double vpaid_hc = rs.getDouble("paid_hc");
                            Double vpaid_197 = rs.getDouble("paid_197");
                            Double vpaid_drug = rs.getDouble("paid_drug");
                            Double vpaid_other = rs.getDouble("paid_other");
                            Double vpaid_total = rs.getDouble("paid_total");
                            Double vdeduct_ophc = rs.getDouble("deduct_ophc");
                            Double vdeduct_oprf = rs.getDouble("deduct_oprf");
                            Double vdeduct_hmain = rs.getDouble("deduct_hmain");
                            Double vdeduct_total = rs.getDouble("deduct_total");
                            Double vdeduct_hmain_final = rs.getDouble("deduct_hmain_final");

                            String vremark = rs.getString("remark");
                            String vis_single = rs.getString("is_single");
                            String vstmp = rs.getString("stmp");
                            String vTxid = rs.getString("txid");
                            String vInvoice_no = rs.getString("invoice_no");

                            row = sheet.createRow(curRow);
                            row.setHeight((short) 340);
                            cell = row.createCell(0);
                            cell.setCellValue(i);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(1);
                            cell.setCellValue(function.subStringPid(vPID));
                            cell.setCellStyle(csStringPid);

                            cell = row.createCell(2);
                            cell.setCellValue(vHN);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(3);
                            cell.setCellValue(vPNAME);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(4);
                            cell.setCellValue(vHCODE + " : " + vHCODENAME);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(5);
                            cell.setCellValue(vDateopdTh);
                            cell.setCellStyle(csStringCenter);

                            cell = row.createCell(6);
                            cell.setCellValue(vpdxcode);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(7);
                            cell.setCellValue(vchrg_hc);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(8);
                            cell.setCellValue(vchrg_197);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(9);
                            cell.setCellValue(vchrg_drug);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(10);
                            cell.setCellValue(vchrg_other);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(11);
                            cell.setCellValue(vchrg_total);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(12);
                            cell.setCellValue(vpaid_hc);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(13);
                            cell.setCellValue(vpaid_197);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(14);
                            cell.setCellValue(vpaid_drug);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(15);
                            cell.setCellValue(vpaid_other);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(16);
                            cell.setCellValue(vpaid_total);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(17);
                            cell.setCellValue(vdeduct_ophc);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(18);
                            cell.setCellValue(vdeduct_oprf);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(19);
                            cell.setCellValue(vdeduct_hmain);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(20);
                            cell.setCellValue(vdeduct_total);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(21);
                            cell.setCellValue(vdeduct_hmain_final);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(22);
                            cell.setCellValue(vremark);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(23);
                            cell.setCellValue(vInvoice_no);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(24);
                            cell.setCellValue(vTxid);
                            cell.setCellStyle(csStringtxid);

                            curRow++;
                            i++;

                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Console.LOG(e.getMessage(), 0);
                    } finally {
                        this.referDAO.closeConnection();
                    }

                    row = sheet.createRow(curRow);
                    cell = row.createCell(0);
                    cell.setCellValue("รวม");
                    sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 6));
                    cell.setCellStyle(csNum4B);

                    row.createCell(1).setCellStyle(csNum4B);
                    row.createCell(2).setCellStyle(csNum4B);
                    row.createCell(3).setCellStyle(csNum4B);
                    row.createCell(4).setCellStyle(csNum4B);
                    row.createCell(5).setCellStyle(csNum4B);
                    row.createCell(6).setCellStyle(csNum4B);

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
                    cell.setCellFormula(builderFormulaSum(15, row_formula_start, curRow));
                    cell.setCellStyle(csDouble2B);

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
                    cell.setCellStyle(csHeadTab);

                    //wb.setSheetName(j + 1, listHospital.get(j).getHosHmainCode().toString());
                    Console.LOG(Message.exportSuccess(listHospital.get(j).getHosHmain()), 1);
                    workbookBase.write(out);

                    out.close();
                    file.close();

                    programeStatus.setMessage(ConstantMessage.MSG_REPORT_SUCCESS);
                    programeStatus.setTitle(ConstantMessage.MSG_REPORT_COMPLETE);
                    programeStatus.setProcessStatus(true);
                }

                programeStatus.setMessage(ConstantMessage.MSG_REPORT_SUCCESS);
                programeStatus.setTitle(ConstantMessage.MSG_REPORT_COMPLETE);
                programeStatus.setProcessStatus(true);
            } else {
                programeStatus.setMessage("ละเอียด_หัก_เดี่ยว+เครือ" + ConstantMessage.MSG_REPORT_NO_DATA);
                programeStatus.setTitle(ConstantMessage.MSG_NO_DATA);
                programeStatus.setProcessStatus(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
            programeStatus.setMessage("ละเอียด_หัก_เดี่ยว+เครือ" + ConstantMessage.MSG_OPREFER_ERROR + ConstantMessage.MSG_PROCESS_FAILS + e.toString());
            programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
            programeStatus.setProcessStatus(false);
        }
        return programeStatus;
    }

    public ProgrameStatus refer_detailPayment_1sheet1file(OppReport report) {
        FileOutputStream out = null;
        FileInputStream file = null;
        boolean status = false;
        ProgrameStatus programeStatus = new ProgrameStatus();
        String stmp = report.getYearMonth() + "-" + report.getNo();
        String pathDirectory = report.getPathFile() + ""+File.separator+"oprf"+File.separator+"" + stmp;
        int col_last = 19;
        int row_start = 6; // index row
        int row_formula_start = row_start + 1;
        int col_txtid_width = col_last + 1;
        try {

            List<HospitalService> listHospital = referDAO.getHospitalService(report);

            System.out.println("Hospital_size==" + listHospital.size());

            if (listHospital != null) {

                for (int j = 0; j < listHospital.size(); j++) {
                    HospitalService objService = listHospital.get(j);
                    //readTemplate 
                    file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"refer_pay_detail.xls"));

                    if (report.getFor_use() == 0) { // 0 = ทั่วไป
                        new FunctionUtil().createFolder(report.getPathFile() + ""+File.separator+"refer_pay_detail_+");
                        out = new FileOutputStream(report.getPathFile() + ""+File.separator+"refer_pay_detail_+"+File.separator+"" + objService.getHosCode() + ".xls");
                    } else { // 1 =  web site
                        new FunctionUtil().createMutiDirectory(new File(pathDirectory));
                        out = new FileOutputStream(pathDirectory + ""+File.separator+"oprf_paid_detail_" + objService.getHosCode() + "_" + stmp + ".xls");
                    }
                    // load style
                    HSSFWorkbook wbRefer = new HSSFWorkbook(file);
                    this.loadStyle(wbRefer);

                    String EXCELL_HEADER = "รายละเอียดการจ่ายชดเชยการจ่ายชดเชยค่าบริการ OP Refer และ A/E ในบัญชีเครือข่ายเดียวกัน ปีงบประมาณ " + new DateUtil().getBudgeMonthYear_543(report.getBudget_stmp(),ConstantVariable.BUDGET_MONTH)+ "";
                    String EXCELL_MONTH = report.getTitle2();
                    String EXCELL_SERVICE = "หน่วยบริการที่รักษา: " + listHospital.get(j).getHosCodeName() + " (" + listHospital.get(j).getHosCode() + ")";

                    // Start sheet 1 *******************************************************************************
                    HSSFSheet sheet = workbookBase.getSheetAt(0);

                    // FreezePane
                    sheet.createFreezePane(4, 6);
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

                    // row 1 Service
                    row = sheet.createRow(1);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_MONTH);
                    sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, col_last));
                    cell.setCellStyle(csHead);

                    // row 1 Service
                    row = sheet.createRow(2);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_SERVICE);
                    sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, col_last));
                    cell.setCellStyle(csHead);

                    int curRow = 6;
                    int i = 1;
                    ResultSet rs;
                    try {
                        report.setServiceCode(listHospital.get(j).getHosCode());
                        rs = referDAO.getReportOpRefer(report);
                        while (rs.next()) {

                            int vNo = rs.getInt("no");
                            String vPID = rs.getString("PID");
                            String vHN = rs.getString("HN");
                            String vhmainop_n = rs.getString("hmain");
                            String vhmainop_name = rs.getString("hmainname");
                            String vFLNAME = rs.getString("PNAME");
                            String vHNAME = rs.getString("hcodeNAME");
                            String vDATEOPD = rs.getString("DATEOPD");
                            String vdateopdTh = rs.getString("dateopd_thai_buddha");
                            String vPDXCODE = rs.getString("PDXCODE");

                            double vCHRG_HC = rs.getDouble("CHRG_HC");
                            double vCHRG_197 = rs.getDouble("CHRG_197");
                            double vCHRG_DRUG = rs.getDouble("CHRG_DRUG");
                            double vCHRG_OTHER = rs.getDouble("CHRG_OTHER");
                            double vCHRG_TOTAL = rs.getDouble("CHRG_TOTAL");
                            double vPAID_HC = rs.getDouble("PAID_HC");
                            double vPAID_197 = rs.getDouble("PAID_197");
                            double vPAID_DRUG = rs.getDouble("PAID_DRUG");
                            double vPAID_OTHER = rs.getDouble("PAID_OTHER");
                            double vPAID_TOTAL = rs.getDouble("PAID_TOTAL");

                            String vREMARK = rs.getString("REMARK");
                            String vis_single = rs.getString("is_single");
                            String vSTMP = rs.getString("STMP");
                            String vTxid = rs.getString("txid");
                            String vInvoice_no = rs.getString("txid");

                            row = sheet.createRow(curRow);
                            row.setHeight((short) 340);
                            cell = row.createCell(0);
                            cell.setCellValue(i);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(1);
                            cell.setCellValue(function.subStringPid(vPID));
                            cell.setCellStyle(csStringPid);

                            cell = row.createCell(2);
                            cell.setCellValue(vHN);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(3);
                            cell.setCellValue(vFLNAME);
                            cell.setCellStyle(csString2);

                            cell = row.createCell(4);
                            cell.setCellValue(vhmainop_n + " : " + vhmainop_name);
                            cell.setCellStyle(csString2);

                            cell = row.createCell(5);
                            cell.setCellValue(vdateopdTh);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(6);
                            cell.setCellValue(vPDXCODE);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(7);
                            cell.setCellValue(vCHRG_HC);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(8);
                            cell.setCellValue(vCHRG_197);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(9);
                            cell.setCellValue(vCHRG_DRUG);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(10);
                            cell.setCellValue(vCHRG_OTHER);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(11);
                            cell.setCellValue(vCHRG_TOTAL);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(12);
                            cell.setCellValue(vPAID_HC);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(13);
                            cell.setCellValue(vPAID_197);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(14);
                            cell.setCellValue(vPAID_DRUG);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(15);
                            cell.setCellValue(vPAID_OTHER);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(16);
                            cell.setCellValue(vPAID_TOTAL);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(17);
                            cell.setCellValue(vPAID_TOTAL);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(18);
                            cell.setCellValue(vREMARK);
                            cell.setCellStyle(csString2);

                            cell = row.createCell(19);
                            cell.setCellValue(vInvoice_no);
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(20);
                            cell.setCellValue(vTxid);
                            cell.setCellStyle(csStringtxid);

                            curRow++;
                            i++;

                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Console.LOG(e.getMessage(), 0);
                    } finally {
                        this.referDAO.closeConnection();
                    }
                    /**
                     * footer summary total
                     */
                    row = sheet.createRow(curRow);
                    cell = row.createCell(0);
                    cell.setCellValue("รวม");
                    sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 6));
                    cell.setCellStyle(csNum4B);

                    row.createCell(1).setCellStyle(csNum4B);
                    row.createCell(2).setCellStyle(csNum4B);
                    row.createCell(3).setCellStyle(csNum4B);
                    row.createCell(4).setCellStyle(csNum4B);
                    row.createCell(5).setCellStyle(csNum4B);
                    row.createCell(6).setCellStyle(csNum4B);

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
                    cell.setCellFormula(builderFormulaSum(15, row_formula_start, curRow));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(16);
                    cell.setCellFormula(builderFormulaSum(16, row_formula_start, curRow));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(17);
                    cell.setCellFormula(builderFormulaSum(17, row_formula_start, curRow));
                    cell.setCellStyle(csDouble2B);

                    cell = row.createCell(18);
                    cell.setCellStyle(csHeadTab);

                    //wb.setSheetName(j + 1, listHospital.get(j).getHosCode().toString());
                    Console.LOG(Message.exportSuccess(listHospital.get(j).getHosCode()), 1);
                    workbookBase.write(out);

                    out.close();
                    file.close();

                }

                programeStatus.setMessage("ละเอียด_จ่าย_เดี่ยว+เครือ" + ConstantMessage.MSG_REPORT_SUCCESS);
                programeStatus.setTitle(ConstantMessage.MSG_REPORT_INFOMATION);
                programeStatus.setProcessStatus(true);
            } else {
                programeStatus.setMessage("ละเอียด_จ่าย_เดี่ยว+เครือ" + ConstantMessage.MSG_REPORT_NO_DATA);
                programeStatus.setTitle(ConstantMessage.MSG_NO_DATA);
                programeStatus.setProcessStatus(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
            programeStatus.setMessage("ละเอียด_จ่าย_เดี่ยว+เครือ" + ConstantMessage.MSG_OPREFER_ERROR + ConstantMessage.MSG_PROCESS_FAILS + e.toString());
            programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
            programeStatus.setProcessStatus(false);
        }
        return programeStatus;
    }

    public ProgrameStatus refer_sumDeduct_1sheet1file_byHcode(OppReport report) {
        FileInputStream file = null;
        FileOutputStream out = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        String stmp = StringOpUtil.removeNull(report.getYearMonth()) + "-" + report.getNo();
        String pathDirectory = report.getPathFile() + ""+File.separator+"oprf"+File.separator+"" + stmp;
        int col_last = 15;
        int row_start = 5; // index row
        int row_formula_start = row_start + 1;
        int col_txtid_width = col_last + 1;
        try {
            List<HospitalService> listHospital = referDAO.getHospitalService(report);
            if (listHospital != null) {
                for (int j = 0; j < listHospital.size(); j++) {
                    HospitalService objService = listHospital.get(j);

                    // ######################  FileInputStream ###############
                    file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"refer_deduct_sum.xls"));

                    if (report.getFor_use() == 0) { // 0 = ทั่วไป
                        out = new FileOutputStream(report.getPathFile() + ""+File.separator+"refer_deduct_sum " + report.getYearMonth() + "-" + report.getNo() + ".xls");
                    } else { // 1= web site
                        new FunctionUtil().createMutiDirectory(new File(pathDirectory));
                        out = new FileOutputStream(pathDirectory + ""+File.separator+"oprf_deduct_sum_" + objService.getHosHmain() + "_" + stmp + ".xls");
                    }
                // ######################  FileInputStream ###############

                    // style Excell
                    HSSFWorkbook wbRefer = new HSSFWorkbook(file);
                    this.loadStyle(wbRefer);

                    String EXCELL_HEADER = "สรุปการหักเงินค่าบริการ OP Refer และ A/E ในบัญชีเครือข่ายเดียวกัน  ปีงบประมาณ " + new DateUtil().getBudgeMonthYear_543(report.getBudget_stmp(),ConstantVariable.BUDGET_MONTH)+ " ";
                    String EXCELL_MONTH = report.getTitle2();

                    HSSFSheet sheet = workbookBase.getSheetAt(0);

                    sheet.createFreezePane(3, 5);

                    HSSFCell cell = null;
                    HSSFRow row = null;

                    // row 0
                    row = sheet.createRow(0);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_HEADER);
                    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col_last));
                    cell.setCellStyle(csHead);

                    // row 1
                    row = sheet.createRow(1);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_MONTH);
                    sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, col_last));
                    cell.setCellStyle(csHead);

                    /**
                     * data detail (row 4)
                     */
                    int curRow = row_start;
                    int i = 1;
                    ResultSet rs = null;

                    try {
                        report.setServiceCode(objService.getHosHmain());
                        rs = referDAO.getReportOpRefer(report);

                        while (rs.next()) {

                            int vNo = rs.getInt("no");
                            String vhmain = rs.getString("hcode");
                            String vhmainname = rs.getString("hcodename");
                            int vpid_normal_count = rs.getInt("pid_normal_count");
                            int vpid_appeal_count = rs.getInt("pid_appeal_count");
                            int vpid_normal_appeal_total = rs.getInt("pid_normal_appeal_total");

                            double vsum_chrg_hc = rs.getDouble("sum_chrg_hc");
                            double vsum_chrg_197 = rs.getDouble("sum_chrg_197");
                            double vsum_chrg_drug = rs.getDouble("sum_chrg_drug");
                            double vsum_chrg_other = rs.getDouble("sum_chrg_other");
                            double vsum_chrg_total = rs.getDouble("sum_chrg_total");

                            double vsum_deduct_ophc = rs.getDouble("sum_deduct_ophc");
                            double vsum_deduct_oprf = rs.getDouble("sum_deduct_oprf");
                            double vsum_deduct_hmain = rs.getDouble("sum_deduct_hmain");
                            double vsum_deduct_total = rs.getDouble("sum_deduct_total");
                            double vdeduct_hmain_final = rs.getDouble("deduct_hmain_final");

                            row = sheet.createRow(curRow);
                            row.setHeight((short) 340);
                            cell = row.createCell(0);
                            cell.setCellValue(vNo);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(1);
                            cell.setCellValue(vhmain);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(2);
                            cell.setCellValue(vhmainname);
                            cell.setCellStyle(csString2);

                            cell = row.createCell(3);
                            cell.setCellValue(vpid_normal_count);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(4);
                            cell.setCellValue(vpid_appeal_count);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(5);
                            cell.setCellValue(vpid_normal_appeal_total);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(6);
                            cell.setCellValue(vsum_chrg_hc);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(7);
                            cell.setCellValue(vsum_chrg_197);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(8);
                            cell.setCellValue(vsum_chrg_drug);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(9);
                            cell.setCellValue(vsum_chrg_other);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(10);
                            cell.setCellValue(vsum_chrg_total);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(11);
                            cell.setCellValue(vsum_deduct_ophc);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(12);
                            cell.setCellValue(vsum_deduct_oprf);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(13);
                            cell.setCellValue(vsum_deduct_hmain);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(14);
                            cell.setCellValue(vsum_deduct_total);
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(15);
                            cell.setCellValue(vdeduct_hmain_final);
                            cell.setCellStyle(csDouble2);

                            curRow++;
                            i++;
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Console.LOG(e.getMessage(), 0);
                        programeStatus.setMessage("สรุป_หัก_เดี่ยว+เครือ" + ConstantMessage.MSG_OPREFER_ERROR + ConstantMessage.MSG_PROCESS_FAILS + e.toString());
                        programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
                        programeStatus.setProcessStatus(false);
                    } finally {
                        this.referDAO.closeConnection();
                    }

                    row = sheet.createRow(curRow);
                    cell = row.createCell(0);
                    cell.setCellValue("รวม");
                    sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 2));
                    cell.setCellStyle(csNum4B);

                    row.createCell(1).setCellStyle(csNum4B);
                    row.createCell(2).setCellStyle(csNum4B);

                    cell = row.createCell(3);
                    cell.setCellFormula(builderFormulaSum(3, row_formula_start, curRow));
                    cell.setCellStyle(csNum4B);

                    cell = row.createCell(4);
                    cell.setCellFormula(builderFormulaSum(4, row_formula_start, curRow));
                    cell.setCellStyle(csNum4B);

                    cell = row.createCell(5);
                    cell.setCellFormula(builderFormulaSum(5, row_formula_start, curRow));
                    cell.setCellStyle(csNum4B);

                    cell = row.createCell(6);
                    cell.setCellFormula(builderFormulaSum(6, row_formula_start, curRow));
                    cell.setCellStyle(csDouble2B);

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
                    cell.setCellFormula(builderFormulaSum(15, row_formula_start, curRow));
                    cell.setCellStyle(csDouble2B);

                    workbookBase.write(out);
                    out.close();
                    Console.LOG("ออกรายงาน สรุปหัก เรียบร้อยแล้ว", 1);

                }
                programeStatus.setMessage("สรุป_หัก_เดี่ยว+เครือ" + ConstantMessage.MSG_REPORT_SUCCESS);
                programeStatus.setTitle(ConstantMessage.MSG_REPORT_COMPLETE);
                programeStatus.setProcessStatus(true);
            } else {
                programeStatus.setMessage("สรุป_หัก_เดี่ยว+เครือ" + ConstantMessage.MSG_NO_DATA);
                programeStatus.setTitle(ConstantMessage.MSG_CAN_NOT_REPORT_ERROR);
                programeStatus.setProcessStatus(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
            programeStatus.setMessage("สรุป_หัก_เดี่ยว+เครือ " + ConstantMessage.MSG_OPREFER_ERROR + ConstantMessage.MSG_PROCESS_FAILS + e.toString());
            programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
            programeStatus.setProcessStatus(false);
        }
        return programeStatus;
    }

    public ProgrameStatus refer_sumPayment_1sheet1file_byHcode(OppReport report) {
        FileOutputStream out;
        FileInputStream file = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        String stmp = StringOpUtil.removeNull(report.getYearMonth()) + "-" + report.getNo();
        String pathDirectory = report.getPathFile() + ""+File.separator+"oprf"+File.separator+"" + stmp;
        int col_last = 15;
        int row_start = 5; // index row
        int row_formula_start = row_start + 1;
        int col_txtid_width = col_last + 1;
        try {

            List<HospitalService> listHospital = referDAO.getHospitalService(report);

            for (int j = 0; j < listHospital.size(); j++) {
                HospitalService objService = listHospital.get(j);

                // ######################### FileInputStream ####################
                file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"refer_pay_sum.xls"));
                if (report.getFor_use() == 0) { // 0 = ทั่วไป                   
                    out = new FileOutputStream(report.getPathFile() + ""+File.separator+"refer_pay_sum" + report.getYearMonth() + "-" + report.getNo() + ".xls");
                } else { // 1 = website
                    new FunctionUtil().createMutiDirectory(new File(pathDirectory));
                    out = new FileOutputStream(pathDirectory + ""+File.separator+"oprf_paid_sum_" + objService.getHosCode() + "_" + stmp + ".xls");
                }
                // ######################### FileInputStream ####################
                /**
                 * style exell
                 */
                HSSFWorkbook wbRefer = new HSSFWorkbook(file);
                this.loadStyle(wbRefer);

                String EXCELL_HEADER = "สรุปการจ่ายชดเชยค่าบริการ OP Refer และ A/E ในบัญชีเครือข่ายเดียวกัน ปีงบประมาณ " + new DateUtil().getBudgeMonthYear_543(report.getBudget_stmp(),ConstantVariable.BUDGET_MONTH)+ "";
                String EXCELL_MONTH = report.getTitle2();

                HSSFSheet sheet = workbookBase.getSheetAt(0);

                // FreezePane
                sheet.createFreezePane(3, 5);

                HSSFCell cell = null;
                HSSFRow row = null;

                // row 0
                row = sheet.createRow(0);
                row.setHeight((short) 390);
                cell = row.createCell(0);
                cell.setCellValue(EXCELL_HEADER);
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col_last));
                cell.setCellStyle(csHead);

                // row 1
                row = sheet.createRow(1);
                row.setHeight((short) 390);
                cell = row.createCell(0);
                cell.setCellValue(EXCELL_MONTH);
                sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, col_last));
                cell.setCellStyle(csHead);

                /**
                 * data detail (row 4)
                 */
                int curRow = row_start;
                int i = 1;
                ResultSet rs = null;

                try {
                    report.setServiceCode(objService.getHosCode());
                    rs = referDAO.getReportOpRefer(report);

                    while (rs.next()) {
                        int vNo = rs.getInt("no");
                        String vHMain = rs.getString("hmain");
                        String vHMainName = rs.getString("hmainname");
                        int vpid_normal_count = rs.getInt("pid_normal_count");
                        int vpid_appeal_count = rs.getInt("pid_appeal_count");
                        int vpid_normal_appeal_total = rs.getInt("pid_normal_appeal_total");

                        double vsum_chrg_hc = rs.getDouble("sum_chrg_hc");
                        double vsum_chrg_197 = rs.getDouble("sum_chrg_197");
                        double vsum_chrg_drug = rs.getDouble("sum_chrg_drug");
                        double vsum_chrg_other = rs.getDouble("sum_chrg_other");
                        double vsum_chrg_total = rs.getDouble("sum_chrg_total");

                        double vsum_paid_hc = rs.getDouble("sum_paid_hc");
                        double vsum_paid_197 = rs.getDouble("sum_paid_197");
                        double vsum_paid_drug = rs.getDouble("sum_paid_drug");
                        double vsum_paid_other = rs.getDouble("sum_paid_other");
                        double vsum_paid_total = rs.getDouble("sum_paid_total");
                        double vsum_paid_final = rs.getDouble("sum_paid_final");

                        row = sheet.createRow(curRow);
                        row.setHeight((short) 340);
                        cell = row.createCell(0);
                        cell.setCellValue(vNo);
                        cell.setCellStyle(csNum4);

                        cell = row.createCell(1);
                        cell.setCellValue(vHMain);
                        cell.setCellStyle(csNum4);

                        cell = row.createCell(2);
                        cell.setCellValue(vHMainName);
                        cell.setCellStyle(csString2);

                        cell = row.createCell(3);
                        cell.setCellValue(vpid_normal_count);
                        cell.setCellStyle(csNum4);

                        cell = row.createCell(4);
                        cell.setCellValue(vpid_appeal_count);
                        cell.setCellStyle(csNum4);

                        cell = row.createCell(5);
                        cell.setCellValue(vpid_normal_appeal_total);
                        cell.setCellStyle(csNum4);

                        cell = row.createCell(6);
                        cell.setCellValue(vsum_chrg_hc);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(7);
                        cell.setCellValue(vsum_chrg_197);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(8);
                        cell.setCellValue(vsum_chrg_drug);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(9);
                        cell.setCellValue(vsum_chrg_other);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(10);
                        cell.setCellValue(vsum_chrg_total);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(11);
                        cell.setCellValue(vsum_paid_hc);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(12);
                        cell.setCellValue(vsum_paid_197);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(13);
                        cell.setCellValue(vsum_paid_drug);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(14);
                        cell.setCellValue(vsum_paid_other);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(15);
                        cell.setCellValue(vsum_paid_total);
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(16);
                        cell.setCellValue(vsum_paid_final);
                        cell.setCellStyle(csDouble2);

                        curRow++;
                        i++;
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    Console.LOG(e.getMessage(), 0);
                    programeStatus.setMessage("สรุป_จ่าย_เดี่ยว+เครือ" + ConstantMessage.MSG_OPREFER_ERROR + ConstantMessage.MSG_PROCESS_FAILS + e.toString());
                    programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
                    programeStatus.setProcessStatus(false);
                } finally {
                    this.referDAO.closeConnection();
                }

                row = sheet.createRow(curRow);
                cell = row.createCell(0);
                cell.setCellValue("รวม");
                sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 2));
                cell.setCellStyle(csNum4B);

                row.createCell(1).setCellStyle(csNum4B);
                row.createCell(2).setCellStyle(csNum4B);

                cell = row.createCell(3);
                cell.setCellFormula(builderFormulaSum(3, row_formula_start, curRow));
                cell.setCellStyle(csNum4B);

                cell = row.createCell(4);
                cell.setCellFormula(builderFormulaSum(4, row_formula_start, curRow));
                cell.setCellStyle(csNum4B);

                cell = row.createCell(5);
                cell.setCellFormula(builderFormulaSum(5, row_formula_start, curRow));
                cell.setCellStyle(csNum4B);

                cell = row.createCell(6);
                cell.setCellFormula(builderFormulaSum(6, row_formula_start, curRow));
                cell.setCellStyle(csDouble2B);

                cell = row.createCell(7);
                cell.setCellFormula(builderFormulaSum(7, row_formula_start, curRow));
                cell.setCellStyle(csDouble2B);

                cell = row.createCell(8);
                cell.setCellFormula(builderFormulaSum(9, row_formula_start, curRow));
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
                cell.setCellFormula(builderFormulaSum(15, row_formula_start, curRow));
                cell.setCellStyle(csDouble2B);

                cell = row.createCell(16);
                cell.setCellFormula(builderFormulaSum(16, row_formula_start, curRow));
                cell.setCellStyle(csDouble2B);

                workbookBase.write(out);
                out.close();
                Console.LOG(ConstantMessage.MSG_REPORT_SUCCESS, 1);

            }

            programeStatus.setMessage("สรุป_จ่าย_เดี่ยว+เครือ" + ConstantMessage.MSG_REPORT_SUCCESS);
            programeStatus.setTitle(ConstantMessage.MSG_REPORT_COMPLETE);
            programeStatus.setProcessStatus(true);

        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
            programeStatus.setMessage("สรุป_จ่าย_เดี่ยว+เครือ " + ConstantMessage.MSG_OPREFER_ERROR + ConstantMessage.MSG_PROCESS_FAILS + e.toString());
            programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
            programeStatus.setProcessStatus(false);
        }
        return programeStatus;
    }
    // #################end web site ################    
    /*
     *  end Single File Single Sheet **************************************************************************
     */

    public ProgrameStatus refer_genReportAllFunction(OppReport report) {
        boolean status = false;
        int intStatus = 0;
        //String serviceCode = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        try {

            report.setReportType(0);

            //referDAO.getConOracle();
            if (report.getFor_use() == 0) { // ทั่วไป user ดู

//                //----------------------------------------------------------------Detail-----------------------------------------------------------------//
                // รายละเอียด
                report.setReportType(0);

                //############################
                //หัก
                report.setCategory('D');
                System.out.println("service_code==>>" + report.getServiceCode());
                //เดี่ยว

                // MutiSheet1File
                report.setAttribute('S');
                //serviceCode = report.getServiceCode();
                programeStatus = this.refer_detailDeduct_singleClinic(report);
                if (programeStatus.getProcessStatus()) {
                    intStatus++;
                }

                //1Sheet1File
                //report.setServiceCode(serviceCode);
                this.refer_detailDeduct_singleClinic_1sheet1file(report);

                // เครือ
                // MutiSheet1File
                report.setAttribute('A');
                //report.setServiceCode(serviceCode);
                System.out.println("Start_ClinicHospital_report.getServiceCode()=====>>>" + report.getServiceCode());
                programeStatus = this.refer_detailDeduct_clinicAndHospital(report);
                if (programeStatus.getProcessStatus()) {
                    intStatus++;
                }
                //1Sheet1File
                //report.setServiceCode(serviceCode);
                this.refer_detailDeduct_clinicAndHospital_1sheet1file(report);

                // รวม เดี่ยว และเครือ
                // MutiSheet1File
                report.setAttribute('N');
                //report.setServiceCode(serviceCode);
                programeStatus = this.refer_detailDeduct(report);
                if (programeStatus.getProcessStatus()) {
                    intStatus++;
                }
                //1Sheet1File
                //report.setServiceCode(serviceCode);
                this.refer_detailDeduct_1sheet1file(report);

                //############################
                //จ่าย
                // MutiSheet1File
                report.setCategory('P');
                //report.setServiceCode(serviceCode);
                programeStatus = this.refer_detailPayment(report);
                if (programeStatus.getProcessStatus()) {
                    intStatus++;
                }
                //1Sheet1File
                // report.setServiceCode(serviceCode);
                this.refer_detailPayment_1sheet1file(report);
//                //------------------------------------------------------------------Summary---------------------------------------------------------------//
                //สรุป
                report.setReportType(1);

                //############################
                //หัก
                report.setCategory('D');
                //เดี่ยว
                report.setAttribute('S');
                report.setServiceCode("");
                programeStatus = this.refer_sumDeduct_singleClinic(report);
                if (programeStatus.getProcessStatus()) {
                    intStatus++;
                }
                // เครือ
                report.setAttribute('A');
                report.setServiceCode("");
                programeStatus = this.refer_sumDeduct_clinicAndHospital(report);
                if (programeStatus.getProcessStatus()) {
                    intStatus++;
                }
                // รวม เดี่ยว และเครือ
                report.setAttribute('N');
                report.setServiceCode("");
                programeStatus = this.refer_sumDeduct(report);
                if (programeStatus.getProcessStatus()) {
                    intStatus++;
                }
                //############################
                //จ่าย
                report.setCategory('P');
                report.setServiceCode("");
                programeStatus = this.refer_sumPayment(report);

                if (programeStatus.getProcessStatus()) {
                    intStatus++;
                }
            } else { // web site 
                // ##################### detail ################
                report.setReportType(0);
                report.setAttribute('N');

                report.setCategory('D');
                refer_detailDeduct_1sheet1file(report);
                report.setCategory('P');
                refer_detailPayment_1sheet1file(report);

                // ##################### end detail #############
                // ##################### sum ################
                report.setReportType(1);

                report.setAttribute('N');
                report.setCategory('D');
                refer_sumDeduct_1sheet1file_byHcode(report);
                report.setCategory('P');
                refer_sumPayment_1sheet1file_byHcode(report);
                // ##################### end sum #############
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

}
