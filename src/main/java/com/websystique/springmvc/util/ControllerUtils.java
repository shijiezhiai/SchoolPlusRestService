package com.websystique.springmvc.util;

import com.websystique.springmvc.constant.Constants;
import com.websystique.springmvc.service.BaseUserService;
import com.websystique.springmvc.types.BaseUser;
import com.websystique.springmvc.types.SchoolPlusResponse;
import com.websystique.springmvc.util.push.PushUtils;
import com.websystique.springmvc.util.redis.JCacheTools;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Created by yangyma on 11/30/16.
 */
@Component
public class ControllerUtils {

    private static final Logger LOGGER = Logger.getLogger(ControllerUtils.class);

    @Autowired
    JCacheTools jCacheTools;

    @Autowired
    PushUtils pushUtils;


    public <T extends SchoolPlusResponse> String verifyKey(String key, T response) {
        if (!jCacheTools.existKey(key)) {
            response.setStatusCode(403);
            response.setResult("Unauthorized");
            response.setShortMessage("用户未登录或登录已过期");

            return null;
        } else {
            return jCacheTools.getStringFromJedis(key);
        }
    }

    public  <S> ResponseEntity<SchoolPlusResponse<S>> getResponseEntity(S s) {
        SchoolPlusResponse<S> response = new SchoolPlusResponse<>();
        if (s == null) {
            response.setStatusCode(204);
            response.setResult("BadRequest");
            response.setShortMessage("未找到符合条件的记录");

            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } else {
            response.setStatusCode(200);
            response.setResult("OK");
            response.setData(s);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    public <T extends SchoolPlusResponse, S extends BaseUser> ResponseEntity<T> doLogin(
            String devType, String devToken, String username, String password, S user, T response) {
        if (user  == null) {
            response.setStatusCode(204);
            response.setResult("NoEntity");
            response.setShortMessage("该校园管理员不存在");

            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } else if (! user.getPasswdMd5().equals(password)) {
            response.setStatusCode(400);
            response.setResult("NotMatch");
            response.setShortMessage("用户名密码不匹配");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            String key = prepareLogin(
                    devType, devToken, Constants.SCHOOL_ADMIN_KEY_PREFIX, username, user.getId(), user);

            response.setStatusCode(200);
            response.setResult("OK");
            response.setData(user);

            user.setCurrentKey(key);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    public <T extends SchoolPlusResponse, U extends BaseUser, S extends BaseUserService> ResponseEntity<T>
            doModifyPassword(
                    String key, String oldPassword, String newPassword, S service, T response) {

        if (verifyKey(key, response) == null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        Long id = Long.parseLong(jCacheTools.getStringFromJedis(key));
        U user = (U)service.findById(id);
        if (user.getPasswdMd5().equals(oldPassword)) {
            user.setPasswdMd5(newPassword);
            U newUser = (U)service.save(user);
            if (newUser == null) {
                response.setStatusCode(500);
                response.setResult("InternalError");
                response.setResult("服务器内部错误，无法更新密码");

                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            response.setStatusCode(200);
            response.setResult("OK");
            response.setData(newUser);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.setStatusCode(200);
            response.setResult("Unauthorized");
            response.setShortMessage("旧密码错误，无法更新密码");

            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }

    public <T extends SchoolPlusResponse, U extends BaseUser, S extends BaseUserService> ResponseEntity<T>
            doRegister(U user, S userService, T response) {

        U userByUsername = (U)userService.findByUsername(user.getUsername());
        if (userByUsername != null) {
            response.setStatusCode(400);
            response.setResult("BadRequest");
            response.setShortMessage("用户名已存在");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        U userByEmail = (U)userService.findByEmailAddress(user.getEmailAddress());
        if (userByEmail != null) {
            response.setStatusCode(400);
            response.setResult("BadRequest");
            response.setShortMessage("电子邮件地址已被注册");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        U userByMobilePhoneNumber = (U)userService.findByMobilePhoneNumber(user.getMobilePhoneNumber());
        if (userByMobilePhoneNumber != null) {
            response.setStatusCode(400);
            response.setResult("BadRequest");
            response.setShortMessage("手机号码已被注册");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        U savedUser = (U)userService.save(user);
        response.setStatusCode(200);
        response.setResult("OK");
        response.setData(savedUser);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public  <T> String prepareLogin(
            String deviceType, String deviceToken, String prefix, String username, Long id, T user) {

        deviceType = (deviceType == null || deviceType.isEmpty()) ? Constants.LOGIN_DEV_WEB : deviceType;
        String key = Utils.calculateKey(deviceType, prefix, username);
        if (jCacheTools.existKey(key)) {
            doLogout(key, user);
            if (! deviceType.equals(Constants.LOGIN_DEV_APP)) {
                // Remember the deviceToken of the user, so that following calls can be made without the deviceToken.
                jCacheTools.addStringToJedis(
                        prefix + "|" + username, deviceToken, Constants.APP_EXPIRE_TIME);
                try {
                    pushUtils.sendLogoutPush(deviceType, deviceToken);
                } catch (Exception e) {
                    LOGGER.error("Failed to send push notification to " +
                            prefix + "|" + username + ":" + deviceToken + ":", e);
                }
            }
        }

        jCacheTools.addStringToJedis(key, id.toString(),
                (deviceType.equals(Constants.LOGIN_DEV_APP)) ? Constants.APP_EXPIRE_TIME : Constants.WEB_EXPIRE_TIME);

        return key;
    }

    public <T> ResponseEntity<SchoolPlusResponse<Boolean>> doLogout(String key, T user) {
        SchoolPlusResponse<Boolean> response = new SchoolPlusResponse<>();

        if (verifyKey(key, response) == null) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        if (user == null) {
            response.setStatusCode(400);
            response.setResult("BadRequest");
            response.setShortMessage("该管理员不存在");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            jCacheTools.delKeyFromJedis(key);

            response.setStatusCode(200);
            response.setResult("OK");
            response.setData(Boolean.TRUE);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}
