package com.websystique.springmvc.util.sms;

import org.apache.http.HttpResponse;

/**
 * Created by yangyma on 11/29/16.
 */
public interface VerificationCodeRequester {

    HttpResponse requestVerificationCode(String mobile, String deviceId, String templateId) throws Exception;

    HttpResponse verifyVerificationCode(String mobile, String code) throws Exception;

}
