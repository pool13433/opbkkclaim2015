/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.support;

import com.claim.constants.ConstantVariable;
import com.claim.dao.UtilDao;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Poolsawat.a
 */
public class DateUtil {

    public final static String DATE_FORMAT = "dd-MM-yyyy";//"yyyyMMdd";
    public final static String DATE_FORMAT_TO = "yyyyMMdd";
    public final static String DATE_DEFAULT = "01-10-2014";//"20141001";
    public final static String DATE_DEFAULT_HC = "28-06-2015";//"20141001";
    public static String DATE_CURRENT = "";

    public int yearADToBC(int yearAD) {
        return yearAD + 543;
    }

    public String getBudgeMonthYear_543(String stmp, int finalBudgetMonth) {
        if (stmp.length() >= 6) {//ex. 201412-1
            String YYYY = stmp.substring(0, 4);
            String MM = stmp.substring(4, 6);
            String ROUND = stmp.substring(7, 8);
            String YYYYMM = YYYY + MM;
            if (new DateUtil().checkDateOpdCurrentBudgetYear(YYYYMM, finalBudgetMonth)) {
                System.out.println("step 1");
                if (Integer.parseInt(ROUND) == 0) { // แสดงว่างวดปลายปี
                    System.out.println("step 2");
                    return String.valueOf(Integer.parseInt(YYYY) + 543); // example : 2014+544 = 2557 เพราะปลายปี
                } else {
                    System.out.println("step 3");
                    if (Integer.parseInt(MM) <= finalBudgetMonth) {
                        return String.valueOf(Integer.parseInt(YYYY) + 543); // example : 2014+543 = 2557
                    } else {
                        return String.valueOf(Integer.parseInt(YYYY) + 544); // example : 2014+544 = 2558
                    }
                }
            } else {
                System.out.println("step 4");
                if (Integer.parseInt(MM) <= finalBudgetMonth) {
                    return String.valueOf(Integer.parseInt(YYYY) + 543); // example : 2014+543 = 2557
                } else {
                    return String.valueOf(Integer.parseInt(YYYY) + 544); // example : 2014+544 = 2558
                }
            }
        } else {
            return "";
        }
    }

