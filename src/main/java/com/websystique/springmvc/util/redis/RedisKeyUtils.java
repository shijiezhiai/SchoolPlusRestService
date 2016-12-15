package com.websystique.springmvc.util.redis;

import com.websystique.springmvc.constant.Constants;
import com.websystique.springmvc.model.ClassGrade;
import com.websystique.springmvc.model.Student;

/**
 * Created by kevin on 2016/12/1.
 */
public class RedisKeyUtils {

    public static String parentGetStudentsKey(Long parentId) {
        return Constants.PARENT_KEY_PREFIX + "|" + parentId + "|students";
    }

    public static String classCourseHomeworkKey(ClassGrade classGrade, String course) {
        return Constants.PARENT_HOMEWORK_KEY_PREFIX + "|" + classGrade.getSchool().getId() +
                "|" + classGrade.getId() + "|" + course;
    }

    public static String parentGetStudentScoreKey(ClassGrade classGrade, String course) {
        return Constants.SCORE_KEY_PREFIX + "|" + classGrade.getSchool().getId() +
                "|" + classGrade.getId() + "|" + course;
    }

    public static String notificationIdKey(Long id) {
        return Constants.NOTIFICATION_ID_KEY_PREFIX + "|" + id.toString();
    }

    public static String studentNotificationIdsKey(Long studentId) {
        return Constants.STUDENT_NOTIFICATION_IDS_KEY_PREFIX + "|" + studentId.toString();
    }

    public static String classGradeNotificationIdsKey(Long classGradeId) {
        return Constants.CLASS_NOTIFICATION_IDS_KEY_PREFIX + "|" + classGradeId.toString();
    }

    public static String schoolNotificationIdsKey(Long schoolId) {
        return Constants.SCHOOL_NOTIFICATION_IDS_KEY_PREFIX + "|" + schoolId.toString();
    }

    public static String activityIdKey(Long id) {
        return Constants.ACTIVITY_ID_KEY_PREFIX + "|" + id.toString();
    }

    public static String classGradeIdKey(Long id) {
        return Constants.CLASS_GRADE_ID_KEY_PREFIX + "|" + id.toString();
    }

    public static String homeworkIdKey(Long id) {
        return Constants.HOMEWORK_ID_KEY_PREFIX + "|" + id.toString();
    }

    public static String courseIdKey(Long id) {
        return Constants.COURSE_ID_KEY_PREFIX + "|" + id.toString();
    }

    public static String courseScoreLastDateKey(Long id) {
        return Constants.COURSE_SCORE_LAST_DATE_KEY_PREFIX + "|" + id.toString();
    }

    public static String articleIdKey(Long id) {
        return Constants.ARTICLE_ID_KEY_PREFIX + "|" + id.toString();
    }

    public static String teacherArticleIdsKey(Long teacherId) {
        return Constants.TEACHER_ARTICLE_IDS_KEY + "|" + teacherId;
    }

    public static String schoolVideoIdKey(Long id) {
        return Constants.SCHOOL_VIDEO_ID_KEY_PREFIX + "|" + id.toString();
    }

    public static String schoolIdSchoolVideoIdsKey(Long id) {
        return Constants.SCHOOL_ID_SCHOOL_VIDEO_IDS_KEY_PREFIX + "|" + id.toString();
    }

    public static String teacherSchoolVideoIdsKey(Long id) {
        return Constants.TEACHER_SCHOOL_VIDEO_IDS_KEY_PREFIX + "|" + id.toString();
    }
}
