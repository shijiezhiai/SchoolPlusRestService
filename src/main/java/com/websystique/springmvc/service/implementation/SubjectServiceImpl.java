package com.websystique.springmvc.service.implementation;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.websystique.springmvc.model.Subject;
import com.websystique.springmvc.repository.SubjectRepository;
import com.websystique.springmvc.service.SubjectService;
import com.websystique.springmvc.util.redis.JCacheTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

/**
 * Created by kevin on 2016/12/13.
 */
@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    SubjectRepository subjectRepository;

    private LoadingCache<String, Subject> subjectCache = CacheBuilder.newBuilder()
            .build(new CacheLoader<String, Subject>() {
                @Override
                public Subject load(String key) throws Exception {
                    return subjectRepository.findByName((String)key);
                }
            });

    @Override
    public Subject findByName(String name) {
        try {
            return subjectCache.get(name);
        } catch (ExecutionException e) {
            return null;
        }
    }
}
