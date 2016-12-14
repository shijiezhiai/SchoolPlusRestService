package com.websystique.springmvc.service.implementation;

import java.util.List;
import com.websystique.springmvc.model.Student;
import com.websystique.springmvc.repository.StudentRepository;
import com.websystique.springmvc.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yangyma on 11/29/16.
 */
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Override
    public Student findById(Long id) {
        return studentRepository.findOne(id);
    }

    @Override
    public List<Student> findByParentId(Long id) {
        return studentRepository.findByParentId(id);
    }

    @Override
    public List<Student> findByIds(List<Long> studentIds) {
        return studentRepository.findAll(studentIds);
    }
}
