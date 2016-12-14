package com.websystique.springmvc.repository;

import com.websystique.springmvc.model.ArticlePicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by kevin on 2016/12/7.
 */
@Repository
public interface ArticlePictureRepository extends JpaRepository<ArticlePicture, Long> {
}
