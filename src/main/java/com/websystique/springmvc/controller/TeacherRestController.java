package com.websystique.springmvc.controller;

import com.websystique.springmvc.constant.Constants;
import com.websystique.springmvc.model.*;
import com.websystique.springmvc.service.*;
import com.websystique.springmvc.types.SchoolPlusResponse;
import com.websystique.springmvc.util.ControllerUtils;
import com.websystique.springmvc.util.Utils;
import com.websystique.springmvc.util.push.PushUtils;
import com.websystique.springmvc.util.redis.JCacheTools;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangyma on 11/27/16.
 */
@RestController
@RequestMapping(value = "/teacher")
public class TeacherRestController {

    private Logger LOGGER = Logger.getLogger(TeacherRestController.class);

    @Autowired
    TeacherService teacherService;

    @Autowired
    AfterSchoolVideoService afterSchoolVideoService;

    @Autowired
    SchoolVideoService schoolVideoService;

    @Autowired
    TextbookService textbookService;

    @Autowired
    ScoreService scoreService;

    @Autowired
    ClassGradeService classGradeService;

    @Autowired
    HomeworkService homeworkService;

    @Autowired
    CourseService courseService;

    @Autowired
    ArticleService articleService;

    @Autowired
    PushUtils pushUtils;

    @Autowired
    JCacheTools jCacheTools;

    @Autowired
    ControllerUtils controllerUtils;


