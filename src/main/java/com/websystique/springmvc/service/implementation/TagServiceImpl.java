package com.websystique.springmvc.service.implementation;

import java.util.concurrent.ExecutionException;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.websystique.springmvc.model.Tag;
import com.websystique.springmvc.repository.TagRepository;
import com.websystique.springmvc.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yangyma on 12/14/16.
 */
public class TagServiceImpl implements TagService {

    @Autowired
    TagRepository tagRepository;

    private LoadingCache<String, Tag> tagLoadingCache = CacheBuilder.<String, Tag>newBuilder()
            .build(
                    new CacheLoader<String, Tag>() {
                        @Override
                        public Tag load(String name) throws Exception {
                            return tagRepository.findByName(name);
                        }
                    });

    public Tag getByName(String name) {
        try {
            return tagLoadingCache.get(name);
        } catch (ExecutionException e) {
            return null;
        }
    }
}
