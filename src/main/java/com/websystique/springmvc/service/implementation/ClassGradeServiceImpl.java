package com.websystique.springmvc.service.implementation;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
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

    private LoadingCache<Long, ClassGrade> classGradeLoadingCache = CacheBuilder.<Long, ClassGrade>newBuilder()
            .expireAfterWrite(365, TimeUnit.DAYS)
            .build(
                    new CacheLoader<Long, ClassGrade>() {
                        @Override
                        public ClassGrade load(Long id) throws Exception {
                            return classGradeRepository.findOne(id);
                        }
                    });

    @Override
    public ClassGrade findById(Long id) {
        try {
            return classGradeLoadingCache.get(id);
        } catch (ExecutionException e) {
            return null;
        }
    }
}
