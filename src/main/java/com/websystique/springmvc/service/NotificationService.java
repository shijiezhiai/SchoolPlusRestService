package com.websystique.springmvc.service;

import java.util.List;

import com.websystique.springmvc.model.ClassGrade;
import com.websystique.springmvc.model.Notification;
import com.websystique.springmvc.model.School;
import com.websystique.springmvc.model.Student;

/**
 * Created by kevin on 2016/12/3.
 */
public interface NotificationService {

    Notification findById(Long id);

    List<Notification> findByStudent(Student student);

    List<Notification> findByClassGrade(ClassGrade classGrade);

    List<Notification> findBySchool(School school);
}
