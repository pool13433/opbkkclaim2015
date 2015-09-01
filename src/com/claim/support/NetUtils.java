/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.support;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Poolsawat.a
 */
public class NetUtils {

    public static String getIpAddress(){
        String userIp = "";
        try {
            System.out.println("IPAddress ::==" + Inet4Address.getLocalHost().getHostAddress());
            userIp = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(NetUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return userIp;
    }
}
