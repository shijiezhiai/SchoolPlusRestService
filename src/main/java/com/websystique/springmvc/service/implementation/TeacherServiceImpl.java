package com.websystique.springmvc.service.implementation;

import com.websystique.springmvc.model.Teacher;
import com.websystique.springmvc.repository.TeacherRepository;
import com.websystique.springmvc.service.BaseUserService;
import com.websystique.springmvc.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kevin on 2016/11/27.
 */
@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    TeacherRepository teacherRepository;

    @Override
    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    @Override
    public List<Teacher> findBySchoolName(String schoolName) {
        return teacherRepository.findBySchoolName(schoolName);
    }

    @Override
    public List<Teacher> findBySchoolNameAndIsAudited(String schoolName, Boolean isAudited) {
        return teacherRepository.findBySchoolNameAndIsAudited(schoolName, isAudited);
    }

    @Override
    public Teacher findById(Long id) {
        return teacherRepository.findById(id);
    }

    @Override
    public Teacher findByUsername(String username) {
        return teacherRepository.findByUsername(username);
    }

    @Override
    public Teacher findByEmailAddress(String emailAddress) {
        return teacherRepository.findByEmailAddress(emailAddress);
    }

    @Override
    public Teacher findByMobilePhoneNumber(String mobilePhoneNumber) {
        return teacherRepository.findByMobilePhoneNumber(mobilePhoneNumber);
    }

    @Override
    public Teacher save(Teacher oldTeacher) {
        return teacherRepository.save(oldTeacher);
    }
}
