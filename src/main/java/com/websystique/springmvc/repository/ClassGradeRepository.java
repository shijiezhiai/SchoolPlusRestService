package com.websystique.springmvc.repository;

import com.websystique.springmvc.model.ClassGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by yangyma on 11/23/16.
 */
@Repository
public interface ClassGradeRepository extends JpaRepository<ClassGrade, Long> {
}
