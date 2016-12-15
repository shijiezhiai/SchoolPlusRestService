package com.websystique.springmvc.controller;

import java.util.List;

import com.websystique.springmvc.constant.Constants;
import com.websystique.springmvc.util.ControllerUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.websystique.springmvc.service.AdvertisementService;
import com.websystique.springmvc.service.SchoolService;
import com.websystique.springmvc.service.SchoolSuperService;
import com.websystique.springmvc.types.SchoolPlusResponse;
import com.websystique.springmvc.model.Admin;
import com.websystique.springmvc.service.AdminService;
import com.websystique.springmvc.model.Advertisement;
import com.websystique.springmvc.model.SchoolSuper;
import com.websystique.springmvc.util.Utils;
import com.websystique.springmvc.util.push.PushUtils;
import com.websystique.springmvc.util.redis.JCacheTools;

@RestController
@RequestMapping("/admin")
public class AdminRestController {

    private static final Logger LOGGER = Logger.getLogger(AdminRestController.class);

	@Autowired
	AdminService adminService;  //Service which will do all data retrieval/manipulation work

    @Autowired
    SchoolService schoolService;

    @Autowired
    AdvertisementService advertisementService;

    @Autowired
    SchoolSuperService schoolSuperService;

    @Autowired
    JCacheTools jCacheTools;

    @Autowired
    PushUtils pushUtils;

    @Autowired
    ControllerUtils controllerUtils;


    @RequestMapping(
            value = "/register",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<Admin>> register(
            @RequestBody Admin admin
    ) {
        SchoolPlusResponse<Admin> response = new SchoolPlusResponse<>();

        return controllerUtils.doRegister(admin, adminService, response);
    }

	@RequestMapping(
	        value = "/login",
			method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
	public ResponseEntity<SchoolPlusResponse<Admin>> login(
            @RequestParam(value = "device_type", required = false) String devType,
            @RequestParam(value = "device_token", required = false) String devToken,
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        SchoolPlusResponse<Admin> response = new SchoolPlusResponse<>();

        Admin admin = adminService.findByUsername(username);

        return controllerUtils.doLogin(devType, devToken, username, password, admin,
                Constants.ADMIN_KEY_PREFIX, response);
	}

    @RequestMapping(
            value = "/logout",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<Boolean>> logout(
            @RequestParam(value = "key") String key) {

        Admin admin = adminService.findById(
                Long.parseLong(jCacheTools.getStringFromJedis(key)));

        return  controllerUtils.doLogout(key, admin);
    }

    @RequestMapping(
            value = "/modify_password",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<Admin>> modifyPassword(
            @RequestParam("key") String key,
            @RequestParam("old_password") String oldPassword,
            @RequestParam("new_password") String newPassword
    ) {
        SchoolPlusResponse<Admin> response = new SchoolPlusResponse<>();

        return controllerUtils.doModifyPassword(
                key, oldPassword, newPassword, adminService, response);
    }

    @RequestMapping(
            value = "/school_supers",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<List<SchoolSuper>>> listSchoolSupers(
            @RequestParam("key") String key,
            @RequestParam(value = "school", required = false) String school,
            @RequestParam(value = "audited", required = false) Boolean isAudited
    ) {
        SchoolPlusResponse<List<SchoolSuper>> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) != null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        List<SchoolSuper> schoolSupers;
        if (school == null && isAudited == null) {
            schoolSupers = schoolSuperService.findAll();
        } else if (school == null) {
            schoolSupers = schoolSuperService.findByIsAudited(isAudited);
        } else if (isAudited == null) {
             schoolSupers = schoolSuperService.findBySchoolName(school);
        } else {
            schoolSupers = schoolSuperService.findBySchoolNameAndIsAudited(school, isAudited);
        }

        return controllerUtils.getResponseEntity(schoolSupers);
    }

    @RequestMapping(
            value = "/school_super/{id}/authenticate/{privilege}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<SchoolSuper>> authenticateSchoolSuper(
            @RequestParam("key") String key,
            @PathVariable("id") Long id,
            @PathVariable("privilege") Integer privilege
    ) {
        SchoolPlusResponse<SchoolSuper> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) != null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        SchoolSuper schoolSuper = schoolSuperService.findById(id);
        if (schoolSuper == null) {
            response.setStatusCode(400);
            response.setResult("NoEntity");
            response.setShortMessage("没有找到id为" + id + "的校园超级管理员");
            response.setData(null);

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        boolean isValidPrivilege = Utils.checkSchoolSuperPrivilege(privilege);
        if (! isValidPrivilege) {
            response.setStatusCode(400);
            response.setResult("BadPrivilege");
            response.setShortMessage(privilege + "不是校园超级管理员的合法权限值");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        schoolSuper.setPrivilege(privilege);
        SchoolSuper updatedSchoolSuper = schoolSuperService.save(schoolSuper);
        if (updatedSchoolSuper == null) {
            response.setStatusCode(500);
            response.setResult("InternalError");
            response.setShortMessage("服务器内部错误，无法更新校园超级管理员权限");
            response.setData(null);

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.setStatusCode(200);
        response.setResult("OK");
        response.setData(updatedSchoolSuper);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/school_super/delete/{id}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<Boolean>> deleteSchoolSuper(
            @RequestParam("key") String key,
            @PathVariable("id") Long id
    ) {
        SchoolPlusResponse<Boolean> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) != null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        Boolean deleted = schoolSuperService.deleteById(id);
        if (deleted) {
            response.setStatusCode(200);
            response.setResult("OK");
            response.setData(Boolean.TRUE);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.setStatusCode(400);
            response.setResult("NoEntity");
            response.setShortMessage("无法删除id为" + id + "的校园超级管理员");
            response.setData(Boolean.FALSE);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(
            value = "/ads",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<List<Advertisement>>> listAdvertisements(
            @RequestParam("key") String key,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "advertiser", required = false) String advertiser
    ) {
        SchoolPlusResponse<List<Advertisement>> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) != null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        List<Advertisement> ads;
        if (type == null && advertiser == null) {
            ads = advertisementService.findAll();
        } else if (type == null) {
            ads = advertisementService.findByType(type);
        } else if (advertiser == null) {
            ads = advertisementService.findByAdvertiser(advertiser);
        } else {
            ads = advertisementService.findByAdvertiserAndType(advertiser, type);
        }

        return controllerUtils.getResponseEntity(ads);
    }

    @RequestMapping(
            value = "/ad/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<Advertisement>> getAdvertisement(
            @RequestParam("key") String key
    ) {
        SchoolPlusResponse<Advertisement> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) != null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        Long id = Long.parseLong(jCacheTools.getStringFromJedis(key));
        Advertisement advertisement = advertisementService.findById(id);
        return controllerUtils.getResponseEntity(advertisement);
    }

    @RequestMapping(
            value = "/ad",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<Advertisement>> saveAdvertisement(
            @RequestParam("key") String key,
            @RequestBody Advertisement ad
    ) {
        SchoolPlusResponse<Advertisement> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) != null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        Advertisement savedAd = advertisementService.save(ad);

        response.setStatusCode(200);
        response.setResult("OK");
        response.setData(savedAd);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/ad/delete/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<Boolean>> deleteAd(
            @RequestParam("key") String key,
            @PathVariable("id") Long id
    ) {
        SchoolPlusResponse<Boolean> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) != null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        Boolean res = advertisementService.deleteById(id);
        if (res) {
            response.setStatusCode(200);
            response.setResult("OK");
            response.setData(res);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.setStatusCode(400);
            response.setResult("NoEntity");
            response.setData(Boolean.FALSE);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

    }
}
