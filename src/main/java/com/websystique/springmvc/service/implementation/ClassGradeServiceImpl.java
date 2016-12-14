package com.websystique.springmvc.service.implementation;

import com.google.gson.Gson;
import com.websystique.springmvc.configuration.RedisConfig;
import com.websystique.springmvc.model.ClassGrade;
import com.websystique.springmvc.repository.ClassGradeRepository;
import com.websystique.springmvc.service.ClassGradeService;
import com.websystique.springmvc.util.redis.JCacheTools;
import com.websystique.springmvc.util.redis.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by kevin on 2016/12/4.
 */
@Service
public class ClassGradeServiceImpl implements ClassGradeService {

    @Autowired
    ClassGradeRepository classGradeRepository;

    @Autowired
    JCacheTools jCacheTools;


    @Override
    public ClassGrade findById(Integer id) {
        Gson gson = new Gson();

        String redisKey = RedisKeyUtils.classGradeIdKey(id);
        if (jCacheTools.existKey(redisKey)) {
            return gson.fromJson(jCacheTools.getStringFromJedis(redisKey), ClassGrade.class);
        } else {
            return classGradeRepository.findOne(id);
        }
    }
}
