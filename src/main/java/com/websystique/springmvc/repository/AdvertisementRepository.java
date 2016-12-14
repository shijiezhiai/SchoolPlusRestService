package com.websystique.springmvc.repository;

import java.util.List;
import com.websystique.springmvc.model.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by yangyma on 11/23/16.
 */
@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

    List<Advertisement> findByType(String type);

    List<Advertisement> findByAdvertiser(String advertiser);

    List<Advertisement> findByAdvertiserAndType(String advertiser, String type);
}
