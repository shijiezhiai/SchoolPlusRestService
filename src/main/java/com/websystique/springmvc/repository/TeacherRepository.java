package com.websystique.springmvc.repository;

import com.websystique.springmvc.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yangyma on 11/23/16.
 */
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    @Query("SELECT teacher FROM Teacher teacher WHERE teacher.school.name = ?1")
    List<Teacher> findBySchoolName(String schoolName);

    Teacher findById(Long id);

    Teacher findByUsername(String username);

    Teacher findByEmailAddress(String emailAddress);

    Teacher findByMobilePhoneNumber(String mobilePhoneNumber);

    @Query("SELECT t FROM Teacher t WHERE t.school.name = ?1 AND t.isAudited = ?2")
    List<Teacher> findBySchoolNameAndIsAudited(String schoolName, Boolean isAudited);
}
