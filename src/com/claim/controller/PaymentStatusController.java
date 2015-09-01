/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.controller;

import com.claim.connection.DBManage;
import java.util.ArrayList;
import java.util.List;
import com.claim.dao.PaymentStatusDao;
import com.claim.object.Obj_log_stamp_payment;
import com.claim.object.ObjPaymentResult;
import com.claim.object.ObjPaymentStatus;
import com.claim.support.Console;
import com.claim.object.ProgrameStatus;
import java.sql.Connection;
import java.sql.SQLException;
import oracle.net.ns.ConnectPacket;

/**
 *
 * @author poon__000
 */
public class PaymentStatusController {

    Connection connection = null;
    ProgrameStatus status = null;

    public ProgrameStatus startRunStore(ObjPaymentStatus objpaysta) {
        try {
            connection = new DBManage().open();
            PaymentStatusDao statusDao = new PaymentStatusDao();
            statusDao.setConnection(connection);
            status = statusDao.callOracleStoredProc(objpaysta);
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
        return status;
    }

    public List<ObjPaymentResult> getListPaymentByCase(ObjPaymentStatus objpaysta) {
        List<ObjPaymentResult> listPayRe = null;
        if (objpaysta.getPayment_type() <= 10) { // รายงานเก่า ใช้ส่วนการทำงานเก่า
            listPayRe = this.getListPaymentResult(objpaysta);
            System.out.println("index TABEL 1 ถึง 10 ::== รายงาน เก่า ใช้ส่วนการทำงานเก่า ");
        } else { // เพิ่มมาใหม่ เลยแยกส่วนกัน เม่อวันที่ 24-08-2015
            listPayRe = this.getListPaymentResultHCBegin28062015(objpaysta);
            System.out.println("index TABEL มากกว่า 10  ::== รายงาน เพิ่มมาใหม่ เลยแยกส่วนกัน เมื่อวันที่ 24-08-2015");
        }
        return listPayRe;
    }

    public List<ObjPaymentResult> getListPaymentResult(ObjPaymentStatus objpaysta) {
        List<ObjPaymentResult> listPayRe = new ArrayList<ObjPaymentResult>();
        try {
            connection = new DBManage().open();
            PaymentStatusDao statusDao = new PaymentStatusDao();
            statusDao.setConnection(connection);

            System.out.println("objpaysta.getPayment_status() :: ==" + objpaysta.getPayment_status());
            //"-", "OPTYPE 6[ClearingHouse]", "OPAE 74", "OPTYPE 5[OP บัตรตัวเอง]", "Individual", "Refer/AE"
            System.out.println("objpaysta.getPayment_type() ::==" + objpaysta.getPayment_type());
            switch (objpaysta.getPayment_type()) {
                case 0:

                    break;
                case 1:
                    listPayRe = statusDao.getPaymentResult_clearinghouse(objpaysta.getStmp(), objpaysta.getPayment_status());
                    break;
                case 2:
                    listPayRe = statusDao.getPaymentResult_ae74(objpaysta.getStmp(), objpaysta.getPayment_status());
                    break;
                case 3:
                    List<ObjPaymentResult> list197Less201501_1 = statusDao.getPaymentResult_type5_197(objpaysta.getStmp(), objpaysta.getPayment_status(), "1");
                    List<ObjPaymentResult> list197Over201501_1 = statusDao.getPaymentResult_type5_197(objpaysta.getStmp(), objpaysta.getPayment_status(), "1,5");
                    listPayRe.addAll(list197Less201501_1);
                    listPayRe.addAll(list197Over201501_1);
                    break;
                case 4: // Type5 HightCost                    
                    List<ObjPaymentResult> listHcLess201501_1 = statusDao.getPaymentResult_type5_HightCost(objpaysta.getStmp(), objpaysta.getPayment_status(), "3");
                    List<ObjPaymentResult> listHcOver201501_1 = statusDao.getPaymentResult_type5_HightCost(objpaysta.getStmp(), objpaysta.getPayment_status(), "3,4");
                    listPayRe.addAll(listHcLess201501_1);
                    listPayRe.addAll(listHcOver201501_1);
                    break;
                case 5:
                    listPayRe = statusDao.getPaymentResult_individual(objpaysta.getStmp(), objpaysta.getPayment_status());
                    break;
                case 6:
                    listPayRe = statusDao.getPaymentResult_refer(objpaysta.getStmp(), objpaysta.getPayment_status());
                    break;
                case 7: // chula ว่างไว้ก่อน
                    listPayRe = statusDao.getPaymentResult_chula16(objpaysta.getStmp(), objpaysta.getPayment_status());
                    break;
                case 8: //noni
                    listPayRe = statusDao.getPaymentResult_noni(objpaysta.getStmp(), objpaysta.getPayment_status());
                    break;
                case 9: //ThaiMedicine ACT
                    listPayRe = statusDao.getPaymentResult_tmdAct(objpaysta.getStmp(), objpaysta.getPayment_status());
                    break;
                case 10: ////ThaiMedicine MOM
                    listPayRe = statusDao.getPaymentResult_tmdMom(objpaysta.getStmp(), objpaysta.getPayment_status());
                    break;
                default:
                    listPayRe = new ArrayList<ObjPaymentResult>();
                    Console.LOG("ไม่พบงวดที่จะดูข้อมูล", 1);
                    break;
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
        return listPayRe;
    }

    public List<ObjPaymentResult> getListPaymentResultHCBegin28062015(ObjPaymentStatus objpaysta) {
        List<ObjPaymentResult> listPayRe = new ArrayList<ObjPaymentResult>();        
        try {
            connection = new DBManage().open();
            PaymentStatusDao statusDao = new PaymentStatusDao();
            statusDao.setConnection(connection);
            /*
             * objpaysta.getPayment_type() > 10
             */
            switch (objpaysta.getPayment_type()) {
                case 11: // 
                    listPayRe = statusDao.getPaymentResult_vajira11535(objpaysta.getStmp(), objpaysta.getPayment_status());
                    break;
                case 12:
                    listPayRe = statusDao.getPaymentResult_vajiraRf(objpaysta.getStmp(), objpaysta.getPayment_status());
                    break;
                case 13:
                    listPayRe = statusDao.getPaymentResult_vajira(objpaysta.getStmp(), objpaysta.getPayment_status());
                    break;
                default:
                    listPayRe = new ArrayList<ObjPaymentResult>();
                    Console.LOG("ไม่พบงวดที่จะดูข้อมูล", 1);
                    break;
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
        return listPayRe;
    }

    public Obj_log_stamp_payment getLastLog() {
        Obj_log_stamp_payment obj = null;
        try {
              connection = new DBManage().open();
            PaymentStatusDao paymentStatusDao = new PaymentStatusDao();
            paymentStatusDao.setConnection(connection);
            obj = paymentStatusDao.getlastLogStampPayment();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }
}
