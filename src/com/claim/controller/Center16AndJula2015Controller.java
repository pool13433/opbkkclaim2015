/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.controller;

import com.claim.xls.ExcellBaseUtil;
import com.claim.connection.DBManage;
import com.claim.dao.Center16AndChula2015DAO;
import com.claim.object.ObjRptChula;
import com.claim.object.OppReport;
import com.claim.support.Console;
import com.claim.constants.ConstantMessage;
import com.claim.support.Message;
import com.claim.object.ProgrameStatus;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author Poolsawat.a
 */
public class Center16AndJula2015Controller extends ExcellBaseUtil {

    //############## POI ##############
    Connection connection = null;
    FileInputStream file = null;
    FileOutputStream out = null;
    String EXCELL_HEADER1 = null;
    String EXCELL_HEADER2 = null;
    String EXCELL_HOSPITAL = null;
    //############## POI ##############

    //############## Global ###########
    static String HCODE_CHULA = "13756";
    static String HCODE_CENTER16 = "13661";
    //############## Global ###########
    

    public ProgrameStatus center16_Jula_2015(OppReport report) {

        ProgrameStatus programeStatus = new ProgrameStatus();
        List<ObjRptChula> listData = new ArrayList<ObjRptChula>();
        int col_last = 29;
        int row_start = 8; // index row
        int row_formula_start = row_start + 1;
        int col_txtid_width = col_last + 1;
        try {
            connection = new DBManage().open();
            Center16AndChula2015DAO chula2015DAO = new Center16AndChula2015DAO();
            chula2015DAO.setConnection(connection);

            if (report.getServiceCode().equals(HCODE_CENTER16)) { // center 16
                listData = chula2015DAO.getListChulaDetail(report.getStmp(), HCODE_CENTER16);
                EXCELL_HEADER1 = "การจ่ายเงินงบประมาณค่าบริการทางการแพทย์ OP งวดเดือน: "
                        + chula2015DAO.getMonthPayment(report.getStmp());
                EXCELL_HEADER2 = "สำหรับศูนย์บริการสาธารณสุข 16 ลุมพินี  รหัสสถานพยาบาล 13661  Model 2 ";
                EXCELL_HOSPITAL = "ชื่อหน่วยบริการที่รักษา  ศูนย์บริการสาธารณสุข 16 ลุมพินี รหัสสถานพยาบาล 13661 ";

                //out 
                out = new FileOutputStream(report.getPathFile() + ""+File.separator+"HC16_13661_" + report.getYearMonth() + "-" + report.getNo() + ".xls");
                report.setServiceName(" ศูนย์บริการสาธารณสุข 16 ลุมพินี");
            } else if (report.getServiceCode().equals(HCODE_CHULA)) { //  Jula
                listData = chula2015DAO.getListChulaDetail(report.getStmp(), HCODE_CHULA);
                EXCELL_HEADER1 = "การจ่ายเงินงบประมาณค่าบริการทางการแพทย์ OP งวดเดือน: "
                        + chula2015DAO.getMonthPayment(report.getStmp());
                EXCELL_HEADER2 = "สำหรับศูนย์บริการสาธารณสุข 16 ลุมพินี  รหัสสถานพยาบาล 13661  Model 2";
                EXCELL_HOSPITAL = "ชื่อหน่วยบริการที่รักษา  โรงพยาบาลจุฬาลงกรณ์  สภากาชาดไทย   รหัสสถานพยาบาล  13756";

                //out 
                out = new FileOutputStream(report.getPathFile() + ""+File.separator+"Chula_13756_" + report.getYearMonth() + "-" + report.getNo() + ".xls");
                report.setServiceName("โรงพยาบาลจุฬาลงกรณ์");
            }

            //readTemplate 
            file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"CH16_CHula_detail_2015.xls"));

            // style Excell
            HSSFWorkbook wbCenter16Jula = new HSSFWorkbook(file);
            this.loadStyle(wbCenter16Jula);

            // Start sheet 1 *******************************************************************************
            HSSFSheet sheet = workbookBase.getSheetAt(0);

