package com.websystique.springmvc.controller;

import com.websystique.springmvc.model.Admin;
import com.websystique.springmvc.model.SuperAdmin;
import com.websystique.springmvc.service.AdminService;
import com.websystique.springmvc.service.SuperAdminService;
import com.websystique.springmvc.types.SchoolPlusResponse;
import com.websystique.springmvc.util.ControllerUtils;
import com.websystique.springmvc.util.redis.JCacheTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;

@RestController
@RequestMapping(value = "/super")
public class SuperAdminRestController {

	@Autowired
	AdminService adminService;  //Service which will do all data retrieval/manipulation work

	@Autowired
	SuperAdminService superAdminService;

    @Autowired
    JCacheTools jCacheTools;

    @Autowired
    ControllerUtils controllerUtils;

	@RequestMapping(
			value = "/login",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE,
			params = {"username", "passwd"}
	)
	public ResponseEntity<SchoolPlusResponse<Boolean>> login(
            @RequestParam(value = "device_type", required = false) String devType,
            @RequestParam(value = "device_token", required = false) String devToken,
            @RequestParam("username") String username,
            @RequestParam("password") String password) {

		SuperAdmin superAdmin = superAdminService.findByUsername(username);

		SchoolPlusResponse<Boolean> response = new SchoolPlusResponse<>();

        return controllerUtils.doLogin(devType, devToken, username, password, superAdmin, response);
	}

    @RequestMapping(
            value = "/logout",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<Boolean>> logout(
            @RequestParam(value = "key") String key) {

        SuperAdmin superAdmin = superAdminService.findById(
                Long.parseLong(jCacheTools.getStringFromJedis(key)));

        return controllerUtils.doLogout(key, superAdmin);
    }

    @RequestMapping(
            value = "/register",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<SuperAdmin>> register(
            @RequestBody SuperAdmin superAdmin
    ) {
        SchoolPlusResponse<SuperAdmin> response = new SchoolPlusResponse<>();

        return controllerUtils.doRegister(superAdmin, superAdminService, response);
    }

    @RequestMapping(
            value = "/modify_passwd",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<SuperAdmin>> modifyPasswd(
            @RequestParam("key") String key,
            @RequestParam("old_passwd") String oldPasswd,
            @RequestParam("new_passwd") String newPasswd
    ) {
        SchoolPlusResponse<SuperAdmin> response = new SchoolPlusResponse<>();

        return controllerUtils.doModifyPassword(key, oldPasswd, newPasswd, superAdminService, response);
    }

	@RequestMapping(
			value = "/admins",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SchoolPlusResponse<List<Admin>>> listAdmins() {
        SchoolPlusResponse<List<Admin>> response = new SchoolPlusResponse<>();
		System.out.println("Fetching all admins");
		List<Admin> admins = adminService.findAll();
		if (admins == null) {
			System.out.println("No admins found");
            response.setStatusCode(200);
            response.setResult("Empty");
            response.setShortMessage("无法找到任何管理员");
            response.setData(null);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		response.setStatusCode(200);
        response.setResult("OK");
        response.setData(admins);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@RequestMapping(
	        value = "/admin/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
	public ResponseEntity<SchoolPlusResponse<Admin>> findAdmin(
            @QueryParam("key") String key,
            @PathVariable("id") Long id) {

        SchoolPlusResponse<Admin> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) == null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

		Admin admin = adminService.findById(id);
		
		if (admin == null) {
            response.setStatusCode(204);
            response.setResult("NoEntity");
            response.setShortMessage("找不到符合条件的记录");
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
		} else {
            response.setStatusCode(200);
            response.setResult("OK");
            response.setData(admin);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

	}

    @RequestMapping(
            value = "/admin",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<Admin>> updateAdmin(
            @QueryParam("username") String username) {

        SchoolPlusResponse<Admin> response = new SchoolPlusResponse<>();
        System.out.println("Lookup Admin (username = " + username + ")");

        Admin admin = adminService.findByUsername(username);

        if (admin == null) {
            System.out.println("Admin with username ( " + username + ") not found");
            response.setStatusCode(200);
            response.setResult("NotFound");
            response.setShortMessage("没有找到任何username为" + username + "的Admin记录");
            response.setData(null);
        } else {
            System.out.println("Admin with username ( " + username + ") found");
            response.setStatusCode(200);
            response.setResult("OK");
            response.setData(admin);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/authenticate_admin/{id}/{privilege}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SchoolPlusResponse<Admin>> authenticateAdmin(
            @RequestParam("key") String key,
            @PathVariable("id") Long id,
            @PathVariable("privilege") Short privilege) {

        SchoolPlusResponse<Admin> response = new SchoolPlusResponse<>();

        if (controllerUtils.verifyKey(key, response) == null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        Admin admin = adminService.findById(id);
        admin.setPrivilege(privilege);
        adminService.save(admin);

        response.setStatusCode(200);
        response.setResult("OK");
        response.setData(admin);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/delete_admin/{id}",
            method = RequestMethod.POST
    )
    public ResponseEntity<SchoolPlusResponse<Boolean>> deleteAdmin(
            @PathVariable("id") Long id
    ) {
        adminService.deleteById(id);

        SchoolPlusResponse<Boolean> response = new SchoolPlusResponse<>();
        response.setStatusCode(200);
        response.setResult("OK");
        response.setData(Boolean.TRUE);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
