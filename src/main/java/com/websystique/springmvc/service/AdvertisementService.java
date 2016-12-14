package com.websystique.springmvc.service;

import com.websystique.springmvc.model.Advertisement;

import java.util.List;

/**
 * Created by kevin on 2016/11/24.
 */
public interface AdvertisementService {

    List<Advertisement> findAll();

    Advertisement findById(Long id);

    List<Advertisement> findByType(String type);

    List<Advertisement> findByAdvertiser(String advertiser);

    List<Advertisement> findByAdvertiserAndType(String advertiser, String type);

    Advertisement save(Advertisement ad);

    Boolean deleteById(Long adId);
}
