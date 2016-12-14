package com.websystique.springmvc.service;

import java.util.List;

import com.websystique.springmvc.model.School;
import com.websystique.springmvc.model.SchoolSuper;

/**
 * Created by kevin on 2016/11/24.
 */
public interface SchoolService {

    School findByName(String name);

}
