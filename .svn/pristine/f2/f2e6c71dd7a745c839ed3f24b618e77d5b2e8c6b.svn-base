/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.controller;

import com.claim.xls.ExcellBaseUtil;
import com.claim.connection.DBManage;
import com.claim.dao.ThaiMedicineDao;
import com.claim.object.HospitalService;
import com.claim.object.ObjRptTmdActDetail;
import com.claim.object.ObjRptTmdActSummary;
import com.claim.object.ObjRptTmdMomDetail;
import com.claim.object.ObjRptTmdMomSummary;
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
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author Poolsawat
 */
public class ThaiMedicineController extends ExcellBaseUtil {

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
    private static String FONT_FAMILY = "TH SarabunPSK";
    public static String HEADER_DETAIL_ACT = "รายละเอียดการจ่ายชดเชยค่าบริการแพทย์แผนไทย ปีงบประมาณ 2558  ครั้งที่ {No.}"; //2558  งวดที่........
    public static String HEADER_DETAIL_MOM = "รายละเอียดการจ่ายชดเชยค่าบริการแพทย์แผนไทยกรณีการบริการมารดาหลังคลอด ปีงบประมาณ 2558  ครั้งที่ {No.}"; //2558  งวดที่........
    public static String HEADER_SUM_ACT = "รายงานการจ่ายชดเชยค่าบริการแพทย์แผนไทย ปีงบประมาณ 2558  ครั้งที่ {No.}"; //2558  งวดที่........
    public static String HEADER_SUM_MOM = " รายงานการจ่ายชดเชยค่าบริการแพทย์แผนไทยกรณีการบริการมารดาหลังคลอด ปีงบประมาณ 2558 ครั้งที่ {No.}"; // 2558  งวดที่........

    private static int SERVICE_LIMIT = 5;
    private static String TABLE_RPT_ACT = "RPT_OPBKK_TMD_ACT";
    private static String TABLE_RPT_MOM = "RPT_OPBKK_TMD_MOM";