    @RequestMapping(
            value = "/register",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<Teacher>> register(
            @RequestBody Teacher teacher
    ) {
        SchoolPlusResponse<Teacher> response = new SchoolPlusResponse<>();

        return controllerUtils.doRegister(teacher, teacherService, response);
    }

    @RequestMapping(
            value = "/login",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<Teacher>> login(
            @RequestParam(value = "device_type") String deviceType,
            @RequestParam(value = "device_token", required = false) String deviceToken,
            @RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password
    ) {
        SchoolPlusResponse<Teacher> response = new SchoolPlusResponse<>();

        Teacher teacher = teacherService.findByUsername(username);

        return controllerUtils.doLogin(deviceType, deviceToken, username, password, teacher,
                Constants.TEACHER_KEY_PREFIX, response);
    }

    @RequestMapping(
            value = "/logout",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<Boolean>> logout(
            @RequestParam("device_type") String devType,
            @RequestParam("id") Long id) {

        SchoolPlusResponse<Boolean> response = new SchoolPlusResponse<>();

        Teacher teacher = teacherService.findById(id);
        if (teacher == null) {
            response.setStatusCode(400);
            response.setResult("BadRequest");
            response.setShortMessage("该教师不存在");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            if (devType.equals(Constants.LOGIN_DEV_ANDROID) ||
                    devType.equals(Constants.LOGIN_DEV_IOS)) {
                devType = Constants.LOGIN_DEV_APP;
            }
            String key = Utils.calculateKey(devType, Constants.TEACHER_KEY_PREFIX, teacher.getUsername());
            jCacheTools.delKeyFromJedis(key);

            response.setStatusCode(200);
            response.setResult("OK");
            response.setData(Boolean.TRUE);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @RequestMapping(
            value = "/modify_password",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<Teacher>> modifyPassword(
            @RequestParam("key") String key,
            @RequestParam("old_password") String oldPassword,
            @RequestParam("new_password") String newPassword
    ) {
        SchoolPlusResponse<Teacher> response = new SchoolPlusResponse<>();

        return controllerUtils.doModifyPassword(key, oldPassword, newPassword, teacherService, response);
    }

    @RequestMapping(
            value = "/homework/assign",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<List<Homework>>> assignHomework(
            @RequestParam("key") String key,
            @RequestParam("course_ids") String courseIds,
            @RequestBody List<Homework> homeworkList
    ) {
        SchoolPlusResponse<List<Homework>> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) != null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        String[] courseIdArray = courseIds.split("|");
        for (int i = 0; i < courseIdArray.length; i++) {
            String courseIdStr = courseIdArray[i];
            Course course = courseService.findById(Long.parseLong(courseIdStr));
            if (course == null) {
                response.setStatusCode(400);
                response.setResult("NoEntity");
                response.setShortMessage("不存在id为" + courseIdStr + "的课程");

                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            homeworkList.get(i).setCourse(course);
        }

        List<Homework> savedHomeworkList = homeworkService.save(homeworkList);

        response.setStatusCode(200);
        response.setResult("OK");
        response.setData(savedHomeworkList);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/homework/{id}/modify",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<Homework>> modifyHomework(
            @RequestParam("key") String key,
            @PathVariable("id") Long id,
            @RequestBody Homework homework
    ) {
        SchoolPlusResponse<Homework> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) != null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        Homework updateHomework;
        try {
            updateHomework = homeworkService.update(id, homework);
        }  catch (IllegalArgumentException e) {
            response.setStatusCode(400);
            response.setResult("BadRequest");
            response.setShortMessage("无效参数");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (updateHomework == null) {
            response.setStatusCode(500);
            response.setResult("InternalError");
            response.setShortMessage("服务器内部错误，无法更新作业");

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return controllerUtils.getResponseEntity(updateHomework);
    }

    @RequestMapping(
            value = "/homework/{id}/delete",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<Boolean>> deleteHomework(
            @RequestParam("key") String key,
            @PathVariable("id") Long id
    ) {
        SchoolPlusResponse<Boolean> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) != null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        if (homeworkService.findById(id) == null) {
            response.setStatusCode(400);
            response.setResult("NoEntity");
            response.setShortMessage("不存在id为" + id + "的作业");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (!homeworkService.deleteById(id)) {
            response.setStatusCode(500);
            response.setResult("InternalError");
            response.setShortMessage("服务器内部错误，无法删除作业");

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return controllerUtils.getResponseEntity(Boolean.TRUE);
    }

    /*
     * If none of the parameters are supplied:
     *      if the teacher is master, show all courses' scores of the students in his/her class, in the latest exam;
     *      if not, show the scores of the courses he/she teaches, in the latest exam.
     * If any of the parameters are supplied,
     *      if the date is supplied, show scores according to the parameters.
     *      if the date is not supplied, show scores according to the parameters and the date of the latest exam.
     */
    @RequestMapping(
            value = "/scores",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<List<Score>>> getScores(
            @RequestParam("key") String key,
            @RequestParam(value = "date", required = false) Date date,
            @RequestParam(value = "class_id", required = false) String classGradeId,
            @RequestParam(value = "subject", required = false) String subjectsStr
    ) {
        SchoolPlusResponse<List<Score>> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) != null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        Teacher thisTeacher = teacherService.findById(
                Long.parseLong(jCacheTools.getStringFromJedis(key)));
        if (date == null && classGradeId == null && subjectsStr == null) {
            List<Score> allScores = new ArrayList<>();
            if (thisTeacher.getHead()) {
                for (Course course : thisTeacher.getInChargeClass().getCourses()) {
                    Date lastExamDate = scoreService.getLastDateByCourse(course);
                    List<Score> scores = scoreService.findByCourseAndDate(course, lastExamDate);
                    allScores.addAll(scores);
                }

                return controllerUtils.getResponseEntity(allScores);
            } else {
                for (Course course : thisTeacher.getTeachingCourses()) {
                    Date lastExamDate = scoreService.getLastDateByCourse(course);
                    List<Score> scores = scoreService.findByCourseAndDate(course, lastExamDate);
                    allScores.addAll(scores);
                }

                return controllerUtils.getResponseEntity(allScores);
            }
        } else if (date != null && classGradeId != null && subjectsStr != null) {
            Course course = courseService.findByClassGradeIdAndSubjectName(Integer.parseInt(classGradeId), subjectsStr);
            List<Score> scores = scoreService.findByCourseAndDate(course, date);

            return controllerUtils.getResponseEntity(scores);
        } else if (classGradeId != null && subjectsStr != null) {
            Course course = courseService.findByClassGradeIdAndSubjectName(Integer.parseInt(classGradeId), subjectsStr);
            List<Score> scores = scoreService.findByCourse(course);

            return controllerUtils.getResponseEntity(scores);
        } else if (classGradeId != null && date != null) {
            List<Score> allScores = new ArrayList<>();
            List<Course> courses = courseService.findByClassGradeId(Integer.parseInt(classGradeId));
            for (Course course : courses) {
                List<Score> scores = scoreService.findByCourseAndDate(course, date);
                allScores.addAll(scores);
            }

            return controllerUtils.getResponseEntity(allScores);
        } else if (subjectsStr != null && date != null) {
            List<Score> allScores = new ArrayList<>();
            List<Course> courses = courseService.findBySubjectName(subjectsStr);
            for (Course course : courses) {
                List<Score> scores = scoreService.findByCourseAndDate(course, date);
                allScores.addAll(scores);
            }

            return controllerUtils.getResponseEntity(allScores);
        } else if (classGradeId != null) {
            List<Score> allScores = new ArrayList<>();
            List<Course> courses = courseService.findByClassGradeId(Integer.parseInt(classGradeId));
            for (Course course : courses) {
                List<Score> scores = scoreService.findByCourseAndDate(course, date);
                allScores.addAll(scores);
            }

            return controllerUtils.getResponseEntity(allScores);
        } else if (subjectsStr != null) {
            List<Score> allScores = new ArrayList<>();
            List<Course> courses = courseService.findBySubjectName(subjectsStr);
            for (Course course : courses) {
                List<Score> scores = scoreService.findByCourseAndDate(course, date);
                allScores.addAll(scores);
            }

            return controllerUtils.getResponseEntity(allScores);
        } else { // date != null
            List<Score> scores = scoreService.findByDate(date);

            return controllerUtils.getResponseEntity(scores);
        }
    }

    @RequestMapping(
            value = "/scores/enter",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<List<Score>>> enterScores(
            @RequestParam("key") String key,
            @RequestBody List<Score> scores
    ) {
        SchoolPlusResponse<List<Score>> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) != null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        List<Score> savedScores = scoreService.save(scores);

        return controllerUtils.getResponseEntity(savedScores);
    }

    @RequestMapping(
            value = "/articles",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<List<Article>>> getArticles(
            @RequestParam("key") String key
    ) {
        SchoolPlusResponse<List<Article>> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) != null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        Long teacherId = Long.parseLong(jCacheTools.getStringFromJedis(key));
        List<Article> articles = articleService.findByTeacherId(teacherId);

        return controllerUtils.getResponseEntity(articles);
    }

    @RequestMapping(
            value = "/article/publish",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<Article>> publishArticle(
            @RequestParam("key") String key,
            @RequestBody Article article,
            @RequestBody List<ArticlePicture> pictures
    ) {
        SchoolPlusResponse<Article> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) != null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        Article savedArticle = articleService.save(article, pictures);

        return controllerUtils.getResponseEntity(article);
    }

    @RequestMapping(
            value = "/article/delete/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<Boolean>> deleteArticle(
            @RequestParam("key") String key,
            @PathVariable("id") Long id
    ) {
        SchoolPlusResponse<Boolean> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) != null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        Article articleToDelete = articleService.findById(id);
        if (articleToDelete == null) {
            return controllerUtils.getResponseEntity(null);
        }

        Boolean deleted = articleService.deleteById(id);

        return controllerUtils.getResponseEntity(deleted);
    }
}
