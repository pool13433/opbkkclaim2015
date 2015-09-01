package com.claim.controller;

import com.claim.xls.ExcellBaseUtil;
import com.claim.connection.DBManage;
import com.claim.dao.IndividualDAO;
import com.claim.object.HospitalService;
import com.claim.object.ObjRptIndividual;
import com.claim.object.ObjRptIndividualSum;
import com.claim.object.Obj_row_summary;
import com.claim.object.OppReport;
import com.claim.object.ResultBean;
import com.claim.support.FunctionUtil;
import com.claim.support.Console;
import com.claim.constants.ConstantMessage;
import com.claim.object.ProgrameStatus;
import com.claim.support.StringOpUtil;
import com.claim.ui.UiReportIndividual;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

public class OpIndividualController extends ExcellBaseUtil {

    private IndividualDAO indiDao;
    private FunctionUtil function;
    private Connection connection = null;

    public static String INDV_HEADER_DETAIL = "รายละเอียดการจ่ายชดเชยค่าข้อมูลผู้ป่วยนอกรายบุคคล ปีงบประมาณ 2558";
    public static String INDV_HEADER_SUM = "สรุปการจ่ายชดเชยค่าข้อมูลผู้ป่วยนอกรายบุคคล ปีงบประมาณ 2558";

    //contructur method
    public OpIndividualController() {
        indiDao = new IndividualDAO();
        function = new FunctionUtil();
    }

