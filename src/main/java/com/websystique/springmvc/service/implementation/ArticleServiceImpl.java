package com.websystique.springmvc.service.implementation;

import com.google.gson.Gson;
import com.websystique.springmvc.constant.Constants;
import com.websystique.springmvc.model.Article;
import com.websystique.springmvc.model.ArticlePicture;
import com.websystique.springmvc.model.Subject;
import com.websystique.springmvc.repository.ArticlePictureRepository;
import com.websystique.springmvc.repository.ArticleRepository;
import com.websystique.springmvc.service.ArticleService;
import com.websystique.springmvc.util.redis.JCacheTools;
import com.websystique.springmvc.util.redis.RedisKeyUtils;
import com.websystique.springmvc.util.storage.MediaStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 2016/11/28.
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    ArticlePictureRepository articlePictureRepository;

    @Autowired
    JCacheTools jCacheTools;

    @Autowired
    MediaStorage mediaStorage;

    @Override
    public List<Article> findBySchoolNameAndGradeAndSubject(
            String schoolName, Integer grade, Subject subject) {
        return articleRepository.findBySchoolNameAndGradeAndSubject(
                schoolName, grade, subject);
    }

    @Override
    public List<Article> findBySchoolNameAndGrade(String schoolName, Integer grade) {
        return articleRepository.findBySchoolNameAndGrade(schoolName, grade);
    }

    @Override
    public List<Article> findBySchoolNameAndSubject(String schoolName, Subject subject) {
        return articleRepository.findBySchoolNameAndSubject(schoolName, subject);
    }

    @Override
    public List<Article> findBySchoolName(String schoolName) {
        return articleRepository.findBySchoolName(schoolName);
    }

    @Override
    public Article findById(Long id) {
        String redisKey = RedisKeyUtils.articleIdKey(id);
        if (jCacheTools.existKey(redisKey)) {
            Gson gson = new Gson();
            return gson.fromJson(
                    jCacheTools.getStringFromJedis(redisKey), Article.class);
        } else {
            return articleRepository.findOne(id);
        }
    }

    @Override
    @Transactional
    public Boolean deleteById(Long id) {
        String redisKey = RedisKeyUtils.articleIdKey(id);
        if (jCacheTools.existKey(redisKey)) {
            jCacheTools.delKeyFromJedis(redisKey);
        }

        List<ArticlePicture> articlePictures = findById(id).getPictures();
        articlePictureRepository.delete(articlePictures);
        articleRepository.delete(id);

        for (ArticlePicture pic : articlePictures) {
            mediaStorage.deletePicture(pic.getSrc());
        }

        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Article save(Article article, List<ArticlePicture> pictures) {
        List<ArticlePicture> savedPictures = articlePictureRepository.save(pictures);
        article.setPictures(savedPictures);
        Article savedArticle = articleRepository.save(article);

        String redisKey = RedisKeyUtils.articleIdKey(savedArticle.getId());
        Gson gson = new Gson();
        jCacheTools.addStringToJedis(redisKey,
                gson.toJson(savedArticle), Constants.ARTICLE_REDIS_EXPIRE);

        return savedArticle;
    }

    @Override
    public List<Article> findByTeacherId(Long teacherId) {
        String redisKey = RedisKeyUtils.teacherArticleIdsKey(teacherId);
        if (jCacheTools.existKey(redisKey)) {
            Gson gson = new Gson();
            List<String> articleIdStrs = jCacheTools.getListFromJedis(redisKey);
            List<Long> articleIds = new ArrayList<>(articleIdStrs.size());
            for (String articleIdStr : articleIdStrs) {
                articleIds.add(Long.parseLong(articleIdStr));
            }
            List<Article> articles = new ArrayList<>(articleIds.size());
            for (Long id : articleIds) {
                articles.add(findById(id));
            }

            return articles;
        } else {
            return articleRepository.findByTeacherId(teacherId);
        }
    }
}
