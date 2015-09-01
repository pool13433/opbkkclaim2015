/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.xls;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 *
 * @author Poolsawat.a
 */
public class ExcellBaseUtil {

    protected static int WIDTH_TXID = 9000; // 10000 = 273 pixel   9000 = 246
    protected static int ROW_LIMIT = 65500;   // limit excell row 65,536 rows by 256 columns
    protected static int COLUMN_LIMIT = 256;
    protected static String FONT_FAMILY = "Tahoma"; //TH SarabunPSK";   //Tahoma
    protected static int FONT_SIZE = 12;
    protected static int FONT_CUSTOM_SIZE = FONT_SIZE + 4;
    protected static Short CELL_COLOR = HSSFColor.LIGHT_TURQUOISE.index;
    protected String[] CHAR_ENG = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
        "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "AA", "AB", "AC", "AD", "AF", "AG", "AH", "AI", "AJ", "AL", "AM", "AN", "AO", "AP", "AQ", "AR", "AS", "AT",
        "AU", "AV", "AW", "AX", "AY", "AZ"
    };
    protected HSSFDataFormat df;
    protected HSSFWorkbook workbookBase;
    protected HSSFFont f1;
    protected HSSFFont f2B;
    protected HSSFFont f2;
    protected HSSFFont fhidden;
    protected HSSFCellStyle leftStyle;
    protected HSSFCellStyle csHead;
    protected HSSFCellStyle csHeadTab;
    protected HSSFCellStyle csDouble2;   // double
    protected HSSFCellStyle csNum3;  // integer
    protected HSSFCellStyle csNum3R;  // integer
    protected HSSFCellStyle csNum3L;  // integer
    protected HSSFCellStyle csDouble2B; // double
    protected HSSFCellStyle csDouble2BR; // double
    protected HSSFCellStyle csDouble2BCenter; // double
    protected HSSFCellStyle csDouble2R; // double
    protected HSSFCellStyle csNum3B;
    protected HSSFCellStyle csNum3BCenter;
    protected HSSFCellStyle csStringB; // string
    protected HSSFCellStyle csStringLeft; // string
    protected HSSFCellStyle csStringCenter;
    protected HSSFCellStyle csString2Center;
    protected HSSFCellStyle csString2;
    protected HSSFCellStyle csNum4;  // integer
    protected HSSFCellStyle csNum4R;  // integer
    protected HSSFCellStyle csNum4L;  // integer
    protected HSSFCellStyle csNum4B;
    protected HSSFCellStyle csNum4BCenter;
    protected HSSFCellStyle csNum4BR;
    protected HSSFCellStyle csNumH;
    protected HSSFCellStyle csStringPid;

    protected HSSFCellStyle csDouble2Center;

    protected HSSFCellStyle csStringtxid;

    protected void loadStyle(HSSFWorkbook wbClearing) {
        // style 
        this.workbookBase = wbClearing;

        df = workbookBase.createDataFormat();

        f1 = workbookBase.createFont();
        f1.setFontHeightInPoints((short) (FONT_CUSTOM_SIZE));
        f1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        f1.setFontName(FONT_FAMILY);

        f2B = workbookBase.createFont();
        f2B.setFontHeightInPoints((short) FONT_SIZE);
        f2B.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        f2B.setFontName(FONT_FAMILY);

        f2 = workbookBase.createFont();
        f2.setFontHeightInPoints((short) FONT_SIZE);
        f2.setFontName(FONT_FAMILY);

        fhidden = workbookBase.createFont();
        fhidden.setFontHeightInPoints((short) FONT_SIZE);
        fhidden.setFontName(FONT_FAMILY);
        fhidden.setColor((short) HSSFColor.WHITE.index);

        leftStyle = workbookBase.createCellStyle();
        leftStyle.setBorderBottom(CellStyle.BORDER_THIN);
        leftStyle.setBorderLeft(CellStyle.BORDER_THIN);
        leftStyle.setBorderRight(CellStyle.BORDER_THIN);
        leftStyle.setBorderTop(CellStyle.BORDER_THIN);

        csHead = workbookBase.createCellStyle();
        csHead.setFont(f1);
        csHead.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        csHead.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        csHead.setWrapText(true);

        csHeadTab = workbookBase.createCellStyle();
        csHeadTab.setFont(f1);
        csHeadTab.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        csHeadTab.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        csHeadTab.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csHeadTab.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csHeadTab.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csHeadTab.setBorderTop(HSSFCellStyle.BORDER_THIN);
        csHeadTab.setWrapText(true);
        csHeadTab.setFillPattern(HSSFCellStyle.FINE_DOTS);
        csHeadTab.setFillForegroundColor(CELL_COLOR);
        csHeadTab.setFillBackgroundColor(CELL_COLOR);

        csDouble2 = workbookBase.createCellStyle();
        csDouble2.setFont(f2);
        csDouble2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        csDouble2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csDouble2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csDouble2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csDouble2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        csDouble2.setDataFormat(df.getFormat("#,##0.00"));

        csNum3 = workbookBase.createCellStyle();
        csNum3.setFont(f2);
        csNum3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        csNum3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        csNum3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csNum3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csNum3.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csNum3.setBorderTop(HSSFCellStyle.BORDER_THIN);

        csNum3R = workbookBase.createCellStyle();
        csNum3R.setFont(f2);
        csNum3R.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        csNum3R.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        csNum3R.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csNum3R.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csNum3R.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csNum3R.setBorderTop(HSSFCellStyle.BORDER_THIN);

        csNum3L = workbookBase.createCellStyle();
        csNum3L.setFont(f2);
        csNum3L.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        csNum3L.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        csNum3L.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csNum3L.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csNum3L.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csNum3L.setBorderTop(HSSFCellStyle.BORDER_THIN);

        csDouble2B = workbookBase.createCellStyle();
        csDouble2B.setFont(f2B);
        csDouble2B.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        csDouble2B.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        csDouble2B.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csDouble2B.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csDouble2B.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csDouble2B.setBorderTop(HSSFCellStyle.BORDER_THIN);
        csDouble2B.setDataFormat(df.getFormat("#,##0.00"));
        csDouble2B.setFillPattern(HSSFCellStyle.FINE_DOTS);
        csDouble2B.setFillForegroundColor(CELL_COLOR);
        csDouble2B.setFillBackgroundColor(CELL_COLOR);

        csDouble2BR = workbookBase.createCellStyle();
        csDouble2BR.setFont(f2B);
        csDouble2BR.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        csDouble2BR.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        csDouble2BR.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csDouble2BR.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csDouble2BR.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csDouble2BR.setBorderTop(HSSFCellStyle.BORDER_THIN);
        csDouble2BR.setDataFormat(df.getFormat("#,##0.00"));
        csDouble2BR.setFillPattern(HSSFCellStyle.FINE_DOTS);
        csDouble2BR.setFillForegroundColor(CELL_COLOR);
        csDouble2BR.setFillBackgroundColor(CELL_COLOR);

        csDouble2BCenter = workbookBase.createCellStyle();
        csDouble2BCenter.setFont(f2B);
        csDouble2BCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        csDouble2BCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        csDouble2BCenter.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csDouble2BCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csDouble2BCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csDouble2BCenter.setBorderTop(HSSFCellStyle.BORDER_THIN);
        csDouble2BCenter.setDataFormat(df.getFormat("#,##0.00"));
        csDouble2BCenter.setFillPattern(HSSFCellStyle.FINE_DOTS);
        csDouble2BCenter.setFillForegroundColor(CELL_COLOR);
        csDouble2BCenter.setFillBackgroundColor(CELL_COLOR);

        csNum3B = workbookBase.createCellStyle();
        csNum3B.setFont(f2B);
        csNum3B.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        csNum3B.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        csNum3B.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csNum3B.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csNum3B.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csNum3B.setBorderTop(HSSFCellStyle.BORDER_THIN);
        csNum3B.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        csNum3B.setDataFormat(df.getFormat("#,##0"));
        csNum3B.setFillPattern(HSSFCellStyle.FINE_DOTS);
        csNum3B.setFillForegroundColor(CELL_COLOR);
        csNum3B.setFillBackgroundColor(CELL_COLOR);

        csNum3BCenter = workbookBase.createCellStyle();
        csNum3BCenter.setFont(f2B);
        csNum3BCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        csNum3BCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        csNum3BCenter.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csNum3BCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csNum3BCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csNum3BCenter.setBorderTop(HSSFCellStyle.BORDER_THIN);
        csNum3BCenter.setDataFormat(df.getFormat("#,##0"));
        csNum3BCenter.setFillPattern(HSSFCellStyle.FINE_DOTS);
        csNum3BCenter.setFillForegroundColor(CELL_COLOR);
        csNum3BCenter.setFillBackgroundColor(CELL_COLOR);

        csStringB = workbookBase.createCellStyle();
        csStringB.setFont(f2B);
        csStringB.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        csStringB.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csStringB.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csStringB.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csStringB.setBorderTop(HSSFCellStyle.BORDER_THIN);
        //csDetail.setWrapText(true);

        csStringLeft = workbookBase.createCellStyle();
        csStringLeft.setFont(f2);
        csStringLeft.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        csStringLeft.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        csStringLeft.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csStringLeft.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csStringLeft.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csStringLeft.setBorderTop(HSSFCellStyle.BORDER_THIN);

        csStringCenter = workbookBase.createCellStyle();
        csStringCenter.setFont(f2);
        csStringCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        csStringCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        csStringCenter.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csStringCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csStringCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csStringCenter.setBorderTop(HSSFCellStyle.BORDER_THIN);

        csString2Center = workbookBase.createCellStyle();
        csString2Center.setFont(f2);
        csString2Center.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        csString2Center.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        csString2Center.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csString2Center.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csString2Center.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csString2Center.setBorderTop(HSSFCellStyle.BORDER_THIN);
        //csDetail2.setWrapText(true);

        csString2 = workbookBase.createCellStyle();
        csString2.setFont(f2);
        csString2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        csString2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csString2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csString2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csString2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        //csDetail2.setWrapText(true);

        csNum4 = workbookBase.createCellStyle();
        csNum4.setFont(f2);
        csNum4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        csNum4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        csNum4.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csNum4.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csNum4.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csNum4.setBorderTop(HSSFCellStyle.BORDER_THIN);
        csNum4.setDataFormat(df.getFormat("#,##0"));

        csNum4R = workbookBase.createCellStyle();
        csNum4R.setFont(f2);
        csNum4R.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        csNum4R.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        csNum4R.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csNum4R.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csNum4R.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csNum4R.setBorderTop(HSSFCellStyle.BORDER_THIN);
        csNum4R.setDataFormat(df.getFormat("#,##0"));

        csNum4L = workbookBase.createCellStyle();
        csNum4L.setFont(f2);
        csNum4L.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        csNum4L.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        csNum4L.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csNum4L.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csNum4L.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csNum4L.setBorderTop(HSSFCellStyle.BORDER_THIN);
        csNum4L.setDataFormat(df.getFormat("#,##0"));

        csNum4B = workbookBase.createCellStyle();
        csNum4B.setFont(f2B);
        csNum4B.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        csNum4B.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        csNum4B.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csNum4B.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csNum4B.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csNum4B.setBorderTop(HSSFCellStyle.BORDER_THIN);
        csNum4B.setDataFormat(df.getFormat("#,##0"));
        csNum4B.setFillPattern(HSSFCellStyle.FINE_DOTS);
        csNum4B.setFillForegroundColor(CELL_COLOR);
        csNum4B.setFillBackgroundColor(CELL_COLOR);

        csNum4BR = workbookBase.createCellStyle();
        csNum4BR.setFont(f2B);
        csNum4BR.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        csNum4BR.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        csNum4BR.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csNum4BR.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csNum4BR.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csNum4BR.setBorderTop(HSSFCellStyle.BORDER_THIN);
        csNum4BR.setDataFormat(df.getFormat("#,##0"));
        csNum4BR.setFillPattern(HSSFCellStyle.FINE_DOTS);
        csNum4BR.setFillForegroundColor(CELL_COLOR);
        csNum4BR.setFillBackgroundColor(CELL_COLOR);
        
        
        csNum4BCenter = workbookBase.createCellStyle();
        csNum4BCenter.setFont(f2B);
        csNum4BCenter.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        csNum4BCenter.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        csNum4BCenter.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csNum4BCenter.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csNum4BCenter.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csNum4BCenter.setBorderTop(HSSFCellStyle.BORDER_THIN);
        csNum4BCenter.setDataFormat(df.getFormat("#,##0"));
        csNum4BCenter.setFillPattern(HSSFCellStyle.FINE_DOTS);
        csNum4BCenter.setFillForegroundColor(CELL_COLOR);
        csNum4BCenter.setFillBackgroundColor(CELL_COLOR);

        csNumH = workbookBase.createCellStyle();
        csNumH.setFont(f2);
        csNumH.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        csNumH.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        csNumH.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csNumH.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csNumH.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csNumH.setBorderTop(HSSFCellStyle.BORDER_THIN);

        csStringPid = workbookBase.createCellStyle();
        csStringPid.setFont(f2);
        csStringPid.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        csStringPid.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csStringPid.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csStringPid.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csStringPid.setBorderTop(HSSFCellStyle.BORDER_THIN);

        csDouble2Center = workbookBase.createCellStyle();
        csDouble2Center.setFont(f2);
        csDouble2Center.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        csDouble2Center.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        csDouble2Center.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csDouble2Center.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csDouble2Center.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csDouble2Center.setBorderTop(HSSFCellStyle.BORDER_THIN);
        csDouble2Center.setDataFormat(df.getFormat("#,##0.00"));

        csDouble2R = workbookBase.createCellStyle();
        csDouble2R.setFont(f2);
        csDouble2R.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        csDouble2R.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        csDouble2R.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        csDouble2R.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        csDouble2R.setBorderRight(HSSFCellStyle.BORDER_THIN);
        csDouble2R.setBorderTop(HSSFCellStyle.BORDER_THIN);
        csDouble2R.setDataFormat(df.getFormat("#,##0.00"));

        csStringtxid = workbookBase.createCellStyle();
        csStringtxid.setFont(fhidden);
        csStringtxid.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        /*csDetailtxid.setBorderBottom(HSSFCellStyle.BORDER_THIN);
         csDetailtxid.setBorderLeft(HSSFCellStyle.BORDER_THIN);
         csDetailtxid.setBorderRight(HSSFCellStyle.BORDER_THIN);
         csDetailtxid.setBorderTop(HSSFCellStyle.BORDER_THIN);*/
    }

    protected void setFontFamily(String font_name) {
        ExcellBaseUtil.FONT_FAMILY = font_name;
    }

    protected void setFontSize(int font_size) {
        ExcellBaseUtil.FONT_SIZE = font_size;
    }

    protected void setFontHeaderSize(int font_size) {
        ExcellBaseUtil.FONT_CUSTOM_SIZE = font_size;
    }

    protected void setColorCell(Short colorIndex) {
        ExcellBaseUtil.CELL_COLOR = colorIndex;
    }

    protected String builderFormulaSum(int indexCell, int rowBegin, int rowEnd) {
        return "SUM(" + CHAR_ENG[indexCell] + String.valueOf(rowBegin) + ":" + CHAR_ENG[indexCell] + (rowEnd) + ")";
    }

    protected String builderFormulaSumRound(int indexCell, int rowBegin, int rowEnd, int roundDigit) {
        return "ROUND(SUM(" + CHAR_ENG[indexCell] + String.valueOf(rowBegin) + ":" + CHAR_ENG[indexCell] + (rowEnd) + ")," + String.valueOf(roundDigit) + ")";
    }

    protected String builderFormularRound(String target, int digit) {
        return "ROUND(" + target + ", " + String.valueOf(digit) + ")";
    }
}