    public static void main(String[] args) {
        Connection connection = null;
        try {
            ThaiMedicineController thaimedicine = new ThaiMedicineController();
            connection = new DBManage().open();

            OppReport report = new OppReport();
            report.setYearMonth("999999");
            report.setNo("9");
            report.setPathFile("C:"+File.separator+"Users"+File.separator+"Poolsawat"+File.separator+"Desktop"+File.separator+"THAIMEDICINDE");
            report.setServiceCode("11583");
            report.setServiceName("TEST 11583 TEST");
            report.setStmp("999999-9");

            ConstantMessage.IS_SHOW_QUERY = true;

            report.setTitle1(ThaiMedicineController.HEADER_DETAIL_ACT);
            //thaimedicine.tmdActDetail(report);
            //thaimedicine.tmdActSummary(report);
            //thaimedicine.tmdMomDetail(report);
            thaimedicine.tmdMomSummary(report);

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

    }

    public ProgrameStatus tmdActDetail(OppReport report) {
        int[] indexsCol = new int[]{0, 1, 2, 3, 4, 5};
        int count_limit = 0;
        boolean is_beginrow = false;
        int col_last = 11;
        int row_start = 4; // index row
        int row_formula_start = row_start + 1;
        ProgrameStatus programeStatus = new ProgrameStatus();
        List<ObjRptTmdActDetail> listData = new ArrayList<ObjRptTmdActDetail>();

        String stmp = StringOpUtil.removeNull(report.getYearMonth()) + "-" + report.getNo();
        String pathDirectory = report.getPathFile() + ""+File.separator+"" + stmp + ""+File.separator+"";
        try {
            connection = new DBManage().open();
            ThaiMedicineDao tmdDao = new ThaiMedicineDao();
            tmdDao.setConnection(connection);

            //readTemplate 
            file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"TMD_ACT_DETAIL.xls"));

            EXCELL_HEADER1 = report.getTitle1().replace("{No.}", subNoStrStmp(stmp));
            report.setTmdTableName(TABLE_RPT_ACT);
            //EXCELL_HEADER2 = new DateUtil().convertStmpToString(report.getStmp()) + getTitleDateOpd(report);
            EXCELL_HEADER2 = getTitleDateOpd(report);
            EXCELL_HOSPITAL = "หน่วยบริการ: " + StringOpUtil.removeNull(report.getServiceName()) + " (" + StringOpUtil.removeNull(report.getServiceCode()) + ")";

            // style Excell
            HSSFWorkbook wbTmd = new HSSFWorkbook(file);
            this.setFontFamily("Arial");
            this.setColorCell(HSSFColor.LIGHT_GREEN.index);
            this.setFontSize(7);
            this.setFontHeaderSize(8);
            this.loadStyle(wbTmd);

            // Start sheet 1 
            HSSFSheet sheet = workbookBase.getSheetAt(0);
            //sheet.createFreezePane(5, row_start); // col[F],row[index 6 = 7]
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
            double sumTotalPay = 0.00;
            int key_rank = 0;
            int autoNumber = 1;
            listData = tmdDao.getListTmdAct(report);
            for (int j = 0; j < listData.size(); j++) {
                ObjRptTmdActDetail data = listData.get(j);

                row = sheet.createRow(curRow);
                row.setHeight((short) 400);

                cell = row.createCell(0);
                cell.setCellValue(autoNumber);
                cell.setCellStyle(csNum4);

                cell = row.createCell(1);
                cell.setCellValue(data.getPid());
                cell.setCellStyle(csStringPid);

                cell = row.createCell(2);
                cell.setCellValue(data.getPname());
                cell.setCellStyle(csStringLeft);

                cell = row.createCell(3);
                cell.setCellValue(data.getHn());
                cell.setCellStyle(csStringCenter);

                cell = row.createCell(4);
                cell.setCellValue(data.getHmain() + ": " + data.getHmainname());
                cell.setCellStyle(csStringLeft);

                cell = row.createCell(5);
                cell.setCellValue(data.getDateopd_th());
                cell.setCellStyle(csStringCenter);

                cell = row.createCell(6);
                cell.setCellValue(data.getItem_type());
                cell.setCellStyle(csStringLeft);

                cell = row.createCell(7);
                cell.setCellValue(data.getItem_code() + ": " + data.getItem_desc());
                cell.setCellStyle(csStringLeft);

                cell = row.createCell(8);
                cell.setCellValue(data.getPoint());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(9);
                cell.setCellValue(data.getRatepay());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(10);
                cell.setCellValue(data.getTotalpay());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(11);
                cell.setCellValue(data.getInvoice_no());
                cell.setCellStyle(csStringLeft);

                cell = row.createCell(12);
                cell.setCellValue(data.getTxid());
                cell.setCellStyle(csStringtxid);

//                System.out.println("key_rank ::==" + key_rank);
//                System.out.println("data.getRank_hcode ::==" + data.getRank_hcode());
                /*
                 Merge
                 */
                /*if (key_rank != data.getRank_hcode() && i > 1) {
                 count_limit = mergeRowLimit(sheet, curRow, count_limit, indexsCol, col1);
                 col1++;
                 } else {
                 count_limit++;
                 }
                 key_rank = data.getRank_hcode();
                 */
                sumTotalPay += data.getTotalpay();

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
            row.createCell(7).setCellStyle(csDouble2B);

            cell = row.createCell(8);
            cell.setCellFormula(builderFormulaSumRound(8, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(9);
            //cell.setCellFormula(builderFormula(9, row_formula_start, curRow));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(10);
            cell.setCellFormula(builderFormulaSumRound(10, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            /*
             ############ วันที่ออกรายงาน ###############
             */
            int rowDateTimeCurrent = curRow + 1;
            // BathText
            row = sheet.createRow((rowDateTimeCurrent));
            cell = row.createCell(0);
            cell.setCellValue("ออกรายงานเมื่อวันที่ _" + new DateUtil().getDateTimeCurrent());
            sheet.addMergedRegion(new CellRangeAddress(rowDateTimeCurrent, rowDateTimeCurrent, 0, 10));
            cell.setCellStyle(csStringB);

            /*
             ############ วันที่ออกรายงาน ###############
             */

            /*
             ############ BathText ###############
             */
            /* int rowBathText = curRow + 1;
             // BathText
             row = sheet.createRow((rowBathText));
             cell = row.createCell(0);
             cell.setCellValue(new UtilDao().getThaiBath(Double.parseDouble(new NumberUtil().numberDigiit(sumTotalPay, 2))));
             sheet.addMergedRegion(new CellRangeAddress(rowBathText, rowBathText, 0, 10));
             cell.setCellStyle(csNum4B_R);
             */
            /*
             ############ BathText ###############
             */
            workbookBase.setSheetName(0, report.getServiceCode());// + "  " + report.getServiceName());
            
            
            /// int sheetIndex, int startColumn, int endColumn, int startRow, int endRow)                    
            /*sheet.setAutobreaks(false);
            sheet.setColumnHidden(col_last + 1, true);
            sheet.setRowBreak((curRow + 1));
            sheet.setColumnBreak(col_last);
            //wb.setPrintArea(0, "A1:K" + (curRow + 1));

            // file out                           
            ExtendedFormatRecord e = new ExtendedFormatRecord();
            e.setShrinkToFit(true);*/

            new FileUtil().mkdirMutiDirectory(pathDirectory);

            out = new FileOutputStream(pathDirectory + ""+File.separator+"thaimedicine_act" + StringOpUtil.removeNull(report.getServiceCode()) + "_" + report.getStmp() + ".xls");
            workbookBase.write(out);

            out.close();
            file.close();
            System.out.println("report.getServiceName() ::==" + report.getServiceName());
            Console.LOG("ออกรายงาน " + StringOpUtil.removeNull(report.getServiceName()) + " งวด: "
                    + report.getYearMonth() + "-"
                    + report.getNo() + " เรียบร้อยแล้วครับ", 1);
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

    public ProgrameStatus tmdMomDetail(OppReport report) {
        int[] indexsCol = new int[]{0, 1, 2, 3, 4, 5};
        int count_limit = 0;
        int col_last = 11;
        int row_start = 4; // index row
        int row_formula_start = row_start + 1;
        ProgrameStatus programeStatus = new ProgrameStatus();
        List<ObjRptTmdMomDetail> listData = new ArrayList<ObjRptTmdMomDetail>();

        String stmp = StringOpUtil.removeNull(report.getYearMonth()) + "-" + report.getNo();
        String pathDirectory = report.getPathFile() + ""+File.separator+"" + stmp + ""+File.separator+"";
        try {
            connection = new DBManage().open();

            ThaiMedicineDao tmdDao = new ThaiMedicineDao();
            tmdDao.setConnection(connection);

            //readTemplate 
            file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"TMD_MOM_DETAIL.xls"));

            EXCELL_HEADER1 = report.getTitle1().replace("{No.}", subNoStrStmp(stmp));
            report.setTmdTableName(TABLE_RPT_MOM);
            EXCELL_HEADER2 = getTitleDateOpd(report);
            EXCELL_HOSPITAL = "หน่วยบริการ: " + StringOpUtil.removeNull(report.getServiceName()) + " (" + StringOpUtil.removeNull(report.getServiceCode()) + ")";

            // style Excell
            HSSFWorkbook wbTmd = new HSSFWorkbook(file);
            this.setFontFamily("Arial");
            this.setFontSize(7);
            this.setColorCell(HSSFColor.LIGHT_GREEN.index);
            this.setFontHeaderSize(8);
            this.loadStyle(wbTmd);

            // Start sheet 1 
            HSSFSheet sheet = workbookBase.getSheetAt(0);
            //sheet.createFreezePane(5, row_start); // col[F],row[index 6 = 7]
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
            double sumTotalPay = 0.00;
            listData = tmdDao.getListTmdMom(report);
            int autoNumber = 1;
            for (int j = 0; j < listData.size(); j++) {
                ObjRptTmdMomDetail data = listData.get(j);

                row = sheet.createRow(curRow);
                row.setHeight((short) 400);

                /*                
                 Merge   sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 7));              
                 */
//                System.out.println("count_limit ::==" + count_limit);
//                System.out.println("curRow :;==" + curRow);
//                System.out.println("j ::==" + (j + row_start));
                cell = row.createCell(0);
                cell.setCellValue(autoNumber);
                cell.setCellStyle(csNum4);

                cell = row.createCell(1);
                cell.setCellValue(data.getPid());
                cell.setCellStyle(csStringPid);

                cell = row.createCell(2);
                cell.setCellValue(data.getPname());
                cell.setCellStyle(csStringLeft);

                cell = row.createCell(3);
                cell.setCellValue(data.getHn());
                cell.setCellStyle(csStringCenter);

                cell = row.createCell(4);
                cell.setCellValue(data.getHmain() + ": " + data.getHmainname());
                cell.setCellStyle(csStringLeft);

                cell = row.createCell(5);
                cell.setCellValue(data.getDateopd_th());
                cell.setCellStyle(csStringCenter);

                cell = row.createCell(6);
                cell.setCellValue(data.getItem_code() + ": " + data.getItem_desc());
                cell.setCellStyle(csStringLeft);

                cell = row.createCell(7);
                cell.setCellValue(data.getCase_place());
                cell.setCellStyle(csStringLeft);

                cell = row.createCell(8);
                cell.setCellValue(data.getPoint());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(9);
                cell.setCellValue(data.getRatepay());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(10);
                cell.setCellValue(data.getTotalpay());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(11);
                cell.setCellValue(data.getInvoice_no());
                cell.setCellStyle(csStringCenter);

                cell = row.createCell(12);
                cell.setCellValue(data.getTxid());
                cell.setCellStyle(csStringtxid);

                /*
                 Merge
                 */
                /*if (count_limit == SERVICE_LIMIT) {
                 System.out.println("write ::==" + col1);
                 count_limit = mergeRowLimit(sheet, curRow, count_limit, indexsCol, col1);
                 col1++;

                 } else {
                 System.out.println("count_limit ++");
                 count_limit++;
                 }
                 */
                sumTotalPay += data.getTotalpay();
                curRow++;
                autoNumber++;
            }

//            if (SERVICE_LIMIT == count_limit) {
//                mergeRowLimit(sheet, curRow, count_limit, indexsCol, col1);
//            }
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
            row.createCell(7).setCellStyle(csDouble2B);

            cell = row.createCell(8);
            cell.setCellFormula(builderFormulaSumRound(8, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(9);
            //cell.setCellFormula(builderFormula(9, row_formula_start, curRow));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(10);
            cell.setCellFormula(builderFormulaSumRound(10, row_formula_start, curRow, 0));
            cell.setCellStyle(csDouble2B);

            /*
             ############ วันที่ออกรายงาน ###############
             */
            int rowDateTimeCurrent = curRow + 1;
            // BathText
            row = sheet.createRow((rowDateTimeCurrent));
            cell = row.createCell(0);
            cell.setCellValue("ออกรายงานเมื่อวันที่ _" + new DateUtil().getDateTimeCurrent());
            sheet.addMergedRegion(new CellRangeAddress(rowDateTimeCurrent, rowDateTimeCurrent, 0, 10));
            cell.setCellStyle(csStringB);
            /*
             ############ วันที่ออกรายงาน ###############
             */

            /*
             ############ BathText ###############
             */
            /*int rowBathText = curRow + 1;
             // BathText
             row = sheet.createRow((rowBathText));
             cell = row.createCell(0);
             cell.setCellValue(new UtilDao().getThaiBath(Double.parseDouble(new NumberUtil().numberDigiit(sumTotalPay, 2))));
             sheet.addMergedRegion(new CellRangeAddress(rowBathText, rowBathText, 0, 10));
             cell.setCellStyle(csNum4B_R);
             */
            /*
             ############ BathText ###############
             */
            workbookBase.setSheetName(0, report.getServiceCode());// + "  " + report.getServiceName());
            
            
            /// int sheetIndex, int startColumn, int endColumn, int startRow, int endRow)                    
            /*sheet.setAutobreaks(false);
            sheet.setColumnHidden(col_last + 1, true);
            sheet.setColumnBreak(col_last);

            //wb.setPrintArea(0, "$A$1:$K$" + (curRow + 1));
            sheet.setColumnBreak(col_last);*/
            
            
            // file out 
            new FileUtil().mkdirMutiDirectory(pathDirectory);

            out = new FileOutputStream(pathDirectory + ""+File.separator+"thaimedicine_mom" + StringOpUtil.removeNull(report.getServiceCode()) + "_" + report.getStmp() + ".xls");
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

    public ProgrameStatus tmdActSummary(OppReport report) {
        int col_last = 18;
        int row_start = 6; // index row
        int col_freeze = 3;
        int row_freeze = 6;
        int row_formula_start = row_start + 1;
        ProgrameStatus programeStatus = new ProgrameStatus();
        List<ObjRptTmdActSummary> listData = new ArrayList<ObjRptTmdActSummary>();
        String stmp = StringOpUtil.removeNull(report.getYearMonth()) + "-" + report.getNo();
        String pathDirectory = report.getPathFile() + ""+File.separator+"" + stmp + ""+File.separator+"";
        try {
            connection = new DBManage().open();
            ThaiMedicineDao tmdDao = new ThaiMedicineDao();
            tmdDao.setConnection(connection);

            file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"TMD_ACT_SUM.xls"));

            // Top Excell Sheet1
            EXCELL_HEADER1 = report.getTitle1().replace("{No.}", subNoStrStmp(stmp));
            report.setTmdTableName(TABLE_RPT_ACT);
            EXCELL_HEADER2 = new DateUtil().convertStmpToString(report.getStmp()) + getTitleDateOpd(report);

            HSSFWorkbook wbTmd = new HSSFWorkbook(file);
            this.setFontFamily("Arial");
            this.setFontSize(7);
            this.setColorCell(HSSFColor.LIGHT_GREEN.index);
            this.setFontHeaderSize(8);
            this.loadStyle(wbTmd);

            HSSFSheet sheet = workbookBase.getSheetAt(0);
            sheet.setDefaultRowHeightInPoints(100);
            //sheet.createFreezePane(col_freeze, row_freeze);

            // row 0 Header
            row = sheet.createRow(0);
            row.setHeight((short) 390);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_HEADER1);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col_last));
            cell.setCellStyle(csHead);

//            // row 2 Header
//            row = sheet.createRow(1);
//            row.setHeight((short) 390);
//            cell = row.createCell(0);
//            cell.setCellValue(EXCELL_HEADER2);
//            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col_last));
//            cell.setCellStyle(csHead);
            int curRow = row_start;
            double sumTotalSumPay = 0.00;
            int i = 1;
            listData = tmdDao.getListSummaryTmdAct(report);
            System.out.println("listData.size() :" + listData.size());
            for (int j = 0; j < listData.size(); j++) {
                ObjRptTmdActSummary objData = listData.get(j);

                row = sheet.createRow(curRow);
                row.setHeight((short) 360);
                cell = row.createCell(0);
                cell.setCellValue(i);
                cell.setCellStyle(csNum4);

                cell = row.createCell(1);
                cell.setCellValue(objData.getHcode());
                cell.setCellStyle(csString2Center);

                cell = row.createCell(2);
                cell.setCellValue(objData.getHcodename());
                cell.setCellStyle(csStringLeft);

                /*
                 1
                 */
                cell = row.createCell(3);
                cell.setCellValue(objData.getCount_dis_txid1());
                cell.setCellStyle(csNum4R);

                cell = row.createCell(4);
                cell.setCellValue(objData.getSum_point1());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(5);
                cell.setCellValue(objData.getSum_totalpay1());
                cell.setCellStyle(csDouble2);

                /*
                 2
                 */
                cell = row.createCell(6);
                cell.setCellValue(objData.getCount_dis_txid2());
                cell.setCellStyle(csNum4R);

                cell = row.createCell(7);
                cell.setCellValue(objData.getSum_point2());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(8);
                cell.setCellValue(objData.getSum_totalpay2());
                cell.setCellStyle(csDouble2);

                /*
                 3
                 */
                cell = row.createCell(9);
                cell.setCellValue(objData.getCount_dis_txid3());
                cell.setCellStyle(csNum4R);

                cell = row.createCell(10);
                cell.setCellValue(objData.getSum_point3());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(11);
                cell.setCellValue(objData.getSum_totalpay3());
                cell.setCellStyle(csDouble2);

                /*
                 4
                 */
                cell = row.createCell(12);
                cell.setCellValue(objData.getCount_dis_txid4());
                cell.setCellStyle(csNum4R);

                cell = row.createCell(13);
                cell.setCellValue(objData.getSum_point4());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(14);
                cell.setCellValue(objData.getSum_totalpay4());
                cell.setCellStyle(csDouble2);
                /*
                 5
                 */
                cell = row.createCell(15);
                cell.setCellValue(objData.getCount_dis_txid5());
                cell.setCellStyle(csNum4R);

                cell = row.createCell(16);
                cell.setCellValue(objData.getSum_point5());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(17);
                cell.setCellValue(objData.getSum_totalpay5());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(18);
                cell.setCellValue(objData.getSum_totalpay_all());
                cell.setCellStyle(csDouble2);
                sumTotalSumPay += objData.getSum_totalpay_all();
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
            sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 2));
            cell.setCellStyle(csNum4B);

            row.createCell(1).setCellStyle(csHeadTab);
            row.createCell(2).setCellStyle(csHeadTab);

            cell = row.createCell(3);
            cell.setCellFormula(builderFormulaSum(3, row_formula_start, curRow));
            cell.setCellStyle(csNum3B);
            cell = row.createCell(4);
            cell.setCellFormula(builderFormulaSum(4, row_formula_start, curRow));
            cell.setCellStyle(csDouble2B);
            cell = row.createCell(5);
            cell.setCellFormula(builderFormulaSum(5, row_formula_start, curRow));
            cell.setCellStyle(csDouble2B);
            cell = row.createCell(6);
            cell.setCellFormula(builderFormulaSum(6, row_formula_start, curRow));
            cell.setCellStyle(csNum3B);
            cell = row.createCell(7);
            cell.setCellFormula(builderFormulaSum(7, row_formula_start, curRow));
            cell.setCellStyle(csDouble2B);
            cell = row.createCell(8);
            cell.setCellFormula(builderFormulaSum(8, row_formula_start, curRow));
            cell.setCellStyle(csDouble2B);
            cell = row.createCell(9);
            cell.setCellFormula(builderFormulaSum(9, row_formula_start, curRow));
            cell.setCellStyle(csNum3B);
            cell = row.createCell(10);
            cell.setCellFormula(builderFormulaSum(10, row_formula_start, curRow));
            cell.setCellStyle(csDouble2B);
            cell = row.createCell(11);
            cell.setCellFormula(builderFormulaSum(11, row_formula_start, curRow));
            cell.setCellStyle(csDouble2B);
            cell = row.createCell(12);
            cell.setCellFormula(builderFormulaSum(12, row_formula_start, curRow));
            cell.setCellStyle(csNum3B);
            cell = row.createCell(13);
            cell.setCellFormula(builderFormulaSum(13, row_formula_start, curRow));
            cell.setCellStyle(csDouble2B);
            cell = row.createCell(14);
            cell.setCellFormula(builderFormulaSum(14, row_formula_start, curRow));
            cell.setCellStyle(csDouble2B);
            cell = row.createCell(15);
            cell.setCellFormula(builderFormulaSum(15, row_formula_start, curRow));
            cell.setCellStyle(csNum3B);
            cell = row.createCell(16);
            cell.setCellFormula(builderFormulaSum(16, row_formula_start, curRow));
            cell.setCellStyle(csDouble2B);
            cell = row.createCell(17);
            cell.setCellFormula(builderFormulaSum(17, row_formula_start, curRow));
            cell.setCellStyle(csDouble2B);
            cell = row.createCell(18);
            cell.setCellFormula(builderFormulaSum(18, row_formula_start, curRow));
            cell.setCellStyle(csDouble2B);

            /*
             ############ วันที่ออกรายงาน ###############
             */
            int rowDateTimeCurrent = curRow + 1;
            // BathText
            row = sheet.createRow((rowDateTimeCurrent));
            cell = row.createCell(0);
            cell.setCellValue("ออกรายงานเมื่อวันที่ _" + new DateUtil().getDateTimeCurrent());
            sheet.addMergedRegion(new CellRangeAddress(rowDateTimeCurrent, rowDateTimeCurrent, 0, 10));
            cell.setCellStyle(csStringB);

            /*
             ############ วันที่ออกรายงาน ###############
             */

            /*
             ################# bathTaxt #####################
             */
            /*System.out.println("(curRow+1) ::=="+(curRow+1));
             CellReference cellReference = new CellReference("S"+(curRow+1)); 
             Row row = sheet.getRow(cellReference.getRow());
             Cell cell = row.getCell(cellReference.getCol());             
             System.out.println("cell.getCellFormula() ::=="+cell.getCellFormula());
             double totalPay = cell.getNumericCellValue();
             int rowBathText = curRow + 1;
             // BathText
             row = sheet.createRow((rowBathText));
             cell = row.createCell(0);
             cell.setCellValue(new UtilDao().getThaiBath(Double.parseDouble(new NumberUtil().numberDigiit(sumTotalSumPay, 2))));
             sheet.addMergedRegion(new CellRangeAddress(rowBathText, rowBathText, 0, 18));
             cell.setCellStyle(csNum4B_R);
             */
            /*
             ################# bathTaxt #####################
             */
            new FileUtil().mkdirMutiDirectory(pathDirectory);

            //write file Excell
            out = new FileOutputStream(pathDirectory + ""+File.separator+"tmdact_summary_" + report.getYearMonth() + "-" + report.getNo() + ".xls");
            workbookBase.write(out);
            out.close();
            file.close();
            Console.LOG("รายงานรายละเอียด การจ่ายชดเชยค่าบริการแพทย์แผนไทย ถูกออกเรียบร้อยแล้ว", 1);

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

    public ProgrameStatus tmdMomSummary(OppReport report) {
        int col_last = 7;
        int row_start = 4; // index row
        int row_freeze = 4;
        int col_freeze = 3;
        int row_formula_start = row_start + 1;
        ProgrameStatus programeStatus = new ProgrameStatus();
        List<ObjRptTmdMomSummary> listData = new ArrayList<ObjRptTmdMomSummary>();
        String stmp = StringOpUtil.removeNull(report.getYearMonth()) + "-" + report.getNo();
        String pathDirectory = report.getPathFile() + ""+File.separator+"" + stmp + ""+File.separator+"";
        try {
            connection = new DBManage().open();
            ThaiMedicineDao tmdDao = new ThaiMedicineDao();
            tmdDao.setConnection(connection);

            file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"TMD_MOM_SUM.xls"));

            // Top Excell Sheet1
            EXCELL_HEADER1 = report.getTitle1().replace("{No.}", subNoStrStmp(stmp));
            report.setTmdTableName(TABLE_RPT_MOM);
            EXCELL_HEADER2 = new DateUtil().convertStmpToString(report.getStmp()) + getTitleDateOpd(report);

            HSSFWorkbook wbTmd = new HSSFWorkbook(file);
            this.setFontFamily("Arial");
            this.setFontSize(7);
            this.setColorCell(HSSFColor.LIGHT_GREEN.index);
            this.setFontHeaderSize(8);
            this.loadStyle(wbTmd);

            HSSFSheet sheet = workbookBase.getSheetAt(0);
            //sheet.createFreezePane(col_freeze, row_freeze);

            // row 0 Header
            row = sheet.createRow(0);
            row.setHeight((short) 390);
            cell = row.createCell(0);
            cell.setCellValue(EXCELL_HEADER1);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col_last));
            cell.setCellStyle(csHead);

//            // row 2 Header
//            row = sheet.createRow(1);
//            row.setHeight((short) 390);
//            cell = row.createCell(0);
//            cell.setCellValue(EXCELL_HEADER2);
//            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, col_last));
//            cell.setCellStyle(csHead);
            int curRow = row_start;
            int i = 1;
            double sumTotalSumPay = 0.00;
            listData = tmdDao.getListSummaryTmdMom(report);
            System.out.println("listData.size() :" + listData.size());
            for (int j = 0; j < listData.size(); j++) {
                ObjRptTmdMomSummary objData = listData.get(j);

                row = sheet.createRow(curRow);
                row.setHeight((short) 360);
                cell = row.createCell(0);
                cell.setCellValue(i);
                cell.setCellStyle(csNum4);

                cell = row.createCell(1);
                cell.setCellValue(objData.getHcode());
                cell.setCellStyle(csString2Center);

                cell = row.createCell(2);
                cell.setCellValue(objData.getHcodename());
                cell.setCellStyle(csStringLeft);

                cell = row.createCell(3);
                cell.setCellValue(objData.getCount_in_hosp());
                cell.setCellStyle(csNum4R);

                cell = row.createCell(4);
                cell.setCellValue(objData.getSum_in_hosp());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(5);
                cell.setCellValue(objData.getCount_out_hosp());
                cell.setCellStyle(csNum4R);

                cell = row.createCell(6);
                cell.setCellValue(objData.getSum_out_hosp());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(7);
                cell.setCellValue(objData.getSum_totalpay());
                cell.setCellStyle(csDouble2);

                sumTotalSumPay += objData.getSum_totalpay();
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
            sheet.addMergedRegion(new CellRangeAddress(curRow, curRow, 0, 2));
            cell.setCellStyle(csNum4B);

            row.createCell(1).setCellStyle(csHeadTab);
            row.createCell(2).setCellStyle(csHeadTab);

            cell = row.createCell(3);
            cell.setCellFormula(builderFormulaSum(3, row_formula_start, curRow));
            cell.setCellStyle(csNum4BR);
            cell = row.createCell(4);
            cell.setCellFormula(builderFormulaSum(4, row_formula_start, curRow));
            cell.setCellStyle(csDouble2B);
            cell = row.createCell(5);
            cell.setCellFormula(builderFormulaSum(5, row_formula_start, curRow));
            cell.setCellStyle(csNum4BR);
            cell = row.createCell(6);
            cell.setCellFormula(builderFormulaSum(6, row_formula_start, curRow));
            cell.setCellStyle(csDouble2B);
            cell = row.createCell(7);
            cell.setCellFormula(builderFormulaSum(7, row_formula_start, curRow));
            cell.setCellStyle(csDouble2B);

            /*
             ############ วันที่ออกรายงาน ###############
             */
            int rowDateTimeCurrent = curRow + 1;
            // BathText
            row = sheet.createRow((rowDateTimeCurrent));
            cell = row.createCell(0);
            cell.setCellValue("ออกรายงานเมื่อวันที่ _" + new DateUtil().getDateTimeCurrent());
            sheet.addMergedRegion(new CellRangeAddress(rowDateTimeCurrent, rowDateTimeCurrent, 0, 10));
            cell.setCellStyle(csStringB);

            /*
             ############ วันที่ออกรายงาน ###############
             */

            /*
             ############ BathText ###############
             */
            /* int rowBathText = curRow + 1;
             // BathText
             row = sheet.createRow((rowBathText));
             cell = row.createCell(0);
             cell.setCellValue(new UtilDao().getThaiBath(Double.parseDouble(new NumberUtil().numberDigiit(sumTotalSumPay, 2))));
             sheet.addMergedRegion(new CellRangeAddress(rowBathText, rowBathText, 0, 7));
             cell.setCellStyle(csNum4B_R);
             */
            /*
             ################# bathTaxt #####################
             */
            new FileUtil().mkdirMutiDirectory(pathDirectory);

            //write file Excell
            out = new FileOutputStream(pathDirectory + ""+File.separator+"tmdmom_summary_" + report.getYearMonth() + "-" + report.getNo() + ".xls");
            workbookBase.write(out);
            out.close();
            file.close();
            Console.LOG("รายงานสรุปการจ่ายชดเชยค่าบริการแพทย์แผนไทยกรณีการบริการมารดาหลังคลอด ถูกออกเรียบร้อยแล้ว", 1);

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

    public ProgrameStatus tmdDetailByHcode(OppReport report) {
        ProgrameStatus programeStatus = new ProgrameStatus();
        List<HospitalService> listData = new ArrayList<>();
        try {
            connection = new DBManage().open();
            ThaiMedicineDao thaiMedicineDao = new ThaiMedicineDao();
            thaiMedicineDao.setConnection(connection); // connection = new DBManage().open();
            listData = thaiMedicineDao.getHospitalService(report);

            for (int i = 0; i < listData.size(); i++) {
                HospitalService objData = listData.get(i);
                report.setServiceCode(objData.getHosCode());
                report.setServiceName(objData.getHosCodeName());
                /*
                 public static String HEADER_SUM_ACT = "รายงานสรุปกิจกรรมนวด ประคบสมุนไพร อบสมุนไพร และจ่ายยาสมุนไพร";
                 public static String HEADER_SUM_MOM = "รายงานสรุปกิจกรรมบริการมารดาหลังคลอด";
                 */
                if (report.getTmdServiceType().equals(ConstantMessage.TMD_ACT)) { // กิจกรรมนวด ประคบสมุนไพร อบสมุนไพร และจ่ายยาสมุนไพร
                    programeStatus = tmdActDetail(report);
                } else {  //กิจกรรมบริการมารดาหลังคลอด
                    programeStatus = tmdMomDetail(report);
                }

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

    public ProgrameStatus tmd_genReportAllFunction(OppReport report) {
        ProgrameStatus programeStatus = new ProgrameStatus();
        try {
            programeStatus = tmdDetailByHcode(report);
            if (report.getTmdServiceType().equals(ConstantMessage.TMD_ACT)) { // กิจกรรมนวด ประคบสมุนไพร อบสมุนไพร และจ่ายยาสมุนไพร
                programeStatus = tmdActSummary(report);
            } else {  //กิจกรรมบริการมารดาหลังคลอด
                programeStatus = tmdMomSummary(report);
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

    public int mergeRowLimit_(HSSFSheet sheet, int curRow, int count_limit, int[] cols, int autoNum) {
        //CellRangeAddress(int firstRow, int lastRow, int firstCol, int lastCol)
        for (int col : cols) {
            sheet.addMergedRegion(new CellRangeAddress(curRow - count_limit, curRow - 1, col, col));
        }
        //System.out.println("write Auto Run ::==" + (curRow - count_limit));
        row = sheet.getRow(curRow - count_limit);
        row.setHeight((short) 340);
        cell = row.createCell(0);
        cell.setCellValue(autoNum);
        cell.setCellStyle(csNum4);
        return 1;
    }

    private String getTitleDateOpd(OppReport report) {
        String title = "";
        try {
            connection = new DBManage().open();
            ThaiMedicineDao tmdDateOpd = new ThaiMedicineDao();
            tmdDateOpd.setConnection(connection);
            HospitalService objDateOpdMinMax = tmdDateOpd.getDateOpdMinMax(report);

            title = "ช่วงวันรับบริการ " + objDateOpdMinMax.getDateopd_begin() + " ถึงวันที่ " + objDateOpdMinMax.getDateopd_end();
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
        return title;
    }

    private String subNoStrStmp(String stmp) {
        return stmp.substring(7, 8);
    }
}
