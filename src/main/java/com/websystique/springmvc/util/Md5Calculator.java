package com.websystique.springmvc.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by yangyma on 11/23/16.
 */
public class Md5Calculator {

    private static MessageDigest md5Calculator;
    static {
        try {
            md5Calculator = MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException e) {
            // Ignore as this will not happen
        }
    }

    public static String calMd5(String source) throws CloneNotSupportedException {
        MessageDigest calculator = (MessageDigest)md5Calculator.clone();
        calculator.update(source.getBytes());
        return calculator.digest().toString();
    }
}
