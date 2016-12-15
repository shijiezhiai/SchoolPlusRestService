package com.websystique.springmvc.controller;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.websystique.springmvc.constant.Constants;
import com.websystique.springmvc.model.*;
import com.websystique.springmvc.repository.ArticleRepository;
import com.websystique.springmvc.repository.SchoolVideoRepository;
import com.websystique.springmvc.service.*;
import com.websystique.springmvc.util.ControllerUtils;
import com.websystique.springmvc.util.redis.RedisKeyUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.websystique.springmvc.types.SchoolPlusResponse;
import com.websystique.springmvc.util.push.PushUtils;
import com.websystique.springmvc.util.redis.JCacheTools;

import static com.websystique.springmvc.util.redis.RedisKeyUtils.parentGetStudentsKey;


/**
 * Created by yangyma on 11/29/16.
 */
@RestController
@RequestMapping(value = "/parent")
public class ParentRestController {

    private Logger LOGGER = Logger.getLogger(ParentRestController.class);

    @Autowired
    ParentService parentService;

    @Autowired
    AfterSchoolVideoService afterSchoolVideoService;

    @Autowired
    SchoolVideoService schoolVideoService;

    @Autowired
    ArticleService articleService;

    @Autowired
    StudentService studentService;

    @Autowired
    AdvertisementService advertisementService;

    @Autowired
    ScoreService scoreService;

    @Autowired
    ActivityService activityService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    JCacheTools jCacheTools;

    @Autowired
    PushUtils pushUtils;

    @Autowired
    ControllerUtils controllerUtils;

    @Autowired
    SubjectService subjectService;

