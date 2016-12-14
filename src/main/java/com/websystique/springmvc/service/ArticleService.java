package com.websystique.springmvc.service;

import com.websystique.springmvc.model.Article;
import com.websystique.springmvc.model.ArticlePicture;
import com.websystique.springmvc.model.Subject;

import java.util.List;

/**
 * Created by kevin on 2016/11/28.
 */
public interface ArticleService {

    List<Article> findBySchoolNameAndGradeAndSubject(String schoolName, Short grade, Subject subject);

    List<Article> findBySchoolNameAndGrade(String schoolName, Short grade);

    List<Article> findBySchoolNameAndSubject(String schoolName, Subject subject);

    List<Article> findBySchoolName(String schoolName);

    Article findById(Long id);

    Boolean deleteById(Long id);

    Article save(Article article, List<ArticlePicture> pictures);

    List<Article> findByTeacherId(Long teacherId);
}
