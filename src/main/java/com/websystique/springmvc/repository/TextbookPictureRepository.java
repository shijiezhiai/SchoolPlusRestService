package com.websystique.springmvc.repository;

import com.websystique.springmvc.model.TextbookPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by yangyma on 11/23/16.
 */
@Repository
public interface TextbookPictureRepository extends JpaRepository<TextbookPicture, Long> {
}
