/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.claim.config;

import com.claim.support.NetUtils;

/**
 *
 * @author Poolsawat.a
 */
public class ConfigApp {
    public static final String VERSION_APP = "3.0.1";
    //public static final String IP_DEV = "10.151.101.57"; //54
    public static final String IP_DEV = "10.151.101.63";
    public static String CURRENT_IP = NetUtils.getIpAddress();
}