    public ProgrameStatus op_individual_detail_SplitFile(OppReport report) {
        FileOutputStream out = null;
        FileInputStream file = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        List<HospitalService> listHospital = new ArrayList<HospitalService>();
        List<ObjRptIndividual> indvList = null;

        int count_limit = 0;
        int col_last = 10;
        int row_start = 5; // index row
        int row_formula_start = row_start + 1;

        try {
            connection = new DBManage().open();
            indiDao.setConnection(connection);

            listHospital = indiDao.getHospitalService(report);

            System.out.println("Hospital_size==" + listHospital.size());

            if (!listHospital.isEmpty()) {

                for (int j = 0; j < listHospital.size(); j++) {
                    HospitalService objHospital = listHospital.get(j);

                    file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"indivi_detail.xls"));

                    out = new FileOutputStream(report.getPathFile() + ""+File.separator+"individual_detail_" + report.getYearMonth() + "-" + report.getNo() + "_" + listHospital.get(j).getHosCode() + ".xls");

                    HSSFWorkbook wbIndivi = new HSSFWorkbook(file);
                    this.loadStyle(wbIndivi);

                    //System.out.println("hospitalService.getHosCode===>>" + listHospital.get(j).getHosCode());
                    String EXCELL_HEADER = UiReportIndividual.txtTitle1.getText() != null ? UiReportIndividual.txtTitle1.getText() : INDV_HEADER_DETAIL;
                    String EXCELL_MONTH = UiReportIndividual.txtTitle2.getText() != null ? UiReportIndividual.txtTitle2.getText() : report.getTitle2();
                    String EXCELL_SERVICE = "หน่วยบริการ: " + objHospital.getHosCodeName() + " (" + objHospital.getHosCode() + ")";

                    HSSFSheet sheet = workbookBase.getSheetAt(0);
                    sheet.createFreezePane(3, row_start);
                    sheet.setColumnWidth((col_last + 1), WIDTH_TXID);

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

                    try {
                        report.setServiceCode(objHospital.getHosCode());
                        indvList = indiDao.getReportIndiviList(report);

                        for (int k = 0; k < indvList.size(); k++) {
                            ObjRptIndividual indvObj = indvList.get(k);

                            row = sheet.createRow(curRow);
                            row.setHeight((short) 360);
                            cell = row.createCell(0);
                            cell.setCellValue(i);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(1);
                            cell.setCellValue(function.subStringPid(indvObj.getPid()));
                            cell.setCellStyle(csStringPid);

                            cell = row.createCell(2);
                            cell.setCellValue(indvObj.getPname());
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(3);
                            cell.setCellValue(indvObj.getHn());
                            cell.setCellStyle(csString2Center);

                            cell = row.createCell(4);
                            cell.setCellValue(indvObj.getDateopd_thai_buddha());
                            cell.setCellStyle(csString2Center);

                            cell = row.createCell(5);
                            cell.setCellValue(indvObj.getSenddate_thai_buddha());
                            cell.setCellStyle(csString2Center);

                            cell = row.createCell(6);
                            cell.setCellValue(indvObj.getIndv_late());
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(7);
                            cell.setCellValue(indvObj.getOptypename());
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(8);
                            cell.setCellValue(indvObj.getMaininscl());
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(9);
                            cell.setCellValue(indvObj.getTotalreimburse());
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(10);
                            cell.setCellValue(indvObj.getInvoice_no());
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(11);
                            cell.setCellValue(indvObj.getTxid());
                            cell.setCellStyle(csStringtxid);

                            curRow++;
                            i++;
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                        Console.LOG(e.getMessage(), 0);
                        programeStatus.setMessage(ConstantMessage.MSG_PROCESS_FAILS + e.toString());
                        programeStatus.setTitle(ConstantMessage.MSG_CONTACT_ADMIN);
                        programeStatus.setProcessStatus(false);
                    }

                    row = sheet.createRow(curRow);
                    row.setHeight((short) 450);
                    cell = row.createCell(0);
                    cell.setCellValue("รวม");
                    sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 8));
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
                    cell.setCellStyle(csNum4B);

                    cell = row.createCell(8);
                    cell.setCellStyle(csNum4B);

                    cell = row.createCell(9);
                    cell.setCellFormula("SUM(J6:J" + (curRow) + ")");
                    cell.setCellStyle(csDouble2B);

                    // หมายเหตุ
//                    row = sheet.createRow(curRow + 2);
//                    row.setHeight((short) 450);
//                    cell = row.createCell(0);
//                    cell.setCellValue("หมายเหตุ :   เนื่องจากปีงบประมาณ 2557 สำนักงานหลักประกันสุขภาพแห่งชาติ เขต 13 กรุงเทพมหานคร  ได้นำทุก Visit  ของการจ่ายชดเชย "
//                            + "มารวมจ่ายในค่าข้อมูลด้วย จึงจ่ายชดเชยเพียงวันละ Visit เท่านั้น");
//                    sheet.addMergedRegion(new CellRangeAddress(curRow + 2, curRow + 3, 0, 9));
//                    cell.setCellStyle(csDetail2);
                    // ############## footer report ################
                    int footerIndex = (curRow + 2);
                    row = sheet.createRow(footerIndex);
                    //CellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol)
                    sheet.addMergedRegion(new CellRangeAddress(footerIndex, footerIndex, 0, 11));
                    cell = row.createCell(0);
                    cell.setCellValue(UiReportIndividual.txtFooter1.getText());
                    cell.setCellStyle(csStringLeft);

                    footerIndex = (curRow + 3);
                    row = sheet.createRow(footerIndex);
                    //CellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol)
                    sheet.addMergedRegion(new CellRangeAddress(footerIndex, footerIndex, 0, 11));
                    cell = row.createCell(0);
                    cell.setCellValue(UiReportIndividual.txtFooter2.getText());
                    cell.setCellStyle(csStringLeft);

                    footerIndex = (curRow + 4);
                    row = sheet.createRow(footerIndex);
                    //CellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol)
                    sheet.addMergedRegion(new CellRangeAddress(footerIndex, footerIndex, 0, 11));
                    cell = row.createCell(0);
                    cell.setCellValue(UiReportIndividual.txtFooter3.getText());
                    cell.setCellStyle(csStringLeft);
                    // ############## footer report ################

                    workbookBase.setSheetName(0, listHospital.get(j).getHosCode().toString());

                    workbookBase.write(out);

                    out.close();
                    file.close();
                    Console.LOG("ออกรายงาน: " + listHospital.get(j).getHosCode().toString() + "เรียบร้อย", 1);
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
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(OpIndividualController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return programeStatus;
    }

    // Console.LOG("กำลังออกรายงาน 1 ไฟล์ ต่อ 1 hcode อาจจะใช้เวลานาน กรุณารอ... ", 1);
    public ProgrameStatus op_individual_detail_SplitFile_caseLimitRowExcell(OppReport report) {        
        int col_last = 10;
        int row_start = 5; // index row
        int row_formula_start = row_start + 1;
        FileOutputStream out = null;
        FileInputStream file = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        List<HospitalService> listHospital = new ArrayList<HospitalService>();
        try {
            connection = new DBManage().open();
            indiDao.setConnection(connection);
            listHospital = indiDao.getHospitalService(report);

            System.out.println("Hospital_size==" + listHospital.size());

            if (!listHospital.isEmpty()) {

                for (int j = 0; j < listHospital.size(); j++) {

                    HospitalService service = listHospital.get(j);

                    report.setServiceCode(service.getHosCode());
                    report.setSql_orderBy(" ORDER BY hcode,pid,dateopd ASC");
                    List<ObjRptIndividual> listIndivi = indiDao.getReportIndiviList(report);

                    Map mapListIndi = spliteRecordData(listIndivi);

                    List<ObjRptIndividual> listIndi1 = (List<ObjRptIndividual>) mapListIndi.get(1);
                    List<ObjRptIndividual> listIndi2 = (List<ObjRptIndividual>) mapListIndi.get(2);

                    double totalReimburse = 0;
                    int i = 1;
                    //###################### case <= 65500 ######################

                    if (listIndi1 != null) {
                        file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"indivi_detail.xls"));

                        //out = new FileOutputStream(report.getPathFile() + ""+File.separator+"individual_detail_" + report.getYearMonth() + "-" + report.getNo() + "_" + listHospital.get(j).getHosCode() + "_1" + ".xls");
                        /*
                         modified by pool 16-06-2015
                         */
                        out = new FileOutputStream(report.getPathFile() + ""+File.separator+"indv_" + service.getHosCode() + "_" + report.getYearMonth() + "-" + report.getNo() + ".xls");

                        HSSFWorkbook wbIndivi = new HSSFWorkbook(file);
                        this.loadStyle(wbIndivi);

                        String EXCELL_HEADER = UiReportIndividual.txtTitle1.getText() != null ? UiReportIndividual.txtTitle1.getText() : INDV_HEADER_DETAIL;
                        String EXCELL_MONTH = UiReportIndividual.txtTitle2.getText() != null ? UiReportIndividual.txtTitle2.getText() : report.getTitle2();
                        String EXCELL_SERVICE = "หน่วยบริการ: " + listHospital.get(j).getHosCodeName() + " (" + listHospital.get(j).getHosCode() + ")";

                        HSSFSheet sheet = workbookBase.getSheetAt(0);
                        sheet.createFreezePane(3, 5);
                        sheet.setColumnWidth((col_last + 1), WIDTH_TXID);

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

                        for (ObjRptIndividual objIndi : listIndi1) {
                            row = sheet.createRow(curRow);
                            row.setHeight((short) 360);
                            cell = row.createCell(0);
                            cell.setCellValue(i);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(1);
                            cell.setCellValue(function.subStringPid(objIndi.getPid()));
                            cell.setCellStyle(csStringPid);

                            cell = row.createCell(2);
                            cell.setCellValue(objIndi.getPname());
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(3);
                            cell.setCellValue(objIndi.getHn());
                            cell.setCellStyle(csString2Center);

                            cell = row.createCell(4);
                            cell.setCellValue(objIndi.getDateopd_thai_buddha());
                            cell.setCellStyle(csString2Center);

                            cell = row.createCell(5);
                            cell.setCellValue(objIndi.getSenddate_thai_buddha());
                            cell.setCellStyle(csString2Center);

                            cell = row.createCell(6);
                            cell.setCellValue(objIndi.getIndv_late());
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(7);
                            cell.setCellValue(objIndi.getOptypename());
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(8);
                            cell.setCellValue(objIndi.getMaininscl());
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(9);
                            cell.setCellValue(objIndi.getTotalreimburse());
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(10);
                            cell.setCellValue(objIndi.getInvoice_no());
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(11);
                            cell.setCellValue(objIndi.getTxid());
                            cell.setCellStyle(csStringtxid);

                            totalReimburse = totalReimburse + objIndi.getTotalreimburse();

                            curRow++;
                            i++;
                        }

                        if (listIndi2.isEmpty()) {
                            row = sheet.createRow(curRow);
                            row.setHeight((short) 450);
                            cell = row.createCell(0);
                            cell.setCellValue("รวม");
                            sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 8));
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
                            cell.setCellStyle(csNum4B);

                            cell = row.createCell(8);
                            cell.setCellStyle(csNum4B);

                            cell = row.createCell(9);
                            cell.setCellFormula(builderFormulaSum(9, row_formula_start, curRow));
                            cell.setCellFormula("SUM(J6:J" + (curRow) + ")");
                            cell.setCellStyle(csDouble2B);
                        }

                        // หมายเหตุ
//                        row = sheet.createRow(curRow + 2);
//                        row.setHeight((short) 450);
//                        cell = row.createCell(0);
//                        cell.setCellValue("หมายเหตุ :   เนื่องจากปีงบประมาณ 2557 สำนักงานหลักประกันสุขภาพแห่งชาติ เขต 13 กรุงเทพมหานคร  ได้นำทุก Visit  ของการจ่ายชดเชย "
//                                + "มารวมจ่ายในค่าข้อมูลด้วย จึงจ่ายชดเชยเพียงวันละ Visit เท่านั้น");
//                        sheet.addMergedRegion(new CellRangeAddress(curRow + 2, curRow + 3, 0, 9));
//                        cell.setCellStyle(csDetail2);
                        // ############## footer report ################
                        int footerIndex = (curRow + 2);
                        row = sheet.createRow(footerIndex);
                        //CellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol)
                        sheet.addMergedRegion(new CellRangeAddress(footerIndex, footerIndex, 0, 11));
                        cell = row.createCell(0);
                        cell.setCellValue(UiReportIndividual.txtFooter1.getText());
                        cell.setCellStyle(csStringLeft);

                        footerIndex = (curRow + 3);
                        row = sheet.createRow(footerIndex);
                        //CellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol)
                        sheet.addMergedRegion(new CellRangeAddress(footerIndex, footerIndex, 0, 11));
                        cell = row.createCell(0);
                        cell.setCellValue(UiReportIndividual.txtFooter2.getText());
                        cell.setCellStyle(csStringLeft);

                        footerIndex = (curRow + 4);
                        row = sheet.createRow(footerIndex);
                        //CellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol)
                        sheet.addMergedRegion(new CellRangeAddress(footerIndex, footerIndex, 0, 11));
                        cell = row.createCell(0);
                        cell.setCellValue(UiReportIndividual.txtFooter3.getText());
                        cell.setCellStyle(csStringLeft);
                        // ############## footer report ################

                        workbookBase.setSheetName(0, listHospital.get(j).getHosCode().toString());

                        workbookBase.write(out);

                        out.close();
                        file.close();
                    }

                    //#######################################################
                    //###################### case > 65500 ######################
                    if (!listIndi2.isEmpty()) {

                        file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"indivi_detail.xls"));

