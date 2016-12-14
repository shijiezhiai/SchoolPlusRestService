package com.websystique.springmvc.service;

import com.websystique.springmvc.model.Activity;
import com.websystique.springmvc.model.School;

import java.util.List;

/**
 * Created by kevin on 2016/12/3.
 */
public interface ActivityService {

    Activity findById(Long id);

    List<Activity> findBySchool(School school);

}
