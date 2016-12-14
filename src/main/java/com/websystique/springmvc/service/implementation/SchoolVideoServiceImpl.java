package com.websystique.springmvc.service.implementation;

import com.websystique.springmvc.model.SchoolVideo;
import com.websystique.springmvc.model.Subject;
import com.websystique.springmvc.repository.SchoolVideoRepository;
import com.websystique.springmvc.service.SchoolVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kevin on 2016/11/27.
 */
@Service
public class SchoolVideoServiceImpl implements SchoolVideoService {

    @Autowired
    SchoolVideoRepository schoolVideoRepository;

    @Override
    public List<SchoolVideo> findBySchoolName(String schoolName) {
        return schoolVideoRepository.findBySchoolName(schoolName);
    }

    @Override
    public SchoolVideo findById(Long id) {
        return schoolVideoRepository.findOne(id);
    }

    @Override
    public Boolean deleteById(Long id) {
        try {
            schoolVideoRepository.delete(id);
        } catch (Exception e) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    @Override
    public List<SchoolVideo> findBySchoolNameAndGradeAndSubject(String schoolName, Short grade, Subject subject) {
        return schoolVideoRepository.findBySchoolNameAndGradeAndSubject(schoolName, grade, subject);
    }

    @Override
    public List<SchoolVideo> findBySchoolNameAndGrade(String schoolName, Short grade) {
        return schoolVideoRepository.findBySchoolNameAndGrade(
                schoolName, grade);
    }

    @Override
    public List<SchoolVideo> findBySchoolNameAndSubject(String schoolName, Subject subject) {
        return schoolVideoRepository.findBySchoolNameAndSubjectt(schoolName, subject);
    }

}
