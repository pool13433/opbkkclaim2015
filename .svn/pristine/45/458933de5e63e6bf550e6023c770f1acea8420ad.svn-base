package com.claim.controller;

import com.claim.dao.CreateDataStoreDAO;
import com.claim.object.ObjCallStoreCreateData;
import com.claim.object.ProgrameStatus;
import java.sql.SQLException;

public class CreateDataStoreController {

    private CreateDataStoreDAO createStoreDAO = null;

    public CreateDataStoreController() {
        createStoreDAO = new CreateDataStoreDAO();
    }

    public ProgrameStatus startRunStore(ObjCallStoreCreateData bean) throws SQLException {

        ProgrameStatus status = new ProgrameStatus();

        if (bean.getGroup_report_type() == 1) {
            //check duppicate stmp
            boolean is_check = createStoreDAO.checkStmpDupicateGroupReportType1(bean.getIndex_table(), bean.getStmp().substring(0, 6));
            if (is_check) {
                // ซ้ำ
                status.setTitle(" System Warning");
                status.setMessage(" STMP นี้ สร้างข้อมูล ไปแล้ว");
                status.setProcessStatus(false);
            } else {
                //System.out.println("bean ::=="+bean.toString());
                status = createStoreDAO.callOracleStoredGroupReportType1(bean);

            }

        } else { // GroupReportType Vajira
            //check duppicate stmp
            boolean is_check = createStoreDAO.checkStmpDupicateGroupReportType2Vajira(bean.getIndex_table(), bean.getStmp().substring(0, 6));
            if (is_check) {
                // ซ้ำ
                status.setTitle(" System Warning");
                status.setMessage(" STMP นี้ สร้างข้อมูล ไปแล้ว");
                status.setProcessStatus(false);
            } else {
                //System.out.println("bean ::=="+bean.toString());
                status = createStoreDAO.callOracleStoredGroupReportType2Vajira(bean);

            }

        }

        return status;
    }

    public String getLogReport(int keyReport) {
        return this.createStoreDAO.getLogReportLastRecord(keyReport);
    }

    public boolean checkCreateDataDupicate(ObjCallStoreCreateData bean) {
        boolean status = false;
        //check duppicate stmp
        System.out.println(" getStmpyyyymm_no: " + bean.getStmpyyyymm_no());
        System.out.println(" bean.getStmp_no() : " + bean.getStmp_no());

        status = createStoreDAO.checkStmpDupicateGroupReportType1(bean.getIndex_table(), bean.getStmpyyyymm_no());
        System.out.println("checkCreateDataDupicate : " + status);
        return status;
    }
    public boolean checkCreateDataDupicateGroupReportTypeVajira(ObjCallStoreCreateData bean) {
        boolean status = false;
        //check duppicate stmp
        System.out.println(" getStmpyyyymm_no: " + bean.getStmpyyyymm_no());
        System.out.println(" bean.getStmp_no() : " + bean.getStmp_no());

        status = createStoreDAO.checkStmpDupicateGroupReportType2Vajira(bean.getIndex_table(), bean.getStmpyyyymm_no());
        System.out.println("checkCreateDataDupicate : " + status);
        return status;
    }
    
}
