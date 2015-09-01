package com.claim.support;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class StringOpUtil {

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static Random rnd = new Random();

    public static String removeNull(String object) {
        String result = "";
        if (object == null) {
            result = "";
        } else {
            result = object;
        }
        return result;
    }

    public static String removeNullTo(String object, String replaceValue) {
        String result = "";
        if (object == null) {
            if (replaceValue == null || replaceValue.equals("")) {
                result = "";
            } else {
                result = replaceValue;
            }
        } else {
            result = object;
        }
        return result;
    }

    public String genarateFileNameExcell(String directory, String type, String category, String hcode, String stmp) {
        String newfileName = "";
        String directoryUpper = directory.toLowerCase();

        if (directoryUpper.equals("oprf")) {

        } else if (directoryUpper.equals("ae74")) {

        } else if (directoryUpper.equals("type5")) {

        } else if (directoryUpper.equals("clh")) {

        } else if (directoryUpper.equals("indv")) {

        } else if (directoryUpper.equals("")) {

        }
        return newfileName;
    }

    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }

    public List<Integer> MYear() {
        try {
            Locale local = new Locale("en", "EN");
            Date dateNow = new Date();
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy", local);
            Integer intYear = Integer.parseInt(format1.format(dateNow).toString());
            List<Integer> listYear = new ArrayList<Integer>();
            for (int i = intYear + 543; i > ((intYear + 542) - 50); i--) {
                if (i > 2555) {
                    listYear.add(new Integer(i));
                }
            }
            return listYear;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
