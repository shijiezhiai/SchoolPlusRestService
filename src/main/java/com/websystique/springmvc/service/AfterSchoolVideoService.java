package com.websystique.springmvc.service;

import com.websystique.springmvc.model.AfterSchoolVideo;

import java.util.List;

/**
 * Created by kevin on 2016/11/27.
 */
public interface AfterSchoolVideoService {

    AfterSchoolVideo findById(Long id);

    List<AfterSchoolVideo> findByGradeAndCourse(Integer grade, String course);

    List<AfterSchoolVideo> findByGrade(Integer grade);

}