    @RequestMapping(
            value = "/login",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<Parent>> login(
            @RequestParam(value = "device_type") String deviceType,
            @RequestParam(value = "device_token", required = false) String deviceToken,
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password
    ) {
        SchoolPlusResponse<Parent> response = new SchoolPlusResponse<>();

        Parent parent = parentService.findByUsername(username);

        return controllerUtils.doLogin(deviceType, deviceToken, username, password, parent,
                Constants.PARENT_KEY_PREFIX, response);
    }

    @RequestMapping(
            value = "/register",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<Parent>> register(
            @RequestBody Parent parent
    ) {
        SchoolPlusResponse<Parent> response = new SchoolPlusResponse<>();

        return controllerUtils.doRegister(parent, parentService, response);
    }

    @RequestMapping(
            value = "/logout",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<Boolean>> logout(
            @RequestParam("key") String key) {

        Parent parent = parentService.findById(
                Long.parseLong(jCacheTools.getStringFromJedis(key)));

        return controllerUtils.doLogout(key, parent);
    }

    @RequestMapping(
            value = "/modify_password",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<Parent>> modifyPassword(
            @RequestParam("key") String key,
            @RequestParam("old_password") String oldPassword,
            @RequestParam("new_password") String newPassword
    ) {
        SchoolPlusResponse<Parent> response = new SchoolPlusResponse<>();

        return controllerUtils.doModifyPassword(key, oldPassword, newPassword, parentService, response);
    }

    @RequestMapping(
            value = "/students",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<List<Student>>> getStudents(
            @RequestParam("key") String key
    ) {
        SchoolPlusResponse<List<Student>> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) != null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        Long parentId = Long.parseLong(jCacheTools.getStringFromJedis(key));
        String getStudentsKey = RedisKeyUtils.parentGetStudentsKey(parentId);
        if (jCacheTools.existKey(getStudentsKey)) {
            List<String> studentIdStrings = jCacheTools.getListFromJedis(getStudentsKey);
            List<Long> studentIds = new ArrayList<>(studentIdStrings.size());
            for (String studentIdString : studentIdStrings) {
                studentIds.add(Long.parseLong(studentIdString));
            }
            List<Student> students = studentService.findByIds(studentIds);
            response.setStatusCode(200);
            response.setResult("OK");
            response.setData(students);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        List<Student> students = studentService.findByParentId(parentId);
        if (students == null) {
            response.setStatusCode(204);
            response.setResult("NoData");
            response.setShortMessage("无法找到该家长的学生信息");

            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } else {
            // Add the student ids to the cache
            List<String> studentIdStrings = new ArrayList<>(students.size());
            for (Student student : students) {
                studentIdStrings.add(student.getId().toString());
            }
            jCacheTools.addListToJedis(getStudentsKey, studentIdStrings, 0);

            response.setStatusCode(200);
            response.setResult("OK");
            response.setData(students);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @RequestMapping(
            value = "student/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<Student>> getStudent(
            @RequestParam("key") String key,
            @PathVariable("id") Long id
    ) {
        SchoolPlusResponse<Student> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) != null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        Student student = studentService.findById(id);
        return controllerUtils.getResponseEntity(student);
    }

    @RequestMapping(
            value = "/ads",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<List<Advertisement>>> getAds(
            @RequestParam("key") String key
    ) {
        SchoolPlusResponse<List<Advertisement>> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) != null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        Parent parent = parentService.findById(
                Long.parseLong(jCacheTools.getStringFromJedis(key)));
        List<Advertisement> ads = getAds(parent);

        return controllerUtils.getResponseEntity(ads);
    }

    @RequestMapping(
            value = "/school/homework",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<List<Homework>>> getHomeworkList(
            @RequestParam("key") String key,
            @RequestParam("student_id") Long studentId,
            @RequestParam("courses") String courses
    ) {
        SchoolPlusResponse<List<Homework>> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) != null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        Student student = studentService.findById(studentId);
        ClassGrade classGrade = student.getClassGrade();
        String[] courseArr = courses.split(",");
        List<Homework> homeworkList = new ArrayList<>(courseArr.length);
        for (String course : courseArr) {
            String homeworkKey = RedisKeyUtils.classCourseHomeworkKey(classGrade, course);
            if (jCacheTools.existKey(homeworkKey)) {
                Long homeworkId = Long.parseLong(
                        jCacheTools.getStringFromJedis(homeworkKey));
                Gson gson = new Gson();
                Homework homework = gson.fromJson(
                        jCacheTools.getStringFromJedis(RedisKeyUtils.homeworkIdKey(homeworkId)),
                        Homework.class);
                homeworkList.add(homework);
            }
        }

        return controllerUtils.getResponseEntity(homeworkList);
    }

    @RequestMapping(
            value = "/school/scores",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<List<Score>>> getScores(
            @RequestParam("key") String key,
            @RequestParam("student_id") Long studentId,
            @RequestParam("subjects") String subjects
    ) {
        SchoolPlusResponse<List<Score>> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) != null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        Student student = studentService.findById(studentId);
        ClassGrade classGrade = student.getClassGrade();
        String[] subjectArr = subjects.split(",");
        List<Score> scores = new ArrayList<>(subjectArr.length);
        for (String subjectName : subjectArr) {
            String scoreKey = RedisKeyUtils.parentGetStudentScoreKey(classGrade, subjectName);
            if (jCacheTools.existKey(scoreKey)) {
                Gson gson = new Gson();
                Score score = gson.fromJson(jCacheTools.getStringFromJedis(scoreKey), Score.class);
                scores.add(score);
            } else {
                Subject subject = subjectService.findByName(subjectName);
                if (subject == null) {
                    response.setStatusCode(400);
                    response.setResult("NoSubject");
                    response.setShortMessage("不存在学科");

                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }
                scores.add(scoreService.findByStudentAndSubject(student, subject));
            }
        }

        return controllerUtils.getResponseEntity(scores);

    }

    @RequestMapping(
            value = "/school/videos",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<List<SchoolVideo>>> getSchoolVideos(
            @RequestParam("key") String key,
            @RequestParam(value = "student_id") Long studentId,
            @RequestParam(value = "grade", required = false) Integer grade,
            @RequestParam(value = "course", required = false) String subjectName,
            @RequestParam(value = "tag", required = false) String tag
    ) {
        SchoolPlusResponse<List<SchoolVideo>> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) != null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        School school = studentService.findById(studentId).getSchool();

        Subject subject = null;
        if (!(subjectName == null || subjectName.isEmpty())) {
            subject = subjectService.findByName(subjectName);
            if (subject == null) {
                response.setStatusCode(400);
                response.setResult("NoSubject");
                response.setShortMessage("不存在名称为" + subjectName + "的学科");

                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }

        List<SchoolVideo> videos;
        if (grade != null && !(subjectName == null || subjectName.isEmpty())) {
            videos = schoolVideoService.findBySchoolNameAndGradeAndSubject(school.getName(), grade, subject);
        } else if (grade != null) {
            videos = schoolVideoService.findBySchoolNameAndGrade(school.getName(), grade);
        } else if (!(subjectName == null || subjectName.isEmpty())) {
            videos = schoolVideoService.findBySchoolNameAndSubject(school.getName(), subject);
        } else {
            videos = schoolVideoService.findBySchool(school);
        }

        response.setStatusCode(200);
        response.setResult("OK");
        response.setData(videos);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/school/articles",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<List<Article>>> getArticles(
            @RequestParam("key") String key,
            @RequestParam("student_id") Long studentId,
            @RequestParam(value = "grade", required = false) Integer grade,
            @RequestParam(value = "course", required = false) String subjectName,
            @RequestParam(value = "tag", required = false) String tag
    ) {
        SchoolPlusResponse<List<Article>> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) != null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        String schoolName = studentService.findById(studentId)
                .getSchool().getName();

        Subject subject = null;
        if (subjectName != null && ! subjectName.isEmpty()) {
            subject = subjectService.findByName(subjectName);
            if (subject == null) {
                response.setStatusCode(400);
                response.setResult("NoSubject");
                response.setShortMessage("不存在名称为" + subjectName + "的科目");

                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }
        List<Article> articles;
        if (grade != null && subjectName != null && ! subjectName.isEmpty()) {
            articles = articleService.findBySchoolNameAndGradeAndSubject(schoolName, grade, subject);
        } else if (grade != null) {
            articles = articleService.findBySchoolNameAndGrade(schoolName, grade);
        } else if (subjectName != null && ! subjectName.isEmpty()) {
            articles = articleService.findBySchoolNameAndSubject(schoolName, subject);
        } else {
            articles = articleService.findBySchoolName(schoolName);
        }

        response.setStatusCode(200);
        response.setResult("OK");
        response.setData(articles);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/after_school/videos",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<List<AfterSchoolVideo>>> getAfterSchoolVideos(
            @RequestParam("key") String key,
            @RequestParam(value = "grade") Integer grade,
            @RequestParam(value = "course", required = false) String course,
            @RequestParam(value = "tag", required = false) String tag
    ) {
        SchoolPlusResponse<List<AfterSchoolVideo>> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) != null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        List<AfterSchoolVideo> videos;
        if (course != null) {
            videos = afterSchoolVideoService.findByGradeAndCourse(grade, course);
        } else {
            videos = afterSchoolVideoService.findByGrade(grade);
        }

        response.setStatusCode(200);
        response.setResult("OK");
        response.setData(videos);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/notifications",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<List<Notification>>> getNotifications(
            @RequestParam(value = "key") String key,
            @RequestParam(value = "student_id") Long studentId
    ) {
        SchoolPlusResponse<List<Notification>> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) != null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        Student student = studentService.findById(studentId);
        ClassGrade classGrade = student.getClassGrade();
        School school  = student.getSchool();

        List<Notification> studentNotifications = notificationService.findByStudent(student);
        List<Notification> classNotifications = notificationService.findByClassGrade(classGrade);
        List<Notification> schoolNotifications = notificationService.findBySchool(school);

        List<Notification> allNotifications = new ArrayList<>();
        allNotifications.addAll(studentNotifications);
        allNotifications.addAll(classNotifications);
        allNotifications.addAll(schoolNotifications);

        return controllerUtils.getResponseEntity(allNotifications);
    }

    @RequestMapping(
            value =  "/activity/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<Activity>> getActivity(
            @RequestParam(value = "key") String key,
            @PathVariable("id") Long id
    ) {
        SchoolPlusResponse<Activity> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) != null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        Activity activity = activityService.findById(id);

        return controllerUtils.getResponseEntity(activity);
    }

    @RequestMapping(
            value =  "/activities",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<List<Activity>>> getActivities(
            @RequestParam(value = "key") String key,
            @RequestParam(value = "student_id") Long studentId
    ) {
        SchoolPlusResponse<List<Activity>> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) != null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        School school = studentService.findById(studentId).getSchool();

        List<Activity> activities = activityService.findBySchool(school);

        return controllerUtils.getResponseEntity(activities);
    }

    @RequestMapping(
            value = "/notification/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<Notification>> getNotification(
            @RequestParam("key") String key,
            @PathVariable("id") Long id
    ) {
        SchoolPlusResponse<Notification> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) != null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        Notification notification = notificationService.findById(id);

        return controllerUtils.getResponseEntity(notification);
    }

    private List<Advertisement> getAds(@SuppressWarnings("unused") Parent parent) {
        if (jCacheTools.existKey("advertisements")) {
            List<String> adIdStrings = jCacheTools.getListFromJedis("advertisement");
            List<Advertisement> advertisements = new ArrayList<>();
            for (String adIdStr : adIdStrings) {
                advertisements.add(advertisementService.findById(
                        Long.parseLong(adIdStr)
                ));
            }
            return advertisements;
        } else {
            return advertisementService.findAll();
        }

    }
}
