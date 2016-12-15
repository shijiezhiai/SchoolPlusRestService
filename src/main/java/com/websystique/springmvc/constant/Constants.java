package com.websystique.springmvc.constant;

/**
 * Created by yangyma on 11/24/16.
 */
public class Constants {

    /*
     * Privileges for admin (including super admin).
     */
    public static final Integer MANAGE_ADMIN = 1;

    public static final Integer MANAGE_SCHOOL_SUPER_ADMIN = 1 << 1;
    public static final Integer UPLOAD_AFTER_SCHOOL_VIDEO = 1 << 2;
    public static final Integer MANAGE_AFTER_SCHOOL_VIDEO = 1 << 3;
    public static final Integer PAY_TEACHER = 1 << 4;

    /*
     * Privileges for school admins (including school super admin).
     */
    public static final Integer MANAGE_SCHOOL_ADMIN = 1;

    public static final Integer MANAGE_TEACHER = 1 << 1;
    public static final Integer MANAGER_STUDENT = 1 << 2;
    public static final Integer MANAGE_VIDEO = 1 << 3;
    public static final Integer MANAGE_ACTIVITY = 1 << 4;
    public static final Integer MANAGE_NOTIFICATION = 1 << 5;
    public static final Integer MANAGE_ARTICLE = 1 << 6;

    /*
     * Privileges for teachers
     */
    public static final Integer VIEW_STUDENT = 1;
    public static final Integer VIEW_STUDENT_LOCATION = 1 << 1;
    public static final Integer VIEW_STUDENT_CHEKING_IN = 1 << 2;
    public static final Integer UPLOAD_SCHOOL_VIDEO = 1 << 3;
    public static final Integer UPLOAD_SCHOOL_ARTICLE = 1 << 4;

    public static final Integer SUPER_ADMIN_ID = 0;

    public static final String SECRET_KEY = "Cp(#m#GyDKrtpKjexjaSWbpaHer&@0a9";
    public static final int APP_EXPIRE_TIME = 7 * 24 * 3600;
    public static final int WEB_EXPIRE_TIME = 24 * 3600;

    // Use these prefix + username to compose unique key in cache
    public static final String SUPER_ADMIN_KEY_PREFIX = "SA";
    public static final String ADMIN_KEY_PREFIX = "A";
    public static final String SCHOOL_SUPER_KEY_PREFIX = "SS";
    public static final String SCHOOL_ADMIN_KEY_PREFIX = "S";
    public static final String PARENT_KEY_PREFIX = "P";
    public static final String TEACHER_KEY_PREFIX = "T";

    public static final String PARENT_HOMEWORK_KEY_PREFIX = "PH";
    public static final String SCORE_KEY_PREFIX = "G"; //goal
    public static final String NOTIFICATION_ID_KEY_PREFIX = "NI"; // notification id
    public static final String STUDENT_NOTIFICATION_IDS_KEY_PREFIX = "SNI";
    public static final String CLASS_NOTIFICATION_IDS_KEY_PREFIX = "CNI";
    public static final String SCHOOL_NOTIFICATION_IDS_KEY_PREFIX = "LNI";
    public static final String ACTIVITY_ID_KEY_PREFIX = "C";
    public static final String CLASS_GRADE_ID_KEY_PREFIX = "CG";
    public static final String HOMEWORK_ID_KEY_PREFIX = "H";
    public static final String COURSE_ID_KEY_PREFIX = "CSI";
    public static final String COURSE_SCORE_LAST_DATE_KEY_PREFIX = "CSD";
    public static final String ARTICLE_ID_KEY_PREFIX = "AI";
    public static final String TEACHER_ARTICLE_IDS_KEY = "TAI";
    public static final String SCHOOL_VIDEO_ID_KEY_PREFIX = "SVI";
    public static final String SCHOOL_ID_SCHOOL_VIDEO_IDS_KEY_PREFIX = "SISVI";
    public static final String TEACHER_SCHOOL_VIDEO_IDS_KEY_PREFIX = "TSVI";

    public static final int NOTIFICATION_REDIS_EXPIRE_TIME = 7 * 24 * 3600;
    public static final int HOMEWORK_REDIS_EXPIRE = 1 * 24 * 3600;
    public static final int ARTICLE_REDIS_EXPIRE = 7 * 24 * 3600;
    public static final int SCHOOL_VIDEO_EXPIRE_TIME = 7 * 24 * 3600;


    public static final String LOGIN_DEV_WEB = "web";
    public static final String LOGIN_DEV_APP = "app";
    public static final String LOGIN_DEV_IOS = "ios";
    public static final String LOGIN_DEV_ANDROID = "android";


    public static final String NETEASE_IM_HOST = "https://api.netease.im";
    public static final String NETEASE_SMS_PATH = "/sms";
    public static final String NETEASE_VERIFICATION_CODE_URI = "/sendcode.action";
    public static final String NETEASE_VERIFY_VERIFICATION_CODE_URI = "/verifycode.action";
    // TODO add the app key for netease im here
    public static final String NETEASE_APP_KEY = "";

}
