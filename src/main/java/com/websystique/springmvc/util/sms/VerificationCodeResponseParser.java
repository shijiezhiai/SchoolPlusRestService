package com.websystique.springmvc.util.sms;

import org.apache.http.HttpResponse;

/**
 * Created by yangyma on 11/29/16.
 */
public interface VerificationCodeResponseParser {

    String parseRequestVerificationCodeResponse(HttpResponse response) throws Exception;

    Boolean parseVerifyVerificationCodeResponse(HttpResponse response) throws Exception;

}
