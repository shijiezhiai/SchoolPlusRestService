package com.websystique.springmvc.repository;

import com.websystique.springmvc.model.ClassGrade;
import com.websystique.springmvc.model.Notification;
import com.websystique.springmvc.model.School;
import com.websystique.springmvc.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by kevin on 2016/12/3.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Notification findById(Long id);

    @Query("SELECT n FROM Notification n WHERE n.student = ?1")
    List<Notification> findByStudent(Student student);

    @Query("SELECT n FROM Notification n WHERE n.classGrade = ?1")
    List<Notification> findByClassGrade(ClassGrade classGrade);

    @Query("SELECT n FROM Notification n WHERE n.school = ?1")
    List<Notification> findBySchool(School school);
}
