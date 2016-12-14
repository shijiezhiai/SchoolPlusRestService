package com.websystique.springmvc.service;

import com.websystique.springmvc.model.Subject;

/**
 * Created by kevin on 2016/12/13.
 */
public interface SubjectService {
    Subject findByName(String name);
}
