package com.claim.constants;

import java.util.Map;

public class ConstantMessage {

    public static String APP_NAME = "OPPBKKCLAIM 3.0";
    public static String APP_NAME_START = "OPPBKKCLAIM start";

    public static String MSG_REPORT_CONFIRM = "ยืนยันการออกรายงาน";
    public static String MSG_DROP_CONFIRM = "ยืนยันการลบข้อมูล";
    public static String MSG_PROGRAME_PROCESSSING = "โปรแกรมกำลังทำงาน กรุณารอ......";
    public static String MSG_REPORT_INFOMATION = "ออกรายงาน";
    public static String MSG_REPORT_SUCCESS = "ออกรายงานเรียบร้อยแล้ว";
    public static String MSG_REPORT_COMPLETE = "เสร็จสิ้น";
    public static String MSG_NO_DATA_FOR_REPORT = "ไม่พบข้อมูลในการออกรายงาน";
    public static String MSG_NO_DATA = "ไม่พบข้อมูลในการออกรายงาน";
    public static String MSG_CAN_NOT_REPORT_ERROR = "ไม่สามารถออกรายงานได้";
    public static String MSG_PROCESS_FAILS = "โปรแกรมเกิดข้อผิดพลาด: ";
    public static String MSG_CONTACT_ADMIN = "กรุณาติดต่อเจ้าหน้าที่";
    public static String MSG_REPORT_NO_DATA = "ไม่มีข้อมูลที่จะออกรายงาน";

    public static String MSG_FILE_LOCATION_PLEASE = "กรุณาเลือกสถานที่เก็บไฟล์";
    public static String MSG_FILE_LOCATION = "เลือกที่เก็บไฟล์ก่อน";

    public static String MSG_INDV_LATE_PLEASE = "กรุณาเลือกสถานะการส่งข้อมูล";
    public static String MSG_INDV_LATE = "เลือกสถานะการส่งข้อมูล";

    public static String MSG_HOSPITAL_SELECT_PLEASE = "กรุณาเลือกหน่วยบริการก่อนการออกรายงาน";
    public static String MSG_HOSPITAL_TYPE = "กรุณาเลือกลักษณะหน่วยให้บริการ (เดี๋ยว / เครือ)";
    public static String MSG_ENTER_DATA = "กรอกข้อมูล ให้ครบ";
    public static String MSG_SELECT_DATA = "กรุณาเลือก";
    
    public static String MSG_ENTER_STMP = "กรุณากรอกเลข stmp";

    public static String MSG_SELECT_DATE_PLEASE = "กรุณาเลือกปีเดือนที่ออกรายงาน";
    
    public static String MSG_SELECT_REPORT_TYPE = "กรุณาเลือกประเภทรายงาน";

    public static String MSG_REPORT_TYPE = "กรุณาเลือกประเภทการออกรายงาน (หัก/จ่าย)";
    public static String MSG_HOSPITAL_ATTRIBUTE = "กรุณาเลือกรูปแแบบหน่วยบริการ";

    public static String MSG_REPOTR_FORMAT = "กรุณาเลือกรูปแบบการออกรายงาน (รายละเอียด/สรุป)";

    public static String MSG_SELECT_NO = "กรุณาเลือกงวดที่";

    public static String MSG_SELECT_NO_FOR_PAYMENT = "กรุณาเลือกงวด การจ่ายเงิน";

    public static String MSG_197_HIGTHCOST = "กรุณาเลือกหัวข้อ   197 รายการ / High Cost";

    public static String MSG_PROGRAME_CONFIRM = "ยืนยันการทำงาน";

    public static String MSG_PROGRAME_EXIT = "ยืนยันการออกจากโปรแกรม";

    public static String MSG_LIST_NULL_WARNING = "ท่านยังไม่ได้ทำการเลือก หน่วยบริการที่จะออกรายงาน";

    public static String MSG_LIST_SELECT = "กรุณาเลือก หน่วยบริการ ก่อนการ กดปุ่ม OK";

    public static String MSG_CLEARING_ERROR = "RPT Clearinghouse: ";
    public static String MSG_OPAE74_ERROR = "RPT Opae74 พิการ: ";
    public static String MSG_TYPE5_ERROR = "RPT Type5: ";
    public static String MSG_INDIVIDUAL_ERROR = "RPT Individual: ";
    public static String MSG_OPREFER_ERROR = "RPT Oprefer: ";

    public static String MSG_CONNECTION_FAIL = " ************ เกิดข้อผิดพลาดในการเชื่อมต่อ ฐานข้อมูล ************";

    public String[] STMP_FIX = new String[]{"201402-1", "201403-1", "201404-1", "201404-2", "201405-1", "201406-1"};

    //Arrays.asList(yourArray).contains(yourChar)
    public static String[] STMP_CLEARING_FIX = new String[]{"201503-1", ""};
    public static boolean IS_SHOW_QUERY = false;
    public static int ROWNUM_LIST_LOGREPORT = 100;

    /*
     แพทย์แผนไทย TMD   Variable 
    */
    public static String TMD_ACT = "RPT_OPBKK_TMD_ACT";
    public static String TMD_MOM = "RPT_OPBKK_TMD_MOM";
    
    
}