    public boolean checkDateOpdCurrentBudgetYear(String dateopdEnd, int minMouth) {
        boolean is = false;
        String day = "";
        try {
            //int current_year = Calendar.getInstance().get(Calendar.YEAR);            
            int current_year = Integer.parseInt(new UtilDao().getCurrentYearFromServer());
            System.out.println("Current Year ::==" + String.valueOf(current_year));
            //20131001
            System.out.println("getOpd_date_stop : " + dateopdEnd);
            String year = dateopdEnd.substring(0, 4);
            String month = dateopdEnd.substring(4, 6);
            if (dateopdEnd.length() > 6) {
                day = dateopdEnd.substring(6, 8);
            }

            System.out.println("year : " + year);
            System.out.println("month : " + month);
            System.out.println("day : " + day);
            if (Integer.parseInt(year) >= current_year) { // 2015 ขึ้นไป
                System.out.println("Integer.parseInt(year) > 2014 ");
                if (Integer.parseInt(month) < minMouth) { // เดือน 10 ขึ้นไป
                    System.out.println("Integer.parseInt(month) > 9");
                    is = true;
                } else {
                    is = false;
                }
            } else if (Integer.parseInt(year) < current_year) {
                if (Integer.parseInt(month) > minMouth) { // เดือน 10 ขึ้นไป
                    System.out.println("Integer.parseInt(month) > 9");
                    is = true;
                } else {
                    is = false;
                }
            } else {
                is = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("isCheck : " + is);
        return is;
    }

    public boolean checkStmpBudgetYear2015(String stmp, int maxMonthBudget) {
        boolean is = false;
        if (stmp.length() >= 6) { // 201410-1
            String year = stmp.substring(0, 4); // 2014
            String month = stmp.substring(4, 6); // 10
            String space = stmp.substring(6, 7); // - 
            String round = stmp.substring(7, 8);  // 1
            int current_year = Integer.parseInt(new UtilDao().getCurrentYearFromServer());
            if ((current_year == Integer.parseInt(year) && Integer.parseInt(month) <= maxMonthBudget)
                    || (Integer.parseInt(year) == (current_year - 1) && Integer.parseInt(month) > maxMonthBudget)) {
                is = true;
            } else if ((current_year == Integer.parseInt(year) && Integer.parseInt(month) > maxMonthBudget)
                    || (Integer.parseInt(year) == (current_year - 1) && Integer.parseInt(month) <= maxMonthBudget)) {
                is = false;
            }
        } else {
            return is;
        }
        return is;
    }

    public String convertStmpToString(String stmp) {
        // stmp 201501-1
        FunctionUtil function = new FunctionUtil();
        if (stmp.length() >= 8) {
            String year = stmp.substring(0, 4);
            String month = stmp.substring(4, 6);
            String no = stmp.substring(7, 8);
            //System.out.println("month : " + month);
            //System.out.println("year_2 : " + year);
            int year_2 = 0;
            year_2 = Integer.parseInt(stmp.substring(2, 4)) + 543;
            //System.out.println("year_2 : " + year_2);
            String result = "";
            try {
                result = ConstantVariable.INDEX_AUTO[(Integer.parseInt(no) + 2)]; // '-','1','2',...
                result += " ( " + ConstantVariable.MMONTH_SHORT[Integer.parseInt(month)] + " ";
                result += String.valueOf(year_2).substring(1, 3);
                result += " ) ";
                System.out.println("result : " + result);
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            return result;
        } else {
            return "";
        }
    }

    public String convertStmpToNoniString(String stmp) {
        // stmp 201501-1
        FunctionUtil function = new FunctionUtil();
        String result = "";
        if (stmp.length() >= 8) {
            String year = stmp.substring(0, 4);
            String month = stmp.substring(4, 6);
            String no = stmp.substring(7, 8);
            //System.out.println("month : " + month);
            //System.out.println("year_2 : " + year);
            int year_2 = 0;
            year_2 = Integer.parseInt(stmp.substring(2, 4)) + 543;
            //System.out.println("year_2 : " + year_2);
            int year_sub2 = Integer.parseInt(String.valueOf(year_2).substring(1, 3));
            if (year_sub2 > 57) {
                result = "NONI" + String.valueOf(year_2).substring(1, 3) + "-" + month;
            } else {
                result = "NONI57-01";
            }
            System.out.println("result : " + result);
            return result;
        } else {
            return "";
        }
    }

    public String getDateTimeCurrent() {
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); //dd/mm/yyyy hh24:mi:ss
        String formattedDate = sdf.format(today);
        System.out.println(formattedDate); // 12/01/2011 4:48:16 PM
        return formattedDate;
    }

    public String getDateTimeForward(int day) {
        Date today = new Date();
        Date tomorrow = new Date(today.getTime() + (day * 1000 * 60 * 60 * 24));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); //dd/mm/yyyy hh24:mi:ss
        String formattedDate = sdf.format(tomorrow);
        System.out.println(formattedDate); // 12/01/2011 4:48:16 PM
        return formattedDate;
    }

    public String convertFormatData(Date dateConvert) {
        try {
            SimpleDateFormat formatter5 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            String formats1 = formatter5.format(dateConvert);
            System.out.println(formats1);

            return formats1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int numberOfDays(String fromDate, String toDate) {
        java.util.Calendar cal1 = new java.util.GregorianCalendar();
        java.util.Calendar cal2 = new java.util.GregorianCalendar();

        //split year, month and days from the date using StringBuffer.
        StringBuffer sBuffer = new StringBuffer(fromDate);
        String yearFrom = sBuffer.substring(6, 10);
        String monFrom = sBuffer.substring(0, 2);
        String ddFrom = sBuffer.substring(3, 5);
        int intYearFrom = Integer.parseInt(yearFrom);
        int intMonFrom = Integer.parseInt(monFrom);
        int intDdFrom = Integer.parseInt(ddFrom);

        // set the fromDate in java.util.Calendar
        cal1.set(intYearFrom, intMonFrom, intDdFrom);

        //split year, month and days from the date using StringBuffer.
        StringBuffer sBuffer1 = new StringBuffer(toDate);
        String yearTo = sBuffer1.substring(6, 10);
        String monTo = sBuffer1.substring(0, 2);
        String ddTo = sBuffer1.substring(3, 5);
        int intYearTo = Integer.parseInt(yearTo);
        int intMonTo = Integer.parseInt(monTo);
        int intDdTo = Integer.parseInt(ddTo);

        // set the toDate in java.util.Calendar
        cal2.set(intYearTo, intMonTo, intDdTo);

        //call method daysBetween to get the number of days between two dates
        int days = daysBetween(cal1.getTime(), cal2.getTime());
        return days;
    }

    //This method is called by the above method numberOfDays
    public int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));

        /*
         use Example         
         DateUtil dateUtil = new DateUtil();
         int num= dateUtil.numberOfDays("04-19-2013", "07-19-2013");
         System.out.println("Number of days between mentioned dates are: "+num);
         */
    }

