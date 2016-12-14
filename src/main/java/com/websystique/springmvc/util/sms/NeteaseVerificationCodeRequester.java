package com.websystique.springmvc.util.sms;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import com.websystique.springmvc.constant.Constants;


/**
 * Created by yangyma on 11/29/16.
 */
public class NeteaseVerificationCodeRequester implements VerificationCodeRequester {

    private static HttpClient client = HttpClientBuilder.create().build();

    @Override
    public HttpResponse requestVerificationCode(String mobile, String deviceId, String templateId)
            throws Exception {

        String uri = Constants.NETEASE_IM_HOST + Constants.NETEASE_SMS_PATH + Constants.NETEASE_VERIFICATION_CODE_URI;


        Map<String, String> requestParamsMap = new HashMap<>();
        requestParamsMap.put("mobile", mobile);
        if (deviceId != null && ! deviceId.isEmpty()) {
            requestParamsMap.put("deviceId", deviceId);
        }
        if (templateId != null && ! templateId.isEmpty()) {
            requestParamsMap.put("templateId", templateId);
        }

        HttpPost post = composeCommonPost(uri, requestParamsMap);

        return client.execute(post);
    }

    @Override
    public HttpResponse verifyVerificationCode(String mobile, String code)
            throws Exception {
        String uri = Constants.NETEASE_IM_HOST + Constants.NETEASE_SMS_PATH +
                Constants.NETEASE_VERIFY_VERIFICATION_CODE_URI;


        Map<String, String> requestParamsMap = new HashMap<>();
        requestParamsMap.put("mobile", mobile);
        requestParamsMap.put("code", code);

        HttpPost post = composeCommonPost(uri, requestParamsMap);

        return client.execute(post);
    }

    private static HttpPost composeCommonPost(String uri, Map<String, String> postBodyMap) {

        String nonce = RandomStringUtils.random(32);
        String curTimestamp = ((Long)(System.currentTimeMillis()/1000)).toString();
        String checksum = NeteaseChecksumBuilder.getCheckSum(
                Constants.NETEASE_APP_KEY, nonce, curTimestamp);

        JSONObject requestParamsJson = new JSONObject(postBodyMap);
        String postBody = requestParamsJson.toString();

        HttpPost post = new HttpPost(uri);
        post.setHeader("AppKey", Constants.NETEASE_APP_KEY);
        post.setHeader("Nonce", nonce);
        post.setHeader("CurTime", curTimestamp.toString());
        post.setHeader("CheckSum", checksum);
        post.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        StringEntity body = new StringEntity(postBody, "UTF-8");
        post.setEntity(body);

        return post;
    }
}
