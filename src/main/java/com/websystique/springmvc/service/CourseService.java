package com.websystique.springmvc.service;

import com.websystique.springmvc.model.Course;

import java.util.List;

/**
 * Created by kevin on 2016/11/28.
 */
public interface CourseService {
    Course findById(Integer id);

    Course findByClassGradeIdAndSubjectName(Integer classGradeId, String subjectsStr);

    List<Course> findByClassGradeId(Integer classGradeId);

    List<Course> findBySubjectName(String subjectName);
}
