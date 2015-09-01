/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.support;

import com.claim.dao.UtilDao;
import java.text.DecimalFormat;

/**
 *
 * @author Poolsawat.a
 */
public class NumberUtil {

    public static int[] INDEX_YEAR(int limit) {
        int[] years = new int[limit + limit];
        Integer year = Integer.parseInt(new UtilDao().getCurrentYearFromServer());
        int j = 0;
        for (int i = year + limit; i > (year - limit); i--) {
            years[j] = i;
            j++;
        }
        return years;
    }

    public String thaiBaht(String number) {
        String[] num = {"ศูนย์", "หนึ่ง", "สอง", "สาม", "สี่", "ห้า", "หก", "เจ็ด", "แปด", "เก้า", "สิบ"};
        String[] rank = { "", "สิบ", "ร้อย", "พัน", "หมื่น", "แสน", "ล้าน" };        
        String bahtTxt, n, bahtTH = "";
        double amount;
        try {
            amount = Double.parseDouble(number);
        } catch (NumberFormatException e) {            
            e.printStackTrace();
            amount = 0;
        }
        
        bahtTxt = numberDigiit(amount, 2);
        String[] temp = bahtTxt.split(".");
        return bahtTH;
    }

    public String numberDigiit(double number, int digit) {
        DecimalFormat df = null;
        String format = "";
        if (digit == 0) {
            format = "####0";
        } else if (digit == 1) {
            format = "####0.0";
        } else if (digit == 2) {
            format = "####0.00";
        } else if (digit == 3) {
            format = "####0.000";
        } else if (digit == 4) {
            format = "####0.0000";
        } else if (digit == 5) {
            format = "####0.00000";
        }
        df = new DecimalFormat(format);
        return df.format(number);
    }

    public static void main(String[] args) {

    }

    public int[] Mnumber() {
        try {
            int[] no = {1, 2, 3, 4, 5, 6, 7, 8, 9};
            return no;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
}