            sheet.createFreezePane(4, 8); // col,row
            sheet.setColumnWidth(col_txtid_width, WIDTH_TXID);

            HSSFCell cell = null;
            HSSFRow row = null;

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

            for (int j = 0; j < listData.size(); j++) {
                ObjRptChula data = listData.get(j);
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
                cell.setCellValue(data.getHn());
                cell.setCellStyle(csStringLeft);

                cell = row.createCell(3);
                cell.setCellValue(data.getPname());
                cell.setCellStyle(csStringLeft);

                cell = row.createCell(4);
                cell.setCellValue(data.getHmain());
                cell.setCellStyle(csStringLeft);

                cell = row.createCell(5);
                cell.setCellValue(data.getDateopd_th());
                cell.setCellStyle(csStringLeft);

                cell = row.createCell(6);
                cell.setCellValue(data.getPdxcode());
                cell.setCellStyle(csNum4);

                cell = row.createCell(7);
                cell.setCellValue(data.getChrg_car());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(8);
                cell.setCellValue(data.getChrg_rehab_inst());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(9);
                cell.setCellValue(data.getChrg_ophc());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(10);
                cell.setCellValue(data.getChrg_car_rehabinst_ophc_total());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(11);
                cell.setCellValue(data.getChrg_202());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(12);
                cell.setCellValue(data.getChrg_stditem());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(13);
                cell.setCellValue(data.getChrg_other());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(14);
                cell.setCellValue(data.getChrg_total());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(15);
                cell.setCellValue(data.getSum_chrg());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(16);
                cell.setCellValue(data.getPaid_car());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(17);
                cell.setCellValue(data.getPaid_rehab_inst());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(18);
                cell.setCellValue(data.getPaid_ophc());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(19);
                cell.setCellValue(data.getPaid_car_rehabinst_ophc_total());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(20);
                cell.setCellValue(data.getPaid_202());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(21);
                cell.setCellValue(data.getPaid_stditem());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(22);
                cell.setCellValue(data.getPaid_other());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(23);
                cell.setCellValue(data.getPaid_202_stditem_other_total());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(24);
                cell.setCellValue(data.getPaid_cal_point());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(25);
                cell.setCellValue(data.getPaid_cal_point_total());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(26);
                cell.setCellValue(data.getPaid_total());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(27);
                cell.setCellValue(data.getCompensation_fee_total());
                cell.setCellStyle(csDouble2);

                cell = row.createCell(28);
                cell.setCellValue(data.getRemark());
                cell.setCellStyle(csStringLeft);

                cell = row.createCell(29);
                cell.setCellValue(data.getInvoice_no());
                cell.setCellStyle(csStringLeft);

                cell = row.createCell(30);
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
            cell.setCellFormula(builderFormulaSum(22, row_formula_start, curRow));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(23);
            cell.setCellFormula(builderFormulaSum(23, row_formula_start, curRow));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(24);
            cell.setCellFormula(builderFormulaSum(24, row_formula_start, curRow));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(25);
            cell.setCellFormula(builderFormulaSum(25, row_formula_start, curRow));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(26);
            cell.setCellFormula(builderFormulaSum(26, row_formula_start, curRow));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(27);
            cell.setCellFormula(builderFormulaSum(27, row_formula_start, curRow));
            cell.setCellStyle(csDouble2B);

            cell = row.createCell(28);
            cell.setCellStyle(csNum4B);

            workbookBase.setSheetName(0, report.getServiceCode() + "  " + report.getServiceName());

            workbookBase.write(out);

            out.close();
            file.close();

            Console.LOG(Message.exportSuccess(report.getServiceName()), 1);
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
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Center16AndJula2015Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return programeStatus;
    }

    public ProgrameStatus center16_Jula_All(OppReport report) {
        ProgrameStatus programeStatus = new ProgrameStatus();
        try {
            report.setServiceCode("13661");
            programeStatus = this.center16_Jula_2015(report);

            report.setServiceCode("13756");
            programeStatus = this.center16_Jula_2015(report);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return programeStatus;
    }
}