                        //out = new FileOutputStream(report.getPathFile() + ""+File.separator+"individual_detail_" + report.getYearMonth() + "-" + report.getNo() + "_" + listHospital.get(j).getHosCode() + "_2" + ".xls");
                        /*
                         modified by pool 16-06-2015
                         */
                        out = new FileOutputStream(report.getPathFile() + ""+File.separator+"indv_" + listHospital.get(j).getHosCode() + "_" + report.getYearMonth() + "-" + report.getNo() + "_2" + ".xls");

                        HSSFWorkbook wbIndivi = new HSSFWorkbook(file);
                        this.loadStyle(wbIndivi);

                        //String EXCELL_HEADER = "รายละเอียดการจ่ายชดเชยค่าข้อมูลผู้ป่วยนอกรายบุคคล ปีงบประมาณ 2557";
                        String EXCELL_HEADER = UiReportIndividual.txtTitle1.getText() != null ? UiReportIndividual.txtTitle1.getText() : INDV_HEADER_DETAIL;
                        String EXCELL_MONTH = report.getTitle2();
                        String EXCELL_SERVICE = "หน่วยบริการ: " + listHospital.get(j).getHosCodeName() + " (" + listHospital.get(j).getHosCode() + ")";

                        HSSFSheet sheet = workbookBase.getSheetAt(0);
                        sheet.createFreezePane(3, 5);
                        sheet.setColumnWidth((col_last+1), WIDTH_TXID);

                        HSSFCell cell = null;
                        HSSFRow row = null;

                        // row 0
                        row = sheet.createRow(0);
                        row.setHeight((short) 390);
                        cell = row.createCell(0);
                        cell.setCellValue(EXCELL_HEADER);
                        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));
                        cell.setCellStyle(csHead);

                        // row 1
                        row = sheet.createRow(1);
                        row.setHeight((short) 390);
                        cell = row.createCell(0);
                        cell.setCellValue(EXCELL_MONTH);
                        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 9));
                        cell.setCellStyle(csHead);

                        // row 2
                        row = sheet.createRow(2);
                        row.setHeight((short) 390);
                        cell = row.createCell(0);
                        cell.setCellValue(EXCELL_SERVICE);
                        sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 9));
                        cell.setCellStyle(csHead);

                        //row 4 ตัวเนื้อหา
                        int curRow = 5;
                        //int i = 1;

                        for (ObjRptIndividual objIndi : listIndi2) {
                            row = sheet.createRow(curRow);
                            row.setHeight((short) 360);
                            cell = row.createCell(0);
                            cell.setCellValue(i);
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(1);
                            cell.setCellValue(function.subStringPid(objIndi.getPid()));
                            cell.setCellStyle(csStringPid);

                            cell = row.createCell(2);
                            cell.setCellValue(objIndi.getPname());
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(3);
                            cell.setCellValue(objIndi.getHn());
                            cell.setCellStyle(csString2Center);

                            cell = row.createCell(4);
                            cell.setCellValue(objIndi.getDateopd_thai_buddha());
                            cell.setCellStyle(csString2Center);

                            cell = row.createCell(5);
                            cell.setCellValue(objIndi.getSenddate_thai_buddha());
                            cell.setCellStyle(csString2Center);

                            cell = row.createCell(6);
                            cell.setCellValue(objIndi.getIndv_late());
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(7);
                            cell.setCellValue(objIndi.getOptypename());
                            cell.setCellStyle(csNum4);

                            cell = row.createCell(8);
                            cell.setCellValue(objIndi.getMaininscl());
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(9);
                            cell.setCellValue(objIndi.getTotalreimburse());
                            cell.setCellStyle(csDouble2);

                            cell = row.createCell(10);
                            cell.setCellValue(objIndi.getInvoice_no());
                            cell.setCellStyle(csStringLeft);

                            cell = row.createCell(11);
                            cell.setCellValue(objIndi.getTxid());
                            cell.setCellStyle(csStringtxid);

                            totalReimburse = totalReimburse + objIndi.getTotalreimburse();

                            curRow++;
                            i++;
                        }

                        row = sheet.createRow(curRow);
                        row.setHeight((short) 450);
                        cell = row.createCell(0);
                        cell.setCellValue("รวม");
                        sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 8));
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
                        cell.setCellStyle(csNum4B);

                        cell = row.createCell(8);
                        cell.setCellStyle(csNum4B);

                        cell = row.createCell(9);
                        cell.setCellValue(totalReimburse);
                        cell.setCellStyle(csDouble2B);

                        // หมายเหตุ
