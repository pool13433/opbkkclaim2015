/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.support;

import com.claim.constants.ConstantVariable;
import com.claim.support.Console;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FunctionUtil {

    public String subStringPid(String personalId) {
        String pid = "";
        try {
            //System.out.println("personalId++"+personalId);
            //1-2090-00055-83-7

            // x-xxxx-xxxxx-xx-x  ,
            // 3-130-60015-43-71
            String p1 = personalId.substring(0, 1);
            String p2 = personalId.substring(1, 5);
            String p3 = personalId.substring(5, 10);
            String p4 = personalId.substring(10, 12);
            String p5 = personalId.substring(12, 13);

            pid = p1 + "-" + p2 + "-" + p3 + "-" + p4 + "-" + p5;
            return pid;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return pid;
    }

    public String[] subStringStmp(String stmp) {
        String[] arrStmp = new String[3];
        //System.out.println("stmp.length===>>>>" + stmp);
        //System.out.println(" stmp.substring(0, 4);===>>" + stmp.substring(0, 4));
        //System.out.println(" stmp.substring(4, 6);===>>" + stmp.substring(4, 6));
        //System.out.println(" stmp.substring(6, 7);===>>" + stmp.substring(7, 8));
        arrStmp[0] = stmp.substring(0, 4).toString();
        arrStmp[1] = stmp.substring(4, 6).toString();
        if(stmp.length() >8 ){  //201507-10
            arrStmp[2] = stmp.substring(7, 9).toString();
        }else{
            arrStmp[2] = stmp.substring(7, 8).toString();
        }

        return arrStmp;
    }

    public String convertMonthThai(String keyMonth) {
        String monthTh = "";
        try {
            Map<String, String> map = new HashMap();
            map.put("01", ConstantVariable.MMonth[1]);
            map.put("02", ConstantVariable.MMonth[2]);
            map.put("03", ConstantVariable.MMonth[3]);
            map.put("04", ConstantVariable.MMonth[4]);
            map.put("05", ConstantVariable.MMonth[5]);
            map.put("06", ConstantVariable.MMonth[6]);
            map.put("07", ConstantVariable.MMonth[7]);
            map.put("08", ConstantVariable.MMonth[8]);
            map.put("09", ConstantVariable.MMonth[9]);
            map.put("10", ConstantVariable.MMonth[10]);
            map.put("11", ConstantVariable.MMonth[11]);
            map.put("12", ConstantVariable.MMonth[12]);

            for (Map.Entry<String, String> entry : map.entrySet()) {
                // System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
                if (keyMonth.equals(entry.getKey())) {
                    monthTh = entry.getValue();
                }
            }
            if (monthTh == null) {
                monthTh = "-";
            }
            return monthTh;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return monthTh;
    }

    public static Map subStringYearMonth(String ym) {
        Map<String, String> mapYM = new HashMap();
        try {
            System.out.println(" ym :: " + ym);
            if (!ym.equals("-")) {
                mapYM.put("Y", ym.substring(0, 4));
                mapYM.put("M", ym.substring(4, 6));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapYM;
    }

    public Integer convertYearFromAD_to_BE(String yearAD) {
        Integer yearBE = null;
        try {
            yearBE = Integer.parseInt(yearAD) + 543;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return yearBE;
    }

    public String getTitleTimeReport(Object obYM, Object obN) {
        StringBuilder titleTime = null;
        try {
            if (obYM != null) {
                //System.out.println("obYM==>>" + obYM);
                String obY = subStringYearMonth(obYM.toString()).get("Y").toString(); // Year
                String obM = subStringYearMonth(obYM.toString()).get("M").toString(); //Month

                if (obY != null) {
                    titleTime = new StringBuilder();
                    titleTime.append(" งวดการจ่ายเงิน");
                    titleTime.append(obYM).append("-").append(obN);
                    titleTime.append(": เดือน ");
                    titleTime.append(convertMonthThai(obM)).append(" ");
                    titleTime.append(convertYearFromAD_to_BE(obY)).append(" ครั้งที่ ");
                    titleTime.append(obN);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return titleTime.toString();
    }

    public  void openDirectoryAutomatic(String pathDirectory) throws IOException {
        Desktop desktop = Desktop.getDesktop();
        File dirToOpen = null;
        try {
            dirToOpen = new File(pathDirectory);
            desktop.open(dirToOpen);
        } catch (IllegalArgumentException iae) {
            System.out.println("File Not Found");
        }
    }

    public  boolean createFolder(String filePath) {
        boolean status = false;
        String[] directory = filePath.split(""+File.separator+""+File.separator+"");
        System.out.println(" length : " + directory.length);
        File file1 = new File(filePath);
        if (!file1.exists()) {
            file1.mkdir();
            status = true;
        } else {
            status = false;
        }
        return status;
    }

    public  boolean createMutiDirectory(File fullPath) {
        boolean status = false;
        System.out.println("fullPath ::==" + fullPath.toString());
        if (!fullPath.exists()) {
            if (fullPath.mkdirs()) {
                System.out.println("Multiple directories are created!");
                Console.LOG("สร้าง folder เก็บไฟล์", 1);
            } else {
                System.out.println("Failed to create multiple directories!");
                Console.LOG("สร้าง folder เก็บไฟล์ ไม่ได้ ", 0);
            }
        }
        return status;
    }

    public static void main(String[] args) {
        
    }

}
