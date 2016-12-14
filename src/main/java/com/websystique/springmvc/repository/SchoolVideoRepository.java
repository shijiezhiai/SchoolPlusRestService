package com.websystique.springmvc.repository;

import com.websystique.springmvc.model.SchoolVideo;
import com.websystique.springmvc.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yangyma on 11/23/16.
 */
@Repository
public interface SchoolVideoRepository extends JpaRepository<SchoolVideo, Long> {

    @Query("SELECT v FROM SchoolVideo v WHERE v.school.name = ?1")
    List<SchoolVideo> findBySchoolName(String schoolName);

    @Query("SELECT v FROM SchoolVideo v WHERE v.school.name = ?1 AND v.grade = ?2")
    List<SchoolVideo> findBySchoolNameAndGrade(
            String schoolName, Integer grade);

    @Query("SELECT v FROM SchoolVideo v WHERE v.school.name = ?1 AND v.subject = ?2")
    List<SchoolVideo> findBySchoolNameAndSubjectt(String schoolName, Subject subject);

    @Query("SELECT v FROM SchoolVideo v WHERE v.school.name = ?1 " +
            "AND v.grade = ?2 AND v.subject = ?3")
    List<SchoolVideo> findBySchoolNameAndGradeAndSubject(
            String schoolName, Integer grade, Subject subject);
}
