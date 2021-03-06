/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.object;

import com.claim.support.StringOpUtil;

/**
 *
 * @author poon__000
 */
public class ObjRptIndividual {

    private int no;
    private String hcode;
    private String hcodename;
    private String pid;
    private String pname;
    private String hn;
    private String dateopd;
    private String dateopd_thai_buddha;
    private String senddate_thai_buddha;
    private String optypename;
    private String senddate;
    private String indv_late;
    private String maininscl;
    private double totalreimburse;
    private String txid;
    private String invoice_no;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getHn() {
        return hn;
    }

    public void setHn(String hn) {
        this.hn = hn;
    }

    public String getDateopd() {
        return dateopd;
    }

    public void setDateopd(String dateopd) {
        this.dateopd = dateopd;
    }

    public String getDateopd_thai_buddha() {
        return dateopd_thai_buddha;
    }

    public void setDateopd_thai_buddha(String dateopd_thai_buddha) {
        this.dateopd_thai_buddha = dateopd_thai_buddha;
    }

    public String getSenddate_thai_buddha() {
        return senddate_thai_buddha;
    }

    public void setSenddate_thai_buddha(String senddate_thai_buddha) {
        this.senddate_thai_buddha = senddate_thai_buddha;
    }

    public String getOptypename() {
        return optypename;
    }

    public void setOptypename(String optypename) {
        this.optypename = optypename;
    }

    public String getSenddate() {
        return senddate;
    }

    public void setSenddate(String senddate) {
        this.senddate = senddate;
    }

    public String getIndv_late() {
        return indv_late;
    }

    public void setIndv_late(String indv_late) {
        this.indv_late = indv_late;
    }

    public String getMaininscl() {
        return maininscl;
    }

    public void setMaininscl(String maininscl) {
        this.maininscl = maininscl;
    }

    public double getTotalreimburse() {
        return totalreimburse;
    }

