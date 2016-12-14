package com.websystique.springmvc.util.storage;

import java.net.URL;

/**
 * Created by kevin on 2016/12/7.
 */
public interface MediaStorage {

    void deletePicture(String src);

    void deleteVideo(String src);
}
