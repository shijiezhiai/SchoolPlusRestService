package com.websystique.springmvc.repository;

import java.util.List;
import com.websystique.springmvc.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by yangyma on 11/23/16.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT student FROM Student student WHERE student.parent.id = ?1")
    List<Student> findByParentId(Long id);

}
