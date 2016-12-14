package com.websystique.springmvc.repository;

import com.websystique.springmvc.model.AfterSchoolVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by kevin on 2016/11/27.
 */
@Repository
public interface AfterSchoolVideoRepository extends JpaRepository<AfterSchoolVideo, Long> {

    @Query("SELECT v FROM AfterSchoolVideo v WHERE v.grade = ?1 AND v.course = ?2")
    List<AfterSchoolVideo> findByGradeAndCourse(Short grade, String course);

    @Query("SELECT v FROM AfterSchoolVideo v WHERE v.grade = ?1")
    List<AfterSchoolVideo> findByGrade(Short grade);

}
