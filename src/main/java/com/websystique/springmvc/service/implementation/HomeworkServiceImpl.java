package com.websystique.springmvc.service.implementation;

import com.google.gson.Gson;
import com.websystique.springmvc.constant.Constants;
import com.websystique.springmvc.model.ClassGrade;
import com.websystique.springmvc.model.Homework;
import com.websystique.springmvc.repository.HomeworkRepository;
import com.websystique.springmvc.service.HomeworkService;
import com.websystique.springmvc.util.redis.JCacheTools;
import com.websystique.springmvc.util.redis.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangyma on 11/27/16.
 */
@Service
public class HomeworkServiceImpl implements HomeworkService {

    @Autowired
    HomeworkRepository homeworkRepository;

    @Autowired
    JCacheTools jCacheTools;


    @Override
    @Transactional
    public List<Homework> save(List<Homework> homeworkList) {
        List<Homework> savedHomeworkList = homeworkRepository.save(homeworkList);
        if (savedHomeworkList != null) {
            int size = savedHomeworkList.size();
            for (int i = 0; i < size; i++) {
                Homework savedHomework = savedHomeworkList.get(i);
                // Add the homework id to Redis
                jCacheTools.addStringToJedis(
                        RedisKeyUtils.classCourseHomeworkKey(
                                savedHomework.getCourse().getClassGrade(),
                                savedHomework.getCourse().getSubject().getName()),
                                savedHomework.getId().toString(),
                                Constants.HOMEWORK_REDIS_EXPIRE);
                // Add the homework to Redis
                Gson gson = new Gson();
                jCacheTools.addStringToJedis(
                        RedisKeyUtils.homeworkIdKey(savedHomework.getId()),
                        gson.toJson(savedHomework),
                        Constants.HOMEWORK_REDIS_EXPIRE);
            }
        }

        return savedHomeworkList;
    }

    @Override
    @Transactional
    public Homework update(Long id, Homework homework) {
        Homework oldHomework = homeworkRepository.findOne(id);
        if (oldHomework == null || (oldHomework.getCourse() != homework.getCourse())) {
            throw new IllegalArgumentException();
        }

        oldHomework.setAssigningTime(homework.getAssigningTime());
        oldHomework.setContent(homework.getContent());
        oldHomework.setDeadline(homework.getDeadline());

        Homework updateHomework = homeworkRepository.save(oldHomework);

        Gson gson = new Gson();
        jCacheTools.addStringToJedis(RedisKeyUtils.homeworkIdKey(id), gson.toJson(updateHomework), 0);

        return updateHomework;
    }

    @Override
    public Homework findById(Long id) {
        String redisKey = RedisKeyUtils.homeworkIdKey(id);
        if (jCacheTools.existKey(redisKey)) {
            Gson gson = new Gson();
            return gson.fromJson(
                    jCacheTools.getStringFromJedis(redisKey),
                    Homework.class);
        } else {
            return homeworkRepository.findOne(id);
        }
    }

    @Override
    @Transactional
    public Boolean deleteById(Long id) {
        String redisKey = RedisKeyUtils.homeworkIdKey(id);
        jCacheTools.delKeyFromJedis(redisKey);
        homeworkRepository.delete(id);

        return Boolean.TRUE;
    }
}
