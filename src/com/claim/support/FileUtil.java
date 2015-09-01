/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.support;

import java.io.File;

/**
 *
 * @author Poolsawat.a
 */
public class FileUtil {

    public boolean mkdirMutiDirectory(String fullDirectory) {
        boolean isMkdir = false;
        File fullPath = new File(fullDirectory);
        if (!fullPath.exists()) {
            if (fullPath.mkdirs()) {
                isMkdir = true;
                System.out.println("Multiple directories are created!");
            } else {
                System.out.println("Failed to create multiple directories!");
            }
        }
        return isMkdir;
    }

    public String mkdirDir(String target_path, String budget_year, String rpt_typename) {
        // ############# mkdir ############
        String full_directory = target_path + "" + File.separator + "";
        String full_mkdir = "";
        if (!budget_year.equals("")) {
            full_mkdir = full_directory + rpt_typename + "_" + budget_year;
        } else {
            full_mkdir = full_directory + rpt_typename + "_2014-2015";
        }
        new FileUtil().mkdirMutiDirectory(full_mkdir);
        full_directory = full_mkdir;
        // ############# mkdir ############
        return full_directory;
    }

   
    
    public static String getPathDesktop() {
        return System.getProperty("user.home") + "" + File.separator + "" + "Desktop";
    }

    public static void main(String[] args) {
        String httpUrl = "10.151.101.10" + File.separator + "bkknhso" + File.separator + "WORK_IT_2014" + File.separator + "_____OPBKKCLAIM_____" + File.separator + "OPBKK_REPORT_FOR_WEBSITE" + File.separator + "ae74" + File.separator + "";
        //String httpUrl = "E:"+File.separator+"SVN";
        File folder = new File(httpUrl);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                System.out.println(file.getName());
            }
        }
    }
}
