package com.websystique.springmvc.util.storage;

import org.springframework.stereotype.Component;

import java.net.URL;

/**
 * Created by kevin on 2016/12/7.
 */
@Component
public class QiniuMediaStorage implements MediaStorage {

    @Override
    public void deletePicture(String src) {
        return;
    }

    @Override
    public void deleteVideo(String src) {
        return;
    }
}
