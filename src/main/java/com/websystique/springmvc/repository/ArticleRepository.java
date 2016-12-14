package com.websystique.springmvc.repository;

import com.websystique.springmvc.model.Article;
import com.websystique.springmvc.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yangyma on 11/23/16.
 */
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("SELECT a FROM Article a WHERE " +
            "a.school.name = ?1 AND a.grade = ?2 AND a.subject = ?3")
    List<Article> findBySchoolNameAndGradeAndSubject(
            String schoolName, Integer grade, Subject subject);

    @Query("SELECT article FROM Article article WHERE " +
            "article.school.name = ?1 AND article.grade = ?2")
    List<Article> findBySchoolNameAndGrade(
            String schoolName, Integer grade);

    @Query("SELECT a FROM Article a WHERE " +
            "a.school.name = ?1 AND a.subject = ?2")
    List<Article> findBySchoolNameAndSubject(
            String schoolName, Subject subject);

    @Query("SELECT a FROM Article a WHERE a.school.name = ?1")
    List<Article> findBySchoolName(String schoolName);

    @Query("SELECT a FROM Article a WHERE a.author.id = ?1")
    List<Article> findByTeacherId(Long teacherId);
}
