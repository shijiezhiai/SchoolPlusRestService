package com.websystique.springmvc.service.implementation;

import java.sql.Date;
import java.util.List;

import com.google.gson.Gson;
import com.websystique.springmvc.model.*;
import com.websystique.springmvc.repository.ScoreRepository;
import com.websystique.springmvc.service.ScoreService;
import com.websystique.springmvc.util.redis.JCacheTools;
import com.websystique.springmvc.util.redis.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yangyma on 11/27/16.
 */
@Service
public class ScoreServiceImpl implements ScoreService {

    @Autowired
    ScoreRepository scoreRepository;

    @Autowired
    JCacheTools jCacheTools;

    @Override
    public Score findByStudentAndSubject(Student student, Subject subject) {
        return scoreRepository.findByStudentAndSubject(student, subject);
    }

    @Override
    public List<Score> findByStudent(Student student) {
        return scoreRepository.findByStudent(student);
    }

    @Override
    public List<Score> findByTeacher(Teacher teacher) {
        return scoreRepository.findByTeacher(teacher);
    }

    @Override
    public List<Score> save(List<Score> scores) {
        return scoreRepository.save(scores);
    }

    @Override
    public Date getLastDateByCourse(Course course) {
        String classGradeScoreLastDateKey = RedisKeyUtils.courseScoreLastDateKey(course.getId());
        if (jCacheTools.existKey(classGradeScoreLastDateKey)) {
            Gson gson = new Gson();
            return gson.fromJson(jCacheTools.getStringFromJedis(classGradeScoreLastDateKey), Date.class);
        } else {
            return scoreRepository.getDatesByCourse(course).get(0);
        }
    }

    @Override
    public List<Score> findByCourseAndDate(Course course, Date date) {
        return scoreRepository.findByCourseAndDate(course, date);
    }

    @Override
    public List<Score> findByCourse(Course course) {
        return scoreRepository.findByCourse(course);
    }

    @Override
    public List<Score> findByDate(Date date) {
        return scoreRepository.findByDate(date);
    }
}
