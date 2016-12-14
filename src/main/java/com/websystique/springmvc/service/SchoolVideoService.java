package com.websystique.springmvc.service;

import com.websystique.springmvc.model.SchoolVideo;
import com.websystique.springmvc.model.Subject;

import java.util.List;

/**
 * Created by kevin on 2016/11/27.
 */
public interface SchoolVideoService {

    List<SchoolVideo> findBySchoolName(String name);

    SchoolVideo findById(Long id);

    Boolean deleteById(Long id);

    List<SchoolVideo> findBySchoolNameAndGradeAndSubject(String schoolName, Short grade, Subject subject);

    List<SchoolVideo> findBySchoolNameAndGrade(String schoolName, Short grade);

    List<SchoolVideo> findBySchoolNameAndSubject(String schoolName, Subject subject);
}
