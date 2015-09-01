/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.claim.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Poolsawat.a
 */
public class CollectionUtil {

    public static Set<String> findDuplicates(List<String> list) {
        Set<String> setToReturn = new HashSet();
        Set<String> uniqueSet = new HashSet<String>(list);
        for (String temp : uniqueSet) {
            //System.out.println(temp + ": " + Collections.frequency(list, temp));
            setToReturn.add(temp);
        }
        return setToReturn;
    }

    public boolean checkStringInListString(String[] stmpFix, String stmp) {
        return Arrays.asList(stmpFix).contains(stmp);
    }
    public static List<String> removeDuplicatObjectOfList(List<String> objectList){        
        List<String> depdupeString = new ArrayList<>(new LinkedHashSet<>(objectList));
        return depdupeString;
    }
    public static void main(String[] args) {
        
    }
}
