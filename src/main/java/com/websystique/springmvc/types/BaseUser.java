package com.websystique.springmvc.types;

/**
 * Created by yangyma on 11/29/16.
 */
public abstract class BaseUser {

    /* The key will be used to identify whether the user login.
     * It has three parts:
     *      1. username
     *      2. current time stamp
     *      3. a 32-letters long secret key
    **/
    protected String currentKey;

    public String getCurrentKey() {
        return currentKey;
    }

    public void setCurrentKey(String currentKey) {
        this.currentKey = currentKey;
    }

    public abstract Long getId();

    public abstract String getUsername();

    public abstract String getEmailAddress();

    public abstract String getMobilePhoneNumber();

    public abstract String getPasswdMd5();

    public abstract void setPasswdMd5(String newPassword);
}
