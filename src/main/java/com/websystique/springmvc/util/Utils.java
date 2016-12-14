package com.websystique.springmvc.util;

import java.util.Date;
import com.websystique.springmvc.constant.Constants;

/**
 * Created by kevin on 2016/11/27.
 */
public class Utils {
    public static boolean checkSchoolSuperPrivilege(Short privilege) {
        // TODO implement privilege check for school super admin

        return true;
    }

    public static Boolean validateSchoolAdminPrivilege(Short privilege) {
        // TODO implement privilege check for school admin

        return true;
    }

    public static long getCurrentTimeStamp() {
        Date currentDate = new Date();
        return currentDate.getTime();
    }

    public static String calculateKey(String devType, String userType, String username) {
        String rawKey = devType + "|" + userType + "|" + username + Constants.SECRET_KEY;
        String key = null;
        try {
            key = Md5Calculator.calMd5(rawKey);
        } catch (Exception e) {
            // pass
        }

        return key;
    }
}