    public void setTotalreimburse(double totalreimburse) {
        this.totalreimburse = totalreimburse;
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public String getHcode() {
        return hcode;
    }

    public void setHcode(String hcode) {
        this.hcode = hcode;
    }

    public String getHcodename() {
        return hcodename;
    }

    public void setHcodename(String hcodename) {
        this.hcodename = hcodename;
    }

    public String getInvoice_no() {
        return StringOpUtil.removeNull(invoice_no);
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }
    
    
}

/*
 

 public void writeExcell(List<HospitalService> listHospital,OppReport report,int excelNumber) {
 FileOutputStream out = null;
 FileInputStream file = null;
 ProgrameStatus programeStatus = new ProgrameStatus();
 try {
 for (int j = 0; j < listHospital.size(); j++) {

 file = new FileInputStream(new File("."+File.separator+"xls"+File.separator+"indivi_detail.xls"));

 //out = new FileOutputStream(report.getPathFile() + ""+File.separator+"individual_detail_" + report.getYearMonth() + "-" + report.getNo() + "_" + listHospital.get(j).getHosCode() + ".xls");
 out = new FileOutputStream(report.getPathFile() + ""+File.separator+"individual_detail_" + 
 report.getYearMonth() + "-" + report.getNo() + "_" + 
 listHospital.get(j).getHosCode() +"_n_"+excelNumber+ ".xls");

 HSSFWorkbook wbIndivi = new HSSFWorkbook(file);
 this.loadStyle(wbIndivi);

 //System.out.println("hospitalService.getHosCode===>>" + listHospital.get(j).getHosCode());

 String EXCELL_HEADER = "รายละเอียดการจ่ายชดเชยค่าข้อมูลผู้ป่วยนอกรายบุคคล ปีงบประมาณ 2557";
 String EXCELL_MONTH = report.getTitleTimeReport();
 String EXCELL_SERVICE = "หน่วยบริการ: " + listHospital.get(j).getHosName() + " (" + listHospital.get(j).getHosCode() + ")";

 HSSFSheet sheet = wb.getSheetAt(0);
 sheet.createFreezePane(3, 5);

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
 int i = 1;
 ResultSet rs = null;

 try {
 report.setServiceCode(listHospital.get(j).getHosCode().toString());
 rs = indiDao.getReportIndividual(report);

 while (rs.next()) {
 int vNo = rs.getInt("no");
 String vPID = rs.getString("PID");
 String vPNAME = rs.getString("PNAME");
 String vHN = rs.getString("HN");
 String vDATEOPD = rs.getString("DATEOPD");
 String vDateOpdTh = rs.getString("dateopd_thai_buddha");
 String vSendDateTh = rs.getString("SENDDATE_thai_buddha");
 String vOptypename = rs.getString("optypename");
 String vSENDDATE = rs.getString("SENDDATE");
 String vINDV_LATE = rs.getString("INDV_LATE");
 String vMAININSCL = rs.getString("MAININSCL");
 double vTOTALREIMBURSE = rs.getDouble("TOTALREIMBURSE");

 row = sheet.createRow(curRow);
 row.setHeight((short) 360);
 cell = row.createCell(0);
 cell.setCellValue(i);
 cell.setCellStyle(csNum4);


 cell = row.createCell(1);
 cell.setCellValue(function.subStringPid(vPID));
 cell.setCellStyle(csDetailPid);

 cell = row.createCell(2);
 cell.setCellValue(vPNAME);
 cell.setCellStyle(csDetailLeft);

 cell = row.createCell(3);
 cell.setCellValue(vHN);
 cell.setCellStyle(csDetail2Center);

 cell = row.createCell(4);
 cell.setCellValue(vDateOpdTh);
 cell.setCellStyle(csDetail2Center);

 cell = row.createCell(5);
 cell.setCellValue(vSendDateTh);
 cell.setCellStyle(csDetail2Center);

 cell = row.createCell(6);
 cell.setCellValue(vINDV_LATE);
 cell.setCellStyle(csNum4);

 cell = row.createCell(7);
 cell.setCellValue(vOptypename);
 cell.setCellStyle(csNum4);

 cell = row.createCell(8);
 cell.setCellValue(vMAININSCL);
 cell.setCellStyle(csDetailLeft);

 cell = row.createCell(9);
 cell.setCellValue(vTOTALREIMBURSE);
 cell.setCellStyle(csNum2);

 curRow++;
 i++;
 }

 } catch (SQLException e) {
 e.printStackTrace();
 area.append("\n Exception: " + e.toString());
 programeStatus.setMessage(Constants.MSG_PROCESS_FAILS + e.toString());
 programeStatus.setTitle(Constants.MSG_CONTACT_ADMIN);
 programeStatus.setProcessStatus(false);
 } finally {
 this.indiDao.closeConnection();
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
 cell.setCellStyle(csNum2B);

 // หมายเหตุ
 row = sheet.createRow(curRow + 2);
 row.setHeight((short) 450);
 cell = row.createCell(0);
 cell.setCellValue("หมายเหตุ :   เนื่องจากปีงบประมาณ 2557 สำนักงานหลักประกันสุขภาพแห่งชาติ เขต 13 กรุงเทพมหานคร  ได้นำทุก Visit  ของการจ่ายชดเชย "
 + "มารวมจ่ายในค่าข้อมูลด้วย จึงจ่ายชดเชยเพียงวันละ Visit เท่านั้น");
 sheet.addMergedRegion(new CellRangeAddress(curRow + 2, curRow + 3, 0, 9));
 cell.setCellStyle(csDetail2);


 wb.setSheetName(0, listHospital.get(j).getHosCode().toString());

 wb.write(out);

 out.close();
 file.close();

 area.append("\n ออกรายงาน: " + listHospital.get(j).getHosCode().toString() + "เรียบร้อย");
 }
 } catch (Exception e) {
 e.printStackTrace();
 }
 }
 */
