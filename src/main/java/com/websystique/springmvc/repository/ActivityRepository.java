package com.websystique.springmvc.repository;

import com.websystique.springmvc.model.Activity;
import com.websystique.springmvc.model.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by kevin on 2016/12/3.
 */
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    Activity findById(Long id);

    @Query("SELECT a FROM Activity a WHERE a.school = ?1")
    List<Activity> findBySchool(School school);
}
