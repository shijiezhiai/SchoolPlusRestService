package com.websystique.springmvc.repository;

import com.websystique.springmvc.model.AdvertisementPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by yangyma on 11/23/16.
 */
@Repository
public interface AdvertisementPictureRepository extends JpaRepository<AdvertisementPicture, Long> {
}
