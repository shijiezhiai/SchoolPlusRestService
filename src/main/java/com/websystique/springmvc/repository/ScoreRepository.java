package com.websystique.springmvc.repository;

import java.sql.Date;
import java.util.List;

import com.websystique.springmvc.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by yangyma on 11/27/16.
 */
@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    @Query("SELECT score FROM Score score WHERE score.student = ?1")
    List<Score> findByStudent(Student student);


    @Query("SELECT s FROM Score s WHERE s.teacher = ?1")
    List<Score> findByTeacher(Teacher teacher);

    @Query("SELECT s FROM Score s WHERE s.student = ?1 and s.course.subject = ?2")
    Score findByStudentAndSubject(Student student, Subject subject);

    @Query("SELECT s.date FROM Score s WHERE s.course = ?1 ORDER BY s.date DESC")
    List<Date> getDatesByCourse(Course course);

    @Query("SELECT s FROM Score s WHERE s.course = ?1 AND s.date = ?2")
    List<Score> findByCourseAndDate(Course course, Date date);

    @Query("SELECT s FROM Score s WHERE s.course = ?1")
    List<Score> findByCourse(Course course);

    @Query("SELECT s FROM Score s WHERE s.date = ?1")
    List<Score> findByDate(Date date);

    @Query("SELECT s FROM Score s WHERE s.student = ?1 AND s.course = ?2 AND s.date = ?3")
    List<Score> findByStudentAndCourseAndDate(Student student, Course course, Date date);

}
