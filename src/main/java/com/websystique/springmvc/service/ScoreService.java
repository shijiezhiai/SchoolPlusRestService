package com.websystique.springmvc.service;

import java.sql.Date;
import java.util.List;

import com.websystique.springmvc.model.*;

/**
 * Created by yangyma on 11/27/16.
 */
public interface ScoreService {

    Score findByStudentAndSubject(Student student, Subject subject);

    List<Score> findByStudent(Student student);

    List<Score> findByTeacher(Teacher teacher);

    List<Score> save(List<Score> scores);

    Date getLastDateByCourse(Course course);

    List<Score> findByCourseAndDate(Course course, Date date);

    List<Score> findByCourse(Course course);

    List<Score> findByDate(Date date);

    List<Score> findByStudentAndCourseAndDate(Student student, Course course, Date date);
}
