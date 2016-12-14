package com.websystique.springmvc.service;

import com.websystique.springmvc.model.Teacher;

import java.util.List;

/**
 * Created by kevin on 2016/11/27.
 */
public interface TeacherService extends BaseUserService<Teacher> {
    List<Teacher> findAll();

    List<Teacher> findBySchoolName(String name);


    List<Teacher> findBySchoolNameAndIsAudited(String schoolName, Boolean isAudited);
}