//                        row = sheet.createRow(curRow + 2);
//                        row.setHeight((short) 450);
//                        cell = row.createCell(0);
//                        cell.setCellValue("หมายเหตุ :   เนื่องจากปีงบประมาณ 2557 สำนักงานหลักประกันสุขภาพแห่งชาติ เขต 13 กรุงเทพมหานคร  ได้นำทุก Visit  ของการจ่ายชดเชย "
//                                + "มารวมจ่ายในค่าข้อมูลด้วย จึงจ่ายชดเชยเพียงวันละ Visit เท่านั้น");
//                        sheet.addMergedRegion(new CellRangeAddress(curRow + 2, curRow + 3, 0, 9));
//                        cell.setCellStyle(csDetail2);
                        // ############## footer report ################
                        int footerIndex = (curRow + 2);
                        row = sheet.createRow(footerIndex);
                        //CellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol)
                        sheet.addMergedRegion(new CellRangeAddress(footerIndex, footerIndex, 0, 11));
                        cell = row.createCell(0);
                        cell.setCellValue(UiReportIndividual.txtFooter1.getText());
                        cell.setCellStyle(csStringLeft);

                        footerIndex = (curRow + 3);
                        row = sheet.createRow(footerIndex);
                        //CellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol)
                        sheet.addMergedRegion(new CellRangeAddress(footerIndex, footerIndex, 0, 11));
                        cell = row.createCell(0);
                        cell.setCellValue(UiReportIndividual.txtFooter2.getText());
                        cell.setCellStyle(csStringLeft);

                        footerIndex = (curRow + 4);
                        row = sheet.createRow(footerIndex);
                        //CellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol)
                        sheet.addMergedRegion(new CellRangeAddress(footerIndex, footerIndex, 0, 11));
                        cell = row.createCell(0);
                        cell.setCellValue(UiReportIndividual.txtFooter3.getText());
                        cell.setCellStyle(csStringLeft);
                        // ############## footer report ################

                        workbookBase.setSheetName(0, listHospital.get(j).getHosCode().toString());

                        workbookBase.write(out);

                        out.close();
                        file.close();
                    }
                    //#######################################################

                    Console.LOG("ออกรายงาน: " + listHospital.get(j).getHosCode().toString() + "เรียบร้อย", 1);
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

    // Console.LOG("กำลังออกรายงาน 1 ไฟล์ ทุก hcode อาจจะใช้เวลานาน กรุณารอ... ", 1);
    public ProgrameStatus op_individual_detail_allcode_spliteFile_65500(OppReport report) {
        FileOutputStream out = null;
        FileInputStream file = null;
        Obj_row_summary obj_row = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        int row_content = 1;
        int file_no = 0;
        int row_data = 1;
         int col_last = 11;
         int row_start = 5; // index row
        HSSFWorkbook wbIndivi = null;
        HSSFSheet sheet = null;
        int curRow = 0;
        try {

            connection = new DBManage().open();
            indiDao.setConnection(connection);
            
            report.setSql_orderBy(" ORDER BY hcode,pid,dateopd ASC");
            List<ObjRptIndividual> list_indivi = indiDao.getReportIndiviList(report);
            obj_row = new Obj_row_summary();

            // loop
            for (int i = 0; i < list_indivi.size(); i++) {
                ObjRptIndividual objIndi = list_indivi.get(i);

                // #################### variable ###################
                HSSFCell cell = null;
                HSSFRow row = null;
                // #################### end  variable ##############

                // new file excell
                if (row_content >= 1 && row_content <= ROW_LIMIT) {  // limit excell row 65,536 rows by 256 columns

                    // #################### header report ############
                    if (row_content == 1) {
                        System.out.println("row_content == 1 : ");
                        file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"indivi_detail_hcode.xls"));

                        out = new FileOutputStream(report.getPathFile() + ""+File.separator+"individual_detail_" + report.getYearMonth() + "-" + report.getNo() + "_file_" + (file_no + 1) + "" + ".xls");

                        String EXCELL_HEADER = UiReportIndividual.txtTitle1.getText() != null ? UiReportIndividual.txtTitle1.getText() : INDV_HEADER_DETAIL;
                        String EXCELL_MONTH = UiReportIndividual.txtTitle2.getText() != null ? UiReportIndividual.txtTitle2.getText() : report.getTitle2();

                        // ################### valiable #################
                        wbIndivi = new HSSFWorkbook(file);
                        this.loadStyle(wbIndivi);

                        sheet = workbookBase.getSheetAt(0);
                        sheet.createFreezePane(3, 5);
                        sheet.setColumnWidth((col_last + 1), WIDTH_TXID);

                        // ################### valiable #################
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
                        curRow = row_start;
                    }
                    // ####################end header report #########               

                    // #################### content report ############                    
                    row = sheet.createRow(curRow);
                    row.setHeight((short) 360);
                    cell = row.createCell(0);
                    cell.setCellValue(row_data);
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(1);
                    cell.setCellValue(objIndi.getHcode() + " : " + objIndi.getHcodename());
                    cell.setCellStyle(csStringPid);

                    cell = row.createCell(2);
                    cell.setCellValue(function.subStringPid(objIndi.getPid()));
                    cell.setCellStyle(csStringPid);

                    cell = row.createCell(3);
                    cell.setCellValue(objIndi.getPname());
                    cell.setCellStyle(csStringLeft);

                    cell = row.createCell(4);
                    cell.setCellValue(objIndi.getHn());
                    cell.setCellStyle(csString2Center);

                    cell = row.createCell(5);
                    cell.setCellValue(objIndi.getDateopd_thai_buddha());
                    cell.setCellStyle(csString2Center);

                    cell = row.createCell(6);
                    cell.setCellValue(objIndi.getSenddate_thai_buddha());
                    cell.setCellStyle(csString2Center);

                    cell = row.createCell(7);
                    cell.setCellValue(objIndi.getIndv_late());
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(8);
                    cell.setCellValue(objIndi.getOptypename());
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(9);
                    cell.setCellValue(objIndi.getMaininscl());
                    cell.setCellStyle(csStringLeft);

                    cell = row.createCell(10);
                    cell.setCellValue(objIndi.getTotalreimburse());
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(11);
                    cell.setCellValue(objIndi.getInvoice_no());
                    cell.setCellStyle(csStringLeft);

                    cell = row.createCell(12);
                    cell.setCellValue(objIndi.getTxid());
                    cell.setCellStyle(csStringtxid);

                    // ###################### sum total ##########
                    obj_row.setCell_K(obj_row.getCell_K() + objIndi.getTotalreimburse());
                    // ######################end sum total #######

                    // #################### end content report ########
                    if (row_content == ROW_LIMIT) {

                        if (row_data == list_indivi.size()) {
                            System.out.println("row_data == list_indivi.size() :");
                            // #################### footer report ############
                            if (list_indivi.size() / ROW_LIMIT == file_no) {

                                System.out.println("list_indivi.size() / ROW_LIMIT : ");
                                curRow++;

                                System.out.println("curRow : ");

                                row = sheet.createRow(curRow);
                                row.setHeight((short) 450);
                                cell = row.createCell(0);
                                cell.setCellValue("รวม");
                                sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 8));
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
                                cell.setCellStyle(csNum4B);

                                cell = row.createCell(8);
                                cell.setCellStyle(csNum4B);

                                cell = row.createCell(9);
                                cell.setCellStyle(csNum4B);

                                cell = row.createCell(10);
                                cell.setCellValue(obj_row.getCell_K());
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
                            Console.LOG(" ออกรายงานไฟล์ ที่ :" + file_no + " สำเร็จเรียบร้อยแล้ว ", 1);
                        } else {
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
                            Console.LOG(" ออกรายงานไฟล์ ที่ :" + file_no + " สำเร็จเรียบร้อยแล้ว ", 1);
                            row_data++;
                        }
                    } else {

                        if (row_data == list_indivi.size()) {
                            System.out.println("row_data == list_indivi.size() :");

                            // #################### footer report ############
                            if (list_indivi.size() / ROW_LIMIT == file_no) {

                                System.out.println("list_indivi.size() / ROW_LIMIT : ");
                                curRow++;

                                System.out.println("curRow : ");

                                row = sheet.createRow(curRow);
                                row.setHeight((short) 450);
                                cell = row.createCell(0);
                                cell.setCellValue("รวม");
                                sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 8));
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
                                cell.setCellStyle(csNum4B);

                                cell = row.createCell(8);
                                cell.setCellStyle(csNum4B);

                                cell = row.createCell(9);
                                cell.setCellStyle(csNum4B);

                                cell = row.createCell(10);
                                cell.setCellValue(obj_row.getCell_K());
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
                            Console.LOG("อออกรายงานไฟล์ ที่ :" + file_no + " สำเร็จเรียบร้อยแล้ว ", 1);
                        }

                        curRow++;
                        row_content++;
                        row_data++;
                    }

