/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Date;
import java.util.Random;

/**
 *
 * @author chunlei
 */
public class Helper {

    //0 for ok, 1 for invalid dmpid
    /**
     *
     * @param dmpid
     * @return
     */
    public static boolean isDmpid(String dmpid) {
        return dmpid != null && dmpid.matches("1[3-6][0-9]{9,20}");
    }

    public static Boolean isNull(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * get new dmp id for user
     */
    public static String genDmpid() {
        Random random = new Random();
        String rs = String.valueOf(random.nextInt(1000000000));
        if (rs.length() < 9) {
            for (int ct = 0; ct < 9 - rs.length(); ct++) {
                rs = "0" + rs;
            }
        }
        return String.valueOf(new Date().getTime()) + "-" + rs;
    }
}
