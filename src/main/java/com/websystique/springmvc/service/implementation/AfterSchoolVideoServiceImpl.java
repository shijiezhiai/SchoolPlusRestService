package com.websystique.springmvc.service.implementation;

import com.websystique.springmvc.model.AfterSchoolVideo;
import com.websystique.springmvc.model.SchoolVideo;
import com.websystique.springmvc.repository.AfterSchoolVideoRepository;
import com.websystique.springmvc.repository.SchoolVideoRepository;
import com.websystique.springmvc.service.AfterSchoolVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kevin on 2016/11/27.
 */
@Service
public class AfterSchoolVideoServiceImpl implements AfterSchoolVideoService {

    @Autowired
    AfterSchoolVideoRepository afterSchoolVideoRepository;

    @Override
    public AfterSchoolVideo findById(Long id) {
        return afterSchoolVideoRepository.findOne(id);
    }

    @Override
    public List<AfterSchoolVideo> findByGradeAndCourse(Short grade, String course) {
        return afterSchoolVideoRepository.findByGradeAndCourse(grade, course);
    }

    @Override
    public List<AfterSchoolVideo> findByGrade(Short grade) {
        return afterSchoolVideoRepository.findByGrade(grade);
    }
}
