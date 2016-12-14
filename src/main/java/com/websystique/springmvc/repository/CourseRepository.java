package com.websystique.springmvc.repository;

import com.websystique.springmvc.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yangyma on 11/23/16.
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    @Query("SELECT c FROM Course c WHERE c.classGrade.id = ?1 AND c.subject.name = ?2")
    Course findByClassGradeIdAndSubjectName(Integer classGradeId, String subjectsStr);

    @Query("SELECT c FROM Course c WHERE c.classGrade.id = ?1")
    List<Course> findByClassGradeId(Integer classGradeId);

    @Query("SELECT c FROM Course c WHERE c.subject.name = ?1")
    List<Course> findBySubjectName(String subjectName);
}