    public Date parseStringToDate(String date, String format) {
        try {
            System.out.println("date.length() ::==" + date.length());
            System.out.println("format.length() ::==" + format.length());
            System.out.println("format ::==" + format);
            if (date.length() == format.length()) {

                SimpleDateFormat formatter = new SimpleDateFormat(format);
                return formatter.parse(date);
            }
        } catch (ParseException e) { //throws ParseException 
            e.printStackTrace();
        }
        return null;
    }

    public String parseDateToString(Date date, String format) {
        SimpleDateFormat formatter5 = new SimpleDateFormat(format);
        String formats1 = formatter5.format(date);
        System.out.println(formats1);
        return formats1;
    }

    public int numberOfDateopdDays(String dateopdStart, String dateopdEnd) {
        Date opdStartDate = this.parseStringToDate(dateopdStart.substring(0, 8), "yyyyMMdd");
        String opdStartString = this.parseDateToString(opdStartDate, "dd-MM-yyyy");
        Date opdEndDate = this.parseStringToDate(dateopdEnd.substring(0, 8), "yyyyMMdd");
        String opdEndString = this.parseDateToString(opdEndDate, "dd-MM-yyyy");
        int numberOfDays = this.numberOfDays(opdStartString, opdEndString);
        System.out.println("numberOfDays ::==" + numberOfDays);
        return numberOfDays;
    }

    public String changeDateFormat(String date, String sourceFormat, String destinationFormat) {
        String returnDate = "";
        try {
            if (!StringOpUtil.removeNull(date).equals("")) {
                SimpleDateFormat formatter = new SimpleDateFormat(sourceFormat);
                Date date1 = formatter.parse(date);

                formatter = new SimpleDateFormat(destinationFormat);
                returnDate = formatter.format(date1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnDate;
    }

    public static void main(String[] args) {
        UtilDao dateUtil = new UtilDao();
        //int datediff = dateUtil.getDifferenceBetween2date("201510", "201502");

        System.out.println(" change ::==" + new DateUtil().changeDateFormat("20-08-2015", "dd-MM-yyyy", "yyyyMMdd"));

        //System.out.println(" days_diff ::==" + dateUtil.getDifferenceBetween2date("201501", "201501"));
        //new DateUtil().getDateTimeForward(1);
        //System.out.println("new DateUtil().getCurrentYearFromServer() ::==" + new DateUtil().getCurrentYearFromServer());
        //System.out.println("DATE : " + new DateUtil().CheckDateOpdBudgetYear2015("201408", 7));
        //new DateUtil().convertStmpToString("201501-1");
        //new DateUtil().convertStmpToNoniString("201501-1");
        //new DateUtil().CheckDateOpdBudgetYear2015("201501-4", 9);
        //new DateUtil().CheckDateOpdBudgetYear2015("20150228", 9);
        //System.out.println("Busget_year ::==" + DateUtil.getBudgeMonthYear_543("201509-1", 9));
//        System.out.println("CheckStmpBudgetYear2015 1::==" + new DateUtil().CheckStmpBudgetYear2015("201409-1", 9));
//        System.out.println("CheckStmpBudgetYear2015 2::==" + new DateUtil().CheckStmpBudgetYear2015("201410-1", 9));
//        System.out.println("CheckStmpBudgetYear2015 3::==" + new DateUtil().CheckStmpBudgetYear2015("201501-1", 9));
//        System.out.println("CheckStmpBudgetYear2015 4::==" + new DateUtil().CheckStmpBudgetYear2015("201510-1", 9));
//        System.out.println("CheckStmpBudgetYear2015 5::==" + new DateUtil().CheckStmpBudgetYear2015("201601-1", 9));
    }
}