//                    System.out.println("curRow  : " + curRow);
//                    System.out.println("row_content : " + row_content);
//                    System.out.println("row_data : " + row_data);
                } // end if
            } // end for

            programeStatus.setMessage(ConstantMessage.MSG_REPORT_SUCCESS);
            programeStatus.setTitle(ConstantMessage.MSG_REPORT_COMPLETE);
            programeStatus.setProcessStatus(true);
        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
            programeStatus.setMessage(ConstantMessage.MSG_PROCESS_FAILS + e.getMessage());
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
    public ProgrameStatus op_individual_detail_byHcode_1fileMutiSheet(OppReport report) {
        FileOutputStream out = null;
        FileInputStream file = null;
        Obj_row_summary obj_row = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        List<HospitalService> listHospital = new ArrayList<HospitalService>();
        String stmp = StringOpUtil.removeNull(report.getYearMonth()) + "-" + report.getNo();
        String pathDirectory = report.getPathFile() + ""+File.separator+"indv"+File.separator+"" + stmp;
        int col_last = 11;
        int row_start = 5; // index row
        int row_formula_start = row_start + 1;
        try {
            connection = new DBManage().open();
            indiDao.setConnection(connection);
            listHospital = indiDao.getHospitalService(report);

            // ############## loop list hospital service #############
            for (int i = 0; i < listHospital.size(); i++) {
                HospitalService objService = listHospital.get(i);

                // ###################### FileInputStream ####################
                HSSFWorkbook wbIndivi = null;

                file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"indivi_detail_hcode.xls"));

                new FunctionUtil().createMutiDirectory(new File(pathDirectory));
                out = new FileOutputStream(pathDirectory + ""+File.separator+"indv_" + objService.getHosCode() + "_" + stmp + ".xls");
                // ###################### FileInputStream ####################

                wbIndivi = new HSSFWorkbook(file);
                this.loadStyle(wbIndivi);

                report.setServiceCode(objService.getHosCode());
                report.setSql_orderBy(" ORDER BY hcode,pid,dateopd ASC");
                List<ObjRptIndividual> list_indivi = indiDao.getReportIndiviList(report);

                System.out.println(" หน่วยบริการ : " + objService.getHosCode() + " จำนวน : " + list_indivi.size());

                // ################### Global Variable ##############
                int row_content = 1;
                int file_no = 0;
                int row_data = 1;
                HSSFSheet sheet = null;
                int curRow = 0;
                obj_row = new Obj_row_summary();
                // ###################End Global Variable ###########

                for (int j = 0; j < list_indivi.size(); j++) {
                    ObjRptIndividual objIndi = list_indivi.get(j);

                    HSSFCell cell = null;
                    HSSFRow row = null;

                    //####################### File Excell Sheet 1 ##################                                                            
                    if (row_content >= 1 && row_content <= ROW_LIMIT) {  // limit excell row 65,536 rows by 256 columns

                        // #################### header report ############
                        if (row_content == 1) {

                            String EXCELL_HEADER = UiReportIndividual.txtTitle1.getText() != null ? UiReportIndividual.txtTitle1.getText() : INDV_HEADER_DETAIL;
                            String EXCELL_MONTH = UiReportIndividual.txtTitle2.getText() != null ? UiReportIndividual.txtTitle2.getText() : report.getTitle2();
                            String EXCELL_SERVICE = "หน่วยบริการ: " + objService.getHosCodeName() + " (" + objService.getHosCode() + ")";

                            // ################### valiable #################
                            sheet = workbookBase.cloneSheet(0);
                            sheet.createFreezePane(3, 5);
                            sheet.setColumnWidth((col_last + 1), WIDTH_TXID);

                            // ################### valiable #################
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
                            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, col_last));
                            cell.setCellStyle(csHead);

                            //row 4 ตัวเนื้อหา
                            curRow = row_start;

                            System.out.println(" Write Sheet " + file_no);
                        }
                        // ####################end header report #########  

                        // #################### content header report ############                    
                        row = sheet.createRow(curRow);
                        row.setHeight((short) 360);
                        cell = row.createCell(0);
                        cell.setCellValue(row_data);
                        cell.setCellStyle(csNum4);

                        cell = row.createCell(1);
                        cell.setCellValue(objIndi.getHcode() + " : " + objIndi.getHcodename());
                        cell.setCellStyle(csStringPid);

                        cell = row.createCell(2);
                        cell.setCellValue(function.subStringPid(objIndi.getPid()));
                        cell.setCellStyle(csStringPid);

                        cell = row.createCell(3);
                        cell.setCellValue(objIndi.getPname());
                        cell.setCellStyle(csStringLeft);

                        cell = row.createCell(4);
                        cell.setCellValue(objIndi.getHn());
                        cell.setCellStyle(csString2Center);

                        cell = row.createCell(5);
                        cell.setCellValue(objIndi.getDateopd_thai_buddha());
                        cell.setCellStyle(csString2Center);

                        cell = row.createCell(6);
                        cell.setCellValue(objIndi.getSenddate_thai_buddha());
                        cell.setCellStyle(csString2Center);

                        cell = row.createCell(7);
                        cell.setCellValue(objIndi.getIndv_late());
                        cell.setCellStyle(csNum4);

                        cell = row.createCell(8);
                        cell.setCellValue(objIndi.getOptypename());
                        cell.setCellStyle(csNum4);

                        cell = row.createCell(9);
                        cell.setCellValue(objIndi.getMaininscl());
                        cell.setCellStyle(csStringLeft);

                        cell = row.createCell(10);
                        cell.setCellValue(objIndi.getTotalreimburse());
                        cell.setCellStyle(csDouble2);

                        cell = row.createCell(11);
                        cell.setCellValue(objIndi.getInvoice_no());
                        cell.setCellStyle(csStringLeft);

                        cell = row.createCell(12);
                        cell.setCellValue(objIndi.getTxid());
                        cell.setCellStyle(csStringtxid);

                        // ###################### sum total ##########
                        obj_row.setCell_K(obj_row.getCell_K() + objIndi.getTotalreimburse());
                        // ######################end sum total #######

                        if (row_content == ROW_LIMIT) {
                            System.out.println("row_content == 65500 : " + row_content);

                            if (row_data == list_indivi.size()) {
                                System.out.println("row_data == list_indivi.size() :" + row_data);
                                // #################### footer report ############
                                if (list_indivi.size() / ROW_LIMIT == file_no) {

                                    System.out.println("list_indivi.size() / ROW_LIMIT : ");
                                    curRow++;

                                    row = sheet.createRow(curRow);
                                    row.setHeight((short) 450);
                                    cell = row.createCell(0);
                                    cell.setCellValue("รวม");
                                    sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 8));
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
                                    cell.setCellValue(obj_row.getCell_K());
                                    cell.setCellStyle(csDouble2B);

                                    // ############## footer report ################
                                    int footerIndex = (curRow + 2);
                                    row = sheet.createRow(footerIndex);
                                    //CellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol)
                                    sheet.addMergedRegion(new CellRangeAddress(footerIndex, footerIndex, 0, 11));
                                    cell = row.createCell(0);
                                    cell.setCellValue(UiReportIndividual.txtFooter1.getText());
                                    cell.setCellStyle(csStringLeft);

                                    footerIndex = (curRow + 3);
                                    row = sheet.createRow(footerIndex);
                                    //CellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol)
                                    sheet.addMergedRegion(new CellRangeAddress(footerIndex, footerIndex, 0, 11));
                                    cell = row.createCell(0);
                                    cell.setCellValue(UiReportIndividual.txtFooter2.getText());
                                    cell.setCellStyle(csStringLeft);

                                    footerIndex = (curRow + 4);
                                    row = sheet.createRow(footerIndex);
                                    //CellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol)
                                    sheet.addMergedRegion(new CellRangeAddress(footerIndex, footerIndex, 0, 11));
                                    cell = row.createCell(0);
                                    cell.setCellValue(UiReportIndividual.txtFooter3.getText());
                                    cell.setCellStyle(csStringLeft);
                                    // ############## footer report ################
                                }
                            // #################### end footer report #########                            

                                // เริ่ม รัน ค่าไหม่
                                row_content = 1;
                                // เริ่ม row ของเอกสารไหม่ ที่ 5
                                curRow = 5;
                                // บวก จำนวนไฟล์ เพิ่ม 1
                                file_no = file_no + 1;
                                workbookBase.setSheetName(file_no, String.valueOf(file_no));
                            } else {

                                // เริ่ม รัน ค่าไหม่
                                row_content = 1;
                                // เริ่ม row ของเอกสารไหม่ ที่ 5
                                curRow = 5;
                                // บวก จำนวนไฟล์ เพิ่ม 1
                                file_no = file_no + 1;

                                row_data++;
                                workbookBase.setSheetName(file_no, String.valueOf(file_no));
                            }
                        } else {

                            if (row_data == list_indivi.size()) {
                                System.out.println("row_data == list_indivi.size() :");

                                // #################### footer report ############
                                if (list_indivi.size() / ROW_LIMIT == file_no) {

                                    System.out.println("list_indivi.size() / ROW_LIMIT : ");
                                    curRow++;

                                    System.out.println("curRow : ");

                                    row = sheet.createRow(curRow);
                                    row.setHeight((short) 450);
                                    cell = row.createCell(0);
                                    cell.setCellValue("รวม");
                                    sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 8));
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
                                    cell.setCellValue(obj_row.getCell_K());
                                    cell.setCellStyle(csDouble2B);

                                    // ############## footer report ################
                                    int footerIndex = (curRow + 2);
                                    row = sheet.createRow(footerIndex);
                                    //CellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol)
                                    sheet.addMergedRegion(new CellRangeAddress(footerIndex, footerIndex, 0, 11));
                                    cell = row.createCell(0);
                                    cell.setCellValue(UiReportIndividual.txtFooter1.getText());
                                    cell.setCellStyle(csStringLeft);

                                    footerIndex = (curRow + 3);
                                    row = sheet.createRow(footerIndex);
                                    //CellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol)
                                    sheet.addMergedRegion(new CellRangeAddress(footerIndex, footerIndex, 0, 11));
                                    cell = row.createCell(0);
                                    cell.setCellValue(UiReportIndividual.txtFooter2.getText());
                                    cell.setCellStyle(csStringLeft);

                                    footerIndex = (curRow + 4);
                                    row = sheet.createRow(footerIndex);
                                    //CellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol)
                                    sheet.addMergedRegion(new CellRangeAddress(footerIndex, footerIndex, 0, 11));
                                    cell = row.createCell(0);
                                    cell.setCellValue(UiReportIndividual.txtFooter3.getText());
                                    cell.setCellStyle(csStringLeft);
                                    // ############## footer report ################
                                }
                            // #################### end footer report #########                          

                                // เริ่ม รัน ค่าไหม่
                                row_content = 1;
                                // เริ่ม row ของเอกสารไหม่ ที่ 5
                                curRow = 5;
                                // บวก จำนวนไฟล์ เพิ่ม 1
                                file_no = file_no + 1;

                                workbookBase.setSheetName(file_no, String.valueOf(file_no));
                            }

                            curRow++;
                            row_content++;
                            row_data++;
                        }

                    }

                    // #################### end content header report ########
                }
                // ################## write file ########################## 
                // write file 
                workbookBase.removeSheetAt(0);
                workbookBase.write(out);

                out.close();
                file.close();
                Console.LOG("ออกรายงานหน่วยบริการ : " + objService.getHosCode() + " เรียบร้อยแล้ว", 1);
                programeStatus.setMessage(ConstantMessage.MSG_REPORT_SUCCESS);
                programeStatus.setTitle(ConstantMessage.MSG_REPORT_COMPLETE);
                programeStatus.setProcessStatus(true);
                // ################## write file ##########################
            }
        } catch (Exception e) {
            e.printStackTrace();
            Console.LOG(e.getMessage(), 0);
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

    public ProgrameStatus op_individual_sum(OppReport report) {
        Connection connection = null;
        FileInputStream file = null;
        FileOutputStream out = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        try {
            connection = new DBManage().open();
            indiDao.setConnection(connection);
            List<ObjRptIndividualSum> indvSumList = indiDao.getIndividualSum(report);

            if (indvSumList != null) {

                file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"indivi_summary.xls"));

                out = new FileOutputStream(report.getPathFile() + ""+File.separator+"indevidual_summary.xls");

                HSSFWorkbook wbIndivi = new HSSFWorkbook(file);
                this.loadStyle(wbIndivi);

                String EXCELL_HEADER = UiReportIndividual.txtTitle1.getText() != null ? UiReportIndividual.txtTitle1.getText() : INDV_HEADER_SUM;//
                String EXCELL_MONTH = UiReportIndividual.txtTitle2.getText() != null ? UiReportIndividual.txtTitle2.getText() : report.getTitle2();

                HSSFSheet sheet = workbookBase.getSheetAt(0);
                sheet.createFreezePane(3, 5);

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

                for (int i = 0; i < indvSumList.size(); i++) {
                    ObjRptIndividualSum indvSumObj = indvSumList.get(i);

                    row = sheet.createRow(curRow);
                    row.setHeight((short) 360);
                    cell = row.createCell(0);
                    cell.setCellValue(indvSumObj.getNo());
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(1);
                    cell.setCellValue(indvSumObj.getHcode());
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(2);
                    cell.setCellValue(indvSumObj.getHcodename());
                    cell.setCellStyle(csStringLeft);

                    cell = row.createCell(3);
                    cell.setCellValue(indvSumObj.getIntime_man());
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(4);
                    cell.setCellValue(indvSumObj.getIntime_visit());
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(5);
                    cell.setCellValue(indvSumObj.getIntime_totalreimburse());
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(6);
                    cell.setCellValue(indvSumObj.getLate_man());
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(7);
                    cell.setCellValue(indvSumObj.getLate_visit());
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(8);
                    cell.setCellValue(indvSumObj.getLate_totalreimburse());
                    cell.setCellStyle(csDouble2);

                    cell = row.createCell(9);
                    cell.setCellValue(indvSumObj.getTotalall_man());
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(10);
                    cell.setCellValue(indvSumObj.getTotalall_visit());
                    cell.setCellStyle(csNum4);

                    cell = row.createCell(11);
                    cell.setCellValue(indvSumObj.getTotalall_totalreimburse());
                    cell.setCellStyle(csDouble2);

                    curRow++;

                }

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

                // setValues From Database
                ResultBean beanSum = indiDao.sum_fromSummaryIndividual(report);

                cell = row.createCell(3);
                //cell.setCellFormula("SUM(D6:D" + (curRow) + ")");
                cell.setCellValue(Integer.parseInt(beanSum.getObject1().toString()));
                cell.setCellStyle(csNum4B);

                cell = row.createCell(4);
                //cell.setCellFormula("SUM(E6:E" + (curRow) + ")");
                cell.setCellValue(Integer.parseInt(beanSum.getObject2().toString()));
                cell.setCellStyle(csNum4B);

                cell = row.createCell(5);
                cell.setCellFormula("SUM(F6:F" + (curRow) + ")");
                cell.setCellStyle(csDouble2B);

                cell = row.createCell(6);
                //cell.setCellFormula("SUM(G6:G" + (curRow) + ")");
                cell.setCellValue(Integer.parseInt(beanSum.getObject3().toString()));
                cell.setCellStyle(csNum4B);

                cell = row.createCell(7);
                //cell.setCellFormula("SUM(H6:H" + (curRow) + ")");
                cell.setCellValue(Integer.parseInt(beanSum.getObject4().toString()));
                cell.setCellStyle(csNum4B);

                cell = row.createCell(8);
                cell.setCellFormula("SUM(I6:I" + (curRow) + ")");
                cell.setCellStyle(csDouble2B);

                cell = row.createCell(9);
                //cell.setCellFormula("SUM(J6:J" + (curRow) + ")");
                cell.setCellValue(Integer.parseInt(beanSum.getObject5().toString()));
                cell.setCellStyle(csNum4B);

                cell = row.createCell(10);
                //cell.setCellFormula("SUM(K6:K" + (curRow) + ")");
                cell.setCellValue(Integer.parseInt(beanSum.getObject6().toString()));
                cell.setCellStyle(csNum4B);

                cell = row.createCell(11);
                cell.setCellFormula("SUM(L6:L" + (curRow) + ")");
                cell.setCellStyle(csDouble2B);

                // ############## footer report ################
                int footerIndex = (curRow + 2);
                row = sheet.createRow(footerIndex);
                //CellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol)
                sheet.addMergedRegion(new CellRangeAddress(footerIndex, footerIndex, 0, 11));
                cell = row.createCell(0);
                cell.setCellValue(UiReportIndividual.txtFooter1.getText());
                cell.setCellStyle(csStringLeft);

                footerIndex = (curRow + 3);
                row = sheet.createRow(footerIndex);
                //CellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol)
                sheet.addMergedRegion(new CellRangeAddress(footerIndex, footerIndex, 0, 11));
                cell = row.createCell(0);
                cell.setCellValue(UiReportIndividual.txtFooter2.getText());
                cell.setCellStyle(csStringLeft);

                footerIndex = (curRow + 4);
                row = sheet.createRow(footerIndex);
                //CellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol)
                sheet.addMergedRegion(new CellRangeAddress(footerIndex, footerIndex, 0, 11));
                cell = row.createCell(0);
                cell.setCellValue(UiReportIndividual.txtFooter3.getText());
                cell.setCellStyle(csStringLeft);
                // ############## footer report ################

                workbookBase.write(out);
                out.close();

                Console.LOG("op_individual_sum ออกเรียบร้อยแล้วครับ", 1);
                programeStatus.setMessage(ConstantMessage.MSG_REPORT_SUCCESS);
                programeStatus.setTitle(ConstantMessage.MSG_REPORT_COMPLETE);
                programeStatus.setProcessStatus(true);
            } else {

                Console.LOG(ConstantMessage.MSG_NO_DATA_FOR_REPORT, 0);
                programeStatus.setMessage(ConstantMessage.MSG_NO_DATA);
                programeStatus.setTitle(ConstantMessage.MSG_NO_DATA_FOR_REPORT);
                programeStatus.setProcessStatus(false);

            }

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

    public ProgrameStatus op_genReportAllFunction(OppReport report) {
        boolean status = false;
        // String serviceCode = null;
        ProgrameStatus programeStatus = new ProgrameStatus();
        try {
            if (report.getFor_use() == 0) { // for genaral use

                //serviceCode = report.getServiceCode();
                //******************************************Detail************************************************
                report.setReportType(0);
                //report.setServiceCode(serviceCode);
                //programeStatus = this.op_individual_detail_SplitFile(report);
                programeStatus = this.op_individual_detail_SplitFile_caseLimitRowExcell(report);
                //******************************************Sumary************************************************
                report.setReportType(1);
                // report.setServiceCode(serviceCode);
                programeStatus = this.op_individual_sum(report);

            } else if (report.getFor_use() == 1) { // for web site claim
                report.setReportType(0);
                programeStatus = this.op_individual_detail_byHcode_1fileMutiSheet(report);
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

    public String execellHeaderMonth(String stmp) {
        String execellHeaderMonth = null;
        Connection connection = null;
        try {
            connection = new DBManage().open();
            indiDao.setConnection(connection);

            execellHeaderMonth = indiDao.getMonthPayment(stmp);

            System.out.println("execellHeaderMonth: " + execellHeaderMonth);
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
        return execellHeaderMonth;
    }

    public Map spliteRecordData(List<ObjRptIndividual> datas) {
        List<ObjRptIndividual> listObject1 = new ArrayList<ObjRptIndividual>();
        List<ObjRptIndividual> listObject2 = new ArrayList<ObjRptIndividual>();
        List<ObjRptIndividual> listObject3 = new ArrayList<ObjRptIndividual>();
        Map<Integer, List<ObjRptIndividual>> mapList = new HashMap<Integer, List<ObjRptIndividual>>();

        for (int i = 0; i < datas.size(); i++) {
            if (i <= 65500) {  // limit excell row 65,536 rows by 256 columns
                listObject1.add(datas.get(i));
            } else if (datas.size() > 65500 && datas.size() <= 131000) {
                listObject2.add(datas.get(i));
            } else {
                listObject3.add(datas.get(i));
            }
        }

        mapList.put(1, listObject1);
        mapList.put(2, listObject2);
        mapList.put(3, listObject3);

        return mapList;
    }
}
