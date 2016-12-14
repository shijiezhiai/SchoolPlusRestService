package com.websystique.springmvc.service.implementation;

import com.google.gson.Gson;
import com.websystique.springmvc.model.Course;
import com.websystique.springmvc.repository.CourseRepository;
import com.websystique.springmvc.service.CourseService;
import com.websystique.springmvc.util.redis.JCacheTools;
import com.websystique.springmvc.util.redis.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

/**
 * Created by kevin on 2016/11/28.
 */
@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    JCacheTools jCacheTools;

    @Override
    public Course findById(Integer id) {
        String redisKey = RedisKeyUtils.courseIdKey(id);
        if (jCacheTools.existKey(redisKey)) {
            Gson gson = new Gson();
            return gson.fromJson(jCacheTools.getStringFromJedis(redisKey), Course.class);
        } else {
            return courseRepository.findOne(id);
        }
    }

    @Override
    public Course findByClassGradeIdAndSubjectName(Integer classGradeId, String subjectsStr) {
        return courseRepository.findByClassGradeIdAndSubjectName(classGradeId, subjectsStr);
    }

    @Override
    public List<Course> findByClassGradeId(Integer classGradeId) {
        return courseRepository.findByClassGradeId(classGradeId);
    }

    @Override
    public List<Course> findBySubjectName(String subjectName) {
        return courseRepository.findBySubjectName(subjectName);
    }
}
