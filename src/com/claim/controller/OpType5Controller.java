package com.claim.controller;

import com.claim.xls.ExcellBaseUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.util.List;
import com.claim.dao.OpType5DAO;
import com.claim.support.FunctionUtil;
import com.claim.object.HospitalService;
import com.claim.object.ObjRptType5Detail;
import com.claim.object.ObjRptType5Sum;
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

public class OpType5Controller extends ExcellBaseUtil {

    private FunctionUtil function;
    private OpType5DAO type5DAO;

    public OpType5Controller() {
        function = new FunctionUtil();
        type5DAO = new OpType5DAO();
    }

    // ############################ for weeb site ###############
    public ProgrameStatus type5_detail(OppReport report) {
        FileOutputStream out = null;
        FileInputStream file = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        String stmp = StringOpUtil.removeNull(report.getYearMonth()) + "-" + report.getNo();
        String pathDirectory = report.getPathFile() + ""+File.separator+"type5"+File.separator+"" + stmp;
        int col_last = 11;
        int row_start = 5; // index row
        int row_formula_start = row_start + 1;
        int col_txtid_width = col_last + 1;
        try {

            List<HospitalService> listHospital = type5DAO.getHospitalService(report);

            System.out.println("Hospital_size==" + listHospital.size());

            if (!listHospital.isEmpty()) {
                for (int j = 0; j < listHospital.size(); j++) {
                    HospitalService objHospital = listHospital.get(j);

                    if (report.getWeb_type5()) { // for web site claim
                        file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"TYPE5_detail_3.xls"));
                        new FunctionUtil().createMutiDirectory(new File(pathDirectory));
                        out = new FileOutputStream(pathDirectory + ""+File.separator+"ophc_" + objHospital.getHosCode() + "_" + stmp + ".xls");
                    } else { // for genaral use
                        if (report.getAttribute() == OpType5DAO.IS197_197) {  // 197
                            file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"TYPE5_detail_1.xls"));
                            out = new FileOutputStream(report.getPathFile() + ""+File.separator+"type5_detail_197_" + report.getYearMonth() + "-" + report.getNo() + "_" + objHospital.getHosHmain() + ".xls");
                        } else if (report.getAttribute() == OpType5DAO.IS197_HC || report.getAttribute() == OpType5DAO.IS197) {  //hc
                            file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"TYPE5_detail_3.xls"));
                            out = new FileOutputStream(report.getPathFile() + ""+File.separator+"type5_detail_hc_" + report.getYearMonth() + "-" + report.getNo() + "_" + objHospital.getHosCode() + ".xls");
                        }
                    }

                    HSSFWorkbook wbType5 = new HSSFWorkbook(file);
                    this.loadStyle(wbType5);

                    String EXCELL_HEADER = "";
                    String EXCELL_SERVICE = "";

                    if (report.getAttribute() == OpType5DAO.IS197_197) {
                        EXCELL_HEADER = "รายละเอียด การจ่ายชดเชยค่าบริการผู้ป่วยนอก  กรณีตรวจวินิจฉัยพิเศษ197รายการ   ปีงบประมาณ " + new DateUtil().getBudgeMonthYear_543(report.getBudget_stmp(),ConstantVariable.BUDGET_MONTH);;
                        EXCELL_SERVICE = "หน่วยบริการประจำ: " + objHospital.getHosHmainName() + " (" + objHospital.getHosHmain() + ")";
                    } else {  // hightcost ,hightcost + website
                        EXCELL_HEADER = "รายละเอียด  การจ่ายชดเชยค่าบริการผู้ป่วยนอก  กรณีค่าใช้จ่ายสูง(OPHC)  ปีงบประมาณ " + new DateUtil().getBudgeMonthYear_543(report.getBudget_stmp(),ConstantVariable.BUDGET_MONTH);
                        EXCELL_SERVICE = "หน่วยบริการประจำ: " + objHospital.getHosCodeName() + " (" + objHospital.getHosCode() + ")";
                    }

                    String EXCELL_MONTH = report.getTitle1();

                    HSSFSheet sheet = workbookBase.getSheetAt(0);
                    sheet.createFreezePane(4, 5);

                    // set columns width 
                    sheet.setColumnWidth(col_txtid_width, WIDTH_TXID);

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

                    // row 2
                    row = sheet.createRow(2);
                    row.setHeight((short) 390);
                    cell = row.createCell(0);
                    cell.setCellValue(EXCELL_SERVICE);
                    sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, col_last));
                    cell.setCellStyle(csHead);

                    //row 4 ตัวเนื้อหา
                    int curRow = row_start;
                    int i = 1;
                    ResultSet rs = null;

                    // ############## แก้ไข 2014-09-05 ####################
                    if (report.getAttribute() == OpType5DAO.IS197_197) {
                        // แสดง hcode หน้า รายงาน where hmain
                        report.setServiceCode(objHospital.getHosHmain());
                    } else if (report.getAttribute() == OpType5DAO.IS197_HC || report.getAttribute() == OpType5DAO.IS197) {
                        // แสดง hmain หน้า รายงาน where hcode
                        report.setServiceCode(objHospital.getHosCode());
                    }

                    List<ObjRptType5Detail> list_type5 = type5DAO.genReportType5_detail(report);
                    if (list_type5 != null) {
                        for (int k = 0; k < list_type5.size(); k++) {
                            ObjRptType5Detail obj_type = list_type5.get(k);

                            row = sheet.createRow(curRow);
                            row.setHeight((short) 360);
                            cell = row.createCell(0);
                            cell.setCellValue(i);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(1);
                            cell.setCellValue(function.subStringPid(obj_type.getPid()));
                            cell.setCellStyle(csStringPid);

                            cell = row.createCell(2);
                            cell.setCellValue(obj_type.getHn());
                            cell.setCellStyle(csNumH);

                            cell = row.createCell(3);
                            cell.setCellValue(obj_type.getP_name());
                            cell.setCellStyle(csStringLeft);

                            if (report.getAttribute() == OpType5DAO.IS197_HC || report.getAttribute() == OpType5DAO.IS197) { // by hcode

                                cell = row.createCell(4);
                                cell.setCellValue(obj_type.getHcode());
                                cell.setCellStyle(csStringLeft);

                            } else if (report.getAttribute() == OpType5DAO.IS197_197) {

                                cell = row.createCell(4);
                                cell.setCellValue(obj_type.getHmain());
                                cell.setCellStyle(csStringLeft);

                            }

                            cell = row.createCell(5);
                            cell.setCellValue(obj_type.getDate_th());
                            cell.setCellStyle(csString2Center);

                            cell = row.createCell(6);
                            cell.setCellValue(obj_type.getItem_code());
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(7);
                            cell.setCellValue(obj_type.getQty());
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(8);
                            cell.setCellValue(obj_type.getPrice_total());
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(9);
                            cell.setCellValue(obj_type.getStd_price());
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(10);
                            cell.setCellValue(obj_type.getTotal_reimburse());
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(11);
                            cell.setCellValue(obj_type.getInvoice_no());
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(12);
                            cell.setCellValue(obj_type.getTxid());
                            cell.setCellStyle(csStringtxid);

                            curRow++;
                            i++;
                        }

                        row = sheet.createRow(curRow);
                        row.setHeight((short) 450);
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
                        cell.setCellStyle(csNum4B);

                        cell = row.createCell(8);
                        cell.setCellFormula(builderFormulaSum(8, row_formula_start, curRow));
                        cell.setCellStyle(csDouble2B);

                        cell = row.createCell(9);
                        cell.setCellFormula(builderFormulaSum(9, row_formula_start, curRow));
                        cell.setCellStyle(csDouble2B);

                        cell = row.createCell(10);
                        cell.setCellFormula(builderFormulaSum(10, row_formula_start, curRow));
                        cell.setCellStyle(csDouble2B);

                        if (report.getAttribute() == OpType5DAO.IS197_197) {
                            // แสดง hcode หน้า รายงาน where hmain
                            workbookBase.setSheetName(0, objHospital.getHosHmain());
                            Console.LOG(Message.exportSuccess(objHospital.getHosHmain()), 1);
                        } else if (report.getAttribute() == OpType5DAO.IS197_HC || report.getAttribute() == OpType5DAO.IS197) {
                            // แสดง hmain หน้า รายงาน where hcode
                            workbookBase.setSheetName(0, objHospital.getHosCode());
                            Console.LOG(Message.exportSuccess(objHospital.getHosCode()), 1);
                        }

                        workbookBase.write(out);

                        out.close();
                        file.close();
                    }
                }

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
    // ############################ for weeb site ###############

    public ProgrameStatus type5_summary(OppReport report) {
        FileInputStream file = null;
        FileOutputStream out = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        int col_last = 5;
        int row_start = 4; // index row
        int row_formula_start = row_start + 1;
        int col_txtid_width = col_last + 1;
        try {
            if (report.getAttribute() == OpType5DAO.IS197_197) {
                file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"TYPE5_summary_1.xls"));
                out = new FileOutputStream(report.getPathFile() + ""+File.separator+"type5_summary_197_" + report.getYearMonth() + "-" + report.getNo() + ".xls");
            } else {  // hightcost , hightcost + website
                file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"TYPE5_summary_3.xls"));
                out = new FileOutputStream(report.getPathFile() + ""+File.separator+"type5_summary_hc_" + report.getYearMonth() + "-" + report.getNo() + ".xls");
            }

            HSSFWorkbook wbType5 = new HSSFWorkbook(file);
            this.loadStyle(wbType5);

            /**
             * End Style
             */
            String EXCELL_HEADER = "";

            if (report.getAttribute() == OpType5DAO.IS197_197) {
                EXCELL_HEADER = "สรุปการจ่ายชดเชยค่าบริการผู้ป่วยนอก  กรณีตรวจวินิจฉัยพิเศษ197รายการ   ปีงบประมาณ " + new DateUtil().getBudgeMonthYear_543(report.getBudget_stmp(),ConstantVariable.BUDGET_MONTH);
            } else {  // hight cost , website (197+hightcost)
                EXCELL_HEADER = "สรุปการจ่ายชดเชยค่าบริการผู้ป่วยนอก  กรณีค่าใช้จ่ายสูง(OPHC)  ปีงบประมาณ " + new DateUtil().getBudgeMonthYear_543(report.getBudget_stmp(),ConstantVariable.BUDGET_MONTH);
            }

            String EXCELL_MONTH = report.getTitle1();

            HSSFSheet sheet = workbookBase.getSheetAt(0);
            sheet.createFreezePane(3, 4);

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

            //row 4 ตัวเนื้อหา
            int curRow = row_start;
            int i = 1;
            ResultSet rs = null;

            List<ObjRptType5Sum> list_type5 = type5DAO.genReportType5_sum(report);

            for (int j = 0; j < list_type5.size(); j++) {

                ObjRptType5Sum obj_type5 = list_type5.get(j);

                row = sheet.createRow(curRow);
                row.setHeight((short) 360);
                cell = row.createCell(0);
                cell.setCellValue(i);
                cell.setCellStyle(csNum4);
                if (report.getAttribute() == OpType5DAO.IS197_HC) {
                    cell = row.createCell(1);
                    cell.setCellValue(obj_type5.getHcode());
                    cell.setCellStyle(csStringCenter);

                    cell = row.createCell(2);
                    cell.setCellValue(obj_type5.getHcode_name());
                    cell.setCellStyle(csStringLeft);
                } else if (report.getAttribute() == OpType5DAO.IS197_197) {

                    cell = row.createCell(1);
                    cell.setCellValue(obj_type5.getHmain());
                    cell.setCellStyle(csStringCenter);

                    cell = row.createCell(2);
                    cell.setCellValue(obj_type5.getHmain_name());
                    cell.setCellStyle(csStringLeft);
                }

                cell = row.createCell(3);
                cell.setCellValue(obj_type5.getCount_pid());
                cell.setCellStyle(csNum4);

                cell = row.createCell(4);
                cell.setCellValue(obj_type5.getCount_visit());
                cell.setCellStyle(csNum4);

                cell = row.createCell(5);
                cell.setCellValue(obj_type5.getSum_item());
                cell.setCellStyle(csNum4);

                cell = row.createCell(6);
                cell.setCellValue(obj_type5.getSum_price_total());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(7);
                cell.setCellValue(obj_type5.getSum_stdprice());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(8);
                cell.setCellValue(obj_type5.getSum_total_reimburse());
                cell.setCellStyle(csDouble2);

                curRow++;
                i++;
            }

            row = sheet.createRow(curRow);
            row.setHeight((short) 450);
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
            //cell.setCellFormula(builderFormula(7, row_formula_start, curRow));
            cell.setCellStyle(csNum4B);// csNum2B

            cell = row.createCell(8);
            cell.setCellFormula(builderFormulaSum(8, row_formula_start, curRow));
            cell.setCellStyle(csDouble2B);

            workbookBase.write(out);
            out.close();

            Console.LOG("รายงานสรุป ออกเรียบร้อยแล้วครับ", 1);
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

    public ProgrameStatus type5_genReportAllFunction(OppReport report) {
        boolean status = false;
        //String serviceCode = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        try {

            if (report.getFor_use() == 0) { // 0 = ทั่วไป
                //serviceCode = report.getServiceCode();

                //****************************************Detail**************************************
                report.setReportType(0);
                //report.setServiceCode(serviceCode);

                report.setAttribute(OpType5DAO.IS197_197); // 197 
                programeStatus = this.type5_detail(report);

                //report.setServiceCode(serviceCode);
                report.setAttribute(OpType5DAO.IS197_HC); // HighCost
                programeStatus = this.type5_detail(report);

                //****************************************Summary**************************************
                report.setReportType(1);
                //report.setServiceCode(serviceCode);

                report.setAttribute(OpType5DAO.IS197_197); // 197 
                programeStatus = this.type5_summary(report);
                //report.setServiceCode(serviceCode);
                report.setAttribute(OpType5DAO.IS197_HC); // HighCost
                programeStatus = this.type5_summary(report);

            } else { // 1 = for web site claim
                // ################### detail #####################
                report.setWeb_type5(true); // true = สำหรับออก รายงาน สำหรับ web site claim 
                report.setAttribute(OpType5DAO.IS197); // highcost  = 3 , 197 = 1  , highcost + 197 = 4'
                programeStatus = this.type5_detail(report);
                // ################### detail #####################
            }
        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
        }
        return programeStatus;
    }
}
