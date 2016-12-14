package com.websystique.springmvc.types;

import java.io.Serializable;

/**
 * Created by kevin on 2016/11/24.
 */
public class SchoolPlusResponse<T> implements Serializable {

    private Integer statusCode;

    private String result;

    private String shortMessage;

    private String longMessage;

    private T data;

    public SchoolPlusResponse() {
    }

    public SchoolPlusResponse(
            Integer statusCode, String result, String shortMessage, String longMessage, T data) {
        this.statusCode = statusCode;
        this.result = result;
        this.shortMessage = shortMessage;
        this.longMessage = longMessage;
        this.data = data;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getShortMessage() {
        return shortMessage;
    }

    public void setShortMessage(String shortMessage) {
        this.shortMessage = shortMessage;
    }

    public String getLongMessage() {
        return longMessage;
    }

    public void setLongMessage(String longMessage) {
        this.longMessage = longMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SchoolPlusResponse{" +
                "statusCode=" + statusCode +
                ", result='" + result + '\'' +
                ", shortMessage='" + shortMessage + '\'' +
                ", longMessage='" + longMessage + '\'' +
                ", data=" + data +
                '}';
    }
}
