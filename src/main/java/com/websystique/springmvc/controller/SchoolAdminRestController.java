package com.websystique.springmvc.controller;

import com.websystique.springmvc.model.SchoolAdmin;
import com.websystique.springmvc.model.SchoolVideo;
import com.websystique.springmvc.model.Teacher;
import com.websystique.springmvc.service.*;
import com.websystique.springmvc.types.SchoolPlusResponse;
import com.websystique.springmvc.util.ControllerUtils;
import com.websystique.springmvc.util.push.PushUtils;
import com.websystique.springmvc.util.redis.JCacheTools;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by kevin on 2016/11/27.
 */
@RestController
@RequestMapping(value = "/school_admin")
public class SchoolAdminRestController {

    private static final Logger LOGGER = Logger.getLogger(SchoolAdminRestController.class);

    @Autowired
    SchoolAdminService schoolAdminService;

    @Autowired
    SchoolService schoolService;

    @Autowired
    TeacherService teacherService;

    @Autowired
    SchoolVideoService schoolVideoService;

    @Autowired
    ControllerUtils controllerUtils;

    @Autowired
    JCacheTools jCacheTools;

    @Autowired
    PushUtils pushUtils;

    @RequestMapping(
            value = "/login",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<SchoolAdmin>> login(
            @RequestParam(value = "device_type", required = false) String devType,
            @RequestParam(value = "device_token", required = false) String devToken,
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        SchoolPlusResponse<SchoolAdmin> response = new SchoolPlusResponse<>();

        SchoolAdmin schoolAdmin = schoolAdminService.findByUsername(username);

        return controllerUtils.doLogin(devType, devToken, username, password, schoolAdmin, response);

    }

    @RequestMapping(
            value = "/logout",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<Boolean>> logout(
            @RequestParam(value = "key") String key) {

        SchoolAdmin schoolAdmin = schoolAdminService.findById(
                Long.parseLong(jCacheTools.getStringFromJedis(key)));

        return controllerUtils.doLogout(key, schoolAdmin);
    }

    @RequestMapping(
            value = "/register",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<SchoolAdmin>> register(
            @RequestBody SchoolAdmin schoolAdmin
    ) {
        SchoolPlusResponse<SchoolAdmin> response = new SchoolPlusResponse<>();

        return controllerUtils.doRegister(schoolAdmin, schoolAdminService, response);
    }

    @RequestMapping(
            value = "/modify_passwd",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<SchoolAdmin>> modifyPasswd(
            @RequestParam("key") String key,
            @RequestParam("old_passwd") String oldPasswd,
            @RequestParam("new_passwd") String newPasswd
    ) {
        SchoolPlusResponse<SchoolAdmin> response = new SchoolPlusResponse<>();
        return controllerUtils.doModifyPassword(key, oldPasswd, newPasswd, teacherService, response);
    }

    @RequestMapping(
            value = "/teachers",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<List<Teacher>>> getTeachers(
            @RequestParam("key") String key,
            @RequestParam("is_audited") Boolean isAudited
    ) {
        SchoolPlusResponse<List<Teacher>> response = new SchoolPlusResponse<>();

        String idStr = controllerUtils.verifyKey(key, response);
        if (idStr == null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        Long id = Long.parseLong(idStr);
        SchoolAdmin schoolAdmin = schoolAdminService.findById(id);
        List<Teacher> teachers;
        if (isAudited == null) {
            teachers = teacherService.findBySchoolName(
                    schoolAdmin.getSchool().getName());
        } else {
            teachers = teacherService.findBySchoolNameAndIsAudited(
                    schoolAdmin.getSchool().getName(), isAudited);
        }
        if (teachers == null) {
            response.setStatusCode(204);
            response.setResult("NoContent");
            response.setShortMessage("没有任何记录");

            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } else {
            response.setStatusCode(200);
            response.setResult("OK");
            response.setData(teachers);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @RequestMapping(
            value = "/teacher",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<Teacher>> getTeacher(
            @RequestParam("key") String key,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "email", required = false) String emailAddress,
            @RequestParam(value = "mobilePhoneNumber", required = false) String mobilePhoneNumber
    ) {
        SchoolPlusResponse<Teacher> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) == null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        if (id != null) {
            Teacher teacher = teacherService.findById(id);
            return controllerUtils.getResponseEntity(teacher);
        }

        if (username != null) {
            Teacher teacher = teacherService.findByUsername(username);
            return controllerUtils.getResponseEntity(teacher);
        }

        if (emailAddress != null) {
            Teacher teacher = teacherService.findByEmailAddress(emailAddress);
            return controllerUtils.getResponseEntity(teacher);
        }

        if (mobilePhoneNumber != null) {
            Teacher teacher = teacherService.findByMobilePhoneNumber(mobilePhoneNumber);
            return controllerUtils.getResponseEntity(teacher);
        }

        response.setStatusCode(400);
        response.setResult("BadRequest");
        response.setShortMessage("请指定筛选条件");

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(
            value = "/teacher/authenticate/{id}/privilege/{privilege}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<Teacher>> authenticateTeacher(
            @RequestParam("key") String key,
            @PathVariable("id") Long id,
            @PathVariable("privilege") Short privilege
    ) {
        SchoolPlusResponse<Teacher> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) == null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        Teacher oldTeacher = teacherService.findById(id);
        if (oldTeacher == null) {
            response.setStatusCode(400);
            response.setResult("BadRequest");
            response.setShortMessage("不存在id为" + id + "的教师");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        oldTeacher.setPrivilege(privilege);
        Teacher newTeacher = teacherService.save(oldTeacher);
        if (newTeacher == null) {
            response.setStatusCode(500);
            response.setResult("InternalError");
            response.setShortMessage("服务器内部错误，无法更新教师权限信息");

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.setStatusCode(200);
        response.setResult("OK");
        response.setData(newTeacher);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/school_videos",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<List<SchoolVideo>>> getSchoolVideos(
            @RequestParam("key") String key
    ) {
        SchoolPlusResponse<List<SchoolVideo>> response = new SchoolPlusResponse<>();

        String adminIdStr = controllerUtils.verifyKey(key, response);
        if (adminIdStr == null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        Long adminId = Long.parseLong(adminIdStr);

        SchoolAdmin schoolAdmin = schoolAdminService.findById(adminId);

        List<SchoolVideo> schoolVideos = schoolVideoService.findBySchoolName(
                schoolAdmin.getSchool().getName());
        if (schoolVideos == null) {
            response.setStatusCode(204);
            response.setResult("NoEntity");
            response.setShortMessage("贵校尚未上传校内课堂视频");

            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } else {
            response.setStatusCode(200);
            response.setResult("OK");
            response.setData(schoolVideos);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @RequestMapping(
            value = "/school_video/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<SchoolVideo>> getSchoolVideo(
            @RequestParam("key") String key,
            @PathVariable("id") Long id
    ) {
        SchoolPlusResponse<SchoolVideo> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) != null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        SchoolVideo schoolVideo = schoolVideoService.findById(id);
        return controllerUtils.getResponseEntity(schoolVideo);
    }

    @RequestMapping(
            value = "/school_video/delete/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<Boolean>> deleteSchoolVideo(
            @RequestParam("key") String key,
            @PathVariable("id") Long id
    ) {
        SchoolPlusResponse<Boolean> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) == null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        SchoolVideo video = schoolVideoService.findById(id);
        if (video == null) {
            response.setStatusCode(400);
            response.setResult("BadRequest");
            response.setShortMessage("该视频不存在");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            Boolean isDeleted = schoolVideoService.deleteById(id);
            if (! isDeleted) {
                response.setStatusCode(500);
                response.setResult("InternalError");
                response.setShortMessage("服务器内部错误，无法删除视频");

                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                response.setStatusCode(200);
                response.setResult("OK");
                response.setData(Boolean.TRUE);

                return new ResponseEntity<>(response, HttpStatus.OK);

            }
        }
    }

    @RequestMapping(
            value = "/school_video/audit/{id}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<SchoolVideo>> authenticateSchoolVideo(
            @RequestParam("key") String key,
            @PathVariable("id") Long id
    ) {
        // TODO authenticate school video of id

        return null;
    }

}
