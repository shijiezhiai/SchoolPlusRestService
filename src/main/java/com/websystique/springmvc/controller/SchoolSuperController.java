package com.websystique.springmvc.controller;

import com.websystique.springmvc.model.SchoolAdmin;
import com.websystique.springmvc.model.SchoolSuper;
import com.websystique.springmvc.service.SchoolAdminService;
import com.websystique.springmvc.service.SchoolService;
import com.websystique.springmvc.service.SchoolSuperService;
import com.websystique.springmvc.types.SchoolPlusResponse;
import com.websystique.springmvc.util.ControllerUtils;
import com.websystique.springmvc.util.Utils;
import com.websystique.springmvc.util.redis.JCacheTools;
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
@RequestMapping(value = "/school_super")
public class SchoolSuperController {

    @Autowired
    SchoolSuperService schoolSuperService;

    @Autowired
    SchoolAdminService schoolAdminService;

    @Autowired
    SchoolService schoolService;

    @Autowired
    JCacheTools jCacheTools;

    @Autowired
    ControllerUtils controllerUtils;

    @RequestMapping(
            value = "/login",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<SchoolSuper>> login(
            @RequestParam(value = "device_type", required = false) String devType,
            @RequestParam(value = "device_token", required = false) String devToken,
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        SchoolPlusResponse<SchoolSuper> response = new SchoolPlusResponse<>();

        SchoolSuper schoolSuper = schoolSuperService.findByUsername(username);

        return controllerUtils.doLogin(devType, devToken, username, password, schoolSuper, response);
    }

    @RequestMapping(
            value = "/logout",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<Boolean>> logout(
            @RequestParam(value = "key") String key) {

        SchoolSuper schoolSuper = schoolSuperService.findById(
                Long.parseLong(jCacheTools.getStringFromJedis(key)));

        return controllerUtils.doLogout(key, schoolSuper);
    }

    @RequestMapping(
            value = "/modify_passwd",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<SchoolSuper>> modifyPasswd(
            @RequestParam("key") String key,
            @RequestParam("old_passwd") String oldPasswd,
            @RequestParam("new_passwd") String newPasswd
    ) {
        SchoolPlusResponse<SchoolSuper> response = new SchoolPlusResponse<>();

        return controllerUtils.doModifyPassword(key, oldPasswd, newPasswd, schoolSuperService, response);
    }

    @RequestMapping(
            value = "/register",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<SchoolSuper>> register(
            @RequestBody SchoolSuper schoolSuper
    ) {
        SchoolPlusResponse<SchoolSuper> response = new SchoolPlusResponse<>();

        return controllerUtils.doRegister(schoolSuper, schoolSuperService, response);
    }

    @RequestMapping(
            value = "/school_admins",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SchoolPlusResponse<List<SchoolAdmin>>> listAdmins(
            @RequestParam("key") String key,
            @RequestParam(value = "audited", required = false) Boolean isAudited
    ) {
        SchoolPlusResponse<List<SchoolAdmin>> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) == null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        List<SchoolAdmin> schoolAdmins;
        if (isAudited == null) {
            schoolAdmins = schoolAdminService.findAll();
        } else {
            schoolAdmins = schoolAdminService.findByIsAudited(isAudited);
        }
        return  controllerUtils.getResponseEntity(schoolAdmins);
    }

    @RequestMapping(
            value = "/school_admin",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<SchoolAdmin>> getSchoolAdmin(
            @RequestParam("key") String key,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "email", required = false) String emailAddress,
            @RequestParam(value = "mobilePhoneNumber", required = false) String mobilePhoneNumber
    ) {
        SchoolPlusResponse<SchoolAdmin> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) == null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        if (id != null) {
            SchoolAdmin schoolAdmin = schoolAdminService.findById(id);
            return controllerUtils.getResponseEntity(schoolAdmin);
        }

        if (username != null) {
            SchoolAdmin schoolAdmin = schoolAdminService.findByUsername(username);
            return controllerUtils.getResponseEntity(schoolAdmin);
        }

        if (emailAddress != null) {
            SchoolAdmin schoolAdmin = schoolAdminService.findByEmailAddress(emailAddress);
            return controllerUtils.getResponseEntity(schoolAdmin);
        }

        if (mobilePhoneNumber != null) {
            SchoolAdmin schoolAdmin = schoolAdminService.findByMobilePhoneNumber(mobilePhoneNumber);
            return controllerUtils.getResponseEntity(schoolAdmin);
        }

        response.setStatusCode(400);
        response.setResult("BadRequest");
        response.setShortMessage("请指定筛选条件");

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(
            value = "/school_admin/authenticate/{id}/privilege/{privilege}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<SchoolAdmin>> authenticateSchoolAdmin(
            @RequestParam("key") String key,
            @PathVariable("id") Long id,
            @PathVariable("privilege") Integer privilege
    ) {
        SchoolPlusResponse<SchoolAdmin> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) == null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        SchoolAdmin schoolAdmin = schoolAdminService.findById(id);
        if (schoolAdmin == null) {
            response.setStatusCode(400);
            response.setResult("BadRequest");
            response.setShortMessage("无法找到id为" + id + "的校园管理员");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Boolean isValidPrivilege = Utils.validateSchoolAdminPrivilege(privilege);
        if (! isValidPrivilege) {
            response.setStatusCode(400);
            response.setResult("InvalidPrivilege");
            response.setShortMessage("无效的校园管理员权限");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        schoolAdmin.setPrivilege(privilege);
        SchoolAdmin savedSchoolAdmin = schoolAdminService.update(schoolAdmin);
        if (savedSchoolAdmin == null) {
            response.setStatusCode(500);
            response.setResult("InternalError");
            response.setShortMessage("无法更新校园管理员的权限");

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            response.setStatusCode(200);
            response.setResult("OK");
            response.setData(savedSchoolAdmin);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @RequestMapping(
            value = "/school_admin/delete/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<Boolean>> deleteSchoolAdmin(
            @RequestParam("key") String key,
            @PathVariable("id") Long id
    ) {
        SchoolPlusResponse<Boolean> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) == null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        SchoolAdmin schoolAdmin = schoolAdminService.findById(id);
        if (schoolAdmin == null) {
            response.setStatusCode(400);
            response.setResult("NoEntity");
            response.setShortMessage("无效的管理员id");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Boolean deleted = schoolAdminService.deleteById(id);
        if (deleted) {
            response.setStatusCode(500);
            response.setResult("InternalError");
            response.setShortMessage("删除校园管理员失败");

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            response.setStatusCode(200);
            response.setResult("OK");
            response.setData(Boolean.TRUE);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

}
