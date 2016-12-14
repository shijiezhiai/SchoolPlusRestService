package com.websystique.springmvc.repository;

import com.websystique.springmvc.model.Homework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by yangyma on 11/27/16.
 */
@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Long> {
}
