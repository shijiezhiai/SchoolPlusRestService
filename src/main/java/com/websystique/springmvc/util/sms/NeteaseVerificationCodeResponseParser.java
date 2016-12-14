package com.websystique.springmvc.util.sms;

import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.json.JSONObject;

/**
 * Created by yangyma on 11/29/16.
 */
public class NeteaseVerificationCodeResponseParser implements VerificationCodeResponseParser {
    @Override
    public String parseRequestVerificationCodeResponse(HttpResponse response) throws Exception {

        if (! validateCommonResponse(response)) {
            return null;
        }

        HttpEntity entity = response.getEntity();
        JSONObject contentJson = parseEntity(entity);

        return (String)contentJson.get("obj");
    }

    @Override
    public Boolean parseVerifyVerificationCodeResponse(HttpResponse response) throws Exception {
        if (! validateCommonResponse(response)) {
            return null;
        }

        HttpEntity entity = response.getEntity();
        JSONObject contentJson = parseEntity(entity);

        return ((int)contentJson.get("code")) == 200;
    }

    private JSONObject parseEntity(HttpEntity entity) throws Exception {
        InputStream contentStream = entity.getContent();
        byte[] contentBytes = new byte[contentStream.available()];
        contentStream.read(contentBytes);
        String content = contentBytes.toString();

        return new JSONObject(content);
    }

    private boolean validateCommonResponse(HttpResponse response) {
        if (response == null) {
            throw new IllegalArgumentException();
        }

        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() != 200) {
            return false;
        }

        HttpEntity entity = response.getEntity();
        if (! entity.getContentType().equals("application/json; charset=utf-8")) {
            return false;
        }

        return true;
    }
}
