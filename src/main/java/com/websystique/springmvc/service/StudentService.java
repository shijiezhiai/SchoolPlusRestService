package com.websystique.springmvc.service;

import java.util.List;
import com.websystique.springmvc.model.Student;

/**
 * Created by yangyma on 11/29/16.
 */
public interface StudentService {

    Student findById(Long id);

    List<Student> findByParentId(Long id);

    List<Student> findByIds(List<Long> studentIds);
}
